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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO for representing a shared component's metadata.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.11
 */
public final class ComponentMetadata extends TemplateMetadata {

    @JsonProperty("description")
    private final Optional<String> m_description;

    @JsonProperty("viewNodes")
    private final List<String> m_viewNodes;

    @JsonProperty("inPorts")
    private final List<ComponentPortInfo> m_inPorts;

    @JsonProperty("outPorts")
    private final List<ComponentPortInfo> m_outPorts;

    @JsonProperty("dialog")
    private final List<ComponentDialogSection> m_dialog;

    @JsonProperty("workflowMeta")
    private final Optional<WorkflowSetMeta> m_workflowSetMeta;

    /**
     * Creates a POJO representing a component's metadata using the information set in the given
     * {@link ComponentMetadataBuilder}
     *
     * @param builder the {@link ComponentMetadataBuilder} whose information will be use to populate the POJO
     */
    ComponentMetadata(final ComponentMetadataBuilder builder) {
        super(builder);
        m_viewNodes = builder.getViewNodes();
        m_inPorts = builder.getInPorts();
        m_outPorts = builder.getOutPorts();
        m_workflowSetMeta = builder.getWorkflowSetMeta();

        Optional<String> d = builder.getDescription();
        if (d.isPresent() && StringUtils.isEmpty(d.get())) {
            d = Optional.empty();
        }
        m_description = d;

        final List<ComponentDialogSection> sections = new ArrayList<>(builder.getDialog().size());
        for (final ComponentDialogSection section : builder.getDialog()) {
            if (!section.isEmpty()) {
                sections.add(section);
            }
        }
        m_dialog = Collections.unmodifiableList(sections);
    }

    /**
     * For internal use only! This constructor creates a copy of the given {@code ComponentMetadata}, but sets the
     * connections to {@code null} and flattens the node tree
     *
     * @param component the {@code ComponentMetadata} to copy
     * @param excludedFactories list of factoryNames to exclude from the flattened node list,, supports regex matching
     */
    private ComponentMetadata(final ComponentMetadata component, final List<String> excludedFactories) {
        super(component, excludedFactories);
        m_description = component.m_description;
        m_viewNodes = component.m_viewNodes;
        m_inPorts = component.m_inPorts;
        m_outPorts = component.m_outPorts;
        m_dialog = component.m_dialog;
        m_workflowSetMeta = component.m_workflowSetMeta;
    }

    /**
     * Returns the description for this component.
     *
     * @return the description
     */
    public Optional<String> getDescription() {
        return m_description;
    }

    /**
     * Return the factoryNames of the views contained in this component.
     *
     * @return factory names of view nodes
     */
    public List<String> getViewNodes() {
        return m_viewNodes;
    }

    /**
     * Returns a list containing the inports for this component, where the indices of the list map to the indices of the
     * ports.
     *
     * @return the inports
     */
    public List<ComponentPortInfo> getInPorts() {
        return m_inPorts;
    }

    /**
     * Returns a list containing the outports for this component, where the indices of the list map to the indices of
     * the ports.
     *
     * @return the outports
     */
    public List<ComponentPortInfo> getOutPorts() {
        return m_outPorts;
    }

    /**
     * Returns the dialog sections for this component.
     *
     * @return the dialog metadata
     */
    public List<ComponentDialogSection> getDialog() {
        return m_dialog;
    }

    /**
     * Returns the content of the workflowset.meta file related to this component.
     *
     * @return the workflowset.meta metadata
     */
    public Optional<WorkflowSetMeta> getWorkflowSetMeta() {
        return m_workflowSetMeta;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());
        if (m_description.isPresent()) {
            builder.append(", description" + m_description.get());
        }
        builder.append(", viewNodes : [ ");
        if (!m_viewNodes.isEmpty()) {
            builder.append(String.join(", ", m_viewNodes));
        }
        builder.append("], inPorts: [ ");
        if (!m_inPorts.isEmpty()) {
            for (final ComponentPortInfo port : m_inPorts) {
                builder.append("{" + port.toString() + "}, ");
            }
            // remove unnecessary final comma and space
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("], outPorts: [ ");
        if (!m_outPorts.isEmpty()) {
            for (final ComponentPortInfo port : m_outPorts) {
                builder.append("{" + port.toString() + "}, ");
            }
            // remove unnecessary final comma and space
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("], dialog: [ ");
        if (!m_dialog.isEmpty()) {
            for (final ComponentDialogSection section : m_dialog) {
                builder.append("{" + section.toString() + "}, ");
            }
            // remove unnecessary final comma and space
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("]");
        if (!m_workflowSetMeta.isPresent()) {
            builder.append(", workflowSetMeta: { " + m_workflowSetMeta.get().toString() + " }");
        }
        return builder.toString();
    }

    /**
     * Creates a {@code ComponentMetadata} with a flattened node list for writing to JSON.
     *
     * @return a {@code ComponentMetadata} whose node list is flat (i.e. depth = 1), and whose connections have been set
     *         to {@code null}
     */
    @Override
    public ComponentMetadata flatten() {
        return new ComponentMetadata(this, Collections.emptyList());
    }

    /**
     * Creates a {@code ComponentMetadata} with a filtered flattened node list for writing to JSON.
     *
     * @param excludedFatoryNames a list of factoryNames to be excluded from the flattened node list, supports regex
     *            matching
     * @return a {@code ComponentMetadata} whose node list is flat (i.e. depth = 1), and whose connections have been set
     *         to {@code null}
     */
    @Override
    public ComponentMetadata flatten(final List<String> excludedFatoryNames) {
        List<String> efn = excludedFatoryNames;
        if (efn == null) {
            efn = Collections.emptyList();
        }
        return new ComponentMetadata(this, efn);
    }

}
