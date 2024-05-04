/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME AG, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 *
 * History
 *   Created on Jul 25, 2023 by Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
package org.knime.cxf.core.fragment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.function.Consumer;

import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.ProxyAuthorizationPolicy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HTTPConduitConfigurer;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.transports.http.configuration.ProxyServerType;
import org.knime.core.util.KNIMEServerHostnameVerifier;
import org.knime.core.util.KNIMEX509TrustManager;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.GlobalProxyConfigProvider;
import org.knime.core.util.proxy.ProxyProtocol;

/**
 * Automatic configurer for HTTP clients by Apache CXF. Always enables the KNIME-specific SSL configuration and uses the
 * current global proxy configuration from the {@link GlobalProxyConfigProvider}.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 */
final class KNIMEConduitConfigurer implements HTTPConduitConfigurer, CXFBusExtension<HTTPConduitConfigurer> {

    @Override
    public HTTPConduitConfigurer getExtension() {
        return this;
    }

    @Override
    public Class<HTTPConduitConfigurer> getRegistrationType() {
        return HTTPConduitConfigurer.class;
    }

    @Override
    public void configure(final String name, final String address, final HTTPConduit c) {
        final var conduit = Objects.requireNonNull(c);
        configureHTTP1OnConduit(conduit);
        configureSSLOnConduit(conduit);
        configureProxyOnConduit(conduit, address);
    }

    /**
     * Avoid using HTTP/2 because it is not well supported, e.g. by MinIO (as of 2023-08-25, see AP-20900).
     *
     * @param conduit of an HTTP client
     */
    static void configureHTTP1OnConduit(final HTTPConduit conduit) {
        modifyClientPolicy(conduit, policy -> policy.setVersion("1.1"));
    }

    /**
     * Adds the KNIME-specific SSL config to the conduit of an HTTP client.
     *
     * @param conduit of an HTTP client
     * @throws IllegalArgumentException if the given object is not a valid JAX-RS proxy
     */
    static void configureSSLOnConduit(final HTTPConduit conduit) {
        final var tlsParams = new TLSClientParameters();
        tlsParams.setHostnameVerifier(KNIMEServerHostnameVerifier.getInstance());
        tlsParams.setTrustManagers(new TrustManager[]{KNIMEX509TrustManager.getInstance()});
        conduit.setTlsClientParameters(tlsParams);

        // always enable auto-redirects
        modifyClientPolicy(conduit, policy -> policy.setAutoRedirect(true));
    }

    /**
     * Uses the System proxy properties to configure the REST request client if needed. Sets the server, port, user and
     * password to a <code>HTTPClientPolicy</code> and <code>ProxyAuthorizationPolicy</code> and adds them to the HTTP
     * conduit.
     *
     * @param conduit of an HTTP client
     * @param address string address of the web target
     */
    static void configureProxyOnConduit(final HTTPConduit conduit, final String address) {
        // Try to create URI to match on proxy exclusion below. Proxy selection works for a null URI as well.
        URI uri = null;
        try {
            uri = new URI(address);
        } catch (URISyntaxException ignored) { // NOSONAR
        }
        // Noop if no proxy protocol was configured.
        final var maybeProxyConfig = GlobalProxyConfigProvider.getCurrentFor(uri);
        if (maybeProxyConfig.isEmpty()) {
            return;
        }
        final var proxyConfig = maybeProxyConfig.get();

        modifyClientPolicy(conduit, policy -> {
            policy.setProxyServer(proxyConfig.host());
            policy.setProxyServerPort(getProxyPort(proxyConfig));
            policy.setProxyServerType(
                proxyConfig.protocol() == ProxyProtocol.SOCKS ? ProxyServerType.SOCKS : ProxyServerType.HTTP);
            policy.setNonProxyHosts(StringUtils.defaultIfEmpty(proxyConfig.excludedHosts(), null));
        });

        // Setting up the proxy authentication data if used.
        if (proxyConfig.useAuthentication()) {
            /**
             * Previously, we assumed a limitation with Apache CXF's synchronous client (Java URLConnection under the
             * hood), and switched to the asynchronous client if authentication was required. While this issue has been
             * resolved, another was identified:
             *
             * Starting form Java 8u111, Basic proxy authentication via HTTPS was disabled. It can be re-enabled by
             * setting a Java system property, see: https://knime.com/faq#42
             */
            final var authorization =
                Objects.requireNonNullElseGet(conduit.getProxyAuthorization(), ProxyAuthorizationPolicy::new);
            authorization.setUserName(proxyConfig.username());
            authorization.setPassword(proxyConfig.password());
            authorization.setAuthorizationType("Basic");
            conduit.setProxyAuthorization(authorization);
        }
    }

    private static int getProxyPort(final GlobalProxyConfig proxyConfig) {
        try {
            return Integer.parseInt(proxyConfig.port());
        } catch (NumberFormatException ignored) { // NOSONAR
            return proxyConfig.protocol().getDefaultPort();
        }
    }

    private static void modifyClientPolicy(final HTTPConduit conduit, final Consumer<HTTPClientPolicy> setter) {
        final var client = Objects.requireNonNullElseGet(conduit.getClient(), HTTPClientPolicy::new);
        setter.accept(client);
        conduit.setClient(client);
    }
}
