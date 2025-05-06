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
 *   May 31, 2024 (leonard.woerteler): created
 */
package org.knime.core.node.workflow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.function.FailableConsumer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.annotation.Owning;
import org.knime.core.node.util.CheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exporter saving one or more items (workflows, templates or data files) as one zipped file.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @param <E> exception type of the export progress updater
 * @since 6.5
 */
public final class WorkflowExporter<E extends Exception> {

    private static final String MODEL_PREFIX = "model_";

    private static final String LOCK_FILE = ".knimeLock";

    private static final String METAINFO_FILE = "workflowset.meta";

    private static final String SAVED_WITH_DATA_FILE = ".savedWithData";

    private static final String WORKFLOW_FILE = "workflow.knime";

    private static final String DROP_DIR_NAME = "drop";

    private static final String INTERN_FILE_DIR = "internal";

    private static final String FILESTORE_FOLDER_PREFIX = "filestore";

    private static final String INTERNAL_TABLE_FOLDER_PREFIX = "internalTables";

    private static final String PORT_FOLDER_PREFIX = "port_";

    /** Types of items to upload. */
    public enum ItemType {
        /** Workflow-like items, like Workflows, Components and Metanodes. */
        WORKFLOW_LIKE,
        /** Workflow groups (i.e., folders). */
        WORKFLOW_GROUP,
        /** Data files. */
        DATA_FILE
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowExporter.class);

    private final boolean m_excludeData;

    /**
     * Constructor.
     *
     * @param excludeData whether execution data should be excluded by the export
     */
    public WorkflowExporter(final boolean excludeData) {
        m_excludeData = excludeData;
    }

    /**
     * Scans the elements to export and collects all files and directories that will be archived. If execution data is
     * to be excluded, it will not be path of this method's result.
     *
     * @param elementsToExport top-level items to be exported
     * @param root root directory relative to which the destination path of the resources is determined
     * @return record of items to be copied
     * @throws IOException if files or folders could not be accessed
     */
    public ResourcesToCopy collectResourcesToCopy(final Iterable<Path> elementsToExport, final Path root)
            throws IOException {
        final Map<Path, IPath> resourceList = new LinkedHashMap<>();
        final long[] counts = { 0, 0 };
        for (Path element : elementsToExport) {
            // add all files within the workflow or group
            addResourcesFor(resourceList, root, counts, element);
        }
        return new ResourcesToCopy(resourceList, (int)counts[0], counts[1]);
    }

    /**
     * Determines whether or not the given ZIP file (assumed to be a {@code .knwf} file) has a single top-level
     * folder containing the item's contents.
     *
     * @param zipFile the ZIP file's path
     * @return {@code true} if there is a single top-level folder, {@code false} otherwise
     * @throws IOException I/O-related exception
     */
    public static boolean hasZipSingleRootFolder(final Path zipFile) throws IOException {
        String root = null;
        try (final var zip = ZipFile.builder().setPath(zipFile).get()) {
            for (final var entries = zip.getEntries(); entries.hasMoreElements();) {
                final var entry = entries.nextElement();
                final var entryPath = org.eclipse.core.runtime.Path.forPosix(entry.getName());
                if (entryPath.segmentCount() == 0) {
                    // entry for the root folder, skip
                    continue;
                }

                if (entryPath.segmentCount() == 1 && !entry.isDirectory()) {
                    // top-level file
                    return false;
                }

                final var newRoot = entryPath.segment(0);
                if (root != null && !root.equals(newRoot)) {
                    // found a second top-level folder
                    return false;
                }
                root = newRoot;
            }
        }
        return root != null;
    }

