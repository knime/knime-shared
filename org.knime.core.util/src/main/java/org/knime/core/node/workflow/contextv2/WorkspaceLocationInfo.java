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
 *   Aug 29, 2022 (bjoern): created
 */
package org.knime.core.node.workflow.contextv2;

import java.net.URI;
import java.nio.file.Path;

import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;

/**
 * Provides information about an Eclipse workspace in the local file system, which holds the current workflow.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class WorkspaceLocationInfo extends LocationInfo {

    /**
     * The path of the Eclipse workspace in the local file system, which holds the current workflow.
     */
    private final Path m_workspacePath;

    WorkspaceLocationInfo(final LocationType type, //
        final Path localWorkflowCopyPath, //
        final Path originalWorkflowPath, //
        final Path tempFolder, //
        final URI mountpointURI, //
        final Path workspacePath) {

        super(type, localWorkflowCopyPath, originalWorkflowPath, tempFolder, mountpointURI);
        m_workspacePath = workspacePath;
    }

    /**
     * @return the path of the Eclipse workspace in the local file system, which holds the current workflow.
     */
    public Path getWorkspacePath() {
        return m_workspacePath;
    }

    /**
     * Builder class for {@link WorkspaceLocationInfo}
     */
    public static final class Builder extends BaseBuilder<Builder, WorkspaceLocationInfo> {

        private Path m_workspacePath;

        /**
         * Constructor.
         */
        public Builder() {
            super(LocationType.WORKSPACE);
        }

        /**
         * Sets the local path of the Eclipse workspace where the current workflow resides.s
         *
         * @param workspacePath The local path of the Eclipse workspace.
         * @return this builder instance.
         */
        public Builder withWorkspacePath(final Path workspacePath) {
            m_workspacePath = workspacePath;
            return this;
        }

        @Override
        protected void checkFields() {
            super.checkFields();
            CheckUtils.checkArgumentNotNull(m_workspacePath, "Workspace path must not be null");
        }

        @Override
        public WorkspaceLocationInfo build() {
            checkFields();
            return new WorkspaceLocationInfo(m_type, //
                m_localWorkflowPath, //
                m_originalLocalWorkflowPath, //
                m_tempFolder,
                m_mountpointURI, //
                m_workspacePath);
        }
    }
}
