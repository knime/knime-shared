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

import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.auth.Authenticator;

/**
 * Provides information about a REST endpoint that holds the current workflow. Examples of such an endpoint are the
 * KNIME Server repository, or the KNIME Hub catalog. Such an endpoint gives access to a whole tree of workflow groups,
 * workflows and files. The current workflow is part of this tree and can be addressed by path (see
 * {@link #getWorkflowPath()}).
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public abstract class RestLocationInfo extends LocationInfo {

    /**
     * Base address of the REST endpoint where the current workflow resides. e.g.
     * <code>https://server.mycompany.com/knime/rest/v4/repository</code>.
     *
     * Same as {@link WorkflowContext#getRemoteRepositoryAddress()}.
     */
    private final URI m_restEndpointAddress;

    /**
     * Path of the workflow within the file tree of the REST endpoint, e.g.
     * <code>/Users/bob/Public/segmentation_example</code>.
     *
     * Same as {@link WorkflowContext#getRelativeRemotePath()}.
     */
    private final String m_workflowPath;

    /**
     * {@link Authenticator} for the REST endpoint where the current workflow resides.
     *
     * Same as {@link WorkflowContext#getServerAuthenticator()}.
     */
    private final Authenticator m_restAuthenticator;

    /**
     * Default mount ID declared by the REST endpoint, e.g. "My-KNIME-Hub".
     *
     * Same as {@link WorkflowContext#getRemoteMountId()}.
     */
    private final String m_defaultMountId;

    RestLocationInfo(final LocationType type, //
        final Path localWorkflowCopyPath, //
        final URI mountpointURI, //
        final URI restEndpointAddress, //
        final Authenticator restAuthenticator, //
        final String remoteWorkflowPath, //
        final String defaultMountId) {

        super(type, localWorkflowCopyPath, null, mountpointURI);
        m_restEndpointAddress = restEndpointAddress;
        m_restAuthenticator = restAuthenticator;
        m_workflowPath = remoteWorkflowPath;
        m_defaultMountId = defaultMountId;
    }

    /**
     * Provides the base address of the REST endpoint where the current workflow resides, e.g.
     * <code>https://server.mycompany.com/knime/rest/v4/repository</code>. The URI does not point to the current
     * workflow, only to the base address of the endpoint (see {@link #getWorkflowPath()}).
     *
     * @return the base address of the REST endpoint where the current workflow resides.
     * @see WorkflowContext#getRemoteRepositoryAddress()
     */
    public URI getRestEndpointAddress() {
        return m_restEndpointAddress;
    }

    /**
     * @return the {@link Authenticator} for the REST endpoint where the current workflow resides.
     * @see WorkflowContext#getServerAuthenticator()
     */
    public Authenticator getRestAuthenticator() {
        return m_restAuthenticator;
    }

    /**
     * @return path of the current workflow within the file tree of the REST endpoint, e.g. /Users/bob/workflow
     * @see WorkflowContext#getRelativeRemotePath()
     **/
    public String getWorkflowPath() {
        return m_workflowPath;
    }

    /**
     * @return the default mount ID declared by the REST endpoint, e.g. "My-KNIME-Hub".
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
         * See {@link RestLocationInfo#getRestEndpointAddress()}.
         */
        protected URI m_restEndpointAddress;

        /**
         * See {@link RestLocationInfo#getWorkflowPath()}.
         */
        protected String m_workflowPath;

        /**
         * See {@link RestLocationInfo#getRestAuthenticator()}.
         */
        protected Authenticator m_restAuthenticator;

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
         * Sets the base address of the REST endpoint where the current workflow resides.
         *
         * @param restEndpointAddress The REST endpoint base address, e.g.
         *            <code>https://server.mycompany.com/knime/rest/v4/repository</code>.
         * @return this builder instance
         */
        @SuppressWarnings("unchecked")
        public final B withRestEndpointAddress(final URI restEndpointAddress) {
            m_restEndpointAddress = restEndpointAddress;
            return (B)this;
        }

        /**
         * Sets the path of the workflow within the file tree of the REST endpoint.
         *
         * @param workflowPath Path of the workflow within the file tree, , e.g.
         *            <code>/Users/bob/Public/segmentation_example</code>.
         * @return this builder instance
         */
        @SuppressWarnings("unchecked")
        public final B withWorkflowPath(final String workflowPath) {
            m_workflowPath = workflowPath;
            return (B)this;
        }

        /**
         * Sets the {@link Authenticator} for the REST endpoint.
         *
         * @param restAuthenticator The {@link Authenticator} for the REST endpoint where the current workflow resides.
         * @return this builder instance
         */
        @SuppressWarnings("unchecked")
        public final B withRestAuthenticator(final Authenticator restAuthenticator) {
            m_restAuthenticator = restAuthenticator;
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
            CheckUtils.checkArgumentNotNull(m_restEndpointAddress, "REST endpoint address must not be null");
            CheckUtils.checkArgumentNotNull(m_workflowPath, "Workflow path must not be null");
            CheckUtils.checkArgumentNotNull(m_restAuthenticator, "REST authenticator must not be null");
            CheckUtils.checkArgumentNotNull(m_defaultMountId, "Default mount id must not be null");
        }
    }
}
