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
 *   26 Apr 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.shared.workflow.def.ComponentNodeDef;
import org.knime.shared.workflow.def.CreatorDef;
import org.knime.shared.workflow.def.PortMetadataDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;

/**
 * This class will save the node settings of a component node and then invoke the {@link WorkflowSaver} to save the
 * contained workflow.
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
final class ComponentNodeSaver extends SingleNodeSaver {

    private final ComponentNodeDef m_componentNode;

    private CreatorDef m_creator;

    ComponentNodeSaver(final ComponentNodeDef componentNode) {
        super(componentNode);
        m_componentNode = componentNode;
    }

    ComponentNodeSaver(final ComponentNodeDef componentNode, final CreatorDef creator) {
        this(componentNode);
        m_creator = creator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void save(final File componentDirectory, final ConfigBase parentWorkflowNodeSettings) throws IOException {
        save(componentDirectory, parentWorkflowNodeSettings, null);
    }

    /**
     * Creates the node settings file on disk.
     * @param componentDirectory
     * @param parentWorkflowNodeSettings
     * @param nodeSettingsModifier nullable logic to update the created node settings
     * @throws IOException
     */
    void save(final File componentDirectory, final ConfigBase parentWorkflowNodeSettings,
        final Consumer<ConfigBase> nodeSettingsModifier) throws IOException {
        var nodeSettings = new SimpleConfig(IOConst.NODE_SETTINGS_FILE_NAME.get());
        addNodeSettings(nodeSettings);

        // Save the contained workflow
        var workflowSaver = new WorkflowSaver(m_componentNode.getWorkflow(), m_creator);
        workflowSaver.save(componentDirectory, s -> {
            // annotation data and custom description will be added to the workflow.knime by the WorkflowSaver
            m_componentNode.getAnnotation().ifPresent(annotation -> SaverUtils.addAnnotationData(s, annotation));
            m_componentNode.getCustomDescription().ifPresent(value -> s.addString(IOConst.CUSTOM_DESCRIPTION_KEY.get(), value));
        });

        // This is the settings.xml of the component
        var componentSettingsFile = new File(componentDirectory, IOConst.NODE_SETTINGS_FILE_NAME.get());

        if (parentWorkflowNodeSettings != null) { // then it's not a standalone and there's a parent config
            // Find the relative path to the parent directory (e.g. "Component (#14)/settings.xml")
            var componentSettingsFileRelativePath =
                componentDirectory.getParentFile().toURI().relativize(componentSettingsFile.toURI());

            // Configure the entry in the parent workflow
            addParentWorkflowNodeSettings(parentWorkflowNodeSettings);
            parentWorkflowNodeSettings.addString(IOConst.NODE_SETTINGS_FILE.get(),
                componentSettingsFileRelativePath.getPath());
        }

        if (nodeSettingsModifier != null) {
            nodeSettingsModifier.accept(nodeSettings);
        }

        // Flush the settings.xml file
        try (var fos = new FileOutputStream(componentSettingsFile)) {
            XMLConfig.save(nodeSettings, fos);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void addNodeSettings(final ConfigBase nodeSettings) {
        SaverUtils.addCreatorInfo(nodeSettings, m_creator);
        nodeSettings.addString(IOConst.NODE_FILE_KEY.get(), IOConst.NODE_SETTINGS_FILE_NAME.get());
        super.addNodeSettings(nodeSettings);
        nodeSettings.addString("workflow-file", IOConst.WORKFLOW_FILE_NAME.get());
        addPorts(nodeSettings);
        addMetadata(nodeSettings);
        SaverUtils.addTemplateInfo(nodeSettings, m_componentNode.getTemplateLink(), m_componentNode.getTemplateMetadata(),
            m_componentNode.getNodeType());
        addDialogSettings(nodeSettings);
    }

    private void addPorts(final ConfigBase nodeSettings) {
        // Add in ports
        nodeSettings.addInt(IOConst.VIRTUAL_IN_ID_KEY.get(), m_componentNode.getVirtualInNodeId());
        var inPorts = new SimpleConfig(IOConst.INPORTS_KEY.get());
        m_componentNode.getInPorts().orElse(List.of()).forEach(port -> SaverUtils.addPort(inPorts, IOConst.INPORT_PREFIX.get(), port));
        nodeSettings.addEntry(inPorts);

        // Add out ports
        nodeSettings.addInt(IOConst.VIRTUAL_OUT_ID_KEY.get(), m_componentNode.getVirtualOutNodeId());
        var outPorts = new SimpleConfig(IOConst.OUTPORTS_KEY.get());
        m_componentNode.getOutPorts().orElse(List.of()).forEach(port -> SaverUtils.addPort(outPorts, IOConst.OUTPORT_PREFIX.get(), port));
        nodeSettings.addEntry(outPorts);
    }

    private void addMetadata(final ConfigBase nodeSettings) {
        final var optionalMetadata = m_componentNode.getMetadata();
        if(optionalMetadata.isEmpty()) {
            return;
        }
        var metadata = optionalMetadata.get();

        var metadataSettings = new SimpleConfig(IOConst.METADATA_KEY.get());

        metadata.getDescription().ifPresent(desc -> metadataSettings.addString(IOConst.DESCRIPTION_KEY.get(), desc));

        metadata.getIcon().ifPresent(
            icon -> metadataSettings.addString(IOConst.ICON_KEY.get(), Base64.getEncoder().encodeToString(icon)));

        // These are not saved with SaverUtils#addPort since they additionally include a description and are represented
        //     differently. Also, we assume that (in|out)Names and (in|out)Descriptions have the same length, otherwise
        //     some values will be omitted.
        var inPorts = new SimpleConfig(IOConst.META_IN_PORTS_KEY.get());
        for(PortMetadataDef portMetadata : metadata.getInPortMetadata().orElse(List.of())) {
            var index = inPorts.getChildCount();
            var port = new SimpleConfig(IOConst.INPORT_PREFIX.get() + index);
            portMetadata.getName().ifPresent(name -> port.addString(IOConst.PORT_NAME_KEY.get(), name));
            portMetadata.getDescription().ifPresent(desc -> port.addString(IOConst.DESCRIPTION_KEY.get(), desc));
            port.addInt(IOConst.PORT_INDEX_KEY.get(), index);
            inPorts.addEntry(port);
        }
        if (inPorts.getChildCount() > 0) {
            metadataSettings.addEntry(inPorts);
        }

        var outPorts = new SimpleConfig(IOConst.META_OUT_PORTS_KEY.get());
        for(PortMetadataDef portMetadata : metadata.getOutPortMetadata().orElse(List.of())) {
            var index = outPorts.getChildCount();
            var port = new SimpleConfig(IOConst.OUTPORT_PREFIX.get() + index);
            portMetadata.getName().ifPresent(name -> port.addString(IOConst.PORT_NAME_KEY.get(), name));
            portMetadata.getDescription().ifPresent(desc -> port.addString(IOConst.DESCRIPTION_KEY.get(), desc));
            port.addInt(IOConst.PORT_INDEX_KEY.get(), index);
            outPorts.addEntry(port);
        }
        if (outPorts.getChildCount() > 0) {
            metadataSettings.addEntry(outPorts);
        }
        nodeSettings.addEntry(metadataSettings);
    }

    private void addDialogSettings(final ConfigBase nodeSettings) {
        if (m_componentNode.getDialogSettings().isEmpty()) {
            return;
        }
        var dialogSettings = m_componentNode.getDialogSettings().get();
        dialogSettings.getLayoutJSON().ifPresent(json -> nodeSettings.addString(IOConst.LAYOUT_JSON_KEY.get(), json));
        dialogSettings.getConfigurationLayoutJSON()
            .ifPresent(json -> nodeSettings.addString(IOConst.CONFIGURATION_LAYOUT_JSON_KEY.get(), json));
        nodeSettings.addBoolean(IOConst.HIDE_IN_WIZARD_KEY.get(), dialogSettings.isHideInWizard());
        dialogSettings.getCssStyles().ifPresent(css -> nodeSettings.addString(IOConst.CUSTOM_CSS_KEY.get(), css));
    }

}
