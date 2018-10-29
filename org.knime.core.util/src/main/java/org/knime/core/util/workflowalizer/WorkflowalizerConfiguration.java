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
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
public class WorkflowalizerConfiguration {

    private boolean m_readVersion;
    private boolean m_readCreatedBy;
    private boolean m_readName;
    private boolean m_readCustomDescription;
    private boolean m_readAnnotations;
    private boolean m_readNodes;
    private boolean m_readConnections;
    private boolean m_readUnexpectedFiles;

    private boolean m_readId;
    private boolean m_readType;

    private boolean m_readModelParameters;
    private boolean m_readAnnotationText;
    private boolean m_readNodeCustomDescription;

    private boolean m_readAuthor;
    private boolean m_readAuthorDate;
    private boolean m_readLastEditor;
    private boolean m_readLastEditedDate;

    private boolean m_readWorkflowSetAuthor;
    private boolean m_readWorkflowSetComments;
    private boolean m_readSVG;
    private boolean m_readArtifacts;

    private boolean m_readTemplateLink;

    private boolean m_readNodeName;
    private boolean m_readFactoryClass;
    private boolean m_readBundleName;
    private boolean m_readBundleSymbolicName;
    private boolean m_readBundleVendor;
    private boolean m_readBundleVersion;
    private boolean m_readFeatureName;
    private boolean m_readFeatureSymbolicName;
    private boolean m_readFeatureVendor;
    private boolean m_readFeatureVersion;

    private WorkflowalizerConfiguration() {
        // Do nothing
    }

    boolean parseVersion() {
        return m_readVersion;
    }

    boolean parseCreatedBy() {
        return m_readCreatedBy;
    }

    boolean parseName() {
        return m_readName;
    }

    boolean parseCustomDescription() {
        return m_readCustomDescription;
    }

