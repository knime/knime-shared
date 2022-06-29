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
 *   31 Mar 2022 (Dionysios Stolis): created
 */
package org.knime.shared.workflow.storage.multidir.loader;

import static org.knime.shared.workflow.storage.multidir.util.LoaderUtils.DEFAULT_CONFIG_MAP;
import static org.knime.shared.workflow.storage.multidir.util.LoaderUtils.DEFAULT_EMPTY_STRING;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.ConfigDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.CredentialPlaceholderDef;
import org.knime.shared.workflow.def.FallibleSupplier;
import org.knime.shared.workflow.def.FlowVariableDef;
import org.knime.shared.workflow.def.RootWorkflowDef;
import org.knime.shared.workflow.def.StandaloneDef;
import org.knime.shared.workflow.def.impl.CredentialPlaceholderDefBuilder;
import org.knime.shared.workflow.def.impl.DefaultStandaloneDef;
import org.knime.shared.workflow.def.impl.FlowVariableDefBuilder;
import org.knime.shared.workflow.def.impl.RootWorkflowDefBuilder;
import org.knime.shared.workflow.def.impl.StandaloneDefBuilder;
import org.knime.shared.workflow.def.impl.WorkflowUISettingsDefBuilder;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;
import org.knime.shared.workflow.storage.util.PasswordRedactor;

/**
 * Loads the description of stand alone units, a stand alone unit could be a Component, Metanode or Workflow
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 * @author Carl Witt, KNIME GmbH, Berlin, Germany
 */
public final class StandaloneLoader {

    private StandaloneLoader() {
    }

    private static final CredentialPlaceholderDef DEFAULT_CREDENTIAL_PLACEHOLDER =
        new CredentialPlaceholderDefBuilder().build();

    private static final FlowVariableDef DEFAULT_FLOW_VARIABLE = new FlowVariableDefBuilder().build();

    /**
     * Loads the properties of either of Component, Metanode or Workflow into {@link FallibleStandaloneDef}, each loader
     * stores the loading exceptions using the {@link FallibleSupplier}.
     *
     * @param directory a {@link File} of the unit's folder.
     * @return a {@link DefaultComponentDef}
     * @throws IOException
     */
    public static DefaultStandaloneDef load(final File directory) throws IOException {
        var builder = new StandaloneDefBuilder();
        var workflowConfig = LoaderUtils.readWorkflowConfigFromFile(directory);
        var templateConfig = LoaderUtils.readTemplateConfigFromFile(directory);
        try {
            //TODO Use the unknownpolicy here
            var contentType = getStandaloneType(directory);
            var loadVersion = LoadVersion.fromVersionString((templateConfig.isPresent())
                ? templateConfig.get().getString("version") : workflowConfig.getString("version"));

            builder.setContentType(contentType) //
                .setCreator(
                    CreatorLoader.loadCreator((templateConfig.isPresent()) ? templateConfig.get() : workflowConfig)); //
            switch (contentType) {
                case COMPONENT:
                    builder.setContents(ComponentNodeLoader.load(workflowConfig, directory, loadVersion, true));
                    break;
                case METANODE:
                    builder.setContents(MetaNodeLoader.load(workflowConfig, directory, loadVersion, true));
                    break;
                case ROOT_WORKFLOW:
                    builder.setContents(loadRootWorkflow(workflowConfig, directory, loadVersion));
                    break;
                default:
                    throw new InvalidSettingsException(String.format("Content type %s is not supported", contentType));
            }
        } catch (InvalidSettingsException e) {
            builder.setCreator(() -> {
                throw e;
            }, null);
        }
        return builder.build();
    }

    private static StandaloneDef.ContentTypeEnum getStandaloneType(final File directory) {
        if (new File(directory, IOConst.NODE_SETTINGS_FILE_NAME.get()).exists()) {
            return StandaloneDef.ContentTypeEnum.COMPONENT;
        } else if (new File(directory, IOConst.TEMPLATE_FILE_NAME.get()).exists()) {
            return StandaloneDef.ContentTypeEnum.METANODE;
        } else {
            return StandaloneDef.ContentTypeEnum.ROOT_WORKFLOW;
        }
    }

    /**
     *
     * Loads the stand alone workflow settings from the {@code settings}, into the {@link RootWorkflowDef}
     *
     * @param workflowConfig a read only representation of the node's settings.xml.
     * @param loadVersion a {@link LoadVersion}
     * @return a {@link RootWorkflowDef}.
     * @throws InvalidSettingsException
     */
    private static RootWorkflowDef loadRootWorkflow(final ConfigBaseRO workflowConfig, final File directory,
        final LoadVersion loadVersion) {

        var builder = new RootWorkflowDefBuilder() //
            .setName(() -> WorkflowLoader.loadName(workflowConfig, loadVersion))//
            .setAuthorInformation(() -> WorkflowLoader.loadAuthorInformation(workflowConfig, loadVersion))
            .setWorkflowEditorSettings(() -> WorkflowLoader.loadWorkflowUISettings(workflowConfig, loadVersion),
                new WorkflowUISettingsDefBuilder().build())
            .setTableBackendSettings(() -> loadTableBackendSettings(workflowConfig), DEFAULT_CONFIG_MAP);
        setCredentialPlaceholders(builder, workflowConfig, loadVersion);
        setWorkflowVariables(builder, workflowConfig, loadVersion);

        WorkflowLoader.setNodes(builder::putToNodes, builder::setNodes, workflowConfig, directory, loadVersion);
        WorkflowLoader.setConnections(builder::addToConnections, builder::setConnections, workflowConfig, loadVersion);
        WorkflowLoader.setAnnotations(builder::putToAnnotations, builder::setAnnotations, workflowConfig, loadVersion);

        return builder.build();
    }

