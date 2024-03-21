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
 *   20 Mar 2023 (carlwitt): created
 */
package org.knime.core.hub.events;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.StringReader;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Test that parsing the string representation of a JsonObject holding KNIME Hub trigger event data works.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class HubRepositoryTriggerEventMapperTest {

    private static final String PREFIX = """
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
                """;

    private static final String VERSION = """
            "itemVersion": "123",
            """;

    private static final String SUFFIX = """
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
            }""";

    private static final String JSON = PREFIX + VERSION + SUFFIX;

    private static final String JSON_WITHOUT_ITEM_VERSION = PREFIX + SUFFIX;

    private static JsonObject toJsonObject(final String json) {
        try (var jsonReader = Json.createReader(new StringReader(json))) {
            return jsonReader.readObject();
        }
    }

    @Test
    void test() {
        var eventData = HubRepositoryTriggerEventMapper.parse(toJsonObject(JSON));
        assertThat(eventData.getSchemaVersion()).as("Wrong schema version.").isEqualTo(1);
        assertThat(eventData.getEventSource()).as("Wrong source.").isEqualTo("catalog");
        assertThat(eventData.getEventType()).as("Wrong type.").isEqualTo("item");
        assertThat(eventData.getAction()).as("Wrong action.").isEqualTo("added");
        assertThat(eventData.getEventId().toString()).as("Wrong event id.")
            .isEqualTo("5b0799db-7620-499b-b305-62d228ca5662");
        assertThat(eventData.getDeploymentId()).as("Wrong deployment id.").isEqualTo("some-deployment-id");
        assertThat(eventData.getTimestamp()).as("Wrong timestamp.")
            .isEqualTo(ZonedDateTime.parse("2023-02-27T10:10:47Z[UTC]"));
        assertThat(eventData.getSubject().getItemId()).as("Wrong subject id.").isEqualTo("some-item-id");
        assertThat(eventData.getSubject().getType()).as("Wrong subject type.").isEqualTo("Workflow");
        assertThat(eventData.getSubject().getPath()).as("Wrong subject path.")
            .isEqualTo("/Users/some-team/some-space/MyTestWorkflow");
        assertThat(eventData.getSubject().getCanonicalPath()).as("Wrong subject canonical path.")
            .isEqualTo("/Users/account:team:some-team-id/some-space/MyTestWorkflow");
        assertThat(eventData.getSubject().getSpacePath()).as("Wrong subject space path.").isEqualTo("/MyTestWorkflow");
        assertThat(eventData.getSubject().getItemVersion()).isPresent().get().as("Wrong item version.")
            .isEqualTo("123");
        assertThat(eventData.getSubject().getItemVersionCreatedBy()).isPresent().get()
            .as("Wrong subject item version created by.").isEqualTo("some user");
        assertThat(eventData.getSubject().getItemVersionCreatedByAccountId()).isPresent().get()
            .as("Wrong subject item version created by account id.").isEqualTo("account:user:some-id");
        assertThat(eventData.getSubject().getSpace().getOwner()).as("Wrong subject space owner.")
            .isEqualTo("some-team");
        assertThat(eventData.getSubject().getSpace().getOwnerAccountId()).as("Wrong subject space owner account id.")
            .isEqualTo("account:team:some-team-id");
        assertThat(eventData.getSubject().getSpace().isPrivate()).as("Wrong subject space is private.").isTrue();
        assertThat(eventData.getSubject().getSpace().getId()).as("Wrong subject space id.").isEqualTo("some-space-id");
        assertThat(eventData.getSubject().getSpace().getName()).isPresent().get().as("Wrong subject space name.")
            .isEqualTo("some-space");
    }

    @Test
    void testParseEventWithoutItemVersion() {
        var eventData = HubRepositoryTriggerEventMapper.parse(toJsonObject(JSON_WITHOUT_ITEM_VERSION));
        assertThat(eventData.getSchemaVersion()).as("Wrong schema version.").isEqualTo(1);
        assertThat(eventData.getEventSource()).as("Wrong source.").isEqualTo("catalog");
        assertThat(eventData.getEventType()).as("Wrong type.").isEqualTo("item");
        assertThat(eventData.getAction()).as("Wrong action.").isEqualTo("added");
        assertThat(eventData.getEventId().toString()).as("Wrong event id.")
            .isEqualTo("5b0799db-7620-499b-b305-62d228ca5662");
        assertThat(eventData.getDeploymentId()).as("Wrong deployment id.").isEqualTo("some-deployment-id");
        assertThat(eventData.getTimestamp()).as("Wrong timestamp.")
            .isEqualTo(ZonedDateTime.parse("2023-02-27T10:10:47Z[UTC]"));
        assertThat(eventData.getSubject().getItemId()).as("Wrong subject id.").isEqualTo("some-item-id");
        assertThat(eventData.getSubject().getType()).as("Wrong subject type.").isEqualTo("Workflow");
        assertThat(eventData.getSubject().getPath()).as("Wrong subject path.")
            .isEqualTo("/Users/some-team/some-space/MyTestWorkflow");
        assertThat(eventData.getSubject().getCanonicalPath()).as("Wrong subject canonical path.")
            .isEqualTo("/Users/account:team:some-team-id/some-space/MyTestWorkflow");
        assertThat(eventData.getSubject().getSpacePath()).as("Wrong subject space path.").isEqualTo("/MyTestWorkflow");
        assertThat(eventData.getSubject().getItemVersion()).as("Item version not empty.").isEmpty();
        assertThat(eventData.getSubject().getItemVersionCreatedBy()).isPresent().get()
            .as("Wrong subject item version created by.").isEqualTo("some user");
        assertThat(eventData.getSubject().getItemVersionCreatedByAccountId()).isPresent().get()
            .as("Wrong subject item version created by account id.").isEqualTo("account:user:some-id");
        assertThat(eventData.getSubject().getSpace().getOwner()).as("Wrong subject space owner.")
            .isEqualTo("some-team");
        assertThat(eventData.getSubject().getSpace().getOwnerAccountId()).as("Wrong subject space owner account id.")
            .isEqualTo("account:team:some-team-id");
        assertThat(eventData.getSubject().getSpace().isPrivate()).as("Wrong subject space is private.").isTrue();
        assertThat(eventData.getSubject().getSpace().getId()).as("Wrong subject space id.").isEqualTo("some-space-id");
        assertThat(eventData.getSubject().getSpace().getName()).isPresent().get().as("Wrong subject space name.")
            .isEqualTo("some-space");
    }

    /**
     * Does not fail on new fields.
     */
    @Test
    void testIngoreUnknownFields() {
        final var json = "{ \"schemaVersion\": 1, \"newField\": \"in subject\" }";
        assertDoesNotThrow(() -> HubRepositoryTriggerEventMapper.parse(toJsonObject(json)), "Fails on unknown fields");
    }
}
