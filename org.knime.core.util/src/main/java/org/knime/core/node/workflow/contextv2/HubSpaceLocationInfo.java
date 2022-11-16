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
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import org.apache.http.client.utils.URIBuilder;
import org.knime.core.node.workflow.contextv2.HubSpaceLocationInfoBuilderFactory.HubSpaceLocationInfoSpaceBuilder;
import org.knime.core.node.workflow.contextv2.RestLocationInfoBuilderFactory.RestLocationInfoReqRABuilder;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.Pair;
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
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
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

    HubSpaceLocationInfo( // NOSONAR only invoked by builder class
            final URI repositoryAddress, //
            final Authenticator authenticator, //
            final String workflowPath, //
            final String defaultMountId, //
            final URI workflowAddress, //
            final String spacePath, //
            final String spaceItemId, //
            final String spaceVersion, //
            final String workflowItemId) {

        super(LocationType.HUB_SPACE, //
            repositoryAddress, //
            authenticator, //
            workflowPath, //
            defaultMountId, //
            workflowAddress);

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

    @Override
    Optional<URI> mountpointURI(final Pair<URI, Path> mountpoint, final Path localWorkflowPath) {
        try {
            final var builder = new URIBuilder(mountpoint.getFirst()).setPath(getWorkflowPath());
            if (m_spaceVersion != null) {
                builder.addParameter("spaceVersion", m_spaceVersion);
            }
            return Optional.of(builder.build().normalize());
        } catch (final URISyntaxException ex) {
            throw new IllegalArgumentException("Path not suitable for mountpoint URI: '" + getWorkflowPath() + "'", ex);
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final HubSpaceLocationInfo that = (HubSpaceLocationInfo)other;
        return Objects.equals(getRepositoryAddress(), that.getRepositoryAddress())
                && Objects.equals(getAuthenticator(), that.getAuthenticator())
                && Objects.equals(getWorkflowPath(), that.getWorkflowPath())
                && Objects.equals(getDefaultMountId(), that.getDefaultMountId())
                && Objects.equals(getSpacePath(), that.getSpacePath())
                && Objects.equals(getSpaceItemId(), that.getSpaceItemId())
                && Objects.equals(getSpaceVersion(), that.getSpaceVersion())
                && Objects.equals(getWorkflowItemId(), that.getWorkflowItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash( //
            getType(), //
            getRepositoryAddress(), //
            getAuthenticator(), //
            getWorkflowPath(), //
            getDefaultMountId());
    }

    /**
     * Creates a fluent builder for {@link HubSpaceLocationInfo} instances.
     *
     * @return new builder
     */
    public static RestLocationInfoReqRABuilder<HubSpaceLocationInfoSpaceBuilder> builder() {
        return HubSpaceLocationInfoBuilderFactory.create();
    }

    @Override
    void addFields(final StringBuilder sb, final int indent) {
        super.addFields(sb, indent);
        final var init = "  ".repeat(indent);
        sb.append(init).append("spacePath=").append(m_spacePath).append("\n");
        sb.append(init).append("spaceItemId=").append(m_spaceItemId).append("\n");
        sb.append(init).append("spaceVersion=").append(m_spaceVersion).append("\n");
        sb.append(init).append("workflowItemId=").append(m_workflowItemId).append("\n");
    }
}
