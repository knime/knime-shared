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
 *   Jun 10, 2024 (lw): created
 */
package org.knime.core.util.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests regular and edge cases for tokenizing excluded hosts.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
class ExcludedHostsTokenizerTest {

    private static final String[] EXCLUDED_HOSTS_ACTUAL = {
        "knime.com;", " www.k_n_i_m_e.com", "", "https://*.knime.com", " ", "test123", "test*", "localhost",
        "192.168.0.1 ", "", "192.168.0.1\n", "   0.0.0.0/0", "", "http://10.*.*.*", "<local>", ";2001:db8::/32"
    };

    private static final String[] EXCLUDED_HOSTS_EXPECTED = {
        "knime.com", "www.k_n_i_m_e.com", "https://*.knime.com", "test123", "test*", "localhost",
        "192.168.0.1", "192.168.0.1", "0.0.0.0/0", "http://10.*.*.*", "<local>", "2001:db8::/32"
    };

    static void assertHostTokenization(final String separator) {
        assertThat(ExcludedHostsTokenizer.tokenize(String.join(separator, EXCLUDED_HOSTS_ACTUAL)))
            .as("Excluded hosts were not correctly tokenized/split")
            .containsExactly(EXCLUDED_HOSTS_EXPECTED);
    }

    @Test
    void testExamplesWithPipeSeparator() {
        assertHostTokenization("|");
    }

    @Test
    void testExamplesWithCommaSeparator() {
        assertHostTokenization(",");
    }

    @Test
    void testExamplesWithSemicolonSeparator() {
        assertHostTokenization(";");
    }

    @Test
    void testExamplesWithWhitespaceSeparator() {
        assertHostTokenization(" ");
    }

    @Test
    void testEmptyTokenization() {
        assertThat(ExcludedHostsTokenizer.tokenize(null)) //
            .as("Should return 'null' but did not") //
            .isNull();
        assertThat(ExcludedHostsTokenizer.tokenize(" ;;;| ,|\n, ||\n| ,,,\n,;, | ; ")) //
            .as("Should return empty excluded hosts but did not") //
            .isEmpty();
    }
}
