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

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the metadata for a KNIME Metanode.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public final class MetanodeMetadata extends AbstractWorkflowMetadata<MetanodeMetadataBuilder> implements NodeMetadata {

    @JsonProperty("node_ID")
    private final int m_nodeId;

    @JsonProperty("type")
    private final String m_type;

    @JsonProperty("annotation_text")
    private final Optional<String> m_annotationText;

    @JsonProperty("template_link")
    private final Optional<String> m_template;

    MetanodeMetadata(final MetanodeMetadataBuilder builder) {
        super(builder);
        m_nodeId = builder.getNodeFields().getId();
        m_type = builder.getNodeFields().getType();
        m_annotationText = builder.getNodeFields().getAnnotationText();
        m_template = builder.getTemplateLink();
    }

    /**
     * For internal use only! This constructor creates a copy of the given {@code MetanodeMetadata}, but sets the
     * nodes/connections to {@code null}
     *
     * @param metanode the {@code MetanodeMetadata} to copy
     */
    private MetanodeMetadata(final MetanodeMetadata metanode) {
        super(metanode);
        m_nodeId = metanode.m_nodeId;
        m_type = metanode.m_type;
        m_annotationText = metanode.m_annotationText;
        m_template = metanode.m_template;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNodeId() {
        return m_nodeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return m_type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getAnnotationText() {
        return m_annotationText;
    }

    /**
     * @return link information for the template
     * @throws UnsupportedOperationException when field hasn't been read (i.e. when field is {@code null})
     */
    public Optional<String> getTemplateLink() {
        return m_template;
    }

    /**
     * {@inheritDoc}
     */
    @JsonProperty("is_metaNode")
    @Override
    public boolean isMetaNode() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
        ", node_ID: " + m_nodeId +
        ", type: " + m_type +
        ", annotation_text: " + m_annotationText.orElse(null) +
        ", template_link: " + m_template.orElse(null);
    }

    /**
     * @return a copy of this {@code MetanodeMetadata}, except the nodes/connections have been set to {@code null}.
     */
    MetanodeMetadata dropNodes() {
        return new MetanodeMetadata(this);
    }
}
