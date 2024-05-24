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
 *   Jun 4, 2024 (lw): created
 */
package org.knime.core.util.proxy.whitelist;

import java.util.regex.Pattern;

/**
 * Checks if the given string is a IP4 range subnet definition of the format.
 * Based on a contribution by Jan Engler.
 */
public final class IPWithSubnetChecker {

    private static final Pattern IP4_SUB_PATTERN =
        Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." // NOSONAR
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])/(\\d|([12]\\d|3[0-2]))$");

    // Could be improved.
    private static final Pattern IP6_SUB_PATTERN = Pattern.compile("^[a-f0-9:]*/\\d+$");

    /**
     * Tests if a given string is of in the correct format for an IP4 subnet mask.
     *
     * @param possibleIPAddress to test for valid format
     * @return true if valid else false
     */
    public static boolean isValidIP4Range(final String possibleIPAddress) {
        return IP4_SUB_PATTERN.matcher(possibleIPAddress).matches();
    }

    /**
     * Tests if a given string is of in the correct format for an IP6 subnet mask.
     *
     * @param possibleIPAddress to test for valid format
     * @return true if valid else false
     */
    public static boolean isValidIP6Range(final String possibleIPAddress) {
        return IP6_SUB_PATTERN.matcher(possibleIPAddress).matches();
    }

    /**
     * Hides constructor.
     */
    private IPWithSubnetChecker() {
    }
}
