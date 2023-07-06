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
 *   Jul 4, 2023 (leonard.woerteler): created
 */
package org.knime.core.node.workflow.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.Test;
import org.knime.core.node.workflow.metadata.WorkflowSetMetaParser.MetadataContents;
import org.knime.core.util.Pair;

class WorkflowSetMetaParserTest {

    @SuppressWarnings("resource")
    private static InputStream getResourceAsStream(final String file) throws URISyntaxException, IOException {
        final var local = Path.of("src/test/resources" + file);
        final var fromClassLoader = WorkflowSetMetaParserTest.class.getResourceAsStream(file);
        return fromClassLoader != null ? fromClassLoader : Files.exists(local) ? Files.newInputStream(local) : null;
    }

    @Test
    void testRoundtripWorkflowTestMetadata() throws IOException, URISyntaxException, XmlException {
        loadSaveLoad(Set.of(), "/workflowSetMeta/workflowset_meta.zip");
    }

    @Test
    void testRoundtripExamplesMetadata() throws IOException, URISyntaxException, XmlException {
        // all of these are also broken in Classic AP
        final Set<String> manuallyChecked = Set.of(
            "40_Partners/01_Microsoft/06_Sentiment_with_Azure_for_publish/workflowset.meta",
            "40_Partners/04_GEMMACON/01_Semi_Automated_ML/workflowset.meta",
            "40_Partners/05_Redfield/01_KNIME_meets_OrientDB/01_Create_ESCO_network_with_SQLite/workflowset.meta",
            "40_Partners/05_Redfield/01_KNIME_meets_OrientDB/02_Network_and_OrientDB/workflowset.meta");
        loadSaveLoad(manuallyChecked, "/workflowSetMeta/workflowset_meta_examples.zip");
    }

    private static void loadSaveLoad(final Set<String> manuallyChecked, final String file)
            throws IOException, XmlException, URISyntaxException {
        try (final var zipStream = new ZipInputStream(getResourceAsStream(file));
                final var bufferedStream = new BufferedInputStream(zipStream)) {
            for (ZipEntry entry; (entry = zipStream.getNextEntry()) != null;) {
                if (!entry.isDirectory() && !manuallyChecked.contains(entry.getName())
                        && Path.of(entry.getName()).endsWith("workflowset.meta")) {

                    final byte[] bytes0 = bufferedStream.readAllBytes();
                    if (bytes0.length == 0) {
                        continue;
                    }

                    final var res0 = loadSaveCycle(bytes0);
                    final MetadataContents contents0 = res0.getFirst();
                    final byte[] bytes1 = res0.getSecond();

                    final var res1 = loadSaveCycle(bytes1);
                    final MetadataContents contents1 = res1.getFirst();
                    final byte[] bytes2 = res1.getSecond();

                    final var res2 = loadSaveCycle(bytes2);
                    final byte[] bytes3 = res2.getSecond();

                    final String out1 = new String(bytes1, StandardCharsets.UTF_8);
                    final String out2 = new String(bytes2, StandardCharsets.UTF_8);
                    final String out3 = new String(bytes3, StandardCharsets.UTF_8);
                    assertEquals(contents0, contents1, "Parsed contents differ.");
                    assertEquals(out1, out2, "Serialized files differ.");
                    assertEquals(out2, out3, "Serialized files differ.");
                }
            }
        }
    }

    private static final Pair<MetadataContents, byte[]> loadSaveCycle(final byte[] contents)
            throws XmlException, IOException {
        try (final var inStream = new ByteArrayInputStream(contents)) {
            final MetadataContents workflowsetMeta = WorkflowSetMetaParser.parse(inStream);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            WorkflowSetMetaParser.write(workflowsetMeta, outStream);
            return Pair.create(workflowsetMeta, outStream.toByteArray());
        }
    }
}