    /**
     * Implements the exclude policy. Called only if "exclude data" is checked.
     *
     * @param store the resource to check
     * @return true if the given resource should be excluded, false if it should be included
     */
    private static boolean excludeResource(final Path store) {
        final var name = store.getFileName().toString();
        if (name.equals(INTERN_FILE_DIR)) {
            return true;
        }

        final Stream<String> excludedPrefixes;
        if (Files.isDirectory(store)) {
            // directories to exclude:
            excludedPrefixes = Stream.of(PORT_FOLDER_PREFIX,
                INTERNAL_TABLE_FOLDER_PREFIX, FILESTORE_FOLDER_PREFIX,
                INTERN_FILE_DIR, DROP_DIR_NAME);
        } else {
            // files to exclude:
            if (name.equals("data.xml")) {
                return true;
            }
            excludedPrefixes = Stream.of(MODEL_PREFIX, SAVED_WITH_DATA_FILE, LOCK_FILE);
        }

        return excludedPrefixes.anyMatch(name::startsWith);
    }

    /**
     * Collects the files (files only) that are contained in the passed workflow or workflow group and are that are not
     * excluded. For workflows it does include all files contained in sub dirs (unless excluded).
     *
     * @param resources result mapping from local resources to export to target path
     * @param counts
     * @param element the resource representing the thing to export
     * @param excludeData true if KNIME data files should be excluded
     * @throws IOException
     */
    private void addResourcesFor(final Map<Path, IPath> resources, final Path root, final long[] counts,
            final Path element) throws IOException {
        if (!Files.isDirectory(element)) {
            addResource(resources, root, counts, element);
        } else if (Files.isRegularFile(element.resolve(WORKFLOW_FILE))) {
            // workflows, components and metanodes: add with optionally excluded data
            addRecursively(resources, root, counts, element);
        } else {
            // workflow groups: only add `workflowset.meta` if present
            final var metadata = element.resolve(METAINFO_FILE);
            if (Files.isRegularFile(metadata)) {
                addResource(resources, root, counts, metadata);
            }
        }
    }

    private void addRecursively(final Map<Path, IPath> resources, final Path root, final long[] counts, final Path dir)
            throws IOException {
        try (final var contents = Files.list(dir)) {
            final Path[] children = contents.toArray(Path[]::new);
            if (children.length == 0) {
                // see AP-13538 (empty dirs are ignored -- so we add them)
                addResource(resources, root, counts, dir);
            }
            for (final var child : children) {
                if (!isMetaNode(child, dir) && m_excludeData && excludeResource(child)) {
                    continue;
                }
                if (Files.isDirectory(child)) {
                    addRecursively(resources, root, counts, child);
                } else if (Files.isRegularFile(child)) {
                    addResource(resources, root, counts, child);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Skipping unexpected item '{}' (neither file nor directory)", child);
                }
            }
        }
    }

    private static void addResource(final Map<Path, IPath> resources, final Path root, final long[] counts,
            final Path child) throws IOException {
        CheckUtils.checkArgument(root == null || child.startsWith(root), //
                "File '%s' is not below the root '%s'", child, root);
        IPath path = org.eclipse.core.runtime.Path.EMPTY;
        for (final var segment : (root == null ? child : root.relativize(child).normalize())) {
            path = path.append(segment.toString());
        }
        if (Files.isDirectory(child)) {
            resources.put(child, path.addTrailingSeparator());
        } else {
            resources.put(child, path);
            counts[0]++;
            counts[1] += Files.size(child);
        }
    }

    /**
     * @param file to check
     * @return true if this is a metanode (or a sub node) in a workflow (or metanode in another metanode, etc.)
     */
    private static boolean isMetaNode(final Path file, final Path parent) {
        return Files.isRegularFile(parent.resolve(WORKFLOW_FILE)) && Files.isDirectory(file)
                && Files.isRegularFile(file.resolve(WORKFLOW_FILE));
    }

    /**
     * Record of all resources to be copied into an archive.
     *
     * @param paths mapping from paths of all resources to copy to their respective path inside the archive
     * @param numFiles number of files (as opposed to directories) to copy
     * @param numBytes number of bytes of all files combined
     */
    public record ResourcesToCopy(Map<Path, IPath> paths, int numFiles, long numBytes) { }

