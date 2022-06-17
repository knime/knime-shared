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
 *   12 May 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.ComponentNodeDef;
import org.knime.shared.workflow.storage.multidir.loader.ComponentNodeLoader;
import org.knime.shared.workflow.storage.multidir.loader.NodeLoaderTestUtils;
import org.knime.shared.workflow.storage.multidir.loader.StandaloneLoader;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings({"squid:S2698", "squid:S5961"})
class ComponentNodeSaverTest {

    static File OUTPUT_DIRECTORY;

    @BeforeAll
    static void setUpOutputDir() throws IOException {
        OUTPUT_DIRECTORY = NodeLoaderTestUtils.readResourceFolder(SaverTestUtils.OUTPUT_DIR_NAME);
        OUTPUT_DIRECTORY.mkdir();
    }

    @Test
    void testStandaloneComponent()
        throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        var inputDir = NodeLoaderTestUtils.readResourceFolder("Component_Template");
        ComponentNodeDef component = (ComponentNodeDef)StandaloneLoader.load(inputDir).getContents();

        var saver = new ComponentNodeSaver(component);
        saver.save(OUTPUT_DIRECTORY, null, s -> SaverUtils.addTemplateInfo(s, component.getTemplateLink(),
            component.getTemplateMetadata(), component.getNodeType()));

