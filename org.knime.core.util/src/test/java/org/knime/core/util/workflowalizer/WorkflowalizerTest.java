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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.core.util.PathUtils;
import org.knime.core.util.Version;
import org.knime.core.util.XMLUtils;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;
import org.knime.core.util.workflowalizer.RepositoryItemMetadata.RepositoryItemType;

/**
 * Tests for {@link Workflowalizer}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public class WorkflowalizerTest {
    private static Path m_workspaceDir;
    private static Path m_workflowDir;
    private static Path m_nodeDir;
    private static Path m_templateDir;
    private static Path m_workflowGroupFile;
    private static List<String> m_readWorkflowLines;
    private static List<String> m_readNodeLines;
    private static List<String> m_readTemplateWorkflowKnime;
    private static List<String> m_readTemplateTemplateKnime;
    private static List<String> m_readWorkflowSetLines;
    private static List<String> m_readWorkflowGroupLines;

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
        m_workspaceDir = PathUtils.createTempDir(WorkflowalizerTest.class.getName());
        try (InputStream is = WorkflowalizerTest.class.getResourceAsStream("/workflowalizer-test.zip")) {
            unzip(is, m_workspaceDir.toFile());
        }
        m_workflowDir = new File(m_workspaceDir.toFile(), "workflowalizer-test/Testing_Workflowalizer_360Pre").toPath();
        m_readWorkflowLines = Files.readAllLines(new File(m_workflowDir.toFile(), "workflow.knime").toPath());

        m_nodeDir = new File(m_workflowDir.toFile(), "Column Splitter (#10)").toPath();
        m_readNodeLines = Files.readAllLines(new File(m_nodeDir.toFile(), "settings.xml").toPath());

        m_templateDir =
            new File(m_workspaceDir.toFile(), "workflowalizer-test/Hierarchical Cluster Assignment").toPath();
        m_readTemplateWorkflowKnime =
            Files.readAllLines(Paths.get(m_templateDir.toAbsolutePath().toString(), "workflow.knime"));
        m_readTemplateTemplateKnime =
            Files.readAllLines(Paths.get(m_templateDir.toAbsolutePath().toString(), "template.knime"));

        final Path workflowSetMetaPath = m_workflowDir.resolve("workflowset.meta");
        m_readWorkflowSetLines = Files.readAllLines(workflowSetMetaPath);

        m_workflowGroupFile = m_workspaceDir.resolve("workflowalizer-test/test_group/workflowset.meta");
        m_readWorkflowGroupLines = Files.readAllLines(m_workflowGroupFile);
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
        final IWorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir);
        final Path workflowPath = new File(m_workflowDir.toFile(), "workflow.knime").toPath();

        testStructure(wkfMd, workflowPath);
    }

    /**
     * Reads a top-level (i.e. not metanode) workflow, and tests that all fields were set and read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testTopLevelWorkflowMetadata() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir);

        assertEquals(RepositoryItemType.WORKFLOW, wkfMd.getType());
        assertEquivalentAnnotations(readAnnotations(m_readWorkflowLines), wkfMd.getAnnotations());
        assertEquals(readAuthorInformation(m_readWorkflowLines), wkfMd.getAuthorInformation());
        assertEquivalentConnections(readConnectionIds(m_readWorkflowLines), wkfMd.getConnections());
        assertEquals(readCreatedBy(m_readWorkflowLines), wkfMd.getCreatedBy());
        assertEquals(readCustomDescription(m_readWorkflowLines), wkfMd.getCustomDescription());
        assertEquals(m_workflowDir.getFileName().toString(), wkfMd.getName());

        final List<Integer> nodeIds = readNodeIds(m_readWorkflowLines);
        final List<NodeMetadata> nodes = wkfMd.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
        }
        assertEquals(readVersion(m_readWorkflowLines), wkfMd.getVersion());

        assertTrue(wkfMd.getWorkflowSvg().isPresent());
        assertEquals(1301, wkfMd.getWorkflowSvg().get().getWidth().intValue());
        assertEquals(501, wkfMd.getWorkflowSvg().get().getHeight().intValue());
        assertEquals(m_workflowDir
            .relativize(new File(m_workflowDir.toFile(), ".artifacts/openapi-input-parameters.json").toPath())
            .toString(), wkfMd.getArtifacts().get().iterator().next());
        assertTrue(wkfMd.getUnexpectedFileNames().isEmpty());
        assertTrue(wkfMd.getWorkflowSetMetadata().isPresent());
        assertEquals(parseWorkflowSetMeta("Author", m_readWorkflowSetLines),
            wkfMd.getWorkflowSetMetadata().get().getAuthor().orElse(null));

        final String comments = parseWorkflowSetMeta("Comments", m_readWorkflowSetLines);
        assertTrue(wkfMd.getWorkflowSetMetadata().isPresent());
        final WorkflowSetMeta wsm = wkfMd.getWorkflowSetMetadata().get();
        assertFalse(wsm.getTitle().isPresent());
        assertEquals(comments, wsm.getDescription().orElse(null));
        assertTrue(wsm.getLinks().get().size() == 0);
        assertTrue(wsm.getTags().get().size() == 0);
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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertTrue(wkfMd.getWorkflowSvg().isPresent());
        assertEquals(1301, wkfMd.getWorkflowSvg().get().getWidth().intValue());
        assertEquals(501, wkfMd.getWorkflowSvg().get().getHeight().intValue());

        final Path svg = m_workflowDir.resolve("workflow.svg");
        final File readSvg = wkfMd.getSvgFile().orElse(null);
        assertNotNull(readSvg);
        final List<String> lines = Files.readAllLines(svg);
        final List<String> readLines = Files.readAllLines(readSvg.toPath());
        assertEquals(lines.size(), readLines.size());
        for (int i = 0; i < lines.size(); i++) {
            assertEquals(lines.get(i), readLines.get(i));
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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        final File test = new File(m_workflowDir.toFile(), ".artifacts/openapi-input-parameters.json");
        assertTrue(wkfMd.getArtifacts().isPresent());
        assertEquals(1, wkfMd.getArtifacts().get().size());
        assertEquals(m_workflowDir.relativize(test.toPath()).toString(), wkfMd.getArtifacts().get().iterator().next());

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
            dataDir = new File(m_workflowDir.toFile(), "data");
            dataDir.mkdirs();
            dataFile = new File(dataDir, "myData.txt");
            dataFile.createNewFile();
            subDataDir = new File(dataDir, "subData");
            subDataDir.mkdirs();
            subDataFile = new File(subDataDir, "moreData.txt");
            subDataFile.createNewFile();
            emptyDir = new File(m_workflowDir.toFile(), "tmp");
            emptyDir.mkdirs();

            final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readUnexpectedFiles().build();
            final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
            final Collection<String> unexpectedFiles = wkfMd.getUnexpectedFileNames();
            assertEquals(2, unexpectedFiles.size());
            assertTrue(unexpectedFiles.contains(m_workflowDir.relativize(dataFile.toPath()).toString()));
            assertTrue(unexpectedFiles.contains(m_workflowDir.relativize(subDataFile.toPath()).toString()));

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertEquivalentAnnotations(readAnnotations(m_readWorkflowLines), wkfMd.getAnnotations());

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertEquals(readAuthorInformation(m_readWorkflowLines), wkfMd.getAuthorInformation());

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertEquivalentConnections(readConnectionIds(m_readWorkflowLines), wkfMd.getConnections());

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertEquals(readCreatedBy(m_readWorkflowLines), wkfMd.getCreatedBy());

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertEquals(readCustomDescription(m_readWorkflowLines), wkfMd.getCustomDescription());

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertEquals(m_workflowDir.getFileName().toString(), wkfMd.getName());

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        final List<NodeMetadata> nodes = wkfMd.getNodes();
        final List<Integer> nodeIds = readNodeIds(m_readWorkflowLines);
        assertEquals(nodeIds.size(), nodes.size());
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
        }

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertEquals(readVersion(m_readWorkflowLines), wkfMd.getVersion());

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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        assertTrue(wkfMd.getWorkflowSetMetadata().isPresent());
        assertEquals(parseWorkflowSetMeta("Author", m_readWorkflowSetLines),
            wkfMd.getWorkflowSetMetadata().get().getAuthor().orElse(null));
        final String comments = parseWorkflowSetMeta("Comments", m_readWorkflowSetLines);
        assertTrue(wkfMd.getWorkflowSetMetadata().isPresent());
        final WorkflowSetMeta wsm = wkfMd.getWorkflowSetMetadata().get();
        assertFalse(wsm.getTitle().isPresent());
        assertEquals(comments, wsm.getDescription().orElse(null));
        assertTrue(wsm.getLinks().get().size() == 0);
        assertTrue(wsm.getTags().get().size() == 0);

        assertUOEThrown(wkfMd::getConnections);
        assertUOEThrown(wkfMd::getNodes);
        assertUOEThrown(wkfMd::getUnexpectedFileNames);
    }

    /**
     * Test reading workflow credentials
     *
     * @throws Exception
     */
    @Test
    public void testReadingWorkflowCredentials() throws Exception {
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
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
        final Path workflowPath = m_workspaceDir.resolve("workflowalizer-test/test_group/test1");
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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertEquals(readAnnotationText(m_readNodeLines), nativeNode.getAnnotationText());
        assertEquals(readCustomNodeDescription(m_readNodeLines), nativeNode.getCustomNodeDescription());
        assertTrue(readNodeConfiguration(new File(m_nodeDir.toFile(), "settings.xml")).get()
            .isIdentical(nativeNode.getNodeConfiguration().get()));
        assertEquals(readNodeAndBundleInformation(false, m_readNodeLines), nativeNode.getNodeAndBundleInformation());
        assertEquals(NodeType.NATIVE_NODE, node.getType());
    }

    /**
     * Tests the a node within a metanode is read correctly when the top-level workflow is read.
     *
     * @throws Exception
     */
    @Test
    public void testReadingNodeInMetanode() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir);
        final MetanodeMetadata mtnMd =
            (MetanodeMetadata)wkfMd.getNodes().stream().filter(n -> n.getType().equals(NodeType.METANODE)).findFirst().get();
        final NativeNodeMetadata node =
            (NativeNodeMetadata)mtnMd.getNodes().stream().filter(n -> n.getNodeId().equals(mtnMd.getNodeId() + ":2")).findFirst().get();
        final Path nodePath =
            new File(m_workflowDir.toFile(), "Hierarchical (#15)/Numeric Distances (#2)/settings.xml").toPath();
        final List<String> readLines = Files.readAllLines(nodePath);

        // No need to test ID, since the list was filtered on that
        assertEquals(readAnnotationText(readLines), node.getAnnotationText());
        assertEquals(readCustomNodeDescription(readLines), node.getCustomNodeDescription());
        assertTrue(readNodeConfiguration(nodePath.toFile()).get().isIdentical(node.getNodeConfiguration().get()));
        assertEquals(readNodeAndBundleInformation(false, readLines), node.getNodeAndBundleInformation());
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
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertEquals(readAnnotationText(m_readNodeLines), nativeNode.getAnnotationText());
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    /**
     * Test that the node's custom description is read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testReadingCustomNodeDescription() throws Exception {
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertEquals(readCustomNodeDescription(m_readNodeLines), nativeNode.getCustomNodeDescription());
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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertTrue(readNodeConfiguration(new File(m_nodeDir.toFile(), "settings.xml")).get()
            .isIdentical(nativeNode.getNodeConfiguration().get()));
    }

    /**
     * Test that the node and bundle information is read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testReadingNodeAndBundleInformation() throws Exception {
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
        final NodeMetadata node = wkfMd.getNodes().stream().filter(n -> n.getNodeId().equals("10")).findFirst().get();

        assertTrue(node instanceof NativeNodeMetadata);
        final NativeNodeMetadata nativeNode = (NativeNodeMetadata)node;

        assertEquals(readNodeAndBundleInformation(false, m_readNodeLines), nativeNode.getNodeAndBundleInformation());
        assertUOEThrown(nativeNode::getNodeConfiguration);
    }

    /**
     * Test that the node ID is read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testReadingNodeId() throws Exception {
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);

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
        final WorkflowalizerConfiguration wc =
            WorkflowalizerConfiguration.builder().readNodes().build();
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir, wc);
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
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir);
        final MetanodeMetadata mtnMtd =
            (MetanodeMetadata)wkfMd.getNodes().stream().filter(n -> n.getType().equals(NodeType.METANODE)).findFirst().get();
        final List<String> readMetaNodeWorkflowLines = Files.readAllLines(
            new File(m_workflowDir.toFile(), "Hierarchical (#15)/workflow.knime").toPath(), StandardCharsets.UTF_8);

        // Read workflow fields
        assertEquivalentAnnotations(readAnnotations(readMetaNodeWorkflowLines), mtnMtd.getAnnotations());
        assertEquivalentConnections(readConnectionIds(readMetaNodeWorkflowLines), mtnMtd.getConnections());
        assertEquals(readCreatedBy(readMetaNodeWorkflowLines), mtnMtd.getCreatedBy());
        assertEquals(readCustomDescription(readMetaNodeWorkflowLines), mtnMtd.getCustomDescription());
        assertEquals(readName(readMetaNodeWorkflowLines), mtnMtd.getName());

        final List<Integer> nodeIds = readNodeIds(readMetaNodeWorkflowLines);
        final List<NodeMetadata> nodes = mtnMtd.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
            assertEquals("15" + ":" + id, node.getNodeId());
        }
        assertEquals(readVersion(readMetaNodeWorkflowLines), mtnMtd.getVersion());
        assertTrue(mtnMtd.getUnexpectedFileNames().isEmpty());

        // Read node fields
        assertEquals("15", mtnMtd.getNodeId());
        assertEquals(readTemplateLink(readMetaNodeWorkflowLines), mtnMtd.getTemplateLink());
        assertEquals(readAnnotationText(readMetaNodeWorkflowLines), mtnMtd.getAnnotationText());
    }

    /**
     * Tests that the WrappedMetaNode was read correctly (both WorkflowMetadata and NodeMetadata).
     *
     * @throws Exception
     */
    @Test
    public void testReadingWrappedMetaNode() throws Exception {
        final WorkflowMetadata wkfMd = Workflowalizer.readWorkflow(m_workflowDir);
        final SubnodeMetadata mtnMtd =
            (SubnodeMetadata)wkfMd.getNodes().stream().filter(n -> n.getType().equals(NodeType.SUBNODE)).findFirst().get();
        final List<String> readMetaNodeWorkflowLines = Files.readAllLines(
            new File(m_workflowDir.toFile(), "Format Outpu (#16)/workflow.knime").toPath(), StandardCharsets.UTF_8);
        final List<String> readMetaNodeNodeLines = Files.readAllLines(
            new File(m_workflowDir.toFile(), "Format Outpu (#16)/settings.xml").toPath(), StandardCharsets.UTF_8);

        // Read workflow fields
        assertEquivalentAnnotations(readAnnotations(readMetaNodeWorkflowLines), mtnMtd.getAnnotations());
        assertEquivalentConnections(readConnectionIds(readMetaNodeWorkflowLines), mtnMtd.getConnections());
        assertEquals(readCreatedBy(readMetaNodeWorkflowLines), mtnMtd.getCreatedBy());
        assertEquals(readCustomDescription(readMetaNodeWorkflowLines), mtnMtd.getCustomDescription());
        assertEquals(readName(readMetaNodeWorkflowLines), mtnMtd.getName());

        final List<Integer> nodeIds = readNodeIds(readMetaNodeWorkflowLines);
        final List<NodeMetadata> nodes = mtnMtd.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
            assertEquals("16" + ":" + id, node.getNodeId());
        }
        assertEquals(readVersion(readMetaNodeWorkflowLines), mtnMtd.getVersion());
        assertTrue(mtnMtd.getUnexpectedFileNames().isEmpty());

        // Read node fields
        assertEquals(readAnnotationText(readMetaNodeNodeLines), mtnMtd.getAnnotationText());
        assertEquals(readCustomNodeDescription(readMetaNodeNodeLines), mtnMtd.getCustomNodeDescription());
        assertTrue(readNodeConfiguration(new File(m_workflowDir.toFile(), "Format Outpu (#16)/settings.xml")).get()
            .isIdentical(mtnMtd.getNodeConfiguration().get()));
        assertEquals("16", mtnMtd.getNodeId());
        assertEquals(readTemplateLink(readMetaNodeNodeLines), mtnMtd.getTemplateLink());
    }

    // -- Test reading from zip --

    /**
     * Test reading a workflow from a zip file
     *
     * @throws Exception
     */
    @Test
    @SuppressWarnings("null")
    public void testReadingZipWorkflow() throws Exception {
        // This zip contains multiple workflows, but only the first should be read
        final Path zipFile = Paths.get(Workflowalizer.class.getResource("/workflowalizer-test.zip").toURI());
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().readAll().build();
        final WorkflowMetadata twm = Workflowalizer.readWorkflow(zipFile, wc);

        assertEquivalentAnnotations(readAnnotations(m_readWorkflowLines), twm.getAnnotations());
        assertEquals(readAuthorInformation(m_readWorkflowLines), twm.getAuthorInformation());
        assertEquivalentConnections(readConnectionIds(m_readWorkflowLines), twm.getConnections());
        assertEquals(readCreatedBy(m_readWorkflowLines), twm.getCreatedBy());
        assertEquals(readCustomDescription(m_readWorkflowLines), twm.getCustomDescription());
        assertEquals("Testing_Workflowalizer_360Pre", twm.getName());
        assertEquals(readVersion(m_readWorkflowLines), twm.getVersion());
        assertTrue(twm.getWorkflowSvg().isPresent());
        assertEquals(1301, twm.getWorkflowSvg().get().getWidth().intValue());
        assertEquals(501, twm.getWorkflowSvg().get().getHeight().intValue());
        assertEquals(".artifacts/openapi-input-parameters.json",
            twm.getArtifacts().get().iterator().next());
        assertTrue(twm.getUnexpectedFileNames().isEmpty());

        // Top-level workflow nodes
        List<Integer> nodeIds = readNodeIds(m_readWorkflowLines);
        List<NodeMetadata> nodes = twm.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        int nativeNodeCount = 0;
        int subnodeCount = 0;
        int metanodeCount = 0;
        MetanodeMetadata mm = null;
        SubnodeMetadata sm = null;
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
            if (node.getType().equals(NodeType.NATIVE_NODE)) {
                nativeNodeCount++;
            }
            if (node.getType().equals(NodeType.SUBNODE)) {
                subnodeCount++;
                sm = (SubnodeMetadata)node;
            }
            if (node.getType().equals(NodeType.METANODE)) {
                metanodeCount++;
                mm = (MetanodeMetadata)node;
            }
        }
        assertEquals(7, nativeNodeCount);
        assertEquals(1, subnodeCount);
        assertEquals(1, metanodeCount);

        // Metanode nodes
        final List<String> readMetaNodeWorkflowLines = Files.readAllLines(
            new File(m_workflowDir.toFile(), "Hierarchical (#15)/workflow.knime").toPath(), StandardCharsets.UTF_8);
        nodeIds = readNodeIds(readMetaNodeWorkflowLines);
        nodes = mm.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        nativeNodeCount = 0;
        subnodeCount = 0;
        metanodeCount = 0;
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
            if (node.getType().equals(NodeType.NATIVE_NODE)) {
                nativeNodeCount++;
            }
            if (node.getType().equals(NodeType.SUBNODE)) {
                subnodeCount++;
            }
            if (node.getType().equals(NodeType.METANODE)) {
                metanodeCount++;
            }
        }
        assertEquals(3, nativeNodeCount);
        assertEquals(0, subnodeCount);
        assertEquals(0, metanodeCount);

        // Subnode nodes
        final List<String> readSubNodeWorkflowLines = Files.readAllLines(
            new File(m_workflowDir.toFile(), "Format Outpu (#16)/workflow.knime").toPath(), StandardCharsets.UTF_8);
        nodeIds = readNodeIds(readSubNodeWorkflowLines);
        nodes = sm.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        nativeNodeCount = 0;
        subnodeCount = 0;
        metanodeCount = 0;
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
            if (node.getType().equals(NodeType.NATIVE_NODE)) {
                nativeNodeCount++;
            }
            if (node.getType().equals(NodeType.SUBNODE)) {
                subnodeCount++;
            }
            if (node.getType().equals(NodeType.METANODE)) {
                metanodeCount++;
            }
        }
        assertEquals(6, nativeNodeCount);
        assertEquals(0, subnodeCount);
        assertEquals(0, metanodeCount);
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

        assertTrue(wm.getWorkflowSvg().isPresent());
        assertEquals(1301, wm.getWorkflowSvg().get().getWidth().intValue());
        assertEquals(501, wm.getWorkflowSvg().get().getHeight().intValue());

        final Path svg = m_workflowDir.resolve("workflow.svg");
        final File readSvg = wm.getSvgFile().orElse(null);
        assertNotNull(readSvg);
        final List<String> lines = Files.readAllLines(svg);
        final List<String> readLines = Files.readAllLines(readSvg.toPath());
        assertEquals(lines.size(), readLines.size());
        for (int i = 0; i < lines.size(); i++) {
            assertEquals(lines.get(i), readLines.get(i));
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
        final TemplateMetadata template = Workflowalizer.readTemplate(m_templateDir);
        final Path t = Paths.get(m_templateDir.toAbsolutePath().toString(), "workflow.knime");
        testStructure(template, t);
    }

    /**
     * Tests that the template fields are read correctly.
     *
     * @throws Exception
     */
    @Test
    public void testTemplateMetadata() throws Exception {
        final TemplateMetadata template = Workflowalizer.readTemplate(m_templateDir);

        assertEquals(RepositoryItemType.TEMPLATE, template.getType());
        assertEquivalentAnnotations(readAnnotations(m_readTemplateWorkflowKnime), template.getAnnotations());
        assertEquals(readAuthorInformation(m_readTemplateWorkflowKnime), template.getAuthorInformation());
        assertEquivalentConnections(readConnectionIds(m_readTemplateWorkflowKnime), template.getConnections());
        assertEquals(readCreatedBy(m_readTemplateTemplateKnime), template.getCreatedBy());
        assertEquals(readCustomDescription(m_readTemplateWorkflowKnime), template.getCustomDescription());
        assertEquals(m_templateDir.getFileName().toString(), template.getName());

        final List<Integer> nodeIds = readNodeIds(m_readTemplateWorkflowKnime);
        final List<NodeMetadata> nodes = template.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
        }
        assertEquals(readTemplateInformation(m_readTemplateTemplateKnime), template.getTemplateInformation());
        assertEquals(readVersion(m_readTemplateTemplateKnime), template.getVersion());
        assertTrue(template.getUnexpectedFileNames().isEmpty());
    }

    /**
     * Test reading a template's author information only
     *
     * @throws Exception
     */
    @Test
    public void testReadingTemplateMetadataAuthorInformation() throws Exception {
        final WorkflowalizerConfiguration config =
            WorkflowalizerConfiguration.builder().build();
        final TemplateMetadata tm = Workflowalizer.readTemplate(m_templateDir, config);

        assertEquals(readAuthorInformation(m_readTemplateWorkflowKnime), tm.getAuthorInformation());

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
        final WorkflowalizerConfiguration config =
            WorkflowalizerConfiguration.builder().build();
        final TemplateMetadata tm = Workflowalizer.readTemplate(m_templateDir, config);

        assertEquals(readTemplateInformation(m_readTemplateTemplateKnime), tm.getTemplateInformation());

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

        assertEquivalentAnnotations(readAnnotations(m_readTemplateWorkflowKnime), tm.getAnnotations());
        assertEquals(readAuthorInformation(m_readTemplateWorkflowKnime), tm.getAuthorInformation());
        assertEquivalentConnections(readConnectionIds(m_readTemplateWorkflowKnime), tm.getConnections());
        assertEquals(readCreatedBy(m_readTemplateTemplateKnime), tm.getCreatedBy());
        assertEquals(readCustomDescription(m_readTemplateWorkflowKnime), tm.getCustomDescription());
        assertEquals("Hierarchical Cluster Assignment", tm.getName());

        final List<Integer> nodeIds = readNodeIds(m_readTemplateWorkflowKnime);
        final List<NodeMetadata> nodes = tm.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
        }
        assertEquals(readTemplateInformation(m_readTemplateTemplateKnime), tm.getTemplateInformation());
        assertEquals(readVersion(m_readTemplateTemplateKnime), tm.getVersion());
        assertTrue(tm.getUnexpectedFileNames().isEmpty());
    }

    /**
     * Tests trying to read a template as a workflow
     *
     * @throws Exception
     */
    @Test
    public void testReadingTemplateAsWorkflow()
        throws Exception {
        m_exception.expect(IllegalArgumentException.class);
        m_exception.expectMessage(m_templateDir + " is a template, not a workflow");
        Workflowalizer.readWorkflow(m_templateDir);
    }

    /**
     * Tests trying to read a workflow as a template
     *
     * @throws Exception
     */
    @Test
    public void testReadingWorkflowAsTemplate() throws Exception {
        m_exception.expect(IllegalArgumentException.class);
        m_exception.expectMessage(m_workflowDir + " is a workflow, not a template");
        Workflowalizer.readTemplate(m_workflowDir);
    }

    // -- Workflow group --

    /**
     * Tests reading a workflow group
     *
     * @throws Exception
     */
    @Test
    public void testReadingWorkflowGroup() throws Exception {
        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(m_workflowGroupFile);
        assertEquals(parseWorkflowSetMeta("Author", m_readWorkflowGroupLines), wsm.getAuthor().orElse(null));
        final String comments = parseWorkflowSetMeta("Comments", m_readWorkflowGroupLines);
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, wsm.getType());
        assertFalse(wsm.getTitle().isPresent());
        assertEquals(comments, wsm.getDescription().orElse(null));
        assertTrue(wsm.getLinks().get().size() == 0);
        assertTrue(wsm.getTags().get().size() == 0);
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
        RepositoryItemMetadata rip = Workflowalizer.readRepositoryItem(m_workflowDir, wc);
        assertEquals(RepositoryItemType.WORKFLOW, rip.getType());
        assertTrue(rip instanceof WorkflowMetadata);

        rip = Workflowalizer.readRepositoryItem(m_templateDir, wc);
        assertEquals(RepositoryItemType.TEMPLATE, rip.getType());
        assertTrue(rip instanceof TemplateMetadata);

        rip = Workflowalizer.readRepositoryItem(m_workflowGroupFile.getParent(), wc);
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, rip.getType());
        assertTrue(rip instanceof WorkflowGroupMetadata);

        try {
            rip = Workflowalizer.readRepositoryItem(m_nodeDir, wc);
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
        final Path zipFile = Paths.get(Workflowalizer.class.getResource("/workflowalizer-test.zip").toURI());
        final WorkflowalizerConfiguration wc = WorkflowalizerConfiguration.builder().build();
        final RepositoryItemMetadata rip = Workflowalizer.readRepositoryItem(zipFile, wc);

        assertEquals(RepositoryItemType.WORKFLOW_GROUP, rip.getType());
        assertTrue(rip instanceof WorkflowGroupMetadata);

        // Test that is the top-level group
        final WorkflowGroupMetadata wgm = (WorkflowGroupMetadata) rip;
        assertFalse(wgm.getAuthor().isPresent());
        assertFalse(wgm.getDescription().isPresent());
        assertFalse(wgm.getTitle().isPresent());
        assertFalse(wgm.getTags().isPresent());
        assertFalse(wgm.getLinks().isPresent());
    }

    // -- Helper methods --

    private static Optional<String> readAnnotationText(final List<String> readLines) {
        for (int i = 0; i < readLines.size(); i++) {
            final String line = readLines.get(i);
            if (line.contains("nodeAnnotation")) {
                final String annotation = readLines.get(i + 1);
                return Optional.ofNullable(XMLUtils
                    .unescape(annotation.substring(annotation.indexOf("value=\"") + 7, annotation.lastIndexOf('"'))));
            }
        }
        return Optional.empty();
    }

    private static Optional<String> readCustomNodeDescription(final List<String> readLines) {
        return Optional.ofNullable(XMLUtils.unescape(parseValue(readLines, "customDescription")));
    }

    private static Optional<ConfigBase> readNodeConfiguration(final File f) throws Exception {
        final ConfigBase params = new MetadataConfig("settings.xml");
        try (final FileInputStream in = new FileInputStream(f)) {
            XMLConfig.load(params, in);
        }
        if (!params.containsKey("model")) {
            return Optional.empty();
        }
        final ConfigBase model = params.getConfigBase("model");
        model.setParent(null);
        return Optional.ofNullable(model);
    }

    private static NodeAndBundleInformation readNodeAndBundleInformation(final boolean isMetanode,
        final List<String> readLines) {
        if (isMetanode) {
            return new NodeAndBundleInformation(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());
        }
        final Optional<String> nodeName = Optional.ofNullable(parseValue(readLines, "node-name"));
        final Optional<String> nodeFactory = Optional.ofNullable(parseValue(readLines, "key=\"factory\""));
        final Optional<String> nodeBundleName = Optional.ofNullable(parseValue(readLines, "node-bundle-name"));
        final Optional<String> nodeBundleSymbolicName =
            Optional.ofNullable(parseValue(readLines, "node-bundle-symbolic-name"));
        final Optional<String> nodeBundleVendor = Optional.ofNullable(parseValue(readLines, "node-bundle-vendor"));
        final String nodeBundleVersionString = parseValue(readLines, "node-bundle-version");
        final Optional<Version> nodeBundleVersion = nodeBundleVersionString == null || nodeBundleVersionString.isEmpty()
            ? Optional.empty() : Optional.ofNullable(new Version(nodeBundleVersionString));
        final Optional<String> nodeFeatureName = Optional.ofNullable(parseValue(readLines, "node-feature-name"));
        final Optional<String> nodeFeatureSymbolicName =
            Optional.ofNullable(parseValue(readLines, "node-feature-symbolic-name"));
        final Optional<String> nodeFeatureVendor = Optional.ofNullable(parseValue(readLines, "node-feature-vendor"));
        final String nodeFeatureVersionString = parseValue(readLines, "node-feature-version");
        final Optional<Version> nodeFeatureVersion =
            nodeFeatureVersionString == null || nodeFeatureVersionString.isEmpty() ? Optional.empty()
                : Optional.ofNullable(new Version(nodeFeatureVersionString));
        return new NodeAndBundleInformation(nodeFactory, nodeBundleSymbolicName, nodeBundleName, nodeBundleVendor,
            nodeName, nodeBundleVersion, nodeFeatureSymbolicName, nodeFeatureName, nodeFeatureVendor,
            nodeFeatureVersion);
    }

    private static Optional<String> readTemplateLink(final List<String> readLines) {
        return Optional.ofNullable(parseValue(readLines, "sourceURI"));
    }

    private static Version readVersion(final List<String> readLines) {
        final String versionString = parseValue(readLines, "key=\"version\"");
        return new Version(versionString);
    }

    private static List<Integer> readNodeIds(final List<String> readLines) {
        final List<Integer> ids = new ArrayList<>();
        readLines.stream().filter(line -> line.contains("<config key=\"node_"))
            .forEach(s -> ids.add(Integer.parseInt(s.substring(s.indexOf("node_") + 5, s.lastIndexOf('"')))));
        return ids;
    }

    private static String readName(final List<String> readLines) {
        return parseValue(readLines, "key=\"name\"");
    }

    private static Optional<String> readCustomDescription(final List<String> readLines) {
        return Optional.ofNullable(parseValue(readLines, "customDescription"));
    }

    private static Version readCreatedBy(final List<String> readLines) {
        final String createdByString = parseValue(readLines, "created_by");
        return new Version(createdByString);
    }

    private static void assertEquivalentConnections(final List<Integer> expectedIds,
        final List<NodeConnection> actual) {
        assertEquals(expectedIds.size() / 4, actual.size());
        for (int i = 0, j = 0; i < actual.size(); i++, j += 4) {
            final int sourceId = expectedIds.get(j).intValue();
            final int destId = expectedIds.get(j + 1).intValue();
            String parsedSource = actual.get(i).getSourceId();
            String parsedDest = actual.get(i).getDestinationId();
            final int indexSource = parsedSource.lastIndexOf(':');
            final int indexDest = parsedDest.lastIndexOf(':');
            if (indexSource >= 0) {
                parsedSource = parsedSource.substring(indexSource + 1, parsedSource.length());
            }
            if (indexDest >= 0) {
                parsedDest = parsedDest.substring(indexDest + 1, parsedDest.length());
            }
            assertEquals(sourceId, Integer.parseInt(parsedSource));
            assertEquals(expectedIds.get(j + 1).intValue(), Integer.parseInt(parsedDest));
            assertEquals(expectedIds.get(j + 2).intValue(), actual.get(i).getSourcePort());
            assertEquals(expectedIds.get(j + 3).intValue(), actual.get(i).getDestinationPort());
            if (sourceId == -1) {
                assertFalse(actual.get(i).getSourceNode().isPresent());
            } else {
                assertTrue(actual.get(i).getSourceNode().isPresent());
            }
            if (destId == -1) {
                assertFalse(actual.get(i).getDestinationNode().isPresent());
            } else {
                assertTrue(actual.get(i).getDestinationNode().isPresent());
            }
        }
    }

    private static List<Integer> readConnectionIds(final List<String> readLines) throws Exception {
        final List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < readLines.size(); i++) {
            final String line = readLines.get(i);
            if (line.contains("connection_")) {
                final String sourceId = readLines.get(i + 1).substring(readLines.get(i + 1).indexOf("value=\"") + 7,
                    readLines.get(i + 1).lastIndexOf('"'));
                final String destId = readLines.get(i + 2).substring(readLines.get(i + 2).indexOf("value=\"") + 7,
                    readLines.get(i + 2).lastIndexOf('"'));
                final String sourcePort = readLines.get(i + 3).substring(readLines.get(i + 3).indexOf("value=\"") + 7,
                    readLines.get(i + 3).lastIndexOf('"'));
                final String destPort = readLines.get(i + 4).substring(readLines.get(i + 4).indexOf("value=\"") + 7,
                    readLines.get(i + 4).lastIndexOf('"'));
                ids.add(Integer.parseInt(sourceId));
                ids.add(Integer.parseInt(destId));
                ids.add(Integer.parseInt(sourcePort));
                ids.add(Integer.parseInt(destPort));
                i += 4;
            }
        }
        return ids;
    }

    private static AuthorInformation readAuthorInformation(final List<String> readWorkflowLines)
        throws IOException, ParseException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        final String author = XMLUtils.unescape(parseValue(readWorkflowLines, "authored-by"));
        final String authoredString = parseValue(readWorkflowLines, "authored-when");
        final String lastEditor = XMLUtils.unescape(parseValue(readWorkflowLines, "lastEdited-by"));
        final String lastEditedString = parseValue(readWorkflowLines, "lastEdited-when");
        Date lastEdited = null;
        if (lastEditedString != null && !lastEditedString.isEmpty()) {
            lastEdited = df.parse(lastEditedString);
        }

        return new AuthorInformation(author, df.parse(authoredString), Optional.ofNullable(lastEditor),
            Optional.ofNullable(lastEdited));
    }

    private static TemplateInformation readTemplateInformation(final List<String> readTemplateLines)
        throws IOException, ParseException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String role = parseValue(readTemplateLines, "role");
        final Date timeStamp = df.parse(parseValue(readTemplateLines, "timestamp"));
        final Optional<String> sourceURI = Optional.ofNullable(parseValue(readTemplateLines, "sourceURI"));
        final String type = parseValue(readTemplateLines, "templateType");

        return new TemplateInformation(role, timeStamp, sourceURI, type);
    }

    private static void assertEquivalentAnnotations(final Optional<List<String>> expected,
        final Optional<List<String>> actual) {
        assertEquals(expected.isPresent(), actual.isPresent());
        if (!expected.isPresent() || !actual.isPresent()) {
            return;
        }
        final List<String> e = expected.get();
        final List<String> a = actual.get();
        assertEquals(e.size(), a.size());
        for (int i = 0; i < e.size(); i++) {
            assertEquals(e.get(i), a.get(i));
        }
    }

    private static Optional<List<String>> readAnnotations(final List<String> readLines) throws Exception {
        final List<String> annotations = new ArrayList<>();
        for (int i = 0; i < readLines.size(); i++) {
            final String line = readLines.get(i);
            if (line.contains("key=\"annotation_")) {
                i++;
                final String text = readLines.get(i).substring(readLines.get(i).indexOf("value=\"") + 7,
                    readLines.get(i).indexOf("\"/>"));
                annotations.add(XMLUtils.unescape(text));
            }
        }
        return annotations.isEmpty() ? Optional.empty() : Optional.ofNullable(annotations);
    }

    private static String parseValue(final List<String> lines, final String filter) {
        final Optional<String> potentialMatch = lines.stream().filter(line -> line.contains(filter)).findFirst();
        if (!potentialMatch.isPresent()) {
            return null;
        }
        final String matchedLine = potentialMatch.get();
        final String value = matchedLine.substring(matchedLine.indexOf("value=\"") + 7, matchedLine.lastIndexOf('"'));
        return value.isEmpty() ? null : StringEscapeUtils.unescapeXml(value);
    }

    private static String parseWorkflowSetMeta(final String fieldName, final List<String> lines) {
        String value = "";
        boolean read = false;
        for (String line : lines) {
            line += "\n";
            int start = 0;
            int end = line.length();
            if (line.contains("name=\"" + fieldName + "\"")) {
                read = true;
            }
            if (line.contains("<element")) {
                start = line.indexOf(">") + 1;
            }
            if (line.contains("</element>")) {
                end = line.indexOf("</");
            }
            if (read) {
                String piece = line.substring(start, end);
                value += piece;
                if (end != line.length()) {
                    return value;
                }
            }
        }
        return null;
    }

    private void testStructure(final IWorkflowMetadata wkfMd, final Path workflowFile) throws Exception {
        final List<NodeMetadata> metaNodes = new ArrayList<>();
        final List<NodeMetadata> wrappedMetaNodes = new ArrayList<>();
        for (final NodeMetadata nodeMeta : wkfMd.getNodes()) {
            if (nodeMeta.getType().equals(NodeType.METANODE)) {
                metaNodes.add(nodeMeta);
            }
            if (nodeMeta.getType().equals(NodeType.SUBNODE)) {
                wrappedMetaNodes.add(nodeMeta);
            }
        }

        final List<String> lines = Files.readAllLines(workflowFile);
        final long nodeCount =
            lines.stream().filter(line -> line.contains("<entry key=\"node_settings_file\"")).count();
        final long metaNodeCount = lines.stream()
            .filter(line -> line.contains("key=\"node_type\"") && line.contains("value=\"MetaNode\"")).count();
        final long wrappedMetaNodeCount = lines.stream()
            .filter(line -> line.contains("key=\"node_type\"") && line.contains("value=\"SubNode\"")).count();

        assertEquals(nodeCount, wkfMd.getNodes().size());
        assertEquals(metaNodeCount, metaNodes.size());
        assertEquals(wrappedMetaNodeCount, wrappedMetaNodes.size());

        for (final NodeMetadata metaNode : metaNodes) {
            final String settingsLine = lines.stream()
                .filter(line -> line.contains("key=\"node_settings_file\"") && line.contains(metaNode.getNodeId() + ""))
                .findFirst().get();
            final String settingsPath =
                settingsLine.substring(settingsLine.indexOf("value=\"") + 7, settingsLine.lastIndexOf('"'));
            final File metaNodeFile = new File(workflowFile.toFile().getParentFile(), settingsPath);
            testStructure((IWorkflowMetadata)metaNode, metaNodeFile.toPath());
        }

        for (final NodeMetadata wrappedMetaNode : wrappedMetaNodes) {
            final String settingsLine = lines.stream().filter(
                line -> line.contains("key=\"node_settings_file\"") && line.contains(wrappedMetaNode.getNodeId() + ""))
                .findFirst().get();
            final String settingsPath =
                settingsLine.substring(settingsLine.indexOf("value=\"") + 7, settingsLine.lastIndexOf('"'));
            final File wMNNodeFile = new File(workflowFile.toFile().getParentFile(), settingsPath);
            final File wMNWorkflowFile = new File(wMNNodeFile.getParentFile(), "workflow.knime");
            testStructure((IWorkflowMetadata)wrappedMetaNode, wMNWorkflowFile.toPath());
        }
    }

    private static void assertUOEThrown(final Callable<?> c) throws Exception {
        try {
            c.call();
            assertTrue("Expected UnsupportedOperationException was not thrown", false);
        } catch (final UnsupportedOperationException ex) {
            // Do nothing, this is expected
        }
    }

    /**
     * Unzips the given zip input to the given folder.
     *
     * @param in Non-null input stream.
     * @param folder Non-null output folder
     * @throws Exception
     */
    private static void unzip(final InputStream in, final File folder) throws Exception {
        try (ArchiveInputStream ais =
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in)) {
            ZipArchiveEntry entry;
            while ((entry = (ZipArchiveEntry)ais.getNextEntry()) != null) {
                final File outputFile = new File(folder, entry.getName());
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    try (OutputStream os = new FileOutputStream(outputFile)) {
                        IOUtils.copy(ais, os);
                    }
                }
            }
        }
    }
}
