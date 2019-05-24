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
 *   Oct 4, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.knime.core.node.config.base.ConfigBase;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents metadata for a KNIME subnode.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public final class SubnodeMetadata extends AbstractWorkflowMetadata<SubnodeMetadataBuilder> implements SingleNodeMetadata {

    @JsonProperty("nodeInstanceId")
    private final String m_nodeId;

    @JsonProperty("type")
    private final NodeType m_type;

    @JsonProperty("nodeConfiguration")
    private final Optional<ConfigBase> m_nodeConfiguration;

    @JsonProperty("annotationText")
    private final Optional<String> m_annotationText;

    @JsonProperty("customNodeDescription")
    private final Optional<String> m_customNodeDescription;

    @JsonProperty("templateLink")
    private final Optional<String> m_template;

    SubnodeMetadata(final SubnodeMetadataBuilder builder) {
        super(builder);
        m_nodeId = builder.getSingleNodeFields().getId();
        m_type = builder.getSingleNodeFields().getType();
        m_nodeConfiguration = builder.getSingleNodeFields().getNodeConfiguration();
        m_annotationText = builder.getSingleNodeFields().getAnnotationText();
        m_customNodeDescription = builder.getSingleNodeFields().getCustomDescription();
        m_template = builder.getTemplateLink();
    }

    /**
     * For internal use only! This constructor creates a copy of the given {@code SubnodeMetadata} but sets
     * nodes/connections to {@code null}.
     *
     * @param subnode {@code SubnodeMetadata} to copy
     * @param excludedFactories list of factoryNames to exclude from the flattened node list, supports regex matching
     */
    private SubnodeMetadata(final SubnodeMetadata subnode, final List<String> excludedFactories) {
        super(subnode, excludedFactories);
        m_nodeId = subnode.m_nodeId;
        m_type = subnode.m_type;
        m_nodeConfiguration = subnode.m_nodeConfiguration;
        m_annotationText = subnode.m_annotationText;
        m_customNodeDescription = subnode.m_customNodeDescription;
        m_template = subnode.m_template;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeId() {
        return m_nodeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeType getType() {
        return m_type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ConfigBase> getNodeConfiguration() {
        if (m_nodeConfiguration == null) {
            throw new UnsupportedOperationException("getNodeConfiguration() is unsupported, field was not read");
        }
        return m_nodeConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getAnnotationText() {
        return m_annotationText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCustomNodeDescription() {
        return m_customNodeDescription;
    }

    /**
     * @return link information for the template
     */
    public Optional<String> getTemplateLink() {
        return m_template;
    }

    @Override
    public String toString() {
        String numNodeConfigParams = null;
        if (m_nodeConfiguration != null && m_nodeConfiguration.isPresent()) {
            numNodeConfigParams = m_nodeConfiguration.get().keySet().size() + "";
        }

        return super.toString() +
                ", node_ID: " + m_nodeId +
                ", type: " + m_type +
                ", num_m_node_configurations: " + numNodeConfigParams +
                ", annotation_text: " + m_annotationText.orElse(null) +
                ", custom_node_description: " + m_customNodeDescription.orElse(null) +
                ", template_link: " + m_template;
    }

    /**
     * @return a copy of this {@code SubnodeMetadata}, except the nodes and connections fields have been set to
     *         {@code null}
     */
    SubnodeMetadata dropNodes() {
        return new SubnodeMetadata(this, Collections.emptyList());
    }
}
