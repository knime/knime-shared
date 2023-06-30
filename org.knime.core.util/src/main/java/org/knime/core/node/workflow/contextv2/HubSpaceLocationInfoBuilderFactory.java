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
        return new HubSpaceLocationInfoSpaceBuilder(repositoryAddress, workflowPath, authenticator, defaultMountId);
    }

    /** Builder accepting information related to the Hub Space where the current workflow is stored. */
    public static final class HubSpaceLocationInfoSpaceBuilder {

        private final URI m_repositoryAddress;
        private final String m_workflowPath;
        private final Authenticator m_authenticator;
        private final String m_defaultMountId;

        HubSpaceLocationInfoSpaceBuilder(final URI repositoryAddress, final String workflowPath,
                final Authenticator authenticator, final String defaultMountId) {
            m_repositoryAddress = repositoryAddress;
            m_workflowPath = workflowPath;
            m_authenticator = authenticator;
            m_defaultMountId = defaultMountId;
        }

        /**
         * Sets information related to the Hub Space where the current workflow is stored.
         *
         * @param spacePath The path of the Hub Space, e.g. /Users/bob/Private.
         * @param spaceItemId The item ID of the Hub Space where the current workflow is stored, e.g. *3hjkfsd7zdsgf
         *            (including leading asterisk).
         * @param spaceVersion ignored
         * @return this builder instance.
         * @deprecated space versions are not supported by KNIME Hub any more, use {@link #withSpace(String, String)}
         */
        @Deprecated(since = "6.0")
        public HubSpaceLocationInfoIDBuilder withSpace(final String spacePath, final String spaceItemId,
                final String spaceVersion) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(spacePath), "Space path must not be null or blank");
            CheckUtils.checkArgument(spacePath.startsWith("/"), "Space path must start with a leading forward slash");

            CheckUtils.checkArgument(StringUtils.isNotBlank(spaceItemId), "Space item id must not be null or blank");
            CheckUtils.checkArgument(spaceItemId.startsWith("*"), "Space item id must start with a leading asterisk");

            return new HubSpaceLocationInfoIDBuilder(this, spacePath, spaceItemId);
        }

        /**
         * Sets information related to the Hub Space where the current workflow is stored.
         *
         * @param spacePath The path of the Hub Space, e.g. /Users/bob/Private.
         * @param spaceItemId The item ID of the Hub Space where the current workflow is stored, e.g. *3hjkfsd7zdsgf
         *            (including leading asterisk).
         * @return this builder instance.
         * @since 6.0
         */
        public HubSpaceLocationInfoIDBuilder withSpace(final String spacePath, final String spaceItemId) {
            CheckUtils.checkArgument(StringUtils.isNotBlank(spacePath), "Space path must not be null or blank");
            CheckUtils.checkArgument(spacePath.startsWith("/"), "Space path must start with a leading forward slash");

            CheckUtils.checkArgument(StringUtils.isNotBlank(spaceItemId), "Space item id must not be null or blank");
            CheckUtils.checkArgument(spaceItemId.startsWith("*"), "Space item id must start with a leading asterisk");

            return new HubSpaceLocationInfoIDBuilder(this, spacePath, spaceItemId);
        }
    }

    /** Builder accepting the item ID of the current workflow. */
    public static final class HubSpaceLocationInfoIDBuilder {

        private final HubSpaceLocationInfoSpaceBuilder m_spaceBuilder;
        private final String m_spacePath;
        private final String m_spaceItemId;

        HubSpaceLocationInfoIDBuilder(final HubSpaceLocationInfoSpaceBuilder spaceBuilder, final String spacePath,
                final String spaceItemId) {
            m_spaceBuilder = spaceBuilder;
            m_spacePath = spacePath;
            m_spaceItemId = spaceItemId;
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
            return new HubSpaceLocationInfoBuilder( //
                m_spaceBuilder.m_repositoryAddress, //
                m_spaceBuilder.m_workflowPath, //
                m_spaceBuilder.m_authenticator, //
                m_spaceBuilder.m_defaultMountId, //
                m_spacePath, //
                m_spaceItemId, //
                workflowItemId);
        }
    }

    /**
     * Finishing stage of the {@link ServerLocationInfo} builder.
     */
    public static class HubSpaceLocationInfoBuilder
            extends RestLocationInfoBuilderFactory.RestLocationInfoBuilder<HubSpaceLocationInfo> {

        private final String m_spacePath;
        private final String m_spaceItemId;
        private final String m_workflowItemId;
        private Integer m_itemVersion;

        HubSpaceLocationInfoBuilder( // NOSONAR only called internally
                final URI repositoryAddress, //
                final String workflowPath, //
                final Authenticator authenticator, //
                final String defaultMountId, //
                final String spacePath, //
                final String spaceItemId, //
                final String workflowItemId) {
            super(LocationType.HUB_SPACE, repositoryAddress, workflowPath, authenticator, defaultMountId);
            m_spacePath = spacePath;
            m_spaceItemId = spaceItemId;
            m_workflowItemId = workflowItemId;
        }

        /**
         * Sets the item version of the current workflow.
         *
         * @param itemVersion item version, or {@code null} for {@code 'current-state'}
         * @return this builder instance.
         * @throws IllegalArgumentException if {@code itemVersion} is not {@code null} but smaller than or equal to 0
         * @since 6.0
         */
        public HubSpaceLocationInfoBuilder withItemVersion(final Integer itemVersion) {
            if (itemVersion != null) {
                CheckUtils.checkArgument(itemVersion > 0,
                    "Item version must be greater than 0, found %d.", itemVersion);
            }
            m_itemVersion = itemVersion;
            return this;
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
                m_workflowItemId, //
                m_itemVersion);
        }
    }
}
