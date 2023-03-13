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

import javax.json.JsonObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Creates {@link HubTriggerEvent} instances from {@link JsonObject} instances following this form:
 *
 * <pre>{@code
{
    "schemaVersion": 1,
    "timestamp": "2023-02-27T10:10:47Z",
    "source": "catalog",
    "type": "item",
    "action" : "added",
    "eventId": "5b0799db-7620-499b-b305-62d228ca5662",
    "deploymentId": "some-deployment-id",
    "subject": {
      "id": "some-item-id",
      "type": "Workflow",
      "path": "/Users/some-team/some-space/MyTestWorkflow",
      "canonicalPath": "/Users/account:team:some-team-id/some-space/MyTestWorkflow",
      "spacePath": "/MyTestWorkflow",
      "itemVersionCreatedBy": "some user",
      "itemVersionCreatedByAccountId": "account:user:some-id",
      "space": {
        "owner": "some-team",
        "ownerAccountId": "account:team:some-team-id",
        "private": true,
        "spaceId": "some-space-id",
        "spaceName": "some-space"
      }
    }
  }
}}
 * </pre>
 *
 * @since 5.23 on AP 5.0 releases; in AP 5.1 releases onwards the version has been bumped to 5.24
 * @noreference Non-public API. This type is not intended to be referenced by clients.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public final class HubRepositoryTriggerEventMapper {

    /** For parsing string representations of hub repository trigger events. */
    public static final ObjectMapper MAPPER = JsonMapper.builder()//
        .addModule(new Jdk8Module())//
        .addModule(new JavaTimeModule())//
        .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)//
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)//
        .build();

    static final TypeReference<HubTriggerEvent<HubRepositoryItemEventSubject>> TARGET_TYPE =
        new TypeReference<HubTriggerEvent<HubRepositoryItemEventSubject>>() {};

    private HubRepositoryTriggerEventMapper() {
    }

    /**
     * @param event data
     * @return parsed data
     * @throws IllegalArgumentException if the data cannot be parsed.
     */
    public static HubTriggerEvent<HubRepositoryItemEventSubject> parse(final JsonObject event)
        throws IllegalArgumentException {
        try {
            return MAPPER.readValue(event.toString(), TARGET_TYPE);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
