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
 *   9 Feb 2022 (Dionysios Stolis): created
 */
package org.knime.shared.workflow.storage.multidir.loader;

import static org.knime.shared.workflow.storage.multidir.util.LoaderUtils.DEFAULT_CONFIG_MAP;
import static org.knime.shared.workflow.storage.multidir.util.LoaderUtils.DEFAULT_EMPTY_STRING;
import static org.knime.shared.workflow.storage.multidir.util.LoaderUtils.DEFAULT_NEGATIVE_INDEX;
import static org.knime.shared.workflow.storage.multidir.util.LoaderUtils.DEFAULT_TEMPLATE_LINK;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.util.LoadVersion;
import org.knime.core.util.workflow.def.FallibleSupplier;
import org.knime.core.workflow.def.BaseNodeDef;
import org.knime.core.workflow.def.ComponentDialogSettingsDef;
import org.knime.core.workflow.def.ComponentMetadataDef;
import org.knime.core.workflow.def.ComponentNodeDef;
import org.knime.core.workflow.def.PortDef;
import org.knime.core.workflow.def.impl.ComponentDialogSettingsDefBuilder;
import org.knime.core.workflow.def.impl.ComponentMetadataDefBuilder;
import org.knime.core.workflow.def.impl.ComponentNodeDefBuilder;
import org.knime.core.workflow.def.impl.DefaultComponentNodeDef;
import org.knime.core.workflow.def.impl.PortDefBuilder;
import org.knime.core.workflow.def.impl.PortTypeDefBuilder;
import org.knime.core.workflow.def.impl.WorkflowDefBuilder;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;

