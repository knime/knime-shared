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

import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Accesses Java's System.properties to retrieve proxy-relevant properties.
 * Returns every property as Optional<String>.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.1
 */
public final class GlobalProxyConfigProvider {

    // Keys used to identify System properties.

    static final String HOST_KEY = "proxyHost";

    static final String PORT_KEY = "proxyPort";

    static final String USER_KEY = "proxyUser";

    static final String PASSWORD_KEY = "proxyPassword";

    static final String EXCLUDED_HOSTS_KEY = "nonProxyHosts";

    /**
     * @see GlobalProxyConfigProvider#getProtocol(Properties)
     *
     * @return ProxyProtocol enum if present
     */
    public static Optional<ProxyProtocol> getProtocol() {
        return getProtocol(System.getProperties());
    }

    /**
     * Determine which protocol of system proxy properties should be queried.
     * @param properties the properties to read the proxy configuration from
     *
     * @return ProxyProtocol enum if present
     */
    private static Optional<ProxyProtocol> getProtocol(final Properties properties) {
        for (var p : ProxyProtocol.values()) {
            if (p.isConfigured(properties)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    private static Optional<String> getProperty(final String key, final Properties properties) {
        return getProtocol().flatMap(p -> p.getProxyProperty(key, properties));
    }

    /**
     * @see GlobalProxyConfigProvider#getHost(Properties)
     *
     * @return Proxy host address
     */
    public static Optional<String> getHost() {
        return getHost(System.getProperties());
    }

    /**
     * Returns the host value that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy host address
     */
    private static Optional<String> getHost(final Properties properties) {
        return getProperty(HOST_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#getPort(Properties)
     *
     * @return Proxy port
     */
    public static Optional<String> getPort() {
        return getPort(System.getProperties());
    }

    /**
     * Returns the port value that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy port
     */
    private static Optional<String> getPort(final Properties properties) {
        return getProperty(PORT_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#useAuthentication(Properties)
     *
     * @return Use proxy authentication?
     */
    public static boolean useAuthentication() {
        return useAuthentication(System.getProperties());
    }

    /**
     * Whether to use authentication, based on the presence of the property.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Use proxy authentication?
     */
    private static boolean useAuthentication(final Properties properties) {
        return getProperty(USER_KEY, properties).isPresent()
                && getProperty(PASSWORD_KEY, properties).isPresent();
    }

    /**
     * @see GlobalProxyConfigProvider#getUsername(Properties)
     *
     * @return Proxy user name
     */
    public static Optional<String> getUsername() {
        return getUsername(System.getProperties());
    }

    /**
     * Returns the user name that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy user name
     */
    private static Optional<String> getUsername(final Properties properties) {
        return getProperty(USER_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#getPassword(Properties)
     *
     * @return Proxy password
     */
    public static Optional<String> getPassword() {
        return getPassword(System.getProperties());
    }

    /**
     * Returns the proxy password that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy password
     */
    private static Optional<String> getPassword(final Properties properties) {
        return getProperty(PASSWORD_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#getExcludedHosts(Properties)
     *
     * @return Proxy-excluded hosts
     */
    public static Optional<String> getExcludedHosts() {
        return getExcludedHosts(System.getProperties());
    }

    /**
     * Returns the excluded hosts value that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy-excluded hosts
     */
    private static Optional<String> getExcludedHosts(final Properties properties) {
        return getProperty(EXCLUDED_HOSTS_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#useExcludedHosts(Properties)
     *
     * @return Use proxy-excluded hosts?
     */
    public static boolean useExcludedHosts() {
        return useExcludedHosts(System.getProperties());
    }

    /**
     * Whether to use excluding hosts, based on the presence of the property.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Use proxy-excluded hosts?
     */
    private static boolean useExcludedHosts(final Properties properties) {
        return getProperty(EXCLUDED_HOSTS_KEY, properties).map(StringUtils::isNotBlank).orElse(false);
    }

    /**
     * Retrieves the current global proxy configuration and stores it in a read-only format.
     * Returns {@link Optional#empty()} if no proxy is configured.
     *
     * @return {@link GlobalProxyConfig}
     */
    public static synchronized Optional<GlobalProxyConfig> getCurrent() {
        final var properties = System.getProperties();
        final var maybeProtocol = getProtocol(properties);
        if (maybeProtocol.isEmpty()) {
            return Optional.empty();
        }
        final var protocol = maybeProtocol.get();
        final var host = getHost(properties).get(); // NOSONAR if protocol exists, host does too
        final var port = getPort(properties).get(); // NOSONAR if protocol exists, port does too
        final var useAuthentication = useAuthentication(properties);
        final var username = getUsername(properties).orElse(null);
        final var password = getPassword(properties).orElse(null);
        final var useExcludedHosts = useExcludedHosts(properties);
        final var excludedHosts = getExcludedHosts(properties).orElse(null);
        return Optional.of(new GlobalProxyConfig(protocol, host, port, useAuthentication, username, password,
            useExcludedHosts, excludedHosts));
    }

    /**
     * Hides the constructor.
     */
    private GlobalProxyConfigProvider() {
    }
}
