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

import org.knime.core.node.config.base.ConfigBase;
import org.knime.shared.workflow.def.ConfigurableNodeDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;

/**
 * This class holds methods that extend given settings by the attributes of a {@link ConfigurableNodeDef}
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
abstract class SingleNodeSaver extends BaseNodeSaver {

    private final ConfigurableNodeDef m_singleNode;

    SingleNodeSaver(final ConfigurableNodeDef singleNode) {
        super(singleNode);
        m_singleNode = singleNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void addNodeSettings(final ConfigBase nodeSettings) {
        nodeSettings.addString(IOConst.NODE_FILE_KEY.get(), IOConst.NODE_SETTINGS_FILE_NAME.get());
        nodeSettings.addEntry(SaverUtils.toConfigEntry(m_singleNode.getInternalNodeSubSettings(),
            IOConst.INTERNAL_NODE_SUBSETTINGS.get()));
        nodeSettings.addEntry(SaverUtils.toConfigEntry(m_singleNode.getModelSettings(), IOConst.MODEL_KEY.get()));
        nodeSettings
            .addEntry(SaverUtils.toConfigEntry(m_singleNode.getVariableSettings(), IOConst.VARIABLES_KEY.get()));

        super.addNodeSettings(nodeSettings);
    }

}
