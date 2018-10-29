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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.util.Version;

/**
 * Represents metadata for a KNIME subnode.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
public final class SubnodeMetadata extends AbstractWorkflowMetadata implements SingleNodeMetadata {

    private final Integer m_nodeId;
    private final String m_type;
    private final Optional<ConfigBase> m_modelParams;
    private final Optional<String> m_annotationText;
    private final Optional<String> m_customNodeDescription;
    private final Optional<String> m_template;

    SubnodeMetadata(final Version version, final Version createdBy, final Optional<List<String>> annotations,
        final List<NodeConnection> connections, final List<NodeMetadata> nodes, final Optional<String> name,
        final Optional<String> customDescription, final Collection<String> unexpectedFiles, final Integer nodeId,
        final String type, final Optional<ConfigBase> modelParams, final Optional<String> annotationText,
        final Optional<String> customNodeDescription, final Optional<String> template) {
        super(version, createdBy, annotations, connections, nodes, name, customDescription, unexpectedFiles);
        m_nodeId = nodeId;
        m_type = type;
        m_modelParams = modelParams;
        m_annotationText = annotationText;
        m_customNodeDescription = customNodeDescription;
        m_template = template;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNodeId() {
        if (m_nodeId == null) {
            throw new UnsupportedOperationException("getNodeId() is unsupported, field was not read");
        }
        return m_nodeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        if (m_type == null) {
            throw new UnsupportedOperationException("getType() is unsupported, field was not read");
        }
        return m_type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ConfigBase> getModelParameters() {
        if (m_modelParams == null) {
            throw new UnsupportedOperationException("getModelParameters() is unsupported, field was not read");
        }
        return m_modelParams;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getAnnotationText() {
        if (m_annotationText == null) {
            throw new UnsupportedOperationException("getAnnotationText() is unsupported, field was not read");
        }
        return m_annotationText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCustomNodeDescription() {
        if (m_customNodeDescription == null) {
            throw new UnsupportedOperationException("getCustomNodeDescription() is unsupported, field was not read");
        }
        return m_customNodeDescription;
    }

    /**
     * @return link information for the template
     * @throws UnsupportedOperationException when field hasn't been read (i.e. when field is {@code null})
     */
    public Optional<String> getTemplateLink() {
        if (m_template == null) {
            throw new UnsupportedOperationException("getTemplateLink() is unsupported, field was not read");
        }
        return m_template;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMetaNode() {
        return true;
    }
}
