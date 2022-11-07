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

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.util.ClassUtils;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.contextv2.AnalyticsPlatformExecutorInfoBuilderFactory.AnalyticsPlatformExecutorInfoBuilder;
import org.knime.core.node.workflow.contextv2.ExecutorInfoBuilderFactory.ExecutorInfoUIBuilder;
import org.knime.core.node.workflow.contextv2.HubJobExecutorInfoBuilderFactory.HubJobExecutorInfoBuilder;
import org.knime.core.node.workflow.contextv2.HubJobExecutorInfoBuilderFactory.HubJobExecutorInfoSBuilder;
import org.knime.core.node.workflow.contextv2.HubSpaceLocationInfoBuilderFactory.HubSpaceLocationInfoBuilder;
import org.knime.core.node.workflow.contextv2.HubSpaceLocationInfoBuilderFactory.HubSpaceLocationInfoSpaceBuilder;
import org.knime.core.node.workflow.contextv2.JobExecutorInfoBuilderFactory.JobExecutorInfoJIBuilder;
import org.knime.core.node.workflow.contextv2.RestLocationInfoBuilderFactory.RestLocationInfoReqRABuilder;
import org.knime.core.node.workflow.contextv2.ServerJobExecutorInfoBuilderFactory.ServerJobExecutorInfoBuilder;
import org.knime.core.node.workflow.contextv2.ServerLocationInfoBuilderFactory.ServerLocationInfoBuilder;
import org.knime.core.util.URIPathEncoder;

