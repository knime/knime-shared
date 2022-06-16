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
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.storage.multidir.loader.MetaNodeLoader;
import org.knime.shared.workflow.storage.multidir.loader.NodeLoaderTestUtils;
import org.knime.shared.workflow.storage.multidir.loader.StandaloneLoader;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.xml.sax.SAXException;

/**
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings({"squid:S2698", "squid:S5961"})
class MetaNodeSaverTest {

    static File OUTPUT_DIRECTORY;

    @BeforeAll
    static void setUpOutputDir() throws IOException {
        OUTPUT_DIRECTORY = NodeLoaderTestUtils.readResourceFolder(SaverTestUtils.OUTPUT_DIR_NAME);
        OUTPUT_DIRECTORY.mkdir();
    }

    @Test
    void testStandaloneMetanode()
        throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        var inputDir = NodeLoaderTestUtils.readResourceFolder("Metanode_Template");
        MetaNodeDef workflow = MetaNodeLoader.load(new SimpleConfig("dummy"), inputDir, LoadVersion.FUTURE);

        var saver = new MetaNodeSaver(workflow);
        saver.save(OUTPUT_DIRECTORY, null);

        var workflowconfig = new File(OUTPUT_DIRECTORY, IOConst.WORKFLOW_FILE_NAME.get());
        var workflowXML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(workflowconfig)) {
            XMLConfig.load(workflowXML, fis);
        }

        assertThat(workflowXML.containsKey(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isTrue();
        assertThat(workflowXML.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isNull();
        assertThat(workflowXML.containsKey(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get())).isFalse();

    }

    @Test
    void testNestedMetanode() throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        var workflowDir = NodeLoaderTestUtils.readResourceFolder("Workflow_Test");
        var inputDir = new File(workflowDir, "MetanodeTest (#12)");

        ConfigBase workflowXML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(new File(workflowDir, "workflow.knime"))) {
            XMLConfig.load(workflowXML, fis);
            workflowXML = workflowXML.getConfigBase(IOConst.WORKFLOW_NODES_KEY.get()).getConfigBase("node_12");
        }
        var metanode = MetaNodeLoader.load(workflowXML, inputDir, LoadVersion.FUTURE);
        var creator = StandaloneLoader.load(workflowDir).getCreator();

        var parentXML = new SimpleConfig("node_12");
        var saver = new MetaNodeSaver(metanode, creator.orElse(StandaloneSaver.DEFAULT_CREATOR));
        saver.save(OUTPUT_DIRECTORY, parentXML);

        assertThat(parentXML.getInt(IOConst.ID_KEY.get())).isEqualTo(12);
        var filename = parentXML.getString(IOConst.NODE_SETTINGS_FILE.get());
        assertThat(filename).isNotNull().isEqualTo(SaverTestUtils.OUTPUT_DIR_NAME + "/workflow.knime");
        assertThat(parentXML.getBoolean(IOConst.WORKFLOW_NODES_NODE_IS_META_KEY.get())).isTrue();
        assertThat(parentXML.getString(IOConst.WORKFLOW_NODES_NODE_TYPE_KEY.get())).isEqualTo("MetaNode");
        assertThat(parentXML.getString(IOConst.UI_CLASSNAME_KEY.get()))
            .isEqualTo("org.knime.core.node.workflow.NodeUIInformation");
        var uiSettings = parentXML.getConfigBase(IOConst.UI_SETTINGS_KEY.get());
        assertThat(uiSettings.getChildCount()).isEqualTo(1);
        assertThat(uiSettings.getIntArray(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get())).containsExactly(324, 317, 94, 64);

        var metanodeConfigFile = new File(OUTPUT_DIRECTORY, IOConst.WORKFLOW_FILE_NAME.get());
        ConfigBase metanodeXML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(metanodeConfigFile)) {
            XMLConfig.load(metanodeXML, fis);
        }

        assertThat(metanodeXML.getString(IOConst.WORKFLOW_HEADER_CREATED_BY_KEY.get())).startsWith("4.5.");
        assertThat(metanodeXML.getBoolean(IOConst.WORKFLOW_HEADER_CREATED_BY_NIGHTLY_KEY.get())).isFalse();
        assertThat(metanodeXML.getString(IOConst.WORKFLOW_HEADER_VERSION_KEY.get())).isEqualTo("4.1.0");

        var template = metanodeXML.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        assertThat(template.getString(IOConst.WORKFLOW_TEMPLATE_ROLE_KEY.get())).isEqualTo("Link");
        assertThat(template.getString(IOConst.TIMESTAMP.get())).startsWith("2022-");
        assertThat(template.getString(IOConst.SOURCE_URI_KEY.get())).isEqualTo("knime://knime.mountpoint/MetanodeTest");
        assertThat(template.containsKey(IOConst.WORKFLOW_TEMPLATE_TYPE_KEY.get())).isFalse();

        assertThat(metanodeXML.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get())
            .getString(IOConst.SOURCE_URI_KEY.get())).isEqualTo("knime://knime.mountpoint/MetanodeTest");
        assertThat(metanodeXML.getString(IOConst.METADATA_NAME_KEY.get())).isEqualTo("MetanodeTest");
        assertThat(metanodeXML.getConfigBase(IOConst.NODE_ANNOTATION_KEY.get()).getChildCount()).isEqualTo(12);
        assertThat(metanodeXML.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isNull();

        assertThat(metanodeXML.getConfigBase(IOConst.META_IN_PORTS_KEY.get()).getChildCount()).isEqualTo(2);

        var outports = metanodeXML.getConfigBase(IOConst.META_OUT_PORTS_KEY.get());
        // TODO This is null in the source, but a default value is written. What should it be?
        // either:  assertThat(outports.getString(IOConst.UI_CLASSNAME_KEY.get())).isNull();
        // or:      assertThat(outports.getString(IOConst.UI_CLASSNAME_KEY.get()))
        //              .isEqualTo("org.knime.core.node.workflow.NodeUIInformation");
        assertThat(outports.getConfigBase(IOConst.PORT_ENUM_KEY.get()).getChildCount()).isEqualTo(2);
        var out0 = outports.getConfigBase(IOConst.PORT_ENUM_KEY.get()).getConfigBase("outport_0");
        assertThat(out0.getInt(IOConst.PORT_INDEX_KEY.get())).isZero();
        assertThat(out0.getString(IOConst.PORT_NAME_KEY.get())).isEqualTo("Connected to: Concatenated table");
        assertThat(out0.getConfigBase(IOConst.PORT_TYPE_KEY.get()).getString(IOConst.PORT_OBJECT_CLASS_KEY.get()))
            .isEqualTo("org.knime.core.node.BufferedDataTable");
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
