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
 *   7 Apr 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.ComponentNodeDef;
import org.knime.shared.workflow.def.CreatorDef;
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.def.NativeNodeDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;

/**
 * This class holds methods that extend given settings by the attributes of a {@link BaseNodeDef}
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
abstract class BaseNodeSaver {

    private final BaseNodeDef m_baseNode;

    BaseNodeSaver(final BaseNodeDef baseNode) {
        m_baseNode = baseNode;
    }

    /**
     * Get an instance of either a NativeNode-, MetaNode- or ComponentNodeSaver, depending on the provided node type
     *
     * @param node The input node that should be saved
     * @param creator Creator information that will be added to the MetaNode- and ComponentNodeSaver
     * @return A saver instance
     */
    static BaseNodeSaver getInstance(final BaseNodeDef node, final CreatorDef creator) {
        var type = node.getNodeType();
        if (type != null) {
            switch (type) {
                case NATIVENODE:
                    return new NativeNodeSaver((NativeNodeDef)node);
                case METANODE:
                    return new MetaNodeSaver((MetaNodeDef)node, creator);
                case COMPONENT:
                    return new ComponentNodeSaver((ComponentNodeDef)node, creator);
            }
            throw new IllegalStateException("Unknown node type " + type.toString() + " -- cannot instantiate saver");
        }
        throw new IllegalStateException("Node type cannot be null of node with id " + node.getId());
    }

    /**
     * Saves the description of the node to disk
     *
     * @param nodeDirectory An already created node directory
     * @param parentWorkflowNodeSettings The parent workflow settings entry corresponding to the node (can be null for
     *            standalones)
     * @throws IOException If there's a problem with writing to disk
     */
    abstract void save(final File nodeDirectory, final ConfigBase parentWorkflowNodeSettings) throws IOException;

    /**
     * Extends the node settings to include the properties of the node
     *
     * @param nodeSettings The {@link ConfigBase} of the settings.xml (or workflow.knime in case of meta node) file
     */
    void addNodeSettings(final ConfigBase nodeSettings) {
        SaverUtils.addAnnotationData(nodeSettings, m_baseNode.getAnnotation());
        nodeSettings.addString(IOConst.CUSTOM_DESCRIPTION_KEY.get(), m_baseNode.getCustomDescription());
        addJobManager(nodeSettings);
        addLocks(nodeSettings);
    }

    /**
     * Extends the entry in the parent workflow.knime file to contain the properties of the node
     *
     * @param parentWorkflowNodeSettings The {@link ConfigBase} of the corresponding entry (key is e.g. "node_42")
     */
    void addParentWorkflowNodeSettings(final ConfigBase parentWorkflowNodeSettings) {
        parentWorkflowNodeSettings.addInt(IOConst.ID_KEY.get(), m_baseNode.getId());
        var nodeType = m_baseNode.getNodeType();
        parentWorkflowNodeSettings.addString(IOConst.WORKFLOW_NODES_NODE_TYPE_KEY.get(),
            SaverUtils.nodeTypeString.get(nodeType));
        parentWorkflowNodeSettings.addBoolean(IOConst.WORKFLOW_NODES_NODE_IS_META_KEY.get(),
            SaverUtils.isNodeTypeMeta.get(nodeType));
        SaverUtils.addUiInfo(parentWorkflowNodeSettings, m_baseNode.getUiInfo());
    }

    private void addLocks(final ConfigBase nodeSettings) {
        // no sonar here because we assume the Boolean values to be non-null. Suppressing rule java:S5411.
        var nodeLocks = m_baseNode.getLocks();
        if (nodeLocks.hasConfigureLock()) {//NOSONAR
            nodeSettings.addBoolean(IOConst.HAS_CONFIGURE_LOCK_KEY.get(), true);
        }
        if (!nodeLocks.hasDeleteLock()) {//NOSONAR
            nodeSettings.addBoolean(IOConst.IS_DELETABLE_KEY.get(), true);
        }
        if (nodeLocks.hasResetLock()) {//NOSONAR
            nodeSettings.addBoolean(IOConst.HAS_RESET_LOCK_KEY.get(), true);
        }
    }

    private void addJobManager(final ConfigBase nodeSettings) {
        var jobManager = m_baseNode.getJobManager();
        if (jobManager.getSettings() != null) {
            var jobManagerSettings = new SimpleConfig(IOConst.JOB_MANAGER_KEY.get());
            jobManagerSettings.addString(IOConst.JOB_MANAGER_FACTORY_ID_KEY.get(), jobManager.getFactory());
            jobManagerSettings
                .addEntry(SaverUtils.toConfigEntry(jobManager.getSettings(), IOConst.JOB_MANAGER_SETTINGS_KEY.get()));
            nodeSettings.addEntry(jobManagerSettings);
        }
    }
}
