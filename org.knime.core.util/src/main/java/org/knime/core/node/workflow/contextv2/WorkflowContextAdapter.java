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
 *   Sep 20, 2022 (bjoern): created
 */
package org.knime.core.node.workflow.contextv2;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;
import org.knime.core.util.Pair;
import org.knime.core.util.auth.Authenticator;

/**
 * Adapter class that wraps a {@link WorkflowContextV2} using the deprecated {@link WorkflowContext} interface.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @deprecated Use {@link WorkflowContextV2} instead.
 */
@Deprecated(since = "4.7.0")
class WorkflowContextAdapter implements WorkflowContext {

    private final WorkflowContextV2 m_context;

    WorkflowContextAdapter(final WorkflowContextV2 context) {
        m_context = context;
    }

    WorkflowContextV2 unwrap() {
        return m_context;
    }

    @Override
    public String getUserid() {
        return m_context.getExecutorInfo().getUserId();
    }

    @Override
    public File getCurrentLocation() {
        return m_context.getExecutorInfo().getLocalWorkflowPath().toFile();
    }

    @Override
    public File getOriginalLocation() {
        return null;
    }

    @Override
    public File getTempLocation() {
        return m_context.getExecutorInfo().getTempFolder().toFile();
    }

    @Override
    public File getMountpointRoot() {
        final var executorInfo = m_context.getExecutorInfo();
        if (executorInfo instanceof AnalyticsPlatformExecutorInfo) {
            return ((AnalyticsPlatformExecutorInfo) executorInfo).getMountpoint()
                    .map(Pair::getSecond)
                    .map(Path::toFile)
                    .orElse(null);
        }
        return null;
    }

    @Override
    public Optional<URI> getMountpointURI() {
        return m_context.getMountpointURI();
    }

    @Override
    public Optional<URI> getRemoteRepositoryAddress() {
        if (m_context.getExecutorType() != ExecutorType.ANALYTICS_PLATFORM) {
            return Optional.of(((RestLocationInfo)m_context.getLocationInfo()).getRepositoryAddress());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getRelativeRemotePath() {
        if (m_context.getExecutorType() != ExecutorType.ANALYTICS_PLATFORM) {
            return Optional.of(((RestLocationInfo)m_context.getLocationInfo()).getWorkflowPath());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Authenticator> getServerAuthenticator() {
        if (m_context.getExecutorType() != ExecutorType.ANALYTICS_PLATFORM) {
            return Optional.of(((RestLocationInfo)m_context.getLocationInfo()).getAuthenticator());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getRemoteMountId() {
        if (m_context.getExecutorType() != ExecutorType.ANALYTICS_PLATFORM) {
            return Optional.of(((RestLocationInfo)m_context.getLocationInfo()).getDefaultMountId());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UUID> getJobId() {
        if (m_context.getExecutorType() != ExecutorType.ANALYTICS_PLATFORM) {
            return Optional.of(((JobExecutorInfo)m_context.getExecutorInfo()).getJobId());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isTemporaryCopy() {
        return m_context.isTemporyWorkflowCopyMode();
    }
}
