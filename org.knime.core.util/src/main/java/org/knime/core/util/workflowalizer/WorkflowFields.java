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
 *   Oct 8, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.knime.core.util.Version;

/**
 * Configuration and quasi-builder for all fields in {@link IWorkflowMetadata}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
final class WorkflowFields {

    private Version m_version;
    private Version m_createdBy;
    private String m_name;
    private Optional<String> m_customDescription;
    private Optional<List<String>> m_annotations;
    private List<NodeConnection> m_connections;
    private List<NodeMetadata> m_nodes;
    private Collection<String> m_unexpectedFileNames;

    private final boolean m_readConnections;
    private final boolean m_readNodes;
    private final boolean m_readUnexpectedFileNames;

    WorkflowFields(final boolean readConnections, final boolean readNodes, final boolean readUnexpectedFileNames) {
        m_readConnections = readConnections;
        m_readNodes = readNodes;
        m_readUnexpectedFileNames = readUnexpectedFileNames;
    }

    // -- Getters --

    Version getVersion() {
        return m_version;
    }

    Version getCreatedBy() {
        return m_createdBy;
    }

    String getName() {
        return m_name;
    }

    Optional<String> getCustomDescription() {
        return m_customDescription;
    }

    Optional<List<String>> getAnnotations() {
        return m_annotations;
    }

    List<NodeConnection> getConnections() {
        return m_connections;
    }

    List<NodeMetadata> getNodes() {
        return m_nodes;
    }

    Collection<String> getUnexpectedFileNames() {
        return m_unexpectedFileNames;
    }

    // -- Setters --

    void setVersion(final Version version) {
        m_version = version;
    }

    void setCreatedBy(final Version createdBy) {
        m_createdBy = createdBy;
    }

    void setName(final String name) {
        m_name = name;
    }

    void setCustomDescription(final Optional<String> customDescription) {
        m_customDescription = customDescription;
    }

    void setAnnotations(final Optional<List<String>> annotations) {
        m_annotations = annotations;
    }

    void setConnections(final List<NodeConnection> connections) {
        m_connections = connections;
    }

    void setNodes(final List<NodeMetadata> nodes) {
        m_nodes = nodes;
    }

    void setUnexpectedFileNames(final Collection<String> unexpectedFileNames) {
        m_unexpectedFileNames = unexpectedFileNames;
    }

    // -- Other --

    void validate() {
        checkPopulated(m_version, "version");
        checkPopulated(m_createdBy, "created by");
        checkPopulated(m_name, "workflow name");
        checkPopulated(m_customDescription, "workflow custom description");
        checkPopulated(m_annotations, "annotations");
        if (m_readNodes) {
            checkPopulated(m_nodes, "nodes");
        }
        if (m_readConnections) {
            if (m_nodes == null && m_connections != null) {
                throw new IllegalArgumentException(
                    "Nodes are null. Connections cannot be read without reading the nodes");
            }
            checkPopulated(m_connections, "connections");
        }
        if (m_readUnexpectedFileNames) {
            checkPopulated(m_unexpectedFileNames, "unexpected file names");
        }
    }

    // -- Helper methods --

    private static void checkPopulated(final Object field, final String name) {
        if (field == null) {
            throw new IllegalArgumentException("Requested field, " + name + ", should not be null");
        }
    }
}