/**
 * Loads the description of a Component into {@link ComponentNodeDef}. Components are internally also referred to as
 * SubNodes.
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
public final class ComponentNodeLoader {

    private ComponentNodeLoader() {
    }

    private static final byte[] DEFAULT_ICON = new byte[0];

    private static final PortDef DEFAULT_PORT_DEF = new PortDefBuilder().build();

    /**
     * Loads the properties of a Component into {@link DefaultComponentDef}, each loader stores the loading exceptions
     * using the {@link FallibleSupplier}.
     *
     * @param workflowConfig a read only representation of the workflow.knime.
     * @param nodeDirectory a {@link File} of the node folder.
     * @param workflowFormatVersion an {@link LoadVersion}.
     * @return a {@link DefaultComponentDef}
     * @throws IOException whether the settings.xml can't be found.
     */
    public static DefaultComponentNodeDef load(final ConfigBaseRO workflowConfig, final File nodeDirectory,
        final LoadVersion workflowFormatVersion) throws IOException {
        var componentConfig = LoaderUtils.readNodeConfigFromFile(nodeDirectory);

        var builder = new ComponentNodeDefBuilder() //
            .setNodeType(BaseNodeDef.NodeTypeEnum.COMPONENT_NODE)//
            .setDialogSettings(loadDialogSettings(componentConfig)) //
            .setVirtualInNodeId(() -> loadVirtualInNodeId(componentConfig), DEFAULT_NEGATIVE_INDEX) //
            .setVirtualOutNodeId(() -> loadVirtualOutNodeId(componentConfig), DEFAULT_NEGATIVE_INDEX) //
            .setDialogSettings(() -> loadDialogSettings(componentConfig), null)
            // The template.knime is redundant for the Components, settings.xml contains the template information.
            .setTemplateInfo(() -> LoaderUtils.loadTemplateLink(componentConfig), DEFAULT_TEMPLATE_LINK) //
            .setMetadata(() -> loadMetadata(componentConfig), new ComponentMetadataDefBuilder() //
                .build()) //
            .setWorkflow(() -> WorkflowLoader.load(nodeDirectory, workflowFormatVersion),
                new WorkflowDefBuilder().build()) //
            // single node properties
            .setInternalNodeSubSettings(() -> SingleNodeLoader.loadInternalNodeSubSettings(componentConfig),
                DEFAULT_CONFIG_MAP) //
            .setModelSettings(() -> SingleNodeLoader.loadModelSettings(componentConfig), DEFAULT_CONFIG_MAP) //
            .setVariableSettings(() -> SingleNodeLoader.loadVariableSettings(componentConfig), DEFAULT_CONFIG_MAP)
            // base node properties
            .setId(() -> BaseNodeLoader.loadNodeId(workflowConfig), BaseNodeLoader.getRandomNodeID()) //
            .setAnnotation(() -> BaseNodeLoader.loadAnnotation(componentConfig, workflowFormatVersion),
                BaseNodeLoader.DEFAULT_NODE_ANNOTATION) //
            .setCustomDescription(
                () -> BaseNodeLoader.loadCustomDescription(workflowConfig, componentConfig, workflowFormatVersion),
                DEFAULT_EMPTY_STRING) //
            .setJobManager(() -> BaseNodeLoader.loadJobManager(componentConfig), BaseNodeLoader.DEFAULT_JOB_MANAGER) //
            .setLocks(BaseNodeLoader.loadLocks(workflowConfig, workflowFormatVersion)) //
            .setUiInfo(BaseNodeLoader.loadUIInfo(workflowConfig, workflowFormatVersion));

        setInPorts(builder, componentConfig);
        setOutPorts(builder, componentConfig);

        return builder.build();

    }

    /**
     * Loads the input ports from the {@code componentConfig}, and set them into the {@code builder}.
     *
     * @param builder an instance of the current {@link ComponentDefBuilder}.
     * @param componentConfig a read only representation of the component's settings.xml.
     */
    private static void setInPorts(final ComponentNodeDefBuilder builder, final ConfigBaseRO componentConfig) {
        try {
            var inPortsSettings = loadSetting(componentConfig, IOConst.INPORTS_KEY.get());
            if (inPortsSettings != null) {
                inPortsSettings.keySet() //
                    .forEach(key -> builder.addToInPorts(() -> loadPort(inPortsSettings.getConfigBase(key)),
                        DEFAULT_PORT_DEF));
            }
        } catch (InvalidSettingsException ex) {
            builder.setInPorts(() -> {
                throw ex;
            });
        }
    }

    /**
     * Loads the out ports from the {@code componentConfig}, and set them into the {@code builder}.
     *
     * @param builder an instance of the current {@link ComponentDefBuilder}.
     * @param componentConfig a read only representation of the component's settings.xml.
     */
    private static void setOutPorts(final ComponentNodeDefBuilder builder, final ConfigBaseRO componentConfig) {
        try {
            var inPortsSettings = loadSetting(componentConfig, IOConst.OUTPORTS_KEY.get());
            if (inPortsSettings != null) {
                inPortsSettings.keySet() //
                    .forEach(key -> builder.addToOutPorts(() -> loadPort(inPortsSettings.getConfigBase(key)),
                        DEFAULT_PORT_DEF));
            }
        } catch (InvalidSettingsException ex) {
            builder.setOutPorts(() -> {
                throw ex;
            });
        }
    }

    /**
     * Loads the component's dialog settings from the {@code settings}
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return a {@link ComponentDialogSettingsDef}
     */
    private static ComponentDialogSettingsDef loadDialogSettings(final ConfigBaseRO settings) {
        return new ComponentDialogSettingsDefBuilder() //
            .setLayoutJSON(settings.getString(IOConst.LAYOUT_JSON_KEY.get(), DEFAULT_EMPTY_STRING)) //
            .setConfigurationLayoutJSON(
                settings.getString(IOConst.CONFIGURATION_LAYOUT_JSON_KEY.get(), DEFAULT_EMPTY_STRING)) //
            .setCssStyles(settings.getString(IOConst.CUSTOM_CSS_KEY.get(), DEFAULT_EMPTY_STRING)) //
            .setHideInWizard(settings.getBoolean(IOConst.HIDE_IN_WIZARD_KEY.get(), false)) //
            .build();
    }

    /**
     * Loads the virtual input node id from the {@code settings}.
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return an {@link Integer}
     * @throws InvalidSettingsException when the virtual input id is less than zero.
     */
    private static Integer loadVirtualInNodeId(final ConfigBaseRO settings) throws InvalidSettingsException {
        var virtualId = settings.getInt(IOConst.VIRTUAL_IN_ID_KEY.get());
        return virtualId;
    }

    /**
     * Loads the virtual output node id from the {@code settings}.
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return an {@link Integer}
     * @throws InvalidSettingsException when the virtual output id is less than zero.
     */
    private static Integer loadVirtualOutNodeId(final ConfigBaseRO settings) throws InvalidSettingsException {
        var virtualId = settings.getInt(IOConst.VIRTUAL_OUT_ID_KEY.get());
        return virtualId;
    }

    /**
     * Loads the component's metadata from the {@code settings}
     *
     * @param settings a read only representation of the node's settings.xml.
     * @return a {@link ComponenetMetadataDef}
     * @throws InvalidSettingsException
     */
    private static ComponentMetadataDef loadMetadata(final ConfigBaseRO settings) throws InvalidSettingsException {
        if (!settings.containsKey(IOConst.DESCRIPTION_KEY.get())) {
            return new ComponentMetadataDefBuilder().build();
        }

        var metadataSettings = settings.getConfigBase(IOConst.METADATA_KEY.get());
        return new ComponentMetadataDefBuilder() //
            .setDescription(() -> metadataSettings.getString(IOConst.DESCRIPTION_KEY.get()), DEFAULT_EMPTY_STRING) //
            .setIcon(() -> loadIcon(metadataSettings), DEFAULT_ICON) //
            .setInPortDescriptions(
                () -> loadPortsProperties(metadataSettings, IOConst.INPORTS_KEY.get(), IOConst.DESCRIPTION_KEY.get())) //
            .setInPortNames(
                () -> loadPortsProperties(metadataSettings, IOConst.INPORTS_KEY.get(), IOConst.METADATA_NAME_KEY.get())) //
            .setOutPortDescriptions(
                () -> loadPortsProperties(metadataSettings, IOConst.OUTPORTS_KEY.get(), IOConst.DESCRIPTION_KEY.get())) //
            .setOutPortNames(
                () -> loadPortsProperties(metadataSettings, IOConst.OUTPORTS_KEY.get(), IOConst.METADATA_NAME_KEY.get())) //
            .build();
    }

    private static List<String> loadPortsProperties(final ConfigBaseRO sub, final String portsKey,
        final String propertyKey) throws InvalidSettingsException {
        //        try {
        var inportsSetting = loadSetting(sub, portsKey);
        if (inportsSetting == null) {
            return List.of();
        }

        var treeMap = new TreeMap<>();
        for (var key : inportsSetting.keySet()) {
            var portSetting = inportsSetting.getConfigBase(key);
            var index = portSetting.getInt(IOConst.PORT_INDEX_KEY.get(), DEFAULT_NEGATIVE_INDEX);
            treeMap.put(index, portSetting.getString(propertyKey));
        }
        return treeMap.values().stream() //
            .map(Object::toString).collect(Collectors.toList());
        //
        //            String[] properties = new String[inportsSetting.keySet().size()]; //NOSONAR
        //            inportsSetting.keySet().stream().forEach(key -> {
        //                try {
        //                    var portSetting = inportsSetting.getConfigBase(key);
        //                    var index = portSetting.getInt(Const.PORT_INDEX_KEY.get(), DEFAULT_NEGATIVE_INDEX);
        //                    properties[index] = portSetting.getString(propertyKey);
        //                } catch (InvalidSettingsException ex) {
        //                    throw new RuntimeException(ex);
        //                }
        //            });
        //            return List.of(properties);
        //        } catch (Exception e) {
        //            var errorMessage =
        //                String.format("Unable to load the component's %s %s: %s", portsKey, propertyKey, e.getMessage());
        //            throw new InvalidSettingsException(errorMessage, e);
        //        }
    }

    private static ConfigBaseRO loadSetting(final ConfigBaseRO sub, final String key) throws InvalidSettingsException {
        try {
            if (sub.containsKey(key)) {
                return sub.getConfigBase(key);
            } else {
                return null;
            }
        } catch (InvalidSettingsException e) {
            var errorMessage = String.format("Unable to load the %s: %s", key, e.getMessage());
            throw new InvalidSettingsException(errorMessage, e);
        }
    }

    private static byte[] loadIcon(final ConfigBaseRO sub) throws InvalidSettingsException {
        if (sub.containsKey(IOConst.ICON_KEY.get())) {
            return DEFAULT_ICON;
        }
        return Base64.getDecoder().decode(sub.getString(IOConst.ICON_KEY.get()));
    }

    private static PortDef loadPort(final ConfigBaseRO settings) {
        if (settings == null) {
            return DEFAULT_PORT_DEF;
        }
        return new PortDefBuilder() //
            .setIndex(() -> settings.getInt(IOConst.PORT_INDEX_KEY.get()), DEFAULT_NEGATIVE_INDEX) //
            .setName(() -> settings.getKey(), DEFAULT_EMPTY_STRING) //
            .setPortType(() -> new PortTypeDefBuilder() //
                .setPortObjectClass(settings.getConfigBase(IOConst.PORT_TYPE_KEY.get()) //
                    .getString(IOConst.PORT_OBJECT_CLASS_KEY.get()))
                .build(), new PortTypeDefBuilder().build()) //
            .build();
    }

}