    boolean parseAnnotations() {
        return m_readAnnotations;
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

    boolean parseId() {
        return m_readId;
    }

    boolean parseType() {
        return m_readType;
    }

    boolean parseModelParameters() {
        return m_readModelParameters;
    }

    boolean parseAnnotationText() {
        return m_readAnnotationText;
    }

    boolean parseNodeCustomDescription() {
        return m_readNodeCustomDescription;
    }

    boolean parseAuthor() {
        return m_readAuthor;
    }

    boolean parseAuthorDate() {
        return m_readAuthorDate;
    }

    boolean parseLastEditor() {
        return m_readLastEditor;
    }

    boolean parseLastEditedDate() {
        return m_readLastEditedDate;
    }

    boolean parseWorkflowSetAuthor() {
        return m_readWorkflowSetAuthor;
    }

    boolean parseWorkflowSetComments() {
        return m_readWorkflowSetComments;
    }

    boolean parseSVG() {
        return m_readSVG;
    }

    boolean parseArtifacts() {
        return m_readArtifacts;
    }

    boolean parseTemplateLink() {
        return m_readTemplateLink;
    }

    boolean parseNodeName() {
        return m_readNodeName;
    }

    boolean parseFactoryClass() {
        return m_readFactoryClass;
    }

    boolean parseBundleName() {
        return m_readBundleName;
    }

    boolean parseBundleSymbolicName() {
        return m_readBundleSymbolicName;
    }

    boolean parseBundleVendor() {
        return m_readBundleVendor;
    }

    boolean parseBundleVersion() {
        return m_readBundleVersion;
    }

    boolean parseFeatureName() {
        return m_readFeatureName;
    }

    boolean parseFeatureSymbolicName() {
        return m_readFeatureSymbolicName;
    }

    boolean parseFeatureVendor() {
        return m_readFeatureVendor;
    }

    boolean parseFeatureVersion() {
        return m_readFeatureVersion;
    }

    WorkflowFields createWorkflowFields() {
        return new WorkflowFields(m_readVersion, m_readCreatedBy, m_readName, m_readCustomDescription,
            m_readAnnotations, m_readConnections, m_readNodes, m_readUnexpectedFiles);
    }

    NodeFields createNodeFields() {
        return new NodeFields(m_readId, m_readType, m_readAnnotationText);
    }

    SingleNodeFields createSingleNodeFields() {
        return new SingleNodeFields(m_readId, m_readType, m_readModelParameters, m_readAnnotationText,
            m_readNodeCustomDescription);
    }

    /**
     * @return a builder for WorkflowalizerConfiguration
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link WorkflowalizerConfiguration}
     */
    public static final class Builder {

        private boolean m_version;
        private boolean m_createdBy;
        private boolean m_name;
        private boolean m_customDescription;
        private boolean m_annotations;
        private boolean m_nodes;
        private boolean m_connections;
        private boolean m_unexpectedFiles;
        private boolean m_id;
        private boolean m_type;
        private boolean m_modelParameters;
        private boolean m_annotationText;
        private boolean m_nodeCustomDescription;
        private boolean m_author;
        private boolean m_authorDate;
        private boolean m_lastEditor;
        private boolean m_lastEditedDate;
        private boolean m_workflowSetAuthor;
        private boolean m_workflowSetComments;
        private boolean m_svg;
        private boolean m_artifacts;
        private boolean m_templateLink;
        private boolean m_nodeName;
        private boolean m_factoryClass;
        private boolean m_bundleName;
        private boolean m_bundleSymbolicName;
        private boolean m_bundleVendor;
        private boolean m_bundleVersion;
        private boolean m_featureName;
        private boolean m_featureSymbolicName;
        private boolean m_featureVendor;
        private boolean m_featureVersion;

        /**
         * Sets the configuration to read the workflow version.
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readVersion() {
            m_version = true;
            return this;
        }

        /**
         * Sets the configuration to read the created by version
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readCreatedBy() {
            m_createdBy = true;
            return this;
        }

        /**
         * Sets the configuration to read the workflow name
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readName() {
            m_name = true;
            return this;
        }

        /**
         * Sets the configuration to read the custom workflow description
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readCustomDescription() {
            m_customDescription = true;
            return this;
        }

        /**
         * Sets the configuration to read the workflow annotations
         *
         * <p>
         * This field will be read for: workflows, metanodes, subnodes, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readAnnotations() {
            m_annotations = true;
            return this;
        }

        /**
         * Sets the configuration to read the nodes
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
         * Sets the configuration to read the nodes and the connections
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
         * Sets the configuration to read all {@link WorkflowMetadata} fields
         *
         * @return the builder
         */
        public Builder readWorkflowFields() {
            m_version = true;
            m_createdBy = true;
            m_name = true;
            m_customDescription = true;
            m_annotations = true;
            m_nodes = true;
            m_connections = true;
            m_unexpectedFiles = true;
            return this;
        }

        /**
         * Sets the configuration to read the node ID
         *
         * <p>
         * This field will be read for: metanodes, subnodes, and native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readId() {
            m_id = true;
            return this;
        }

        /**
         * Sets the configuration to read the the node type
         *
         * <p>
         * This field will be read for: metanodes, subnodes, and native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readType() {
            m_type = true;
            return this;
        }

        /**
         * Sets the configuration to read the node annotation text
         *
         * <p>
         * This field will be read for: metanodes, subnodes, and native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readAnnotationText() {
            m_annotationText = true;
            return this;
        }

        /**
         * Sets the configuration to read all the {@link NodeMetadata} fields
         *
         * @return the builder
         */
        public Builder readNodeFields() {
            m_id = true;
            m_type = true;
            m_annotationText = true;
            return this;
        }

        /**
         * Sets the configuration to read the node model parameters
         *
         * <p>
         * This field will be read for: subnodes, and native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readModelParameters() {
            m_modelParameters = true;
            return this;
        }

        /**
         * Sets the configuration to read the custom node description
         *
         * <p>
         * This field will be read for: subnodes, and native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readNodeCustomDescription() {
            m_nodeCustomDescription = true;
            return this;
        }

        /**
         * Sets the configuration to read all the {@link SingleNodeMetadata} fields
         *
         * @return the builder
         */
        public Builder readSingleNodeFields() {
            m_modelParameters = true;
            m_nodeCustomDescription = true;
            return this;
        }

        /**
         * Sets the configuration to read the workflow author
         *
         * <p>
         * This field will be read for: workflows, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readAuthor() {
            m_author = true;
            return this;
        }

        /**
         * Sets the configuration to read the the workflow creation date
         *
         * <p>
         * This field will be read for: workflows, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readAuthorDate() {
            m_authorDate = true;
            return this;
        }

        /**
         * Sets the configuration to read the last workflow editor
         *
         * <p>
         * This field will be read for: workflows, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readLastEditor() {
            m_lastEditor = true;
            return this;
        }

        /**
         * Sets the configuration to read the last edited date
         *
         * <p>
         * This field will be read for: workflows, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readLastEditedDate() {
            m_lastEditedDate = true;
            return this;
        }

        /**
         * Sets the configuration to read all {@link AuthorInformation} fields
         *
         * <p>
         * This field will be read for: workflows, and templates
         * </p>
         *
         * @return the builder
         */
        public Builder readAuthorInformation() {
            m_author = true;
            m_authorDate = true;
            m_lastEditor = true;
            m_lastEditedDate = true;
            return this;
        }

        /**
         * Sets the configuration to read the Author field in workflowset.meta
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         */
        public Builder readWorkflowSetAuthor() {
            m_workflowSetAuthor = true;
            return this;
        }

        /**
         * Sets the configuration to read the Comments field in workflowset.meta
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         */
        public Builder readWorkflowSetComments() {
            m_workflowSetComments = true;
            return this;
        }

        /**
         * Sets the configuration to read all fields in {@link WorkflowSetMeta}
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         */
        public Builder readWorkflowSetMeta() {
            m_workflowSetAuthor = true;
            m_workflowSetComments = true;
            return this;
        }

        /**
         * Sets the configuration to read the workflow SVG file path
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         */
        public Builder readSvg() {
            m_svg = true;
            return this;
        }

        /**
         * Sets the configuration to read the file paths in the artifacts directory
         *
         * <p>
         * This field will be read for: workflows
         * </p>
         *
         * @return the builder
         */
        public Builder readArtifacts() {
            m_artifacts = true;
            return this;
        }

        /**
         * Sets the configuration to read the template links for metanodes
         *
         * <p>
         * This field will be read for: metanodes, and subnodes
         * </p>
         *
         * @return the builder
         */
        public Builder readTemplateLink() {
            m_templateLink = true;
            return this;
        }

        /**
         * Sets the configuration to read the node name
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readNodeName() {
            m_nodeName = true;
            return this;
        }

        /**
         * Sets the configuration to read the node factory class
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readFactoryClass() {
            m_factoryClass = true;
            return this;
        }

        /**
         * Sets the configuration to read the bundle name
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readBundleName() {
            m_bundleName = true;
            return this;
        }

        /**
         * Sets the configuration to read the bundle symbolic name
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readBundleSymbolicName() {
            m_bundleSymbolicName = true;
            return this;
        }

        /**
         * Sets the configuration to read the bundle vendor
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readBundleVendor() {
            m_bundleVendor = true;
            return this;
        }

        /**
         * Sets the configuration to read the bundle version
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readBundleVersion() {
            m_bundleVersion = true;
            return this;
        }

        /**
         * Sets the configuration to read the feature name
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readFeatureName() {
            m_featureName = true;
            return this;
        }

        /**
         * Sets the configuration to read the feature symbolic name
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readFeatureSymbolicName() {
            m_featureSymbolicName = true;
            return this;
        }

        /**
         * Sets the configuration to read the feature vendor
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readFeatureVendor() {
            m_featureVendor = true;
            return this;
        }

        /**
         * Sets the configuration to read the feature version
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readFeatureVersion() {
            m_featureVersion = true;
            return this;
        }

        /**
         * Sets the configuration to read all the {@link NodeAndBundleInformation} fields
         *
         * <p>
         * This field will be read for: native nodes
         * </p>
         *
         * @return the builder
         */
        public Builder readNodeAndBundleInformation() {
            m_nodeName = true;
            m_factoryClass = true;
            m_bundleName = true;
            m_bundleSymbolicName = true;
            m_bundleVendor = true;
            m_bundleVersion = true;
            m_featureName = true;
            m_featureSymbolicName = true;
            m_featureVendor = true;
            m_featureVersion = true;
            return this;
        }

        /**
         * Sets the configuration to read all the fields
         *
         * @return the builder
         */
        public Builder readAll() {
            m_version = true;
            m_createdBy = true;
            m_name = true;
            m_customDescription = true;
            m_annotations = true;
            m_nodes = true;
            m_connections = true;
            m_unexpectedFiles = true;
            m_id = true;
            m_type = true;
            m_modelParameters = true;
            m_annotationText = true;
            m_nodeCustomDescription = true;
            m_author = true;
            m_authorDate = true;
            m_lastEditor = true;
            m_lastEditedDate = true;
            m_workflowSetAuthor = true;
            m_workflowSetComments = true;
            m_svg = true;
            m_artifacts = true;
            m_templateLink = true;
            m_nodeName = true;
            m_factoryClass = true;
            m_bundleName = true;
            m_bundleSymbolicName = true;
            m_bundleVendor = true;
            m_bundleVersion = true;
            m_featureName = true;
            m_featureSymbolicName = true;
            m_featureVendor = true;
            m_featureVersion = true;
            return this;
        }

        /**
         * Builds the configured {@link WorkflowalizerConfiguration}
         *
         * @return the configured {@link WorkflowalizerConfiguration}
         */
        public WorkflowalizerConfiguration build() {
            final WorkflowalizerConfiguration config = new WorkflowalizerConfiguration();
            config.m_readVersion = m_version;
            config.m_readCreatedBy = m_createdBy;
            config.m_readName = m_name;
            config.m_readCustomDescription = m_customDescription;
            config.m_readAnnotations = m_annotations;
            config.m_readNodes = m_nodes;
            config.m_readConnections = m_connections;
            config.m_readUnexpectedFiles = m_unexpectedFiles;
            config.m_readId = m_id;
            config.m_readType = m_type;
            config.m_readModelParameters = m_modelParameters;
            config.m_readAnnotationText = m_annotationText;
            config.m_readNodeCustomDescription = m_nodeCustomDescription;
            config.m_readAuthor = m_author;
            config.m_readAuthorDate = m_authorDate;
            config.m_readLastEditor = m_lastEditor;
            config.m_readLastEditedDate = m_lastEditedDate;
            config.m_readWorkflowSetAuthor = m_workflowSetAuthor;
            config.m_readWorkflowSetComments = m_workflowSetComments;
            config.m_readSVG = m_svg;
            config.m_readArtifacts = m_artifacts;
            config.m_readTemplateLink = m_templateLink;
            config.m_readNodeName = m_nodeName;
            config.m_readFactoryClass = m_factoryClass;
            config.m_readBundleName = m_bundleName;
            config.m_readBundleSymbolicName = m_bundleSymbolicName;
            config.m_readBundleVendor = m_bundleVendor;
            config.m_readBundleVersion = m_bundleVersion;
            config.m_readFeatureName = m_featureName;
            config.m_readFeatureSymbolicName = m_featureSymbolicName;
            config.m_readFeatureVendor = m_featureVendor;
            config.m_readFeatureVersion = m_featureVersion;
            return config;
        }
    }
}
