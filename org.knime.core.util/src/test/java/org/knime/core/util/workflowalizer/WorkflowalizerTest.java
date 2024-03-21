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
 *   Sep 19, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.compress.utils.IOUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.knime.core.util.PathUtils;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;
import org.knime.core.util.workflowalizer.RepositoryItemMetadata.ContentType;
import org.knime.core.util.workflowalizer.RepositoryItemMetadata.RepositoryItemType;
import org.xml.sax.SAXParseException;

/**
 * Tests for {@link Workflowalizer}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public class WorkflowalizerTest extends AbstractWorkflowalizerTest {

    private static Path workspaceDir;
    private static Path workflowDir;
    private static Path workflowDirWithAuthorNull;
    private static Path nodeDir;
    private static Path templateDir;
    private static Path templateDirWithTimezone;
    private static Path workflowGroupFile;
    private static Path workflowGroupFileLongTitle;
    private static Path componentTemplateDir;

    private static List<String> readWorkflowLines;
    private static List<String> readWorkflowLinesWithAuthorNull;
    private static List<String> readNodeLines;
    private static List<String> readTemplateWorkflowKnime;
    private static List<String> readTemplateTemplateKnime;
    private static List<String> readTemplateWorkflowKnimeWithTimezone;
    private static List<String> readTemplateTemplateKnimeWithTimezone;
    private static List<String> readWorkflowSetLines;
    private static List<String> readWorkflowGroupLines;
    private static List<String> readWorkflowGroupLinesLongTitle;
    private static List<String> readComponentTemplateWorkflowKnime;
    private static List<String> readComponentTemplateTemplateKnime;

    /**
     * Readers zip archive, and creates temporary directory for workflow files.
     *
     * @throws Exception
     */
    @BeforeAll
    public static void setup() throws Exception {
        workspaceDir = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (InputStream is = getResourceAsStream("/workflowalizer-test.zip")) {
            unzip(is, workspaceDir);
        }
        workflowDir = new File(workspaceDir.toFile(), "workflowalizer-test/Testing_Workflowalizer_360Pre").toPath();
        workflowDirWithAuthorNull = new File(workspaceDir.toFile(), "workflowalizer-test/Testing_author_null").toPath();
        readWorkflowLines = Files.readAllLines(new File(workflowDir.toFile(), "workflow.knime").toPath());
        readWorkflowLinesWithAuthorNull =
            Files.readAllLines(new File(workflowDirWithAuthorNull.toFile(), "workflow.knime").toPath());

        nodeDir = new File(workflowDir.toFile(), "Column Splitter (#10)").toPath();
        readNodeLines = Files.readAllLines(new File(nodeDir.toFile(), "settings.xml").toPath());

        templateDir = new File(workspaceDir.toFile(), "workflowalizer-test/Hierarchical Cluster Assignment").toPath();
        readTemplateWorkflowKnime =
            Files.readAllLines(Paths.get(templateDir.toAbsolutePath().toString(), "workflow.knime"));
        readTemplateTemplateKnime =
            Files.readAllLines(Paths.get(templateDir.toAbsolutePath().toString(), "template.knime"));

        templateDirWithTimezone =
            workspaceDir.resolve("workflowalizer-test/Hierarchical Cluster Assignment with timezone");
        readTemplateWorkflowKnimeWithTimezone = Files.readAllLines(templateDirWithTimezone.resolve("workflow.knime"));
        readTemplateTemplateKnimeWithTimezone = Files.readAllLines(templateDirWithTimezone.resolve("template.knime"));

        componentTemplateDir = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = getResourceAsStream("/component-template-simple.zip")) {
            unzip(is, componentTemplateDir);
        }
        componentTemplateDir = componentTemplateDir.resolve("Simple-Component");
        readComponentTemplateWorkflowKnime = Files.readAllLines(componentTemplateDir.resolve("workflow.knime"));
        readComponentTemplateTemplateKnime = Files.readAllLines(componentTemplateDir.resolve("template.knime"));

        final Path workflowSetMetaPath = workflowDir.resolve("workflowset.meta");
        readWorkflowSetLines = Files.readAllLines(workflowSetMetaPath);

        workflowGroupFile = workspaceDir.resolve("workflowalizer-test/test_group/workflowset.meta");
        workflowGroupFileLongTitle = workspaceDir.resolve("workflowalizer-test/test_group/workflowset_long_title.meta");

        readWorkflowGroupLines = Files.readAllLines(workflowGroupFile);
        readWorkflowGroupLinesLongTitle = Files.readAllLines(workflowGroupFileLongTitle);
    }

    // -- Test reading workflow --

    /**
     * Tests the structure of the {@link IWorkflowMetadata} (i.e. checks that the expected number of nodes are present
     * for each 'workflow').
     *
     * @throws Exception
     */
    @Test
    void testStructure() throws Exception {
        final IWorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        final Path workflowPath = new File(workflowDir.toFile(), "workflow.knime").toPath();

        testStructure(wkfMd, workflowPath);
    }

    /**
     * Reads a top-level (i.e. not metanode) workflow, and tests that all fields were set and read correctly.
     *
     * @throws Exception
     */
    @Test
    void testTopLevelWorkflowMetadata() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        assertThat(wkfMd.getType()).isEqualTo(RepositoryItemType.WORKFLOW);

        testAnnotations(readWorkflowLines, wkfMd);
        testAuthorInformation(readWorkflowLines, wkfMd);
        testConnections(readWorkflowLines, wkfMd);
        testCreatedBy(readWorkflowLines, wkfMd);
        testCustomDescription(readWorkflowLines, wkfMd);
        testWorkflowName(workflowDir, wkfMd);
        testNodeIds(readWorkflowLines, wkfMd, null);
        testVersion(readWorkflowLines, wkfMd);
        testSvg(1301, 501, wkfMd);

        assertThat(wkfMd.getUnexpectedFileNames().isEmpty()).isTrue();
        assertThat(wkfMd.getWorkflowSetMetadata().isPresent()).isTrue();
        testWorkflowSetMetaSimple(readWorkflowSetLines, wkfMd.getWorkflowSetMetadata().get());

        assertThat(wkfMd.getArtifacts().get().containsAll(List.of(
                workflowDir.relativize(new File(workflowDir.toFile(), ".artifacts/openapi-input-parameters.json").toPath())
                        .toString(),
                workflowDir.relativize(new File(workflowDir.toFile(), ".artifacts/openapi-input-resources.json").toPath())
                        .toString(),
                workflowDir.relativize(new File(workflowDir.toFile(), ".artifacts/openapi-output-parameters.json").toPath())
                        .toString(),
                workflowDir.relativize(new File(workflowDir.toFile(), ".artifacts/openapi-output-resources.json").toPath())
                        .toString(),
                workflowDir.relativize(new File(workflowDir.toFile(), ".artifacts/workflow-configuration.json").toPath())
                        .toString(),
                workflowDir
                        .relativize(
                                new File(workflowDir.toFile(), ".artifacts/workflow-configuration-representation.json").toPath())
                        .toString()))).as("Expected artifacts file").isTrue();

        assertThat(wkfMd.getWorkflowConfiguration().get()).as("Unexpected workflow configuration").isEqualTo(WorkflowalizerArtifactContent.WORKFLOW_CONFIGURATION.value());
        assertThat(wkfMd.getWorkflowConfigurationRepresentation().get()).as("Unexpected workflow configuration representation").isEqualTo(WorkflowalizerArtifactContent.WORKFLOW_CONFIGURATION_REPRESENTATION.value());
        assertThat(wkfMd.getOpenapiInputParameters().get()).as("Unexpected openapi input parameters").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_INPUT_PARAMETERS.value());
        assertThat(wkfMd.getOpenapiInputResources().get()).as("Unexpected openapi input resources").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_INPUT_RESOURCES.value());
        assertThat(wkfMd.getOpenapiOutputParameters().get()).as("Unexpected openapi output parameters").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_OUTPUT_PARAMETERS.value());
        assertThat(wkfMd.getOpenapiOutputResources().get()).as("Unexpected openapi output resources").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_OUTPUT_RESOURCES.value());
    }

    /**
     * Tests that reading the workflow configuration fields without setting them in the
     * {@link WorkflowalizerConfiguration} results in a {@link UnsupportedOperationException}
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReadingWorkflowConfigurations() throws Exception {
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir,
            WorkflowalizerConfiguration.builder().readNodeConfiguration().build());
        assertUOEThrown(wm::getWorkflowConfiguration);
        assertUOEThrown(wm::getWorkflowConfigurationRepresentation);
    }

    /**
     * Tests that reading the openapi fields without setting them in the {@link WorkflowalizerConfiguration} results in
     * a {@link UnsupportedOperationException}
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReadingOpenapi() throws Exception {
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir,
            WorkflowalizerConfiguration.builder().readNodeConfiguration().build());
        assertUOEThrown(wm::getOpenapiInputParameters);
        assertUOEThrown(wm::getOpenapiInputResources);
        assertUOEThrown(wm::getOpenapiOutputParameters);
        assertUOEThrown(wm::getOpenapiOutputResources);
    }

    /**
     * Tests that reading the hub event input fields without setting them in the
     * {@link WorkflowalizerConfiguration} results in a {@link UnsupportedOperationException}
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReadingHubEvents() throws Exception {
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir,
            WorkflowalizerConfiguration.builder().readNodeConfiguration().build());
        assertUOEThrown(wm::getHubEventInputParameters);
    }

    /**
     * Tests that reading the secret store fields without setting them in the
     * {@link WorkflowalizerConfiguration} results in a {@link UnsupportedOperationException}
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReadingSecretStoreEvents() throws Exception {
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir,
            WorkflowalizerConfiguration.builder().readNodeConfiguration().build());
        assertUOEThrown(wm::getSecretStoreSecretIds);
    }

    // -- Test reading individual workflow fields --

    /**
     * Tests reading workflow SVG file path.
     *
     * @throws Exception
     */
    @Test
    void testReadingSVG() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testSvg(1301, 501, wkfMd);

        final Path svg = workflowDir.resolve("workflow.svg");
        try (final InputStream readSvg = wkfMd.getSvgInputStream().orElse(null);
                final InputStream referenceSvg = Files.newInputStream(svg)) {
            assertThat(readSvg).isNotNull();
            assertThat(Arrays.equals(IOUtils.toByteArray(referenceSvg), IOUtils.toByteArray(readSvg))).isTrue();
        }

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that the file paths in the .artifacts directory are read and stored.
     *
     * @throws Exception
     */
    @Test
    void testReadingArtifactsDirectory() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final File test = new File(workflowDir.toFile(), ".artifacts/openapi-input-parameters.json");
        assertThat(wkfMd.getArtifacts().isPresent()).isTrue();
        assertThat(wkfMd.getArtifacts().get().size()).as("Unexpected artifacts size").isEqualTo(8);
        assertThat(wkfMd.getArtifacts().get().contains(workflowDir.relativize(test.toPath()).toString())).as("Expected artifacts file").isTrue();

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that unexpected file paths are stored.
     *
     * @throws Exception
     */
    @Test
    void testReadingUnexpectedFilePaths() throws Exception {
        File dataDir = null;
        File dataFile = null;
        File subDataDir = null;
        File subDataFile = null;
        File emptyDir = null;

        try {
            // Add some unexpected files
            dataDir = new File(workflowDir.toFile(), "data");
            dataDir.mkdirs();
            dataFile = new File(dataDir, "myData.txt");
            dataFile.createNewFile();
            subDataDir = new File(dataDir, "subData");
            subDataDir.mkdirs();
            subDataFile = new File(subDataDir, "moreData.txt");
            subDataFile.createNewFile();
            emptyDir = new File(workflowDir.toFile(), "tmp");
            emptyDir.mkdirs();

            final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readUnexpectedFiles().build();
            final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
            final Collection<String> unexpectedFiles = wkfMd.getUnexpectedFileNames();
            assertThat(unexpectedFiles.size()).isEqualTo(2);
            assertThat(unexpectedFiles.contains(workflowDir.relativize(dataFile.toPath()).toString())).isTrue();
            assertThat(unexpectedFiles.contains(workflowDir.relativize(subDataFile.toPath()).toString())).isTrue();

            assertUOEThrown(wkfMd::getConnections);
            assertUOEThrown(wkfMd::getNodes);
            assertUOEThrown(wkfMd::getWorkflowSetMetadata);
        } finally {
            // remove the unexpected files
            if (emptyDir != null) {
                emptyDir.delete();
            }
            if (subDataFile != null) {
                subDataFile.delete();
            }
            if (subDataDir != null) {
                subDataDir.delete();
            }
            if (dataFile != null) {
                dataFile.delete();
            }
            if (dataDir != null) {
                dataDir.delete();
            }
        }
    }

    /**
     * Tests that workflow annotations are read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingAnnotations() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testAnnotations(readWorkflowLines, wkfMd);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that {@link AuthorInformation} is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingAuthorInformation() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testAuthorInformation(readWorkflowLines, wkfMd);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that {@link AuthorInformation} is read correctly if author is null.
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowWithAuthorNullFromZip() throws Exception {
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDirWithAuthorNull);

        testAuthorInformation(readWorkflowLinesWithAuthorNull, wm);
    }

    /**
     * Tests that node connections are read correctly. This only checks that the IDs in the xml file match those of the
     * {@link NodeConnection}s.
     *
     * @throws Exception
     */
    @Test
    void testReadingNodesAndConnections() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodesAndConnections().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testConnections(readWorkflowLines, wkfMd);

        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that "createdBy" is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingCreatedBy() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testCreatedBy(readWorkflowLines, wkfMd);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that customDescription is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingCustomDescription() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testCustomDescription(readWorkflowLines, wkfMd);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that the name is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingName() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testWorkflowName(workflowDir, wkfMd);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that all nodes are read, and assigned the proper ids. This does not verbosely test that the
     * {@link SingleNodeMetadata} is correct.
     *
     * @throws Exception
     */
    @Test
    void testReadingNodes() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testNodeIds(readWorkflowLines, wkfMd, null);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that the workflow version is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingVersion() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testVersion(readWorkflowLines, wkfMd);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Test reading workflowset.meta for a workflow
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowWorkflowSetMeta() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readWorkflowMeta().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final WorkflowSetMeta workflowSetMetadata = wkfMd.getWorkflowSetMetadata().orElseThrow();
        assertThat(workflowSetMetadata.getContentType()).isEqualTo(ContentType.TEXT_PLAIN);
        assertThat(workflowSetMetadata.getTitle().isEmpty()).isTrue();
        assertThat(workflowSetMetadata.getCreated().get().getYear()).isEqualTo(2018);
        assertThat(workflowSetMetadata.getLastModified().isEmpty()).isTrue();
        assertThat(workflowSetMetadata.getLinks().get().isEmpty()).isTrue();
        assertThat(workflowSetMetadata.getTags().get().isEmpty()).isTrue();
        testWorkflowSetMetaSimple(readWorkflowSetLines, workflowSetMetadata);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
    }

    /**
     * Test reading workflow configuration files for a workflow
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowConfigurationFiles() throws Exception {
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readWorkflowConfigurationFiles().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertThat(wkfMd.getWorkflowConfiguration().isPresent()).isTrue();
        assertThat(wkfMd.getWorkflowConfigurationRepresentation().isPresent()).isTrue();

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Test reading openapi files for a workflow
     *
     * @throws Exception
     */
    @Test
    void testReadingOpenapiFiles() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readOpenapiFiles().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertThat(wkfMd.getOpenapiInputParameters().isPresent()).as("Expected openapi input parameters file is present").isTrue();
        assertThat(wkfMd.getOpenapiInputResources().isPresent()).as("Expected openapi input resources file is present").isTrue();
        assertThat(wkfMd.getOpenapiOutputParameters().isPresent()).as("Expected openapi output parameters file is present").isTrue();
        assertThat(wkfMd.getOpenapiOutputResources().isPresent()).as("Expected openapi output resources file is present").isTrue();

        assertThat(wkfMd.getOpenapiInputParameters().get()).as("Unexpected openapi input parameters").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_INPUT_PARAMETERS.value());
        assertThat(wkfMd.getOpenapiInputResources().get()).as("Unexpected openapi input resources").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_INPUT_RESOURCES.value());
        assertThat(wkfMd.getOpenapiOutputParameters().get()).as("Unexpected openapi output parameters").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_OUTPUT_PARAMETERS.value());
        assertThat(wkfMd.getOpenapiOutputResources().get()).as("Unexpected openapi output resources").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_OUTPUT_RESOURCES.value());

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }


    /**
     * Test reading hub event files for a workflow
     *
     * @throws Exception
     */
    @Test
    void testReadingHubEventFiles() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readHubEventFiles().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertThat(wkfMd.getHubEventInputParameters().isPresent()).as("Expected hub event input parameters file is present").isTrue();

        assertThat(wkfMd.getHubEventInputParameters().get()).as("Unexpected hub event input parameters").isEqualTo(WorkflowalizerArtifactContent.HUB_EVENT_INPUT_RESOURCES.value());

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Test reading secret store files for a workflow
     *
     * @throws Exception
     */
    @Test
    void testReadingSecretStoreFiles() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readSecretStoreFiles().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertThat(wkfMd.getSecretStoreSecretIds().isPresent()).as("Expected secret store parameters file is present").isTrue();

        final var secretIds = Set.of(WorkflowalizerArtifactContent.SECRET_STORE_RESOURCES_SECRET_ID.value());
        assertThat(wkfMd.getSecretStoreSecretIds().get()).as("Unexpected secret store parameters").isEqualTo(secretIds);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Test reading workflow credentials
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowCredentials() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final List<String> cred = wkfMd.getWorkflowCredentialsNames();
        assertThat(cred.size()).isEqualTo(1);
        assertThat(cred.get(0)).isEqualTo("Credential");

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Test reading workflow variables
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowVariables() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final List<String> vars = wkfMd.getWorkflowVariables();
        assertThat(vars.size()).isEqualTo(3);
        assertThat(vars.contains("TestDouble")).isTrue();
        assertThat(vars.contains("TestInteger")).isTrue();
        assertThat(vars.contains("TestString")).isTrue();

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Test reading if a workflow has a report
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    void testReadingHasReport() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertThat(wkfMd.hasReport()).isFalse();

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests attempting to read a workflowset.meta file, for a workflow which doesn't have such a file
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowSetNotPresent() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readWorkflowMeta().build();
        final Path workflowPath = workspaceDir.resolve("workflowalizer-test/test_group/test1");
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowPath, wc);
        assertThat(wkfMd.getWorkflowSetMetadata().isPresent()).isFalse();

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
    }

    // -- Test reading NativeNode --

    /**
     * Tests that a node is read correctly as part of reading a workflow.
     *
     * @throws Exception
     */
    @Test
    void testReadingNodeInWorkflow() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertThat(node instanceof NativeNodeMetadata).isTrue();
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;
        assertThat(node.getType()).isEqualTo(NodeType.NATIVE_NODE);

        testAnnotationText(readNodeLines, nativeNode);
        testCustomNodeDescription(readNodeLines, nativeNode);
        testNodeConfiguration(nodeDir, nativeNode);
        testNodeAndBundleInformation(readNodeLines, nativeNode);
    }

    /**
     * Tests the a node within a metanode is read correctly when the top-level workflow is read.
     *
     * @throws Exception
     */
    @Test
    void testReadingNodeInMetanode() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        final MetanodeMetadata mtnMd = (MetanodeMetadata)wkfMd.getNodes().stream()
            .filter(n -> n.getType().equals(NodeType.METANODE)).findFirst().get();
        final NativeNodeMetadata node = (NativeNodeMetadata)mtnMd.getNodes().stream()
            .filter(n -> n.getNodeId().equals(mtnMd.getNodeId() + ":2")).findFirst().get();
        final Path nodePath =
            new File(workflowDir.toFile(), "Hierarchical (#15)/Numeric Distances (#2)/settings.xml").toPath();
        final List<String> readLines = Files.readAllLines(nodePath);

        // No need to test ID, since the list was filtered on that
        testAnnotationText(readLines, node);
        testCustomNodeDescription(readLines, node);
        testNodeConfiguration(nodePath.getParent(), node);
        testNodeAndBundleInformation(readLines, node);
        assertThat(node.getType()).isEqualTo(NodeType.NATIVE_NODE);
    }

    // -- Test reading individual node fields --

    /**
     * Test that node annotation text is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingAnnotationText() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertThat(node instanceof NativeNodeMetadata).isTrue();
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        testAnnotationText(readNodeLines, nativeNode);
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    /**
     * Test that the node's custom description is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingCustomNodeDescription() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertThat(node instanceof NativeNodeMetadata).isTrue();
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        testCustomNodeDescription(readNodeLines, nativeNode);
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    /**
     * Test that the node's configuration are read correctly.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    void testReadingNodeConfiguration() throws Exception {
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readNodes().readNodeConfiguration().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertThat(node instanceof NativeNodeMetadata).isTrue();
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;
        testNodeConfiguration(nodeDir, nativeNode);
    }

    /**
     * Test that the node and bundle information is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingNodeAndBundleInformation() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertThat(node instanceof NativeNodeMetadata).isTrue();
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        testNodeAndBundleInformation(readNodeLines, nativeNode);
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    /**
     * Test that the node ID is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingNodeId() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);

        // Filter on something that isn't ID, so the ID check actual tests something
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> (n instanceof NativeNodeMetadata)
            && ((NativeNodeMetadata)n).getNodeAndBundleInformation().getNodeName().isPresent()
            && ((NativeNodeMetadata)n).getNodeAndBundleInformation().getNodeName().get().contains("Column Splitter"))
            .findFirst().get();

        assertThat(node instanceof NativeNodeMetadata).isTrue();
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertThat(nativeNode.getNodeId()).isEqualTo("10");
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    /**
     * Test that the node type is read correctly.
     *
     * @throws Exception
     */
    @Test
    void testReadingType() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertThat(node instanceof NativeNodeMetadata).isTrue();
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertThat(nativeNode.getType()).isEqualTo(NodeType.NATIVE_NODE);
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    // -- Test reading (Wrapped)MetaNodes

    /**
     * Test that the MetaNode was read correctly (both WorkflowMetadata and NodeMetadata).
     *
     * @throws Exception
     */
    @Test
    void testReadingMetaNode() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        final MetanodeMetadata mtnMtd = (MetanodeMetadata)wkfMd.getNodes().stream()
            .filter(n -> n.getType().equals(NodeType.METANODE)).findFirst().get();
        final List<String> readMetaNodeWorkflowLines = Files.readAllLines(
            new File(workflowDir.toFile(), "Hierarchical (#15)/workflow.knime").toPath(), StandardCharsets.UTF_8);

        // Read workflow fields
        testAnnotations(readMetaNodeWorkflowLines, mtnMtd);
        testConnections(readMetaNodeWorkflowLines, mtnMtd);
        testCreatedBy(readMetaNodeWorkflowLines, mtnMtd);
        testCustomDescription(readMetaNodeWorkflowLines, mtnMtd);
        testWorkflowName(readMetaNodeWorkflowLines, mtnMtd);
        testNodeIds(readMetaNodeWorkflowLines, mtnMtd, "15:");
        testVersion(readMetaNodeWorkflowLines, mtnMtd);

        assertThat(mtnMtd.getUnexpectedFileNames().isEmpty()).isTrue();

        // Read node fields
        assertThat(mtnMtd.getNodeId()).isEqualTo("15");

        testTemplateLink(readMetaNodeWorkflowLines, mtnMtd);
        testAnnotationText(readMetaNodeWorkflowLines, mtnMtd);
    }

    /**
     * Tests that the WrappedMetaNode was read correctly (both WorkflowMetadata and NodeMetadata).
     *
     * @throws Exception
     */
    @Test
    void testReadingWrappedMetaNode() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        final SubnodeMetadata mtnMtd = (SubnodeMetadata)wkfMd.getNodes().stream()
            .filter(n -> n.getType().equals(NodeType.SUBNODE)).findFirst().get();
        final List<String> readMetaNodeWorkflowLines = Files.readAllLines(
            new File(workflowDir.toFile(), "Format Outpu (#16)/workflow.knime").toPath(), StandardCharsets.UTF_8);
        final List<String> readMetaNodeNodeLines = Files.readAllLines(
            new File(workflowDir.toFile(), "Format Outpu (#16)/settings.xml").toPath(), StandardCharsets.UTF_8);

        // Read workflow fields
        testAnnotations(readMetaNodeWorkflowLines, mtnMtd);
        testConnections(readMetaNodeWorkflowLines, mtnMtd);
        testCreatedBy(readMetaNodeWorkflowLines, mtnMtd);
        testCustomDescription(readMetaNodeWorkflowLines, mtnMtd);
        testWorkflowName(readMetaNodeWorkflowLines, mtnMtd);
        testNodeIds(readMetaNodeWorkflowLines, mtnMtd, "16:");
        testVersion(readMetaNodeWorkflowLines, mtnMtd);

        assertThat(mtnMtd.getUnexpectedFileNames().isEmpty()).isTrue();

        // Read node fields
        testAnnotationText(readMetaNodeNodeLines, mtnMtd);
        testCustomNodeDescription(readMetaNodeNodeLines, mtnMtd);
        testNodeConfiguration(workflowDir.resolve("Format Outpu (#16)"), mtnMtd);
        testTemplateLink(readMetaNodeNodeLines, mtnMtd);

        assertThat(mtnMtd.getNodeId()).isEqualTo("16");
    }

    // -- Test reading from zip --

    /**
     * Test reading a workflow from a zip file
     *
     * @throws Exception
     */
    @Test
    void testReadingZipWorkflow() throws Exception {
        // This zip contains multiple workflows, but only the first should be read
        final Path zipFile = getResourcePath("/workflowalizer-test.zip");
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readAll().build();
        final WorkflowMetadata twm = Workflowalizer.readWorkflow(zipFile, wc);
        assertThat(twm.getName()).isEqualTo("Testing_Workflowalizer_360Pre");

        testAnnotations(readWorkflowLines, twm);
        testAuthorInformation(readWorkflowLines, twm);
        testConnections(readWorkflowLines, twm);
        testCreatedBy(readWorkflowLines, twm);
        testCustomDescription(readWorkflowLines, twm);
        testVersion(readWorkflowLines, twm);
        testSvg(1301, 501, twm);

        assertThat(twm.getArtifacts().get().containsAll(List.of( //
                ".artifacts/openapi-input-parameters.json", //
                ".artifacts/openapi-input-resources.json", //
                ".artifacts/openapi-output-parameters.json", //
                ".artifacts/openapi-output-resources.json", //
                ".artifacts/workflow-configuration.json", //
                ".artifacts/workflow-configuration-representation.json", //
                ".artifacts/hub-event-input-parameters.json", //
                ".artifacts/secret-store-parameters.json"))).as("Expected artifacts file").isTrue();
        assertThat(twm.getUnexpectedFileNames().isEmpty()).as("List of unexpected artifacts is empty").isTrue();

        // Top-level workflow nodes
        testNodeIds(readWorkflowLines, twm, 7, 1, 1);
        MetanodeMetadata mm = null;
        SubnodeMetadata sm = null;
        for (final NodeMetadata node : twm.getNodes()) {
            if (node.getType().equals(NodeType.SUBNODE)) {
                sm = (SubnodeMetadata)node;
            }
            if (node.getType().equals(NodeType.METANODE)) {
                mm = (MetanodeMetadata)node;
            }
        }

        // Metanode nodes
        final List<String> readMetaNodeWorkflowLines = Files.readAllLines(
            new File(workflowDir.toFile(), "Hierarchical (#15)/workflow.knime").toPath(), StandardCharsets.UTF_8);
        testNodeIds(readMetaNodeWorkflowLines, mm, 3, 0, 0);

        // Subnode nodes
        final List<String> readSubNodeWorkflowLines = Files.readAllLines(
            new File(workflowDir.toFile(), "Format Outpu (#16)/workflow.knime").toPath(), StandardCharsets.UTF_8);
        testNodeIds(readSubNodeWorkflowLines, sm, 6, 0, 0);

        // Configuration files
        assertThat(twm.getWorkflowConfiguration().get()).as("Unexpected workflow configuration").isEqualTo(WorkflowalizerArtifactContent.WORKFLOW_CONFIGURATION.value());
        assertThat(twm.getWorkflowConfigurationRepresentation().get()).as("Unexpected workflow configuration representation").isEqualTo(WorkflowalizerArtifactContent.WORKFLOW_CONFIGURATION_REPRESENTATION.value());
        assertThat(twm.getOpenapiInputParameters().get()).as("Unexpected openapi input parameters").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_INPUT_PARAMETERS.value());
        assertThat(twm.getOpenapiInputResources().get()).as("Unexpected openapi input resources").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_INPUT_RESOURCES.value());
        assertThat(twm.getOpenapiOutputParameters().get()).as("Unexpected openapi output parameters").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_OUTPUT_PARAMETERS.value());
        assertThat(twm.getOpenapiOutputResources().get()).as("Unexpected openapi output resources").isEqualTo(WorkflowalizerArtifactContent.OPENAPI_OUTPUT_RESOURCES.value());
    }

    /**
     * Test reading and extracting a workflow SVG from a zip.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    void testReadingSVGFromZip() throws Exception {
        final Path zipFile = getResourcePath("/workflowalizer-test.zip");
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(zipFile, wc);
        testSvg(1301, 501, wm);

        final Path svg = workflowDir.resolve("workflow.svg");
        try (final InputStream readSvg = wm.getSvgInputStream().orElse(null)) {
            assertThat(readSvg).isNotNull();
            final byte[] bytes = IOUtils.toByteArray(Files.newInputStream(svg));
            final byte[] readBytes = IOUtils.toByteArray(readSvg);
            assertThat(readBytes.length).isEqualTo(bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                assertThat(readBytes[i]).isEqualTo(bytes[i]);
            }
        }

        assertUOEThrown(wm::getConnections);
        assertUOEThrown(wm::getNodes);
        assertUOEThrown(wm::getUnexpectedFileNames);
        assertUOEThrown(wm::getWorkflowSetMetadata);
    }

    // -- Test reading template --

    /**
     * Tests that the template has the correct structure
     *
     * @throws Exception
     */
    @Test
    void testTemplateStructure() throws Exception {
        final TemplateMetadata template = Workflowalizer.readTemplate(templateDir);
        final Path t = Paths.get(templateDir.toAbsolutePath().toString(), "workflow.knime");
        testStructure(template, t);
    }

    /**
     * Tests that the template fields are read correctly.
     *
     * @throws Exception
     */
    @Test
    void testTemplateMetadata() throws Exception {
        final TemplateMetadata template = Workflowalizer.readTemplate(templateDir);

        assertThat(template.getType()).isEqualTo(RepositoryItemType.TEMPLATE);
        assertThat(template.getName()).isEqualTo(templateDir.getFileName().toString());
        assertThat(template.getUnexpectedFileNames().isEmpty()).isTrue();

        testAnnotations(readTemplateWorkflowKnime, template);
        testAuthorInformation(readTemplateWorkflowKnime, template);
        testConnections(readTemplateWorkflowKnime, template);
        testCreatedBy(readTemplateTemplateKnime, template);
        testCustomDescription(readTemplateWorkflowKnime, template);
        testNodeIds(readTemplateWorkflowKnime, template, null);
        testTemplateInformation(readTemplateTemplateKnime, template);
        testVersion(readTemplateTemplateKnime, template);

        testTemplateInformation(readTemplateTemplateKnimeWithTimezone,
            Workflowalizer.readTemplate(templateDirWithTimezone));
    }

    /**
     * Test reading a template's author information only
     *
     * @throws Exception
     */
    @Test
    void testReadingTemplateMetadataAuthorInformation() throws Exception {
        final WorkflowalizerConfiguration config = WorkflowalizerConfiguration.builder().build();
        final TemplateMetadata tm = Workflowalizer.readTemplate(templateDir, config);

        testAuthorInformation(readTemplateWorkflowKnime, tm);

        assertUOEThrown(tm::getConnections);
        assertUOEThrown(tm::getNodes);
        assertUOEThrown(tm::getUnexpectedFileNames);
    }

    /**
     * Tests reading a template's template information only
     *
     * @throws Exception
     */
    @Test
    void testReadingTemplateMetadataTemplateInformation() throws Exception {
        final WorkflowalizerConfiguration config = WorkflowalizerConfiguration.builder().build();
        final TemplateMetadata tm = Workflowalizer.readTemplate(templateDir, config);

        testTemplateInformation(readTemplateTemplateKnime, tm);

        assertUOEThrown(tm::getConnections);
        assertUOEThrown(tm::getNodes);
        assertUOEThrown(tm::getUnexpectedFileNames);
    }

    /**
     * Tests reading the 'comments' (aka description) in the workflowset.meta file (workflow(-group) metadata) and makes
     * sure that the 'line.separator'-system property is NOT being used.
     *
     * @throws Exception
     */
    @Test
    void testLineSeparatorMetadata() throws Exception {
        var lineSeparatorKey = "line.separator";
        var lineSeparator = System.getProperty(lineSeparatorKey);
        System.setProperty(lineSeparatorKey, "nonesense");
        try {
            var metadata = Workflowalizer.readWorkflowGroup(workflowGroupFile);
            var title = metadata.getTitle().orElse(null);
            var description = metadata.getDescription().orElse(null);
            assertThat(title, is("Title"));
            assertThat(description, Matchers.startsWith("Lorem ipsum dolor sit amet"));
        } finally {
            System.setProperty(lineSeparatorKey, lineSeparator);
        }
    }

    /**
     * Tests reading a template from a zip
     *
     * @throws Exception
     */
    @Test
    void testReadingTemplateFromZip() throws Exception {
        final Path zipFile = getResourcePath("/workflowalizer-test.zip");
        final TemplateMetadata tm = Workflowalizer.readTemplate(zipFile);

        assertThat(tm.getName()).isEqualTo("Hierarchical Cluster Assignment with timezone");
        assertThat(tm.getUnexpectedFileNames().isEmpty()).isTrue();

        List<String> templateAndWorkflowDotKnimeFileLines = new ArrayList<>();
        templateAndWorkflowDotKnimeFileLines.addAll(readTemplateTemplateKnimeWithTimezone);
        templateAndWorkflowDotKnimeFileLines.addAll(readTemplateWorkflowKnimeWithTimezone);

        testAnnotations(templateAndWorkflowDotKnimeFileLines, tm);
        testAuthorInformation(templateAndWorkflowDotKnimeFileLines, tm);
        testConnections(templateAndWorkflowDotKnimeFileLines, tm);
        testCreatedBy(templateAndWorkflowDotKnimeFileLines, tm);
        testCustomDescription(templateAndWorkflowDotKnimeFileLines, tm);
        testNodeIds(templateAndWorkflowDotKnimeFileLines, tm, null);
        testTemplateInformation(templateAndWorkflowDotKnimeFileLines, tm);
        testVersion(templateAndWorkflowDotKnimeFileLines, tm);
    }

    /**
     * Tests trying to read a template as a workflow
     *
     * @throws Exception
     */
    @Test
    void testReadingTemplateAsWorkflow() throws Exception {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            Workflowalizer.readWorkflow(templateDir);
        });
        assertThat(exception.getMessage().contains(templateDir + " is a template, not a workflow")).isTrue();
    }

    /**
     * Tests trying to read a workflow as a template
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowAsTemplate() throws Exception {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            Workflowalizer.readTemplate(workflowDir);
        });
        assertThat(exception.getMessage().contains(workflowDir + " is a workflow, not a template")).isTrue();
    }

    // -- Component Template --

    /**
     * Tests that the component template has the correct structure
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testComponentTemplateStructure() throws Exception {
        final TemplateMetadata template = Workflowalizer.readTemplate(componentTemplateDir);
        final Path t = Paths.get(componentTemplateDir.toAbsolutePath().toString(), "workflow.knime");
        assertThat(template instanceof ComponentMetadata).isTrue();
        testStructure(template, t);
    }

    /**
     * Tests reading the "workflow" fields of the component template
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplateWorkflowFields() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        assertThat(tm.getName()).isEqualTo("Simple-Component");
        assertThat(tm.getUnexpectedFileNames().isEmpty()).isTrue();

        testAnnotations(readComponentTemplateWorkflowKnime, tm);
        testAuthorInformation(readComponentTemplateWorkflowKnime, tm);
        testConnections(readComponentTemplateWorkflowKnime, tm);
        testCreatedBy(readComponentTemplateTemplateKnime, tm);
        testCustomDescription(readComponentTemplateWorkflowKnime, tm);
        testNodeIds(readComponentTemplateWorkflowKnime, tm, null);
        testTemplateInformation(readComponentTemplateTemplateKnime, tm);
        testVersion(readComponentTemplateTemplateKnime, tm);
    }

    /**
     * Tests reading a components ports.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplatePorts() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // inports
        final List<ComponentPortInfo> inports = cm.getInPorts();
        assertThat(inports.size()).isEqualTo(2);

        assertThat(inports.get(0).getDescription().orElse("")).isEqualTo("1st data table inport");
        assertThat(inports.get(0).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");
        assertThat(inports.get(0).getName().orElse("")).isEqualTo("Inport 1");

        assertThat(inports.get(1).getDescription().orElse("")).isEqualTo("2nd data table inport");
        assertThat(inports.get(1).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");
        assertThat(inports.get(1).getName().orElse("")).isEqualTo("Inport 2");

        // outports
        final List<ComponentPortInfo> outports = cm.getOutPorts();
        assertThat(outports.size()).isEqualTo(3);
        assertThat(outports.get(0).getDescription().orElse("")).isEqualTo("Variable outport.");
        assertThat(outports.get(0).getObjectClass()).isEqualTo("org.knime.core.node.port.flowvariable.FlowVariablePortObject");
        assertThat(outports.get(0).getName().orElse("")).isEqualTo("Outport 1");

        assertThat(outports.get(1).getDescription().orElse("")).isEqualTo("Data outport.");
        assertThat(outports.get(1).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");
        assertThat(outports.get(1).getName().orElse("")).isEqualTo("Outport 2");

        assertThat(outports.get(2).getDescription().orElse("")).isEqualTo("Image outport.");
        assertThat(outports.get(2).getObjectClass()).isEqualTo("org.knime.core.node.port.image.ImagePortObject");
        assertThat(outports.get(2).getName().orElse("")).isEqualTo("Outport 3");
    }

    /**
     * Tests that all "view" (JS views, quickforms, widget, etc.) nodes were read.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplateViewNodes() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // 3 JS views, 1 widget, 1 legacy quickform
        assertThat(cm.getViewNodes().size()).isEqualTo(5);
        assertThat(cm.getViewNodes().contains("org.knime.js.base.node.widget.input.bool.BooleanWidgetNodeFactory")).isTrue();
        assertThat(cm.getViewNodes().contains("org.knime.js.base.node.quickform.input.bool.BooleanInputQuickFormNodeFactory")).isTrue();
        assertThat(cm.getViewNodes().contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory:f822b045")).isTrue();
        assertThat(cm.getViewNodes().contains("org.knime.js.base.node.viz.heatmap.HeatMapNodeFactory")).isTrue();
        assertThat(cm.getViewNodes().contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory:1ce36c2f")).isTrue();

        // 3 JS views, 1 widget, 1 legacy quickform
        assertThat(cm.getViewNodeFactoryIds().size()).isEqualTo(5);
        assertThat(cm.getViewNodeFactoryIds().contains("org.knime.js.base.node.widget.input.bool.BooleanWidgetNodeFactory")).isTrue();
        assertThat(cm.getViewNodeFactoryIds()
                .contains("org.knime.js.base.node.quickform.input.bool.BooleanInputQuickFormNodeFactory")).isTrue();
        assertThat(cm.getViewNodeFactoryIds()
                .contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory#Parallel Coordinates Plot")).isTrue();
        assertThat(cm.getViewNodeFactoryIds().contains("org.knime.js.base.node.viz.heatmap.HeatMapNodeFactory")).isTrue();
        assertThat(cm.getViewNodeFactoryIds().contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory#Histogram")).isTrue();
    }

    /**
     * Tests reading the component template's description.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplateDescription() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;

        assertThat(cm.getDescription().orElse("")).isEqualTo("Simple component description.");
    }

    /**
     * Tests reading the component template's dialog.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplateDialog() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // 1 configuration node, 1 quickform node
        assertThat(cm.getDialog().size()).isEqualTo(1);
        assertThat(cm.getDialog().get(0).getFields().size()).isEqualTo(2);
        assertThat(cm.getDialog().get(0).getSectionHeader().isPresent()).isFalse();
        assertThat(cm.getDialog().get(0).getSectionDescription().isPresent()).isFalse();
        assertThat(cm.getDialog().get(0).getFields().get(0).getName().orElse("")).isEqualTo("Legacy dialog name");
        assertThat(cm.getDialog().get(0).getFields().get(0).getDescription().orElse("")).isEqualTo("Legacy dialog description");
        assertThat(cm.getDialog().get(0).getFields().get(1).getName().orElse("")).isEqualTo("Dialog field name");
        assertThat(cm.getDialog().get(0).getFields().get(1).getDescription().orElse("")).isEqualTo("Dialog field description");
    }

    /**
     * Tests reading the component's type, for a component with {@code LoadVersion < 4.1.0}.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentType() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;
        assertThat(cm.getComponentType().isPresent()).isFalse();
    }

    /**
     * Tests reading the component's icon, for a component with {@code LoadVersion < 4.1.0}.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentIcon() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;
        assertThat(cm.getIcon().isPresent()).isFalse();
    }

    /**
     * Tests reading a component template with nested elements (i.e. other components or metanodes).
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplateNested() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = getResourceAsStream("/component-template-nested.zip")) {
            unzip(is, componentPath);
        }
        componentPath = componentPath.resolve("Nested Component");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertThat(cm.getDescription().orElse("")).isEqualTo("I like kitty cats");

        // InPorts
        final List<ComponentPortInfo> inports = cm.getInPorts();
        assertThat(inports.size()).isEqualTo(2);

        assertThat(inports.get(0).getDescription().orElse("")).isEqualTo("IP1 desc");
        assertThat(inports.get(0).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");
        assertThat(inports.get(0).getName().orElse("")).isEqualTo("InPort 1");

        assertThat(inports.get(1).getDescription().orElse("")).isEqualTo("IP2 desc");
        assertThat(inports.get(1).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");
        assertThat(inports.get(1).getName().orElse("")).isEqualTo("InPort 2");

        // OutPorts
        final List<ComponentPortInfo> outports = cm.getOutPorts();
        assertThat(outports.size()).isEqualTo(2);

        assertThat(outports.get(0).getDescription().orElse("")).isEqualTo("OP1 desc");
        assertThat(outports.get(0).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");
        assertThat(outports.get(0).getName().orElse("")).isEqualTo("OutPort 1");

        assertThat(outports.get(1).getDescription().orElse("")).isEqualTo("OP2 desc");
        assertThat(outports.get(1).getObjectClass()).isEqualTo("org.knime.core.node.port.flowvariable.FlowVariablePortObject");
        assertThat(outports.get(1).getName().orElse("")).isEqualTo("OutPort 2");

        // Dialog - "top-level" dialog elements only
        assertThat(cm.getDialog().size()).isEqualTo(1);
        assertThat(cm.getDialog().get(0).getFields().size()).isEqualTo(1);
        assertThat(cm.getDialog().get(0).getFields().get(0).getName().orElse("")).isEqualTo("Field Name");
        assertThat(cm.getDialog().get(0).getFields().get(0).getDescription().orElse("")).isEqualTo("Field Description");

        // Views - including all nested views
        assertThat(cm.getViewNodes().size()).isEqualTo(2);
        assertThat(cm.getViewNodes().contains("org.knime.js.base.node.viz.heatmap.HeatMapNodeFactory")).isTrue();
        assertThat(cm.getViewNodes().contains("org.knime.js.base.node.viz.pagedTable.PagedTableViewNodeFactory")).isTrue();

        // Views - including all nested views
        assertThat(cm.getViewNodeFactoryIds().size()).isEqualTo(2);
        assertThat(cm.getViewNodeFactoryIds().contains("org.knime.js.base.node.viz.heatmap.HeatMapNodeFactory")).isTrue();
        assertThat(cm.getViewNodeFactoryIds().contains("org.knime.js.base.node.viz.pagedTable.PagedTableViewNodeFactory")).isTrue();

        // Type
        assertThat(cm.getComponentType().isPresent()).isFalse();

        // Icon
        assertThat(cm.getIcon().isPresent()).isFalse();
    }

    /**
     * Tests reading a component template which has no ports.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplateNoPorts() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = getResourceAsStream("/component-template-no-ports.zip")) {
            unzip(is, componentPath);
        }
        componentPath = componentPath.resolve("No Connections");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertThat(cm.getDescription().isPresent()).isFalse();

        // Ports
        assertThat(cm.getInPorts().isEmpty()).isTrue();
        assertThat(cm.getOutPorts().isEmpty()).isTrue();

        // Dialog - "top-level" dialog elements only
        assertThat(cm.getDialog().isEmpty()).isTrue();

        // Views - including all nested views
        assertThat(cm.getViewNodes().isEmpty()).isTrue();

        // Views - including all nested views
        assertThat(cm.getViewNodeFactoryIds().isEmpty()).isTrue();

        // Type
        assertThat(cm.getComponentType().isPresent()).isFalse();

        // Icon
        assertThat(cm.getIcon().isPresent()).isFalse();
    }

    /**
     * Tests reading a component template which has ports but without descriptions.
     *
     * @throws Exception if an error occurs
     * @since 5.13
     */
    @Test
    void testReadingComponentNoPortDescriptions() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = getResourceAsStream("/component-no-port-descriptions.zip")) {
            unzip(is, componentPath);
        }
        componentPath = componentPath.resolve("No Descriptions");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertThat("Unexpected metadata subtype read", tm, is(instanceOf(ComponentMetadata.class)));
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertThat(cm.getDescription().isPresent()).isFalse();

        // Ports
        assertThat("No input ports found", cm.getInPorts(), is(not(empty())));
        assertThat("Unexpected description for input port", cm.getInPorts().get(0).getDescription().orElse(null),
            is(nullValue()));
    }

    /**
     * Tests reading a component template whose ports are not all connected.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingComponentTemplateMissingConnections() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = getResourceAsStream("/component-template-missing-connection.zip")) {
            unzip(is, componentPath);
        }
        componentPath = componentPath.resolve("Missing Connection");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertThat(tm instanceof ComponentMetadata).isTrue();
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertThat(cm.getDescription().isPresent()).isFalse();

        // InPorts
        final List<ComponentPortInfo> inports = cm.getInPorts();
        assertThat(inports.size()).isEqualTo(2);

        assertThat(inports.get(0).getDescription().isPresent()).isFalse();
        assertThat(inports.get(0).getName().orElse("")).isEqualTo("Port 1");
        assertThat(inports.get(0).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");

        assertThat(inports.get(1).getDescription().isPresent()).isFalse();
        assertThat(inports.get(1).getName().orElse("")).isEqualTo("Port 2");
        assertThat(inports.get(1).getObjectClass()).isEqualTo("org.knime.core.node.BufferedDataTable");

        // OutPorts
        final List<ComponentPortInfo> outports = cm.getOutPorts();
        assertThat(outports.size()).isEqualTo(1);

        assertThat(outports.get(0).getDescription().isPresent()).isFalse();
        assertThat(outports.get(0).getName().orElse("")).isEqualTo("Port 1");
        assertThat(outports.get(0).getObjectClass()).isEqualTo("org.knime.core.node.port.image.ImagePortObject");

        // Dialog - "top-level" dialog elements only
        assertThat(cm.getDialog().isEmpty()).isTrue();

        // Views - including all nested views
        assertThat(cm.getViewNodes().size()).isEqualTo(1);
        assertThat(cm.getViewNodes().get(0)).isEqualTo("org.knime.dynamic.js.v30.DynamicJSNodeFactory:1d2c1a0e");

        // Views - including all nested views
        assertThat(cm.getViewNodeFactoryIds().size()).isEqualTo(1);
        assertThat(cm.getViewNodeFactoryIds().get(0)).isEqualTo("org.knime.dynamic.js.v30.DynamicJSNodeFactory#Bar Chart");

        // Type
        assertThat(cm.getComponentType().isPresent()).isFalse();

        // Icon
        assertThat(cm.getIcon().isPresent()).isFalse();
    }

    // -- Workflow group --

    /**
     * Tests reading a workflow group
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowGroup() throws Exception {
        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(workflowGroupFile);
        assertThat(wsm.getType()).isEqualTo(RepositoryItemType.WORKFLOW_GROUP);

        testWorkflowSetMeta(readWorkflowGroupLines, wsm);
    }

    /**
     * Tests reading a workflow group with a title that has over 20 words
     *
     * @throws Exception
     */
    @Test
    void testReadingWorkflowGroupWithLongTitle() throws Exception {
        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(workflowGroupFileLongTitle);
        assertThat(wsm.getType()).isEqualTo(RepositoryItemType.WORKFLOW_GROUP);

        testWorkflowSetMeta(readWorkflowGroupLinesLongTitle, wsm);
    }

    /**
     * Tests reading a workflow group from a directory path.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    void testReadingWorkflowGroupDir() throws Exception {
        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(workflowGroupFile.getParent());
        assertThat(wsm.getType()).isEqualTo(RepositoryItemType.WORKFLOW_GROUP);

        testWorkflowSetMeta(readWorkflowGroupLines, wsm);
    }

    /**
     * Tests reading a zipped workflow group
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    void testReadingWorkflowGroupZip() throws Exception {
        final Path zipFile = getResourcePath("/workflowalizer-test.zip");
        final WorkflowGroupMetadata wgm = Workflowalizer.readWorkflowGroup(zipFile);
        assertThat(wgm.getType()).isEqualTo(RepositoryItemType.WORKFLOW_GROUP);
        assertThat(wgm.getAuthor().isPresent()).isFalse();
        assertThat(wgm.getDescription().isPresent()).isFalse();
        assertThat(wgm.getTitle().isPresent()).isFalse();
        assertThat(wgm.getTags().isPresent()).isFalse();
        assertThat(wgm.getLinks().isPresent()).isFalse();
    }

    // -- General repository item --

    /**
     * Tests the method for reading a generic repository item on the file system.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    void testReadingRepositoryItem() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        RepositoryItemMetadata rip = Workflowalizer.readRepositoryItem(workflowDir, wc);
        assertThat(rip.getType()).isEqualTo(RepositoryItemType.WORKFLOW);
        assertThat(rip instanceof WorkflowMetadata).isTrue();

        rip = Workflowalizer.readRepositoryItem(templateDir, wc);
        assertThat(rip.getType()).isEqualTo(RepositoryItemType.TEMPLATE);
        assertThat(rip instanceof TemplateMetadata).isTrue();

        rip = Workflowalizer.readRepositoryItem(workflowGroupFile.getParent(), wc);
        assertThat(rip.getType()).isEqualTo(RepositoryItemType.WORKFLOW_GROUP);
        assertThat(rip instanceof WorkflowGroupMetadata).isTrue();

        try {
            rip = Workflowalizer.readRepositoryItem(nodeDir, wc);
            fail("Expected exception was not thrown");
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage().startsWith("No template, workflow, or workflow group found")).isTrue();
        }
    }

    /**
     * Tests the method for reading a generic zipped repository item.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    void testReadingRepositoryItemZip() throws Exception {
        // Test reading group
        final Path zipFile = getResourcePath("/workflowalizer-test.zip");
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final RepositoryItemMetadata rip = Workflowalizer.readRepositoryItem(zipFile, wc);

        assertThat(rip.getType()).isEqualTo(RepositoryItemType.WORKFLOW_GROUP);
        assertThat(rip instanceof WorkflowGroupMetadata).isTrue();

        // Test that is the top-level group
        final WorkflowGroupMetadata wgm = (WorkflowGroupMetadata)rip;
        assertThat(wgm.getAuthor().isPresent()).isFalse();
        assertThat(wgm.getDescription().isPresent()).isFalse();
        assertThat(wgm.getTitle().isPresent()).isFalse();
        assertThat(wgm.getTags().isPresent()).isFalse();
        assertThat(wgm.getLinks().isPresent()).isFalse();

        // Test reading workflow
        final Path zipWorkflow = getResourcePath("/simple-workflow.zip");
        final RepositoryItemMetadata workflow = Workflowalizer.readRepositoryItem(zipWorkflow, wc);

        assertThat(workflow.getType()).isEqualTo(RepositoryItemType.WORKFLOW);
        assertThat(workflow instanceof WorkflowMetadata).isTrue();

        // Test reading component
        final Path zipComponent = getResourcePath("/component-template-simple.zip");
        final RepositoryItemMetadata component = Workflowalizer.readRepositoryItem(zipComponent, wc);

        assertThat(component.getType()).isEqualTo(RepositoryItemType.TEMPLATE);
        assertThat(component instanceof ComponentMetadata).isTrue();

        // Test zip with no groups, workflows, or components
        try {
            final Path noRepoItems = getResourcePath("/noRepoItems.zip");
            Workflowalizer.readRepositoryItem(noRepoItems, wc);
            fail("No exception thrown for zip archive containing no repository items");
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage().startsWith("No template," + " workflow, or workflow group found in zip file at: ")).as("Incorrect exception message: " + ex.getMessage()).isTrue();
        }
    }

    /**
     * Tests that nodes in "org.knime.features.testing.core" are instead given the extension symbolic name
     * "org.knime.features.testing.application".
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    void testReadingWorkflowWithTestNodes() throws Exception {
        final Path zipFile = getResourcePath("/Testing_nodes_wkfl.knwf");
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(zipFile, wc);

        for (NodeMetadata nm : wm.getNodes()) {
            if ((nm instanceof NativeNodeMetadata)) {
                NativeNodeMetadata nn = (NativeNodeMetadata)nm;
                if (nn.getFactoryName().equals("org.knime.testing.node.disturber.DisturberNodeFactory") || nn
                    .getFactoryName().equals("org.knime.testing.internal.nodes.differ.DifferenceCheckerNodeFactory")) {
                    assertThat(nn.getNodeAndBundleInformation().getFeatureSymbolicName().orElse(null)).as("Testing node '" + nn.getFactoryName() + "' has incorrect extension symbolic name").isEqualTo("org.knime.features.testing.application");
                }
            }
        }
    }

    /**
     * Tests reading a component template which does not have the inport/outport configuration objects in the XML, but
     * does have port objects listed in the XML.
     *
     * @throws Exception
     */
    @Test
    void testComponentPortsWithoutText() throws Exception {
        Path zipFile = getResourcePath("/Component-No-Port-Text.knwf");
        WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        TemplateMetadata tm = Workflowalizer.readTemplate(zipFile, wc);

        assertThat(tm instanceof ComponentMetadata).as("Template was not read as component").isTrue();
        ComponentMetadata cm = (ComponentMetadata)tm;

        assertThat(cm.getInPorts().size()).as("Unexpected number of inports").isEqualTo(1);
        assertThat(cm.getOutPorts().size()).as("Unexpected number of outports").isEqualTo(1);

        ComponentPortInfo inport = cm.getInPorts().get(0);
        assertThat(Optional.empty()).as("Unexpected description for inport").isEqualTo(inport.getDescription());
        assertThat(Optional.empty()).as("Unexpected name for inport").isEqualTo(inport.getName());
        assertThat(inport.getObjectClass()).as("Unexpected object class for inport").isEqualTo("org.knime.core.node.BufferedDataTable");

        ComponentPortInfo outport = cm.getOutPorts().get(0);
        assertThat(Optional.empty()).as("Unexpected description for outport").isEqualTo(outport.getDescription());
        assertThat(Optional.empty()).as("Unexpected name for outport").isEqualTo(outport.getName());
        assertThat(outport.getObjectClass()).as("Unexpected object class for outport").isEqualTo("org.knime.core.node.BufferedDataTable");
    }

    /**
     * Tests that the parsing fails when parsing invalid workflowset.meta XML from a workflow directory.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testWorkflowWithBrokenXML() throws Exception {
        Path workspace = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (InputStream is = getResourceAsStream("/Workflow_BrokenMetaXML.knwf")) {
            unzip(is, workspace);
        }
        final var directory = workspace.resolve("Workflow_BrokenMetaXML");
        assertThrows(SAXParseException.class, () -> Workflowalizer.readRepositoryItem(directory),
            "Unexpected response code when parsing a workflow with invalid XML");
    }

    /**
     * Tests that the parsing fails when parsing invalid workflowset.meta XML from a zip archive.
     *
     * @throws Exception if error occurs
     */
    @Test
    void testWorkflowWithBrokenXMLZip() throws Exception {
        Path workflowPath = getResourcePath("/Workflow_BrokenMetaXML.knwf");
        assertThrows(SAXParseException.class, () -> Workflowalizer.readRepositoryItem(workflowPath),
            "Unexpected response code when parsing a workflow with invalid XML");
    }

    @Test
    void testReadingDynamicNodes() throws Exception {
        Path workspace = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (InputStream is = getResourceAsStream("/Workflowalizer_DynamicNodes.knwf")) {
            unzip(is, workspace);
        }
        final var directory = workspace.resolve("Workflowalizer_DynamicNodes");
        var wm = Workflowalizer.readWorkflow(directory);

        // dynamic node created with 5.2 (with a proper 'factory-id-uniquifier')
        assertThat(((NativeNodeMetadata)wm.getNodes().get(0)).getFactoryId()).isEqualTo(
            "org.knime.core.node.NodeFactoryIdTestNodeSetFactory$NodeFactoryIdTestDynamicNodeFactory1#factory-id-uniquifier-1");
        // dynamic node created before 5.2 (with the node name in the factory-id)
        assertThat(((NativeNodeMetadata)wm.getNodes().get(1)).getFactoryId()).isEqualTo(
                "org.knime.core.node.NodeFactoryIdTestNodeSetFactory$NodeFactoryIdTestDynamicNodeFactory2#dynamic node name 2");
    }
}
