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
 *   28 Apr 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.util;

/**
 * Collection of constants that are used to save and load a workflow
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings("javadoc")
public enum IOConst {
        NODE_NAME_KEY("node-name"),
        NODE_BUNDLE_NAME_KEY("node-bundle-name"), //
        NODE_BUNDLE_SYMBOLIC_NAME_KEY("node-bundle-symbolic-name"), //
        NODE_BUNDLE_VENDOR_KEY("node-bundle-vendor"), //
        NODE_BUNDLE_VERSION_KEY("node-bundle-version"),
        NODE_FEATURE_NAME_KEY("node-feature-name"), //
        NODE_FEATURE_SYMBOLIC_NAME_KEY("node-feature-symbolic-name"), //
        NODE_FEATURE_VENDOR_KEY("node-feature-vendor"), //
        NODE_FEATURE_VERSION_KEY("node-feature-version"),
        NODE_CREATION_CONFIG_KEY("node_creation_config"),
        NODE_FILE_KEY("node_file"),
        WORKFLOW_FILE_KEY("workflow-file"),
        FACTORY_KEY("factory"),
        FACTORY_SETTINGS_KEY("factory_settings"),
        FILESTORES_KEY("filestores"), //
        FILESTORES_LOCATION_KEY("file_store_location"), FILESTORES_ID_KEY("file_store_id"),

        META_IN_PORTS_KEY("meta_in_ports"),
        META_OUT_PORTS_KEY("meta_out_ports"),
        PORT_ENUM_KEY("port_enum"),
        UI_SETTINGS_KEY("ui_settings"),

        PORT_INDEX_KEY("index"), //
        PORT_NAME_KEY("name"), //
        PORT_TYPE_KEY("type"), //
        PORT_OBJECT_CLASS_KEY("object_class"),

        DESCRIPTION_KEY("description"), //
        METADATA_KEY("metadata"), //
        METADATA_NAME_KEY("name"), //
        INPORTS_KEY("inports"), //
        INPORT_PREFIX("inport_"),
        OUTPORT_PREFIX("outport_"),
        OUTPORTS_KEY("outports"),
        LAYOUT_JSON_KEY("layoutJSON"),
        CONFIGURATION_LAYOUT_JSON_KEY("configurationLayoutJSON"),
        CUSTOM_CSS_KEY("customCSS"),
        HIDE_IN_WIZARD_KEY("hideInWizard"),

        WORKFLOW_TEMPLATE_INFORMATION_KEY("workflow_template_information"), //
        WORKFLOW_TEMPLATE_ROLE_KEY("role"),
        WORKFLOW_TEMPLATE_ROLE_TEMPLATE("Template"),
        WORKFLOW_TEMPLATE_ROLE_LINK("Link"),
        WORKFLOW_TEMPLATE_TYPE_KEY("templateType"),
        SOURCE_URI_KEY("sourceURI"), //
        TIMESTAMP("timestamp"),
        E_TAG("eTag"),
        VIRTUAL_IN_ID_KEY("virtual-in-ID"),
        VIRTUAL_OUT_ID_KEY("virtual-out-ID"),
        ICON_KEY("icon"),

        INTERNAL_NODE_SUBSETTINGS("internal_node_subsettings"),
        VARIABLES_KEY("variables"),
        MODEL_KEY("model"),
        SCOPE_STACK_KEY("scope_stack"), FLOW_STACK_KEY("flow_stack"),
        TYPE_KEY("type"), VARIABLE("variable"),
        INACTIVE("_INACTIVE"),
        LOOP("LOOP"), FLOW("FLOW"), SCOPE("SCOPE"),

        ID_KEY("id"),
        CUSTOM_NAME_KEY("customName"), //
        NODE_ANNOTATION_KEY("nodeAnnotation"),
        JOB_MANAGER_KEY("job.manager"), //
        JOB_MANAGER_FACTORY_ID_KEY("job.manager.factory.id"), //
        JOB_MANAGER_SETTINGS_KEY("job.manager.settings"),
        IS_DELETABLE_KEY("isDeletable"), //
        HAS_RESET_LOCK_KEY("hasResetLock"), //
        HAS_CONFIGURE_LOCK_KEY("hasConfigureLock"),
        CUSTOM_DESCRIPTION_KEY("customDescription"),
        EXTRA_NODE_INFO_BOUNDS_KEY("extrainfo.node.bounds"),

        WORKFLOW_FILE_NAME("workflow.knime"),
        NODE_SETTINGS_FILE("node_settings_file"),
        NODE_SETTINGS_FILE_NAME("settings.xml"),
        TEMPLATE_FILE_NAME("template.knime"),
        TEMPLATE_CONFIG_KEY("Template"),

        TABLE_BACKEND_KEY("tableBackend"),
        WORKFLOW_VARIABLES_KEY("workflow_variables"),
        WORKFLOW_VARIABLE_PREFIX("Var_"),
        FLOW_VARIABLE_NAME_KEY("name"),
        FLOW_VARIABLE_VALUE_KEY("value"),
        FLOW_VARIABLE_CLASS_KEY("class"),

        CREDENTIAL_PLACEHOLDERS_KEY("workflow_credentials"),
        CREDENTIAL_PLACEHOLDER_NAME_KEY("name"), //
        CREDENTIAL_PLACEHOLDER_LOGIN_KEY("login"), //

        WORKFLOW_HEADER_CREATED_BY_KEY("created_by"),
        WORKFLOW_HEADER_CREATED_BY_NIGHTLY_KEY("created_by_nightly"),
        WORKFLOW_HEADER_VERSION_KEY("version"),
        WORKFLOW_CONNECTIONS_KEY("connections"),
        WORKFLOW_CONNECTION_PREFIX("connection_"),
        WORKFLOW_CONNECTION_SOURCE_ID("sourceID"),
        WORKFLOW_CONNECTION_SOURCE_PORT("sourcePort"),
        WORKFLOW_CONNECTION_DESTINATION_ID("destID"),
        WORKFLOW_CONNECTION_DESTINATION_PORT("destPort"),
        WORKFLOW_NODES_KEY("nodes"),
        WORKFLOW_NODE_PREFIX("node_"),
        WORKFLOW_NODES_NODE_TYPE_KEY("node_type"),
        WORKFLOW_NODES_NODE_IS_META_KEY("node_is_meta"),
        AUTHOR_INFORMATION_KEY("authorInformation"), AUTHORED_BY_KEY("authored-by"),
        AUTHORED_WHEN_KEY("authored-when"), LAST_EDITED_BY_KEY("lastEdited-by"),
        LAST_EDITED_WHEN_KEY("lastEdited-when"),
        WORKFLOW_ANNOTATIONS_KEY("annotations"),
        WORKFLOW_EDITOR_SETTINGS_KEY("workflow_editor_settings"), //
        WORKFLOW_EDITOR_SNAPTOGRID_KEY("workflow.editor.snapToGrid"), //
        WORKFLOW_EDITOR_SHOWGRID_KEY("workflow.editor.ShowGrid"), //
        WORKFLOW_EDITOR_GRID_X_KEY("workflow.editor.gridX"), //
        WORKFLOW_EDITOR_GRID_Y_KEY("workflow.editor.gridY"), //
        WORKFLOW_EDITOR_ZOOM_LEVEL_KEY("workflow.editor.zoomLevel"), //
        UI_CLASSNAME_KEY("ui_classname"), //
        NODE_UI_INFORMATION_CLASSNAME("org.knime.core.node.workflow.NodeUIInformation"), //
        CONNECTION_UI_INFORMATION_CLASSNAME("org.knime.core.node.workflow.ConnectionUIInformation"), //
        WORKFLOW_EDITOR_CONNECTION_WIDTH_KEY("workflow.editor.connectionWidth"), //
        WORKFLOW_EDITOR_CURVED_CONNECTIONS_KEY("workflow.editor.curvedConnections"),
        EXTRA_INFO_CLASS_NAME_KEY("extraInfoClassName"),
        EXTRA_INFO_CONNECTION_BENDPOINTS_KEY("extrainfo.conn.bendpoints"),
        EXTRA_INFO_CONNECTION_BENDPOINTS_SIZE_KEY("extrainfo.conn.bendpoints_size");

    /**
     * @param string
     */
    IOConst(final String string) {
        m_key = string;
    }

    /**
     * @return the key
     */
    public String get() {
        return m_key;
    }

    final String m_key;
}