        var workflowconfig = new File(OUTPUT_DIRECTORY, IOConst.WORKFLOW_FILE_NAME.get());
        var workflowXML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(workflowconfig)) {
            XMLConfig.load(workflowXML, fis);
        }

        assertThat(workflowXML.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isNull();

        var nodeconfig = new File(OUTPUT_DIRECTORY, IOConst.NODE_SETTINGS_FILE_NAME.get());
        var nodeXML = new SimpleConfig("settings.xml");
        try (var fis = new FileInputStream(nodeconfig)) {
            XMLConfig.load(nodeXML, fis);
        }

        for (var c : List.of(workflowXML, nodeXML)) {
            var nodeAnnotation = c.getConfigBase(IOConst.NODE_ANNOTATION_KEY.get());
            assertThat(nodeAnnotation.getChildCount()).isEqualTo(12);
            assertThat(nodeAnnotation.getInt("bgcolor")).isEqualTo(16777215);
            assertThat(nodeAnnotation.getString("alignment")).isEqualTo("CENTER");
            assertThat(nodeAnnotation.getInt("annotation-version")).isEqualTo(20151123);
            assertThat(nodeAnnotation.getConfigBase("styles").getChildCount()).isZero();
        }

        assertThat(nodeXML.getString(IOConst.NODE_FILE_KEY.get())).isEqualTo("settings.xml");
        assertThat(nodeXML.getConfigBase(IOConst.INTERNAL_NODE_SUBSETTINGS.get()).getChildCount()).isOne();
        assertThat(nodeXML.getConfigBase(IOConst.INTERNAL_NODE_SUBSETTINGS.get()).getString("memory_policy"))
            .isEqualTo("CacheSmallInMemory");

        assertThat(nodeXML.getConfigBase(IOConst.MODEL_KEY.get()).getChildCount()).isZero();
        assertThat(nodeXML.getConfigBase(IOConst.VARIABLES_KEY.get()).getChildCount()).isZero();

        assertThat(nodeXML.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isNull();
        assertThat(nodeXML.getString(IOConst.WORKFLOW_FILE_KEY.get())).isEqualTo("workflow.knime");

        assertThat(nodeXML.getInt(IOConst.VIRTUAL_IN_ID_KEY.get())).isEqualTo(3);
        var inports = nodeXML.getConfigBase(IOConst.INPORTS_KEY.get());
        assertThat(inports.getChildCount()).isOne();
        assertThat(inports.getConfigBase("inport_0").getInt(IOConst.PORT_INDEX_KEY.get())).isZero();
        assertThat(inports.getConfigBase("inport_0").getConfigBase(IOConst.PORT_TYPE_KEY.get())
            .getString(IOConst.PORT_OBJECT_CLASS_KEY.get()))
                .isEqualTo("org.knime.filehandling.core.port.FileSystemPortObject");

        assertThat(nodeXML.getInt(IOConst.VIRTUAL_OUT_ID_KEY.get())).isEqualTo(4);
        assertThat(nodeXML.getConfigBase(IOConst.OUTPORTS_KEY.get()).getChildCount()).isZero();

        var metadata = nodeXML.getConfigBase(IOConst.METADATA_KEY.get());
        assertThat(metadata.getChildCount()).as(metadata.keySet().toString()).isEqualTo(2);
        assertThat(metadata.getString(IOConst.DESCRIPTION_KEY.get())).isEmpty();
        var metaInports = metadata.getConfigBase(IOConst.META_IN_PORTS_KEY.get());
        assertThat(metaInports.getChildCount()).isOne();
        assertThat(metaInports.getConfigBase("inport_0").getString(IOConst.PORT_NAME_KEY.get())).isEqualTo("Port 1");
        assertThat(metaInports.getConfigBase("inport_0").getString(IOConst.DESCRIPTION_KEY.get())).isEmpty();
        assertThat(metaInports.getConfigBase("inport_0").getInt(IOConst.PORT_INDEX_KEY.get())).isZero();

        var template = nodeXML.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        assertThat(template.getString(IOConst.WORKFLOW_TEMPLATE_ROLE_KEY.get())).isEqualTo("Template");
        assertThat(template.getString(IOConst.TIMESTAMP.get())).startsWith("2022-");
        assertThat(template.getString(IOConst.SOURCE_URI_KEY.get())).isNull();
        assertThat(template.getString(IOConst.WORKFLOW_TEMPLATE_TYPE_KEY.get())).isEqualTo("SubNode");

        assertThat(nodeXML.getString(IOConst.LAYOUT_JSON_KEY.get())).isEqualTo("{\"parentLayoutLegacyMode\":false}");
        assertThat(nodeXML.getString(IOConst.CONFIGURATION_LAYOUT_JSON_KEY.get())).isEqualTo("{}");
        assertThat(nodeXML.getBoolean(IOConst.HIDE_IN_WIZARD_KEY.get())).isFalse();
        assertThat(nodeXML.getString(IOConst.CUSTOM_CSS_KEY.get())).isEmpty();
    }

    @Test
    void testNestedLinkedComponent()
        throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        var workflowDir = NodeLoaderTestUtils.readResourceFolder("Workflow_Test");
        var inputDir = new File(workflowDir, "Component (#14)");

        ConfigBase parentWorkflowXML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(new File(workflowDir, "workflow.knime"))) {
            XMLConfig.load(parentWorkflowXML, fis);
            parentWorkflowXML =
                parentWorkflowXML.getConfigBase(IOConst.WORKFLOW_NODES_KEY.get()).getConfigBase("node_14");
        }

        var componentnode = ComponentNodeLoader.load(parentWorkflowXML, inputDir, LoadVersion.FUTURE, false);
        var creator = StandaloneLoader.load(workflowDir).getCreator();

        var parentXML = new SimpleConfig("node_14");
        var saver = new ComponentNodeSaver(componentnode, creator.orElse(StandaloneSaver.DEFAULT_CREATOR));
        saver.save(OUTPUT_DIRECTORY, parentXML);

        assertThat(parentXML.getInt(IOConst.ID_KEY.get())).isEqualTo(14);
        var filename = parentXML.getString(IOConst.NODE_SETTINGS_FILE.get());
        assertThat(filename).isNotNull().isEqualTo(SaverTestUtils.OUTPUT_DIR_NAME + "/settings.xml");
        assertThat(parentXML.getBoolean(IOConst.WORKFLOW_NODES_NODE_IS_META_KEY.get())).isTrue();
        assertThat(parentXML.getString(IOConst.WORKFLOW_NODES_NODE_TYPE_KEY.get())).isEqualTo("SubNode");
        assertThat(parentXML.getString(IOConst.UI_CLASSNAME_KEY.get()))
            .isEqualTo("org.knime.core.node.workflow.NodeUIInformation");
        var uiSettings = parentXML.getConfigBase(IOConst.UI_SETTINGS_KEY.get());
        assertThat(uiSettings.getChildCount()).isEqualTo(1);
        assertThat(uiSettings.getIntArray(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get())).containsExactly(275, 511, 80, 77);

        var componentWorkflowSettingsFile = new File(OUTPUT_DIRECTORY, IOConst.WORKFLOW_FILE_NAME.get());
        ConfigBase componentWorkflowXML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(componentWorkflowSettingsFile)) {
            XMLConfig.load(componentWorkflowXML, fis);
        }

        assertThat(componentWorkflowXML.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isNull();

        var componentNodeSettingsFile = new File(OUTPUT_DIRECTORY, IOConst.NODE_SETTINGS_FILE_NAME.get());
        ConfigBase componentNodeXML = new SimpleConfig("settings.xml");
        try (var fis = new FileInputStream(componentNodeSettingsFile)) {
            XMLConfig.load(componentNodeXML, fis);
        }

        for (var c : List.of(componentWorkflowXML, componentNodeXML)) {
            var nodeAnnotation = c.getConfigBase(IOConst.NODE_ANNOTATION_KEY.get());
            assertThat(nodeAnnotation.getChildCount()).isEqualTo(12);
            assertThat(nodeAnnotation.getInt("bgcolor")).isEqualTo(16777215);
            assertThat(nodeAnnotation.getString("alignment")).isEqualTo("CENTER");
            assertThat(nodeAnnotation.getInt("annotation-version")).isEqualTo(20151123);
            assertThat(nodeAnnotation.getConfigBase("styles").getChildCount()).isZero();
        }

        assertThat(componentNodeXML.getString(IOConst.NODE_FILE_KEY.get())).isEqualTo("settings.xml");
        assertThat(componentNodeXML.getConfigBase(IOConst.INTERNAL_NODE_SUBSETTINGS.get()).getChildCount()).isOne();
        assertThat(componentNodeXML.getConfigBase(IOConst.INTERNAL_NODE_SUBSETTINGS.get()).getString("memory_policy"))
            .isEqualTo("CacheSmallInMemory");

        assertThat(componentNodeXML.getConfigBase(IOConst.MODEL_KEY.get()).getChildCount()).isZero();

        assertThat(componentNodeXML.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isNull();
        assertThat(componentNodeXML.getString(IOConst.WORKFLOW_FILE_KEY.get())).isEqualTo("workflow.knime");

        assertThat(componentNodeXML.getInt(IOConst.VIRTUAL_IN_ID_KEY.get())).isEqualTo(10);
        var inports = componentNodeXML.getConfigBase(IOConst.INPORTS_KEY.get());
        assertThat(inports.getChildCount()).isOne();
        assertThat(inports.getConfigBase("inport_0").getInt(IOConst.PORT_INDEX_KEY.get())).isZero();
        assertThat(inports.getConfigBase("inport_0").getConfigBase(IOConst.PORT_TYPE_KEY.get())
            .getString(IOConst.PORT_OBJECT_CLASS_KEY.get())).isEqualTo("org.knime.core.node.BufferedDataTable");

        assertThat(componentNodeXML.getInt(IOConst.VIRTUAL_OUT_ID_KEY.get())).isEqualTo(11);
        var outports = componentNodeXML.getConfigBase(IOConst.OUTPORTS_KEY.get());
        assertThat(outports.getChildCount()).isEqualTo(2);
        assertThat(outports.getConfigBase("outport_1").getInt(IOConst.PORT_INDEX_KEY.get())).isOne();
        assertThat(outports.getConfigBase("outport_1").getConfigBase(IOConst.PORT_TYPE_KEY.get())
            .getString(IOConst.PORT_OBJECT_CLASS_KEY.get())).isEqualTo("org.knime.core.node.BufferedDataTable");

        var metadata = componentNodeXML.getConfigBase(IOConst.METADATA_KEY.get());
        assertThat(metadata.getChildCount()).as(metadata.toString()).isEqualTo(3);
        assertThat(metadata.getString(IOConst.DESCRIPTION_KEY.get())).isEmpty();
        var metaOutports = metadata.getConfigBase(IOConst.META_OUT_PORTS_KEY.get());
        assertThat(metaOutports.getChildCount()).isEqualTo(2);
        assertThat(metaOutports.getConfigBase("outport_1").getString(IOConst.PORT_NAME_KEY.get()))
            .isEqualTo("outport2");
        assertThat(metaOutports.getConfigBase("outport_1").getString(IOConst.DESCRIPTION_KEY.get()))
            .isEqualTo("description of outport 2");
        assertThat(metaOutports.getConfigBase("outport_1").getInt(IOConst.PORT_INDEX_KEY.get())).isOne();

        var template = componentNodeXML.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        assertThat(template.containsKey(IOConst.WORKFLOW_TEMPLATE_TYPE_KEY.get())).isFalse();
        assertThat(template.getString(IOConst.WORKFLOW_TEMPLATE_ROLE_KEY.get())).isEqualTo("Link");
        assertThat(template.getString(IOConst.TIMESTAMP.get())).startsWith("2022-");
        assertThat(template.getString(IOConst.SOURCE_URI_KEY.get())).startsWith("knime://knime.mountpoint/Component");

        assertThat(componentNodeXML.getString(IOConst.LAYOUT_JSON_KEY.get()))
            .isEqualTo("{\"parentLayoutLegacyMode\":false}");
        assertThat(componentNodeXML.getString(IOConst.CONFIGURATION_LAYOUT_JSON_KEY.get())).isEqualTo("{}");
        assertThat(componentNodeXML.getBoolean(IOConst.HIDE_IN_WIZARD_KEY.get())).isFalse();
        assertThat(componentNodeXML.getString(IOConst.CUSTOM_CSS_KEY.get())).isEmpty();
    }

    /**
     * This test is simply there to test that the template information is only written to the settings.xml, if the
     * component is a standalone
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws InvalidSettingsException
     */
    @Test
    void testNestedTemplateComponent()
        throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        var workflowDir = NodeLoaderTestUtils.readResourceFolder("Workflow_Test");
        var inputDir = new File(workflowDir, "Airport Airp (#7)");

        ConfigBase parentWorkflowXML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(new File(workflowDir, "workflow.knime"))) {
            XMLConfig.load(parentWorkflowXML, fis);
            parentWorkflowXML =
                parentWorkflowXML.getConfigBase(IOConst.WORKFLOW_NODES_KEY.get()).getConfigBase("node_7");
        }

        var metanode = ComponentNodeLoader.load(parentWorkflowXML, inputDir, LoadVersion.FUTURE, false);
        var creator = StandaloneLoader.load(workflowDir).getCreator();

        var parentXML = new SimpleConfig("node_7");
        var saver = new ComponentNodeSaver(metanode, creator.orElse(StandaloneSaver.DEFAULT_CREATOR));
        saver.save(OUTPUT_DIRECTORY, parentXML);

        var componentNodeSettingsFile = new File(OUTPUT_DIRECTORY, IOConst.NODE_SETTINGS_FILE_NAME.get());
        ConfigBase componentNodeXML = new SimpleConfig("settings.xml");
        try (var fis = new FileInputStream(componentNodeSettingsFile)) {
            XMLConfig.load(componentNodeXML, fis);
        }

        assertThat(parentXML.getInt(IOConst.ID_KEY.get())).isEqualTo(7);
        assertThat(componentNodeXML.containsKey(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get())).isFalse();
    }

    @AfterEach
    void cleanOutputDir() throws IOException {
        FileUtils.cleanDirectory(OUTPUT_DIRECTORY);
    }

    @AfterAll
    static void cleanUp() throws IOException {
        FileUtils.deleteDirectory(OUTPUT_DIRECTORY);
    }

}
