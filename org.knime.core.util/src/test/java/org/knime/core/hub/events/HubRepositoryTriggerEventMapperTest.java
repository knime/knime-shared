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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Test that parsing the string representation of a JsonObject holding KNIME Hub trigger event data works.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public class HubRepositoryTriggerEventMapperTest {

    private static final String JSON = "{\n"
        + "    \"schemaVersion\": 1,\n"
        + "    \"timestamp\": \"2023-03-21T11:37:03.08798149Z[GMT]\",\n"
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
    public void test() throws JsonProcessingException {
        var eventData = HubRepositoryTriggerEventMapper.parse(object);
        assertEquals("Wrong schema version.", 1, eventData.getSchemaVersion());
        assertEquals("Wrong timestamp.", "2023-03-21T11:37:03.087981490Z",
            eventData.getTimestamp().toOffsetDateTime().toString());
        assertEquals("Wrong source.", "catalog", eventData.getEventSource());
        assertEquals("Wrong type.", "item", eventData.getEventType());
        assertEquals("Wrong action.", "added", eventData.getAction());
        assertEquals("Wrong event id.", UUID.fromString("5b0799db-7620-499b-b305-62d228ca5662"),
            eventData.getEventId());
        assertEquals("Wrong deployment id.", "some-deployment-id", eventData.getDeploymentId());
        assertEquals("Wrong subject id.", "some-item-id", eventData.getSubject().getItemId());
        assertEquals("Wrong subject type.", "Workflow", eventData.getSubject().getType());
        assertEquals("Wrong subject path.", "/Users/some-team/some-space/MyTestWorkflow",
            eventData.getSubject().getPath());
        assertEquals("Wrong subject canonical path.", "/Users/account:team:some-team-id/some-space/MyTestWorkflow",
            eventData.getSubject().getCanonicalPath());
        assertEquals("Wrong subject space path.", "/MyTestWorkflow", eventData.getSubject().getSpacePath());
        assertEquals("Wrong subject item version created by.", "some user",
            eventData.getSubject().getItemVersionCreatedBy().get());
        assertEquals("Wrong subject item version created by account id.", "account:user:some-id",
            eventData.getSubject().getItemVersionCreatedByAccountId().get());
        assertEquals("Wrong subject space owner.", "some-team", eventData.getSubject().getSpace().getOwner());
        assertEquals("Wrong subject space owner account id.", "account:team:some-team-id",
            eventData.getSubject().getSpace().getOwnerAccountId());
        assertTrue("Wrong subject space private.", eventData.getSubject().getSpace().isPrivate());
        assertEquals("Wrong subject space id.", "some-space-id", eventData.getSubject().getSpace().getId());
        assertEquals("Wrong subject space name.", "some-space", eventData.getSubject().getSpace().getName().get());
    }

}
