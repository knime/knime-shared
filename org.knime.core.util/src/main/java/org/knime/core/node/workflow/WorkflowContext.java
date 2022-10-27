/*
 * ------------------------------------------------------------------------
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
 * Created on 03.06.2013 by thor
 */
package org.knime.core.node.workflow;

import java.io.File;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.knime.core.node.workflow.contextv2.WorkflowContextV2;
import org.knime.core.util.User;
import org.knime.core.util.auth.Authenticator;

/**
 * <p>
 * This used to be a class, which has since AP 4.7.0 been replaced with {@link WorkflowContextV2}.
 * </p>
 *
 * This interface provides information about the context in which a workflow currently resides. It includes information
 * such as the current workflow directory or the ID of the user executing the workflow.
 *
 * <b>This class is not intended to be used by clients.</b>
 *
 * @author Thorsten Meinl, KNIME.com Zurich, Switzerland
 * @since 4.4
 * @noextend This class is not intended to be subclassed by clients.
 * @deprecated since 4.7
 */
@Deprecated(since = "4.7.0")
public interface WorkflowContext {

    /**
     * Returns the ID of the user which executes this workflow.
     *
     * @return a user id, never <code>null</code>
     */
    String getUserid();

    /**
     * Returns the current location of the workflow, which can be a temporary directory.
     *
     * @return a local directory, never <code>null</code>
     */
    File getCurrentLocation();

    /**
     * Returns the original location of the workflow, e.g. in the server repository. This has only meaning if the
     * current directory is a copy and is <code>null</code> otherwise.
     *
     * @return a local directory or <code>null</code>
     */
    File getOriginalLocation();

    /**
     * Returns the location of the temporary directory for this workflow or <code>null</code> if no specific temporary
     * directory for the workflow exists.
     *
     * @return a temporary directory or <code>null</code>
     */
    File getTempLocation();

    /**
     * Returns the root of the mountpoint the workflow is contained in or <code>null</code> if this information is not
     * available.
     *
     * @return the mountpoint root or <code>null</code>
     */
    File getMountpointRoot();

    /**
     * Returns the URI of the workflow inside a mount point. If this information is not known an empty optional is
     * returned.
     *
     * @return the workflow URI if known, an empty optional otherwise
     * @since 5.4
     */
    Optional<URI> getMountpointURI();

    /**
     * Returns the base address of the server repository (the REST endpoint). This value is only set if the workflow is
     * executed in a server executor.
     *
     * @return the repository base address or an empty optional if unknown
     */
    Optional<URI> getRemoteRepositoryAddress();

    /**
     * Returns the path of the workflow relative to the repository root (see {@link #getRemoteRepositoryAddress()}. This
     * value is only set if the workflow is executed in a server executor.
     *
     * @return the relative path or an empty optional if unknown
     */
    Optional<String> getRelativeRemotePath();

    /**
     * Returns the authenticator that should be used when talking to the server specified by
     * {@link #getRemoteRepositoryAddress()}. This value is only set if the workflow is executed in a server executor.
     *
     * @return the authenticator or an empty optional
     * @since 5.19
     */
    Optional<Authenticator> getServerAuthenticator();

    /**
     * Returns the (default) mount id of the remote server. This value is only set if the workflow is executed in a
     * server executor.
     *
     * @return a mount id or an empty optional
     */
    Optional<String> getRemoteMountId();

    /**
     * Returns the job id of the workflow. This value is only set if the workflow is executed in a server executor.
     *
     * @return a job id or an empty optional
     * @since 5.11
     */
    Optional<UUID> getJobId();

    /**
     * Returns whether the workflow location is a temporary copy of a workflow living somewhere else. In this case the
     * resolution of relative knime-URLs has to be done differently than for "real" workflows.
     *
     * @return <code>true</code> if the workflow is a temporary copy, <code>false</code> otherwise
     * @since 5.9
     */
    boolean isTemporaryCopy();

