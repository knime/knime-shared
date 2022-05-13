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
 *   13 May 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.text.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.knime.shared.workflow.def.impl.WorkflowDefBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class DefClipboardContentTest {

    /**
     * Test Jackson serialization and deserialization keeps all metadata.
     * @throws JsonProcessingException
     */
    @Test
    void testRoundtrip() throws JsonProcessingException {
        // content
        final var payloadIdentifier = new UUID(1, 2);
        final var payload = new WorkflowDefBuilder().build();
        DefClipboardContent content = new DefClipboardContent(payloadIdentifier, payload);

        // serialize
        String serialized = ObjectMapperUtil.toString(content);

        assertThat(serialized)//
            // don't make assumptions on the payload structure (e.g., workflow def changes shouldn't let this test fail)
            .contains("\"payloadIdentifier\":\"00000000-0000-0001-0000-000000000002\"")//
            .contains("\"version\":\"" + DefClipboardContent.Version.latest().toString() + "\"");

        // deserialize
        DefClipboardContent deserialized = DefClipboardContent.valueOf(serialized).get();

        assertThat(deserialized.getVersion()).isEqualTo(DefClipboardContent.Version.latest());
        assertThat(deserialized.getPayloadIdentifier()).isEqualTo(payloadIdentifier);
        assertThat(deserialized.getPayload()).isEqualTo(payload);
    }

    /**
     * Attempt to parse future/unknown versions will fail.
     * Exceptions can be accessed by using <code>ObjectMapperUtil.fromString(string, DefClipboardContent.class)</code>.
     */
    @Test
    void testDeserializeUnknownVersionContent() {
        assertThat(DefClipboardContent.valueOf("{\"version\": \"bogus_version!@\", "
            + "\"payload\": {}, "
            + "\"payloadIdentifier\": \"00000000-0000-0001-0000-000000000002\"}")).isEmpty();
    }

    /**
     * Utility method for determining clipboard contents.
     */
    @Test
    void testDeserializeOtherContent() {
        assertThat(DefClipboardContent.valueOf(null)).isEmpty();
        assertThat(DefClipboardContent.valueOf("{\"bogus\": 2}")).isEmpty();
        assertThat(DefClipboardContent.valueOf("bogus content")).isEmpty();
    }

    /**
     * Safeguard check to remind developers to add backwards compatibility if new fields are added.
     */
    @Test
    void testSchemaUnchanged() {
        assertThat(DefClipboardContent.class.getDeclaredFields())//
            .extracting(Field::getName)//
            .containsOnly("m_payloadIdentifier", "m_payload", "m_version");
    }

}
