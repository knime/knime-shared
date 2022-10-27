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
import java.util.Optional;

import org.apache.http.client.utils.URIBuilder;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.Pair;
import org.knime.core.util.auth.Authenticator;

/**
 * This class holds information about the repository, where the current workflow resides (see
 * {@link #getRepositoryAddress()}. The endpoint gives access to a whole tree of workflow groups, workflows and files.
 * The current workflow is part of this tree and can be addressed by path (see {@link #getWorkflowPath()}).
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
@SuppressWarnings("deprecation")
public abstract class RestLocationInfo extends LocationInfo {

    /**
     * {@link URI} of the repository where the current workflow resides. e.g.
     * <code>https://server.mycompany.com/knime/rest/v4/repository</code>.
     *
     * Same as {@link WorkflowContext#getRemoteRepositoryAddress()}.
     */
    private final URI m_repositoryAddress;

    /**
     * Path of the workflow within the file tree of the repository, e.g.
     * <code>/Users/bob/Public/segmentation_example</code>.
     *
     * Same as {@link WorkflowContext#getRelativeRemotePath()}.
     */
    private final String m_workflowPath;

    /**
     * The full URI of the workflow in the repository, obtained by concatenating {@link #getRepositoryAddress()} and
     * {@link #getWorkflowPath()}.
     */
    private URI m_workflowAddress;

    /**
     * {@link Authenticator} for the repository where the current workflow resides.
     *
     * Same as {@link WorkflowContext#getServerAuthenticator()}.
     */
    private final Authenticator m_authenticator;

    /**
     * Default mount ID declared by the repository, e.g. "My-KNIME-Hub".
     *
     * Same as {@link WorkflowContext#getRemoteMountId()}.
     */
    private final String m_defaultMountId;

    RestLocationInfo(final LocationType type, //
        final URI repositoryAddress, //
        final Authenticator authenticator, //
        final String workflowPath, //
        final String defaultMountId) {

        super(type);
        m_repositoryAddress = repositoryAddress;
        m_authenticator = authenticator;
        m_workflowPath = workflowPath;
        m_defaultMountId = defaultMountId;

        try {
            m_workflowAddress = new URI(m_repositoryAddress.getScheme(), //
                m_repositoryAddress.getHost(), //
                m_repositoryAddress.getPath() + m_workflowPath, //
                null).normalize();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Failed to create workflow address URI", ex);
        }
    }

    /**
     * Provides {@link URI} of the repository where the current workflow resides. e.g.
     * <code>https://server.mycompany.com/knime/rest/v4/repository</code>. To get the {@link URI} of the workflow see
     * {@link #getWorkflowAddress()}).
     *
     * @return {@link URI} that holds the address of the repository, where the current workflow resides.
     * @see WorkflowContext#getRemoteRepositoryAddress()
     */
    public URI getRepositoryAddress() {
        return m_repositoryAddress;
    }

    /**
     * @return the {@link Authenticator} for the repository where the current workflow resides.
     * @see WorkflowContext#getServerAuthenticator()
     */
    public Authenticator getAuthenticator() {
        return m_authenticator;
    }

    /**
     * Provides the path of the current workflow within the file tree of the repository, e.g. /Users/bob/workflow. To
     * get the full {@link URI} of the workflow see {@link #getWorkflowAddress()}).
     *
     * @return path of the current workflow within the file tree of the repository.
     * @see WorkflowContext#getRelativeRemotePath()
     **/
    public String getWorkflowPath() {
        return m_workflowPath;
    }

    /**
     * Provides the full URI of the workflow in the repository, obtained by concatenating
     * {@link #getRepositoryAddress()} and {@link #getWorkflowPath()}.
     *
     * @return the full {@link URI} of the workflow in the repository.
     */
    public URI getWorkflowAddress() {
        return m_workflowAddress;
    }

    /**
     * @return the default mount ID declared by the repository, e.g. "My-KNIME-Hub".
     * @see WorkflowContext#getRemoteMountId()
     */
    public String getDefaultMountId() {
        return m_defaultMountId;
    }

    @Override
    Optional<URI> mountpointURI(final Pair<URI, Path> mountpoint, final Path localWorkflowPath) {
        try {
            return Optional.of(new URIBuilder(mountpoint.getFirst()).setPath(getWorkflowPath()).build());
        } catch (final URISyntaxException ex) {
            throw new IllegalArgumentException("Path not suitable for mountpoint URI: '" + getWorkflowPath() + "'", ex);
        }
    }

    @Override
    void addFields(final StringBuilder sb, final int indent) {
        super.addFields(sb, indent);
        final var init = "  ".repeat(indent);
        sb.append(init).append("repositoryAddress=").append(m_repositoryAddress).append("\n");
        sb.append(init).append("workflowPath=").append(m_workflowPath).append("\n");
        sb.append(init).append("workflowAddress=").append(m_workflowAddress).append("\n");
        sb.append(init).append("authenticator=").append(m_authenticator.getClass().getSimpleName()).append("\n");
        sb.append(init).append("defaultMountId=").append(m_defaultMountId).append("\n");
    }
}
