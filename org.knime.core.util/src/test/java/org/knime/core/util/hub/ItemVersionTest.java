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
 *   18 Mar 2025 (Manuel Hotz, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.util.hub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ItemVersion} creation and handling.
 *
 * @author Manuel Hotz, KNIME GmbH, Konstanz, Germany
 */
final class ItemVersionTest {

    @SuppressWarnings("static-method")
    @Test
    void testSpecificVersionArguments() {
        final var versionNumber = -1;
        final var thrown = assertThrows(IllegalArgumentException.class, () -> new SpecificVersion(versionNumber),
            "Expected to reject negative version number");
        assertTrue(thrown.getMessage().contains("" + versionNumber), "Expected exception to contain version number");
        assertEquals(0, new SpecificVersion(0).version(), "Expected version number to be 0");
        assertEquals(1, ItemVersion.of(1).version(), "Expected version number to be 1");
    }

    @SuppressWarnings("static-method")
    @Test
    void testVersionMatch() {
        final var current = ItemVersion.currentState();
        final var matchedCurrent = current.match(CurrentState::getInstance,
            () -> fail("Unexpected most-recent version"), sv -> fail("Unexpected specific version: " + sv));
        assertEquals(CurrentState.getInstance(), matchedCurrent, "Expected to match current state record");
        assertFalse(current.isVersioned(), "Expected unversioned");

        final var mostRecent = ItemVersion.mostRecent();
        final var matchedMostRecent = mostRecent.match(() -> fail("Unexpected current-state"), MostRecent::getInstance,
            sv -> fail("Unexpected specific version: " + sv));
        assertEquals(MostRecent.getInstance(), matchedMostRecent, "Expected to match most recent record");
        assertTrue(mostRecent.isVersioned(), "Expected is versioned");

        final var specific = new SpecificVersion(42);
        final var matchedSpecific = specific.match(() -> fail("Unexpected current-state"),
            () -> fail("Unexpected most-recent version"), sv -> sv);
        assertEquals(42, matchedSpecific, "Expected to match specific version record");
        assertEquals("42", new SpecificVersion(matchedSpecific).getVersionString(),
            "Expected version string to be \"42\"");
        assertTrue(specific.isVersioned(), "Expected is versioned");
    }

    @SuppressWarnings("static-method")
    @Test
    void testCast() {
        // simple test for the "marker method"
        assertEquals(ItemVersion.currentState(), ItemVersion.currentState().cast(CurrentState.class),
            "Expected to cast to CurrentState");
    }

    @SuppressWarnings("static-method")
    @Test
    void testIdentifiers() {
        assertEquals("most-recent", MostRecent.getIdentifier(),
            "Expected \"most-recent\" for \"most-recent\" version record");
        assertEquals("current-state", CurrentState.getIdentifier(),
            "Expected \"current-state\" for \"current-state\" version record");
    }

}
