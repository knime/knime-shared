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
 *   31 Jan 2022 (Dionysios Stolis): created
 */
package org.knime.shared.workflow.storage.multidir.loader;

import static org.knime.shared.workflow.storage.multidir.util.LoaderUtils.DEFAULT_CONFIG_MAP;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.SingleNodeDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;

/**
 * Loads the description of a SingleNode into {@link SingleNodeDef}.
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 * @author Carl Witt, KNIME GmbH, Berlin, Germany
 */
final class SingleNodeLoader {

    private SingleNodeLoader() {
    }

    /**
     * Loads the internal node sub settings (e.g memory_policy) from the {@code settings}.
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return a {@link ConfigMapDef}
     * @throws InvalidSettingsException
     */
    static ConfigMapDef loadInternalNodeSubSettings(final ConfigBaseRO settings)
        throws InvalidSettingsException {
        if (!settings.containsKey(IOConst.INTERNAL_NODE_SUBSETTINGS.get())) {
            return DEFAULT_CONFIG_MAP;
        }
        return LoaderUtils.toConfigMapDef(settings.getConfigBase(IOConst.INTERNAL_NODE_SUBSETTINGS.get()));
    }

    /**
     * Loads the variable settings from the {@code settings}.
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return a {@link ConfigMapDef}
     * @throws InvalidSettingsException
     */
    static ConfigMapDef loadVariableSettings(final ConfigBaseRO settings) throws InvalidSettingsException {
        if (!settings.containsKey(IOConst.VARIABLES_KEY.get())) {
            return DEFAULT_CONFIG_MAP;
        }
        return LoaderUtils.toConfigMapDef(settings.getConfigBase(IOConst.VARIABLES_KEY.get()));
    }

    /**
     * Loads the model settings (e.g SettingsModelID) from the {@code settings}.
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return a {@link ConfigMapDef}
     * @throws InvalidSettingsException
     */
    static ConfigMapDef loadModelSettings(final ConfigBaseRO settings) throws InvalidSettingsException {
        if (!settings.containsKey(IOConst.MODEL_KEY.get())) {
            return DEFAULT_CONFIG_MAP;
        }
        // settings not present if node never had settings (different since 2.8 -- before the node always had settings,
        // defined by NodeModel#saveSettings -- these settings were never confirmed through validate/load though)
        return LoaderUtils.toConfigMapDef(settings.getConfigBase(IOConst.MODEL_KEY.get()));
    }
}
