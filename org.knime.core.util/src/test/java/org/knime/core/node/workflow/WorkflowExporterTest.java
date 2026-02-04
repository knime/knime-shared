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
 *   4 Feb 2026 (leonard.woerteler): created
 */
package org.knime.core.node.workflow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.knime.core.util.workflowalizer.AbstractWorkflowalizerTest;

/**
 * Tests {@link WorkflowExporter}.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 */
class WorkflowExporterTest {

    private static final ZipShort DATA_MARKER = new ZipShort(WorkflowExporter.MARKER_HEADER_ID);

    private static final String WORKFLOW_NAME = "WorkflowWithData";

    private static Path testDataDir;

    private record EntryInfo(String name, long size, String hash) {}

    /** Workflow metadata, always exported and at the start of the KNWF. */
    private static final Set<EntryInfo> METADATA = Set.of( //
        new EntryInfo(".artifacts/workflow-configuration-representation.json", 3, "3/ihcx9ZzK0FazRhAtHh0BS4Q/M="),
        new EntryInfo(".artifacts/workflow-configuration.json", 3, "3/ihcx9ZzK0FazRhAtHh0BS4Q/M="),
        new EntryInfo("CSV Reader (#4)/settings.xml", 24037, "cXb1chnnF+r51eR2ymeEEUR2y+o="),
        new EntryInfo("Metanode (#7)/Excel Reader (#5)/settings.xml", 23962, "rlrgDr/1DKuLwN4fa7qEgqfTM/w="),
        new EntryInfo("Metanode (#7)/workflow.knime", 3544, "4l/+Iv4OwWBE6nW8Kr9I1ku6B8M="),
        new EntryInfo("workflow-metadata.xml", 255, "DOmF+3vpQgBNLZVK1ELK7mO8R/k="),
        new EntryInfo("workflow.knime", 3611, "lzIAvBF0/h83vA/qYG74EEEfzIM="),
        new EntryInfo("workflow.svg", 63473, "UxaykC980rVqrWhBvtqPxAtiW8E="),
        new EntryInfo("workflowset.meta", 450, "gvg1lPukBgwrtCZHmlKTa8Y4NHk="));

    /** User data, always exported but after the metadata. */
    private static final Set<EntryInfo> DATA_FOLDER = Set.of( //
        new EntryInfo("data/file.csv", 9124, "0gLKRVoOgaKeYUvBYH5GEWaRTBU="),
        new EntryInfo("data/file.xlsx", 9418, "Fmqypppz96jgoC3zcI7bqH1FVzg="));

    /** Execution data, not exported if "Exclude data" is selected and after the metadata otherwise. */
    private static final Set<EntryInfo> EXECUTION_DATA = Set.of( //
        new EntryInfo(".knimeLock", 0, "2jmj7l5rSw0yVb/vlWAYkK/YBwk="),
        new EntryInfo(".savedWithData", 219, "sNg0f54dOBOaRvrQPwLgNv6e8q0="),
        new EntryInfo("CSV Reader (#4)/internal/", 0, "2jmj7l5rSw0yVb/vlWAYkK/YBwk="),
        new EntryInfo("CSV Reader (#4)/port_1/data.xml", 757, "nTthaOK3cTKITlmME5w2E84pzyY="),
        new EntryInfo("CSV Reader (#4)/port_1/r0/data.xml", 1103, "wy5CNQK0bRxi8bvlbaI6TZ0Hwew="),
        new EntryInfo("CSV Reader (#4)/port_1/r0/r0/data.xml", 562, "Dz37Z2StA++NabhgAI/Cb/Gma+o="),
        new EntryInfo("CSV Reader (#4)/port_1/r0/r0/data.zip", 4966, "555RSsON/dIWsy47XhNUjx6QkyA="),
        new EntryInfo("CSV Reader (#4)/port_1/r0/r0/spec.xml", 7332, "cI/0W46s6+o31+cM/GldQWFw8MA="),
        new EntryInfo("CSV Reader (#4)/port_1/r0/spec.xml", 7332, "cI/0W46s6+o31+cM/GldQWFw8MA="),
        new EntryInfo("CSV Reader (#4)/port_1/spec.xml", 7332, "cI/0W46s6+o31+cM/GldQWFw8MA="),
        new EntryInfo("Metanode (#7)/.savedWithData", 219, "LQu5BftGFa1cWPo4bjOyQNnO+sg="),
        new EntryInfo("Metanode (#7)/Excel Reader (#5)/internal/", 0, "2jmj7l5rSw0yVb/vlWAYkK/YBwk="),
        new EntryInfo("Metanode (#7)/Excel Reader (#5)/port_1/data.xml", 562, "lX16k2pUtDByP+unb3P+YoIhuxE="),
        new EntryInfo("Metanode (#7)/Excel Reader (#5)/port_1/data.zip", 4987, "HHTSJdtSzI7Y9njqzdgI0bD2Jvk="),
        new EntryInfo("Metanode (#7)/Excel Reader (#5)/port_1/spec.xml", 7320, "6bc7zjDtbXU6zTfCQKebmmg+Fk8="));

