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

import java.util.Optional;

import org.knime.core.hub.events.HubTriggerEvent.HubEventSubject;
import org.knime.core.node.util.CheckUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Holds KNIME Hub trigger event data. Describes what was changed in a repository change.
 *
 * @since 5.22 on AP 4.7 releases; in AP 5.0 releases onwards the version has been bumped to 5.24
 * @noreference Non-public API. This type is not intended to be referenced by clients.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
@JsonDeserialize(builder = HubRepositoryItemEventSubject.Builder.class)
public final class HubRepositoryItemEventSubject implements HubEventSubject {

    private final String m_itemId;

    private final String m_repositoryItemType;

    private final String m_path;

    private final String m_canonicalPath;

    private final String m_spacePath;

    private final Optional<String> m_itemVersionCreatedBy;

    private final Optional<String> m_itemVersionCreatedByAccountId;

    private final Space m_space;

    /**
     * @param id see {@link #id()}
     * @param type see {@link #type()}
     * @param path see {@link #path()}
     * @param canonicalPath see {@link #canonicalPath()}
     * @param spacePath see {@link #spacePath()}
     * @param space see {@link #getSpace()}
     */
    private HubRepositoryItemEventSubject(final String id, final String type, final String path,
        final String canonicalPath, final String spacePath, final String itemVersionCreatedBy,
        final String itemVersionCreatedByAccountId, final Space space) {

        this.m_itemId = id;
        this.m_repositoryItemType = type;
        this.m_path = path;
        this.m_canonicalPath = canonicalPath;
        this.m_spacePath = spacePath;
        this.m_itemVersionCreatedBy = Optional.ofNullable(itemVersionCreatedBy);
        this.m_itemVersionCreatedByAccountId = Optional.ofNullable(itemVersionCreatedByAccountId);
        this.m_space = space;
    }

    /**
     * @return identifier of the item in the repository, e.g., <code>*aEPsAXPUrBLHGFhx</code>
     */
    @JsonProperty("id")
    public String getItemId() {
        return m_itemId;
    }

    /**
     * @return kind of the repository item, e.g., "Workflow" or "WorkflowGroup"
     */
    @JsonProperty("type")
    public String getType() {
        return m_repositoryItemType;
    }

    /**
     * @return For instance <code>/Users/thor/MySpace/KNIME_project2</code>
     */
    @JsonProperty("path")
    public String getPath() {
        return m_path;
    }

    /**
     * @return For instance <code>/Users/account:user:e9fc3d3f-fd88-4f61-b7d9-9a89fc5b037d/MySpace/KNIME_project2</code>
     */
    @JsonProperty("canonicalPath")
    public String getCanonicalPath() {
        return m_canonicalPath;
    }

    /**
     * @return path relative to the space's root directory, e.g., <code>/KNIME_project2</code>
     */
    @JsonProperty("spacePath")
    public String getSpacePath() {
        return m_spacePath;
    }

    /**
     * @return account name that triggered the event
     */
    @JsonProperty("itemVersionCreatedBy")
    public Optional<String> getItemVersionCreatedBy() {
        return m_itemVersionCreatedBy;
    }

    /**
     * @return account id that triggered the event
     */
    @JsonProperty("itemVersionCreatedByAccountId")
    public Optional<String> getItemVersionCreatedByAccountId() {
        return m_itemVersionCreatedByAccountId;
    }

    /**
     * @return details about the space the item belongs to
     */
    @JsonProperty("space")
    public Space getSpace() {
        return m_space;
    }

    /** @return builder */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Standard builder pattern.
     */
    @JsonPOJOBuilder(withPrefix = "", buildMethodName = "buildSubject")
    public static class Builder extends HubTriggerEvent.EventBuilder<HubRepositoryItemEventSubject, Builder> {
        private String m_itemId;

        private String m_repositoryItemType;

        private String m_path;

        private String m_canonicalPath;

        private String m_spacePath;

        private String m_itemVersionCreatedBy;

        private String m_itemVersionCreatedByAccountId;

        private String m_spaceId;

        private String m_spaceName;

        private String m_spaceOwner;

