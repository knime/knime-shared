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

import java.util.Locale;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.node.config.base.ConfigBaseWO;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;

/**
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 * @since 6.0
 */
public final class NumberFormatter {

    final DecimalFormat m_format;

    private final int m_minimumDecimals;

    private final int m_maximumDecimals;

    private final String m_groupSeparator;

    private final String m_decimalSeparator;

    private final boolean m_alwaysShowDecimalSeparator;

    /**
     * Creates a formatter with the given parameters.
     * @param settings validated settings
     */
    private NumberFormatter(final Builder settings) {
        m_minimumDecimals = settings.m_minimumDecimals;
        m_maximumDecimals = settings.m_maximumDecimals;

        m_groupSeparator = settings.m_groupSeparator;
        m_decimalSeparator = settings.m_decimalSeparator;
        m_alwaysShowDecimalSeparator = settings.m_alwaysShowDecimalSeparator;

        m_format = createDecimalFormat(settings);
    }

    /**
     * @param settings valid settings
     * @see #validate(Builder)
     */
    private static DecimalFormat createDecimalFormat(final Builder settings) {
        var format = new DecimalFormat();

        format.setMinimumFractionDigits(settings.m_minimumDecimals);
        format.setMaximumFractionDigits(settings.m_maximumDecimals);

        format.setDecimalSeparatorAlwaysShown(settings.m_alwaysShowDecimalSeparator);

        // symbols define the decimal and group separator
        DecimalFormatSymbols symbols = defaultSymbols();
        // if group separator is not empty apply grouping and use separator
        if (!settings.m_groupSeparator.isEmpty()) {
            format.setGroupingUsed(true);
            format.setGroupingSize(3);
            symbols.setGroupingSeparatorString(settings.m_groupSeparator);
        }
        symbols.setDecimalSeparatorString(settings.m_decimalSeparator);
        format.setDecimalFormatSymbols(symbols);
        return format;
    }

    private static DecimalFormatSymbols defaultSymbols() {
        return new DecimalFormatSymbols(Locale.ENGLISH);
    }

    /**
     * @param value a Long, Double, BigDecimal etc.
     * @return formatted value
     */
    public String format(final Object value) {
        return m_format.format(value);
    }

    /**
     * @param settings to validate
     * @throws InvalidSettingsException
     */
    public static void validate(final Builder settings) throws InvalidSettingsException {
        // non-negative minimum decimals
        if(settings.m_minimumDecimals < 0) {
            throw new InvalidSettingsException("The minimum number of decimals must be non-negative.");
        }

        // non-negative maximum decimals
        if(settings.m_maximumDecimals < 0) {
            throw new InvalidSettingsException("The maximum number of decimals must be non-negative.");
        }

        // minimum decimals <= maximum decimals
        if(settings.m_minimumDecimals > settings.m_maximumDecimals) {
            throw new InvalidSettingsException(
                "The minimum number of decimals must be smaller or equal to the maximum number of decimals.");
        }
    }

    /**
     * @return minimum number of decimals to display, adds trailing zeros if necessary
     */
    public int getMinimumDecimals() {
        return m_minimumDecimals;
    }

    /**
     * @return maximum number of decimals to display, rounds if necessary
     */
    public int getMaximumDecimals() {
        return m_maximumDecimals;
    }

    /**
     * @return the separator used to separate groups of digits, e.g. 1,000,000
     */
    public String getGroupSeparator() {
        return m_groupSeparator;
    }

    /**
     * @return the separator used to separate the integer part from the decimal part, e.g. 1.5
     */
    public String getDecimalSeparator() {
        return m_decimalSeparator;
    }

    /**
     * @return whether to always show the decimal separator, even if there are no decimals
     */
    public boolean isAlwaysShowDecimalSeparator() {
        return m_alwaysShowDecimalSeparator;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof NumberFormatter) {
            NumberFormatter other = (NumberFormatter)obj;
            return m_minimumDecimals == other.m_minimumDecimals
                && m_maximumDecimals == other.m_maximumDecimals
                && m_groupSeparator.equals(other.m_groupSeparator)
                && m_decimalSeparator.equals(other.m_decimalSeparator)
                && m_alwaysShowDecimalSeparator == other.m_alwaysShowDecimalSeparator;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return m_minimumDecimals
            + 31 * m_maximumDecimals
            + 31 * m_groupSeparator.hashCode()
            + 31 * m_decimalSeparator.hashCode()
            + 31 * Boolean.hashCode(m_alwaysShowDecimalSeparator);
    }

