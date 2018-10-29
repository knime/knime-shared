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
 *   Sep 27, 2018 (awalter): created
 */
package org.knime.core.util;

/**
 * Utility class for reading, writing, parsing, etc. XML files.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
public final class XMLUtils {

    private XMLUtils() {
        // Prevent instantiation of util class
    }

    /**
     * Escapes all forbidden XML characters so that we can save them
     * nevertheless. They are escaped as &quot;%%ddddd&quot;, with ddddd being
     * their decimal Unicode.
     *
     * @param s the string to escape
     * @return the escaped string
     */
    public static final String escape(final String s) {
        if (s == null) {
            return null;
        }
        char[] c = s.toCharArray();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            if (((c[i] < 32) || (c[i] > 0xd7ff))
                    || ((i < c.length - 1) && (c[i] == '%') && c[i + 1] == '%')) {
                // if c contains '%' we encode the '%'
                buf.append("%%");
                if (c[i] < 10) {
                    buf.append('0');
                }
                if (c[i] < 100) {
                    buf.append('0');
                }
                if (c[i] < 1000) {
                    buf.append('0');
                }
                if (c[i] < 10000) {
                    buf.append('0');
                }

                buf.append(Integer.toString(c[i]));
            } else {
                buf.append(c[i]);
            }
        }

        return buf.toString();
    }

    /**
     * Unescapes all forbidden XML characters that were previous escaped by
     * {@link #escape(String)}. Must pay attention to handle not escaped
     * strings for backward compatibility (it will not correctly handle them,
     * they still are unescaped, but it must not fail on those strings).
     *
     * @param s the escaped string
     * @return the unescaped string
     */
    public static final String unescape(final String s) {
        if (s == null) {
            return null;
        }
        char[] c = s.toCharArray();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            if ((c[i] == '%') && (i < c.length - 6) && c[i + 1] == '%'
                    && Character.isDigit(c[i + 2])
                    && Character.isDigit(c[i + 3])
                    && Character.isDigit(c[i + 4])
                    && Character.isDigit(c[i + 5])
                    && Character.isDigit(c[i + 6])) {
                buf
                        .append((char)((c[i + 2] - '0') * 10000
                                + (c[i + 3] - '0') * 1000 + (c[i + 4] - '0')
                                * 100 + (c[i + 5] - '0') * 10 + (c[i + 6] - '0')));
                i += 6;
            } else {
                buf.append(c[i]);
            }
        }

        return buf.toString();
    }

}
