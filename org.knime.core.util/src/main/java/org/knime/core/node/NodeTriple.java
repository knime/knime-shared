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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
 *   18.03.2016 (thor): created
 */
package org.knime.core.node;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A triple of nodes (predecessor, node, successor) with a count, i.e. the frequency of occurrences of that triple.
 *
 * @since 5.4
 */
public class NodeTriple {
    private final NodeInfo m_predecessor;

    private final NodeInfo m_node;

    private final NodeInfo m_successor;

    private int m_count;

    @JsonCreator
    private NodeTriple(@JsonProperty("predecessor") final NodeInfo predecessor,
        @JsonProperty("node") final NodeInfo node, @JsonProperty("successor") final NodeInfo successor,
        @JsonProperty("count") final int count) {
        m_predecessor = predecessor;
        m_node = node;
        m_successor = successor;
        m_count = count;
    }

    /**
     * Creates a new new node triple with a count of 0 (!).
     *
     * @param predecessor the predecessor node, may be <code>null</code>
     * @param node the node, must not be <code>null</code>
     * @param successor the sucessor node, must not be <code>null</code>
     */
    public NodeTriple(final NodeInfo predecessor, final NodeInfo node, final NodeInfo successor) {
        this(predecessor, node, successor, 0);
    }

    /**
     * Returns the predecessor node, if there is any.
     *
     * @return the predecessor node or an empty optional
     */
    @JsonProperty("predecessor")
    public Optional<NodeInfo> getPredecessor() {
        return Optional.ofNullable(m_predecessor);
    }

    /**
     * Returns the node.
     *
     * @return the node or an empty optional
     */
    @JsonProperty("node")
    public Optional<NodeInfo> getNode() {
        return Optional.ofNullable(m_node);
    }

    /**
     * Returns the successor.
     *
     * @return the successor, never <code>null</code>
     */
    @JsonProperty("successor")
    public NodeInfo getSuccessor() {
        return m_successor;
    }

    /**
     * Returns the count of this node triple.
     *
     * @return the frequency of the triple
     */
    @JsonProperty("count")
    public int getCount() {
        return m_count;
    }

    /**
     * Increments the count by one.
     */
    public void incrementCount() {
        m_count++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_node == null) ? 0 : m_node.hashCode());
        result = prime * result + ((m_predecessor == null) ? 0 : m_predecessor.hashCode());
        result = prime * result + ((m_successor == null) ? 0 : m_successor.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NodeTriple other = (NodeTriple)obj;
        return Objects.equals(m_node, other.m_node) && Objects.equals(m_predecessor, other.m_predecessor)
            && Objects.equals(m_successor, other.m_successor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getPredecessor().map(i -> i.toString()).orElse("?") + ";" + getNode().map(i -> i.toString()).orElse("?")
            + ";" + getSuccessor() + ";" + getCount();
    }
}
