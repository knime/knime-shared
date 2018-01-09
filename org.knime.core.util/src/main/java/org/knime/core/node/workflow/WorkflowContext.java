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

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.knime.core.util.User;

/**
 * This class holds information about the context in which a workflows currently resides. It includes information such
 * as the current workflow directory or the ID of the user executing the workflow. Instances must be created via the
 * {@link Factory} since the workflow context is purely read-only.
 *
 * <b>This class is not intended to be used by clients.</b>
 *
 * @author Thorsten Meinl, KNIME.com Zurich, Switzerland
 * @since 4.4
 */
public final class WorkflowContext implements Externalizable {

    private static final long serialVersionUID = 67323L;

    /**
     * Factory for workflow contexts. This class is not thread-safe!
     */
    public static final class Factory {
        String m_userid;

        File m_currentLocation;

        File m_originalLocation;

        File m_tempLocation;

        File m_mountpointRoot;

        URI m_mountpointUri;

        URI m_remoteRepositoryAddress;

        String m_relativeRemotePath;

        String m_remoteAuthToken;

        String m_remoteMountId;

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
         * @param origContext To copy from - not null.
         * @since 5.3 */
        public Factory(final WorkflowContext origContext) {
            m_currentLocation = origContext.m_currentLocation;
            m_userid = origContext.m_userid;
            m_mountpointRoot = origContext.m_mountpointRoot;
            m_originalLocation = origContext.m_originalLocation;
            m_tempLocation = origContext.m_tempLocation;
            m_mountpointUri = origContext.m_mountpointUri;
            m_remoteRepositoryAddress = origContext.m_remoteRepositoryAddress;
            m_remoteMountId = origContext.m_remoteMountId;
            m_remoteAuthToken = origContext.m_remoteAuthToken;
            m_relativeRemotePath = origContext.m_relativeRemotePath;
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
         * Sets the authentication token (JWT) that should be used when talking to the server specified via
         * {@link #setRemoteAddress(URI, String)}.
         *
         * @param token a JWT, may be <code>null</code>
         * @return the updated factory
         */
        public Factory setRemoteAuthToken(final String token) {
            m_remoteAuthToken = token;
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
         * Creates a new workflow context with the information set in this factory.
         *
         * @return a new workflow context
         */
        public WorkflowContext createContext() {
            return new WorkflowContext(this);
        }
    }

    private String m_userid;

    private File m_currentLocation;

    private File m_originalLocation;

    private File m_tempLocation;

    private File m_mountpointRoot;

    private transient URI m_mountpointUri; // the URI is only meaningful within the same instance

    private URI m_remoteRepositoryAddress;

    private String m_relativeRemotePath;

    private String m_remoteAuthToken;

    private String m_remoteMountId;


    private WorkflowContext(final Factory factory) {
        assert factory.m_userid != null : "User is must not be null";
        assert factory.m_currentLocation != null : "Current workflow location must not be null";
        m_userid = factory.m_userid;
        m_currentLocation = factory.m_currentLocation;
        m_originalLocation = factory.m_originalLocation;
        m_tempLocation = factory.m_tempLocation;
        m_mountpointRoot = factory.m_mountpointRoot;
        if (factory.m_mountpointUri != null) {
            if (factory.m_mountpointUri.getPath().endsWith("/workflow.knime")) {
                String path = factory.m_mountpointUri.getPath();
                try {
                    m_mountpointUri = new URI(factory.m_mountpointUri.getScheme(), factory.m_mountpointUri.getUserInfo(),
                        factory.m_mountpointUri.getHost(), factory.m_mountpointUri.getPort(),
                        path.substring(0, path.length() - "/workflow.knime".length()),
                        factory.m_mountpointUri.getQuery(), factory.m_mountpointUri.getFragment());
                } catch (URISyntaxException ex) {
                    // shouldn't happen because we come from a valid URI
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            } else {
                m_mountpointUri = factory.m_mountpointUri;
            }
        }
        m_remoteRepositoryAddress = factory.m_remoteRepositoryAddress;
        m_relativeRemotePath = factory.m_relativeRemotePath;
        m_remoteAuthToken = factory.m_remoteAuthToken;
        m_remoteMountId = factory.m_remoteMountId;
    }

    /**
     * Returns the ID of the user which executes this workflow.
     *
     * @return a user id, never <code>null</code>
     */
    public String getUserid() {
        return m_userid;
    }

    /**
     * Returns the current location of the workflow, which can be a temporary directory.
     *
     * @return a local directory, never <code>null</code>
     */
    public File getCurrentLocation() {
        return m_currentLocation;
    }

    /**
     * Returns the original location of the workflow, e.g. in the server repository. This has only meaning if the
     * current directory is a copy and is <code>null</code> otherwise.
     *
     * @return a local directory or <code>null</code>
     */
    public File getOriginalLocation() {
        return m_originalLocation;
    }

    /**
     * Returns the location of the temporary directory for this workflow or <code>null</code> if no specific temporary
     * directory for the workflow exists.
     *
     * @return a temporary directory or <code>null</code>
     */
    public File getTempLocation() {
        return m_tempLocation;
    }

    /**
     * Returns the root of the mountpoint the workflow is contained in or <code>null</code> if this information is not
     * available.
     *
     * @return the mountpoint root or <code>null</code>
     */
    public File getMountpointRoot() {
        return m_mountpointRoot;
    }

    /**
     * Returns the URI of the workflow inside a mount point. If this information is not known an empty optional is
     * returned.
     *
     * @return the workflow URI if known, an empty optional otherwise
     * @since 5.4
     */
    public Optional<URI> getMountpointURI() {
        return Optional.ofNullable(m_mountpointUri);
    }

    /**
     * Returns the base address of the server repository (the REST endpoint). This value is only set if the workflow is
     * executed in a server executor.
     *
     * @return the repository base address or an empty optional if unknown
     */
    public Optional<URI> getRemoteRepositoryAddress() {
        return Optional.ofNullable(m_remoteRepositoryAddress);
    }

    /**
     * Returns the path of the workflow relative to the repository root (see {@link #getRemoteRepositoryAddress()}.
     * This value is only set if the workflow is executed in a server executor.
     *
     * @return the relative path or an empty optional if unknown
     */
    public Optional<String> getRelativeRemotePath() {
        return Optional.ofNullable(m_relativeRemotePath);
    }

    /**
     * Returns the JWT that should be used when talking to the server specified by {@link #getRemoteRepositoryAddress()}
     * . This value is only set if the workflow is executed in a server executor.
     *
     * @return an authentication token or an empty optional
     */
    public Optional<String> getServerAuthToken() {
        return Optional.ofNullable(m_remoteAuthToken);
    }

    /**
     * Returns the (default) mount id of the remote server. This value is only set if the workflow is executed in a
     * server executor.
     *
     * @return a mount id or an empty optional
     */
    public Optional<String> getRemoteMountId() {
        return Optional.ofNullable(m_remoteMountId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return m_currentLocation.getAbsolutePath() + ((m_mountpointRoot != null) ? (" @ " + m_mountpointRoot) : "");
    }

    /*----------------------------------------------------------------------------------*/
    /* Externalizable stuff */
    /* ---------------------------------------------------------------------------------*/
    /**
     * Serialisation constructor. Don't use!
     */
    public WorkflowContext() {
        // Do not use.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeUTF(m_currentLocation.getAbsolutePath()); /* not null */
        writeFilePath(out, m_mountpointRoot);
        writeFilePath(out, m_originalLocation);
        writeFilePath(out, m_tempLocation);
        out.writeUTF(m_userid); /* not null */

        if (m_remoteRepositoryAddress != null) {
            out.writeBoolean(true);
            out.writeUTF(m_remoteRepositoryAddress.toString());
            out.writeUTF(m_relativeRemotePath);
        } else {
            out.writeBoolean(false);
        }
        if (m_remoteAuthToken != null) {
            out.writeBoolean(true);
            out.writeUTF(m_remoteAuthToken);
        } else {
            out.writeBoolean(false);
        }
        if (m_remoteMountId != null) {
            out.writeBoolean(true);
            out.writeUTF(m_remoteMountId);
        } else {
            out.writeBoolean(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        m_currentLocation = new File(in.readUTF());
        m_mountpointRoot = readFilePath(in);
        m_originalLocation = readFilePath(in);
        m_tempLocation = readFilePath(in);
        m_userid = in.readUTF();

        if (in.readBoolean()) {
            m_remoteRepositoryAddress = URI.create(in.readUTF());
            m_relativeRemotePath = in.readUTF();
        }

        if (in.readBoolean()) {
            m_remoteAuthToken = in.readUTF();
        }
        if (in.readBoolean()) {
            m_remoteMountId = in.readUTF();
        }
    }

    private void writeFilePath(final ObjectOutput out, final File f) throws IOException {
        out.writeBoolean(f != null);
        if (f != null) {
            out.writeUTF(f.getAbsolutePath());
        }
    }

    private File readFilePath(final ObjectInput in) throws IOException, ClassNotFoundException {
        if (in.readBoolean()) {
            return new File(in.readUTF());
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_currentLocation.hashCode();
        result = prime * result + ((m_mountpointRoot == null) ? 0 : m_mountpointRoot.hashCode());
        result = prime * result + ((m_originalLocation == null) ? 0 : m_originalLocation.hashCode());
        result = prime * result + ((m_tempLocation == null) ? 0 : m_tempLocation.hashCode());
        result = prime * result + ((m_userid == null) ? 0 : m_userid.hashCode());
        result = prime * result + ((m_mountpointUri == null) ? 0 : m_mountpointUri.hashCode());
        result = prime * result + ((m_remoteRepositoryAddress == null) ? 0 : m_remoteRepositoryAddress.hashCode());
        result = prime * result + ((m_relativeRemotePath == null) ? 0 : m_relativeRemotePath.hashCode());
        result = prime * result + ((m_remoteAuthToken == null) ? 0 : m_remoteAuthToken.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WorkflowContext other = (WorkflowContext)obj;
        if (m_currentLocation == null) {
            if (other.m_currentLocation != null) {
                return false;
            }
        } else if (!m_currentLocation.equals(other.m_currentLocation)) {
            return false;
        }
        if (m_mountpointRoot == null) {
            if (other.m_mountpointRoot != null) {
                return false;
            }
        } else if (!m_mountpointRoot.equals(other.m_mountpointRoot)) {
            return false;
        }
        if (m_originalLocation == null) {
            if (other.m_originalLocation != null) {
                return false;
            }
        } else if (!m_originalLocation.equals(other.m_originalLocation)) {
            return false;
        }
        if (m_tempLocation == null) {
            if (other.m_tempLocation != null) {
                return false;
            }
        } else if (!m_tempLocation.equals(other.m_tempLocation)) {
            return false;
        }
        if (m_userid == null) {
            if (other.m_userid != null) {
                return false;
            }
        } else if (!m_userid.equals(other.m_userid)) {
            return false;
        }
        return Objects.equals(m_mountpointUri, other.m_mountpointUri)
                && Objects.equals(m_remoteAuthToken, other.m_remoteAuthToken)
                && Objects.equals(m_remoteRepositoryAddress, other.m_remoteRepositoryAddress)
                && Objects.equals(m_relativeRemotePath, other.m_relativeRemotePath);
    }
}
