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

import org.knime.core.node.workflow.contextv2.WorkflowContextV2.LocationType;
import org.knime.core.util.Pair;

/**
 * Base class of {@link LocationInfo}s. A {@link LocationInfo} provides information about where the current
 * workflow currently resides (is stored).
 *
 * @author Bjoern Lohrmann, KNIME GmbH
 * @noreference non-public API
 * @noinstantiate non-public API
 */
public abstract class LocationInfo {

    private final LocationType m_type;


    LocationInfo(final LocationType type) {
        m_type = type;
    }

    /**
     *
     * @return the {@link LocationType} where the current workflow resides/is stored.
     */
    public LocationType getType() {
        return m_type;
    }

    abstract Optional<URI> mountpointURI(Pair<URI, Path> mountpoint, Path localWorkflowPath);

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    @Override
    public String toString() {
        return toString(new StringBuilder(), 0).toString();
    }

    /**
     * Adds a string representation of this location info to the given string builder.
     *
     * @param sb string builder to add to
     * @param indent indentation level
     */
    final StringBuilder toString(final StringBuilder sb, final int indent) {
        final var init = "  ".repeat(indent);
        sb.append(init).append(getClass().getSimpleName()).append("[\n");
        addFields(sb, indent + 1);
        return sb.append(init).append("]\n");
    }

    void addFields(final StringBuilder sb, final int indent) {
        final var init = "  ".repeat(indent);
        sb.append(init).append("type=").append(m_type).append("\n");
    }
}