    /**
     * Exports the resources into the given output stream.
     *
     * @param resources resources to copy into export archive
     * @param outputStream stream to write the export archive to, will be closed in the end
     * @param updater progress updater
     * @throws E if execution was cancelled
     * @throws IOException if something went wrong with the export
     */
    public void exportInto(final ResourcesToCopy resources, final OutputStream outputStream,
            final FailableConsumer<Double, E> updater)
            throws E, IOException {
        try (final var zipper = new Zipper(outputStream)) {
            final var numBytesWritten = new AtomicLong();
            final FailableConsumer<Long, E> subUpdater =
                    add -> updater.accept(1.0 * numBytesWritten.addAndGet(add) / resources.numBytes());
            for (final var file : resources.paths().entrySet()) {
                zipper.addEntry(file.getKey(), file.getValue(), subUpdater);
            }
        }
    }

    private final class Zipper implements Closeable {

        private static final int BUFFER_SIZE = 2 * (int)FileUtils.ONE_MB;

        private final byte[] m_buffer = new byte[BUFFER_SIZE];

        private @Owning ZipOutputStream m_zipOutStream;

        Zipper(final OutputStream outputStream) {
            m_zipOutStream = new ZipOutputStream(new BufferedOutputStream(outputStream, BUFFER_SIZE));
        }

        void addEntry(final Path source, final IPath destination, final FailableConsumer<Long, E> updater)
                throws IOException, E {
            m_zipOutStream.setLevel(Deflater.BEST_COMPRESSION);
            if (Files.isDirectory(source)) {
                // mostly for empty directories (but non-empty dirs are accepted also)
                m_zipOutStream.putNextEntry(new ZipEntry(destination.addTrailingSeparator().toString()));
                m_zipOutStream.closeEntry();
            } else {
                final var entry = new ZipEntry(destination.toString());
                final var size = Files.size(source);
                if (size == 0) {
                    // this is mainly for the .knimeLock file of open workflows; the file is locked and windows forbids
                    // mmap-ing locked files but FileInputStream seems to mmap files which leads to exceptions while
                    // reading the (non-existing) contents of the file
                    m_zipOutStream.putNextEntry(entry);
                    m_zipOutStream.closeEntry();
                    return;
                }

                try (final var inStream = new BufferedInputStream(Files.newInputStream(source), BUFFER_SIZE)) {
                    if (size > FileUtils.ONE_KB && isAlreadyCompressed(inStream)) {
                        m_zipOutStream.setLevel(Deflater.NO_COMPRESSION);
                    }

                    m_zipOutStream.putNextEntry(entry);
                    for (int read; (read = inStream.read(m_buffer)) >= 0;) {
                        m_zipOutStream.write(m_buffer, 0, read);
                        updater.accept((long)read);
                    }
                } catch (final IOException ioe) {
                    throw new IOException(String.format("Unable to add file \"%s\" to archive: %s",
                        source.toAbsolutePath(), ioe.getMessage()), ioe);
                } finally {
                    m_zipOutStream.closeEntry();
                }
            }
        }

        private static final byte[][] COMPRESSED_DATA_MAGIC_BYTES = { //
            { 'P', 'K', 0x03, 0x04 }, // ZIP
            { 'A', 'R', 'R', 'O', 'W', '1' } // ARROW
        };

        private static boolean isAlreadyCompressed(final BufferedInputStream inStream) throws IOException {
            inStream.mark(6);
            final var bytes = inStream.readNBytes(6);
            inStream.reset();
            return Arrays.stream(COMPRESSED_DATA_MAGIC_BYTES) //
                    .anyMatch(mb -> bytes.length >= mb.length && Arrays.equals(bytes, 0, mb.length, mb, 0, mb.length));
        }

        @Override
        public void close() throws IOException {
            try (final var zipOut = m_zipOutStream) {
                m_zipOutStream = null;
            }
        }
    }
}
