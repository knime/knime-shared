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
 *   5 Apr 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.util.LoadVersion;
import org.knime.core.workflow.def.NativeNodeDef;
import org.knime.shared.workflow.storage.multidir.loader.NativeNodeLoader;
import org.knime.shared.workflow.storage.multidir.loader.NodeLoaderTestUtils;
import org.knime.shared.workflow.storage.multidir.saver.NativeNodeSaver;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;

/**
 * Tests the NativeNodeSaver by executing load-save-load round trips and making sure no information is lost
 *
 * @author Jasper Krauter, KNIME AG, Zurich, Switzerland
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class NativeNodeSaverTest {

    private static final Set<String> excludedKeys =
        new HashSet<>(Arrays.asList("flow_stack", "state", "hasContent", "isInactive", "ports", "filestores"));

    /**
     * Load-Save-Load tests for native nodes
     *
     * @throws IOException If the test resource directories cannot be accessed
     */
    @Test
    void testNativeNodeRoundtrips() throws IOException {
        testSingleNativeNodeRoundtrip("Component_Template", "Call Workflow Service (#2)");
        testSingleNativeNodeRoundtrip("Metanode_Template", "Test Data Generator (#1)");
        testSingleNativeNodeRoundtrip("Workflow_Test", "Table Creator");
        testSingleNativeNodeRoundtrip("Workflow_Test", "Concatenate (#11)");
        testSingleNativeNodeRoundtrip("Workflow_Test/Airlines Rou (#8)", "CSV Reader (#1)");
        testSingleNativeNodeRoundtrip("Workflow_Test/Airlines Rou (#8)", "Joiner (#5)");
    }

    /**
     * Load-save-load for a single native node.
     *
     * @throws IOException If the test resource directories cannot be accessed
     */
    void testSingleNativeNodeRoundtrip(final String workflowDirName, final String nodeDirName) throws IOException {
        // given
        var workflowDirectory = NodeLoaderTestUtils.readResourceFolder(workflowDirName);
        var workflowConfig = LoaderUtils.readWorkflowConfigFromFile(workflowDirectory);
        var nodeDirectory = new File(workflowDirectory, nodeDirName);

        // the original settings.xml
        File inputFile = new File(nodeDirectory, IOConst.NODE_SETTINGS_FILE_NAME.get());
        ConfigBase beforeSave = (ConfigBase)SimpleConfig.parseConfig("", inputFile);

        NativeNodeDef nativeNodeDef = NativeNodeLoader.load(workflowConfig, nodeDirectory, LoadVersion.V4010);
        // assume that `load` works, tested in NativeNodeLoaderTest

        // when
        var outputWorkflowDirectory = NodeLoaderTestUtils.readResourceFolder("Workflow_Test_Output");
        outputWorkflowDirectory.mkdir();

        // These settings will be properly tested in the WorkflowSaverTest
        var workflowNodeSettings = new SimpleConfig("node_" + nativeNodeDef.getId());

        var saver = new NativeNodeSaver(nativeNodeDef);
        saver.save(outputWorkflowDirectory, workflowNodeSettings);

        // then
        String outputDirname = String.format("%s (#%d)", nativeNodeDef.getNodeName(), nativeNodeDef.getId());

        // was the output dir created?
        File outputDir = new File(outputWorkflowDirectory, outputDirname);
        assertThat(outputDir).as("Check whether the output path exists").exists();
        assertThat(outputDir).as("Check whether the output path is a directory").isDirectory();

        // does the settings file exist?
        File outputFile = new File(outputDir, IOConst.NODE_SETTINGS_FILE_NAME.get());
        assertThat(outputFile).as("Check whether the output file exists").exists();
        assertThat(outputFile).as("Check whether the output file is a file").isFile();

        // The newly written settings.xml
        ConfigBase afterSave = (ConfigBase)SimpleConfig.parseConfig("", outputFile);

        // confirm that all settings are there
        assertThat(beforeSave).as("Check if no new keys have been introduced").containsAll(afterSave.keySet());

        for (var key : beforeSave.keySet()) {
            if (excludedKeys.contains(key)) {
                continue;
            }
            assertThat(afterSave).as("Check if key '%s' is present in output", key).contains(key);
            assertThat(beforeSave.getEntry(key)).as("Check if the contents of key '%s' match", key)
                .isEqualTo(afterSave.getEntry(key));
        }
    }

}
