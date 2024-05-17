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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.net.proxy.IProxyService;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Accesses Java's System.properties to retrieve proxy-relevant properties.
 * Returns every property as Optional<String>.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.2
 */
public final class GlobalProxyConfigProvider {

    // Keys used to identify System properties.

    static final String HOST_KEY = "proxyHost";

    static final String PORT_KEY = "proxyPort";

    static final String USER_KEY = "proxyUser";

    static final String PASSWORD_KEY = "proxyPassword";

    static final String EXCLUDED_HOSTS_KEY = "nonProxyHosts";

    // Proxy service reference.

    private static final ServiceTracker<IProxyService, IProxyService> PROXY_SERVICE_TRACKER;

    static {
        final var bundle = FrameworkUtil.getBundle(GlobalProxyConfigProvider.class);
        if (bundle != null) {
            // using proxy service class name as String to avoid initialization of the class
            PROXY_SERVICE_TRACKER = new ServiceTracker<>(bundle.getBundleContext(), //
                "org.eclipse.core.net.proxy.IProxyService", null);
            PROXY_SERVICE_TRACKER.open();
        } else {
            PROXY_SERVICE_TRACKER = null;
        }
    }

    /**
     * @see GlobalProxyConfigProvider#getProtocol(Properties, ProxyProtocol...)
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
     * @see GlobalProxyConfigProvider#getHost(Properties, ProxyProtocol)
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
     * @see GlobalProxyConfigProvider#getPort(Properties, ProxyProtocol)
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
     * @see GlobalProxyConfigProvider#useAuthentication(Properties, ProxyProtocol)
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
     * @see GlobalProxyConfigProvider#getUsername(Properties, ProxyProtocol)
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
     * @see GlobalProxyConfigProvider#getPassword(Properties, ProxyProtocol)
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
     * @see GlobalProxyConfigProvider#getExcludedHosts(Properties, ProxyProtocol)
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
     * @see GlobalProxyConfigProvider#useExcludedHosts(Properties, ProxyProtocol)
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

    // -- PROXY SEARCH STRATEGIES --

    private static Optional<GlobalProxyConfig> getConfigFromJava(final URI uri) {
        // try extracting the protocol from the URI's scheme
        var validProtocols = ProxyProtocol.values();
        if (uri != null) {
            final var extractedName = StringUtils.upperCase(uri.getScheme());
            final var extractedProcotol = EnumUtils.getEnum(ProxyProtocol.class, extractedName);
            if (extractedProcotol != null) {
                validProtocols = new ProxyProtocol[] { extractedProcotol };
            }
        }
        final var properties = System.getProperties();
        return getProtocol(properties, validProtocols) //
            .map(p -> new GlobalProxyConfig(p, //
                getHost(properties, p).get(), //
                getPort(properties, p).get(), //
                useAuthentication(properties, p), //
                getUsername(properties, p).orElse(null), //
                getPassword(properties, p).orElse(null), //
                useExcludedHosts(properties, p), //
                getExcludedHosts(properties, p).orElse(null) //
            ));
    }

    private static Optional<GlobalProxyConfig> getConfigFromEclipse(final URI uri) {
        // we do not initialize the service here, this is the responsibility of other bundles
        // some applications using the 'GlobalProxySearch' API may not initialize the service at all
        if (PROXY_SERVICE_TRACKER == null) {
            // can be null in non-Eclipse applications as there is no bundle context
            return Optional.empty();
        }
        final var service = PROXY_SERVICE_TRACKER.getService();
        if (service == null || !service.isProxiesEnabled()) {
            return Optional.empty();
        }
        final var excludedHosts =
            String.join("|", Objects.requireNonNullElse(service.getNonProxiedHosts(), new String[0]));

        // if the URI is null, choose proxy data independently (retrieve any configuration)
        final var validData = uri != null ? service.select(uri) : service.getProxyData();
        for (var data : validData) {
            if (StringUtils.isNotBlank(data.getHost())) {
                return Optional.of(new GlobalProxyConfig( //
                    ProxyProtocol.valueOf(data.getType()), //
                    data.getHost(), //
                    String.valueOf(data.getPort()), //
                    StringUtils.isNotBlank(data.getUserId()) && StringUtils.isNotEmpty(data.getPassword()), //
                    data.getUserId(), //
                    data.getPassword(), //
                    StringUtils.isNotEmpty(excludedHosts), //
                    excludedHosts));
            }
        }
        return Optional.empty();
    }

    // -- CURRENT GLOBAL PROXY CONFIG --

    /**
     * Retrieves the current global proxy configuration and stores it in a read-only format.
     * Returns {@link Optional#empty()} if no proxy is configured.
     *
     * @return {@link GlobalProxyConfig}
     */
    public static Optional<GlobalProxyConfig> getCurrent() {
        return getCurrentFor((URI)null);
    }

    /**
     * Retrieves the current global proxy configuration for given {@link URL} (which may be null)
     * Returns {@link Optional#empty()} if no proxy is configured.
     *
     * @param url nullable URL to which to connect via a proxy
     * @return proxy configuration, if present and not excluding URL
     * @since 6.2
     */
    public static Optional<GlobalProxyConfig> getCurrentFor(final URL url) {
        URI uri = null;
        if (url != null) {
            try {
                uri = url.toURI();
            } catch (URISyntaxException ignored) { // NOSONAR
                // ignored, falls through and uses null URI
            }
        }
        return getCurrentFor(uri).filter(cfg -> !cfg.isHostExcluded(url));
    }

    /**
     * Retrieves the current global proxy configuration for given {@link URI} (which may be null)
     * Returns {@link Optional#empty()} if no proxy is configured.
     * <p>
     * Sequentially, first checks Java's {@link System} properties (existing API in this class).
     * If it is not present, Eclipse's proxy service is checked for a proxy configuration.
     * Note that the service may not be initialized, in this case the check returns {@link Optional#empty()}.
     * </p>
     *
     * @param uri nullable URI to which to connect via a proxy
     * @return proxy configuration, if present and not excluding URI
     * @since 6.2
     */
    public static Optional<GlobalProxyConfig> getCurrentFor(final URI uri) {
        // (1) check Java system properties
        final var javaResult = getConfigFromJava(uri);
        if (javaResult.filter(cfg -> !cfg.isHostExcluded(uri)).isPresent()) {
            return javaResult;
        }

        // (2) check Eclipse proxy service
        final var eclipseResult = getConfigFromEclipse(uri);
        if (eclipseResult.filter(cfg -> !cfg.isHostExcluded(uri)).isPresent()) {
            return eclipseResult;
        }

        return Optional.empty();
    }

    /**
     * Hides the constructor.
     */
    private GlobalProxyConfigProvider() {
    }
}
