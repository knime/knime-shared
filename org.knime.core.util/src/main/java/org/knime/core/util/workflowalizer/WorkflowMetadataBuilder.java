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

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Builder for {@link WorkflowMetadata}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
class WorkflowMetadataBuilder extends AbstractRepositoryItemBuilder<WorkflowMetadata> {

    private static final Log LOGGER = LogFactory.getLog(WorkflowMetadataBuilder.class);

    private Integer m_svgWidth;
    private Integer m_svgHeight;
    private Optional<Collection<String>> m_artifacts;
    private Optional<String> m_workflowConfiguration;
    private Optional<String> m_workflowConfigurationRepresentation;
    private Optional<String> m_openapiInputParameters;
    private Optional<String> m_openapiInputResources;
    private Optional<String> m_openapiOutputParameters;
    private Optional<String> m_openapiOutputResources;
    private Optional<String> m_hubEventInputParameters;
    private Optional<WorkflowSetMeta> m_workflowSetMeta;
    private List<String> m_credentials;
    private List<String> m_variables;
    private Boolean m_hasReport;
    private Path m_svgFile;
    private String m_svgZipEntry;

    void setSvgWidth(final Integer svgWidth) {
        m_svgWidth = svgWidth;
    }

    void setSvgHeight(final Integer svgHeight) {
        m_svgHeight = svgHeight;
    }

    void setArtifactsFileNames(final Optional<Collection<String>> artifactsFileNames) {
        m_artifacts = artifactsFileNames;
    }

    void setWorkflowConfiguration(final String workflowConfiguration) {
        if (StringUtils.isEmpty(workflowConfiguration)) {
            m_workflowConfiguration = Optional.empty();
        } else {
            m_workflowConfiguration = Optional.of(workflowConfiguration);
        }
    }

    void setWorkflowConfigurationRepresentation(final String workflowConfigurationRepresentation) {
        if (StringUtils.isEmpty(workflowConfigurationRepresentation)) {
            m_workflowConfigurationRepresentation = Optional.empty();
        } else {
            m_workflowConfigurationRepresentation = Optional.of(workflowConfigurationRepresentation);
        }
    }

    void setOpenapiInputParameters(final String openapiInputParameters) {
        if (StringUtils.isEmpty(openapiInputParameters)) {
            m_openapiInputParameters = Optional.empty();
        } else {
            m_openapiInputParameters = Optional.of(openapiInputParameters);
        }
    }

    void setOpenapiInputResources(final String openapiInputResources) {
        if (StringUtils.isEmpty(openapiInputResources)) {
            m_openapiInputResources = Optional.empty();
        } else {
            m_openapiInputResources = Optional.of(openapiInputResources);
        }
    }

    void setOpenapiOutputParameters(final String openapiOutputParameters) {
        if (StringUtils.isEmpty(openapiOutputParameters)) {
            m_openapiOutputParameters = Optional.empty();
        } else {
            m_openapiOutputParameters = Optional.of(openapiOutputParameters);
        }
    }

    void setOpenapiOutputResources(final String openapiOutputResources) {
        if (StringUtils.isEmpty(openapiOutputResources)) {
            m_openapiOutputResources = Optional.empty();
        } else {
            m_openapiOutputResources = Optional.of(openapiOutputResources);
        }
    }

    void setHubEventInputParameters(final String hubEventInputParameters) {
        if (StringUtils.isEmpty(hubEventInputParameters)) {
            m_hubEventInputParameters = Optional.empty();
        } else {
            m_hubEventInputParameters = Optional.of(hubEventInputParameters);
        }
    }

    void setWorkflowSetMeta(final Optional<WorkflowSetMeta> workflowSetMeta) {
        m_workflowSetMeta = workflowSetMeta;
    }

    void setWorkflowCredentialsNames(final List<String> credentials) {
        m_credentials = credentials;
    }

    void setWorkflowVariables(final List<String> variables) {
        m_variables = variables;
    }

    void setHasReport(final Boolean hasReport) {
        m_hasReport = hasReport;
    }

    void setSvgFile(final Path svg) {
        m_svgFile = svg;
    }

    void setSvgZipEntry(final String zipEntryPath) {
        m_svgZipEntry = zipEntryPath;
    }

    Integer getSvgWidth() {
        return m_svgWidth;
    }

    Integer getSvgHeight() {
        return m_svgHeight;
    }

    Optional<Collection<String>> getArtifactsFileNames() {
        return m_artifacts;
    }

    Optional<String> getWorkflowConfiguration() {
        return m_workflowConfiguration;
    }

    Optional<String> getWorkflowConfigurationRepresentation() {
        return m_workflowConfigurationRepresentation;
    }

    Optional<String> getOpenapiInputParameters() {
        return m_openapiInputParameters;
    }

    Optional<String> getOpenapiInputResources() {
        return m_openapiInputResources;
    }

    Optional<String> getOpenapiOutputParameters() {
        return m_openapiOutputParameters;
    }

    Optional<String> getOpenapiOutputResources() {
        return m_openapiOutputResources;
    }

    Optional<String> getHubEventInputParameters() {
        return m_hubEventInputParameters;
    }

    Optional<WorkflowSetMeta> getWorkflowSetMeta() {
        return m_workflowSetMeta;
    }

    List<String> getWorkflowCredentialsNames() {
        return m_credentials;
    }

    List<String> getWorkflowVariables() {
        return m_variables;
    }

    Boolean getHasReport() {
        return m_hasReport;
    }

    Path getSvgFile() {
        return m_svgFile;
    }

    String getSvgZipEntry() {
        return m_svgZipEntry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    WorkflowMetadata buildExtraFields(final WorkflowalizerConfiguration wc) { // NOSONAR not very complex
        checkPopulated(getAuthor(), "author");
        checkPopulated(getAuthorDate(), "authored date");
        checkPopulated(getLastEditDate(), "last edited date");
        checkPopulated(getLastEditor(), "last editor");
        checkPopulated(m_artifacts, "artifacts directory");
        checkPopulated(m_credentials, "workflow credentials");
        checkPopulated(m_variables, "workflow variables");
        checkPopulated(m_hasReport, "has report");
        if (wc.parseWorkflowMeta()) {
            checkPopulated(m_workflowSetMeta, "workflowset.meta");
        }
        if (wc.parseWorkflowConfiguration()) {
            checkPopulated(getWorkflowConfiguration(), "workflow configuration");
        }
        if (wc.parseWorkflowConfigurationRepresentation()) {
            checkPopulated(getWorkflowConfigurationRepresentation(), "workflow configuration representation");
        }
        if (wc.parseOpenapiInputParameters()) {
            checkPopulated(getOpenapiInputParameters(), "openapi input parameters");
        }
        if (wc.parseOpenapiInputResources()) {
            checkPopulated(getOpenapiInputResources(), "openapi input resources");
        }
        if (wc.parseOpenapiOutputParameters()) {
            checkPopulated(getOpenapiOutputParameters(), "openapi output parameters");
        }
        if (wc.parseOpenapiOutputResources()) {
            checkPopulated(getOpenapiOutputResources(), "openapi output resources");
        }
        if (wc.parseHubEventInputParameters()) {
            checkPopulated(getHubEventInputParameters(), "hub event input parameters");
        }
        if (m_svgWidth == null || m_svgHeight == null) {
            LOGGER.warn("Could not extract SVG width/height for workflow: " + getWorkflowFields().getName());
        }
        return new WorkflowMetadata(this);
    }

}
