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
 *   Oct 12, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

/**
 * Configuration to specify which fields should be read by the {@link Workflowalizer}.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public class WorkflowalizerConfiguration {

    private boolean m_readNodes;
    private boolean m_readConnections;
    private boolean m_readUnexpectedFiles;
    private boolean m_readNodeConfiguration;
    private boolean m_readWorkflowMeta;
    private boolean m_readWorkflowConfiguration;
    private boolean m_readWorkflowConfigurationRepresentation;
    private boolean m_readOpenapiInputParameters;
    private boolean m_readOpenapiInputResources;
    private boolean m_readOpenapiOutputParameters;
    private boolean m_readOpenapiOutputResources;

    private WorkflowalizerConfiguration() {
        // Do nothing
    }

    boolean parseNodes() {
        return m_readNodes;
    }

    boolean parseConnections() {
        return m_readConnections;
    }

    boolean parseUnexpectedFiles() {
        return m_readUnexpectedFiles;
    }

    boolean parseNodeConfiguration() {
        return m_readNodeConfiguration;
    }

    boolean parseWorkflowMeta() {
        return m_readWorkflowMeta;
    }

    boolean parseWorkflowConfiguration() {
        return m_readWorkflowConfiguration;
    }

    boolean parseWorkflowConfigurationRepresentation() {
        return m_readWorkflowConfigurationRepresentation;
    }

    boolean parseOpenapiInputParameters() {
        return m_readOpenapiInputParameters;
    }

    boolean parseOpenapiInputResources() {
        return m_readOpenapiInputResources;
    }

    boolean parseOpenapiOutputParameters() {
        return m_readOpenapiOutputParameters;
    }

    boolean parseOpenapiOutputResources() {
        return m_readOpenapiOutputResources;
    }

    WorkflowFields createWorkflowFields() {
        return new WorkflowFields(m_readConnections, m_readNodes, m_readUnexpectedFiles);
    }

    NodeFields createNodeFields() {
        return new NodeFields();
    }

    SingleNodeFields createSingleNodeFields() {
        return new SingleNodeFields(m_readNodeConfiguration);
    }

    /**
     * @return a builder for WorkflowalizerConfiguration
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link WorkflowalizerConfiguration}.
     */
    public static final class Builder {
        private boolean m_nodes;
        private boolean m_connections;
        private boolean m_unexpectedFiles;
        private boolean m_nodeConfiguration;
        private boolean m_workflowMeta;
        private boolean m_workflowConfiguration;
        private boolean m_workflowConfigurationRepresentation;
        private boolean m_openapiInputParameters;
        private boolean m_openapiInputResources;
        private boolean m_openapiOutputParameters;
        private boolean m_openapiOutputResources;

        /**
         * Sets the configuration to read the nodes.
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readNodes() {
            m_nodes = true;
            return this;
        }

        /**
         * Sets the configuration to read the nodes and the connections.
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readNodesAndConnections() {
            m_nodes = true;
            m_connections = true;
            return this;
        }

        /**
         * Sets the configuration to read the unexpected file paths
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readUnexpectedFiles() {
            m_unexpectedFiles = true;
            return this;
        }

        /**
         * Sets the configuration to read the node configuration.
         *
         * <p>
         * This field will be read for: subnodes, and native nodes
         * </p>
         *
         * @return the builder
         * @since 5.11
         */
        public Builder readNodeConfiguration() {
            m_nodeConfiguration = true;
            return this;
        }

        /**
         * Sets the configuration to read the author and comments workflowset.meta.
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         */
        public Builder readWorkflowMeta() {
            m_workflowMeta = true;
            return this;
        }

        /**
         * Sets the configuration to read the workflow configurations files.
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         * @since 5.16
         */
        public Builder readWorkflowConfigurationFiles() {
            m_workflowConfiguration = true;
            m_workflowConfigurationRepresentation = true;
            return this;
        }

        /**
         * Sets the configuration to read the openapi files.
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         */
        public Builder readOpenapiFiles() {
            m_openapiInputParameters = true;
            m_openapiInputResources = true;
            m_openapiOutputParameters = true;
            m_openapiOutputResources = true;
            return this;
        }

        /**
         * Sets the configuration to read all the fields.
         *
         * @return the builder
         */
        public Builder readAll() {
            m_nodes = true;
            m_connections = true;
            m_unexpectedFiles = true;
            m_nodeConfiguration = true;
            m_workflowMeta = true;
            m_workflowConfiguration = true;
            m_workflowConfigurationRepresentation = true;
            m_openapiInputParameters = true;
            m_openapiInputResources = true;
            m_openapiOutputParameters = true;
            m_openapiOutputResources = true;
            return this;
        }

        /**
         * Builds the configured {@link WorkflowalizerConfiguration}.
         *
         * @return the configured {@link WorkflowalizerConfiguration}
         */
        public WorkflowalizerConfiguration build() {
            final WorkflowalizerConfiguration config = new WorkflowalizerConfiguration();
            config.m_readNodes = m_nodes;
            config.m_readConnections = m_connections;
            config.m_readUnexpectedFiles = m_unexpectedFiles;
            config.m_readNodeConfiguration = m_nodeConfiguration;
            config.m_readWorkflowMeta = m_workflowMeta;
            config.m_readWorkflowConfiguration = m_workflowConfiguration;
            config.m_readWorkflowConfigurationRepresentation = m_workflowConfigurationRepresentation;
            config.m_readOpenapiInputParameters = m_openapiInputParameters;
            config.m_readOpenapiInputResources = m_openapiInputResources;
            config.m_readOpenapiOutputParameters = m_openapiOutputParameters;
            config.m_readOpenapiOutputResources = m_openapiOutputResources;
            return config;
        }
    }
}
