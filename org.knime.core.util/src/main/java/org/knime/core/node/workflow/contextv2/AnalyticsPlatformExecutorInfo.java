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

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;

import org.knime.core.node.workflow.contextv2.AnalyticsPlatformExecutorInfoBuilderFactory.AnalyticsPlatformExecutorInfoBuilder;
import org.knime.core.node.workflow.contextv2.ExecutorInfoBuilderFactory.ExecutorInfoUIBuilder;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;
import org.knime.core.util.Pair;

/**
 * Provides information about an Analytics Platform executor.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class AnalyticsPlatformExecutorInfo extends ExecutorInfo {

    /**
     * Mount point under which the workflow is located in the executor, {@code null} for workflows that were loaded
     * directly from another disk location (e.g., batch execution mode).
     *
     * The first entry identifies the mount point. The mount point id is the authority of the URI. For example
     * {@code knime://LOCAL} or {@code knime://Teamspace}.
     *
     * The second entry is an absolute path that locates the mount point root within the host machines file system.
     */
    private final Pair<URI, Path> m_mountpoint;

    private final boolean m_isBatchMode;

    AnalyticsPlatformExecutorInfo( //
            final String userId, //
            final Path workflowPath, //
            final Path tempFolder, //
            final Pair<URI, Path> mountpoint, //
            final boolean isBatchMode) {
        super(ExecutorType.ANALYTICS_PLATFORM, userId, workflowPath, tempFolder);
        m_mountpoint = mountpoint;
        m_isBatchMode = isBatchMode;
    }

    /**
     * The mountpoint under which the workflow is mounted in the AP, represented as a pair of the absolute KNIME URI
     * referencing the root of the mountpoint (e.g. {@code knime://My-Mountpoint}) and the root path of the content
     * provider under which the workflow is mounted in the AP.
     *
     * @return the mountpoint if present, {@link Optional#empty()} otherwise
     */
    public Optional<Pair<URI, Path>> getMountpoint() {
        return Optional.ofNullable(m_mountpoint);
    }

    /**
     * @return true, if the Analytics Platform running the current workflow was started in (headless) batch mode.
     */
    public boolean isBatchMode() {
        return m_isBatchMode;
    }

    @Override
    public boolean isHeadless() {
        return isBatchMode();
    }

    @Override
    void addFields(final StringBuilder sb, final int indent) {
        super.addFields(sb, indent);
        final var init = "  ".repeat(indent);
        sb.append(init).append("mountpoint=").append(m_mountpoint).append("\n");
        sb.append(init).append("isBatchMode=").append(m_isBatchMode).append("\n");
    }

    /**
     * Creates a builder for {@link AnalyticsPlatformExecutorInfo} instances.
     *
     * @return new builder
     */
    public static ExecutorInfoUIBuilder<AnalyticsPlatformExecutorInfoBuilder> builder() {
        return AnalyticsPlatformExecutorInfoBuilderFactory.create();
    }
}
