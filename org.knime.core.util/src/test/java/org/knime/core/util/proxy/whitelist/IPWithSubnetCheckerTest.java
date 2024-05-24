/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *  and
 *  Copyright by Markus Bernhardt, Copyright 2016, and
 *  Copyright by Bernd Rosstauscher, Copyright 2009
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
 *   Jun 10, 2024 (lw): created
 */
package org.knime.core.util.proxy.whitelist;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link IPWithSubnetChecker}.
 * IP range examples are from proxy-vole tests, the test structure is different.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
class IPWithSubnetCheckerTest {

    /**
     * Asserts the validity of each in a list of IP ranges (given as {@link String}).
     *
     * @param ips list of string IP ranges
     * @param checker validator of IP ranges, either for IP4, or for IP6
     * @param expected whether the given ranges are valid
     */
    private static void assertIPRanges(final List<String> ips, final Predicate<String> checker,
        final boolean expected) {
        final var verb = expected ? "valid" : "invalid";
        final var message = "Is %s IP range but was not detected as such".formatted(verb);
        for (var ip : ips) {
            assertThat(checker.test(ip)).as(message).isEqualTo(expected);
        }
    }

    @Test
    void testIsValidIP4() {
        assertIPRanges(List.of( //
            "127.0.0.1/8", "127.0.0.1/32", "255.255.255.255/32", "0.0.0.0/0" //
        ), IPWithSubnetChecker::isValidIP4Range, true);

        assertIPRanges(List.of( //
            "127.0.0.1", "localhost", "http://knime.com", "knime.com", "400.400.400.400", "www.test.com/8", //
            "127.0.0.1/33", "127.0.0.*", "127.0.0.*/8", "127.0.0.1/33.html" //
        ), IPWithSubnetChecker::isValidIP4Range, false);
    }

    @Test
    void testIsValidIP6() {
        assertIPRanges(List.of( //
            "2001:db8::/32", "0::0/0", "2001:db8::/128" //
        ), IPWithSubnetChecker::isValidIP6Range, true);

        assertIPRanges(List.of( //
            "2001:zb8::/32", "localhost" //
        ), IPWithSubnetChecker::isValidIP6Range, false);
    }
}
