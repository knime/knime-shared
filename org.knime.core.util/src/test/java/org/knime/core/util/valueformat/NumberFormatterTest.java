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
 *   24 May 2023 (carlwitt): created
 */
package org.knime.core.util.valueformat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.SimpleConfig;

/**
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class NumberFormatterTest {

    final Supplier<NumberFormatter.Builder> baseBuilder = () -> new NumberFormatter.Builder()//
        .setMinimumDecimals(0)//
        .setMaximumDecimals(4)//
        .setGroupSeparator("$")//
        .setDecimalSeparator("|")//
        .setAlwaysShowDecimalSeparator(true);

    /**
     * Show trailing zeros.
     *
     * @throws InvalidSettingsException
     */
    @Test
    void testMinimumDecimals() throws InvalidSettingsException {
        {
            // given a number formatter that shows at least 2 decimals
            NumberFormatter formatter = baseBuilder.get().setMinimumDecimals(2).build();
            // when formatting a number with only zero decimals
            // then show trailing zeros
            assertThat(formatter.format(1.0)).as("Value is formatted incorrectly.").isEqualTo("1|00");
            // when formatting a number with more than zero decimals
            assertThat(formatter.format(1.000123)).as("Value is formatted incorrectly.").isEqualTo("1|0001");
        }

        {
            // given a number formatter that shows at least 3 decimals
            NumberFormatter formatter = baseBuilder.get().setMinimumDecimals(3).build();
            // when formatting a number with only zero decimals
            // then show trailing zeros
            assertThat(formatter.format(1.0)).as("Value is formatted incorrectly.").isEqualTo("1|000");
            // when formatting a number with more than zero decimals
            assertThat(formatter.format(1.000123)).as("Value is formatted incorrectly.").isEqualTo("1|0001");
        }
    }

    @Test
    void testMaximumDecimals() throws InvalidSettingsException {
        {
            NumberFormatter formatter = baseBuilder.get().setMaximumDecimals(2).build();
            assertThat(formatter.format(1.0)).as("Value is formatted incorrectly.").isEqualTo("1|");
            assertThat(formatter.format(1.000123)).as("Value is formatted incorrectly.").isEqualTo("1|");
            assertThat(formatter.format(1.123)).as("Value is formatted incorrectly.").isEqualTo("1|12");
        }

        {
            NumberFormatter formatter = baseBuilder.get().setMaximumDecimals(3).build();
            assertThat(formatter.format(1.000123)).as("Value is formatted incorrectly.").isEqualTo("1|");
            assertThat(formatter.format(1.123)).as("Value is formatted incorrectly.").isEqualTo("1|123");
        }

        {
            NumberFormatter formatter = baseBuilder.get().setMaximumDecimals(4).build();
            assertThat(formatter.format(1.000123)).as("Value is formatted incorrectly.").isEqualTo("1|0001");
            assertThat(formatter.format(1.123)).as("Value is formatted incorrectly.").isEqualTo("1|123");
        }

        {
            NumberFormatter formatter = baseBuilder.get().setMaximumDecimals(5).build();
            assertThat(formatter.format(1.000123)).as("Value is formatted incorrectly.").isEqualTo("1|00012");
            assertThat(formatter.format(1.123)).as("Value is formatted incorrectly.").isEqualTo("1|123");
        }
    }

    @Test
    void testGroupSeparator() throws InvalidSettingsException {
        {
            NumberFormatter formatter = baseBuilder.get().setGroupSeparator("$")//
                .setAlwaysShowDecimalSeparator(false).build();
            assertThat(formatter.format(1000)).as("Value is formatted incorrectly.").isEqualTo("1$000");
            assertThat(formatter.format(10000)).as("Value is formatted incorrectly.").isEqualTo("10$000");
            assertThat(formatter.format(1000000)).as("Value is formatted incorrectly.").isEqualTo("1$000$000");
        }

        {
            NumberFormatter formatter = baseBuilder.get().setGroupSeparator("≈")//
                .setAlwaysShowDecimalSeparator(false).build();
            assertThat(formatter.format(1000)).as("Value is formatted incorrectly.").isEqualTo("1≈000");
            assertThat(formatter.format(10000)).as("Value is formatted incorrectly.").isEqualTo("10≈000");
            assertThat(formatter.format(1000000)).as("Value is formatted incorrectly.").isEqualTo("1≈000≈000");
        }

        // separator with more than one character
        {
            NumberFormatter formatter = baseBuilder.get().setGroupSeparator("≈≈")//
                .setAlwaysShowDecimalSeparator(false).build();
            assertThat(formatter.format(1000)).as("Value is formatted incorrectly.").isEqualTo("1≈≈000");
            assertThat(formatter.format(10000)).as("Value is formatted incorrectly.").isEqualTo("10≈≈000");
            assertThat(formatter.format(1000000)).as("Value is formatted incorrectly.").isEqualTo("1≈≈000≈≈000");
        }

        // empty separator
        {
            NumberFormatter formatter = baseBuilder.get().setGroupSeparator("")//
                .setAlwaysShowDecimalSeparator(false).build();
            assertThat(formatter.format(1000)).as("Value is formatted incorrectly.").isEqualTo("1000");
            assertThat(formatter.format(10000)).as("Value is formatted incorrectly.").isEqualTo("10000");
            assertThat(formatter.format(1000000)).as("Value is formatted incorrectly.").isEqualTo("1000000");
        }
    }

    @Test
    void testDecimalSeparator() throws InvalidSettingsException {
        {
            NumberFormatter formatter = baseBuilder.get().setDecimalSeparator("|")//
                .setAlwaysShowDecimalSeparator(false).build();
            // should still not be shown if set but not necessary
            assertThat(formatter.format(10)).as("Value is formatted incorrectly.").isEqualTo("10");
            assertThat(formatter.format(10.4)).as("Value is formatted incorrectly.").isEqualTo("10|4");
            assertThat(formatter.format(1000.1)).as("Value is formatted incorrectly.").isEqualTo("1$000|1");
            assertThat(formatter.format(1000.123)).as("Value is formatted incorrectly.").isEqualTo("1$000|123");
        }

        {
            // use same decimal separator and group separator (hell yeah)
            NumberFormatter formatter = baseBuilder.get().setDecimalSeparator("$").build();
            assertThat(formatter.format(1000.1)).as("Value is formatted incorrectly.").isEqualTo("1$000$1");
        }

        // separator with more than one character
        {
            NumberFormatter formatter = baseBuilder.get().setDecimalSeparator("||")//
                .setAlwaysShowDecimalSeparator(false).build();
            assertThat(formatter.format(10)).as("Value is formatted incorrectly.").isEqualTo("10");
            assertThat(formatter.format(10.4)).as("Value is formatted incorrectly.").isEqualTo("10||4");
            assertThat(formatter.format(1000.1)).as("Value is formatted incorrectly.").isEqualTo("1$000||1");
            assertThat(formatter.format(1000.123)).as("Value is formatted incorrectly.").isEqualTo("1$000||123");
        }
    }

    @Test
    void testBuilderValidation() throws InvalidSettingsException {
        // minimum decimals must be non-negative
        assertThrows(InvalidSettingsException.class, () -> baseBuilder.get().setMinimumDecimals(-1).build(),
            "Builder must fail on negative minimum decimals.");

        // maximum decimals must be non-negative
        assertThrows(InvalidSettingsException.class, () -> baseBuilder.get().setMaximumDecimals(-1).build(),
            "Builder must fail on negative maximum decimals.");

        // maximum decimals must be at least as large as minimum decimals
        assertThrows(InvalidSettingsException.class,
            () -> baseBuilder.get().setMinimumDecimals(2).setMaximumDecimals(1).build(),
            "Builder must fail on maximum decimals smaller than minimum decimals.");
    }

    @Test
    void testLocaleIndependence() throws InvalidSettingsException {
        // given a number formatter
        NumberFormatter formatter = new NumberFormatter.Builder()//
            .setMinimumDecimals(3)//
            .setMaximumDecimals(3)//
            .build();

        // when setting the locale to german
        Locale.setDefault(Locale.GERMAN);

        // then default decimal separator is still a dot
        assertThat(formatter.format(1.111)).as("Value is formatted incorrectly.").isEqualTo("1.111");
    }

    @Test
    void testPersistor() throws InvalidSettingsException {
        // given a number formatter
        NumberFormatter formatter = new NumberFormatter.Builder()//
            .setMinimumDecimals(1)//
            .setMaximumDecimals(2)//
            .setGroupSeparator("≈")//
            .setDecimalSeparator("|")//
            .setAlwaysShowDecimalSeparator(true)//
            .build();

        // when persisting it
        SimpleConfig config = new SimpleConfig("root");
        NumberFormatter.Persistor.save(config, formatter);
        NumberFormatter persisted = NumberFormatter.Persistor.load(config);

        // then the persisted string contains the minimum and maximum decimals
        assertThat(persisted.getMinimumDecimals()).as("Wrong minimum decimals after loading.").isEqualTo(1);
        assertThat(persisted.getMaximumDecimals()).as("Wrong maximum decimals after loading.").isEqualTo(2);
        assertThat(persisted.getGroupSeparator()).as("Wrong group separator after loading.").isEqualTo("≈");
        assertThat(persisted.getDecimalSeparator()).as("Wrong decimal separator after loading.").isEqualTo("|");
        assertThat(persisted.isAlwaysShowDecimalSeparator()).as("Wrong always show decimal separator after loading.").isTrue();
    }

}
