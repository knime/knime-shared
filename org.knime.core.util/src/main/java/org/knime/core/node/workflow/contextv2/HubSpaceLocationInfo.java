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

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.auth.Authenticator;

/**
 * Provides information about where on a KNIME Hub a workflow is stored. In Hub, all workflows reside in a Space, and
 * Spaces support versioning. Additionally, all items in the Hub file tree can be referenced not only by path (mutable)
 * but also by ID (immutable).
 *
 * <p>
 * Please also see the Javadoc of {@link RestLocationInfo} for the fields not specific to KNIME Hub.
 * </p>
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @see RestLocationInfo
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class HubSpaceLocationInfo extends RestLocationInfo {

    /**
     * The path of the Hub Space where the current workflow is stored, e.g. /Users/bob/Private
     */
    private final String m_spacePath;

    /**
     * The item ID of the Hub Space where the current workflow is stored, e.g. *3hjkfsd7zdsgf (including leading
     * asterisk).
     */
    private final String m_spaceItemId;

    /**
     * The version of the Hub Space where the current workflow is stored. May be null.
     */
    private final String m_spaceVersion;

    /**
     * The item ID of the current workflow, e.g. *2zwDuYFgpLVveXfX (including leading asterisk).
     */
    private final String m_workflowItemId;

    HubSpaceLocationInfo(final Path localWorkflowCopyPath, // NOSONAR only invoked by builder class
        final Path tempFolder, //
        final URI mountpointURI, //
        final URI repositoryAddress, //
        final Authenticator authenticator, //
        final String workflowPath, //
        final String defaultMountId, //
        final String spacePath, //
        final String spaceItemId, //
        final String spaceVersion, //
        final String workflowItemId) {

        super(LocationType.HUB_SPACE, //
            localWorkflowCopyPath, //
            tempFolder, //
            mountpointURI, //
            repositoryAddress, //
            authenticator, //
            workflowPath, //
            defaultMountId);

        m_spacePath = spacePath;
        m_spaceItemId = spaceItemId;
        m_spaceVersion = spaceVersion;
        m_workflowItemId = workflowItemId;
    }

    /**
     * @return the path of the Hub Space where the current workflow is stored, e.g. /Users/bob/Private.
     */
    public String getSpacePath() {
        return m_spacePath;
    }

    /**
     * @return the item ID of the Hub Space where the current workflow is stored, e.g. *3hjkfsd7zdsgf (including leading
     *         asterisk).
     */
    public String getSpaceItemId() {
        return m_spaceItemId;
    }

    /**
     * @return the version of the Hub Space where the current workflow is stored. May be null, which indicates the
     *         staging version (latest version plus unversioned changes on top).
     */
    public Optional<String> getSpaceVersion() {
        return Optional.ofNullable(m_spaceVersion);
    }

    /**
     * @return the item ID of the current workflow, e.g. *2zwDuYFgpLVveXfX (including leading asterisk).
     */
    public String getWorkflowItemId() {
        return m_workflowItemId;
    }

    /**
     * Builder for {@link HubSpaceLocationInfo} instances.
     */
    public static class Builder extends RestLocationInfo.Builder<Builder, HubSpaceLocationInfo> {

        private String m_spacePath;

        private String m_spaceItemId;

        private String m_spaceVersion;

        private String m_workflowItemId;

        /**
         * Constructor.
         */
        public Builder() {
            super(LocationType.HUB_SPACE);
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
        public Builder withSpace(final String spacePath, final String spaceItemId, final String spaceVersion) {
            m_spacePath = spacePath;
            m_spaceItemId = spaceItemId;
            m_spaceVersion = spaceVersion;
            return this;
        }

        /**
         * Sets the item ID of the current workflow.
         *
         * @param workflowItemId The item ID of the current workflow, e.g. *2zwDuYFgpLVveXfX (including leading
         *            asterisk).
         * @return this builder instance.
         */
        public Builder withWorkflowItemId(final String workflowItemId) {
            m_workflowItemId = workflowItemId;
            return this;
        }

        @Override
        public HubSpaceLocationInfo build() {
            super.checkFields();
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_spacePath), "Space path must not be null or blank");
            CheckUtils.checkArgument(m_spacePath.startsWith("/"), "Space path must start with a leading forward slash");

            CheckUtils.checkArgument(StringUtils.isNotBlank(m_spaceItemId), "Space item id must not be null or blank");
            CheckUtils.checkArgument(m_spaceItemId.startsWith("*"), "Space item id must start with a leading asterisk");

            CheckUtils.checkArgument(StringUtils.isNotBlank(m_workflowItemId),
                "Workflow item id must not be null or blank");
            CheckUtils.checkArgument(m_workflowItemId.startsWith("*"),
                "Workflow item id must start with a leading asterisk");

            if (m_spaceVersion != null) {
                CheckUtils.checkArgument(StringUtils.isNotBlank(m_spaceItemId), "Space version must not be blank.");
            }

            return new HubSpaceLocationInfo(m_localWorkflowPath, //
                m_tempFolder, //
                m_mountpointURI, //
                m_repositoryAddress, //
                m_authenticator, //
                m_workflowPath, //
                m_defaultMountId, //
                m_spacePath, //
                m_spaceItemId, //
                m_spaceVersion, //
                m_workflowItemId);
        }
    }
}
