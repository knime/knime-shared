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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.time.ZonedDateTime;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Test that parsing the string representation of a JsonObject holding KNIME Hub trigger event data works.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class HubRepositoryTriggerEventMapperTest {

    private static final String JSON = "{\n"
        + "    \"schemaVersion\": 1,\n"
        + "    \"timestamp\": \"2023-02-27T10:10:47Z\",\n"
        + "    \"source\": \"catalog\",\n"
        + "    \"type\": \"item\",\n"
        + "    \"action\" : \"added\",\n"
        + "    \"eventId\": \"5b0799db-7620-499b-b305-62d228ca5662\",\n"
        + "    \"deploymentId\": \"some-deployment-id\",\n"
        + "    \"subject\": {\n"
        + "      \"id\": \"some-item-id\",\n"
        + "      \"type\": \"Workflow\",\n"
        + "      \"path\": \"/Users/some-team/some-space/MyTestWorkflow\",\n"
        + "      \"canonicalPath\": \"/Users/account:team:some-team-id/some-space/MyTestWorkflow\",\n"
        + "      \"spacePath\": \"/MyTestWorkflow\",\n"
        + "      \"itemVersionCreatedBy\": \"some user\",\n"
        + "      \"itemVersionCreatedByAccountId\": \"account:user:some-id\",\n"
        + "      \"space\": {\n"
        + "        \"owner\": \"some-team\",\n"
        + "        \"ownerAccountId\": \"account:team:some-team-id\",\n"
        + "        \"private\": true,\n"
        + "        \"spaceId\": \"some-space-id\",\n"
        + "        \"spaceName\": \"some-space\"\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "";

    private static JsonObject object;

    static {
        try(JsonReader jsonReader = Json.createReader(new StringReader(JSON))){
            object = jsonReader.readObject();
        }
    }

    @Test
    void test() throws JsonProcessingException {
        var eventData = HubRepositoryTriggerEventMapper.parse(object);
        assertEquals(1, eventData.getSchemaVersion(), "Wrong schema version.");
        assertEquals("catalog", eventData.getEventSource(), "Wrong source.");
        assertEquals("item", eventData.getEventType(), "Wrong type.");
        assertEquals("added", eventData.getAction(), "Wrong action.");
        assertEquals("5b0799db-7620-499b-b305-62d228ca5662", eventData.getEventId().toString(), "Wrong event id.");
        assertEquals("some-deployment-id", eventData.getDeploymentId(), "Wrong deployment id.");
        assertEquals(ZonedDateTime.parse("2023-02-27T10:10:47Z[UTC]"), eventData.getTimestamp(), "Wrong timestamp.");
        assertEquals("some-item-id", eventData.getSubject().getItemId(), "Wrong subject id.");
        assertEquals("Workflow", eventData.getSubject().getType(),
            "Wrong subject type.");
        assertEquals("/Users/some-team/some-space/MyTestWorkflow", eventData.getSubject().getPath(),
            "Wrong subject path.");
        assertEquals("/Users/account:team:some-team-id/some-space/MyTestWorkflow",
            eventData.getSubject().getCanonicalPath(), "Wrong subject canonical path.");
        assertEquals("/MyTestWorkflow", eventData.getSubject().getSpacePath(), "Wrong subject space path.");
        assertEquals("some user", eventData.getSubject().getItemVersionCreatedBy().get(),
            "Wrong subject item version created by.");
        assertEquals("account:user:some-id", eventData.getSubject().getItemVersionCreatedByAccountId().get(),
            "Wrong subject item version created by account id.");
        assertEquals("some-team", eventData.getSubject().getSpace().getOwner(), "Wrong subject space owner.");
        assertEquals("account:team:some-team-id", eventData.getSubject().getSpace().getOwnerAccountId(),
            "Wrong subject space owner account id.");
        assertTrue(eventData.getSubject().getSpace().isPrivate(), "Wrong subject space is private.");
        assertEquals("some-space-id", eventData.getSubject().getSpace().getId(), "Wrong subject space id.");
        assertEquals("some-space", eventData.getSubject().getSpace().getName().get(), "Wrong subject space name.");
    }

}
