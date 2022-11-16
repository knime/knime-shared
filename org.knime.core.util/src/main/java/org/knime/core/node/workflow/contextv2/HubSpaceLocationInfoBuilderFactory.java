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

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.HubSpaceLocationInfoBuilderFactory.HubSpaceLocationInfoSpaceBuilder;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.auth.Authenticator;

/**
 * Factory for fluent builders for {@link HubSpaceLocationInfo}.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public final class HubSpaceLocationInfoBuilderFactory
        extends RestLocationInfoBuilderFactory<HubSpaceLocationInfoSpaceBuilder> {

    /** Singleton factory instance. */
    private static final HubSpaceLocationInfoBuilderFactory FACTORY = new HubSpaceLocationInfoBuilderFactory();

    /**
     * Creates a new builder for {@link HubSpaceLocationInfo} instances.
     *
     * @return new builder
     */
    public static RestLocationInfoReqRABuilder<HubSpaceLocationInfoSpaceBuilder> create() {
        return FACTORY.newInstance();
    }

    private HubSpaceLocationInfoBuilderFactory() {
    }

    @Override
    HubSpaceLocationInfoSpaceBuilder createRestBuilder(final URI repositoryAddress, final String workflowPath,
            final Authenticator authenticator, final String defaultMountId) {
        return new HubSpaceLocationInfoSpaceBuilder((space, workflowItemId) ->
            new HubSpaceLocationInfoBuilder( //
                repositoryAddress, //
                workflowPath, //
                authenticator, //
                defaultMountId, //
                space[0], space[1], space[2], //
                workflowItemId));
    }

    /** Builder accepting information related to the Hub Space where the current workflow is stored. */
    public static final class HubSpaceLocationInfoSpaceBuilder {

        final BiFunction<String[], String, HubSpaceLocationInfoBuilder> m_continuation;

        HubSpaceLocationInfoSpaceBuilder(final BiFunction<String[], String, HubSpaceLocationInfoBuilder> continuation) {
            m_continuation = continuation;
        }

        /**
         * Sets information related to the Hub Space where the current workflow is stored.
         *
         * @param spacePath The path of the Hub Space, e.g. /Users/bob/Private.
         * @param spaceItemId The item ID of the Hub Space where the current workflow is stored, e.g. *3hjkfsd7zdsgf
         *            (including leading asterisk).
         * @param spaceVersion The version of the Hub Space where the current workflow is stored. May be null to
         *            indicate the staging version (latest version plus unversioned changes on top).
         * @return this builder instance.
         */
        public HubSpaceLocationInfoIDBuilder withSpace(final String spacePath, final String spaceItemId,
                final String spaceVersion) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(spacePath), "Space path must not be null or blank");
            CheckUtils.checkArgument(spacePath.startsWith("/"), "Space path must start with a leading forward slash");

            CheckUtils.checkArgument(StringUtils.isNotBlank(spaceItemId), "Space item id must not be null or blank");
            CheckUtils.checkArgument(spaceItemId.startsWith("*"), "Space item id must start with a leading asterisk");

            if (spaceVersion != null) {
                CheckUtils.checkArgument(StringUtils.isNotBlank(spaceItemId), "Space version must not be blank.");
            }
            return new HubSpaceLocationInfoIDBuilder(workflowItemId ->
                    m_continuation.apply(new String[] { spacePath, spaceItemId, spaceVersion }, workflowItemId));
        }
    }

    /** Builder accepting the item ID of the current workflow. */
    public static final class HubSpaceLocationInfoIDBuilder {

        final Function<String, HubSpaceLocationInfoBuilder> m_continuation;

        HubSpaceLocationInfoIDBuilder(final Function<String, HubSpaceLocationInfoBuilder> continuation) {
            m_continuation = continuation;
        }

        /**
         * Sets the item ID of the current workflow.
         *
         * @param workflowItemId The item ID of the current workflow, e.g. *2zwDuYFgpLVveXfX (including leading
         *            asterisk).
         * @return this builder instance.
         */
        public HubSpaceLocationInfoBuilder withWorkflowItemId(final String workflowItemId) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(workflowItemId),
                    "Workflow item id must not be null or blank");
            CheckUtils.checkArgument(workflowItemId.startsWith("*"),
                "Workflow item id must start with a leading asterisk");
            return m_continuation.apply(workflowItemId);
        }
    }

    /**
     * Finishing stage of the {@link ServerLocationInfo} builder.
     */
    public static class HubSpaceLocationInfoBuilder
            extends RestLocationInfoBuilderFactory.RestLocationInfoBuilder<HubSpaceLocationInfo> {

        private final String m_spacePath;
        private final String m_spaceItemId;
        private final String m_spaceVersion;
        private final String m_workflowItemId;

        HubSpaceLocationInfoBuilder( // NOSONAR only called internally
                final URI repositoryAddress, //
                final String workflowPath, //
                final Authenticator authenticator, //
                final String defaultMountId, //
                final String spacePath, //
                final String spaceItemId, //
                final String spaceVersion, //
                final String workflowItemId) {
            super(LocationType.HUB_SPACE, repositoryAddress, workflowPath, authenticator, defaultMountId);
            m_spacePath = spacePath;
            m_spaceItemId = spaceItemId;
            m_spaceVersion = spaceVersion;
            m_workflowItemId = workflowItemId;
        }

        @Override
        public HubSpaceLocationInfo build() {
            return new HubSpaceLocationInfo(
                m_repositoryAddress, //
                m_authenticator, //
                m_workflowPath, //
                m_defaultMountId, //
                createWorkflowAddress(), //
                m_spacePath, //
                m_spaceItemId, //
                m_spaceVersion, //
                m_workflowItemId);
        }
    }
}
