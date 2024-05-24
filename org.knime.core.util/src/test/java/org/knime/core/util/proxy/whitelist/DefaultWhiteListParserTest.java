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

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link DefaultWhiteListParser}.
 * Host examples are from proxy-vole tests, the test structure is different.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
class DefaultWhiteListParserTest {

    private static final WhiteListParser PARSER = new DefaultWhiteListParser();

    static void assertWhiteListing(final String excludedHosts, final List<String> excluded,
        final List<String> included) {
        final var predicates = PARSER.parseWhiteList(excludedHosts);
        assertThat(excluded) //
            .as("All these hosts should be excluded but were not") //
            .map(URI::new) //
            .allSatisfy(host -> predicates.stream().anyMatch(predicate -> predicate.test(host)));
        assertThat(included) //
            .as("All these hosts should not be excluded but were") //
            .map(URI::new) //
            .allSatisfy(host -> predicates.stream().noneMatch(predicate -> predicate.test(host)));
    }

    @Test
    void testPrefixExclusion() {
        assertWhiteListing("*.knime.com", List.of("http://www.knime.com"), List.of("http://www.knime.com.test"));
    }

    @Test
    void testPostfixExclusion() {
        assertWhiteListing("www.knime.*", List.of("http://www.knime.com.test"), List.of("http://test.knime.com"));
    }

    @Test
    void testMultipleEntries() {
        assertThat(PARSER.parseWhiteList("www.knime.com, *.knime.invalid, junit*")) //
            .as("Not enough host predicate were parsed from string of excluded hosts") //
            .hasSize(3);
    }

    @Test
    void testIPRangeExclusion() {
        assertWhiteListing("192.168.0.0/24", List.of("http://192.168.0.1", "http://192.168.0.11"),
            List.of("http://www.knime.com", "http://145.5.5.1"));
    }

    @Test
    void testLocalExclusion() {
        assertWhiteListing("<local>", List.of("http://localhost"), List.of("http://www.knime.com"));
    }
}
