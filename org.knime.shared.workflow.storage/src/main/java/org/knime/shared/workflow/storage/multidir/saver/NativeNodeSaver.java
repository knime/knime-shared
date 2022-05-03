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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigBaseWO;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.core.workflow.def.NativeNodeDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;

/**
 * Saves the description of a native node. Native nodes are also referred to as KNIME Nodes.
 *
 * @author Dionysios Stolis, KNIME AG, Zurich, Switzerland
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
final class NativeNodeSaver extends SingleNodeSaver {

    private NativeNodeDef m_nativeNode;

    /**
     * Creates a new {@code NativeNodeSaver} instance
     *
     * @param nativeNode The description of a native node
     */
    public NativeNodeSaver(final NativeNodeDef nativeNode) {
        super(nativeNode);
        m_nativeNode = nativeNode;
    }

    /**
     * Saves a nativeNode, i.e. creates its directory and writes its properties to an XML file in that directory. It
     * furthermore configures the entry that should be added to the enclosing workflow configuration.
     *
     * @throws IOException If there has been a read/write error
     */
    @Override
    void save(final File workflowDirectory, final ConfigBase workflowNodeSettings) throws IOException {
        addWorkflowNodeSettings(workflowNodeSettings);

        var nodeSettings = new SimpleConfig(IOConst.NODE_SETTINGS_FILE_NAME.get());
        addNodeSettings(nodeSettings);

        // create node directory: "<node name> (#<node id>)" inside the workflow directory
        var safeNodeName = SaverUtils.getValidFileName(m_nativeNode.getNodeName(), -1);
        var nativeNodeDirectory = SaverUtils.createNodeDir(workflowDirectory, safeNodeName, m_nativeNode.getId());

        // write as file to node directory (usually settings.xml)
        var nativeNodeSettingsFile = new File(nativeNodeDirectory, IOConst.NODE_SETTINGS_FILE_NAME.get());

        // add filepath to enclosing workflow.knime config
        var settingsFilePath = workflowDirectory.toURI().relativize(nativeNodeSettingsFile.toURI());
        workflowNodeSettings.addString(IOConst.NODE_SETTINGS_FILE.get(), settingsFilePath.getPath());

        try (var fos = new FileOutputStream(nativeNodeSettingsFile)) {
            XMLConfig.save(nodeSettings, fos);
        }
    }

    @Override
    void addWorkflowNodeSettings(final ConfigBase workflowNodeSettings) {
        // The following lines would be better off in the BaseNodeSaver, but then the order is mixed up.
        workflowNodeSettings.addString(IOConst.WORKFLOW_NODES_NODE_TYPE_KEY.get(),
            m_nativeNode.getNodeType().toString());
        workflowNodeSettings.addBoolean(IOConst.WORKFLOW_NODES_NODE_TYPE_KEY.get(),
            SaverUtils.isNodeTypeMeta(m_nativeNode.getNodeType()));
        super.addWorkflowNodeSettings(workflowNodeSettings);

    }

    @Override
    void addNodeSettings(final ConfigBase nodeSettings) {
        nodeSettings.addString(IOConst.FACTORY_KEY.get(), m_nativeNode.getFactory());
        nodeSettings.addString(IOConst.NODE_NAME_KEY.get(), m_nativeNode.getNodeName());
        saveBundle(nodeSettings);
        saveFeature(nodeSettings);
        SaverUtils.addEntryIfNotNull(nodeSettings, m_nativeNode.getFactorySettings(),
            IOConst.FACTORY_SETTINGS_KEY.get());
        SaverUtils.addEntryIfNotNull(nodeSettings, m_nativeNode.getNodeCreationConfig(),
            IOConst.NODE_CREATION_CONFIG_KEY.get());
        nodeSettings.addString(IOConst.METADATA_NAME_KEY.get(), m_nativeNode.getNodeName());
        super.addNodeSettings(nodeSettings);
    }

    private void saveBundle(final ConfigBaseWO nodeSettings) {
        var bundleVendor = m_nativeNode.getBundle();
        nodeSettings.addString(IOConst.NODE_BUNDLE_NAME_KEY.get(), bundleVendor.getName());
        nodeSettings.addString(IOConst.NODE_BUNDLE_SYMBOLIC_NAME_KEY.get(), bundleVendor.getSymbolicName());
        nodeSettings.addString(IOConst.NODE_BUNDLE_VENDOR_KEY.get(), bundleVendor.getVendor());
        nodeSettings.addString(IOConst.NODE_BUNDLE_VERSION_KEY.get(), bundleVendor.getVersion());
    }

    private void saveFeature(final ConfigBaseWO nodeSettings) {
        var featureVendor = m_nativeNode.getFeature();
        nodeSettings.addString(IOConst.NODE_FEATURE_NAME_KEY.get(), featureVendor.getName());
        nodeSettings.addString(IOConst.NODE_FEATURE_SYMBOLIC_NAME_KEY.get(), featureVendor.getSymbolicName());
        nodeSettings.addString(IOConst.NODE_FEATURE_VENDOR_KEY.get(), featureVendor.getVendor());
        nodeSettings.addString(IOConst.NODE_FEATURE_VERSION_KEY.get(), featureVendor.getVersion());
    }

}
