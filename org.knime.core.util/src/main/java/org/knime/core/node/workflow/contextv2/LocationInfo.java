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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;

/**
 * Base class of {@link LocationInfo}s. A {@link LocationInfo} provides information about where the current
 * workflow currently resides (is stored).
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public class LocationInfo {

    private final LocationType m_type;

    /**
     * The {@link Path} of the current workflow in the local file system of the workflow executor. If {@link #getType()}
     * returns {@link LocationType#WORKSPACE}, then this path is the actual location where the workflow resides/stored.
     * For other {@link LocationType}s, this path is only the path of a local job copy.
     */
    private final Path m_localWorkflowPath;

    /**
     * Sometimes a workflow is an ad-hoc copy of another one, e.g. during cluster/streamed execution. This field holds
     * the path of the original workflow in local file system of the workflow executor. May be null.
     */
    private final Path m_originalLocalWorkflowPath;

    /**
     * {@link Path} of a temporary folder in the local file system of the workflow executor. The folder can be used by
     * the workflow to store temporary files.
     */
    private final Path m_tempFolder;

    /**
     * A mountpoint-absolute knime:// URI that points to where the workflow resides from the perspective of the
     * executor's mount table, e.g. knime://LOCAL/Users/bob/myworkflow, or knime://devserver1/Users/bob/myworkflow.
     */
    private final URI m_mountpointURI;


    LocationInfo(final LocationType type, //
        final Path workflowPath, //
        final Path originalWorkflowPath, //
        final Path tempFolder, //
        final URI mountpointURI) {

        m_type = type;
        m_localWorkflowPath = workflowPath;
        m_originalLocalWorkflowPath = originalWorkflowPath;
        m_tempFolder = tempFolder;
        m_mountpointURI = mountpointURI;
    }

    /**
     *
     * @return the {@link LocationType} where the current workflow resides/is stored.
     */
    public LocationType getType() {
        return m_type;
    }

    /**
     * Provides the {@link Path} of the current workflow in the local file system of the workflow executor. If
     * {@link #getType()} returns {@link LocationType#WORKSPACE}, then this path is the actual location where the
     * workflow resides/stored. For other {@link LocationType}s, this path is only the path of a local job copy.
     *
     * @return path of workflow in local file system of the workflow executor.
     */
    public Path getLocalWorkflowPath() {
        return m_localWorkflowPath;
    }

    /**
     * Sometimes a workflow is an ad-hoc copy of another one, e.g. during cluster/streamed execution.
     *
     * @return path of the original workflow in local file system of the workflow executor.
     */
    public Optional<Path> getOriginalLocalWorkflowPath() {
        return Optional.ofNullable(m_originalLocalWorkflowPath);
    }

    /**
     * {@link Path} of a temporary folder in the local file system of the workflow executor. The folder can be used by
     * the workflow to store temporary files.
     *
     * @return the {@link Path} of a temporary folder which can be used by the workflow to store temporary files.
     */
    public Path getTempFolder() {
        return m_tempFolder;
    }

    /**
     * @return a mountpoint-absolute knime:// URI that points to where the workflow resides from the perspective of the
     *         executor's mount table.
     */
    public URI getMountpointURI() {
        return m_mountpointURI;
    }

    /**
     * Base class for {@link LocationInfo} builders.
     *
     * @param <B> The actual type of the builder.
     * @param <I> The type of {@link LocationInfo} produced.
     */
    @SuppressWarnings("rawtypes")
    abstract static class BaseBuilder<B extends BaseBuilder, I extends LocationInfo> {

        /**
         * See {@link LocationInfo#getType()}.
         */
        protected LocationType m_type;

        /**
         * See {@link LocationInfo#getLocalWorkflowPath()}.
         */
        protected Path m_localWorkflowPath;

        /**
         * See {@link LocationInfo#getOriginalLocalWorkflowPath()}.
         */
        protected Path m_originalLocalWorkflowPath;

        /**
         * See {@link LocationInfo#getTempFolder()}.
         */
        protected Path m_tempFolder;

        /**
         * See {@link LocationInfo#getMountpointURI()}.
         */
        protected URI m_mountpointURI;

        protected BaseBuilder(final LocationType type) {
            m_type = type;
        }

        @SuppressWarnings("unchecked")
        public final B withLocalWorkflowPath(final Path localWorkflowPath) {
            m_localWorkflowPath = localWorkflowPath;
            return (B)this;
        }

        @SuppressWarnings("unchecked")
        public final B withOriginalWorkflowPath(final Path originalLocalWorkflowPath) {
            m_originalLocalWorkflowPath = originalLocalWorkflowPath;
            return (B)this;
        }

        @SuppressWarnings("unchecked")
        public final B withTempFolder(final Path tempFolder) {
            m_tempFolder = tempFolder;
            return (B)this;
        }

        @SuppressWarnings("unchecked")
        public final B withMountpointURI(final URI mountpointURI) {
            m_mountpointURI = mountpointURI;
            return (B)this;
        }

        protected void checkFields() {
            CheckUtils.checkArgumentNotNull(m_type, "Location type must not be null");
            CheckUtils.checkArgumentNotNull(m_localWorkflowPath, "Local workflow path must not be null");
            CheckUtils.checkArgumentNotNull(m_mountpointURI, "Mountpoint URI must not be null");

            if (m_tempFolder == null) {
                try {
                    // this creates a new temp folder under System.getProperty("java.io.tmpdir")
                    // which will always be the KNIME temp folder, see KNIMEConstants
                    m_tempFolder = Files.createTempDirectory("knime_").toRealPath();
                } catch (IOException e) {
                    throw new IllegalStateException("Can't create workflow temp folder", e);
                }
            }
        }

        public abstract I build();

    }
}
