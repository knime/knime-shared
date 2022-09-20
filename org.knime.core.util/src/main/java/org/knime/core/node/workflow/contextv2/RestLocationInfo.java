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

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
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

    RestLocationInfo(final LocationType type, // NOSONAR invoked through builder only, number of arguments irrelevant
        final Path localWorkflowCopyPath, //
        final Path tempFolder, //
        final URI mountpointURI, //
        final URI repositoryAddress, //
        final Authenticator authenticator, //
        final String workflowPath, //
        final String defaultMountId) {

        super(type, localWorkflowCopyPath, null, tempFolder, mountpointURI);
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

    /**
     * Abstract builder superclass for {@link RestLocationInfo} instances.
     *
     * @author Bjoern Lohrmann, KNIME GmbH
     * @param <B> The actual type of the builder.
     * @param <I> The type of {@link LocationInfo} produced.
     */
    @SuppressWarnings("rawtypes")
    public abstract static class Builder<B extends Builder, I extends RestLocationInfo>
        extends LocationInfo.BaseBuilder<B, I> {

        /**
         * See {@link RestLocationInfo#getRepositoryAddress()}.
         */
        protected URI m_repositoryAddress;

        /**
         * See {@link RestLocationInfo#getWorkflowPath()}.
         */
        protected String m_workflowPath;

        /**
         * See {@link RestLocationInfo#getAuthenticator()}.
         */
        protected Authenticator m_authenticator;

        /**
         * See {@link RestLocationInfo#getDefaultMountId()}.
         */
        protected String m_defaultMountId;

        /**
         * Constructor.
         *
         * @param type The type of location.
         */
        protected Builder(final LocationType type) {
            super(type);
        }

        /**
         * Sets the {@link URI} of the repository where the current workflow resides. e.g.
         * <code>https://server.mycompany.com/knime/rest/v4/repository</code>.
         *
         * @param repositoryAddress {@link URI} of the repository where the current workflow resides.
         * @return this builder instance
         */
        @SuppressWarnings("unchecked")
        public final B withRepositoryAddress(final URI repositoryAddress) {
            m_repositoryAddress = repositoryAddress;
            return (B)this;
        }

        /**
         * Sets the path of the current workflow within the file tree of the repository, e.g. /Users/bob/workflow.
         *
         * @param workflowPath Pathof the current workflow within the file tree of the repository.
         * @return this builder instance
         */
        @SuppressWarnings("unchecked")
        public final B withWorkflowPath(final String workflowPath) {
            m_workflowPath = workflowPath;
            return (B)this;
        }

        /**
         * Sets the {@link Authenticator} for the repository where the current workflow resides.
         *
         * @param authenticator The {@link Authenticator} for the repository.
         * @return this builder instance
         */
        @SuppressWarnings("unchecked")
        public final B withAuthenticator(final Authenticator authenticator) {
            m_authenticator = authenticator;
            return (B)this;
        }

        /**
         * Sets the default mount ID declared by the REST endpoint, e.g. "My-KNIME-Hub".
         *
         * @param defaultMountId The default mount ID declared by the REST endpoint.
         * @return this builder instance
         */
        @SuppressWarnings("unchecked")
        public final B withDefaultMountId(final String defaultMountId) {
            m_defaultMountId = defaultMountId;
            return (B)this;
        }

        @Override
        protected final void checkFields() {
            super.checkFields();
            CheckUtils.checkArgumentNotNull(m_repositoryAddress, "Repository address must not be null");
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_workflowPath), "Workflow path must not be null or blank");
            CheckUtils.checkArgument(m_workflowPath.startsWith("/"), "Workflow path must have a leading forward slash");
            CheckUtils.checkArgumentNotNull(m_authenticator, "Authenticator must not be null");
            CheckUtils.checkArgument(StringUtils.isNotBlank(m_defaultMountId),
                "Default mount id must not be null or blank.");
        }
    }
}
