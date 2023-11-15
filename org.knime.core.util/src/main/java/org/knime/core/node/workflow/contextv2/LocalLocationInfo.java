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
 *   Sep 29, 2022 (leonard.woerteler): created
 */
package org.knime.core.node.workflow.contextv2;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.apache.http.client.utils.URIBuilder;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.Pair;

/**
 * Provides information about a workflow from the local file system, which does not have a remote location.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 4.7
 */
public final class LocalLocationInfo extends LocationInfo {

    /**
     * Default instance used for all local locations that do not originate from an archive.
     */
    private static final LocalLocationInfo NO_ARCHIVE = new LocalLocationInfo(null);

    private final Path m_sourceArchive;

    /**
     * Returns a {@link LocationInfo} for a local workflow.
     *
     * @param archivePath path to the source archive the workflow was extracted from, may be {@code null}
     * @return location info
     */
    public static LocalLocationInfo getInstance(final Path archivePath) {
        return archivePath == null ? NO_ARCHIVE : new LocalLocationInfo(archivePath);
    }

    private LocalLocationInfo(final Path localArchive) {
        super(LocationType.LOCAL);
        m_sourceArchive = localArchive;
    }

    /**
     * {@link Path} to the archive this workflow was loaded from, if applicable.
     *
     * @return path to the source archive if applicable, {@link Optional#empty()} otherwise
     */
    public Optional<Path> getSourceArchive() {
        return Optional.ofNullable(m_sourceArchive);
    }

    @Override
    Optional<URI> mountpointURI(final Pair<URI, Path> mountpoint, final Path localWorkflowPath) {
        if (m_sourceArchive != null) {
            // temporary copies of archives can't be mounted
            return Optional.empty();
        }

        final var mountpointRoot = mountpoint.getSecond();
        final var relPath = mountpointRoot.relativize(localWorkflowPath);
        CheckUtils.checkState(!relPath.isAbsolute(),
            "Workflow path %s is not located under mountpoint root %s.", mountpointRoot, localWorkflowPath);


        // divide the relative path into segments
        final var pathSegments = new ArrayList<String>();
        relPath.forEach(segment -> pathSegments.add(segment.toString()));

        // if the path is to the `workflow.knime` instead of the directory, go one level up
        final int last = pathSegments.size() - 1;
        if (last >= 0 && pathSegments.get(last).equals("workflow.knime")) {
            pathSegments.remove(last);
        }

        try {
            // append workflow path (e.g., /SomeWorkflow) to mount point URI (e.g., knime://LOCAL)
            return Optional.of(new URIBuilder(mountpoint.getFirst()).setPathSegments(pathSegments).build().normalize());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid knime URI, cannot determine mountpoint URI for " +
                    mountpoint.getFirst() + " and path " + relPath, ex);
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        final LocalLocationInfo that = (LocalLocationInfo) other;
        return Objects.equals(getSourceArchive(), that.getSourceArchive());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), m_sourceArchive);
    }

    @Override
    void addFields(final StringBuilder sb, final int indent) {
        super.addFields(sb, indent);
        final var init = "  ".repeat(indent);
        sb.append(init).append("sourceArchive=").append(m_sourceArchive).append("\n");
    }
}
