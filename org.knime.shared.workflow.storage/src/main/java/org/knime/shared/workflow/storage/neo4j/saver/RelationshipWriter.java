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
 *   26 Sept 2023 (carlwitt): created
 */
package org.knime.shared.workflow.storage.neo4j.saver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.ComponentNodeDef;
import org.knime.shared.workflow.def.ConnectionDef;
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.def.NativeNodeDef;
import org.knime.shared.workflow.def.WorkflowDef;
import org.knime.shared.workflow.storage.multidir.util.Loader;

/**
 *
 * # node instances repositoryItemId, fullyQualifiedNodeId, nodeName, nodeFactory, nodeType
 *
 * # node properties repositoryItemId, fullyQualifiedNodeId, propertyName, propertyValue
 *
 * # connections repositoryItemId, fullyQualifiedNodeIdSource, fullyQualifiedNodeIdTarget
 *
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public class RelationshipWriter implements BiConsumer<String, WorkflowDef>, AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(RelationshipWriter.class.getSimpleName());

    private static final String COMPONENT_INPUT_NODE_FACTORY =
        "org.knime.core.node.workflow.virtual.subnode.VirtualSubNodeInputNodeFactory";

    private static final String COMPONENT_OUTPUT_NODE_FACTORY =
        "org.knime.core.node.workflow.virtual.subnode.VirtualSubNodeOutputNodeFactory";

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
    }

    private BufferedWriter m_nodeInstances;

    private BufferedWriter m_connections;

    private static record NodeInstance(String repositoryItemId, String fullyQualifiedNodeId, String nodeName,
        String nodeFactory, String nodeType, String templateLink) {

        static final String HEADER =
            "repositoryItemId,fullyQualifiedNodeId,nodeName,nodeFactory,nodeType,templateLink%n".formatted();

        String asCsvLine() {
            return "\"%s\",%s,\"%s\",%s,%s,\"%s\"%n".formatted(repositoryItemId(), fullyQualifiedNodeId(), nodeName(),
                nodeFactory(), nodeType(), templateLink());
        }
    }

    private static record NodeProperty(String repositoryItemId, String fullyQualifiedNodeId, String propertyName,
        String propertyValue) {
    }

    private static record Connection(String repositoryItemId, Type type, String fullyQualifiedNodeIdSource,
        int sourcePort, String fullyQualifiedNodeIdTarget, int targetPort) {

        static final String HEADER = "repositoryItemId,type,fullyQualifiedNodeIdSource,sourcePort,"
            + "fullyQualifiedNodeIdTarget,targetPort%n".formatted();

        private enum Type {
                /** Regular data flow connections */
                SUCCESOR,
                /** Structural connections between metanodes/components and all their contained nodes. */
                CONTAINS,
                /** Virtual connection between a component and its component input node. */
                COMPONENT_START,
                /** Virtual connection between a component and its component output node. */
                COMPONENT_END,
                /** Virtual connections between a metanode and the nodes connected to its input bar. */
                METANODE_START,
                /** Virtual connections between the nodes connected to a metanodes output bar. */
                METANODE_END
        }

        String asCsvLine() {
            return "\"%s\",%s,%s,%d,%s,%d%n".formatted(repositoryItemId(), type().toString(),
                fullyQualifiedNodeIdSource(), sourcePort(), fullyQualifiedNodeIdTarget(), targetPort());
        }

    }

    /**
     * @param outputDir where to put the csv files
     * @throws IOException
     */
    public RelationshipWriter(final Path outputDir) throws IOException {
        LOGGER.info(() -> "Writing output csv files to %s".formatted(outputDir));

        // open output files
        m_nodeInstances = Files.newBufferedWriter(outputDir.resolve("node_instances.csv"), StandardOpenOption.CREATE);
        m_nodeInstances.write(NodeInstance.HEADER);
        // m_nodeProperties = Files.newBufferedWriter(outputDir.resolve("node_properties.csv"),
        m_connections = Files.newBufferedWriter(outputDir.resolve("connections.csv"), StandardOpenOption.CREATE);
        m_connections.write(Connection.HEADER);
    }

    @Override
    public void accept(final String repositoryItemId, final WorkflowDef workflow) {
        try {
            process("", workflow, repositoryItemId);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Recursively process the workflow and add all nodes, properties, and connections to the respective lists.
     *
     * @param repositoryItemId
     * @throws IOException
     */
    private void process(final String idPrefix, final WorkflowDef workflow, final String repositoryItemId)
        throws IOException {

        // store component input and output nodes if any
        var componentInputNodeId = OptionalInt.empty();
        var componentOutputNodeId = OptionalInt.empty();

        // add all nodes
        for (Map.Entry<String, BaseNodeDef> entry : workflow.getNodes().entrySet()) {
            // prefix all previous node ids to get a workflow-scope unique id

            final var node = entry.getValue();

            String qualifiedNodeId = qualify(idPrefix, entry.getValue().getId());

            NodeInstance nodeInstance = null;
            if (node instanceof NativeNodeDef nativeNode) {
                if (COMPONENT_INPUT_NODE_FACTORY.equals(nativeNode.getFactory())) {
                    componentInputNodeId = OptionalInt.of(nativeNode.getId());
                }
                if (COMPONENT_OUTPUT_NODE_FACTORY.equals(nativeNode.getFactory())) {
                    componentOutputNodeId = OptionalInt.of(nativeNode.getId());
                }
                nodeInstance = new NodeInstance(repositoryItemId, qualifiedNodeId, nativeNode.getNodeName(),
                    nativeNode.getFactory(), nativeNode.getNodeType().toString(), "");
            } else if (node instanceof ComponentNodeDef component) {
                nodeInstance = new NodeInstance(repositoryItemId, qualifiedNodeId, "", "",
                    component.getNodeType().toString(), component.getTemplateInfo().getUri());
                process(qualifiedNodeId, component.getWorkflow(), repositoryItemId);
            } else if (node instanceof MetaNodeDef metanode) {
                nodeInstance = new NodeInstance(repositoryItemId, qualifiedNodeId, "", "",
                    metanode.getNodeType().toString(), metanode.getLink().getUri());
                process(qualifiedNodeId, metanode.getWorkflow(), repositoryItemId);
            } else {
                throw new IllegalStateException();
            }
            m_nodeInstances.write(nodeInstance.asCsvLine());

            // add contains relationships
            if (!idPrefix.isEmpty()) {
                m_connections
                    .write(new Connection(repositoryItemId, Connection.Type.CONTAINS, idPrefix, 0, qualifiedNodeId, 0)
                        .asCsvLine());
            }

        }

        // add connections
        for (ConnectionDef connection : workflow.getConnections()) {

            String qualifiedSourceId = qualify(idPrefix, connection.getSourceID());
            String qualifiedTargetId = qualify(idPrefix, connection.getDestID());
            var connectionType = Connection.Type.SUCCESOR;

            // special case metanodes: use parent node's qualified node id as connection target
            if (connection.getSourceID() == -1) {
                qualifiedSourceId = idPrefix;
                connectionType = Connection.Type.METANODE_START;
            } else if (connection.getDestID() == -1) {
                qualifiedTargetId = idPrefix;
                connectionType = Connection.Type.METANODE_END;
            }

            m_connections.write(new Connection(repositoryItemId, connectionType, qualifiedSourceId,
                connection.getSourcePort(), qualifiedTargetId, connection.getDestPort()).asCsvLine());

        }

        if (componentInputNodeId.isPresent()) {
            // special case component nodes: add virtual connections to component input and output nodes
            m_connections.write(new Connection(repositoryItemId, Connection.Type.COMPONENT_START, idPrefix, 0,
                qualify(idPrefix, componentInputNodeId.getAsInt()), 0).asCsvLine());
        }

        if (componentOutputNodeId.isPresent()) {
            // special case component nodes: add virtual connections to component input and output nodes
            m_connections.write(new Connection(repositoryItemId, Connection.Type.COMPONENT_END,
                qualify(idPrefix, componentOutputNodeId.getAsInt()), 0, idPrefix, 0).asCsvLine());
        }

    }

    @Override
    public void close() throws Exception {
        m_connections.close();
        m_nodeInstances.close();
    }

    private static final String qualify(final String idPrefix, final int nodeId) {
        return idPrefix.isEmpty() ? Integer.toString(nodeId) : "%s:%s".formatted(idPrefix, nodeId);
    }

    /**
     * @param args First argument is a path to a directory that contains workflows or directories of workflows. <br/>
     *            Second argument is the path to the output directory. The program will mirror the input directory
     *            structure but instead of workflow will output json files. <br/>
     *            Third argument is a boolean flag indicating whether to skip duplicate workflows. <br/>
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        if (args.length < 2) {
            // print usage
            LOGGER.severe("Usage: java -jar converter.jar <input-directory> <output-directory> <skipDuplicates] [");
            System.exit(1);
        }

        // path to directory of workflows
        final var wfDirPath = Paths.get(args[0]);
        final var outPath = Paths.get(args[1]);
        final var skipDuplicates = args.length > 2 && Boolean.parseBoolean(args[2]);

        // create output path if it does not exist
        Files.createDirectories(outPath);

        try (final var processor = new RelationshipWriter(outPath)) {
            final var loader = new Loader(processor, skipDuplicates);
            // recursively walk the tree of directories and load all workflows
            loader.processDirectory(wfDirPath);
            // output load time, skipped dirs, etc.
            loader.logStats();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Could not close output files", ex);
        }

    }

}
