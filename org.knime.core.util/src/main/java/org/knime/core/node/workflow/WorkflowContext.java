/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
        private String m_userid;

        private File m_currentLocation;

        private File m_originalLocation;

        private File m_tempLocation;

        private File m_mountpointRoot;

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

        /**
         * Sets the user id of the context. The default the user id (if not set explicitly) is the id of the user
         * executing this process.
         *
         * @param userId the user id
         */
        public void setUserId(final String userId) {
            if (userId == null) {
                throw new IllegalArgumentException("User id must be set.");
            }
            m_userid = userId;
        }

        /**
         * Sets the current file system location of the workflow.
         *
         * @param currentLocation the current workflow location
         */
        public void setCurrentLocation(final File currentLocation) {
            if (currentLocation == null) {
                throw new IllegalArgumentException("Current workflow location must be set.");
            }
            m_currentLocation = currentLocation;
        }

        /**
         * Sets the original file system location of the workflow, e.g. if the current location is a copy.
         *
         * @param originalLocation the original workflow location
         */
        public void setOriginalLocation(final File originalLocation) {
            m_originalLocation = originalLocation;
        }

        /**
         * Sets the location for temporary files associated with the workflow.
         *
         * @param tempLocation the temp location
         */
        public void setTempLocation(final File tempLocation) {
            m_tempLocation = tempLocation;
        }

        /**
         * Sets the root of the mountpoint the workflow is contained in.
         *
         * @param mountpointRoot the path to the mountpoint's root
         */
        public void setMountpointRoot(final File mountpointRoot) {
            m_mountpointRoot = mountpointRoot;
        }

        /**
         * Creates a new workflow context with the information set in this factory.
         *
         * @return a new workflow context
         */
        public WorkflowContext createContext() {
            return new WorkflowContext(m_userid, m_currentLocation, m_originalLocation, m_tempLocation,
                m_mountpointRoot);
        }
    }

    private String m_userid;

    private File m_currentLocation;

    private File m_originalLocation;

    private File m_tempLocation;

    private File m_mountpointRoot;

    private WorkflowContext(final String userId, final File currentLocation, final File originalLocation,
        final File tempLocation, final File mountpointRoot) {
        assert userId != null;
        assert currentLocation != null;
        m_userid = userId;
        m_currentLocation = currentLocation;
        m_originalLocation = originalLocation;
        m_tempLocation = tempLocation;
        m_mountpointRoot = mountpointRoot;
    }

    /**
     * Returns the ID of the user which executes this workflow or <code>null</code> if this information is not
     * available.
     *
     * @return a user id or <code>null</code>
     */
    public String getUserid() {
        return m_userid;
    }

    /**
     * Returns the current location of the workflow, which can be a temporary directory.
     *
     * @return a local directory or <code>null</code>
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
        out.writeInt(20130606);
        out.writeUTF(m_currentLocation.getAbsolutePath()); /* not null */
        writeFilePath(out, m_mountpointRoot);
        writeFilePath(out, m_originalLocation);
        writeFilePath(out, m_tempLocation);
        out.writeUTF(m_userid); /* not null */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        final int version = in.readInt();
        if (version < 20130606) {
            throw new IOException("Unknown version number: " + version);
        }
        m_currentLocation = new File(in.readUTF());
        m_mountpointRoot = readFilePath(in);
        m_originalLocation = readFilePath(in);
        m_tempLocation = readFilePath(in);
        m_userid = in.readUTF();
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
}
