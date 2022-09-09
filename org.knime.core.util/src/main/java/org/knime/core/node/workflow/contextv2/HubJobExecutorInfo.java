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

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;

/**
 * Provides information about a KNIME Hub executor.
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class HubJobExecutorInfo extends JobExecutorInfo {

    /**
     * The scope under which the current job has been created. The scope is the account (e.g. team) that owns the
     * execution context in which the job runs. This field holds the technical account ID (unique, immutable).
     */
    private final String m_scopeId;

    /**
     * See {@link #m_scopeId}. This field holds the human-readable (unique but mutable) account name.
     */
    private final String m_scopeName;

    /**
     * The human-readable (unique but mutable) name of the account that created the current job. See
     * {@link #getJobCreatorId()} for the technical account ID.
     */
    private final String m_jobCreatorName;

    HubJobExecutorInfo(final UUID jobId, final boolean isRemote, final String scope, final String scopeName,
        final String jobCreatorId, final String jobCreatorName) {
        super(ExecutorType.HUB_EXECUTOR, jobCreatorId, jobId, isRemote);
        m_scopeId = scope;
        m_scopeName = scopeName;
        m_jobCreatorName = jobCreatorName;
    }

    /**
     * Provides the scope under which the current job has been created. The scope is the account (e.g. team) that owns
     * the execution context in which the job runs. This method returns the technical account ID (unique, immutable),
     * for example "team:f202a301-5fda-4763-afa6-85a2c0bf8a37".
     *
     * @return the scope as a technical account ID.
     */
    public String getScopeId() {
        return m_scopeId;
    }

    /**
     * Provides the scope under which the current job has been created. The scope is the account (e.g. team) that owns
     * the execution context in which the job runs. This method returns the human-readable account name (unique but
     * mutable), for example "marketing".
     *
     * @return the scopeName
     */
    public String getScopeName() {
        return m_scopeName;
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
     * Please see {@link #getJobCreatorId()}.
     */
    @Override
    public String getUserId() {
        return getJobCreatorId();
    }

    /**
     * Builder class for {@link HubJobExecutorInfo}
     */
    public static final class Builder extends JobExecutorInfo.Builder<Builder, HubJobExecutorInfo> {

        /**
         * See {@link HubJobExecutorInfo#getScopeId()}.
         */
        private String m_scopeId;

        /**
         * See {@link HubJobExecutorInfo#getScopeName()}.
         */
        private String m_scopeName;

        /**
         * See {@link HubJobExecutorInfo#getJobCreatorName()}.
         */
        private String m_jobCreatorName;

        /**
         * Constructor.
         */
        public Builder() {
            super(ExecutorType.HUB_EXECUTOR);
        }

        /**
         * Sets the scope, which is the account (e.g. team) that owns the execution context in which the job runs.
         *
         * @param scopeId The technical account ID, e.g.g "user:f192a301-5fda-4763-afa6-85a2c0bf8ae1"
         * @param scopeName The human-readable account name, e.g. "bob.miller"
         *
         * @return this builder instance
         * @see HubJobExecutorInfo#getScopeId()
         */
        public Builder withScope(final String scopeId, final String scopeName) {
            m_scopeId = scopeId;
            m_scopeName = scopeName;
            return this;
        }

        /**
         * Sets the job creator, which is the account (e.g. user) that that created the current job.
         *
         * @param jobCreatorId The technical account ID, e.g. "user:f192a301-5fda-4763-afa6-85a2c0bf8ae1".
         * @param jobCreatorName The human-readable account name, e.g. "bob.miller".
         *
         * @return this builder instance
         * @see HubJobExecutorInfo#getJobCreatorId()
         */
        public Builder withJobCreator(final String jobCreatorId, final String jobCreatorName) {
            withUserId(jobCreatorId);
            m_jobCreatorName = jobCreatorName;
            return this;
        }

        @Override
        public HubJobExecutorInfo build() {
            checkFields();
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_scopeId), "Scope ID must not be null or blank");
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_scopeName), "Scope name must not be null or blank");
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_userId), "Job creator ID must not be null or blank");
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_jobCreatorName),
                "Job creator name must not be null or blank");

            return new HubJobExecutorInfo(m_jobId, //
                m_isRemote, //
                m_scopeId, //
                m_scopeName, //
                m_userId, m_jobCreatorName);
        }
    }
}
