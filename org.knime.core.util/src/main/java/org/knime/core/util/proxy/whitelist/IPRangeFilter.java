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

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Filters an URI by inspecting its IP address is in a given range. The range
 * as must be defined in CIDR notation, e.g. 192.0.2.1/24.
 */
public class IPRangeFilter implements Predicate<URI> {

    private static final Logger LOGGER = Logger.getLogger(IPRangeFilter.class.getName());

    private final byte[] m_matchTo;

    private final int m_numOfBits;

    /**
     * Constructor.
     *
     * @param matchTo the match subnet in CIDR notation
     */
    public IPRangeFilter(final String matchTo) {
        final var parts = matchTo.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("IP range is not valid:" + matchTo);
        }

        try {
            final var address = InetAddress.getByName(parts[0].trim());
            m_matchTo = address.getAddress();
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("IP range is not valid:" + matchTo, e);
        }

        m_numOfBits = Integer.parseInt(parts[1].trim());
    }

    @Override
    public boolean test(final URI uri) {
        if (uri == null || uri.getHost() == null) {
            return false;
        }
        try {
            final var address = InetAddress.getByName(uri.getHost());
            final byte[] addr = address.getAddress();

            // Comparing IP6 against IP4?
            if (addr.length != m_matchTo.length) {
                return false;
            }

            var bit = 0;
            for (var nibble = 0; nibble < addr.length; nibble++) {
                for (var nibblePos = 7; nibblePos >= 0; nibblePos--) {
                    int mask = 1 << nibblePos;
                    if ((m_matchTo[nibble] & mask) != (addr[nibble] & mask)) { // NOSONAR
                        return false;
                    }
                    bit++;
                    if (bit >= m_numOfBits) { // NOSONAR
                        return true;
                    }
                }
            }

        } catch (UnknownHostException e) {
            // In this case we can not get the IP to not match.
            LOGGER.log(Level.WARNING, e, () -> "Could not match IP: " + e.getMessage());
        }
        return false;
    }
}