/**
 * Workflow context class that provides information about the KNIME process that executes the current workflow (see
 * {@link #getExecutorInfo()}, as well as the storage location where the current workflow resides (see
 * {@link #getLocationInfo()}).
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public final class WorkflowContextV2 {

    /**
     * Enum that describes the type of executor running the current workflow.
     */
    public enum ExecutorType {
        /**
         * KNIME Analytics Platform (desktop application).
         */
        ANALYTICS_PLATFORM,

        /**
         * KNIME Server Executor.
         */
        SERVER_EXECUTOR,

        /**
         * KNIME Hub Executor.
         */
        HUB_EXECUTOR
    }

    /**
     * Enum that describes the type of location where the current workflow resides/is stored.
     */
    public enum LocationType {
        /**
         * On the Analytics Platform's local file system.
         */
        LOCAL,

        /**
         * A KNIME Server repository (access through REST-based).
         */
        SERVER_REPOSITORY,

        /**
         * A Space in a KNIME Hub instance.
         */
        HUB_SPACE
    }

    /**
     * Creates a fluent builder for a {@link WorkflowContextV2} instance.
     *
     * @return the new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    private final URI m_mountpointURI;

    private final ExecutorInfo m_executorInfo;

    private final LocationInfo m_locationInfo;

    /**
     * Constructor.
     *
     * @param executorInfo Provides information about the process that runs the current workflow.
     * @param locationInfo Provides information about where the current workflow currently resides (is stored).
     */
    private WorkflowContextV2(final ExecutorInfo executorInfo, final LocationInfo locationInfo) {
        super();
        m_mountpointURI = ClassUtils.castOptional(AnalyticsPlatformExecutorInfo.class, executorInfo)
            .flatMap(apInfo -> apInfo
                .getMountpoint()
                .flatMap(mountpoint -> locationInfo.mountpointURI(mountpoint, apInfo.getLocalWorkflowPath())))
            .orElse(null);
        m_executorInfo = executorInfo;
        m_locationInfo = locationInfo;
    }

    /**
     * @return the type of the executor running the current workflow.
     */
    public ExecutorType getExecutorType() {
        return m_executorInfo.getType();
    }

    /**
     * @return an {@link ExecutorInfo} that provides information about the executor running the current workflow.
     */
    public ExecutorInfo getExecutorInfo() {
        return m_executorInfo;
    }

    /**
     * Returns the type of location where the current workflow is stored/resides. Depending on the returned value you
     * will be able to cast the result of {@link #getLocationInfo()} to a proper subclass.
     *
     * @return the type of location where the current workflow is stored.
     */
    public LocationType getLocationType() {
        return m_locationInfo.getType();
    }

    /**
     * Returns information about where the current workflow is stored/resides. Depending on its {@link LocationType},
     * you will be able to cast the returned value to a proper subclass.
     *
     * @return a {@link LocationInfo} describing the location where the current workflow is stored.
     */
    public LocationInfo getLocationInfo() {
        return m_locationInfo;
    }

    /**
     * The "temporary workflow copy mode" (aka yellow bar editor) is active when the workflow is executed in AP but
     * actually lives in a remote repository or a compressed archive.
     * In this case, AP is just executing a temporary copy of the workflow.
     *
     * @return true, if "temporary workflow copy mode" (aka yellow bar editor) is active, false otherwise.
     */
    public boolean isTemporyWorkflowCopyMode() {
        return getTempSourceLocation().isPresent();
    }

    /**
     *
     * A mountpoint-absolute knime:// URI that points to where the workflow resides from the perspective of the
     * executor's mount table, e.g. knime://LOCAL/Users/bob/myworkflow, or knime://devserver1/Users/bob/myworkflow.
     *
     * @return a mountpoint-absolute knime:// URI that points to where the workflow resides from the perspective of the
     *         executor's mount table.
     */
    public Optional<URI> getMountpointURI() {
        return Optional.ofNullable(m_mountpointURI);
    }

    /**
     * Returns the source URI (either {@code file://} or {@code knime://}) of the workflow if it is being opened in
     * "temporary workflow copy mode" (aka yellow bar editor), and {@link Optional#empty()} otherwise.
     * This is the case when the workflow originates either from a server repository or from a compressed archive
     * ({@code .knwf}) and  temporary copy is opened in the Analytics platform.
     *
     * @return original location URI if opened in temp-copy mode, {@link Optional#empty()} otherwise
     */
    public Optional<URI> getTempSourceLocation() {
        if (getExecutorType() != ExecutorType.ANALYTICS_PLATFORM) {
            // temporary copies only exist in the AP
            return Optional.empty();
        }
        if (m_locationInfo instanceof RestLocationInfo) {
            // return the mountpoint URI (which must exist) for REST sources
            return Optional.of(m_mountpointURI);
        }
        return ((LocalLocationInfo) m_locationInfo)
                .getSourceArchive()
                .map(Path::toUri)
                .map(URIPathEncoder.UTF_8::encodePathSegments);
    }

    /**
     * Creates a context based on a temporary workflow run within a local KNIME AP. That is, execution type is
     * {@link ExecutorType#ANALYTICS_PLATFORM} and location is {@link LocationType#LOCAL}.
     *
     * @param workflowFolderPath The non-null folder path where the workflow is (or will be) located
     * @param sourceArchive path to the archive the workflow came from ({@code null} if not applicable).
     *        Non-null only for "yellow bar" editors.
     * @return A new context.
     */
    public static WorkflowContextV2 forTemporaryWorkflow(final Path workflowFolderPath,
            final Path sourceArchive) {
        return WorkflowContextV2.builder()
                .withAnalyticsPlatformExecutor(exec -> exec
                    .withCurrentUserAsUserId()
                    .withLocalWorkflowPath(workflowFolderPath))
                .withArchiveLocation(sourceArchive)
                .build();
    }

    /**
     * Temporary method to convert legacy {@link WorkflowContext} into a {@link WorkflowContextV2}. Only works for local
     * AP execution (not temp workflow copy) and Server execution.
     *
     * @param legacyContext
     * @return a {@link WorkflowContextV2} instance.
     * @deprecated The old workflow context should not be used any more.
     */
    @Deprecated(since = "4.7.0")
    public static WorkflowContextV2 fromLegacyWorkflowContext(final WorkflowContext legacyContext) {

        if (legacyContext instanceof WorkflowContextAdapter) {
            // transition logic
            return ((WorkflowContextAdapter) legacyContext).unwrap();
        }

        ExecutorInfo execInfo;
        if (legacyContext.getRemoteRepositoryAddress().isEmpty()) {
            final var builder = AnalyticsPlatformExecutorInfo.builder()
                .withUserId(legacyContext.getUserid())
                .withLocalWorkflowPath(legacyContext.getCurrentLocation().toPath());
            final var optURI = legacyContext.getMountpointURI();
            if (optURI.isPresent()) {
                builder.withMountpoint(optURI.get().getAuthority(), legacyContext.getMountpointRoot().toPath());
            }
            execInfo = builder.build();
        } else {
            execInfo = ServerJobExecutorInfo.builder()
                .withUserId(legacyContext.getUserid())
                .withLocalWorkflowPath(legacyContext.getCurrentLocation().toPath())
                .withJobId(legacyContext.getJobId().orElseThrow())
                .build();
        }

        LocationInfo locInfo;
        if (execInfo.getType() == ExecutorType.ANALYTICS_PLATFORM && !legacyContext.isTemporaryCopy()) {
            locInfo = LocalLocationInfo.getInstance(null);
        } else if (execInfo.getType() == ExecutorType.SERVER_EXECUTOR) {
            var mountId = legacyContext.getRemoteMountId().orElseThrow();
            var relativeRemotePath = legacyContext.getRelativeRemotePath().orElseThrow();
            locInfo = ServerLocationInfo.builder()
                .withRepositoryAddress(legacyContext.getRemoteRepositoryAddress().orElseThrow())
                .withWorkflowPath(relativeRemotePath)
                .withAuthenticator(legacyContext.getServerAuthenticator().orElseThrow())
                .withDefaultMountId(mountId)
                .build();
        } else {
            throw new UnsupportedOperationException("WorkflowContextV2 is not yet supported for current combination "
                + "of executor and workflow storage locality");
        }

        return new WorkflowContextV2(execInfo, locInfo);
    }

    /**
     * Only intended for the transition period from {@link WorkflowContext} to {@link WorkflowContextV2}!
     *
     * @return a legacy {@link WorkflowContext} that is based on the values in this {@link WorkflowContextV2}.
     * @deprecated The old workflow context should not be used any more.
     */
    @Deprecated(since = "4.7.0")
    public WorkflowContext toLegacyWorkflowContext() {
        return new WorkflowContextAdapter(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append("[\n");
        sb.append("  mountpointURI=").append(m_mountpointURI).append("\n");
        m_executorInfo.toString(sb, 1);
        m_locationInfo.toString(sb, 1);
        return sb.append("]").toString();
    }

    /**
     * Builder for {@link WorkflowContextV2} instances.
     */
    public static final class Builder {

        private ExecutorInfo m_executorInfo;

        private LocationInfo m_locationInfo;

        /** Should only be called from {@link #builder()}. */
        private Builder() {
            // nothing to do
        }

        /**
         * Sets the given {@link ExecutorInfo}.
         *
         * @param executorInfo executor info to use
         * @return this builder
         */
        public Builder withExecutor(final ExecutorInfo executorInfo) {
            m_executorInfo = executorInfo;
            return this;
        }

        /**
         * Provides a builder to configure a {@link AnalyticsPlatformExecutorInfo} to set in this builder.
         *
         * @param callee callback to configure the executor info (preferably with a lambda function)
         * @return this builder
         */
        public Builder withAnalyticsPlatformExecutor(final Function<ExecutorInfoUIBuilder<
                AnalyticsPlatformExecutorInfoBuilder>, AnalyticsPlatformExecutorInfoBuilder> callee) {
            return withExecutor(callee.apply(AnalyticsPlatformExecutorInfo.builder()).build());
        }

        /**
         * Provides a builder to configure a {@link ServerJobExecutorInfo} to set in this builder.
         *
         * @param callee callback to configure the executor info (preferably with a lambda function)
         * @return this builder
         */
        public Builder withServerJobExecutor(final Function<ExecutorInfoUIBuilder<JobExecutorInfoJIBuilder<
                ServerJobExecutorInfoBuilder>>, ServerJobExecutorInfoBuilder> callee) {
            return withExecutor(callee.apply(ServerJobExecutorInfo.builder()).build());
        }

        /**
         * Provides a builder to configure a {@link HubJobExecutorInfo} to set in this builder.
         *
         * @param callee callback to configure the executor info (preferably with a lambda function)
         * @return this builder
         */
        public Builder withHubJobExecutor(final Function<ExecutorInfoUIBuilder<JobExecutorInfoJIBuilder<
                HubJobExecutorInfoSBuilder>>, HubJobExecutorInfoBuilder> callee) {
            return withExecutor(callee.apply(HubJobExecutorInfo.builder()).build());
        }

        /**
         * Sets the given {@link LocationInfo}.
         *
         * @param locationInfo location info to use
         * @return this builder
         */
        public Builder withLocation(final LocationInfo locationInfo) {
            m_locationInfo = locationInfo;
            return this;
        }

        /**
         * Sets a {@link LocalLocationInfo} without source archive.
         *
         * @return this builder
         */
        public Builder withLocalLocation() {
            return withLocation(LocalLocationInfo.getInstance(null));
        }

        /**
         * Sets a {@link LocalLocationInfo} with a path to an archive the workflow came from.
         *
         * @param sourceLocation location info of the archive this workflow was extracted from
         * @return this builder
         */
        public Builder withArchiveLocation(final Path sourceLocation) {
            return withLocation(LocalLocationInfo.getInstance(sourceLocation));
        }

        /**
         * Provides a builder to configure a {@link ServerLocationInfo} to set in this builder.
         *
         * @param callee callback to configure the executor info (preferably with a lambda function)
         * @return this builder
         */
        public Builder withServerLocation(
            final Function<RestLocationInfoReqRABuilder<ServerLocationInfoBuilder>, ServerLocationInfoBuilder> callee) {
            return withLocation(callee.apply(ServerLocationInfo.builder()).build());
        }

        /**
         * Provides a builder to configure a {@link AnalyticsPlatformExecutorInfo} to set in this builder.
         *
         * @param callee callback to configure the executor info (preferably with a lambda function)
         * @return this builder
         */
        public Builder withHubSpaceLocation(
                final Function<RestLocationInfoReqRABuilder<HubSpaceLocationInfoSpaceBuilder>,
                        HubSpaceLocationInfoBuilder> callee) {
            return withLocation(callee.apply(HubSpaceLocationInfo.builder()).build());
        }

        /**
         * Builds a {@link WorkflowContextV2} from the executor info and location info configured in this builder.
         * None of the two components may be {@code null}.
         *
         * @return new {@link WorkflowContextV2} instance
         * @throws IllegalStateException if either one of the two infos is {@code null}
         */
        public WorkflowContextV2 build() {
            CheckUtils.checkState(m_executorInfo != null, "Executor info can't be null.");
            CheckUtils.checkState(m_locationInfo != null, "Location info can't be null.");
            return new WorkflowContextV2(m_executorInfo, m_locationInfo);
        }
    }
}