    /**
     * Sets up the test by extracting the test workflow into a temporary directory.
     *
     * @throws Exception on errors
     */
    @BeforeAll
    static void setup() throws Exception {
        testDataDir = Files.createTempDirectory(WorkflowExporterTest.class.getSimpleName());
        try (final var testWorkflowIn = AbstractWorkflowalizerTest.getResourceAsStream("/" + WORKFLOW_NAME + ".knwf");
                final var zipIn = new ZipArchiveInputStream(testWorkflowIn)) {
            for (ZipArchiveEntry entry; (entry = zipIn.getNextEntry()) != null;) {
                final Path entryPath = testDataDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zipIn, entryPath);
                }
            }
        }
    }

    /**
     * Tests exporting a workflow with and without execution data.
     *
     * @param excludeData whether to exclude execution data
     * @throws Exception on errors
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false }) // excludeData
    void testExportWorkflow(final boolean excludeData) throws Exception {
        final var knwfBytes = exportKNWF(excludeData);

        final Set<EntryInfo> metadataEntries = new HashSet<>();
        final Set<EntryInfo> dataEntries = new HashSet<>();

        readKNWFEntries(metadataEntries, dataEntries, knwfBytes);


        final var nonMetadata = new HashSet<EntryInfo>(DATA_FOLDER);
        if (!excludeData) {
            nonMetadata.addAll(EXECUTION_DATA);
        }

        // check all entries
        Assertions.assertThat(metadataEntries).containsExactlyInAnyOrderElementsOf(METADATA);
        Assertions.assertThat(dataEntries).containsExactlyInAnyOrderElementsOf(nonMetadata);
    }

    /**
     * Exports the test workflow into a byte array.
     *
     * @param excludeData whether to exclude execution data
     * @return the exported KNWF as byte array
     * @throws Exception on errors
     */
    private static byte[] exportKNWF(final boolean excludeData) throws Exception {
        final WorkflowExporter<Exception> exporter = new WorkflowExporter<>(excludeData);
        final Iterable<Path> sourceWorkflows = List.of(testDataDir.resolve(WORKFLOW_NAME));
        final WorkflowExporter.ResourcesToCopy collectedResources =
            exporter.collectResourcesToCopy(sourceWorkflows, testDataDir);
        try (final var baos = new ByteArrayOutputStream()) {
            exporter.exportInto(collectedResources, baos, dbl -> {});
            return baos.toByteArray();
        }
    }

    /**
     * Reads the entries of a KNWF from a byte array into the given sets.
     *
     * @param metadata set to fill with metadata entries
     * @param data set to fill with data entries
     * @param knwf the KNWF as byte array
     * @throws Exception on errors
     */
    private static void readKNWFEntries(final Set<EntryInfo> metadata, final Set<EntryInfo> data, final byte[] knwf)
            throws Exception {
        // flag is flipped when the first data entry is encountered
        boolean isData = false;
        try (final var zipIn = new ZipArchiveInputStream(new ByteArrayInputStream(knwf))) {
            for (ZipArchiveEntry entry; (entry = zipIn.getNextEntry()) != null;) {
                final var firstDataEntry = isMarkedAsFirstDataEntry(entry);
                isData |= firstDataEntry;
                final var content = zipIn.readAllBytes();
                final var info = new EntryInfo(entry.getName().substring(WORKFLOW_NAME.length() + 1), content.length,
                    Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest(content)));
                (isData ? data : metadata).add(info);
            }
        }
    }

    /**
     * Checks whether the given entry is marked as the first data entry.
     *
     * @param entry the entry to check
     * @return {@code true} if it is marked as first data entry, {@code false} otherwise
     */
    private static boolean isMarkedAsFirstDataEntry(final ZipArchiveEntry entry) {
        final var dataMarker = entry.getExtraField(DATA_MARKER);
        return dataMarker != null && Arrays.equals(dataMarker.getLocalFileDataData(), new byte[] { 1 });
    }

    @AfterAll
    static void teardown() throws Exception {
        if (testDataDir != null) {
            FileUtils.deleteQuietly(testDataDir.toFile());
        }
    }
}
