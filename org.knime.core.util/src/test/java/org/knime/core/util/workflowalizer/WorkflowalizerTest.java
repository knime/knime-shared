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

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.knime.core.util.PathUtils;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;
import org.knime.core.util.workflowalizer.RepositoryItemMetadata.RepositoryItemType;

/**
 * Tests for {@link Workflowalizer}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public class WorkflowalizerTest extends AbstractWorkflowalizerTest {

    private static Path workspaceDir;
    private static Path workflowDir;
    private static Path nodeDir;
    private static Path templateDir;
    private static Path templateDirWithTimezone;
    private static Path workflowGroupFile;
    private static Path workflowGroupFileLongTitle;
    private static Path componentTemplateDir;

    private static List<String> readWorkflowLines;
    private static List<String> readNodeLines;
    private static List<String> readTemplateWorkflowKnime;
    private static List<String> readTemplateTemplateKnime;
    private static List<String> readTemplateTemplateKnimeWithTimezone;
    private static List<String> readWorkflowSetLines;
    private static List<String> readWorkflowGroupLines;
    private static List<String> readWorkflowGroupLinesLongTitle;
    private static List<String> readComponentTemplateWorkflowKnime;
    private static List<String> readComponentTemplateTemplateKnime;

    /** Exception for testing */
    @Rule
    public ExpectedException m_exception = ExpectedException.none();

    /**
     * Readers zip archive, and creates temporary directory for workflow files.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setup() throws Exception {
        workspaceDir = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (InputStream is = WorkflowalizerTest.class.getResourceAsStream("/workflowalizer-test.zip")) {
            unzip(is, workspaceDir.toFile());
        }
        workflowDir = new File(workspaceDir.toFile(), "workflowalizer-test/Testing_Workflowalizer_360Pre").toPath();
        readWorkflowLines = Files.readAllLines(new File(workflowDir.toFile(), "workflow.knime").toPath());

        nodeDir = new File(workflowDir.toFile(), "Column Splitter (#10)").toPath();
        readNodeLines = Files.readAllLines(new File(nodeDir.toFile(), "settings.xml").toPath());

        templateDir = new File(workspaceDir.toFile(), "workflowalizer-test/Hierarchical Cluster Assignment").toPath();
        readTemplateWorkflowKnime =
            Files.readAllLines(Paths.get(templateDir.toAbsolutePath().toString(), "workflow.knime"));
        readTemplateTemplateKnime =
            Files.readAllLines(Paths.get(templateDir.toAbsolutePath().toString(), "template.knime"));

        templateDirWithTimezone =
            workspaceDir.resolve("workflowalizer-test/Hierarchical Cluster Assignment with timezone");
        readTemplateTemplateKnimeWithTimezone = Files.readAllLines(templateDirWithTimezone.resolve("template.knime"));

        componentTemplateDir = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = WorkflowalizerTest.class.getResourceAsStream("/component-template-simple.zip")) {
            unzip(is, componentTemplateDir.toFile());
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
    public void testStructure() throws Exception {
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
    public void testTopLevelWorkflowMetadata() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        assertEquals(RepositoryItemType.WORKFLOW, wkfMd.getType());

        testAnnotations(readWorkflowLines, wkfMd);
        testAuthorInformation(readWorkflowLines, wkfMd);
        testConnections(readWorkflowLines, wkfMd);
        testCreatedBy(readWorkflowLines, wkfMd);
        testCustomDescription(readWorkflowLines, wkfMd);
        testWorkflowName(workflowDir, wkfMd);
        testNodeIds(readWorkflowLines, wkfMd, null);
        testVersion(readWorkflowLines, wkfMd);
        testSvg(1301, 501, wkfMd);

        assertTrue(wkfMd.getUnexpectedFileNames().isEmpty());
        assertTrue(wkfMd.getWorkflowSetMetadata().isPresent());
        testWorkflowSetMetaSimple(readWorkflowSetLines, wkfMd.getWorkflowSetMetadata().get());

        assertTrue("Expected artifacts file",
            wkfMd.getArtifacts().get()
                .contains(workflowDir
                    .relativize(new File(workflowDir.toFile(), ".artifacts/openapi-input-parameters.json").toPath())
                    .toString()));

        String testFile = "{\n"
                + "  \"test\": \"value\",\n"
                + "  \"test\": \"two\"\n"
                + "}\n";
        assertEquals("Unexpected workflow configuration", testFile, wkfMd.getWorkflowConfiguration().get());
        assertEquals("Unexpected workflow configuration representation", testFile,
            wkfMd.getWorkflowConfigurationRepresentation().get());
    }

    /**
     * Tests that reading the workflow configuration fields without setting them in the
     * {@link WorkflowalizerConfiguration} results in a {@link UnsupportedOperationException}
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testReadingWorkflowConfigurations() throws Exception {
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir, WorkflowalizerConfiguration.builder().readNodeConfiguration().build());
        assertUOEThrown(wm::getWorkflowConfiguration);
        assertUOEThrown(wm::getWorkflowConfigurationRepresentation);
    }

    // -- Test reading individual workflow fields --

    /**
     * Tests reading workflow SVG file path.
     *
     * @throws Exception
     */
    @Test
    public void testReadingSVG() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testSvg(1301, 501, wkfMd);

        final Path svg = workflowDir.resolve("workflow.svg");
        try (final InputStream readSvg = wkfMd.getSvgInputStream().orElse(null)) {
            assertNotNull(readSvg);
            final byte[] bytes = IOUtils.toByteArray(Files.newInputStream(svg));
            final byte[] readBytes = IOUtils.toByteArray(readSvg);
            assertEquals(bytes.length, readBytes.length);
            for (int i = 0; i < bytes.length; i++) {
                assertEquals(bytes[i], readBytes[i]);
            }
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
    public void testReadingArtifactsDirectory() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final File test = new File(workflowDir.toFile(), ".artifacts/openapi-input-parameters.json");
        assertTrue(wkfMd.getArtifacts().isPresent());
        assertEquals(3, wkfMd.getArtifacts().get().size());
        assertTrue("Expected artifacts file",wkfMd.getArtifacts().get().contains(workflowDir.relativize(test.toPath()).toString()));

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
    public void testReadingUnexpectedFilePaths() throws Exception {
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
            assertEquals(2, unexpectedFiles.size());
            assertTrue(unexpectedFiles.contains(workflowDir.relativize(dataFile.toPath()).toString()));
            assertTrue(unexpectedFiles.contains(workflowDir.relativize(subDataFile.toPath()).toString()));

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
    public void testReadingAnnotations() throws Exception {
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
    public void testReadingAuthorInformation() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        testAuthorInformation(readWorkflowLines, wkfMd);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
        assertUOEThrown(wkfMd::getWorkflowSetMetadata);
    }

    /**
     * Tests that node connections are read correctly. This only checks that the IDs in the xml file match those of the
     * {@link NodeConnection}s.
     *
     * @throws Exception
     */
    @Test
    public void testReadingNodesAndConnections() throws Exception {
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
    public void testReadingCreatedBy() throws Exception {
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
    public void testReadingCustomDescription() throws Exception {
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
    public void testReadingName() throws Exception {
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
    public void testReadingNodes() throws Exception {
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
    public void testReadingVersion() throws Exception {
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
    public void testReadingWorkflowWorkflowSetMeta() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readWorkflowMeta().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertTrue(wkfMd.getWorkflowSetMetadata().isPresent());
        testWorkflowSetMetaSimple(readWorkflowSetLines, wkfMd.getWorkflowSetMetadata().get());

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
    public void testReadingWorkflowConfigurationFiles() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readWorkflowConfigurationFiles().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertTrue(wkfMd.getWorkflowConfiguration().isPresent());
        assertTrue(wkfMd.getWorkflowConfigurationRepresentation().isPresent());

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
    public void testReadingWorkflowCredentials() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final List<String> cred = wkfMd.getWorkflowCredentialsNames();
        assertEquals(1, cred.size());
        assertEquals("Credential", cred.get(0));

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
    public void testReadingWorkflowVariables() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final List<String> vars = wkfMd.getWorkflowVariables();
        assertEquals(3, vars.size());
        assertTrue(vars.contains("TestDouble"));
        assertTrue(vars.contains("TestInteger"));
        assertTrue(vars.contains("TestString"));

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
    public void testReadingHasReport() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        assertFalse(wkfMd.hasReport());

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
    public void testReadingWorkflowSetNotPresent() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readWorkflowMeta().build();
        final Path workflowPath = workspaceDir.resolve("workflowalizer-test/test_group/test1");
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowPath, wc);
        assertTrue(wkfMd.getWorkflowSetMetadata() != null);
        assertFalse(wkfMd.getWorkflowSetMetadata().isPresent());

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
    public void testReadingNodeInWorkflow() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;
        assertEquals(NodeType.NATIVE_NODE, node.getType());

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
    public void testReadingNodeInMetanode() throws Exception {
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
        assertEquals(NodeType.NATIVE_NODE, node.getType());
    }

    // -- Test reading individual node fields --

    /**
     * Test that node annotation text is read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testReadingAnnotationText() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
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
    public void testReadingCustomNodeDescription() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
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
    public void testReadingNodeConfiguration() throws Exception {
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readNodes().readNodeConfiguration().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;
        testNodeConfiguration(nodeDir, nativeNode);
    }

    /**
     * Test that the node and bundle information is read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testReadingNodeAndBundleInformation() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
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
    public void testReadingNodeId() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);

        // Filter on something that isn't ID, so the ID check actual tests something
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> (n instanceof NativeNodeMetadata)
            && ((NativeNodeMetadata)n).getNodeAndBundleInformation().getNodeName().isPresent()
            && ((NativeNodeMetadata)n).getNodeAndBundleInformation().getNodeName().get().contains("Column Splitter"))
            .findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertEquals("10", nativeNode.getNodeId());
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    /**
     * Test that the node type is read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testReadingType() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertEquals(NodeType.NATIVE_NODE, nativeNode.getType());
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    // -- Test reading (Wrapped)MetaNodes

    /**
     * Test that the MetaNode was read correctly (both WorkflowMetadata and NodeMetadata).
     *
     * @throws Exception
     */
    @Test
    public void testReadingMetaNode() throws Exception {
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

        assertTrue(mtnMtd.getUnexpectedFileNames().isEmpty());

        // Read node fields
        assertEquals("15", mtnMtd.getNodeId());

        testTemplateLink(readMetaNodeWorkflowLines, mtnMtd);
        testAnnotationText(readMetaNodeWorkflowLines, mtnMtd);
    }

    /**
     * Tests that the WrappedMetaNode was read correctly (both WorkflowMetadata and NodeMetadata).
     *
     * @throws Exception
     */
    @Test
    public void testReadingWrappedMetaNode() throws Exception {
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

        assertTrue(mtnMtd.getUnexpectedFileNames().isEmpty());

        // Read node fields
        testAnnotationText(readMetaNodeNodeLines, mtnMtd);
        testCustomNodeDescription(readMetaNodeNodeLines, mtnMtd);
        testNodeConfiguration(workflowDir.resolve("Format Outpu (#16)"), mtnMtd);
        testTemplateLink(readMetaNodeNodeLines, mtnMtd);

        assertEquals("16", mtnMtd.getNodeId());
    }

    // -- Test reading from zip --

    /**
     * Test reading a workflow from a zip file
     *
     * @throws Exception
     */
    @Test
    public void testReadingZipWorkflow() throws Exception {
        // This zip contains multiple workflows, but only the first should be read
        final Path zipFile = Paths.get(Workflowalizer.class.getResource("/workflowalizer-test.zip").toURI());
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readAll().build();
        final WorkflowMetadata twm = Workflowalizer.readWorkflow(zipFile, wc);
        assertEquals("Testing_Workflowalizer_360Pre", twm.getName());

        testAnnotations(readWorkflowLines, twm);
        testAuthorInformation(readWorkflowLines, twm);
        testConnections(readWorkflowLines, twm);
        testCreatedBy(readWorkflowLines, twm);
        testCustomDescription(readWorkflowLines, twm);
        testVersion(readWorkflowLines, twm);
        testSvg(1301, 501, twm);

        assertEquals(".artifacts/openapi-input-parameters.json", twm.getArtifacts().get().iterator().next());
        assertTrue(twm.getUnexpectedFileNames().isEmpty());

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
        String testFile = "{\n"
                + "  \"test\": \"value\",\n"
                + "  \"test\": \"two\"\n"
                + "}\n";
        assertEquals("Unexpected workflow configuration", testFile, twm.getWorkflowConfiguration().get());
        assertEquals("Unexpected workflow configuration representation", testFile, twm.getWorkflowConfigurationRepresentation().get());
    }

    /**
     * Test reading and extracting a workflow SVG from a zip.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    public void testReadingSVGFromZip() throws Exception {
        final Path zipFile = Paths.get(Workflowalizer.class.getResource("/workflowalizer-test.zip").toURI());
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(zipFile, wc);
        testSvg(1301, 501, wm);

        final Path svg = workflowDir.resolve("workflow.svg");
        try (final InputStream readSvg = wm.getSvgInputStream().orElse(null)) {
            assertNotNull(readSvg);
            final byte[] bytes = IOUtils.toByteArray(Files.newInputStream(svg));
            final byte[] readBytes = IOUtils.toByteArray(readSvg);
            assertEquals(bytes.length, readBytes.length);
            for (int i = 0; i < bytes.length; i++) {
                assertEquals(bytes[i], readBytes[i]);
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
    public void testTemplateStructure() throws Exception {
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
    public void testTemplateMetadata() throws Exception {
        final TemplateMetadata template = Workflowalizer.readTemplate(templateDir);

        assertEquals(RepositoryItemType.TEMPLATE, template.getType());
        assertEquals(templateDir.getFileName().toString(), template.getName());
        assertTrue(template.getUnexpectedFileNames().isEmpty());

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
    public void testReadingTemplateMetadataAuthorInformation() throws Exception {
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
    public void testReadingTemplateMetadataTemplateInformation() throws Exception {
        final WorkflowalizerConfiguration config = WorkflowalizerConfiguration.builder().build();
        final TemplateMetadata tm = Workflowalizer.readTemplate(templateDir, config);

        testTemplateInformation(readTemplateTemplateKnime, tm);

        assertUOEThrown(tm::getConnections);
        assertUOEThrown(tm::getNodes);
        assertUOEThrown(tm::getUnexpectedFileNames);
    }

    /**
     * Tests reading a template from a zip
     *
     * @throws Exception
     */
    @Test
    public void testReadingTemplateFromZip() throws Exception {
        final Path zipFile = Paths.get(Workflowalizer.class.getResource("/workflowalizer-test.zip").toURI());
        final TemplateMetadata tm = Workflowalizer.readTemplate(zipFile);

        assertEquals("Hierarchical Cluster Assignment", tm.getName());
        assertTrue(tm.getUnexpectedFileNames().isEmpty());

        testAnnotations(readTemplateWorkflowKnime, tm);
        testAuthorInformation(readTemplateWorkflowKnime, tm);
        testConnections(readTemplateWorkflowKnime, tm);
        testCreatedBy(readTemplateTemplateKnime, tm);
        testCustomDescription(readTemplateWorkflowKnime, tm);
        testNodeIds(readTemplateWorkflowKnime, tm, null);
        testTemplateInformation(readTemplateTemplateKnime, tm);
        testVersion(readTemplateTemplateKnime, tm);
    }

    /**
     * Tests trying to read a template as a workflow
     *
     * @throws Exception
     */
    @Test
    public void testReadingTemplateAsWorkflow() throws Exception {
        m_exception.expect(IllegalArgumentException.class);
        m_exception.expectMessage(templateDir + " is a template, not a workflow");
        Workflowalizer.readWorkflow(templateDir);
    }

    /**
     * Tests trying to read a workflow as a template
     *
     * @throws Exception
     */
    @Test
    public void testReadingWorkflowAsTemplate() throws Exception {
        m_exception.expect(IllegalArgumentException.class);
        m_exception.expectMessage(workflowDir + " is a workflow, not a template");
        Workflowalizer.readTemplate(workflowDir);
    }

    // -- Component Template --

    /**
     * Tests that the component template has the correct structure
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testComponentTemplateStructure() throws Exception {
        final TemplateMetadata template = Workflowalizer.readTemplate(componentTemplateDir);
        final Path t = Paths.get(componentTemplateDir.toAbsolutePath().toString(), "workflow.knime");
        assertTrue(template instanceof ComponentMetadata);
        testStructure(template, t);
    }

    /**
     * Tests reading the "workflow" fields of the component template
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentTemplateWorkflowFields() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertTrue(tm instanceof ComponentMetadata);
        assertEquals("Simple-Component", tm.getName());
        assertTrue(tm.getUnexpectedFileNames().isEmpty());

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
    public void testReadingComponentTemplatePorts() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // inports
        final List<ComponentPortInfo> inports = cm.getInPorts();
        assertEquals(2, inports.size());

        assertEquals("1st data table inport", inports.get(0).getDescription().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", inports.get(0).getObjectClass());
        assertEquals("Inport 1", inports.get(0).getName().orElse(""));

        assertEquals("2nd data table inport", inports.get(1).getDescription().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", inports.get(1).getObjectClass());
        assertEquals("Inport 2", inports.get(1).getName().orElse(""));

        // outports
        final List<ComponentPortInfo> outports = cm.getOutPorts();
        assertEquals(3, outports.size());
        assertEquals("Variable outport.", outports.get(0).getDescription().orElse(""));
        assertEquals("org.knime.core.node.port.flowvariable.FlowVariablePortObject", outports.get(0).getObjectClass());
        assertEquals("Outport 1", outports.get(0).getName().orElse(""));

        assertEquals("Data outport.", outports.get(1).getDescription().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", outports.get(1).getObjectClass());
        assertEquals("Outport 2", outports.get(1).getName().orElse(""));

        assertEquals("Image outport.", outports.get(2).getDescription().orElse(""));
        assertEquals("org.knime.core.node.port.image.ImagePortObject", outports.get(2).getObjectClass());
        assertEquals("Outport 3", outports.get(2).getName().orElse(""));
    }

    /**
     * Tests that all "view" (JS views, quickforms, widget, etc.) nodes were read.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentTemplateViewNodes() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // 3 JS views, 1 widget, 1 legacy quickform
        assertEquals(5, cm.getViewNodes().size());
        assertTrue(cm.getViewNodes().contains("org.knime.js.base.node.widget.input.bool.BooleanWidgetNodeFactory"));
        assertTrue(
            cm.getViewNodes().contains("org.knime.js.base.node.quickform.input.bool.BooleanInputQuickFormNodeFactory"));
        assertTrue(cm.getViewNodes().contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory:f822b045"));
        assertTrue(cm.getViewNodes().contains("org.knime.js.base.node.viz.heatmap.HeatMapNodeFactory"));
        assertTrue(cm.getViewNodes().contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory:1ce36c2f"));
    }

    /**
     * Tests reading the component template's description.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentTemplateDescription() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        assertEquals("Simple component description.", cm.getDescription().orElse(""));
    }

    /**
     * Tests reading the component template's dialog.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentTemplateDialog() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // 1 configuration node, 1 quickform node
        assertEquals(1, cm.getDialog().size());
        assertEquals(2, cm.getDialog().get(0).getFields().size());
        assertFalse(cm.getDialog().get(0).getSectionHeader().isPresent());
        assertFalse(cm.getDialog().get(0).getSectionDescription().isPresent());
        assertEquals("Legacy dialog name", cm.getDialog().get(0).getFields().get(0).getName().orElse(""));
        assertEquals("Legacy dialog description", cm.getDialog().get(0).getFields().get(0).getDescription().orElse(""));
        assertEquals("Dialog field name", cm.getDialog().get(0).getFields().get(1).getName().orElse(""));
        assertEquals("Dialog field description", cm.getDialog().get(0).getFields().get(1).getDescription().orElse(""));
    }

    /**
     * Tests reading the component's type, for a component with {@code LoadVersion < 4.1.0}.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentType() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;
        assertFalse(cm.getComponentType().isPresent());
    }

    /**
     * Tests reading the component's icon, for a component with {@code LoadVersion < 4.1.0}.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentIcon() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentTemplateDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;
        assertFalse(cm.getIcon().isPresent());
    }

    /**
     * Tests reading a component template with nested elements (i.e. other components or metanodes).
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentTemplateNested() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = WorkflowalizerTest.class.getResourceAsStream("/component-template-nested.zip")) {
            unzip(is, componentPath.toFile());
        }
        componentPath = componentPath.resolve("Nested Component");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertEquals("I like kitty cats", cm.getDescription().orElse(""));

        // InPorts
        final List<ComponentPortInfo> inports = cm.getInPorts();
        assertEquals(2, inports.size());

        assertEquals("IP1 desc", inports.get(0).getDescription().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", inports.get(0).getObjectClass());
        assertEquals("InPort 1", inports.get(0).getName().orElse(""));

        assertEquals("IP2 desc", inports.get(1).getDescription().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", inports.get(1).getObjectClass());
        assertEquals("InPort 2", inports.get(1).getName().orElse(""));

        // OutPorts
        final List<ComponentPortInfo> outports = cm.getOutPorts();
        assertEquals(2, outports.size());

        assertEquals("OP1 desc", outports.get(0).getDescription().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", outports.get(0).getObjectClass());
        assertEquals("OutPort 1", outports.get(0).getName().orElse(""));

        assertEquals("OP2 desc", outports.get(1).getDescription().orElse(""));
        assertEquals("org.knime.core.node.port.flowvariable.FlowVariablePortObject", outports.get(1).getObjectClass());
        assertEquals("OutPort 2", outports.get(1).getName().orElse(""));

        // Dialog - "top-level" dialog elements only
        assertEquals(1, cm.getDialog().size());
        assertEquals(1, cm.getDialog().get(0).getFields().size());
        assertEquals("Field Name", cm.getDialog().get(0).getFields().get(0).getName().orElse(""));
        assertEquals("Field Description", cm.getDialog().get(0).getFields().get(0).getDescription().orElse(""));

        // Views - including all nested views
        assertEquals(2, cm.getViewNodes().size());
        assertTrue(cm.getViewNodes().contains("org.knime.js.base.node.viz.heatmap.HeatMapNodeFactory"));
        assertTrue(cm.getViewNodes().contains("org.knime.js.base.node.viz.pagedTable.PagedTableViewNodeFactory"));

        // Type
        assertFalse(cm.getComponentType().isPresent());

        // Icon
        assertFalse(cm.getIcon().isPresent());
    }

    /**
     * Tests reading a component template which has no ports.
     *
     * @throws Exception
     * @since 5.13
     */
    @Test
    public void testReadingComponentTemplateNoPorts() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is = WorkflowalizerTest.class.getResourceAsStream("/component-template-no-ports.zip")) {
            unzip(is, componentPath.toFile());
        }
        componentPath = componentPath.resolve("No Connections");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertFalse(cm.getDescription().isPresent());

        // Ports
        assertTrue(cm.getInPorts().isEmpty());
        assertTrue(cm.getOutPorts().isEmpty());

        // Dialog - "top-level" dialog elements only
        assertTrue(cm.getDialog().isEmpty());

        // Views - including all nested views
        assertTrue(cm.getViewNodes().isEmpty());

        // Type
        assertFalse(cm.getComponentType().isPresent());

        // Icon
        assertFalse(cm.getIcon().isPresent());
    }

    /**
     * Tests reading a component template which has ports but without descriptions.
     *
     * @throws Exception if an error occurs
     * @since 5.13
     */
    @Test
    public void testReadingComponentNoPortDescriptions() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is =
            WorkflowalizerTest.class.getResourceAsStream("/component-no-port-descriptions.zip")) {
            unzip(is, componentPath.toFile());
        }
        componentPath = componentPath.resolve("No Descriptions");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertThat("Unexpected metadata subtype read", tm, is(instanceOf(ComponentMetadata.class)));
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertFalse(cm.getDescription().isPresent());

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
    public void testReadingComponentTemplateMissingConnections() throws Exception {
        Path componentPath = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (final InputStream is =
            WorkflowalizerTest.class.getResourceAsStream("/component-template-missing-connection.zip")) {
            unzip(is, componentPath.toFile());
        }
        componentPath = componentPath.resolve("Missing Connection");

        final TemplateMetadata tm = Workflowalizer.readTemplate(componentPath);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        // Description
        assertFalse(cm.getDescription().isPresent());

        // InPorts
        final List<ComponentPortInfo> inports = cm.getInPorts();
        assertEquals(2, inports.size());

        assertFalse(inports.get(0).getDescription().isPresent());
        assertEquals("Port 1", inports.get(0).getName().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", inports.get(0).getObjectClass());

        assertFalse(inports.get(1).getDescription().isPresent());
        assertEquals("Port 2", inports.get(1).getName().orElse(""));
        assertEquals("org.knime.core.node.BufferedDataTable", inports.get(1).getObjectClass());

        // OutPorts
        final List<ComponentPortInfo> outports = cm.getOutPorts();
        assertEquals(1, outports.size());

        assertFalse(outports.get(0).getDescription().isPresent());
        assertEquals("Port 1", outports.get(0).getName().orElse(""));
        assertEquals("org.knime.core.node.port.image.ImagePortObject", outports.get(0).getObjectClass());

        // Dialog - "top-level" dialog elements only
        assertTrue(cm.getDialog().isEmpty());

        // Views - including all nested views
        assertEquals(1, cm.getViewNodes().size());
        assertEquals("org.knime.dynamic.js.v30.DynamicJSNodeFactory:1d2c1a0e", cm.getViewNodes().get(0));

        // Type
        assertFalse(cm.getComponentType().isPresent());

        // Icon
        assertFalse(cm.getIcon().isPresent());
    }

    // -- Workflow group --

    /**
     * Tests reading a workflow group
     *
     * @throws Exception
     */
    @Test
    public void testReadingWorkflowGroup() throws Exception {
        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(workflowGroupFile);
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, wsm.getType());

        testWorkflowSetMeta(readWorkflowGroupLines, wsm);
    }

    /**
     * Tests reading a workflow group with a title that has over 20 words
     *
     * @throws Exception
     */
    @Test
    public void testReadingWorkflowGroupWithLongTitle() throws Exception {
        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(workflowGroupFileLongTitle);
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, wsm.getType());

        testWorkflowSetMeta(readWorkflowGroupLinesLongTitle, wsm);
    }

    /**
     * Tests reading a workflow group from a directory path.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    public void testReadingWorkflowGroupDir() throws Exception {
        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(workflowGroupFile.getParent());
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, wsm.getType());

        testWorkflowSetMeta(readWorkflowGroupLines, wsm);
    }

    /**
     * Tests reading a zipped workflow group
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    public void testReadingWorkflowGroupZip() throws Exception {
        final Path zipFile = Paths.get(Workflowalizer.class.getResource("/workflowalizer-test.zip").toURI());
        final WorkflowGroupMetadata wgm = Workflowalizer.readWorkflowGroup(zipFile);
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, wgm.getType());
        assertFalse(wgm.getAuthor().isPresent());
        assertFalse(wgm.getDescription().isPresent());
        assertFalse(wgm.getTitle().isPresent());
        assertFalse(wgm.getTags().isPresent());
        assertFalse(wgm.getLinks().isPresent());
    }

    // -- General repository item --

    /**
     * Tests the method for reading a generic repository item on the file system.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    public void testReadingRepositoryItem() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        RepositoryItemMetadata rip = Workflowalizer.readRepositoryItem(workflowDir, wc);
        assertEquals(RepositoryItemType.WORKFLOW, rip.getType());
        assertTrue(rip instanceof WorkflowMetadata);

        rip = Workflowalizer.readRepositoryItem(templateDir, wc);
        assertEquals(RepositoryItemType.TEMPLATE, rip.getType());
        assertTrue(rip instanceof TemplateMetadata);

        rip = Workflowalizer.readRepositoryItem(workflowGroupFile.getParent(), wc);
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, rip.getType());
        assertTrue(rip instanceof WorkflowGroupMetadata);

        try {
            rip = Workflowalizer.readRepositoryItem(nodeDir, wc);
            assertTrue("Expected exception was not thrown", false);
        } catch (final IllegalArgumentException ex) {
            assertTrue(ex.getMessage().startsWith("No template, workflow, or workflow group found"));
        }
    }

    /**
     * Tests the method for reading a generic zipped repository item.
     *
     * @throws Exception
     * @since 5.11
     */
    @Test
    public void testReadingRepositoryItemZip() throws Exception {
        // Test reading group
        final Path zipFile = Paths.get(Workflowalizer.class.getResource("/workflowalizer-test.zip").toURI());
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final RepositoryItemMetadata rip = Workflowalizer.readRepositoryItem(zipFile, wc);

        assertEquals(RepositoryItemType.WORKFLOW_GROUP, rip.getType());
        assertTrue(rip instanceof WorkflowGroupMetadata);

        // Test that is the top-level group
        final WorkflowGroupMetadata wgm = (WorkflowGroupMetadata)rip;
        assertFalse(wgm.getAuthor().isPresent());
        assertFalse(wgm.getDescription().isPresent());
        assertFalse(wgm.getTitle().isPresent());
        assertFalse(wgm.getTags().isPresent());
        assertFalse(wgm.getLinks().isPresent());

        // Test reading workflow
        final Path zipWorkflow = Paths.get(Workflowalizer.class.getResource("/simple-workflow.zip").toURI());
        final RepositoryItemMetadata workflow = Workflowalizer.readRepositoryItem(zipWorkflow, wc);

        assertEquals(RepositoryItemType.WORKFLOW, workflow.getType());
        assertTrue(workflow instanceof WorkflowMetadata);

        // Test reading component
        final Path zipComponent = Paths.get(Workflowalizer.class.getResource("/component-template-simple.zip").toURI());
        final RepositoryItemMetadata component = Workflowalizer.readRepositoryItem(zipComponent, wc);

        assertEquals(RepositoryItemType.TEMPLATE, component.getType());
        assertTrue(component instanceof ComponentMetadata);

        // Test zip with no groups, workflows, or components
        try {
            final Path noRepoItems = Paths.get(Workflowalizer.class.getResource("/noRepoItems.zip").toURI());
            Workflowalizer.readRepositoryItem(noRepoItems, wc);
            assertTrue("No exception thrown for zip archive containing no repository items", false);
        } catch (final IllegalArgumentException ex) {
            assertTrue("Incorrect exception message: " + ex.getMessage(),
                ex.getMessage().startsWith("No template," + " workflow, or workflow group found in zip file at: "));
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
    public void testReadingWorkflowWithTestNodes() throws Exception {
        final Path zipFile = Paths.get(WorkflowalizerTest.class.getResource("/Testing_nodes_wkfl.knwf").toURI());
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(zipFile, wc);

        for (NodeMetadata nm : wm.getNodes()) {
            if ((nm instanceof NativeNodeMetadata)) {
                NativeNodeMetadata nn = (NativeNodeMetadata)nm;
                if (nn.getFactoryName().equals("org.knime.testing.node.disturber.DisturberNodeFactory") || nn
                    .getFactoryName().equals("org.knime.testing.internal.nodes.differ.DifferenceCheckerNodeFactory")) {
                    assertEquals("Testing node '" + nn.getFactoryName() + "' has incorrect extension symbolic name",
                        "org.knime.features.testing.application",
                        nn.getNodeAndBundleInformation().getFeatureSymbolicName().orElse(null));
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
    public void testComponentPortsWithoutText() throws Exception {
        Path zipFile = Paths.get(Workflowalizer.class.getResource("/Component-No-Port-Text.knwf").toURI());
        WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        TemplateMetadata tm = Workflowalizer.readTemplate(zipFile, wc);

        assertTrue("Template was not read as component", tm instanceof ComponentMetadata);
        ComponentMetadata cm = (ComponentMetadata)tm;

        assertEquals("Unexpected number of inports", cm.getInPorts().size(), 1);
        assertEquals("Unexpected number of outports", cm.getOutPorts().size(), 1);

        ComponentPortInfo inport = cm.getInPorts().get(0);
        assertEquals("Unexpected description for inport", inport.getDescription(), Optional.empty());
        assertEquals("Unexpected name for inport", inport.getName(), Optional.empty());
        assertEquals("Unexpected object class for inport", inport.getObjectClass(),
            "org.knime.core.node.BufferedDataTable");

        ComponentPortInfo outport = cm.getOutPorts().get(0);
        assertEquals("Unexpected description for outport", outport.getDescription(), Optional.empty());
        assertEquals("Unexpected name for outport", outport.getName(), Optional.empty());
        assertEquals("Unexpected object class for outport", outport.getObjectClass(),
            "org.knime.core.node.BufferedDataTable");
    }
}
