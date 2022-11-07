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
 *   Aug 30, 2022 (bjoern): created
 */
package org.knime.core.node.workflow.contextv2;

import java.nio.file.Path;
import java.util.UUID;

import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;

/**
 * Provides information about a multi-tenant executor that executes workflows as jobs.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public class JobExecutorInfo extends ExecutorInfo {

    private final UUID m_jobId;

    /**
     * The remote workflow editor allows to open a workflow job in AP, while the job is executed in a Server or Hub
     * executor. For that workflow job, we then have two workflow contexts: One in AP where this field is true, and one
     * in the Server/Hub executor, where this field is false.
     */
    private final boolean m_isRemote;

    JobExecutorInfo( //
            final ExecutorType type, //
            final String userId, //
            final Path workflowPath, //
            final Path tempFolder, //
            final UUID jobId, //
            final boolean isRemote) {
        super(type, userId, workflowPath, tempFolder);
        m_jobId = jobId;
        m_isRemote = isRemote;
    }

    /**
     * @return the ID of the workflow job.
     */
    public UUID getJobId() {
        return m_jobId;
    }

    /**
     * The remote workflow editor allows to open a workflow in Analytics Platform, while the execution is in KNIME
     * Server or Hub. For that workflow job, we then have two workflow contexts: One in Analytics Platform where this
     * method returns true, and one in KNIME Server/Hub, where this method returns false.
     *
     * @return true, if the workflow job is running in a remote executor, false otherwise (i.e. the current JVM is the
     *         executor).
     */
    public boolean isRemote() {
        return m_isRemote;
    }

    @Override
    void addFields(final StringBuilder sb, final int indent) {
        super.addFields(sb, indent);
        final var init = "  ".repeat(indent);
        sb.append(init).append("jobId=").append(m_jobId).append("\n");
    }
}
