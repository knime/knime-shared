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
 *   13 Apr 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.shared.workflow.def.CreatorDef;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.def.impl.DefaultWorkflowDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;

/**
 * This class holds methods to save a workflow, either top-level or e.g. contained in a MetaNode.
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
final class WorkflowSaver {

    private final WorkflowDef m_workflow;

    private CreatorDef m_creator;

    WorkflowSaver(final WorkflowDef workflow) {
        m_workflow = workflow;
        if(workflow instanceof DefaultWorkflowDef) {
            var loadExceptionTree = ((DefaultWorkflowDef) workflow).getLoadExceptionTree();
            if(loadExceptionTree.hasExceptions()) {
                loadExceptionTree.getFlattenedLoadExceptions().forEach(ex -> ex.printStackTrace());
//                throw new IllegalArgumentException(
//                    "The workflow to save has load exceptions. This may cause problems, such as null pointer exceptions.");
            }
        }
    }

    WorkflowSaver(final WorkflowDef workflow, final CreatorDef creator) {
        this(workflow);
        m_creator = creator;
    }

    /**
     * Saves a workflow and all contained nodes
     *
     * @param workflowDirectory The parent directory, in which the {@code workflow.knime} file should be saved
     * @throws IOException When flushing to disk is not possible
     */
    void save(final File workflowDirectory) throws IOException {
        save(workflowDirectory, null);
    }

    /**
     * Saves a workflow and all contained nodes, provided a function that modifies the {@link ConfigBase} of the
     * workflow.knime file, which will be executed just before saving.
     *
     * @param workflowDirectory The parent directory, in which the {@code workflow.knime} file should be saved
     * @param workflowSettingsModifier A modifier for the workflow.knime config
     * @throws IOException When flushing to disk is not possible
     */
    void save(final File workflowDirectory, final Consumer<ConfigBase> workflowSettingsModifier) throws IOException {
        var workflowSettings = new SimpleConfig(IOConst.WORKFLOW_FILE_NAME.get());
        addSettings(workflowSettings, workflowDirectory);

        if (workflowSettingsModifier != null) {
            // will e.g. used be provided by a metanode since it saves its nodesettings to the workflow.knime as well
            workflowSettingsModifier.accept(workflowSettings);
        }

        var outputFile = new File(workflowDirectory, IOConst.WORKFLOW_FILE_NAME.get());
        // flush the workflow.knime to disk
        try (var fos = new FileOutputStream(outputFile)) {
            XMLConfig.save(workflowSettings, fos);
        }
    }

    private void addSettings(final ConfigBase workflowSettings, final File workflowDirectory) {
        addHeader(workflowSettings);
        addAuthorInfo(workflowSettings);
        saveChildNodes(workflowSettings, workflowDirectory);
        addConnections(workflowSettings);
        addAnnotations(workflowSettings);
        addWorkflowUISettings(workflowSettings);
    }

    private void addHeader(final ConfigBase workflowSettings) {
        SaverUtils.addCreatorInfo(workflowSettings, m_creator);
        m_workflow.getName().ifPresent(name -> workflowSettings.addString(IOConst.METADATA_NAME_KEY.get(), name));
        if (!workflowSettings.containsKey(IOConst.CUSTOM_DESCRIPTION_KEY.get())) {
            workflowSettings.addString(IOConst.CUSTOM_DESCRIPTION_KEY.get(), null);
        }
    }

    private void addAuthorInfo(final ConfigBase workflowSettings) {
        if(m_workflow.getAuthorInformation().isEmpty()) {
            return;
        }
        var authorInformation = m_workflow.getAuthorInformation().get();//NOSONAR
        ConfigBase authorConfig = new SimpleConfig(IOConst.AUTHOR_INFORMATION_KEY.get());

        authorConfig.addString(IOConst.AUTHORED_BY_KEY.get(), authorInformation.getAuthoredBy());
        authorConfig.addString(IOConst.AUTHORED_WHEN_KEY.get(),
            authorInformation.getAuthoredWhen().format(LoaderUtils.DATE_FORMAT));

        authorInformation.getLastEditedBy()
            .ifPresent(value -> authorConfig.addString(IOConst.LAST_EDITED_BY_KEY.get(), value));
        authorInformation.getLastEditedWhen()//
            .map(d -> d.format(LoaderUtils.DATE_FORMAT))//
            .ifPresent(value -> authorConfig.addString(IOConst.LAST_EDITED_WHEN_KEY.get(), value));

        workflowSettings.addEntry(authorConfig);
    }

    /**
     * Creates directories for the child nodes (usually called "Child node name (#\<id\>)")
     *
     * @param workflowSettings The workflow.knime {@link ConfigBase}
     * @param workflowDirectory The workflow Directory, in which the directories are created
     */
    private void saveChildNodes(final ConfigBase workflowSettings, final File workflowDirectory) {
        var nodes = new SimpleConfig(IOConst.WORKFLOW_NODES_KEY.get());
        m_workflow.getNodes().orElse(Map.of()).forEach((k, n) -> {
            try {
                var workflowNodeSettings = new SimpleConfig(k);
                var dir = SaverUtils.createNodeDir(workflowDirectory, n);
                BaseNodeSaver.getInstance(n, m_creator).save(dir, workflowNodeSettings);
                nodes.addEntry(workflowNodeSettings);
            } catch (IOException e) {
                Logger.getLogger(WorkflowSaver.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        });
        workflowSettings.addEntry(nodes);
    }

    private void addConnections(final ConfigBase workflowSettings) {
        var connections = new SimpleConfig(IOConst.WORKFLOW_CONNECTIONS_KEY.get());
        m_workflow.getConnections().orElse(List.of()).forEach(c -> {
            var ix = connections.getChildCount();
            var connectionConfig = new SimpleConfig(IOConst.WORKFLOW_CONNECTION_PREFIX.get() + ix);
            connectionConfig.addInt(IOConst.WORKFLOW_CONNECTION_SOURCE_ID.get(), c.getSourceID());
            connectionConfig.addInt(IOConst.WORKFLOW_CONNECTION_DESTINATION_ID.get(), c.getDestID());
            connectionConfig.addInt(IOConst.WORKFLOW_CONNECTION_SOURCE_PORT.get(), c.getSourcePort());
            connectionConfig.addInt(IOConst.WORKFLOW_CONNECTION_DESTINATION_PORT.get(), c.getDestPort());

            c.getUiSettings().ifPresent(uiSettings -> SaverUtils.addConnectionUISettings(connectionConfig, uiSettings));
            connections.addEntry(connectionConfig);
        });
        workflowSettings.addEntry(connections);
    }

    private void addAnnotations(final ConfigBase workflowSettings) {
        if (!m_workflow.getAnnotations().isEmpty()) {
            var annotations = new SimpleConfig(IOConst.WORKFLOW_ANNOTATIONS_KEY.get());
            m_workflow.getAnnotations().orElse(Map.of())
                .forEach((ix, a) -> annotations.addEntry(SaverUtils.annotationToConfig(a, "annotation_" + ix)));
            workflowSettings.addEntry(annotations);
        }
    }

    private void addWorkflowUISettings(final ConfigBase workflowSettings) {
        if(m_workflow.getWorkflowEditorSettings().isEmpty()) {
            return;
        }
        var workflowEditor = m_workflow.getWorkflowEditorSettings().get(); // NOSONAR
        // Metanode workflows don't have any contents here. Using zoom level as a stand-in for other properties:
        // If the zoom level is not null, we assume the same for the other properties.
        if (workflowEditor.getZoomLevel() != null) {
            ConfigBase uiConfig = new SimpleConfig(IOConst.WORKFLOW_EDITOR_SETTINGS_KEY.get());
            uiConfig.addBoolean(IOConst.WORKFLOW_EDITOR_SNAPTOGRID_KEY.get(), workflowEditor.isSnapToGrid());
            uiConfig.addBoolean(IOConst.WORKFLOW_EDITOR_SHOWGRID_KEY.get(), workflowEditor.isShowGrid());
            uiConfig.addInt(IOConst.WORKFLOW_EDITOR_GRID_X_KEY.get(), workflowEditor.getGridX());
            uiConfig.addInt(IOConst.WORKFLOW_EDITOR_GRID_Y_KEY.get(), workflowEditor.getGridY());
            uiConfig.addDouble(IOConst.WORKFLOW_EDITOR_ZOOM_LEVEL_KEY.get(), workflowEditor.getZoomLevel());
            if (workflowEditor.isCurvedConnections() != null) {
                uiConfig.addBoolean(IOConst.WORKFLOW_EDITOR_CURVED_CONNECTIONS_KEY.get(),
                    workflowEditor.isCurvedConnections());
            }
            if (workflowEditor.getConnectionLineWidth() != null) {
                uiConfig.addInt(IOConst.WORKFLOW_EDITOR_CONNECTION_WIDTH_KEY.get(),
                    workflowEditor.getConnectionLineWidth());
            }
            workflowSettings.addEntry(uiConfig);
        }
    }
}
