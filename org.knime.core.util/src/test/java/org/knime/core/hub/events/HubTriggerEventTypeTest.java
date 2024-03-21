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
 *   22 Mar 2023 (carlwitt): created
 */
package org.knime.core.hub.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class HubTriggerEventTypeTest {

    /**
     * Test method for {@link org.knime.core.hub.events.HubTriggerEventType#isHubEventContentType(java.lang.String)}.
     */
    @Test
    void testIsHubEventContentType() {
        assertThat(HubTriggerEventType.isHubEventContentType("knime-hub-event/repository"))
            .as("Known event type not recognized.").isTrue();
        assertThat(HubTriggerEventType.values()).as(
            "Double check HubTriggerEventType.isHubEventContentType implementation, new enum values have been added.")
            .hasSize(1);

        assertThat(HubTriggerEventType.isHubEventContentType(null)).as("Null is not a hub event content type.")
            .isFalse();
        assertThat(HubTriggerEventType.isHubEventContentType("some-other-prefix/subtype"))
            .as("Bogus content type is not a hub event content type.").isFalse();
    }

    /**
     * Test method for {@link org.knime.core.hub.events.HubTriggerEventType#getHubEventContentType(java.lang.String)}.
     */
    @Test
    void testGetHubEventContentType() {
        assertThat(HubTriggerEventType.getHubEventContentType("knime-hub-event/repository")).as("Parsing did not work.")
            .isEqualTo(HubTriggerEventType.REPOSITORY);
    }

    /**
     * Fail if the reserved parameter name is changed.
     */
    @Test
    void testRepositoryParamterNameUnchanged() {
        assertThat(HubTriggerEventType.REPOSITORY.getReservedParameterName())
            .as("The reserved parameter name has been changed.").isEqualTo("knime.hub.trigger.repository");
    }

}
