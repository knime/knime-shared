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
 */
package org.knime.core.hub.events;

import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;
import org.knime.core.node.dialog.ContentType;
import org.knime.core.node.dialog.ExternalNodeData;

/**
 * Defines the classes of KNIME Hub trigger events.
 *
 * @since 5.24
 * @noreference Non-public API. This type is not intended to be referenced by clients.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public enum HubTriggerEventType {
        /** Denotes events indicating changes to a KNIME Hub repository, e.g., a workflow was uploaded. */
        REPOSITORY("knime.hub.trigger.repository");

    private final String m_reservedParameterName;

    /**
     * @param string
     */
    HubTriggerEventType(final String reservedParameterName) {
        m_reservedParameterName = reservedParameterName;
    }

    /**
     * @return the identifier of the hub event content type, for use in {@link ExternalNodeData#getContentType()}.
     */
    public String getContentType() {
        return ContentType.HUB_EVENT_CONTENT_TYPE_PREFIX + name().toLowerCase(Locale.ROOT);
    }

    /**
     * @return the InputNode parameter name that is reserved for nodes receiving this kind of hub trigger event data.
     */
    public String getReservedParameterName() {
        return m_reservedParameterName;
    }

    /**
     * Verify if given content type is a known event type.
     *
     * @param contentType content type to check
     * @return {@code true} if given content type is a {@link HubTriggerEventType}
     */
    public static boolean isHubEventContentType(@Nullable final String contentType) {
        return contentType != null && contentType.equals(REPOSITORY.getContentType());
    }

    /**
     * Get the event type from a given content type.
     *
     * @param contentType the content type with an event type
     * @return the event type or {@code null}
     */
    @Nullable
    public static HubTriggerEventType getHubEventContentType(@Nullable final String contentType) {
        if (contentType != null && contentType.equals(REPOSITORY.getContentType())) {
            return REPOSITORY;
        } else {
            return null;
        }
    }
}
