/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   01.06.2018 (thor): created
 */
package org.knime.core.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link PathUtils}.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 */
class PathUtilsTest {

    /**
     * Checks storing and restoring Unix permissions in zip files.
     *
     * @throws Exception if an error occurs
     */
    @Test
    void testZipWithPermissions() throws Exception {
        Assumptions.assumeFalse(SystemUtils.IS_OS_WINDOWS, "Only possible under Linux or macOS");

        final Path sourceDir = PathUtils.createTempDir(getClass().getSimpleName());
        final Path executable = sourceDir.resolve("script.sh");
        Files.createFile(executable);
        Files.setPosixFilePermissions(executable, PosixFilePermissions.fromString("rwxr-xr--"));

        final Path nonExecutable = sourceDir.resolve("sub").resolve("text.txt");
        Files.createDirectories(nonExecutable.getParent());
        Files.createFile(nonExecutable);
        Files.setPosixFilePermissions(nonExecutable, PosixFilePermissions.fromString("rw-rw----"));

        final Path zipFile = PathUtils.createTempFile(getClass().getSimpleName(), ".zip");
        try (ZipArchiveOutputStream zout = new ZipArchiveOutputStream(Files.newOutputStream(zipFile))) {
            PathUtils.zip(sourceDir, zout, false);
        }

        final Path destDir = unzipZF(zipFile);
        assertThat("Unexpected permission for executable file",
            PosixFilePermissions.toString(Files.getPosixFilePermissions(destDir.resolve("script.sh"))),
            is("rwxr-xr--"));
        assertThat("Unexpected permission for text file",
            PosixFilePermissions.toString(Files.getPosixFilePermissions(destDir.resolve("sub").resolve("text.txt"))),
            is("rw-rw----"));
    }

    /**
     * Checks that absolute paths in ZIP archives are turned into relative paths when unzipping. See AP-19699.
     *
     * @throws Exception if an error occurs
     */
    @Test
    void testZipSlipAbsolutePath() throws Exception {
        final Path zipFile = PathUtils.createTempFile(getClass().getSimpleName(), ".zip");
        try (ZipOutputStream zout = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            zout.putNextEntry(new ZipEntry("/tmp/foo.txt"));
            zout.write("Test".getBytes(StandardCharsets.UTF_8));
            zout.closeEntry();
        }

        Path expectedFile = unzipZIS(zipFile).resolve("tmp/foo.txt");
        assertThat("Zip slip not detected when unzipping stream", Files.exists(expectedFile), is(true));

        expectedFile = unzipZF(zipFile).resolve("tmp/foo.txt");
        assertThat("Zip slip not detected when unzipping file", Files.exists(expectedFile), is(true));
    }

    /**
     * Checks that directory traversals in ZIP archives are prevented. See AP-19699.
     *
     * @throws Exception if an error occurs
     */
    @Test
    void testZipSlipDirectoryTraversal() throws Exception {
        final Path zipFile = PathUtils.createTempFile(getClass().getSimpleName(), ".zip");
        try (ZipOutputStream zout = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            zout.putNextEntry(new ZipEntry("../foo.txt"));
            zout.write("Test".getBytes(StandardCharsets.UTF_8));
            zout.closeEntry();
        }

        assertThrows(IOException.class, () -> unzipZIS(zipFile), "Zip slip not detected when unzipping stream");
        assertThrows(IOException.class, () -> unzipZF(zipFile), "Zip slip not detected when unzipping file");
    }

    private Path unzipZF(final Path zipFile) throws IOException {
        final var destDir = PathUtils.createTempDir(getClass().getSimpleName());
        try (ZipFile zif = new ZipFile(zipFile.toFile())) {
            PathUtils.unzip(zif, destDir);
        }
        return destDir;
    }

    private Path unzipZIS(final Path zipFile) throws IOException {
        final var destDir = PathUtils.createTempDir(getClass().getSimpleName());
        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(zipFile))) {
            PathUtils.unzip(zin, destDir);
        }
        return destDir;
    }
}