    /**
     * Factory for workflow contexts. This class is not thread-safe!
     */
    class Factory {
        String m_userid;

        File m_currentLocation;

        File m_originalLocation;

        File m_tempLocation;

        File m_mountpointRoot;

        URI m_mountpointUri;

        URI m_remoteRepositoryAddress;

        String m_relativeRemotePath;

        String m_remoteMountId;

        UUID m_jobId;

        boolean m_isTempCopy;

        Authenticator m_authenticator;

        /**
         * Creates a new factory for workflow contexts.
         *
         * @param currentLocation the current workflow location in the filesystem
         */
        public Factory(final File currentLocation) {
            m_currentLocation = currentLocation;
            try {
                m_userid = User.getUsername();
            } catch (Exception ex) {
                m_userid = System.getProperty("user.name");
            }
            if (m_userid == null) {
                throw new IllegalArgumentException("User id must be set.");
            }
            if (m_currentLocation == null) {
                throw new IllegalArgumentException("Current workflow location must be set.");
            }
        }

        /** New instance based on the value of the passed reference.
         *
         * @param origContext To copy from - not null.
         * @since 5.3 */
        protected Factory(final WorkflowContext origContext) {
            m_currentLocation = origContext.getCurrentLocation();
            m_userid = origContext.getUserid();
            m_mountpointRoot = origContext.getMountpointRoot();
            m_originalLocation = origContext.getOriginalLocation();
            m_tempLocation = origContext.getTempLocation();
            m_mountpointUri = origContext.getMountpointURI().orElse(null);
            m_remoteRepositoryAddress = origContext.getRemoteRepositoryAddress().orElse(null);
            m_remoteMountId = origContext.getRemoteMountId().orElse(null);
            m_relativeRemotePath = origContext.getRelativeRemotePath().orElse(null);
            m_isTempCopy = origContext.isTemporaryCopy();
            m_jobId = origContext.getJobId().orElse(null);
            m_authenticator = origContext.getServerAuthenticator().orElse(null);
        }

        /**
         * Sets the user id of the context. The default the user id (if not set explicitly) is the id of the user
         * executing this process.
         *
         * @param userId the user id
         */
        public void setUserId(final String userId) {
            if (userId == null) {
                throw new IllegalArgumentException();
            }
            m_userid = userId;
        }

        /**
         * Sets the current file system location of the workflow.
         *
         * @param currentLocation the current workflow location
         * @return the updated factory
         */
        public Factory setCurrentLocation(final File currentLocation) {
            if (currentLocation == null) {
                throw new IllegalArgumentException("Current workflow location must be set.");
            }
            m_currentLocation = currentLocation;
            return this;
        }

        /**
         * Sets the original file system location of the workflow, e.g. if the current location is a copy.
         *
         * @param originalLocation the original workflow location
         * @return the updated factory
         */
        public Factory setOriginalLocation(final File originalLocation) {
            m_originalLocation = originalLocation;
            return this;
        }

        /**
         * Sets the location for temporary files associated with the workflow.
         *
         * @param tempLocation the temp location
         * @return the updated factory
         */
        public Factory setTempLocation(final File tempLocation) {
            m_tempLocation = tempLocation;
            return this;
        }

        /**
         * Sets the root of the mountpoint the workflow is contained in.
         *
         * @param mountpointRoot the path to the mountpoint's root
         * @return the updated factory
         */
        public Factory setMountpointRoot(final File mountpointRoot) {
            m_mountpointRoot = mountpointRoot;
            return this;
        }

        /**
         * Sets the mount point ID and the path within the mountpoint. These values are derived from the given URI.
         *
         * @param uri an URI for the workflow
         * @return the updated factory
         * @since 5.4
         */
        public Factory setMountpointURI(final URI uri) {
            m_mountpointUri = uri;
            return this;
        }

