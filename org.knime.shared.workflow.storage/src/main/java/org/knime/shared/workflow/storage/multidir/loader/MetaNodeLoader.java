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
 *   9 Feb 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.multidir.loader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.BoundsDef;
import org.knime.shared.workflow.def.FallibleSupplier;
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.def.PortDef;
import org.knime.shared.workflow.def.PortTypeDef;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.def.impl.DefaultMetaNodeDef;
import org.knime.shared.workflow.def.impl.DefaultPortTypeDef;
import org.knime.shared.workflow.def.impl.MetaNodeDefBuilder;
import org.knime.shared.workflow.def.impl.PortDefBuilder;
import org.knime.shared.workflow.def.impl.PortTypeDefBuilder;
import org.knime.shared.workflow.def.impl.WorkflowDefBuilder;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;

/**
 * Loads the description of a MetaNode into {@link MetaNodeDef}.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
public final class MetaNodeLoader {

    private MetaNodeLoader() {
    }

    private static final WorkflowDef DEFAULT_WORKFLOW = new WorkflowDefBuilder().strict().build();

    private static final DefaultPortTypeDef DEFAULT_PORT_TYPE =
        new PortTypeDefBuilder().strict().setPortObjectClass("org.knime.core.node.BufferedDataTable").build();

    private static final PortDef DEFAULT_PORT_DEF =
        new PortDefBuilder().strict().setIndex(-1).setPortType(DEFAULT_PORT_TYPE).build();

    /**
     * Loads the properties of a MetaNode in a workflow into {@link DefaultMetaNodeDef}, stores the loading exceptions
     * using the {@link FallibleSupplier}.
     *
     * @param nodeConfig a read only representation of the part of the workflow.knime that describes the node (e.g.,
     *            contains node id)
     * @param nodeDirectory a {@link File} of the node folder.
     * @param workflowFormatVersion an {@link LoadVersion}.
     * @param isStandalone whether true if the metanode is a template, false if it is part of a workflow
     * @return a {@link DefaultMetaNodeDef}
     * @throws IOException whether the settings.xml can't be found.
     */
    public static DefaultMetaNodeDef load(final ConfigBaseRO nodeConfig, final File nodeDirectory,
        final LoadVersion workflowFormatVersion, final boolean isStandalone) throws IOException {
        var metaNodeConfig = LoaderUtils.readWorkflowConfigFromFile(nodeDirectory);
        // if the template.knime doesn't exist the template information lives in the MetaNode's workflow.knime.
        var templateConfig = LoaderUtils.readTemplateConfigFromFile(nodeDirectory).orElseGet(() -> metaNodeConfig);

        var builder = new MetaNodeDefBuilder()//
            .setNodeType(BaseNodeDef.NodeTypeEnum.META) //
            .setWorkflow(() -> WorkflowLoader.load(nodeDirectory, workflowFormatVersion), DEFAULT_WORKFLOW)//
            .setInPortsBarBounds(
                () -> loadPortsBarUIInfo(metaNodeConfig, IOConst.META_IN_PORTS_KEY.get(), workflowFormatVersion)
                    .orElse(null))//
            .setOutPortsBarBounds(
                () -> loadPortsBarUIInfo(metaNodeConfig, IOConst.META_OUT_PORTS_KEY.get(), workflowFormatVersion)
                    .orElse(null))//
            .setTemplateLink(() -> LoaderUtils.loadTemplateLink(templateConfig).orElse(null)) //
            .setTemplateMetadata(() -> LoaderUtils.loadTemplateMetadata(templateConfig).orElse(null))//
            // base node properties
            .setAnnotation(() -> BaseNodeLoader.loadAnnotation(metaNodeConfig, workflowFormatVersion),
                BaseNodeLoader.DEFAULT_NODE_ANNOTATION) //
            .setCustomDescription(() -> BaseNodeLoader
                .loadCustomDescription(nodeConfig, metaNodeConfig, workflowFormatVersion).orElse(null)) //
            .setJobManager(() -> BaseNodeLoader.loadJobManager(metaNodeConfig).orElse(null)) //
            .setLocks(BaseNodeLoader.loadLocks(nodeConfig, workflowFormatVersion)) //
            .setBounds(() -> BaseNodeLoader.loadBoundsDef(nodeConfig, workflowFormatVersion).orElse(null));

        if(!isStandalone) {
            builder.setId(() -> BaseNodeLoader.loadNodeId(nodeConfig));
        }

        setInPorts(builder, metaNodeConfig, workflowFormatVersion);
        setOutPorts(builder, metaNodeConfig, workflowFormatVersion);

        return builder.build();
    }

    /**
     * Load the input/output ports bar ui info from the {@code settings}.
     *
     * @param settings
     * @param key whether to load input port or output port bar information.
     * @return a {@link NodeUIInfoDef}
     * @throws InvalidSettingsException
     */
    private static Optional<BoundsDef> loadPortsBarUIInfo(final ConfigBaseRO settings, final String key,
        final LoadVersion loadVersion) throws InvalidSettingsException {
        if (loadVersion.isOlderThan(LoadVersion.V200)) {
            return Optional.empty();
        }
        return loadNodeUIInformation(loadPortsSetting(settings, key, loadVersion), loadVersion);
    }

    private static ConfigBaseRO loadSetting(final ConfigBaseRO sub, final String key, final LoadVersion loadVersion)
        throws InvalidSettingsException {
        try {
            if (loadVersion.isOlderThan(LoadVersion.V200) || !sub.containsKey(key)) {
                return null;
            }
            return sub.getConfigBase(key);
        } catch (InvalidSettingsException e) {
            var errorMessage = String.format("Can't load workflow ports %s, config not found: %s", key, e.getMessage());
            throw new InvalidSettingsException(errorMessage, e);
        }
    }

    private static ConfigBaseRO loadPortsSettingsEnum(final ConfigBaseRO settings) throws InvalidSettingsException {
        if (settings == null || !settings.containsKey(IOConst.PORT_ENUM_KEY.get())) {
            return null;
        } else {
            return settings.getConfigBase(IOConst.PORT_ENUM_KEY.get());
        }
    }

    /**
     * Uses the builder to add the input ports defined in settings. Does not call anything on the builder if no ports
     * are defined.
     *
     * @param builder
     * @param settings
     * @param loadVersion
     */
    private static void setInPorts(final MetaNodeDefBuilder builder, final ConfigBaseRO settings,
        final LoadVersion loadVersion) {
        try {
            var inPortsSettings = loadSetting(settings, IOConst.META_IN_PORTS_KEY.get(), loadVersion);
            var inPortsEnum = loadPortsSettingsEnum(inPortsSettings);
            if (inPortsEnum != null) {
                inPortsEnum.keySet().forEach(
                    key -> builder.addToInPorts(() -> loadPort(inPortsEnum.getConfigBase(key)), DEFAULT_PORT_DEF));
            }
        } catch (InvalidSettingsException ex) {
            builder.setInPorts(() -> {
                throw ex;
            });
        }
    }

    /**
     * Uses the builder to add the output ports defined in settings. Does not call anything on the builder if no ports
     * are defined.
     *
     * @param builder
     * @param settings
     * @param loadVersion
     */
    private static void setOutPorts(final MetaNodeDefBuilder builder, final ConfigBaseRO settings,
        final LoadVersion loadVersion) {
        try {
            var inPortsSettings = loadSetting(settings, IOConst.META_OUT_PORTS_KEY.get(), loadVersion);
            var inPortsEnum = loadPortsSettingsEnum(inPortsSettings);
            if (inPortsEnum != null) {
                inPortsEnum.keySet().forEach(
                    key -> builder.addToOutPorts(() -> loadPort(inPortsEnum.getConfigBase(key)), DEFAULT_PORT_DEF));
            }
        } catch (InvalidSettingsException ex) {
            builder.setOutPorts(() -> {
                throw ex;
            });
        }
    }

    private static PortDef loadPort(final ConfigBaseRO settings) {
        if (settings == null) {
            return DEFAULT_PORT_DEF;
        }
        return new PortDefBuilder()//
            .setIndex(() -> settings.getInt(IOConst.PORT_INDEX_KEY.get()), -1) // Negative default index
            .setName(() -> settings.getString(IOConst.PORT_NAME_KEY.get()))//
            .setPortType(() -> loadPortTypeDef(settings), DEFAULT_PORT_TYPE) //
            .build();
    }

    private static PortTypeDef loadPortTypeDef(final ConfigBaseRO settings) throws InvalidSettingsException {
        return new PortTypeDefBuilder()//
            .setPortObjectClass(settings.getConfigBase(IOConst.PORT_TYPE_KEY.get()) //
                .getString(IOConst.PORT_OBJECT_CLASS_KEY.get()))//
            .build();
    }

    private static Optional<BoundsDef> loadNodeUIInformation(final ConfigBaseRO portSettings, final LoadVersion loadVersion)
        throws InvalidSettingsException {
        if (portSettings == null) {
            return Optional.empty();
        }

        // in previous releases, the settings were directly written to the
        // top-most node settings object; since 2.0 they are put into a
        // separate sub-settings object
        if (loadVersion.isOlderThan(LoadVersion.V200)) {
            return BaseNodeLoader.loadBoundsDef(portSettings, loadVersion);
        } else {
            if (!portSettings.containsKey(IOConst.UI_SETTINGS_KEY.get())) {
                return Optional.empty();
            } else {
                return BaseNodeLoader.loadBoundsDef(portSettings.getConfigBase(IOConst.UI_SETTINGS_KEY.get()),
                    loadVersion);
            }
        }
    }

    /**
     * @param key whether to load input port or output port settings
     * @return nullable
     * @throws InvalidSettingsException
     */
    private static ConfigBaseRO loadPortsSetting(final ConfigBaseRO settings, final String key,
        final LoadVersion loadVersion) throws InvalidSettingsException {
        if (loadVersion.isOlderThan(LoadVersion.V200) || !settings.containsKey(key)) {
            return null;
        }
        return settings.getConfigBase(key);
    }
}
