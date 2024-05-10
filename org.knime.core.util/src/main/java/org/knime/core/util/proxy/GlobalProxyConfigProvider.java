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

import java.net.URI;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.util.proxy.search.GlobalProxySearch;

/**
 * Accesses Java's System.properties to retrieve proxy-relevant properties.
 * Returns every property as Optional<String>.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.2
 */
public final class GlobalProxyConfigProvider {

    static final String USE_SYSTEM_PROXIES_KEY = "java.net.useSystemProxies";

    static final String HOST_KEY = "proxyHost";

    static final String PORT_KEY = "proxyPort";

    static final String USER_KEY = "proxyUser";

    static final String PASSWORD_KEY = "proxyPassword";

    static final String EXCLUDED_HOSTS_KEY = "nonProxyHosts";

    // Special authentication keys for SOCKS, are switched to when using #getUsername() or
    // #getPassword() and a ProxyProtocol.SOCKS proxy is detected or specified.

    static final String SOCKS_USER_KEY = "java.net.socks.username";

    static final String SOCKS_PASSWORD_KEY = "java.net.socks.password";

    /**
     * @see GlobalProxyConfigProvider#getProtocol(Properties)
     *
     * @return ProxyProtocol enum if present
     */
    public static Optional<ProxyProtocol> getProtocol() {
        return getProtocol(System.getProperties(), ProxyProtocol.values());
    }

    /**
     * Determine which protocol of system proxy properties should be queried.
     * @param properties the properties to read the proxy configuration from
     *
     * @return ProxyProtocol enum if present
     */
    private static Optional<ProxyProtocol> getProtocol(final Properties properties, final ProxyProtocol... protocols) {
        for (var p : protocols) {
            if (p.isConfigured(properties)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    /**
     * @see GlobalProxyConfigProvider#getHost(Properties)
     *
     * @return Proxy host address
     */
    public static Optional<String> getHost() {
        return getProtocol().flatMap(p -> getHost(System.getProperties(), p));
    }

    /**
     * Returns the host value that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy host address
     */
    private static Optional<String> getHost(final Properties properties, final ProxyProtocol protocol) {
        return protocol.getProxyProperty(HOST_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#getPort(Properties)
     *
     * @return Proxy port
     */
    public static Optional<String> getPort() {
        return getProtocol().flatMap(p -> getPort(System.getProperties(), p));
    }

    /**
     * Returns the port value that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy port
     */
    private static Optional<String> getPort(final Properties properties, final ProxyProtocol protocol) {
        return protocol.getProxyProperty(PORT_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#useAuthentication(Properties)
     *
     * @return Use proxy authentication?
     */
    public static boolean useAuthentication() {
        return getProtocol().map(p -> useAuthentication(System.getProperties(), p)).orElse(false);
    }

    /**
     * Whether to use authentication, based on the presence of the property.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Use proxy authentication?
     */
    private static boolean useAuthentication(final Properties properties, final ProxyProtocol protocol) {
        return protocol.getProxyProperty(USER_KEY, properties).isPresent()
                && protocol.getProxyProperty(PASSWORD_KEY, properties).isPresent();
    }

    /**
     * @see GlobalProxyConfigProvider#getUsername(Properties)
     *
     * @return Proxy user name
     */
    public static Optional<String> getUsername() {
        return getProtocol().flatMap(p -> getUsername(System.getProperties(), p));
    }

    /**
     * Returns the user name that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy user name
     */
    private static Optional<String> getUsername(final Properties properties, final ProxyProtocol protocol) {
        return protocol.getProxyProperty(USER_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#getPassword(Properties)
     *
     * @return Proxy password
     */
    public static Optional<String> getPassword() {
        return getProtocol().flatMap(p -> getPassword(System.getProperties(), p));
    }

    /**
     * Returns the proxy password that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy password
     */
    private static Optional<String> getPassword(final Properties properties, final ProxyProtocol protocol) {
        return protocol.getProxyProperty(PASSWORD_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#getExcludedHosts(Properties)
     *
     * @return Proxy-excluded hosts
     */
    public static Optional<String> getExcludedHosts() {
        return getProtocol().flatMap(p -> getExcludedHosts(System.getProperties(), p));
    }

    /**
     * Returns the excluded hosts value that is present with a certain protocol. The protocol can be retrieved via
     * {@link GlobalProxyConfigProvider#getProtocol()}.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Proxy-excluded hosts
     */
    private static Optional<String> getExcludedHosts(final Properties properties, final ProxyProtocol protocol) {
        return protocol.getProxyProperty(EXCLUDED_HOSTS_KEY, properties);
    }

    /**
     * @see GlobalProxyConfigProvider#useExcludedHosts(Properties)
     *
     * @return Use proxy-excluded hosts?
     */
    public static boolean useExcludedHosts() {
        return getProtocol().map(p -> useExcludedHosts(System.getProperties(), p)).orElse(false);
    }

    /**
     * Whether to use excluding hosts, based on the presence of the property.
     * @param properties the properties to read the proxy configuration from
     *
     * @return Use proxy-excluded hosts?
     */
    private static boolean useExcludedHosts(final Properties properties, final ProxyProtocol protocol) {
        return protocol.getProxyProperty(EXCLUDED_HOSTS_KEY, properties).map(StringUtils::isNotBlank).orElse(false);
    }

    /**
     * Retrieves the current global proxy configuration *only* from System properties and stores
     * it in a read-only format. Returns {@link Optional#empty()} if no proxy is configured.
     * <p>
     * Deprecated since we actually want to get the current proxy independent of its source which
     * can be retrieved via {@link GlobalProxySearch}. For Java-only proxy configuration, see
     * {@link #getConfigFromSystemProperties(ProxyProtocol...)}.
     *
     * @return {@link GlobalProxyConfig} if configuration is present
     * @deprecated use {@link GlobalProxySearch#getCurrentFor(URI, ProxyProtocol...)} for instead
     */
    @Deprecated(since = "5.3.0", forRemoval = true)
    public static Optional<GlobalProxyConfig> getCurrent() {
        return getConfigFromSystemProperties(ProxyProtocol.values());
    }

    /**
     * Retrieves the proxy configuration *only* from System properties.
     *
     * @param protocols a selection of protocols for which to get the config
     * @return GlobalProxyConfig
     * @since 6.3
     */
    public static Optional<GlobalProxyConfig> getConfigFromSystemProperties(final ProxyProtocol... protocols) {
        return getConfigFromSystemProperties(System.getProperties(), protocols);
    }

    static Optional<GlobalProxyConfig> getConfigFromSystemProperties(final Properties properties,
        final ProxyProtocol... protocols) {
        return getProtocol(properties, protocols) //
            .map(p -> new GlobalProxyConfig(p, //
                getHost(properties, p).get(), //
                getPort(properties, p).orElse(null), //
                useAuthentication(properties, p), //
                getUsername(properties, p).orElse(null), //
                getPassword(properties, p).orElse(null), //
                useExcludedHosts(properties, p), //
                getExcludedHosts(properties, p).orElse(null) //
            ));
    }

    /**
     * Hides the constructor.
     */
    private GlobalProxyConfigProvider() {
    }
}