    /**
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @author Carl Witt, KNIME AG, Zurich, Switzerland
     */
    public static class Builder {
        private int m_minimumDecimals;

        private int m_maximumDecimals = 2;

        private String m_groupSeparator = "";

        private String m_decimalSeparator = ".";

        private boolean m_alwaysShowDecimalSeparator;

        /**
         * @param minimumDecimals the minimum number of decimals to display, adds trailing zeros if necessary
         * @return this for fluent API
         */
        public Builder setMinimumDecimals(final int minimumDecimals) {
            m_minimumDecimals = minimumDecimals;
            return this;
        }

        /**
         * @param maximumDecimals the maximum number of decimals to display, rounds if necessary
         * @return this for fluent API
         */
        public Builder setMaximumDecimals(final int maximumDecimals) {
            m_maximumDecimals = maximumDecimals;
            return this;
        }

        /**
         * @param groupSeparator the separator used to separate groups of digits, e.g. 1,000,000
         * @return this for fluent API
         */
        public Builder setGroupSeparator(final String groupSeparator) {
            m_groupSeparator = groupSeparator;
            return this;
        }

        /**
         * @param decimalSeparator the separator used to separate the integer part from the decimal part, e.g. 1.5
         * @return this for fluent API
         */
        public Builder setDecimalSeparator(final String decimalSeparator) {
            m_decimalSeparator = decimalSeparator;
            return this;
        }

        /**
         * @param alwaysShowDecimalSeparator whether to always show the decimal separator, even if there are no decimals
         * @return this for fluent API
         */
        public Builder setAlwaysShowDecimalSeparator(final boolean alwaysShowDecimalSeparator) {
            m_alwaysShowDecimalSeparator = alwaysShowDecimalSeparator;
            return this;
        }

        /**
         * @return the formatter with the given settings
         * @throws InvalidSettingsException
         */
        public NumberFormatter build() throws InvalidSettingsException {
            validate(this);
            return new NumberFormatter(this);
        }
    }

    /**
     * Utility for loading and saving to KNIME configuration objects.
     *
     * @author Carl Witt, KNIME AG, Zurich, Switzerland
     */
    public static final class Persistor {

        private static final String CONFIG_MINIMUM_DECIMALS = "minimum_decimals";

        private static final String CONFIG_MAXIMUM_DECIMALS = "maximum_decimals";

        private static final String CONFIG_GROUP_SEPARATOR = "group_separator";

        private static final String CONFIG_DECIMAL_SEPARATOR = "decimal_separator";

        private static final String CONFIG_ALWAYS_SHOW_DECIMAL_SEPARATOR = "always_show_decimal_separator";

        private Persistor() {

        }

        /**
         * Persist number formatter settings.
         *
         * @param config empty settings to write into
         * @param formatter provides settings
         */
        public static void save(final ConfigBaseWO config, final NumberFormatter formatter) {
            config.addInt(CONFIG_MINIMUM_DECIMALS, formatter.getMinimumDecimals());
            config.addInt(CONFIG_MAXIMUM_DECIMALS, formatter.getMaximumDecimals());
            config.addString(CONFIG_GROUP_SEPARATOR, formatter.getGroupSeparator());
            config.addString(CONFIG_DECIMAL_SEPARATOR, formatter.getDecimalSeparator());
            config.addBoolean(CONFIG_ALWAYS_SHOW_DECIMAL_SEPARATOR, formatter.isAlwaysShowDecimalSeparator());
        }

        /**
         * @param config to read from.
         * @return a new instance with loaded parameters.
         * @throws InvalidSettingsException If the settings could not be read.
         */
        public static NumberFormatter load(final ConfigBaseRO config) throws InvalidSettingsException {
            var minimumDecimals = config.getInt(CONFIG_MINIMUM_DECIMALS);
            var maximumDecimals = config.getInt(CONFIG_MAXIMUM_DECIMALS);
            var groupSeparator = config.getString(CONFIG_GROUP_SEPARATOR);
            var decimalSeparator = config.getString(CONFIG_DECIMAL_SEPARATOR);
            var alwaysShowDecimalSeparator = config.getBoolean(CONFIG_ALWAYS_SHOW_DECIMAL_SEPARATOR);
            return new NumberFormatter.Builder()//
                .setMinimumDecimals(minimumDecimals)//
                .setMaximumDecimals(maximumDecimals)//
                .setGroupSeparator(groupSeparator)//
                .setDecimalSeparator(decimalSeparator)//
                .setAlwaysShowDecimalSeparator(alwaysShowDecimalSeparator)//
                .build();
        }

    }
}
