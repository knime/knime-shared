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
 *   Aug 14, 2024 (lw): created
 */
package org.knime.core.util.proxy.apache5;

import java.net.URI;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.eclipse.jdt.annotation.Owning;

/**
 * Factory methods for {@link CloseableHttpClient} instances that are
 * pre-configured with access to KNIME's proxy configuration.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.11
 * @see HttpClients
 */
public final class ProxyHttpClients {

    // -- CLIENT BUILDER FACTORY --

    /**
     * Creates builder object for construction of custom {@link CloseableHttpClient}
     * instances, pre-configured for proxy support.
     *
     * @return builder for a {@link HttpClient}
     */
    public static HttpClientBuilder custom() {
        return customForBuilder(HttpClients.custom());
    }

    /**
     * Configures a builder object for construction of custom {@link CloseableHttpClient}
     * instances, pre-configured for proxy support.
     *
     * @param builder a {@link HttpClientBuilder}, possibly already configured
     * @return builder for a {@link HttpClient}
     */
    public static HttpClientBuilder customForBuilder(final HttpClientBuilder builder) {
        return builder //
            .setRoutePlanner(ProxyHttpRoutePlanner.INSTANCE) //
            .setDefaultCredentialsProvider(ProxyCredentialsProvider.INSTANCE) //
            .setConnectionReuseStrategy(ProxyConnectionReuseStrategy.INSTANCE);
    }

    /**
     * Creates builder object for construction of custom {@link CloseableHttpClient}
     * instances, pre-configured for proxy support.
     *
     * @param defaultTarget a fallback {@link URI} to select the proxy credentials by,
     * useful for avoiding false positives when the the {@link ProxyCredentialsProvider}
     * matches its stored credentials against the active target
     * @return builder for a {@link HttpClient}
     */
    public static HttpClientBuilder custom(final URI defaultTarget) {
        // replacing the generic credentials provider with one that has a fallback
        // for matching credentials more accurately to the auth scope
        return custom() //
            .setDefaultCredentialsProvider(new ProxyCredentialsProvider(defaultTarget));
    }

    /**
     * Configures a builder object for construction of custom {@link CloseableHttpClient}
     * instances, pre-configured for proxy support.
     *
     * @param builder a {@link HttpClientBuilder}, possibly already configured
     * @param defaultTarget a fallback {@link URI} to select the proxy credentials by,
     * useful for avoiding false positives when the the {@link ProxyCredentialsProvider}
     * matches its stored credentials against the active target
     * @return builder for a {@link HttpClient}
     */
    public static HttpClientBuilder customForBuilder(final HttpClientBuilder builder, final URI defaultTarget) {
        // replacing the generic credentials provider with one that has a fallback
        // for matching credentials more accurately to the auth scope
        return customForBuilder(builder) //
            .setDefaultCredentialsProvider(new ProxyCredentialsProvider(defaultTarget));
    }

    // -- CLIENT FACTORY --

    /**
     * Creates {@link CloseableHttpClient} instance with default configuration,
     * but pre-configured for proxy support.
     *
     * @return proxy-supporting {@link HttpClient} with default settings
     */
    public static @Owning CloseableHttpClient createDefault() {
        return custom().build();
    }

    /**
     * Creates {@link CloseableHttpClient} instance with default configuration,
     * but pre-configured for proxy support.
     *
     * @param defaultTarget a fallback {@link URI} to select the proxy credentials by,
     * useful for avoiding false positives when the the {@link ProxyCredentialsProvider}
     * matches its stored credentials against the active target
     * @return proxy-supporting {@link HttpClient} with default settings
     */
    public static @Owning CloseableHttpClient createDefault(final URI defaultTarget) {
        return custom(defaultTarget).build();
    }

    /**
     * Hides the constructor.
     */
    private ProxyHttpClients() {
    }
}
