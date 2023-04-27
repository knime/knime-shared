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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.knime.core.util.Version;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract base class for {@link IWorkflowMetadata}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
abstract class AbstractWorkflowMetadata<B extends AbstractWorkflowBuilder<?>> implements IWorkflowMetadata {

    @JsonProperty("version")
    private final Version m_version;

    @JsonProperty("createdBy")
    private final Version m_createdBy;

    @JsonProperty("annotations")
    private final Optional<List<String>> m_annotations;

    @JsonProperty("connections")
    private final List<NodeConnection> m_connections;

    @JsonProperty("nodes")
    private final List<NodeMetadata> m_nodes;

    @JsonProperty("name")
    private final String m_name;

    @JsonProperty("customWorkflowDescription")
    private final Optional<String> m_customDescription;

    @JsonProperty("unexpectedFiles")
    private final Collection<String> m_unexpectedFiles;

    @JsonProperty("containsEncrypted")
    private final Boolean m_containsEncrypted;

    /**
     * @param builder
     */
    public AbstractWorkflowMetadata(final B builder) {
        m_version = builder.getWorkflowFields().getVersion();
        m_createdBy = builder.getWorkflowFields().getCreatedBy();
        m_annotations = builder.getWorkflowFields().getAnnotations();
        m_connections = builder.getWorkflowFields().getConnections();
        m_nodes = builder.getWorkflowFields().getNodes();
        m_name = builder.getWorkflowFields().getName();
        m_customDescription = builder.getWorkflowFields().getCustomDescription();
        m_unexpectedFiles = builder.getWorkflowFields().getUnexpectedFileNames();

        if (m_nodes != null) {
            boolean containsEncrypted = m_nodes.stream().anyMatch(n -> {
                if (n instanceof SubnodeMetadata) {
                    return isEncrypted(((SubnodeMetadata)n).getEncryption());
                } else if (n instanceof MetanodeMetadata) {
                    return isEncrypted(((MetanodeMetadata)n).getEncryption());
                }
                return false;
            });
            m_containsEncrypted = containsEncrypted;
        } else {
            m_containsEncrypted = null;
        }
    }

    private static boolean isEncrypted(final Encryption encryption) {
        return encryption != Encryption.NONE;
    }

    /**
     * For internal use only! If the given {@code AbstractWorkflowMetadata<?>} is not a {@code NodeMetadata} then the
     * node list will be flattened, otherwise the node list will be set to {@code null}. Connections will always be set
     * to {@code null}.
     *
     * @param workflow {@code AbstractWorkflowMetadata} to copy
     * @param excludedFactories list of factoryNames to exclude from the flattened node list, supports regex matching
     */
    protected AbstractWorkflowMetadata(final AbstractWorkflowMetadata<?> workflow,
        final List<String> excludedFactories) {
        m_version = workflow.m_version;
        m_createdBy = workflow.m_createdBy;
        m_annotations = workflow.m_annotations;
        m_name = workflow.m_name;
        m_customDescription = workflow.m_customDescription;
        m_unexpectedFiles = workflow.m_unexpectedFiles;

        m_connections = null;

        List<NodeMetadata> nodes = null;
        if (!(workflow instanceof NodeMetadata) && workflow.m_nodes != null) {
            nodes = new ArrayList<>();
            for (final NodeMetadata node : workflow.m_nodes) {
                addNodes(node, nodes, excludedFactories);
            }
        }
        m_nodes = nodes;
        m_containsEncrypted = workflow.containsEncrypted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version getVersion() {
        return m_version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version getCreatedBy() {
        return m_createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<String>> getAnnotations() {
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
    public String getName() {
        return m_name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCustomDescription() {
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

    @Override
    public boolean containsEncrypted() {
        if (m_nodes == null) {
            throw new UnsupportedOperationException("containsEncrypted() is unsupported, the nodes were not read");
        }
        return m_containsEncrypted;
    }

    @Override
    public String toString() {
        String annotations = "[";
        if (m_annotations.isPresent()) {
            annotations += String.join(", ", m_annotations.get());
        }
        annotations += "]";

        String numConnections = null;
        if (m_connections != null) {
            numConnections = m_connections.size() + "";
        }

        String numNodes = null;
        if (m_nodes != null) {
           numNodes = m_nodes.size() + "";
        }

        String uf = null;
        if (m_unexpectedFiles != null) {
            uf = "[" + String.join(", ", m_unexpectedFiles) + "]";
        }

        return "version: " + m_version +
        ", created_by: " + m_createdBy +
        ", annotations: " + annotations +
        ", num_connections: " + numConnections +
        ", num_nodes: " + numNodes +
        ", name: " + m_name +
        ", custom_workflow_description: " + m_customDescription.orElse(null) +
        ", unexpected_files: " + uf;
    }

    private void addNodes(final NodeMetadata nm, final List<NodeMetadata> nodes, final List<String> excludedFactories) {
        if (nm.getType().equals(NodeType.NATIVE_NODE)) {
            final NativeNodeMetadata nativeNode = (NativeNodeMetadata)nm;
            if (!excludedFactories.stream().anyMatch(exclude -> nativeNode.getFactoryName().matches(exclude))) {
                nodes.add(nativeNode);
            }
            return;
        }
        final IWorkflowMetadata wm = (IWorkflowMetadata) nm;
        for (final NodeMetadata node : wm.getNodes()) {
            addNodes(node, nodes, excludedFactories);
        }
        if (wm instanceof SubnodeMetadata) {
            nodes.add(((SubnodeMetadata)nm).dropNodes());
        }
        else {
            nodes.add(((MetanodeMetadata)nm).dropNodes());
        }
    }
}
