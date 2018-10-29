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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.knime.core.util.Version;

/**
 * Represents the metadata of a top level KNIME workflow (i.e. not a metanode).
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
public final class TopLevelWorkflowMetadata extends AbstractWorkflowMetadata {

    private final AuthorInformation m_authorInfo;
    private final Optional<String> m_svg;
    private final Optional<Collection<String>> m_artifacts;
    private final Optional<WorkflowSetMeta> m_workflowSetMeta;

    TopLevelWorkflowMetadata(final Version version, final Version createdBy, final Optional<List<String>> annotations,
        final List<NodeConnection> connections, final List<NodeMetadata> nodes, final Optional<String> name,
        final Optional<String> customDescription, final Collection<String> unexpectedFiles,
        final AuthorInformation authorInfo, final Optional<String> svg, final Optional<Collection<String>> artifacts,
        final Optional<WorkflowSetMeta> workflowSetMeta) {
        super(version, createdBy, annotations, connections, nodes, name, customDescription, unexpectedFiles);
        m_authorInfo = authorInfo;
        m_svg = svg;
        m_artifacts = artifacts;
        m_workflowSetMeta = workflowSetMeta;
    }

    /**
     * @return the {@link AuthorInformation} associated with this workflow
     * @throws UnsupportedOperationException when field hasn't been read (i.e. when field is {@code null})
     */
    public AuthorInformation getAuthorInformation() {
        if (m_authorInfo == null) {
            throw new UnsupportedOperationException("getAuthorInformation() is unsupported, field was not read");
        }
        return m_authorInfo;
    }

    /**
     * @return a file path for the workflow SVG if present
     * @throws UnsupportedOperationException when field hasn't been read (i.e. when field is {@code null})
     */
    public Optional<String> getWorkflowSvg() {
        if (m_svg == null) {
            throw new UnsupportedOperationException("getWorkflowSvg() is unsupported, field was not read");
        }
        return m_svg;
    }

    /**
     * @return a collection of file paths for items in the artifacts directory, if the directory exists
     * @throws UnsupportedOperationException when field hasn't been read (i.e. when field is {@code null})
     */
    public Optional<Collection<String>> getArtifacts() {
        if (m_artifacts == null) {
            throw new UnsupportedOperationException("getArtifacts() is unsupported, field was not read");
        }
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

}
