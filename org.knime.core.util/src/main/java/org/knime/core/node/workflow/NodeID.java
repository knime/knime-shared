/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 * ------------------------------------------------------------------------
 */
package org.knime.core.node.workflow;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Holds hierarchical ID of a node. The hierarchy models nested meta nodes.
 * All IDs will have one static instance of ROOTID as their top ID in this
 * hierarchy.
 *
 * @author M. Berthold/B. Wiswedel, University of Konstanz
 */
public class NodeID implements Serializable, Comparable<NodeID> {
    /** Utility class that only stores the workflow relative NodeID path. If the NodeID of the workflow is
     * 0:3 and the quickforms in there are 0:3:1:1 and 0:3:1:2 then it only saves {1,1} and {1,2}. We must not
     * save the wfm ID with the NodeIDs as those may change when the workflow is swapped out/read back in.
     * See also bug 4478.
     */
    public static final class NodeIDSuffix implements Serializable {
        private static final long serialVersionUID = -6967588595219250919L;
        private final int[] m_suffixes;

        private NodeIDSuffix(final int[] suffixes) {
            m_suffixes = suffixes;
        }

        /**
         * Createa a node ID suffix by removing the parent ID from the node ID.
         *
         * @param parentID the parent ID
         * @param nodeID the complete node ID
         * @return the ID suffix
         * @throws IllegalArgumentException If the parent ID is not a prefix of the node ID
         */
        public static NodeIDSuffix create(final NodeID parentID, final NodeID nodeID) {
            if (!nodeID.hasPrefix(parentID)) {
                throw new IllegalArgumentException("The argument node ID \"" + nodeID
                    + "\" does not have the expected parent prefix \"" + parentID + "\"");
            }
            List<Integer> suffixList = new ArrayList<Integer>();
            NodeID traverse = nodeID;
            do {
                suffixList.add(traverse.getIndex());
                traverse = traverse.getPrefix();
            } while (!parentID.equals(traverse));
            Collections.reverse(suffixList);
            return new NodeIDSuffix(ArrayUtils.toPrimitive(suffixList.toArray(new Integer[suffixList.size()])));
        }


        /** Create a new nodeID with this as prefix and the argument as index.
         * @param index The child index.
         * @return a new ID.
         */
        public NodeIDSuffix createChild(final int index) {
            return new NodeIDSuffix(ArrayUtils.add(m_suffixes, index));
        }

        /**
         * Combines this suffix with a parent ID and returns a new node ID. This is the reverse operation to
         * {@link #create(NodeID, NodeID)}.
         *
         * @param parentID the parent's ID
         * @return a new node ID
         */
        public NodeID prependParent(final NodeID parentID) {
            NodeID result = parentID;
            for (int i : m_suffixes) {
                result = new NodeID(result, i);
            }
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return StringUtils.join(ArrayUtils.toObject(m_suffixes), ':');
        }

