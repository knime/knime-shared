/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME AG, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 *
 * History
 *   Created on 13 Mar 2023 by carlwitt
 */
package org.knime.core.hub.events;

import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;
import org.knime.core.node.dialog.ContentType;
import org.knime.core.node.dialog.ExternalNodeData;

/**
 * Defines the classes of KNIME Hub trigger events.
 *
 * @since 5.22 on AP 4.7 releases; in AP 5.0 releases onwards the version has been bumped to 5.24
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
