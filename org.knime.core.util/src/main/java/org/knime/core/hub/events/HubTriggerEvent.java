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
 *   13 Mar 2023 (carlwitt): created
 */
package org.knime.core.hub.events;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.knime.core.hub.events.HubTriggerEvent.HubEventSubject;
import org.knime.core.node.util.CheckUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Holds generic KNIME Hub trigger event data. The type parameter allows
 *
 * @since 5.24
 * @noreference Non-public API. This type is not intended to be referenced by clients.
 *
 * @param <T> subject of this event
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
@JsonDeserialize(builder = HubTriggerEvent.EventBuilder.class)
public final class HubTriggerEvent<T extends HubEventSubject> {

    private int m_schemaVersion;

    private String m_eventSource;

    private String m_eventType;

    private ZonedDateTime m_datetime;

    private String m_action;

    private UUID m_eventId;

    private String m_deploymentId;

    private T m_subject;

    /** Hub event subjects **/
    public interface HubEventSubject {

    }

    /**
     *
     * @param datetime see {@link #datetime()}
     * @param space see {@link #space()}
     * @param item see {@link #subject()}
     * @param action see {@link #action()}
     */
    private HubTriggerEvent(final int schemaVersion, final String eventSource, final String eventType,
        final ZonedDateTime datetime, final String action, final UUID eventId, final String deploymentId,
        final T subject) {

        m_schemaVersion = schemaVersion;
        m_eventSource = eventSource;
        m_eventType = eventType;
        m_datetime = datetime;
        m_action = action;
        m_eventId = eventId;
        m_deploymentId = deploymentId;
        m_subject = subject;
    }

    /** @return the schema version */
    @JsonProperty("schemaVersion")
    public int getSchemaVersion() {
        return m_schemaVersion;
    }

    /** @return ISO formatted date & time */
    @JsonProperty("timestamp")
    public ZonedDateTime getTimestamp() {
        return m_datetime;
    }

    /** @return the event source */
    @JsonProperty("source")
    public String getEventSource() {
        return m_eventSource;
    }

    /** @return the event type */
    @JsonProperty("type")
    public String getEventType() {
        return m_eventType;
    }

    /** @return how the repository item was changed */
    @JsonProperty("action")
    public String getAction() {
        return m_action;
    }

    /** @return the event identifier */
    @JsonProperty("eventId")
    public UUID getEventId() {
        return m_eventId;
    }

    /** @return the deployment identifier */
    @JsonProperty("deploymentId")
    public String getDeploymentId() {
        return m_deploymentId;
    }

    /** @return type of the repository item that was changed */
    @JsonProperty("subject")
    public T getSubject() {
        return m_subject;
    }

    @JsonPOJOBuilder(withPrefix = "")
    static class EventBuilder<T extends HubEventSubject, B extends EventBuilder<T,B>> {

        private int m_schemaVersion = -1;

        private String m_eventSource;

        private String m_eventType;

        private ZonedDateTime m_datetime;

        private String m_action;

        private UUID m_eventId;

        private String m_deploymentId;

        private T m_subject;

        EventBuilder() {
        }

        EventBuilder(final int schemaVersion, final String eventSource, final String eventType) {
            m_schemaVersion = schemaVersion;
            m_eventSource = eventSource;
            m_eventType = eventType;
        }

        /**
         * @param dateTime of the change to the repository
         * @return this builder
         */
        @JsonProperty("timestamp")
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B dateTime(final ZonedDateTime dateTime) {
            m_datetime = dateTime;
            return (B)this;
        }

        /**
         * @param action type of change, e.g., "added", "deleted", "updated"
         * @return this builder
         */
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B action(final String action) {
            m_action = action;
            return (B)this;
        }

        /**
         * @param eventId
         * @return this builder
         */
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B eventId(final UUID eventId) {
            m_eventId = eventId;
            return (B)this;
        }

        /**
         * @param deploymentId of the deployment that is triggered
         * @return this builder
         */
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B deploymentId(final String deploymentId) {
            m_deploymentId = deploymentId;
            return (B)this;
        }

        /**
         * @param schemaVersion of the event message schema
         * @return this builder
         */
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B schemaVersion(final int schemaVersion) {
            m_schemaVersion = schemaVersion;
            return (B)this;
        }

        /**
         * @param eventSource origin of the event, e.g., "catalog"
         * @return this builder
         */
        @JsonProperty("source")
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B eventSource(final String eventSource) {
            m_eventSource = eventSource;
            return (B)this;
        }

        /**
         * @param eventType kind of the event, e.g., "item"
         * @return this builder
         */
        @JsonProperty("type")
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B eventType(final String eventType) {
            m_eventType = eventType;
            return (B)this;
        }

        /**
         * @param subject the event type specific data, e.g., for the changed item for a repository event
         * @return this builder
         */
        @SuppressWarnings("unchecked") // we want to return the specialized builder
        public B subject(final T subject) {
            m_subject = subject;
            return (B)this;
        }

        HubTriggerEvent<T> build() {
            CheckUtils.checkState(m_schemaVersion > 0, "No valid schema version was set.");
            return new HubTriggerEvent<>(m_schemaVersion, m_eventSource, m_eventType, m_datetime, m_action, m_eventId,
                m_deploymentId, m_subject);
        }
    }
}
