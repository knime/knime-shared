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

import java.net.URI;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.util.Pair;

/**
 * Tests if a host name of a given URI matches some criteria.
 */
public class HostnameFilter implements Predicate<URI> {

    private static final String PROTOCOL_ENDING = "://";

    enum Mode {
            BEGINS_WITH, ENDS_WITH, REGEX
    }

    private final Mode m_mode;

    private final String m_protocolFilter;

    private final String m_matchTo;

    /**
     * Constructor.
     *
     * @param mode the filter mode
     * @param matchTo the match criteria
     */
    public HostnameFilter(final Mode mode, final String matchTo) {
        m_mode = mode;
        final var filters = extractProtocolFilter(matchTo);
        // literal string filter that protocol is matched against
        m_protocolFilter = filters.getFirst();
        // literal string filter or RegEx pattern (if Mode.REGEX)
        m_matchTo = StringUtils.lowerCase(filters.getSecond());
    }

    /**
     * Extracts the protocol if one is given to initialize the protocol matcher.
     */
    private static Pair<String, String> extractProtocolFilter(final String matchTo) {
        int protocolIndex = matchTo.indexOf(PROTOCOL_ENDING);
        if (protocolIndex != -1) {
            return Pair.create(matchTo.substring(0, protocolIndex),
                matchTo.substring(protocolIndex + PROTOCOL_ENDING.length()));
        }
        return Pair.create(null, matchTo);
    }

    @Override
    public boolean test(final URI uri) {
        if (uri == null || uri.getAuthority() == null) {
            return false;
        }

        if (!isProtocolMatching(uri)) {
            return false;
        }

        String host = uri.getAuthority();

        // Strip away port take special care for IP6.
        final var index = host.indexOf(':');
        final var index2 = host.lastIndexOf(']');
        if (index != -1 && index2 < index) {
            host = host.substring(0, index);
        }

        return switch (this.m_mode) {
            case BEGINS_WITH -> StringUtils.startsWithIgnoreCase(host, m_matchTo);
            case ENDS_WITH -> StringUtils.endsWithIgnoreCase(host, m_matchTo);
            case REGEX -> host == null
                || Pattern.compile(m_matchTo, Pattern.CASE_INSENSITIVE).matcher(host).matches();
        };
    }

    /**
     * Applies the protocol filter if available to see if we have a match.
     *
     * @param uri to test for a correct protocol
     * @return true if passed else false
     */
    private boolean isProtocolMatching(final URI uri) {
        return m_protocolFilter == null || uri.getScheme() == null
            || StringUtils.equalsIgnoreCase(uri.getScheme(), m_protocolFilter);
    }
}
