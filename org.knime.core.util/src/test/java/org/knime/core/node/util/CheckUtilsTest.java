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
 *   15 Nov 2022 ("Manuel Hotz &lt;manuel.hotz@knime.com&gt;"): created
 */
package org.knime.core.node.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests new {@link CheckUtils} methods.
 *
 * @author Manuel Hotz, KNIME GmbH, Konstanz, Germany
 */
class CheckUtilsTest {

    /**
     * Tests the {@code CheckUtils#checkIsInstance} method.
     */
    @Test
    final void testCheckCast() {
        final Object obj = "testIsInstance";
        final String ret1 = CheckUtils.checkCast(obj, String.class, IllegalArgumentException::new,
            "Given object '%s' is not a string.", obj);
        assertThat(obj).as("Should result in equal objects").isEqualTo(ret1);

        final Object notAString = 42L;
        assertThrows(Exception.class, () -> CheckUtils.checkCast(notAString, String.class, Exception::new,
            "Object %s is not a string.", notAString), "Expected to not be a string");
    }

    /**
     * Tests the {@code CheckUtils#check} method.
     */
    @Test
    final void testCheck() {
        final Object obj = "testCheck";
        CheckUtils.check(obj instanceof String, IllegalArgumentException::new,
            () -> String.format("Object %s is not a string.", obj));

        assertThrows(Exception.class, () -> CheckUtils.check(false, Exception::new, () -> "Should throw this"),
            "Expected to throw");
    }
}
