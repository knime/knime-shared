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
 *   Sep 27, 2022 (leonard.woerteler): created
 */
package org.knime.core.node.workflow.contextv2;

import java.nio.file.Path;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.HubJobExecutorInfoBuilderFactory.HubJobExecutorInfoSBuilder;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.ExecutorType;

/**
 * Factory for fluent builders for {@link HubJobExecutorInfo}.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public final class HubJobExecutorInfoBuilderFactory extends JobExecutorInfoBuilderFactory<HubJobExecutorInfoSBuilder> {

    /** Singleton factory instance. */
    private static final HubJobExecutorInfoBuilderFactory FACTORY =
            new HubJobExecutorInfoBuilderFactory();

    /**
     * Creates a new builder for {@link HubJobExecutorInfo} instances.
     *
     * @return new builder
     */
    public static ExecutorInfoUIBuilder<JobExecutorInfoJIBuilder<HubJobExecutorInfoSBuilder>> create() {
        return FACTORY.newInstance();
    }

    private HubJobExecutorInfoBuilderFactory() {
    }

    @Override
    HubJobExecutorInfoSBuilder createJobBuilder(final String userId, final Path localWorkflowPath, final UUID jobId) {
        return new HubJobExecutorInfoSBuilder(
            (scope, creatorId) ->
                new HubJobExecutorInfoBuilder(userId, localWorkflowPath, jobId, scope[0], scope[1], creatorId));
    }

    /** Builder accepting the execution scope of the job. */
    public static final class HubJobExecutorInfoSBuilder {

        final BiFunction<Supplier<String>[], String, HubJobExecutorInfoBuilder> m_continuation;

        HubJobExecutorInfoSBuilder(
            final BiFunction<Supplier<String>[], String, HubJobExecutorInfoBuilder> continuation) {
            m_continuation = continuation;
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
        @SuppressWarnings("unchecked")
        public HubJobExecutorInfoJCBuilder withScope(final String scopeId, final String scopeName) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(scopeId), "Scope ID must not be null or blank");
            CheckUtils.checkArgument(StringUtils.isNotBlank(scopeName), "Scope name must not be null or blank");
            return new HubJobExecutorInfoJCBuilder((jobCreatorName -> m_continuation
                .apply(new Supplier[]{() -> scopeId, () -> scopeName}, jobCreatorName)));
        }

        /**
         * Sets the scope, which is the account (e.g. team) that owns the execution context in which the job runs.
         *
         * @param scopeId The technical account ID, e.g.g "user:f192a301-5fda-4763-afa6-85a2c0bf8ae1"
         * @param scopeName The human-readable account name, e.g. "bob.miller"
         *
         * @return this builder instance
         * @see HubJobExecutorInfo#getScopeId()
         * @since 6.2
         */
        @SuppressWarnings("unchecked")
        public HubJobExecutorInfoJCBuilder withScope(final Supplier<String> scopeId, final Supplier<String> scopeName) {
            return new HubJobExecutorInfoJCBuilder((jobCreatorName -> m_continuation
                .apply(new Supplier[]{scopeId, scopeName}, jobCreatorName)));
        }
    }

    /** Builder accepting the job creator of the job. */
    public static final class HubJobExecutorInfoJCBuilder {

        final Function<String, HubJobExecutorInfoBuilder> m_continuation;

        HubJobExecutorInfoJCBuilder(final Function<String, HubJobExecutorInfoBuilder> continuation) {
            m_continuation = continuation;
        }

        /**
         * Sets the job creator name, which is the name of the account (e.g. user) that that created the current job.
         * The job creator's ID, the technical account ID (e.g. "user:f192a301-5fda-4763-afa6-85a2c0bf8ae1"), must be
         * set as user ID.
         *
         * @param jobCreatorName The human-readable account name, e.g. "bob.miller".
         *
         * @return this builder instance
         * @see HubJobExecutorInfo#getJobCreatorId()
         */
        public HubJobExecutorInfoBuilder withJobCreator(final String jobCreatorName) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(jobCreatorName),
                "Job creator name must not be null or blank");
            return m_continuation.apply(jobCreatorName);
        }
    }

    /**
     * Finishing stage of the {@link HubJobExecutorInfo} builder.
     */
    public static class HubJobExecutorInfoBuilder
            extends JobExecutorInfoBuilderFactory.JobExecutorInfoBuilder<HubJobExecutorInfo,
                    HubJobExecutorInfoBuilder> {

        /**
         * See {@link HubJobExecutorInfo#getScopeId()}.
         */
        private final Supplier<String> m_scopeId;

        /**
         * See {@link HubJobExecutorInfo#getScopeName()}.
         */
        private final Supplier<String> m_scopeName;

        /**
         * See {@link HubJobExecutorInfo#getJobCreatorName()}.
         */
        private final String m_jobCreatorName;

        HubJobExecutorInfoBuilder( //
                final String userId, //
                final Path localWorkflowPath, //
                final UUID jobId, //
                final Supplier<String> scopeId, //
                final Supplier<String> scopeName, //
                final String jobCreatorName) {
            super(ExecutorType.HUB_EXECUTOR, userId, localWorkflowPath, jobId);
            m_scopeId = scopeId;
            m_scopeName = scopeName;
            m_jobCreatorName = jobCreatorName;
        }

        @Override
        public HubJobExecutorInfo build() {
            return new HubJobExecutorInfo(m_jobId, //
                m_localWorkflowPath, //
                ensureTempFolder(), //
                m_isRemote, //
                m_scopeId, //
                m_scopeName, //
                m_userId, //
                m_jobCreatorName);
        }
    }
}