        /**
         * Creates a new node ID suffix from a string.
         *
         * @param string the string as returned by {@link #toString()}
         * @return a new {@link NodeIDSuffix}.
         * @throws IllegalArgumentException if the passed string is not a valid node ID suffix
         */
        public static NodeIDSuffix fromString(final String string) {
            String[] splitString = StringUtils.split(string, ':');
            int[] suffixes = new int[splitString.length];
            for (int i = 0; i < suffixes.length; i++) {
                try {
                    suffixes[i] = Integer.parseInt(splitString[i]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Can't parse node id suffix string \""
                            + string + "\": " + e.getMessage(), e);
                }
            }
            return new NodeIDSuffix(suffixes);
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Arrays.hashCode(m_suffixes);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof NodeIDSuffix)) {
                return false;
            }
            return Arrays.equals(m_suffixes, ((NodeIDSuffix)obj).m_suffixes);
        }
    }

    private static final long serialVersionUID = 7099500617597215889L;

    private final NodeID m_prefix;
    private final int m_index;

    /** Root node ID, all nodeID will derive from this instance. */
    public static final NodeID ROOTID = new NodeID();
    private static final String PREFIX_SEPERATOR = ":";

    /** Creates now NodeID object based on a predefined prefix (usually the
     * ID of the encapsulating project or metanode) and the node's ID itself.
     *
     * @param prefix of ID
     * @param ix itself
     */
    public NodeID(final NodeID prefix, final int ix) {
        assert ix >= 0;
        assert prefix != null;
        m_prefix = prefix;
        m_index = ix;
    }

    /** Creates top level NodeID object.
     *
     * @param ix itself
     */
    public NodeID(final int ix) {
        assert ix >= 0;
        m_prefix = ROOTID;
        m_index = ix;
    }

    /* Create root node id.
     */
    private NodeID() {
        m_prefix = null;
        m_index = 0;
    }

    /**
     * @return prefix of this node's ID.
     */
    public NodeID getPrefix() {
        return m_prefix;
    }

    /**
     * @return index of this node (without prefix!).
     */
    public int getIndex() {
        return m_index;
    }

    /** Create a new nodeID with this as prefix and the argument as index.
     * @param index The child index.
     * @return a new ID.
     * @since 5.3 */
    public NodeID createChild(final int index) {
        return new NodeID(this, index);
    }

    /**
     * @return string representation of ID without the leading "0:"
     * @deprecated Do not use this method any more, it will be removed
     * in future releases. Use {@link #toString()} instead
     */
    @Deprecated
    public String getIDWithoutRoot() {
        String id = toString();
        String withoutRoot = id.substring(id.indexOf(PREFIX_SEPERATOR) + 1);
        return withoutRoot;
    }

    /** Checks for exact matching prefixes.
     *
     * @param prefix to check
     * @return true if prefix are the same
     */
    public boolean hasSamePrefix(final NodeID prefix) {
        return m_prefix.equals(prefix);
    }

    /** Checks for matching prefix (this node prefix can be longer, though).
     *
     * @param prefix to check
     * @return true if prefix are the same
     */
    public boolean hasPrefix(final NodeID prefix) {
        if (m_prefix.equals(prefix)) {
            return true;
        }
        if (m_prefix == ROOTID) {
            return false;
        }
        return m_prefix.hasPrefix(prefix);
    }

    /**
     * Constructs a NodeID from a given serialized input string. Reverse operation of {@link #toString()}.
     *
     * @param nodeIDString The serialized NodeID string to parse.
     * @return NodeID deserialized from string argument.
     * @throws IllegalArgumentException If string cannot be parsed.
     * @throws NullPointerException If string argument is null.
     */
    public static NodeID fromString(final String nodeIDString) {
        String[] idStrings = nodeIDString.split(PREFIX_SEPERATOR);
        NodeID nodeID = null;
        for (String idString : idStrings) {
            try {
                int id = Integer.parseInt(idString);
                if (nodeID == null) {
                    nodeID = new NodeID(id);
                } else {
                    nodeID = new NodeID(nodeID, id);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot parse NodeID string (" + nodeIDString + ")", e);
            }
        }
        return nodeID;
    }

    /**
     * @return string representation of ID.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        assembleString(sb);
        return sb.toString();
    }

    /**
     * Put together String representation of ID.
     */
    private void assembleString(final StringBuilder sb) {
        if (m_prefix != null) {
            m_prefix.assembleString(sb);
            if (sb.length() > 0) {
                sb.append(PREFIX_SEPERATOR);
            }
            sb.append(m_index);
        }
        return;  // don't "print out" ROOT index (which is always "0:")
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof NodeID)) {
            return false;
        }
        NodeID objID = (NodeID)obj;
        return this.compareTo(objID) == 0;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // TODO make more efficient and smarter?
        return this.toString().hashCode();
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final NodeID o) {
        if (this == o) {
            return 0;
        }
        if (this == ROOTID) {
            return -1;
        }
        if (o == ROOTID) {
            return +1;
        }
        int prefixComp = this.m_prefix.compareTo(o.m_prefix);
        if (prefixComp != 0) {
            return prefixComp;
        }
        return m_index - o.m_index;
    }

    /** Read singleton ROOT (iff this is ROOT).
     * As suggested by java.io.Serializable.
     * @return this if this is not ROOT, otherwise ROOT.
     * @throws ObjectStreamException Not actually thrown but required
     * by Serializable interface.
     */
    private Object readResolve() throws ObjectStreamException {
        if (m_prefix == null) {
            return ROOTID;
        }
        return this;
    }
}