    /**
     * Set the credential place holders into the {@code builder}
     *
     * @param builder
     * @param workflowConfig
     * @param loadVersion
     */
    private static void setCredentialPlaceholders(final RootWorkflowDefBuilder builder,
        final ConfigBaseRO workflowConfig, final LoadVersion loadVersion) {
        if (loadVersion.isOlderThan(LoadVersion.V220)) {
            builder.setCredentialPlaceholders(List.of());
        }
        try {
            var credentialSettings = workflowConfig.getConfigBase(IOConst.CREDENTIAL_PLACEHOLDERS_KEY.get());
            if (credentialSettings != null) {
                credentialSettings.keySet() //
                    .forEach(key -> builder.addToCredentialPlaceholders(
                        () -> loadCredentialPlaceholder(credentialSettings.getConfigBase(key)),
                        DEFAULT_CREDENTIAL_PLACEHOLDER));
            }
        } catch (InvalidSettingsException ex) {
            builder.setCredentialPlaceholders(() -> {
                throw ex;
            });
        }
    }

    /**
     * Loads the table backend settings from the {@code settings} to a {@link ConfigMapDef}
     *
     * @param workflowConfig a read-only representation of the node's settings.xml
     * @return The generated {@link ConfigMapDef}
     * @throws InvalidSettingsException
     */
    private static ConfigMapDef loadTableBackendSettings(final ConfigBaseRO workflowConfig)
        throws InvalidSettingsException {
        if (workflowConfig.containsKey(IOConst.TABLE_BACKEND_KEY.get())) {
            return LoaderUtils.toConfigMapDef(workflowConfig.getConfigBase(IOConst.TABLE_BACKEND_KEY.get()));
        }
        return null;
    }

    /**
     * Set the global workflow's flow variable from the {@code workflowConfig} to the {@code builder}.
     *
     * @param builder
     * @param workflowConfig
     * @param loadVersion
     */
    private static void setWorkflowVariables(final RootWorkflowDefBuilder builder, final ConfigBaseRO workflowConfig,
        final LoadVersion loadVersion) {
        if (loadVersion.isOlderThan(LoadVersion.V200)
            || !workflowConfig.containsKey(IOConst.WORKFLOW_VARIABLES_KEY.get())) {
            builder.setFlowVariables(List.of());
        }
        try {
            var workflowVarSettings = workflowConfig.getConfigBase(IOConst.WORKFLOW_VARIABLES_KEY.get());
            if (workflowVarSettings != null) {
                workflowVarSettings.keySet() //
                    .forEach(key -> builder.addToFlowVariables(
                        () -> loadFlowVariable(workflowVarSettings.getConfigBase(key)), DEFAULT_FLOW_VARIABLE));
            }
        } catch (InvalidSettingsException ex) {
            builder.setFlowVariables(() -> {
                throw ex;
            });
        }
    }

    private static FlowVariableDef loadFlowVariable(final ConfigBase workflowVarSettings) {
        return new FlowVariableDefBuilder() //
            .setName(workflowVarSettings.getString(IOConst.FLOW_VARIABLE_NAME_KEY.get(), DEFAULT_EMPTY_STRING))//
            .setValue(() -> loadFlowVariableValue(workflowVarSettings), null) //
            .setPropertyClass(
                workflowVarSettings.getString(IOConst.FLOW_VARIABLE_CLASS_KEY.get(), DEFAULT_EMPTY_STRING))//
            .build();
    }

    private static ConfigDef loadFlowVariableValue(final ConfigBase workflowConfig)
        throws InvalidSettingsException {
        if (workflowConfig.containsKey(IOConst.FLOW_VARIABLE_VALUE_KEY.get())) {
            var valueKey = IOConst.FLOW_VARIABLE_VALUE_KEY.get();
            return LoaderUtils.toConfigDef(workflowConfig.getEntry(valueKey), valueKey, PasswordRedactor.unsafe());
        }
        return null;
    }

    private static CredentialPlaceholderDef loadCredentialPlaceholder(final ConfigBaseRO credentialConfig)
        throws InvalidSettingsException {
        return new CredentialPlaceholderDefBuilder()//
            .setName(credentialConfig.getString(IOConst.CREDENTIAL_PLACEHOLDER_NAME_KEY.get()))//
            .setLogin(credentialConfig.getString(IOConst.CREDENTIAL_PLACEHOLDER_LOGIN_KEY.get()))//
            .build();
    }
}
