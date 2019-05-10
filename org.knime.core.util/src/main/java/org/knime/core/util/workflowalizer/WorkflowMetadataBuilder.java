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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Builder for {@link WorkflowMetadata}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
class WorkflowMetadataBuilder extends AbstractWorkflowBuilder<WorkflowMetadata> {

    private static final Log LOGGER = LogFactory.getLog(WorkflowMetadataBuilder.class);

    private Integer m_svgWidth;
    private Integer m_svgHeight;
    private Optional<Collection<String>> m_artifacts;
    private String m_author;
    private Date m_authorDate;
    private Optional<String> m_lastEditor;
    private Optional<Date> m_lastEditDate;
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

    void setAuthor(final String author) {
        m_author = author;
    }

    void setAuthorDate(final Date authorDate) {
        m_authorDate = authorDate;
    }

    void setLastEditor(final Optional<String> lastEditor) {
        m_lastEditor = lastEditor;
    }

    void setLastEditDate(final Optional<Date> lastEditDate) {
        m_lastEditDate = lastEditDate;
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

    String getAuthor() {
        return m_author;
    }

    Date getAuthorDate() {
        return m_authorDate;
    }

    Optional<String> getLastEditor() {
        return m_lastEditor;
    }

    Optional<Date> getLastEditDate() {
        return m_lastEditDate;
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
    WorkflowMetadata buildExtraFields(final WorkflowalizerConfiguration wc) {
        checkPopulated(m_author, "author");
        checkPopulated(m_authorDate, "authored date");
        checkPopulated(m_lastEditDate, "last edited date");
        checkPopulated(m_lastEditor, "last editor");
        checkPopulated(m_artifacts, "artifacts directory");
        checkPopulated(m_credentials, "workflow credentials");
        checkPopulated(m_variables, "workflow variables");
        checkPopulated(m_hasReport, "has report");
        if (wc.parseWorkflowMeta()) {
            checkPopulated(m_workflowSetMeta, "workflowset.meta");
        }
        if (m_svgWidth == null || m_svgHeight == null) {
            LOGGER.warn("Could not extract SVG width/height for workflow: " + getWorkflowFields().getName());
        }
        return new WorkflowMetadata(this);
    }

}
