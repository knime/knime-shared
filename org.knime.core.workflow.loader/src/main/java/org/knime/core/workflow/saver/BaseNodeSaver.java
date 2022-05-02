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
package org.knime.core.workflow.saver;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.workflow.def.BaseNodeDef;
import org.knime.core.workflow.def.BoundsDef;
import org.knime.core.workflow.util.IOConst;
import org.knime.core.workflow.util.SaverUtils;

/**
 * This class holds methods that extend given settings by the attributes of a {@link BaseNodeDef}
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
abstract class BaseNodeSaver {

    private BaseNodeDef m_baseNode;

    BaseNodeSaver(final BaseNodeDef baseNode) {
        m_baseNode = baseNode;
    }

    /**
     * Saves the description of the node to disk
     *
     * @param workflowDirectory The parent workflow directory
     * @param workflowNodeSettings The parent workflow settings entry corresponding to the node
     * @throws IOException If there's a problem with writing to disk
     */
    abstract void save(final File workflowDirectory, final ConfigBase workflowNodeSettings) throws IOException;

    /**
     * Extends the node settings to include the properties of the base node
     */
    void addNodeSettings(final ConfigBase nodeSettings) {
        addAnnotationData(nodeSettings);
        nodeSettings.addString(IOConst.CUSTOM_DESCRIPTION_KEY.get(), m_baseNode.getCustomDescription());
        addJobManager(nodeSettings);
        addLocks(nodeSettings);
    }

    void addWorkflowNodeSettings(final ConfigBase workflowNodeSettings) {
        workflowNodeSettings.addInt(IOConst.ID_KEY.get(), m_baseNode.getId());
        addUiInfo(workflowNodeSettings);
    }

    private void addUiInfo(final ConfigBase workflowNodeSettings) {
        workflowNodeSettings.addString(IOConst.UI_CLASSNAME_KEY.get(), IOConst.UI_INFORMATION_CLASSNAME.get());

        var uiInfo = m_baseNode.getUiInfo();
        ConfigBase nodeUIConfig = new SimpleConfig(IOConst.UI_SETTINGS_KEY.get());

        BoundsDef bounds = uiInfo.getBounds();
        if (bounds != null) {
            var boundsArray = new int[4];
            boundsArray[0] = (bounds.getLocation() == null) ? 0 : bounds.getLocation().getX();
            boundsArray[1] = (bounds.getLocation() == null) ? 0 : bounds.getLocation().getY();
            boundsArray[2] = (bounds.getHeight() == null) ? 0 : bounds.getHeight();
            boundsArray[3] = (bounds.getWidth() == null) ? 0 : bounds.getWidth();
            nodeUIConfig.addIntArray(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get(), boundsArray);
        }
        workflowNodeSettings.addEntry(nodeUIConfig);
    }

    private void addLocks(final ConfigBase nodeSettings) {
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

    private void addAnnotationData(final ConfigBase nodeSettings) {
        var annotation = m_baseNode.getAnnotation();
        var annotationData = annotation.getData();
        if (annotationData != null && !annotation.isAnnotationDefault()) {
            nodeSettings
                .addEntry(SaverUtils.annotationToConfig(annotation.getData(), IOConst.NODE_ANNOTATION_KEY.get()));
        }
    }

    private void addJobManager(final ConfigBase nodeSettings) {
        var jobManager = m_baseNode.getJobManager();
        if (jobManager.getSettings() != null) {
            var jobManagerSettings = new SimpleConfig(IOConst.JOB_MANAGER_KEY.get());
            jobManagerSettings.addString(IOConst.JOB_MANAGER_FACTORY_ID_KEY.get(), jobManager.getFactory());
            jobManagerSettings
                .addEntry(SaverUtils.toConfigBase(jobManager.getSettings(), IOConst.JOB_MANAGER_SETTINGS_KEY.get()));
            nodeSettings.addEntry(jobManagerSettings);
        }
    }
}
