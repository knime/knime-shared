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
 *   Aug 10, 2023 (Leon Wenzler, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.util.proxy;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.knime.core.util.KNIMEServerHostnameVerifier;
import org.knime.core.util.Pair;
import org.knime.core.util.proxy.search.GlobalProxySearch;

/**
 * Consistently builds {@link URLConnection} with a proxy if configured.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.2
 */
public final class URLConnectionFactory {

    private static final Log LOGGER = LogFactory.getLog(URLConnectionFactory.class);

    /**
     * Retrieves the {@link URLConnection} for a given @{link URL}, and performs
     * custom configuration (incl. proxies, HTTP redirects, and hostname verification).
     *
     * @param url the URL to open the connection for
     * @return opened URLConnection
     * @throws IOException if something went wrong
     */
    public static URLConnection getConnection(final URL url) throws IOException {
        final var proxyConfig = resolveJavaNetProxy(url);
        final var proxy = proxyConfig.getFirst();

        // although we could explictly configure URL connections with NO_PROXY, the NO_PROXY case
        // is likely for non-HTTP(S) URLs which have URL handlers that do not support the
        // URL#openConnection(Proxy) API and will throw an UnsupportedOperationException
        if (proxy == null || Proxy.NO_PROXY.equals(proxy)) {
            return completeConfiguration(url.openConnection());
        }

        URLConnection connection;
        try {
            connection = url.openConnection(proxy);
            // handle proxy requiring authentication
            final var authenticator = proxyConfig.getSecond();
            if (authenticator != null && connection instanceof HttpURLConnection httpConnection) {
                httpConnection.setAuthenticator(authenticator);
            }
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("URL handler threw UOE while invoking URL#openConnection(Proxy), re-trying without proxy", e);
            connection = url.openConnection();
        }
        return completeConfiguration(connection);
    }

    /**
     * Queries the {@link GlobalProxySearch} for the global proxy configuration.
     * If a config is found that does not exclude the given {@link URL} it is returned
     * with the corresponding {@link Authenticator} holding credentials.
     *
     * @param url the URL to open the connection for
     * @return pair of java.net proxy and authenticator
     */
    private static Pair<Proxy, Authenticator> resolveJavaNetProxy(final URL url) {
        // return early for file:// URLs before starting to parse as URI
        if ("file".equalsIgnoreCase(url.getProtocol())) {
            return Pair.create(Proxy.NO_PROXY, null);
        }
        final var uri = createNullableURI(url);
        return GlobalProxySearch.getCurrentFor(uri) //
            .filter(cfg -> !cfg.isHostExcluded(uri)) //
            .map(GlobalProxyConfig::forJavaNetProxy) //
            .orElseGet(() -> Pair.create(Proxy.NO_PROXY, null));
    }

    /**
     * Adds common configuration to {@link URLConnection}s. Uses configuration pieces from these classes.
     * <ul>
     *   <li> <tt>org.owasp.dependencycheck.utils.URLConnectionFactory</tt>
     *   <li> <tt>com.knime.enterprise.server.rest.api.Util</tt>
     *   <li> <tt>com.knime.enterprise.server.rest.api.UTF8BasicAuthSupplier</tt>
     *   <li> <tt>org.knime.cxf.core.fragment.KNIMEConduitConfigurer</tt>
     * </ul>
     *
     * @param connection the URL connection
     * @return same connection, but with additional configuratin
     */
    private static URLConnection completeConfiguration(final URLConnection connection) {
        // (1) it is discouraged to pass basic server authentication per user info instead of the header, see
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#access_using_credentials_in_the_url
        final var userInfo = connection.getURL().getUserInfo();
        if (userInfo != null) {
            LOGGER.info("Detected server authentication in user info of URL, moving to header");
            final var encodedAuth = Base64.getEncoder().encodeToString(userInfo.getBytes(StandardCharsets.UTF_8));
            connection.addRequestProperty(HttpHeaders.AUTHORIZATION, "Basic %s".formatted(encodedAuth));
        }

        if (connection instanceof HttpURLConnection httpConnection) {
            // (2) we don't want to allow interactive authentication or popups
            // at least for the clients reading this field (unlike Eclipse's NetAuthenticator)
            httpConnection.setAllowUserInteraction(false);

            // (3) generally, we additionally always follow redirects (as in KNIMEConduitConfigurer)
            httpConnection.setInstanceFollowRedirects(true);

            // (4) use own host name verifier if it is a HTTPS connection
            if (httpConnection instanceof HttpsURLConnection httpsConnection) {
                httpsConnection.setHostnameVerifier(KNIMEServerHostnameVerifier.getInstance());
            }
        }
        return connection;
    }

    /**
     * Only a utility class.
     */
    private URLConnectionFactory() {
    }

    /**
     * Creates a {@link URI} from a {@link URL}, will return {@code null} on any failure.
     *
     * @param url the string encode and turn into an URI
     * @return nullable URI
     */
    private static URI createNullableURI(final URL url) {
        if (url != null) {
            try {
                return url.toURI();
            } catch (URISyntaxException e) {
                LOGGER.debug("Could not parse URL as URI for proxy selection", e);
            }
        }
        return null;
    }
}
