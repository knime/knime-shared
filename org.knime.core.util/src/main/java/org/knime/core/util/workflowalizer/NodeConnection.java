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
 *   Sep 12, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The connection between two nodes.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public final class NodeConnection {

    @JsonIgnore
    private final Optional<NodeMetadata> m_source;

    @JsonIgnore
    private final Optional<NodeMetadata> m_dest;

    @JsonProperty("source_ID")
    private final int m_sourceId;

    @JsonProperty("destination_ID")
    private final int m_destId;

    @JsonProperty("source_port")
    private final int m_sourcePort;

    @JsonProperty("destination_port")
    private final int m_destPort;

    /**
     * @param sourceId the ID of the connection's source node
     * @param source the connection's source node
     * @param destId the ID of the connections destination node
     * @param dest the connection's destination node
     */
    NodeConnection(final int sourceId, final Optional<NodeMetadata> source, final int sourcePort, final int destId,
        final Optional<NodeMetadata> dest, final int destPort) {
        // If the ID is not -1, then the node should be present/non-null
        if (sourceId != -1 && (source == null || !source.isPresent())) {
            throw new IllegalArgumentException("Missing source node with ID " + sourceId);
        }
        if (destId != -1 && (dest == null || !dest.isPresent())) {
            throw new IllegalArgumentException("Missing destination node with ID " + destId);
        }
        m_sourceId = sourceId;
        m_destId = destId;
        m_source = source;
        m_dest = dest;
        m_sourcePort = sourcePort;
        m_destPort = destPort;
    }

    /**
     * @return the ID of the connection source node
     */
    public int getSourceId() {
        return m_sourceId;
    }

    /**
     * @return the ID of the connection destination node
     */
    public int getDestinationId() {
        return m_destId;
    }

    /**
     * @return the source node
     */
    public Optional<NodeMetadata> getSourceNode() {
        return m_source;
    }

    /**
     * @return the destination node
     */
    public Optional<NodeMetadata> getDestinationNode() {
       return m_dest;
    }

    /**
     * @return the source port
     */
    public int getSourcePort() {
        return m_sourcePort;
    }

    /**
     * @return the dest port
     */
    public int getDestinationPort() {
        return m_destPort;
    }

    @Override
    public String toString() {
        return "source_ID: " + m_sourceId +
        ", destination_ID: " + m_destId +
        ", source_port: " + m_sourcePort +
        ", destination_port: " + m_destPort;
    }
}
