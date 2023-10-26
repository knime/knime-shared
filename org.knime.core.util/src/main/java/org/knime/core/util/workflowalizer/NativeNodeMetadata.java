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
 *   Sep 13, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Optional;

import org.knime.core.node.NodeFactoryId;
import org.knime.core.node.config.base.ConfigBase;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the metadata for a KNIME native node.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public final class NativeNodeMetadata implements SingleNodeMetadata {

    @JsonProperty("factoryName")
    private final String m_factoryName;

    @JsonProperty("factoryId")
    private final String m_factoryId;

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

    @JsonProperty("nodeAndBundleInformation")
    private final NodeAndBundleInformation m_nodeAndBundleInfo;

    NativeNodeMetadata(final NativeNodeMetadataBuilder builder) {
        m_factoryName = builder.getFactoryClass().orElse("") + builder.getFactorySettings();
        var isDynamicNodeFactory = !builder.getFactorySettings().isEmpty();
        m_factoryId = NodeFactoryId.compose(builder.getFactoryClass().orElseThrow(), isDynamicNodeFactory,
            builder.getFactoryIdUniquifier().orElse(null), () -> builder.getNodeName().orElseThrow());
        m_nodeId = builder.getSingleNodeFields().getId();
        m_type = builder.getSingleNodeFields().getType();
        m_nodeConfiguration = builder.getSingleNodeFields().getNodeConfiguration();
        m_annotationText = builder.getSingleNodeFields().getAnnotationText();
        m_customNodeDescription = builder.getSingleNodeFields().getCustomDescription();
        m_nodeAndBundleInfo = new NodeAndBundleInformation(builder.getFactoryClass(), builder.getBundleSymbolicName(),
            builder.getBundleName(), builder.getBundleVendor(), builder.getNodeName(), builder.getBundleVersion(),
            builder.getFeatureSymbolicName(), builder.getFeatureName(), builder.getFeatureVendor(),
            builder.getFeatureVersion());
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
     * @return information about the node's bundle/feature versions
     * @throws UnsupportedOperationException when field hasn't been read (i.e. when field is {@code null})
     */
    public NodeAndBundleInformation getNodeAndBundleInformation() {
        return m_nodeAndBundleInfo;
    }

    /**
     * Returns the unique node identifier, which can be used to lookup nodes in the node catalog.
     *
     * @return the unique node identifier
     * @deprecated use {@link #getFactoryId()} instead
     */
    @Deprecated
    public String getFactoryName() {
        return m_factoryName;
    }

    /**
     * @return the factory-id as per NodeFactory.getFactoryId
     */
    public String getFactoryId() {
        return m_factoryId;
    }

    @Override
    public String toString() {
        String numNodeConfig = null;
        if (m_nodeConfiguration != null && m_nodeConfiguration.isPresent()) {
            numNodeConfig = m_nodeConfiguration.get().keySet().size() + "";
        }

        return "factory_name: " + m_factoryName +
                ", node_ID: " + m_nodeId +
                ", type: " + m_type +
                ", num_node_config: " + numNodeConfig +
                ", annotation_text: " + m_annotationText.orElse(null) +
                ", custom_node_description: " + m_customNodeDescription.orElse(null) +
                ", " + m_nodeAndBundleInfo;
    }
}
