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
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.knime.core.util.Pair;

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
        final var proxyAuthPair = getProxy(url);
        if (proxyAuthPair.isPresent()) {
            final var proxy = proxyAuthPair.get().getFirst();
            final var connection = url.openConnection(proxy);
            // handle proxy requiring authentication
            final var authenticator = proxyAuthPair.get().getSecond();
            if (authenticator != null && connection instanceof HttpURLConnection httpConnection) {
                httpConnection.setAuthenticator(authenticator);
            }
            return connection;
        } else {
            return url.openConnection();
        }
    }

    /**
     * Wraps the global proxy configuration in a {@link Proxy} object.
     */
    private static Optional<Pair<Proxy, Authenticator>> getProxy(final URL url) {
        final var maybeProxyConfig = GlobalProxyConfigProvider.getCurrentFor(url);
        if (maybeProxyConfig.isEmpty()) {
            // corresponds to a proxy using `java.net.Proxy.Type.DIRECT`
            return Optional.empty();
        }
        final var proxyConfig = maybeProxyConfig.get();
        if (proxyConfig.isHostExcluded(url)) {
            // do not use proxy, host is in exclude list
            return Optional.empty();
        }

        // parsing proxy port, defaults to the protocol's default port
        int intPort;
        try {
            intPort = Integer.parseInt(proxyConfig.port());
        } catch (NumberFormatException nfe) {
            final var defaultPort = proxyConfig.protocol().getDefaultPort();
            intPort = defaultPort;
            LOGGER.log(Level.INFO,
                String.format("Cannot parse proxy port \"%s\", defaulting to \"%s\"", proxyConfig.port(), defaultPort),
                nfe);
        }
        final var proxyAuthenticator = !proxyConfig.useAuthentication() ? null : new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(proxyConfig.username(), proxyConfig.password().toCharArray());
            }
        };
        final var proxyType = proxyConfig.protocol() == ProxyProtocol.SOCKS ? Proxy.Type.SOCKS : Proxy.Type.HTTP;
        final var proxyAddress = new InetSocketAddress(proxyConfig.host(), intPort);
        return Optional.of(Pair.create(new Proxy(proxyType, proxyAddress), proxyAuthenticator));
    }

    /**
     * Only a utility class.
     */
    private URLConnectionFactory() {
    }
}
