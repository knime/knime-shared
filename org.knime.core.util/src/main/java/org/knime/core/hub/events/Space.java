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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used in Hub Trigger Events to describe the space that in which a repository change occurred.
 *
 * @since 5.23 on AP 5.0 releases; in AP 5.1 releases onwards the version has been bumped to 5.24
 * @noreference Non-public API. This type is not intended to be referenced by clients.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public class Space {
    private final String m_id;

    private final Optional<String> m_name;

    private final String m_owner;

    private final String m_ownerAccountId;

    private final boolean m_isPrivate;

    /**
     * @param id see {@link #getId()}
     * @param name see {@link #getName()}
     * @param owner see {@link #getOwner()}
     * @param ownerAccountId see {@link #getOwnerAccountId()}
     * @param isPrivate see {@link #isPrivate()}
     */
    @JsonCreator
    Space(@JsonProperty("spaceId") final String id, @JsonProperty("spaceName") final String name,
        @JsonProperty("owner") final String owner, @JsonProperty("ownerAccountId") final String ownerAccountId,
        @JsonProperty("private") final boolean isPrivate) {

        this.m_id = id;
        this.m_name = Optional.ofNullable(name);
        this.m_owner = owner;
        this.m_ownerAccountId = ownerAccountId;
        this.m_isPrivate = isPrivate;
    }

    /** @return the identifier of the space, e.g., <code>*2zwDuYFgpLVveXfX</code> */
    @JsonProperty("spaceId")
    public String getId() {
        return m_id;
    }

    /** @return the name of the space */
    @JsonProperty("spaceName")
    public Optional<String> getName() {
        return m_name;
    }

    /** @return account name that owns the space */
    @JsonProperty("owner")
    public String getOwner() {
        return m_owner;
    }

    /** @return identifier of the account that owns the space */
    @JsonProperty("ownerAccountId")
    public String getOwnerAccountId() {
        return m_ownerAccountId;
    }

    /**
     * @return true if the space is a private space, false if the space is a public space.
     */
    @JsonProperty("private")
    public boolean isPrivate() {
        return m_isPrivate;
    }

}