        /**
         * Sets the root address of the server repository (the REST endpoint).
         *
         * @param baseUri a URI to root of the server repository
         * @param relativePath the relative path of the workflow in the server repository
         * @return the updated factory
         */
        public Factory setRemoteAddress(final URI baseUri, final String relativePath) {
            if (baseUri == null) {
                throw new IllegalArgumentException("Base address must not be null");
            }
            if (relativePath == null) {
                throw new IllegalArgumentException("Relative path must not be null");
            }
            m_remoteRepositoryAddress = baseUri;
            m_relativeRemotePath = relativePath;
            return this;
        }

        /**
         * Sets the authenticator that should be used when talking to the server specified via
         * {@link #setRemoteAddress(URI, String)}.
         *
         * @param authenticator the authenticator
         * @return the updated factory
         * @since 5.19
         */
        public Factory setRemoteAuthenticator(final Authenticator authenticator) {
            m_authenticator = authenticator;
            return this;
        }

        /**
         * Sets the (default) mount ID of the remote server.
         *
         * @param id a mount ID, may be <code>null</code>
         * @return the updated factory
         */
        public Factory setRemoteMountId(final String id) {
            m_remoteMountId = id;
            return this;
        }

        /**
         * Sets the jobId of the workflow
         *
         * @param jobId the jobId of the workflow
         * @return the updated factory
         * @since 5.11
         */
        public Factory setJobId(final UUID jobId) {
            m_jobId = jobId;
            return this;
        }

        /**
         * Sets whether the workflow location is a temporary copy of a workflow living somewhere else. In this case the
         * resolution of relative knime-URLs has to be done differently than for "real" workflows.
         *
         * @param b <code>true</code> if the workflow is a temporary copy, <code>false</code> otherwise
         * @return the updated factory
         * @since 5.9
         */
        public Factory setTemporaryCopy(final boolean b) {
            m_isTempCopy = b;
            return this;
        }

        /**
         * Creates a new workflow context with the information set in this factory.
         *
         * @return a new workflow context
         */
        public WorkflowContext createContext() {
            final var userid = m_userid;
            final var currentLocation = m_currentLocation;
            final var originalLocation = m_originalLocation;
            final var tempLocation = m_tempLocation;
            final var mountpointRoot = m_mountpointRoot;
            final var mountpointUri = m_mountpointUri;
            final var remoteRepositoryAddress = m_remoteRepositoryAddress;
            final var relativeRemotePath = m_relativeRemotePath;
            final var remoteMountId = m_remoteMountId;
            final var jobId = m_jobId;
            final var isTempCopy = m_isTempCopy;
            final var authenticator = m_authenticator;

            return new WorkflowContext() { // NOSONAR this anonymous class is lengthy but very boring

                @Override
                public String getUserid() {
                    return userid;
                }

                @Override
                public File getCurrentLocation() {
                    return currentLocation;
                }

                @Override
                public File getOriginalLocation() {
                    return originalLocation;
                }

                @Override
                public File getTempLocation() {
                    return tempLocation;
                }

                @Override
                public File getMountpointRoot() {
                    return mountpointRoot;
                }

                @Override
                public Optional<URI> getMountpointURI() {
                    return Optional.ofNullable(mountpointUri);
                }

                @Override
                public Optional<URI> getRemoteRepositoryAddress() {
                    return Optional.ofNullable(remoteRepositoryAddress);
                }

                @Override
                public Optional<String> getRelativeRemotePath() {
                    return Optional.ofNullable(relativeRemotePath);
                }

                @Override
                public Optional<Authenticator> getServerAuthenticator() {
                    return Optional.ofNullable(authenticator);
                }

                @Override
                public Optional<String> getRemoteMountId() {
                    return Optional.ofNullable(remoteMountId);
                }

                @Override
                public Optional<UUID> getJobId() {
                    return Optional.ofNullable(jobId);
                }

                @Override
                public boolean isTemporaryCopy() {
                    return isTempCopy;
                }
            };
        }
    }
}
