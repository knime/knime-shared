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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.knime.core.node.workflow.contextv2.ExecutorInfoBuilderFactory.ExecutorInfoUIBuilder;
import org.knime.core.node.workflow.contextv2.HubJobExecutorInfoBuilderFactory.HubJobExecutorInfoSBuilder;
import org.knime.core.node.workflow.contextv2.JobExecutorInfoBuilderFactory.JobExecutorInfoJIBuilder;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;

/**
 * Provides information about a KNIME Hub executor.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public class HubJobExecutorInfo extends JobExecutorInfo {

    /**
     * The deployment run mode indicates whether jobs created from a deployment is run as the initiator (e.g. the
     * calling user), or the account application password.
     *
     * @since 6.8
     */
    public enum DeploymentRunMode {

        /**
         * The job created from the deployment is supposed to run as the initiator. E.g. as the user who creates the job
         * from the deployment for data-apps and service deployments. For trigger and schedule deployments the caller is
         * the user who created the deployment.
         */
        INITIATOR,

        /**
         * The job created from the deployment is supposed to run in the account scope, using the account application
         * password.
         */
        ACCOUNT;
    }

    /**
     * The scope under which the current job has been created. The scope is the account (e.g. team) that owns the
     * execution context in which the job runs. This field holds the technical account ID (unique, immutable).
     */
    private final Supplier<String> m_scopeId;

    /**
     * See {@link #m_scopeId}. This field holds the human-readable (unique but mutable) account name.
     */
    private final Supplier<String> m_scopeName;

    /**
     * The human-readable (unique but mutable) name of the account that created the current job. See
     * {@link #getJobCreatorId()} for the technical account ID.
     */
    private final String m_jobCreatorName;

    /**
     * The deployment ID, may be {@code null}.
     */
    private final String m_deploymentId;

    /**
     * The deployment run mode, may be {@code null}.
     */
    private final DeploymentRunMode m_deploymentRunMode;

    HubJobExecutorInfo( // NOSONAR only called internally
            final UUID jobId, //
            final Path workflowPath, //
            final Path tempFolder, //
            final boolean isRemote, //
            final String localMountId, //
            final String remoteExecutorVersion, //
            final Supplier<String> scope, //
            final Supplier<String> scopeName, //
            final String jobCreatorId, //
            final String jobCreatorName, //
            final String deploymentId, //
            final DeploymentRunMode deploymentRunMode) {
        super(ExecutorType.HUB_EXECUTOR, jobCreatorId, workflowPath, tempFolder, jobId, isRemote, localMountId, //
            remoteExecutorVersion);
        m_scopeId = scope;
        m_scopeName = scopeName;
        m_jobCreatorName = jobCreatorName;
        m_deploymentId = deploymentId;
        m_deploymentRunMode = deploymentRunMode;
    }

    /**
     * Provides the scope under which the current job has been created. The scope is the account (e.g. team) that owns
     * the execution context in which the job runs. This method returns the technical account ID (unique, immutable),
     * for example "team:f202a301-5fda-4763-afa6-85a2c0bf8a37".
     *
     * @return the scope as a technical account ID.
     */
    public String getScopeId() {
        return m_scopeId.get();
    }

    /**
     * Provides the scope under which the current job has been created. The scope is the account (e.g. team) that owns
     * the execution context in which the job runs. This method returns the human-readable account name (unique but
     * mutable), for example "marketing".
     *
     * @return the scopeName
     */
    public String getScopeName() {
        return m_scopeName.get();
    }

    /**
     * Provides the technical account ID (unique and immutable) of the account that created the current job, e.g.
     * "user:f192a301-5fda-4763-afa6-85a2c0bf8ae1". Same as {@link #getUserId()}.
     *
     * @return the technical account ID of the account that created the current job
     */
    public String getJobCreatorId() {
        return getUserId();
    }

    /**
     * Provides the human-readable account name (unique but mutable) of the account that created the current job, e.g.
     * "user:f192a301-5fda-4763-afa6-85a2c0bf8ae1".
     *
     * @return the technical account ID of the account that created the current job.
     */
    public String getJobCreatorName() {
        return m_jobCreatorName;
    }

    /**
     * Provides the ID of the deployment this job is run in the context of, if applicable.
     *
     * @return deployment ID
     * @since 6.8
     */
    public Optional<String> getDeploymentId() {
        return Optional.ofNullable(m_deploymentId);
    }

    /**
     * Provides the deployment run mode, indicating whether jobs created from a deployment is run as the initiator
     * (e.g. the calling user) or as the account application password.
     *
     * @return deployment run mode
     * @since 6.8
     */
    public Optional<DeploymentRunMode> getDeploymentRunMode() {
        return Optional.ofNullable(m_deploymentRunMode);
    }

    /**
     * Creates a builder for {@link HubJobExecutorInfo} instances.
     *
     * @return new builder
     */
    public static ExecutorInfoUIBuilder<JobExecutorInfoJIBuilder<HubJobExecutorInfoSBuilder>> builder() {
        return HubJobExecutorInfoBuilderFactory.create();
    }

    @Override
    void addFields(final StringBuilder sb, final int indent) {
        super.addFields(sb, indent);
        final var init = "  ".repeat(indent);
        sb.append(init).append("scopeId=").append(m_scopeId).append("\n");
        sb.append(init).append("scopeName=").append(m_scopeName).append("\n");
        sb.append(init).append("jobCreatorName=").append(m_jobCreatorName).append("\n");
        sb.append(init).append("deploymentId=").append(m_deploymentId).append("\n");
        sb.append(init).append("deploymentRunMode=").append(m_deploymentRunMode).append("\n");
    }
}
