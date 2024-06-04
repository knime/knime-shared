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
 *   Apr 30, 2024 (lw): created
 */
package org.knime.core.util.proxy.search;

import java.net.URI;
import java.util.Optional;

import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.ProxyProtocol;

/**
 * Interface for retrieving the single best fitting proxy configuration for a given {@link URI}
 * and/or a set of valid {@link ProxyProtocol} for which the configuration should be present.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.3
 */
interface GlobalProxyStrategy {

    /**
     * Retrieves the current global proxy configuration for a URI and a selection of proxy protocols
     * and stores it in a read-only format. If the {@link URI} is non-null and proxy protocols are not empty,
     * the resulting proxy configuration was configured for the given criteria.
     * Returns {@link Optional#empty()} if no such proxy is configured.
     * <p>
     * If the URI is null, the proxy is selected only based on specified protocols.
     * </p>
     *
     * @param uri the {@link URI} to select the proxy for (may be null)
     * @param protocols protocols whose configurations are queried in order, first one present is returned
     * @return {@link GlobalProxyConfig} if configuration is present
     */
    GlobalProxySearchResult getCurrentFor(final URI uri, final ProxyProtocol... protocols);

    /**
     * Tri-state for the {@link GlobalProxyConfig} search result. The {@link #signal()} indicator
     * determines whether to continue search or return the found config value. The following three
     * states are possible.
     * <ul>
     *   <li>Nothing was found and search should continue, see {@link #empty()}.</li>
     *   <li>Nothing was found since proxies are disabled, see {@link #stop()}.</li>
     *   <li>A proxy config was found, see {@link #found(GlobalProxyConfig)}.</li>
     * </ul>
     *
     * @param signal the {@link SearchSignal} indicator
     * @param value the {@link GlobalProxyConfig} value
     */
    record GlobalProxySearchResult(SearchSignal signal, Optional<GlobalProxyConfig> value) {

        enum SearchSignal {
                EVALUATE, STOP;
        }

        static GlobalProxySearchResult stop() {
            return new GlobalProxySearchResult(SearchSignal.STOP, Optional.empty());
        }

        static GlobalProxySearchResult empty() {
            return new GlobalProxySearchResult(SearchSignal.EVALUATE, Optional.empty());
        }

        static GlobalProxySearchResult found(final GlobalProxyConfig value) {
            return new GlobalProxySearchResult(SearchSignal.EVALUATE, Optional.of(value));
        }
    }
}
