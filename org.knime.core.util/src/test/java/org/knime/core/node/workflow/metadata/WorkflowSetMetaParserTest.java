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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.xmlbeans.XmlException;
import org.eclipse.jdt.annotation.Owning;
import org.junit.jupiter.api.Test;
import org.knime.core.node.workflow.metadata.WorkflowSetMetaParser.MetadataContents;
import org.knime.core.util.Pair;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Tests for the {@link WorkflowSetMetaParser}.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings("deprecation")
class WorkflowSetMetaParserTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.registerModule(new Jdk8Module());
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        MAPPER.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    }

    /**
     * Makes sure that {@code workflowset.meta} files from the test server roundtrip intact.
     *
     * @throws Exception
     */
    @Test
    void testRoundtripWorkflowTestMetadata() throws Exception {
        loadSaveLoad(Set.of(), "/workflowSetMeta/workflowset_meta.zip");
    }

    /**
     * Makes sure that {@code workflowset.meta} files from the test server are read correctly.
     *
     * @throws Exception
     */
    @Test
    void compareMetadataContents() throws Exception {
        final var original = readFileContents("/workflowSetMeta/workflowset_meta.zip", "/workflowset.meta");
        final var oldJson = readFileContents("/workflowSetMeta/workflowset_meta_json_old.zip", "/metadata.json");
        for (final Entry<String, String> e : original.entrySet()) {
            final var name = e.getKey();
            final var contents = e.getValue();
            if (contents.isEmpty()) {
                continue;
            }
            final var json = serializeToJson(WorkflowSetMetaParser.parse(
                new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8))));
            assertThat(json).as(name).isEqualTo(oldJson.get(name));
        }
    }

    /**
     * Makes sure that {@code workflowset.meta} files from the Examples workflows roundtrip intact.
     *
     * @throws Exception
     */
    @Test
    void testRoundtripExamplesMetadata() throws Exception {
        // all of these are also broken in Classic AP
        final Set<String> manuallyChecked = Set.of(
            "40_Partners/04_GEMMACON/01_Semi_Automated_ML/workflowset.meta",
            "40_Partners/05_Redfield/01_KNIME_meets_OrientDB/01_Create_ESCO_network_with_SQLite/workflowset.meta",
            "40_Partners/05_Redfield/01_KNIME_meets_OrientDB/02_Network_and_OrientDB/workflowset.meta");
        loadSaveLoad(manuallyChecked, "/workflowSetMeta/workflowset_meta_examples.zip");
    }

    /**
     * Makes sure that {@code workflowset.meta} files from the Examples workflows are read correctly.
     *
     * @throws Exception
     */
    @Test
    void compareMetadataContentsExamples() throws Exception {
        // all of these are also broken in Classic AP
        final var manuallyChecked = Set.of("40_Partners/01_Microsoft/06_Sentiment_with_Azure_for_publish");
        final var original = readFileContents("/workflowSetMeta/workflowset_meta_examples.zip", "/workflowset.meta");
        final var oldJson = readFileContents("/workflowSetMeta/workflowset_meta_examples_json_old.zip",
            "/metadata.json");
        for (final Entry<String, String> e : original.entrySet()) {
            final var name = e.getKey();
            final var contents = e.getValue();
            if (manuallyChecked.contains(name) || contents.isEmpty()) {
                continue;
            }
            String oldContent = oldJson.get(name)
                    .replace(
                        "\"url\" : \"https://www.knime.org/the-new-iris-data-modular-data-generators \"",
                        "\"url\" : \"https://www.knime.org/the-new-iris-data-modular-data-generators\"")
                    .replace(
                        "\"url\" : \"http://stat-computing.org/dataexpo/2009/the-data.html \"",
                        "\"url\" : \"http://stat-computing.org/dataexpo/2009/the-data.html\"");
            final var json = serializeToJson(WorkflowSetMetaParser.parse(
                new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8))));
            assertThat(json).as(name).isEqualTo(oldContent);
        }
    }

    private static @Owning InputStream getResourceAsStream(final String file) throws IOException {
        final var local = Path.of("src/test/resources" + file);
        final var fromClassLoader = WorkflowSetMetaParserTest.class.getResourceAsStream(file);
        return fromClassLoader != null ? fromClassLoader : Files.exists(local) ? Files.newInputStream(local) : null;
    }

    private static String serializeToJson(final MetadataContents contents) throws IOException {
        final var outStream = new ByteArrayOutputStream();
        MAPPER.writeValue(outStream, contents);
        return new String(outStream.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    private static Map<String, String> readFileContents(final String zipPath, final String fileName)
            throws IOException {
        final var out = new HashMap<String, String>();
        try (final var zipStream = new ZipInputStream(getResourceAsStream(zipPath));
                final var bufferedStream = new BufferedInputStream(zipStream)) {
            for (ZipEntry entry; (entry = zipStream.getNextEntry()) != null;) {
                final var name = entry.getName();
                if (!entry.isDirectory() && name.endsWith(fileName)) {
                    out.put(name.substring(0, name.length() - fileName.length()),
                        new String(bufferedStream.readAllBytes(), StandardCharsets.UTF_8).replace("\r\n", "\n"));
                }
            }
        }
        return out;
    }

    private static void loadSaveLoad(final Set<String> manuallyChecked, final String file)
            throws IOException, XmlException {
        try (final var zipStream = new ZipInputStream(getResourceAsStream(file));
                final var bufferedStream = new BufferedInputStream(zipStream)) {
            for (ZipEntry entry; (entry = zipStream.getNextEntry()) != null;) {
                if (!entry.isDirectory() && !manuallyChecked.contains(entry.getName())
                        && Path.of(entry.getName()).endsWith("workflowset.meta")) {

                    final var contents0 = new String(bufferedStream.readAllBytes(), StandardCharsets.UTF_8);
                    if (contents0.isEmpty()) {
                        continue;
                    }

                    final var res0 = loadSaveCycle(contents0);
                    final var parsed0 = serializeToJson(res0.getFirst());
                    final var contents1 = res0.getSecond();

                    final var res1 = loadSaveCycle(contents1);
                    final var parsed1 = serializeToJson(res1.getFirst());
                    final var contents2 = res1.getSecond();

                    final var res2 = loadSaveCycle(contents2);
                    final var contents3 = res2.getSecond();

                    assertThat(parsed1).as(entry.getName() + "\n" + contents0).isEqualTo(parsed0);
                    assertThat(contents2).as("Serialized files differ.").isEqualTo(contents1);
                    assertThat(contents3).as("Serialized files differ.").isEqualTo(contents2);
                }
            }
        }
    }

    private static final Pair<MetadataContents, String> loadSaveCycle(final String contents)
            throws XmlException, IOException {
        try (final var inStream = new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8))) {
            final MetadataContents workflowsetMeta = WorkflowSetMetaParser.parse(inStream);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            WorkflowSetMetaParser.write(workflowsetMeta, outStream);
            return Pair.create(workflowsetMeta,
                new String(outStream.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n"));
        }
    }
}
