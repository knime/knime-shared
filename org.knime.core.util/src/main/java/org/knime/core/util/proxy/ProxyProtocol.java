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
 *   8 Mar 2023 (leon.wenzler): created
 */
package org.knime.core.util.proxy;

import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Type of protocol to use for the proxy connection, come with default ports.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.2
 */
public enum ProxyProtocol {
        /**
         * Uses the HTTP protocol to establish a proxy connection.
         * Some also use 8080 or 3128 as default port.
         */
        HTTP(80),
        /**
         * Uses the HTTPS protocol to establish a proxy connection. More secure than HTTP.
         * Some also use 8080 or 3128 as default port.
         */
        HTTPS(443),
        /**
         * Uses the SOCKS protocol to establish a proxy connection. Defined in RFC 1928, can safely traverse a firewall
         * both at the TCP and UDP level.
         */
        SOCKS(1080);

    private final int m_defaultPort;

    ProxyProtocol(final int defaultPort) {
        m_defaultPort = defaultPort;
    }

    /**
     * Returns the default port per protocol.
     *
     * @return int port
     */
    public int getDefaultPort() {
        return m_defaultPort;
    }

    /**
     * Checks whether there is are host and port entries for this proxy protocol.
     * Acts as an identifier whether it was configured.
     * <p>
     * First checks whether system proxies are enabled. If so, property-based are not to be considered.
     * It is discouraged to use the "proxySet" property, therefore we check the host's presence.
     * </p>
     *
     * @return is configured in the system properties?
     */
    boolean isConfigured(final Properties properties) {
        if (Boolean.parseBoolean(properties.getProperty(GlobalProxyConfigProvider.USE_SYSTEM_PROXIES_KEY))) {
            return false;
        }
        // The port is not required for configuration. If it is absent, the default port is used.
        return getProxyProperty(GlobalProxyConfigProvider.HOST_KEY, properties)//
            .filter(StringUtils::isNotBlank)//
            .isPresent();
    }

    /**
     * Implements the proxy property syntax for the HTTP, HTTPS, and SOCKS protocol according to the
     * Java documentation. See below for the documentation URL.
     *
     * @param name property name
     * @param properties current {@link System#getProperties()}
     * @return string of the configured value, if present
     * @see "https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/net/doc-files/net-properties.html"
     */
    Optional<String> getProxyProperty(final String name, final Properties properties) {
        if (properties == null || properties.isEmpty() || name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(properties.getProperty(createPropertyKey(name)));
    }

    String createPropertyKey(final String name) {
        if (this == ProxyProtocol.SOCKS) {
            if (GlobalProxyConfigProvider.USER_KEY.equals(name)) {
                return GlobalProxyConfigProvider.SOCKS_USER_KEY;
            }
            if (GlobalProxyConfigProvider.PASSWORD_KEY.equals(name)) {
                return GlobalProxyConfigProvider.SOCKS_PASSWORD_KEY;
            }
            // For the SOCKS protocol, keys do not contain a dot and are in camel-case.
            return asLowerString() + StringUtils.capitalize(name);
        }

        if (GlobalProxyConfigProvider.EXCLUDED_HOSTS_KEY.equals(name)) {
            // The HTTP and HTTPS protocol use the same property for non-proxy-hosts.
            return "http." + name;
        }

        // If none of the special cases match, use standard "<protocol>.<property>" syntax.
        return asLowerString() + "." + name;
    }

    /**
     * String which can be used as a prefix for System properties.
     *
     * @return protocol string
     */
    String asLowerString() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
