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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Represents the metadata of a top level KNIME workflow (i.e. not a metanode).
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonTypeInfo(include = As.PROPERTY, use = Id.CLASS)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public final class WorkflowMetadata extends AbstractWorkflowMetadata<WorkflowMetadataBuilder> {

    @JsonProperty("author_information")
    private final AuthorInformation m_authorInfo;

    @JsonProperty("workflow_svg")
    private final Optional<String> m_svg;

    @JsonProperty("artifacts_files")
    private final Optional<Collection<String>> m_artifacts;

    @JsonProperty("workflow_meta")
    private final Optional<WorkflowSetMeta> m_workflowSetMeta;

    @JsonProperty("workflow_credentials")
    private final List<String> m_credentials;

    @JsonProperty("workflow_variables")
    private final List<String> m_variables;

    WorkflowMetadata(final WorkflowMetadataBuilder builder) {
        super(builder);
        m_authorInfo = new AuthorInformation(builder.getAuthor(), builder.getAuthorDate(), builder.getLastEditor(),
            builder.getLastEditDate());
        m_svg = builder.getWorkflowSVGFile();
        m_artifacts = builder.getArtifactsFileNames();
        m_workflowSetMeta = builder.getWorkflowSetMeta();
        m_credentials = builder.getWorkflowCredentialsNames();
        m_variables = builder.getWorkflowVariables();
    }

    /**
     * For internal use only! This constructor creates a copy of the given {@code WorkflowMetadata}, but sets the
     * connections to {@code null} and flattens the node tree
     *
     * @param workflow the {@code WorkflowMetadata} to copy
     */
    private WorkflowMetadata(final WorkflowMetadata workflow) {
        super(workflow);
        m_authorInfo = workflow.m_authorInfo;
        m_svg = workflow.m_svg;
        m_artifacts = workflow.m_artifacts;
        m_workflowSetMeta = workflow.m_workflowSetMeta;
        m_credentials = workflow.m_credentials;
        m_variables = workflow.m_variables;
    }

    /**
     * @return the {@link AuthorInformation} associated with this workflow
     */
    public AuthorInformation getAuthorInformation() {
        return m_authorInfo;
    }

    /**
     * @return a file path for the workflow SVG if present
     */
    public Optional<String> getWorkflowSvg() {
        return m_svg;
    }

    /**
     * @return a collection of file paths for items in the artifacts directory, if the directory exists
     */
    public Optional<Collection<String>> getArtifacts() {
        return m_artifacts;
    }

    /**
     * @return {@link WorkflowSetMeta} containing fields from workflowset file, if the file existed
     * @throws UnsupportedOperationException when field hasn't been read (i.e. when field is {@code null})
     */
    public Optional<WorkflowSetMeta> getWorkflowSetMetadata() {
        if (m_workflowSetMeta == null) {
            throw new UnsupportedOperationException("getWorkflowSetMetadata() is unsupported, field was not read");
        }
        return m_workflowSetMeta;
    }

    /**
     * @return a list of workflow credentials' names
     */
    public List<String> getWorkflowCredentialsNames() {
        return m_credentials;
    }

    /**
     * @return a list of workflow variable names
     */
    public List<String> getWorkflowVariables() {
        return m_variables;
    }

    @Override
    public String toString() {
        String artifacts = null;
        if (m_artifacts != null) {
            artifacts = "[";
            if (m_artifacts.isPresent()) {
                artifacts += String.join(", ", m_artifacts.get());
            }
            artifacts += "]";
        }

        String wsm = null;
        if (m_workflowSetMeta != null) {
            wsm = m_workflowSetMeta.isPresent() ? m_workflowSetMeta.toString() : null;
        }

        return super.toString() +
                ", " + m_authorInfo +
                ", workflow_svg: " + m_svg.orElse(null) +
                ", artifacts_files: " + artifacts +
                ", workflow_meta: " + wsm +
                ", workflow_credentials: [" + String.join(", ", m_credentials) + "]" +
                ", workflow_variables: [" + String.join(", ", m_variables) + "]";
    }

    /**
     * @return an {@link ObjectMapper} configured to read WorkflowMetadata
     */
    public static ObjectMapper getConfiguredObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        // don't write null fields
        mapper.setSerializationInclusion(Include.NON_NULL);

        // Jdk8Module will serialize Optional fields as their value or null
        mapper.registerModule(new Jdk8Module());

        // Add custom serializers for Version and ConfigBase fields
        final SimpleModule sm = new SimpleModule();
        sm.addSerializer(new VersionSerializer());
        sm.addSerializer(new ConfigBaseEntrySerializer());
        mapper.registerModule(sm);

        // Add format for serializing Date fields
        // @JsonFormat isn't used because it converts Date objects to UTC time
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        mapper.setDateFormat(df);

        return mapper;
    }

    /**
     * Creates a {@code WorkflowMetadata} with a flattened node list for writing to JSON.
     *
     * @return a {@code WorkflowMetadata} whose node list is flat (i.e. depth = 1), and whose connections have been set
     *         to {@code null}
     */
    public WorkflowMetadata flatten() {
        return new WorkflowMetadata(this);
    }
}
