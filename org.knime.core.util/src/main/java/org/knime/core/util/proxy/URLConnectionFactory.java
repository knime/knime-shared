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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpHeaders;
import org.knime.core.util.KNIMEServerHostnameVerifier;
import org.knime.core.util.proxy.search.GlobalProxySearch;

/**
 * Consistently builds {@link URLConnection} with a proxy if configured.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.2
 */
public final class URLConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(URLConnectionFactory.class.getName());

    /**
     * Retrieves the {@link URLConnection} for a given @{link URL}.
     * Queries the {@link GlobalProxyConfigProvider} for the global proxy configuration.
     * If one is configured, it opens the connection using that config.
     *
     * @param url the URL to open the connection for
     * @return opened URLConnection
     * @throws IOException if something went wrong
     */
    public static URLConnection getConnection(final URL url) throws IOException {
        final URLConnection connection;
        final var proxyAuthPair = GlobalProxySearch.getCurrentFor(url) //
                .map(GlobalProxyConfig::forJavaNetProxy);
        if (proxyAuthPair.isPresent()) {
            final var proxy = proxyAuthPair.get().getFirst();
            connection = url.openConnection(proxy);
            // handle proxy requiring authentication
            final var authenticator = proxyAuthPair.get().getSecond();
            if (authenticator != null && connection instanceof HttpURLConnection httpConnection) {
                httpConnection.setAuthenticator(authenticator);
            }
        } else {
            connection = url.openConnection();
        }
        return completeConfiguration(connection);
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
            LOGGER.log(Level.INFO, "Detected server authentication in user info of URL, moving to header");
            final var encodedAuth = Base64.getEncoder().encodeToString(userInfo.getBytes(StandardCharsets.UTF_8));
            connection.addRequestProperty(HttpHeaders.AUTHORIZATION, "Basic %s".formatted(encodedAuth));
        }

        if (connection instanceof HttpURLConnection httpConnection) {
            // (2) we don't want to allow interactive authentication or popups
            // at least for the clients reading this field (unlike Eclipse's NetAuthenticator)
            httpConnection.setAllowUserInteraction(false);

            // (3) generally, we additionally always follow redirects (as configured in KNIMEConduitConfigurer)
            // but this is only possible to set statically for HttpURLConnections, which we don't want here

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
}
