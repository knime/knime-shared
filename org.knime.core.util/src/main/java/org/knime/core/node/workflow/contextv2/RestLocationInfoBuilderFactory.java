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
 *   Sep 26, 2022 (leonard.woerteler): created
 */
package org.knime.core.node.workflow.contextv2;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.auth.Authenticator;

/**
 * Factory of fluent builders for all {@link RestLocationInfo} types.
 *
 * @param <B> type of the rest of the builder chain for a specific location type
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class RestLocationInfoBuilderFactory<B> {

    RestLocationInfoBuilderFactory() {
    }

    /**
     * Creates a new builder instance.
     *
     * @return new builder
     */
    public RestLocationInfoReqRABuilder<B> newInstance() {
        return new RestLocationInfoReqRABuilder<>((dmiBuilder, defaultMountId) -> {
            final var aBuilder = dmiBuilder.m_aBuilder;
            final var wpBuilder = aBuilder.m_wpBuilder;
            return createRestBuilder(wpBuilder.m_repositoryAddress, aBuilder.m_workflowPath, dmiBuilder.m_authenticator,
                defaultMountId);
        });
    }

    /**
     * Builder accepting the repository address.
     *
     * @param <B> type of the rest of the builder chain
     */
    public static class RestLocationInfoReqRABuilder<B> {

        final BiFunction<RestLocationInfoReqDMIBuilder<B>, String, B> m_continuation;

        RestLocationInfoReqRABuilder(final BiFunction<RestLocationInfoReqDMIBuilder<B>, String, B> continuation) {
            m_continuation = continuation;
        }

        /**
         * Sets the {@link URI} of the repository where the current workflow resides. e.g.
         * <code>https://server.mycompany.com/knime/rest/v4/repository</code>.
         *
         * @param repositoryAddress {@link URI} of the repository where the current workflow resides.
         * @return rest of the builder chain
         */
        public RestLocationInfoReqWPBuilder<B> withRepositoryAddress(final URI repositoryAddress) {
            CheckUtils.checkArgumentNotNull(repositoryAddress, "Repository address must not be null");
            return new RestLocationInfoReqWPBuilder<>(this, repositoryAddress);
        }
    }

    /**
     * Builder accepting the workflow path.
     *
     * @param <B> type of the rest of the builder chain
     */
    public static class RestLocationInfoReqWPBuilder<B> {

        final RestLocationInfoReqRABuilder<B> m_raBuilder;
        final URI m_repositoryAddress;

        RestLocationInfoReqWPBuilder(final RestLocationInfoReqRABuilder<B> raBuilder,
                final URI repositoryAddress) {
            m_raBuilder = raBuilder;
            m_repositoryAddress = repositoryAddress;
        }

        /**
         * Sets the path of the current workflow within the file tree of the repository, e.g. /Users/bob/workflow.
         *
         * @param workflowPath Path of the current workflow within the file tree of the repository.
         * @return rest of the builder chain
         */
        public RestLocationInfoReqABuilder<B> withWorkflowPath(final String workflowPath) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(workflowPath), "Workflow path must not be null or blank");
            CheckUtils.checkArgument(workflowPath.startsWith("/"), "Workflow path must have a leading forward slash");
            return new RestLocationInfoReqABuilder<>(this, workflowPath);
        }
    }

    /**
     * Builder accepting the authenticator.
     *
     * @param <B> type of the rest of the builder chain
     */
    public static class RestLocationInfoReqABuilder<B> {

        final RestLocationInfoReqWPBuilder<B> m_wpBuilder;
        final String m_workflowPath;

        RestLocationInfoReqABuilder(final RestLocationInfoReqWPBuilder<B> wpBuilder, final String workflowPath) {
            m_wpBuilder = wpBuilder;
            m_workflowPath = workflowPath;
        }

        /**
         * Sets the {@link Authenticator} for the repository where the current workflow resides.
         *
         * @param authenticator The {@link Authenticator} for the repository.
         * @return rest of the builder chain
         */
        public RestLocationInfoReqDMIBuilder<B> withAuthenticator(final Authenticator authenticator) {
            CheckUtils.checkArgumentNotNull(authenticator, "Authenticator must not be null");
            return new RestLocationInfoReqDMIBuilder<>(this, authenticator);
        }
    }

    /**
     * Builder accepting the default mount ID.
     *
     * @param <B> type of the rest of the builder chain
     */
    public static class RestLocationInfoReqDMIBuilder<B> {

        final RestLocationInfoReqABuilder<B> m_aBuilder;
        final Authenticator m_authenticator;

        RestLocationInfoReqDMIBuilder(final RestLocationInfoReqABuilder<B> aBuilder,
                final Authenticator authenticator) {
            m_aBuilder = aBuilder;
            m_authenticator = authenticator;
        }

        /**
         * Sets the default mount ID declared by the REST endpoint, e.g. "My-KNIME-Hub".
         *
         * @param defaultMountId The default mount ID declared by the REST endpoint.
         * @return rest of the builder chain
         */
        public B withDefaultMountId(final String defaultMountId) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(defaultMountId),
                    "Default mount id must not be null or blank.");
            return m_aBuilder.m_wpBuilder.m_raBuilder.m_continuation.apply(this, defaultMountId);
        }
    }

    /**
     * Creates the remaining builder steps for the specific subclass of {@link RestLocationInfo} to build.
     */
    abstract B createRestBuilder(URI repositoryAddress, String workflowPath, Authenticator authenticator,
        String defaultMountId);

    /**
     * Base class for the finishing (optional argument) stage of {@link RestLocationInfo} builders.
     *
     * @param <I> The type of {@link RestLocationInfo} produced.
     */
    public abstract static class RestLocationInfoBuilder<I extends RestLocationInfo> {

        final LocationType m_type;
        final URI m_repositoryAddress;
        final String m_workflowPath;
        final Authenticator m_authenticator;
        final String m_defaultMountId;

        RestLocationInfoBuilder( //
                final LocationType type, //
                final URI repositoryAddress, //
                final String workflowPath, //
                final Authenticator authenticator, //
                final String defaultMountId) {
            m_type = type;
            m_repositoryAddress = repositoryAddress;
            m_workflowPath = workflowPath;
            m_authenticator = authenticator;
            m_defaultMountId = defaultMountId;
        }

        /**
         * Creates the workflow's address in the REST repository by adding its path segments to the repository address.
         *
         * @return address of the workflow in the repository
         */
        URI createWorkflowAddress() {
            try {
                final var uriBuilder = new URIBuilder(m_repositoryAddress, StandardCharsets.UTF_8);
                final var pathSegments = new ArrayList<>(uriBuilder.getPathSegments());
                pathSegments.addAll(new URIBuilder().setPath(m_workflowPath).getPathSegments());
                return uriBuilder.setPathSegments(pathSegments).build().normalize();
            } catch (URISyntaxException ex) {
                throw new IllegalStateException("Failed to create workflow address URI", ex);
            }
        }

        /**
         * Builds a new {@link LocationInfo} instance according to the configuration of this builder.
         *
         * @return new instance
         */
        public abstract I build();
    }
}
