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
 *   May 24, 2024 (lw): created
 */
package org.knime.core.util.proxy;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Searches through the environment variables configured in the JVM.
 * Uses {@link System#getenv(String)} to retrieve the following environment variables.
 * <ul>
 *   <li>{@code HTTP_PROXY} for HTTP proxy connections.</li>
 *   <li>{@code HTTPS_PROXY} for HTTPS proxy connections.</li>
 *   <li>{@code SOCKS_PROXY} for SOCKS proxy connections</li>
 *   <li>{@code ALL_PROXY} as general proxy to be used by all connections (queried as fallback).</li>
 *   <li>{@code NO_PROXY} as comma-separated list of hosts to be excluded from the proxy.</li>
 * </ul>
 *
 * @see <a href="https://curl.se/docs/tutorial.html#environment-variables">libcurl documentation</a>
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.3
 */
public class EnvironmentProxyConfigProvider {

    private static final Logger LOGGER = Logger.getLogger(EnvironmentProxyConfigProvider.class.getName());

    /**
     * Proxy environment variables used by convention (popular use in {@code libcurl}).
     * Specifies {@link #getValue()} for retrieving its value from the environment.
     */
    enum ProxyEnvVar {
        HTTP_PROXY, HTTPS_PROXY, SOCKS_PROXY, ALL_PROXY, NO_PROXY;

        /**
         * Converts from {@link ProxyProtocol} to {@link ProxyEnvVar}.
         *
         * @param protocol the proxy protocol (HTTP, HTTPS, or SOCKS).
         * @return corresponding environment variable
         */
        private static ProxyEnvVar fromProxyProtocol(final ProxyProtocol protocol) {
            return switch (protocol) {
                case HTTP -> HTTP_PROXY;
                case HTTPS -> HTTPS_PROXY;
                case SOCKS -> SOCKS_PROXY;
            };
        }

        private String getLowerKey() {
            return StringUtils.lowerCase(name());
        }

        private String getUpperKey() {
            return StringUtils.upperCase(name());
        }

        /**
         * Returns value from the environment. Checks both, lower-case and upper-case variables.
         * These should yield the same result on Windows, but are different values on Linux.
         * By checking both, we can be sure that nothing was missed (without OS-specific queries).
         *
         * @return value if present (otherwise default value)
         */
        String getValue(final Map<String, String> variables) {
            final var lowerResult = variables.get(getLowerKey());
            if (StringUtils.isNotBlank(lowerResult)) {
                return lowerResult;
            }
            final var upperResult = variables.get(getUpperKey());
            if (StringUtils.isNotBlank(upperResult)) {
                return upperResult;
            }
            return null;
        }
    }

    /**
     * Maps a {@link String} value of an environment variable to a {@link GlobalProxyConfig}.
     *
     * @param proxy non-null string result
     * @param protocol non-empty proxy protocol
     * @param noProxyValue non-null value of the NO_PROXY variable
     * @return GlobalProxyConfig instance
     */
    private static GlobalProxyConfig toGlobalProxyConfig(final String proxy, final ProxyProtocol protocol,
        final String noProxyValue) {
        String host = proxy;
        String port = null;
        String username = null;
        String password = null;
        final var userInfoIdx = proxy.indexOf('@');
        if (userInfoIdx > 0) {
            host = proxy.substring(userInfoIdx + 1);
            final var userInfo = proxy.substring(0, userInfoIdx).split(":", -1);
            username = userInfo[0];
            if (userInfo.length > 1) {
                password = userInfo[1];
            }
        }
        // tries to parse host and integer port from string
        final var portIdx = host.lastIndexOf(':');
        if (portIdx > 0) {
            port = host.substring(portIdx + 1);
            host = host.substring(0, portIdx);
        }
        // the NO_PROXY variable specifies excluded hosts for all proxies in comma-separated list,
        // re-joining this list with '|' instead to use the same format as Java
        final var excludedHosts = StringUtils.replaceChars(noProxyValue, ',', '|');
        return new GlobalProxyConfig( //
            protocol, //
            host, //
            port, //
            !Objects.isNull(username) && !Objects.isNull(password), //
            username, //
            password, //
            StringUtils.isNotEmpty(excludedHosts), //
            excludedHosts);
    }

    /**
     * Retrieves the proxy configuration *only* from environment variables.
     *
     * @param uri the {@link URI} for which to search
     * @param protocols a selection of protocols for which to search
     * @return GlobalProxyConfig
     * @since 6.3
     */
    public static Optional<GlobalProxyConfig> getConfigFromEnvironment(final URI uri,
        final ProxyProtocol... protocols) {
        return getConfigFromEnvironment(System.getenv(), uri, protocols);
    }

    static Optional<GlobalProxyConfig> getConfigFromEnvironment(final Map<String, String> environmentVariables,
        final URI uri, final ProxyProtocol... protocols) {
        final var noProxyValue = ProxyEnvVar.NO_PROXY.getValue(environmentVariables);

        // select protocol-based *_PROXY variable value
        for (var procotol : protocols) {
            final var result = ProxyEnvVar.fromProxyProtocol(procotol).getValue(environmentVariables);
            if (result != null) {
                return Optional.of(toGlobalProxyConfig(result, procotol, noProxyValue));
            }
        }

        // use ALL_PROXY variable value as fallback
        final var fallbackResult = ProxyEnvVar.ALL_PROXY.getValue(environmentVariables);
        if (fallbackResult != null) {
            // if not found, try getting the URI protocol for proxy but fall back to HTTP if null
            final var fallbackProtocol = Objects.requireNonNullElse(uri == null ? null //
                : EnumUtils.getEnum(ProxyProtocol.class, uri.getScheme()), ProxyProtocol.HTTP);
            return Optional.of(toGlobalProxyConfig(fallbackResult, fallbackProtocol, noProxyValue));
        }
        return Optional.empty();
    }
}
