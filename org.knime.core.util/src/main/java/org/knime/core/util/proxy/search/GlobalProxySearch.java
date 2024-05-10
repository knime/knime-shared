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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.ProxyProtocol;
import org.knime.core.util.proxy.search.GlobalProxyStrategy.GlobalProxySearchResult.SearchSignal;

/**
 * Employs multiple {@link GlobalProxyStrategy}s to search for the current global proxy
 * configuration for a given web address, and optionally valid {@link ProxyProtocol}s.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.3
 */
public final class GlobalProxySearch {

    private static final Logger LOGGER = Logger.getLogger(GlobalProxySearch.class.getName());

    private static final List<GlobalProxyStrategy> SEARCH_STRATEGIES = List.of( //
        new EclipseProxyStrategy(), new JavaProxyStrategy(), new EnvironmentProxyStrategy() //
    );

    /**
     * Retrieves the current global proxy configuration for the URL and stores it in a read-only format.
     * Returns {@link Optional#empty()} if no proxy is configured.
     * <p>
     * The {@link URL} may be null. In this case, any proxy configuration is considered valid.
     * </p>
     *
     * @param url the URL to select the proxy for (may be null)
     * @return {@link GlobalProxyConfig} if configuration is present
     */
    public static Optional<GlobalProxyConfig> getCurrentFor(final URL url) {
        URI uri = null;
        if (url != null) {
            // return early for file:// URLs before trying to parse as URI
            if ("file".equalsIgnoreCase(url.getProtocol())) {
                return Optional.empty();
            }
            try {
                uri = url.toURI();
            } catch (URISyntaxException e) {
                LOGGER.log(Level.WARNING,
                    "Could not convert URL \"%s\" to URI for proxy exclusion detection".formatted(url), e);
            }
        }
        return getCurrentFor(uri);
    }

    /**
     * Retrieves the current global proxy configuration for the URI and stores it in a read-only format.
     * Returns {@link Optional#empty()} if no proxy is configured.
     * <p>
     * The {@link URI} may be null. In this case, any proxy configuration is considered valid.
     * </p>
     *
     * @param uri the URI to select the proxy for (may be null)
     * @return {@link GlobalProxyConfig} if configuration is present
     */
    public static Optional<GlobalProxyConfig> getCurrentFor(final URI uri) {
        ProxyProtocol protocol = null;
        if (uri != null) {
            protocol = EnumUtils.getEnum(ProxyProtocol.class, StringUtils.upperCase(uri.getScheme()));
        }
        return getCurrentFor(uri, protocol);
    }

    /**
     * Retrieves the current global proxy configuration for a selection of proxy protocols.
     * Returns {@link Optional#empty()} if no proxy is configured.
     * <p>
     * The protocols may be null or empty. In this case, all {@link ProxyProtocol}s are considered valid).
     * </p>
     *
     * @param protocols configurations are queried in protocol order, first one present is returned
     * @return {@link GlobalProxyConfig} if configuration is present
     */
    public static Optional<GlobalProxyConfig> getCurrentFor(final ProxyProtocol... protocols) {
        return getCurrentFor(null, protocols);
    }

    /**
     * Iterates through all registered {@link #SEARCH_STRATEGIES} and searches for a proxy configuration
     * given the {@link URI} and {@link ProxyProtocol}s constraints. If none of the strategies return
     * a matching configuration, {@link Optional#empty()} is returned.
     * <p>
     * The {@link URI} may be null and protocols may be null or empty (in this case, all {@link ProxyProtocol}s
     * are considered valid for proxy selection).
     * </p>
     *
     * @param uri URI for which the configuration is valid (may be null)
     * @param protocols set of protocols for which the configuration must be defined
     * @return GlobalProxyConfig if present
     */
    public static Optional<GlobalProxyConfig> getCurrentFor(final URI uri, final ProxyProtocol... protocols) {
        if (uri != null && "file".equalsIgnoreCase(uri.getScheme())) {
            return Optional.empty();
        }
        // while the URI may be null, protocol elements should be valid for strategies to work with
        final var validProtocols = validateProxyProtocols(protocols);
        // search through all registered strategies
        for (var strategy : SEARCH_STRATEGIES) {
            final var result = strategy.getCurrentFor(uri, validProtocols);
            final var value = result.value();
            // return if signaled by strategy
            if (result.signal() == SearchSignal.STOP) {
                return value;
            }
            // otherwise evaluate and *only* return if present and valid for URI
            if (value.filter(cfg -> !cfg.isHostExcluded(uri)).isPresent()) {
                return value;
            }
        }
        return Optional.empty();
    }

    /**
     * Transforms the given array of proxy protocols into a non-null, non-empty array of {@link ProxyProtocol}s.
     * If protocols are not specified (null or empty as input), all protocols are considered valid.
     *
     * @param protocols input array of protocols to choose from
     * @return validated set of protocols
     */
    private static ProxyProtocol[] validateProxyProtocols(final ProxyProtocol[] protocols) {
        final var filtered = Arrays.stream(Objects.requireNonNullElseGet(protocols, () -> new ProxyProtocol[0])) //
                .filter(Objects::nonNull) //
                .toArray(ProxyProtocol[]::new);
        return filtered.length > 0 ? filtered : ProxyProtocol.values();
    }

    /**
     * Hides the constructor.
     */
    private GlobalProxySearch() {
    }
}
