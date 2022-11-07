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
 *   Aug 26, 2022 (bjoern): created
 */
package org.knime.core.node.workflow.contextv2;

import java.nio.file.Path;

import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;

/**
 * Provides information about the process that runs the current workflow.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public abstract class ExecutorInfo {

    private final ExecutorType m_type;

    private final String m_userId;

    /**
     * The {@link Path} of the current workflow in the local file system of the workflow executor. If {@link #getType()}
     * returns {@link LocationType#WORKSPACE}, then this path is the actual location where the workflow resides/stored.
     * For other {@link LocationType}s, this path is only the path of a local job copy.
     */
    private final Path m_localWorkflowPath;

    /**
     * {@link Path} of a temporary folder in the local file system of the workflow executor. The folder can be used by
     * the workflow to store temporary files.
     */
    private final Path m_tempFolder;

    ExecutorInfo( //
            final ExecutorType type, //
            final String userId, //
            final Path workflowPath, //
            final Path tempFolder) {
        m_type = type;
        m_userId = userId;
        m_localWorkflowPath = workflowPath;
        m_tempFolder = tempFolder;
    }

    /**
     * @return the type of executor running the current workflow.
     */
    public ExecutorType getType() {
        return m_type;
    }

    /**
     * @return the user ID of the user running the current workflow.
     */
    public String getUserId() {
        return m_userId;
    }

    /**
     * @return true, if the executor that runs the current workflow does not have a SWT/Swing UI.
     */
    public boolean isHeadless() {
        return true;
    }

    /**
     * Provides the {@link Path} of the current workflow in the local file system of the workflow executor. If
     * {@link LocationInfo#getType()} returns {@link LocationType#LOCAL}, then this path is the actual location
     * where the workflow resides/stored. For other {@link LocationType}s, this path is only the path of a local job
     * copy.
     *
     * @return path of workflow in local file system of the workflow executor.
     */
    public Path getLocalWorkflowPath() {
        return m_localWorkflowPath;
    }

    /**
     * {@link Path} of a temporary folder in the local file system of the workflow executor. The folder can be used by
     * the workflow to store temporary files. The folder may or may not exist. If it does not exist, the workflow will
     * create the folder denoted by this path and also delete it when closed. Otherwise it will not delete it.
     *
     * @return the {@link Path} of a temporary folder which can be used by the workflow to store temporary files (not
     *         null).
     */
    public Path getTempFolder() {
        return m_tempFolder;
    }

    /**
     * Adds a string representation of this executor info to the given string builder.
     *
     * @param sb string builder to add to
     * @param indent indentation level
     */
    final StringBuilder toString(final StringBuilder sb, final int indent) {
        sb.append("  ".repeat(indent)).append(getClass().getSimpleName()).append("[\n");
        addFields(sb, indent + 1);
        return sb.append("  ".repeat(indent)).append("]\n");
    }

    void addFields(final StringBuilder sb, final int indent) {
        final var init = "  ".repeat(indent);
        sb.append(init).append("type=").append(m_type).append("\n");
        sb.append(init).append("userId=").append(m_userId).append("\n");
        sb.append(init).append("localWorkflowPath=").append(m_localWorkflowPath).append("\n");
        sb.append(init).append("tempFolder=").append(m_tempFolder).append("\n");
    }

    @Override
    public String toString() {
        return toString(new StringBuilder(), 0).toString();
    }
}
