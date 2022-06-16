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
 *   27 Apr 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.shared.workflow.def.CreatorDef;
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;

/**
 * This class will invoke the {@link WorkflowSaver} but pass the attributes of a {@link MetaNodeDef}, such that they are
 * saved to workflow.knime
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
final class MetaNodeSaver extends BaseNodeSaver {

    private final MetaNodeDef m_metaNode;

    private CreatorDef m_creator;

    MetaNodeSaver(final MetaNodeDef metaNode) {
        super(metaNode);
        m_metaNode = metaNode;
    }

    MetaNodeSaver(final MetaNodeDef metaNode, final CreatorDef creator) {
        this(metaNode);
        m_creator = creator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void save(final File metaNodeDirectory, final ConfigBase parentWorkflowNodeSettings) throws IOException {
        if (parentWorkflowNodeSettings != null) { // then it's not a standalone and there is a parent config
            var metaNodeSettingsFile = new File(metaNodeDirectory, IOConst.WORKFLOW_FILE_NAME.get());
            var settingsFilePath = metaNodeDirectory.getParentFile().toURI().relativize(metaNodeSettingsFile.toURI());
            parentWorkflowNodeSettings.addString(IOConst.NODE_SETTINGS_FILE.get(), settingsFilePath.getPath());
            addParentWorkflowNodeSettings(parentWorkflowNodeSettings);
        }

        var workflowSaver = new WorkflowSaver(m_metaNode.getWorkflow(), m_creator);
        workflowSaver.save(metaNodeDirectory, this::addNodeSettings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void addNodeSettings(final ConfigBase nodeSettings) {
        super.addNodeSettings(nodeSettings);
        SaverUtils.addTemplateInfo(nodeSettings, m_metaNode.getTemplateLink(), m_metaNode.getTemplateMetadata(), m_metaNode.getNodeType());
        addMetaInports(nodeSettings);
        addMetaOutports(nodeSettings);
    }

    private void addMetaInports(final ConfigBase nodeSettings) {
        if (m_metaNode.getInPorts().isEmpty()) {
            return;
        }
        var metaInPorts = new SimpleConfig(IOConst.META_IN_PORTS_KEY.get());
        m_metaNode.getInPortsBarBounds().ifPresent(info -> SaverUtils.addUiInfo(metaInPorts, info));
        var portEnum = new SimpleConfig(IOConst.PORT_ENUM_KEY.get());
        m_metaNode.getInPorts()
            .ifPresent(ps -> ps.forEach(p -> SaverUtils.addPort(portEnum, IOConst.INPORT_PREFIX.get(), p)));
        metaInPorts.addEntry(portEnum);
        nodeSettings.addEntry(metaInPorts);
    }

    private void addMetaOutports(final ConfigBase nodeSettings) {
        if (m_metaNode.getOutPorts().isEmpty()) {
            return;
        }
        var metaOutPorts = new SimpleConfig(IOConst.META_OUT_PORTS_KEY.get());
        m_metaNode.getOutPortsBarBounds().ifPresent(uiInfo -> SaverUtils.addUiInfo(metaOutPorts, uiInfo));
        var portEnum = new SimpleConfig(IOConst.PORT_ENUM_KEY.get());
        m_metaNode.getOutPorts()
            .ifPresent(ps -> ps.forEach(p -> SaverUtils.addPort(portEnum, IOConst.OUTPORT_PREFIX.get(), p)));
        metaOutPorts.addEntry(portEnum);
        nodeSettings.addEntry(metaOutPorts);
    }
}
