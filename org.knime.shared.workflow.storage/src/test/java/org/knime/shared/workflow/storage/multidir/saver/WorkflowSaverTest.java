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
 *   4 May 2022 (jasper): created
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.storage.multidir.loader.NodeLoaderTestUtils;
import org.knime.shared.workflow.storage.multidir.loader.WorkflowLoader;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.xml.sax.SAXException;

/**
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings({"squid:S2698", "squid:S5961"})
class WorkflowSaverTest {

    static File INPUT_DIRECTORY, OUTPUT_DIRECTORY;

    static ConfigBase XML;

    @BeforeAll
    static void setUp() throws IOException, SAXException, ParserConfigurationException {
        INPUT_DIRECTORY = NodeLoaderTestUtils.readResourceFolder("Workflow_Test");
        WorkflowDef workflow = WorkflowLoader.load(INPUT_DIRECTORY, LoadVersion.V4010);

        OUTPUT_DIRECTORY = NodeLoaderTestUtils.readResourceFolder(SaverTestUtils.OUTPUT_DIR_NAME);
        OUTPUT_DIRECTORY.mkdir();
        FileUtils.cleanDirectory(OUTPUT_DIRECTORY); // So that we don't interfere with previous tests
        var saver = new WorkflowSaver(workflow);
        saver.save(OUTPUT_DIRECTORY);

        var rootWorkflowDotKNIME = new File(OUTPUT_DIRECTORY, IOConst.WORKFLOW_FILE_NAME.get());
        XML = new SimpleConfig("workflow.knime");
        try (var fis = new FileInputStream(rootWorkflowDotKNIME)) {
            XMLConfig.load(XML, fis);
        }
    }

    /**
     * Test that all relevant settings have been written to the workflow.knime
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws InvalidSettingsException
     * @throws Exception
     */
    @Test
    void testWorkflowKNIME() throws IOException, SAXException, ParserConfigurationException, InvalidSettingsException {
        //then
        assertThat(OUTPUT_DIRECTORY).as("At least *something* should have been written").isNotEmptyDirectory();

        assertThat(XML.getString(IOConst.METADATA_NAME_KEY.get())).isNull();
        assertThat(XML.getString(IOConst.CUSTOM_DESCRIPTION_KEY.get())).isNull();

        var authorInformation = XML.getConfigBase(IOConst.AUTHOR_INFORMATION_KEY.get());
        assertThat(authorInformation.getChildCount()).isEqualTo(4);
        assertThat(authorInformation.getString(IOConst.AUTHORED_BY_KEY.get())).isEqualTo("apugh");

        var annotations = XML.getConfigBase(IOConst.WORKFLOW_ANNOTATIONS_KEY.get());
        assertThat(annotations.getChildCount()).isEqualTo(3);
        assertThat(annotations.getConfigBase("annotation_1").getString("text")).isEqualTo("Meta Node section\r\n");
        assertThat(annotations.getConfigBase("annotation_2").getConfigBase("styles").getChildCount()).isZero();

        var connections = XML.getConfigBase(IOConst.WORKFLOW_CONNECTIONS_KEY.get());
        assertThat(connections.getChildCount()).isEqualTo(6);

        var connection2 = connections.getConfigBase("connection_2");
        assertThat(connection2.getInt("sourceID")).isEqualTo(12);
        assertThat(connection2.getInt("destID")).isEqualTo(11);
        assertThat(connection2.getInt("sourcePort")).isEqualTo(1);
        assertThat(connection2.getInt("destPort")).isEqualTo(2);

        var editorsettings = XML.getConfigBase(IOConst.WORKFLOW_EDITOR_SETTINGS_KEY.get());
        assertThat(editorsettings.getBoolean(IOConst.WORKFLOW_EDITOR_SHOWGRID_KEY.get())).isFalse();
        assertThat(editorsettings.getDouble(IOConst.WORKFLOW_EDITOR_ZOOM_LEVEL_KEY.get())).isEqualTo(1.0);
        assertThat(editorsettings.getInt(IOConst.WORKFLOW_EDITOR_CONNECTION_WIDTH_KEY.get())).isEqualTo(2);
    }

    /**
     * Test that for every child node, there is a corresponding configuration entry and file
     *
     * @throws InvalidSettingsException
     */
    @Test
    void testChildNodes() throws InvalidSettingsException {
        var nodes = XML.getConfigBase(IOConst.WORKFLOW_NODES_KEY.get());
        assertThat(nodes.getChildCount()).isEqualTo(7);
        for (var ix : List.of(7, 8, 10, 11, 12, 13, 14)) {
            var key = IOConst.WORKFLOW_NODE_PREFIX.get() + ix;
            assertThat(nodes.containsKey(key)).isTrue();
            var node = nodes.getConfigBase(key);
            assertThat(node.getInt(IOConst.ID_KEY.get())).isEqualTo(ix);
            assertThat(new File(OUTPUT_DIRECTORY, node.getString(IOConst.NODE_SETTINGS_FILE.get()))).isFile().exists()
                .isNotEmpty();
        }

        var node14 = nodes.getConfigBase("node_14");
        assertThat(
            node14.getConfigBase(IOConst.UI_SETTINGS_KEY.get()).getIntArray(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get()))
                .containsExactly(275, 511, 80, 77);
        assertThat(node14.getString(IOConst.WORKFLOW_NODES_NODE_TYPE_KEY.get())).isEqualTo("SubNode");
        assertThat(node14.getBoolean(IOConst.WORKFLOW_NODES_NODE_IS_META_KEY.get())).isTrue();

        var node11 = nodes.getConfigBase("node_11");
        assertThat(
            node11.getConfigBase(IOConst.UI_SETTINGS_KEY.get()).getIntArray(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get()))
                .containsExactly(484, 317, 84, 77);
        assertThat(node11.getString(IOConst.WORKFLOW_NODES_NODE_TYPE_KEY.get())).isEqualTo("NativeNode");
        assertThat(node11.getBoolean(IOConst.WORKFLOW_NODES_NODE_IS_META_KEY.get())).isFalse();
    }

    @AfterAll
    static void cleanUp() throws IOException {
        FileUtils.deleteDirectory(OUTPUT_DIRECTORY);
    }
}
