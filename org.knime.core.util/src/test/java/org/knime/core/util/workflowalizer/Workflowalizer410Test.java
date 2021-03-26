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
 *   Nov 26, 2019 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.util.LoadVersion;
import org.knime.core.util.PathUtils;
import org.knime.core.util.workflowalizer.RepositoryItemMetadata.RepositoryItemType;

/**
 * Tests the {@link Workflowalizer} for {@link LoadVersion#V4010}.
 *
 * <p>
 * The intention of these tests is to ensure the Workflowalizer can parse 4.1.0 items, and to test changes specific to
 * this version. They are not meant to comprehensively test <b>all</b> Workflowalizer functionality.
 * </p>
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.13
 */
public class Workflowalizer410Test extends AbstractWorkflowalizerTest {

    private static Path workspaceDir;
    private static Path componentDir;

    private static List<String> componentSettingsXml;

    /**
     * Readers zip archive, and creates temporary directory for workflow files.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setup() throws Exception {
        workspaceDir = PathUtils.createTempDir(Workflowalizer410Test.class.getName());
        try (InputStream is = Workflowalizer410Test.class.getResourceAsStream("/workflowalizer-test-410.zip")) {
            unzip(is, workspaceDir.toFile());
        }

        final Path cd = PathUtils.createTempDir(Workflowalizer410Test.class.getName());
        try (final InputStream is = Workflowalizer410Test.class.getResourceAsStream("/Component-410-Full-Meta.zip")) {
            unzip(is, cd.toFile());
        }
        componentDir = cd.resolve("Full-Metadata");
        componentSettingsXml = Files.readAllLines(componentDir.resolve("settings.xml"), StandardCharsets.UTF_8);
    }

    /**
     * Tests that 4.1.0 workflows can be parsed.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testReadingWorkflow() throws Exception {
        final Path workflowDir = workspaceDir.resolve("workflowalizer-test/Testing_Workflowalizer_360Pre");
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir);
        assertEquals("4.1.0", wm.getVersion().toString());
        testStructure(wm, workflowDir.resolve("workflow.knime"));
        assertThat("Unexpected workflow configuration", "test: value\n", is(wm.getWorkflowConfiguration().get()));
        assertThat("Unexpected workflow configuration representation", "test: value\n",
            is(wm.getWorkflowConfigurationRepresentation().get()));
    }

    /**
     * Tests that reading the workflow configuration fields without setting them in the
     * {@link WorkflowalizerConfiguration} results in a {@link UnsupportedOperationException}
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testReadingWorkflowConfigurations() throws Exception {
        final Path workflowDir = workspaceDir.resolve("workflowalizer-test/Testing_Workflowalizer_360Pre");
        final WorkflowMetadata wm = Workflowalizer.readWorkflow(workflowDir,
            WorkflowalizerConfiguration.builder().readNodeConfiguration().build());
        assertUOEThrown(wm::getWorkflowConfiguration);
        assertUOEThrown(wm::getWorkflowConfigurationRepresentation);
    }

    /**
     * Tests that 4.1.0 workflow groups can be parsed.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testReadingWorkflowGroup() throws Exception {
        final Path workflowGroup = workspaceDir.resolve("workflowalizer-test/test_group");
        final List<String> rawLines = Files.readAllLines(
            workspaceDir.resolve("workflowalizer-test/test_group/workflowset.meta"), StandardCharsets.UTF_8);

        final WorkflowGroupMetadata wsm = Workflowalizer.readWorkflowGroup(workflowGroup);
        assertEquals(RepositoryItemType.WORKFLOW_GROUP, wsm.getType());
        testWorkflowSetMeta(rawLines, wsm);
    }

    /**
     * Tests that 4.1.0 metanode templates can be parsed.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testReadingTemplate() throws Exception {
        final Path templateDir = workspaceDir.resolve("workflowalizer-test/Hierarchical Cluster Assignment");
        final TemplateMetadata tm = Workflowalizer.readTemplate(templateDir);
        assertEquals("4.1.0", tm.getVersion().toString());
        testStructure(tm, templateDir.resolve("workflow.knime"));
    }

    /**
     * Tests that 4.1.0 components can be parsed.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testReadingComponent() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentDir);
        assertTrue(tm instanceof ComponentMetadata);
        assertEquals("4.1.0", tm.getVersion().toString());
        testStructure(tm, componentDir.resolve("workflow.knime"));
    }

    /**
     * Tests that the component fields, which weren't modified as part of the 4.1.0 version, are still read correctly.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testComponentFields() throws Exception {
        final List<String> componentWorkflowKnime = Files.readAllLines(componentDir.resolve("workflow.knime"));
        final List<String> componentTemplateKnime = Files.readAllLines(componentDir.resolve("template.knime"));
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentDir);
        assertTrue(tm instanceof ComponentMetadata);
        assertEquals("Full-Metadata", tm.getName());
        assertTrue(tm.getUnexpectedFileNames().isEmpty());

        testAnnotations(componentWorkflowKnime, tm);
        testAuthorInformation(componentWorkflowKnime, tm);
        testConnections(componentWorkflowKnime, tm);
        testCreatedBy(componentTemplateKnime, tm);
        testCustomDescription(componentWorkflowKnime, tm);
        testNodeIds(componentWorkflowKnime, tm, null);
        testTemplateInformation(componentTemplateKnime, tm);
        testVersion(componentTemplateKnime, tm);

        final ComponentMetadata cm = (ComponentMetadata)tm;

        // 3 JS views, 1 widget, 1 legacy quickform
        assertEquals(5, cm.getViewNodes().size());
        assertTrue(cm.getViewNodes().contains("org.knime.js.base.node.widget.input.bool.BooleanWidgetNodeFactory"));
        assertTrue(
            cm.getViewNodes().contains("org.knime.js.base.node.quickform.input.bool.BooleanInputQuickFormNodeFactory"));
        assertTrue(cm.getViewNodes().contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory:f822b045"));
        assertTrue(cm.getViewNodes().contains("org.knime.js.base.node.viz.heatmap.HeatMapNodeFactory"));
        assertTrue(cm.getViewNodes().contains("org.knime.dynamic.js.v30.DynamicJSNodeFactory:1ce36c2f"));

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
     * Tests the the component's ports were parsed correctly.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testComponentPorts() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;
        testComponentPorts(componentSettingsXml, cm, 2, 3);
    }

    /**
     * Tests that the component description is correctly parsed.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testComponentDescription() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;
        testComponentDescription(componentSettingsXml, cm);
    }

    /**
     * Tests that the component type is correctly parsed.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testComponentType() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;
        testComponentType(componentSettingsXml, cm);
    }

    /**
     * Tests that the component icon is correctly parsed.
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testComponentIcon() throws Exception {
        final TemplateMetadata tm = Workflowalizer.readTemplate(componentDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;
        testComponentIcon(componentSettingsXml, cm);
    }

    /**
     * Tests parsing the component's metadata, for a component which has no metadata set.
     * <p>
     * This test specifically checks the component's ports, description, icon, and type since these are the fields which
     * were added or moved in 4.1.0.
     * </p>
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testComponentNoMetadata() throws Exception {
        final Path tempNoMetaDir = PathUtils.createTempDir(Workflowalizer410Test.class.getName());
        try (final InputStream is = Workflowalizer410Test.class.getResourceAsStream("/Component-410-No-Meta.zip")) {
            unzip(is, tempNoMetaDir.toFile());
        }
        final Path noMetaDir = tempNoMetaDir.resolve("No Metadata");
        final List<String> settingsXml = Files.readAllLines(noMetaDir.resolve("settings.xml"), StandardCharsets.UTF_8);

        final TemplateMetadata tm = Workflowalizer.readTemplate(noMetaDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        testComponentPorts(settingsXml, cm, 1, 0);
        testComponentDescription(settingsXml, cm);
        testComponentType(settingsXml, cm);
        testComponentIcon(settingsXml, cm);
    }

    /**
     * Tests parsing the component's metadata, for a component which has no ports.
     * <p>
     * This test specifically checks the component's ports, description, icon, and type since these are the fields which
     * were added or moved in 4.1.0.
     * </p>
     *
     * @throws Exception if error occurs
     */
    @Test
    public void testComponentNoPorts() throws Exception {
        final Path tempNoPortsDir = PathUtils.createTempDir(Workflowalizer410Test.class.getName());
        try (final InputStream is = Workflowalizer410Test.class.getResourceAsStream("/Component-410-No-Ports.zip")) {
            unzip(is, tempNoPortsDir.toFile());
        }
        final Path noPortsDir = tempNoPortsDir.resolve("No Ports");
        final List<String> settingsXml = Files.readAllLines(noPortsDir.resolve("settings.xml"), StandardCharsets.UTF_8);

        final TemplateMetadata tm = Workflowalizer.readTemplate(noPortsDir);
        assertTrue(tm instanceof ComponentMetadata);
        final ComponentMetadata cm = (ComponentMetadata)tm;

        testComponentPorts(settingsXml, cm, 0, 0);
        testComponentDescription(settingsXml, cm);
        testComponentType(settingsXml, cm);
        testComponentIcon(settingsXml, cm);
    }

}
