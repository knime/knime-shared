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
 *   Jul 2, 2019 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.List;
import java.util.Optional;

/**
 * Builder for {@link ComponentMetadata}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.11
 */
final class ComponentMetadataBuilder extends TemplateMetadataBuilder {

    private Optional<String> m_description;
    private List<String> m_viewNodes;
    private List<ComponentPortInfo> m_ports;
    private List<ComponentDialogSection> m_dialog;
    private Optional<WorkflowSetMeta> m_workflowSetMeta;

    void setDescription(final Optional<String> description) {
        m_description = description;
    }

    void setViewNodes(final List<String> viewNodes) {
        m_viewNodes = viewNodes;
    }

    void setPorts(final List<ComponentPortInfo> ports) {
        m_ports = ports;
    }

    void setDialog(final List<ComponentDialogSection> dialog) {
        m_dialog = dialog;
    }

    void setWorkflowSetMeta(final Optional<WorkflowSetMeta> workflowSetMeta) {
        m_workflowSetMeta = workflowSetMeta;
    }

    Optional<String> getDescription() {
        return m_description;
    }

    List<String> getViewNodes() {
        return m_viewNodes;
    }

    List<ComponentPortInfo> getPorts() {
        return m_ports;
    }

    List<ComponentDialogSection> getDialog() {
        return m_dialog;
    }

    Optional<WorkflowSetMeta> getWorkflowSetMeta() {
        return m_workflowSetMeta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    ComponentMetadata buildExtraFields(final WorkflowalizerConfiguration wc) {
        checkPopulated(getAuthor(), "author");
        checkPopulated(getAuthorDate(), "authored date");
        checkPopulated(getLastEditDate(), "last edited date");
        checkPopulated(getLastEditor(), "last editor");
        checkPopulated(getRole(), "template role");
        checkPopulated(getTimeStamp(), "template timestamp");
        checkPopulated(getSourceURI(), "template source URI");
        checkPopulated(getType(), "template type");
        checkPopulated(m_description, "description");
        checkPopulated(m_viewNodes, "view nodes");
        checkPopulated(m_ports, "ports");
        checkPopulated(m_dialog, "dialog");
        checkPopulated(m_workflowSetMeta, "workflow set meta");
        return new ComponentMetadata(this);
    }

}
