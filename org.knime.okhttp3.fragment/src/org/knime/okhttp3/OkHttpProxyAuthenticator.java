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
 *   Mar 10, 2026 (lw): created
 */
package org.knime.okhttp3;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.net.URIBuilder;
import org.knime.core.util.Pair;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.ProxyProtocol;
import org.knime.core.util.proxy.ProxySelectorAdapter;
import org.knime.core.util.proxy.search.GlobalProxySearch;

import okhttp3.Address;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * An {@link Authenticator} implementation to perform proxy authentication.
 * Credentials are fetched from our {@link GlobalProxySearch}.
 * <p>
 * Ideally, the proxy-selector in OkHttp (see {@link Address#proxySelector()}) has already
 * been set to our proxy-adapter - the {@link ProxySelectorAdapter}!
 * </p>
 *
 * @author Alexander Bondaletov
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
public class OkHttpProxyAuthenticator implements Authenticator {

    @Override
    public Request authenticate(final Route route, final Response response) throws IOException {
        if (response.request().header(HttpHeaders.PROXY_AUTHORIZATION) != null) {
            return null; // Give up, we've already failed to authenticate.
        }

        // (1) Convert to `java.net.URI` to detect the proxy for.
        final var httpUrl = route.address().url();
        URI httpUri;
        try {
            httpUri = toHttpUri(httpUrl);
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        // (2) Check if a proxy is used and if it's authenticated.
        final var proxy = GlobalProxySearch.getCurrentFor(httpUri).map(OkHttpProxyAuthenticator::fromGlobalProxyConfig);
        final var credentials = proxy.flatMap(Pair::getSecond).orElse(null);
        if (credentials == null) {
            return null;
        }

        // (3) Append proxy-authorization header value.
        return response.request().newBuilder().header(HttpHeaders.PROXY_AUTHORIZATION, credentials).build();
    }

    /**
     * Rebuilds the Kotlin-based {@link HttpUrl} semantically to create a {@link URI}
     * object. Because of the semantic reconstruction, it is unlikely that this
     * method will throw a {@link URISyntaxException}, but it may.
     *
     * @param httpUrl a Kotlin-based {@code okhttp3.HttpUrl} object
     * @return a Java-based {@code java.net.URI} object
     * @throws URISyntaxException if something went wrong
     */
    private static URI toHttpUri(final HttpUrl httpUrl) throws URISyntaxException {
        final var builder = new URIBuilder() //
            .setScheme(httpUrl.scheme()) //
            .setHost(httpUrl.host()) //
            .setPort(httpUrl.port()) //
            .setPathSegments(httpUrl.pathSegments()) //
            .setFragment(httpUrl.fragment());
        for (var i = 0; i < httpUrl.querySize(); i++) {
            builder.addParameter(httpUrl.queryParameterName(i), httpUrl.queryParameterValue(i));
        }
        return builder.build();
    }

    /**
     * Converts this proxy configuration to what OkHttp's clients expect (along with credentials).
     * The credentials are a {@link String} suitable for use as the value of an Basic authentication
     * header (i.e. the literal {@code "Basic "} prefix followed by a {@link java.util.Base64}-encoded
     * {@code username:password} payload as returned by {@link Credentials#basic(String, String)}).
    * <p>
     * The credentials are absent if no authentication is to be used for this proxy.
     * The {@link HttpUrl} is always non-{@code null}.
     * </p>
     *
     * @throws IllegalArgumentException for {@link ProxyProtocol#SOCKS}, incompatible with {@link HttpUrl}
     * @return OkHttp proxy specification
     */
    private static Pair<HttpUrl, Optional<String>> fromGlobalProxyConfig(final GlobalProxyConfig config)
        throws IllegalArgumentException {
        final var httpUrl = new HttpUrl.Builder() //
            .scheme(StringUtils.lowerCase(config.protocol().name())) // fails for SOCKS
            .host(config.host()) //
            .port(config.intPort()) //
            .build();

        if (config.useAuthentication()) {
            return Pair.create(httpUrl, Optional.of(Credentials.basic(config.username(), config.password())));
        } else {
            return Pair.create(httpUrl, Optional.empty());
        }
    }
}