        private String m_spaceOwnerAccountId;

        private boolean m_spaceIsPrivate;

        Builder() {
            super(1, "catalog", "item");
        }

        /**
         * @param itemId see {@link #getItemId()}
         * @return this builder
         */
        @JsonProperty("id")
        public Builder itemId(final String itemId) {
            m_itemId = itemId;
            return this;
        }

        /**
         * @param repositoryItemType the type as used in the json representation, e.g., "Workflow". Not null. See
         *            {@link #getType()}.
         * @return this builder
         */
        @JsonProperty("type")
        public Builder repositoryItemType(final String repositoryItemType) {
            CheckUtils.checkArgumentNotNull(repositoryItemType);
            m_repositoryItemType = repositoryItemType;
            return this;
        }

        /**
         * @param path see {@link #getPath()}
         * @return this builder
         */
        public Builder path(final String path) {
            m_path = path;
            return this;
        }

        /**
         * @param canonicalPath see {@link #getCanonicalPath()}
         * @return this builder
         */
        public Builder canonicalPath(final String canonicalPath) {
            m_canonicalPath = canonicalPath;
            return this;
        }

        /**
         * @param spacePath see {@link #getSpacePath()}
         * @return this builder
         */
        public Builder spacePath(final String spacePath) {
            m_spacePath = spacePath;
            return this;
        }

        /**
         * @param user see {@link #getItemVersionCreatedBy()}
         * @return this builder
         */
        public Builder itemVersionCreatedBy(final String user) {
            m_itemVersionCreatedBy = user;
            return this;
        }

        /**
         * @param accountId see {@link #getItemVersionCreatedByAccountId()}
         * @return this builder
         */
        public Builder itemVersionCreatedByAccountId(final String accountId) {
            m_itemVersionCreatedByAccountId = accountId;
            return this;
        }

        /**
         * @param space see {@link HubRepositoryItemEventSubject#getSpace()}
         * @return this builder
         */
        public Builder space(final Space space) {
            m_spaceId = space.getId();
            m_spaceName = space.getName().orElse(null);
            m_spaceOwner = space.getOwner();
            m_spaceOwnerAccountId = space.getOwnerAccountId();
            m_spaceIsPrivate = space.isPrivate();
            return this;
        }

        /**
         * @param spaceId see {@link Space#getId()}
         * @return this builder
         */
        public Builder spaceId(final String spaceId) {
            m_spaceId = spaceId;
            return this;
        }

        /**
         * @param spaceName see {@link Space#getName()}
         * @return this builder
         */
        public Builder spaceName(final String spaceName) {
            m_spaceName = spaceName;
            return this;
        }

        /**
         * @param spaceOwner see {@link Space#getOwner()}
         * @return this builder
         */
        public Builder spaceOwner(final String spaceOwner) {
            m_spaceOwner = spaceOwner;
            return this;
        }

        /**
         * @param spaceOwnerAccountId see {@link Space#getOwnerAccountId()}
         * @return this builder
         */
        public Builder spaceOwnerAccountId(final String spaceOwnerAccountId) {
            m_spaceOwnerAccountId = spaceOwnerAccountId;
            return this;
        }

        /**
         * @param spaceIsPrivate see {@link Space#isPrivate()}
         * @return this builder
        */
        public Builder spaceIsPrivate(final boolean spaceIsPrivate) {
            m_spaceIsPrivate = spaceIsPrivate;
            return this;
        }

        @Override
        public HubTriggerEvent<HubRepositoryItemEventSubject> build() {
            super.subject(buildSubject());
            return super.build();
        }

        /**
         * @return the event subject (event type specific data). This is used only for jackson deserialization.
         */
        public HubRepositoryItemEventSubject buildSubject() {
            final var space = new Space(m_spaceId, m_spaceName, m_spaceOwner, m_spaceOwnerAccountId, m_spaceIsPrivate);
            return new HubRepositoryItemEventSubject(m_itemId, m_repositoryItemType, m_path,
                m_canonicalPath, m_spacePath, m_itemVersionCreatedBy, m_itemVersionCreatedByAccountId, space);
        }
    }
}
