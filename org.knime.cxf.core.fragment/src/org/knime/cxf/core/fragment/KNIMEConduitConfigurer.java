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
import org.knime.core.util.proxy.ProxyProtocol;
import org.knime.core.util.proxy.search.GlobalProxySearch;

/**
 * Automatic configurer for HTTP clients by Apache CXF. Always enables the KNIME-specific SSL configuration and uses the
 * current global proxy configuration from the {@link GlobalProxySearch}.
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
     */
    static void configureProxyOnConduit(final HTTPConduit conduit, final String address) {
        // Try to create URI to match on proxy exclusion below. Proxy selection works for a null URI as well.
        URI uri = null;
        try {
            uri = new URI(address);
        } catch (URISyntaxException ignored) { // NOSONAR
        }
        // Noop if no proxy protocol was configured.
        final var maybeProxyConfig = GlobalProxySearch.getCurrentFor(uri);
        if (maybeProxyConfig.isEmpty()) {
            return;
        }
        final var proxyConfig = maybeProxyConfig.get();

        modifyClientPolicy(conduit, policy -> {
            policy.setProxyServer(proxyConfig.host());
            policy.setProxyServerPort(proxyConfig.intPort());
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

    private static void modifyClientPolicy(final HTTPConduit conduit, final Consumer<HTTPClientPolicy> setter) {
        final var client = Objects.requireNonNullElseGet(conduit.getClient(), HTTPClientPolicy::new);
        setter.accept(client);
        conduit.setClient(client);
    }
}
