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
 *   Jul 25, 2023 (Leon Wenzler, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.util.proxy;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.knime.core.util.Pair;

/**
 * Captures the current global proxy configuration (in System properties).
 * <p>
 * The {@code nonProxyHosts} property (stored here as {@code excludedHosts}) follows the syntax described
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/net/doc-files/net-properties.html">here</a>.
 * <p>
 * Example: {@code nonProxyHosts="localhost|www.google.*|*.knime.com"} where '{@code |}' are host separators,
 * '{@code .}' are real dots (unlike in regexes), and '{@code *}' is any char (length >= 0).
 *
 * @param protocol proxy protocol, either HTTP, HTTPS, or SOCKS
 * @param host proxy server hostname
 * @param port proxy server port
 * @param useAuthentication whether to use (Basic) authentication
 * @param username proxy server username
 * @param password proxy server password
 * @param useExcludedHosts whether to exclude hostnames from using the proxy
 * @param excludedHosts list of exluded hostnames
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.2
 */
public record GlobalProxyConfig(ProxyProtocol protocol, String host, String port, boolean useAuthentication,
    String username, String password, boolean useExcludedHosts, String excludedHosts) {

    private static final Logger LOGGER = Logger.getLogger(GlobalProxyConfig.class.getName());

    /**
     * Attemps to parse the string-stored {@link #port()} into an integer. Returns the protocol's
     * default port if parsing fails. See {@link ProxyProtocol#getDefaultPort()}.
     *
     * @return integer port
     * @since 6.3
     */
    public int intPort() {
        // attempt to parse port, fall back to protocol's default port
        var intPort = protocol.getDefaultPort();
        try {
            intPort = Integer.parseInt(String.valueOf(port()));
        } catch (NumberFormatException nfe) {
            LOGGER.log(Level.WARNING,
                String.format("Cannot parse proxy port \"%s\", defaulting to \"%s\"", port(), intPort),
                nfe);
        }
        return intPort;
    }

    /**
     * Checks whether the host of the given URI is excluded by this proxy configuration.
     *
     * @param uri the URI which to connect to
     * @return whether the given URI is excluded from using the proxy
     * @since 6.3
     */
    public boolean isHostExcluded(final URI uri) {
        if (uri == null) {
            return false;
        }
        final var uriHost = uri.getHost();
        if (!useExcludedHosts || excludedHosts == null || StringUtils.isBlank(uriHost)) {
            return false;
        }

        // translation from pattern to regex taken from `org.apache.cxf.transport.http.RegexBuilder#build(String)`
        return uriHost.matches(excludedHosts.replace(".", "\\.").replace("*", ".*"));
    }

    // -- CONVERTING TO OTHER CONFIGS --

    /**
     * Converts this proxy configuration to Java's {@link Proxy} along with an authenticator,
     * if authentication is needed. The {@link Authenticator} is never null,
     * but if authentication is *not* needed, it returns {@code null} on request.
     *
     * @return Java Net proxy specification
     * @since 6.3
     */
    public Pair<Proxy, Authenticator> forJavaNetProxy() {
        final var authenticator = new Authenticator() {
            private boolean verifyRequestor() {
                // we omit matching on protocol since (1) #getRequestingProtocol() is not consistent
                // and (2) with ALL_PROXY configured proxies do not specify a protocol to match on
                return getRequestorType() == RequestorType.PROXY //
                        && StringUtils.equals(getRequestingHost(), host()) //
                        && getRequestingPort() == intPort();
            }

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (!useAuthentication() || !verifyRequestor()) {
                    return null;
                }
                return new PasswordAuthentication(username(), password().toCharArray());
            }
        };
        final var proxyType = protocol() == ProxyProtocol.SOCKS ? Proxy.Type.SOCKS : Proxy.Type.HTTP;
        final var proxyAddress = InetSocketAddress.createUnresolved(host(), intPort());
        return Pair.create(new Proxy(proxyType, proxyAddress), authenticator);
    }

    /**
     * Converts this proxy configuration to Apache's {@link org.apache.http.client.HttpClient},
     * along with a credentials if needed. The {@link CredentialsProvider} is never null,
     * but if authentication is *not* needed, it does not contain credentials.
     *
     * @return Apache HttpClient proxy specification
     * @since 6.3
     */
    public Pair<HttpHost, CredentialsProvider> forApacheHttpClient() {
        final var httpHost = new HttpHost(host(), intPort(), protocol().asLowerString());
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (useAuthentication()) {
            credentialsProvider.setCredentials( //
                new AuthScope(httpHost), //
                new UsernamePasswordCredentials(username(), password()));
        }
        return Pair.create(httpHost, credentialsProvider);
    }
}
