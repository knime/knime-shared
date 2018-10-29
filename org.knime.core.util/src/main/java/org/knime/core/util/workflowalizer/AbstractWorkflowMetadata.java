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

import org.knime.core.util.Version;

/**
 * Abstract base class for {@link WorkflowMetadata}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
abstract class AbstractWorkflowMetadata implements WorkflowMetadata {

    private final Version m_version;
    private final Version m_createdBy;
    private final Optional<List<String>> m_annotations;
    private final List<NodeConnection> m_connections;
    private final List<NodeMetadata> m_nodes;
    private final Optional<String> m_name;
    private final Optional<String> m_customDescription;
    private final Collection<String> m_unexpectedFiles;

    /**
     * @param version
     * @param createdBy
     * @param annotations
     * @param connections
     * @param nodes
     * @param name
     * @param customDescription
     * @param unexpectedFiles
     */
    public AbstractWorkflowMetadata(final Version version, final Version createdBy,
        final Optional<List<String>> annotations, final List<NodeConnection> connections,
        final List<NodeMetadata> nodes, final Optional<String> name, final Optional<String> customDescription,
        final Collection<String> unexpectedFiles) {
        m_version = version;
        m_createdBy = createdBy;
        m_annotations = annotations;
        m_connections = connections;
        m_nodes = nodes;
        m_name = name;
        m_customDescription = customDescription;
        m_unexpectedFiles = unexpectedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version getVersion() {
        if (m_version == null) {
            throw new UnsupportedOperationException("getVersion() is unsupported, field was not read");
        }
        return m_version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version getCreatedBy() {
        if (m_createdBy == null) {
            throw new UnsupportedOperationException("getCreatedBy() is unsupported, field was not read");
        }
        return m_createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<String>> getAnnotations() {
        if (m_annotations == null) {
            throw new UnsupportedOperationException("getAnnotations() is unsupported, field was not read");
        }
        return m_annotations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NodeConnection> getConnections() {
        if (m_connections == null) {
            throw new UnsupportedOperationException("getConnections() is unsupported, field was not read");
        }
        return m_connections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NodeMetadata> getNodes() {
        if (m_nodes == null) {
            throw new UnsupportedOperationException("getNodes() is unsupported, field was not read");
        }
        return m_nodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getName() {
        if (m_name == null) {
            throw new UnsupportedOperationException("getName() is unsupported, field was not read");
        }
        return m_name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCustomDescription() {
        if (m_customDescription == null) {
            throw new UnsupportedOperationException("getCustomDescription() is unsupported, field was not read");
        }
        return m_customDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getUnexpectedFileNames() {
        if (m_unexpectedFiles == null) {
            throw new UnsupportedOperationException("getUnexpectedFileNames() is unsupported, field was not read");
        }
        return m_unexpectedFiles;
    }
}
