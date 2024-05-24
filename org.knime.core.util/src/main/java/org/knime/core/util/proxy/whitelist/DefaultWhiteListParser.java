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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.knime.core.util.proxy.ExcludedHostsTokenizer;
import org.knime.core.util.proxy.whitelist.HostnameFilter.Mode;

/**
 * Default implementation for an white list parser. This will support the most
 * common forms of filters found in white lists. The white list is a comma (or
 * space) separated list of domain names or IP addresses. The following section
 * shows some examples.
 *
 * .mynet.com - Filters all host names ending with .mynet.com *.mynet.com -
 * Filters all host names ending with .mynet.com www.mynet.* - Filters all host
 * names starting with www.mynet. 123.12.32.1 - Filters the IP 123.12.32.1
 * 123.12.32.1/255 - Filters the IP range http://www.mynet.com - Filters only
 * HTTP protocol not FTP and no HTTPS
 *
 * Example of a list:
 *
 * .mynet.com, *.my-other-net.org, 123.55.23.222, 123.55.23.0/24
 *
 * Some info about this topic can be found here:
 * http://kb.mozillazine.org/No_proxy_for
 * http://technet.microsoft.com/en-us/library/dd361953.aspx
 *
 * Note that this implementation does not cover all variations of all browsers
 * but should cover the most used formats.
 */
public class DefaultWhiteListParser implements WhiteListParser {

    @Override
    public List<Predicate<URI>> parseWhiteList(final String whiteList) {
        List<Predicate<URI>> result = new ArrayList<>();

        for (var tkn : ExcludedHostsTokenizer.tokenize(whiteList)) {
            if (IPWithSubnetChecker.isValidIP4Range(tkn) || IPWithSubnetChecker.isValidIP6Range(tkn)) {
                result.add(new IPRangeFilter(tkn));
            } else if (tkn.endsWith("*")) {
                tkn = tkn.substring(0, tkn.length() - 1);
                result.add(new HostnameFilter(Mode.BEGINS_WITH, tkn));
            } else if (tkn.startsWith("*")) {
                tkn = tkn.substring(1);
                result.add(new HostnameFilter(Mode.ENDS_WITH, tkn));
            } else if (tkn.equals("<local>")) {
                result.add(new LocalBypassFilter());
            } else {
                result.add(new HostnameFilter(Mode.ENDS_WITH, tkn));
            }
        }

        return result;
    }
}
