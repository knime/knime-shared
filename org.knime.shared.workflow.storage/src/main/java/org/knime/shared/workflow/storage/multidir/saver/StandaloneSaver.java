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
 *   9 May 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.saver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.shared.workflow.def.ComponentNodeDef;
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.def.RootWorkflowDef;
import org.knime.shared.workflow.def.StandaloneDef;
import org.knime.shared.workflow.def.TemplateInfoDef;
import org.knime.shared.workflow.storage.multidir.util.IOConst;
import org.knime.shared.workflow.storage.multidir.util.SaverUtils;

/**
 * This class holds methods to save a {@link StandaloneDef} to disk
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
public final class StandaloneSaver {

    private final StandaloneDef m_standalone;

    /**
     * Create a new StandaloneSaver instance
     *
     * @param standalone
     */
    public StandaloneSaver(final StandaloneDef standalone) {
        m_standalone = standalone;
    }

    /**
     * Saves a standalone by invoking the corresponding saver class and possibly writing a template.knime file
     *
     * @param parentDirectory The parent directory in which to save the standalone
     * @param dirname The name of the
     * @throws IOException
     */
    public void save(final File parentDirectory, final String dirname) throws IOException {
        var safeFilename = SaverUtils.getValidFileName(dirname, -1);
        var standaloneDirectory = new File(parentDirectory, safeFilename);
        standaloneDirectory.mkdir();
        switch (m_standalone.getContentType()) {
            case ROOT_WORKFLOW:
                saveRootWorkflow(standaloneDirectory);
                break;
            case COMPONENT:
                saveComponentNode(standaloneDirectory);
                break;
            case METANODE:
                saveMetaNode(standaloneDirectory);
                break;
            case WORKFLOW:
                throw new IllegalStateException("Saving a non-root workflow is not (yet?) supported.");
        }
    }

    private void saveRootWorkflow(final File workflowDirectory) throws IOException {
        var workflow = (RootWorkflowDef)m_standalone.getContents();
        var workflowSaver = new WorkflowSaver(workflow, m_standalone.getCreator());
        workflowSaver.save(workflowDirectory, s -> {
            if (workflow.getTableBackendSettings() != null) {
                s.addEntry(
                    SaverUtils.toConfigEntry(workflow.getTableBackendSettings(), IOConst.TABLE_BACKEND_KEY.get()));
            }
            SaverUtils.addFlowVariables(s, workflow.getFlowVariables());
            SaverUtils.addCredentials(s, workflow.getCredentialPlaceholders());
        });
    }

    private void saveComponentNode(final File standaloneDirectory) throws IOException {
        var componentNode = (ComponentNodeDef)m_standalone.getContents();
        var componentNodeSaver = new ComponentNodeSaver(componentNode, m_standalone.getCreator());
        componentNodeSaver.save(standaloneDirectory, null,
            s -> SaverUtils.addTemplateInfo(s, componentNode.getTemplateInfo(), componentNode.getNodeType()));
        saveTemplateFile(standaloneDirectory, componentNode.getTemplateInfo(), NodeTypeEnum.COMPONENT);
    }

    private void saveMetaNode(final File standaloneDirectory) throws IOException {
        var metaNode = (MetaNodeDef)m_standalone.getContents();
        var metaNodeSaver = new MetaNodeSaver(metaNode, m_standalone.getCreator());
        metaNodeSaver.save(standaloneDirectory, null);
        saveTemplateFile(standaloneDirectory, metaNode.getLink(), NodeTypeEnum.METANODE);
    }

    /**
     * Saves a template.knime file which holds information about the creator (KNIME version etc.) and template (e.g. the
     * timestamp that is used to check for updates
     *
     * @param directory
     * @param templateInfo
     * @param nodeType
     * @throws IOException
     */
    private void saveTemplateFile(final File directory, final TemplateInfoDef templateInfo, final NodeTypeEnum nodeType)
        throws IOException {
        var templateConfig = new SimpleConfig(IOConst.TEMPLATE_CONFIG_KEY.get());
        SaverUtils.addCreatorInfo(templateConfig, m_standalone.getCreator());
        SaverUtils.addTemplateInfo(templateConfig, templateInfo, nodeType);

        // flush template.knime
        try (var fos = new FileOutputStream(new File(directory, IOConst.TEMPLATE_FILE_NAME.get()))) {
            XMLConfig.save(templateConfig, fos);
        }
    }

}
