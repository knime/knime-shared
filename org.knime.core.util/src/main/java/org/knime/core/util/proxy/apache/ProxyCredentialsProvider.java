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
 *   Jun 24, 2024 (lw): created
 */
package org.knime.core.util.proxy.apache;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.knime.core.util.proxy.search.GlobalProxySearch;

/**
 * Basic credentials provider that first queries the {@link GlobalProxySearch},
 * then checks its stored {@link Credentials}.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.3
 */
public final class ProxyCredentialsProvider extends BasicCredentialsProvider {

    private static final Log LOGGER = LogFactory.getLog(ProxyCredentialsProvider.class.getName());

    /**
     * Instance object for a state-less version of the credentials provider. Does not use a fallback {@link URI} for
     * auth scope parsing.
     * <p>
     * Note: This might yield inaccurate credential results when requesting {@link AuthScope#ANY}, as an arbitrary
     * proxy will be requested for {@code null} constraints (i.e. no address, no protocol). If a {@link URI} is
     * specified as fallback in {@link #ProxyCredentialsProvider(URI)}, the proxy will be selected according to that.
     * </p>
     */
    public static final ProxyCredentialsProvider INSTANCE = new ProxyCredentialsProvider(null);

    private final URI m_defaultTarget;

    /**
     * Public constructor for registering a fallback {@link URI} for parsing {@link AuthScope}s.
     * @param defaultTarget
     */
    public ProxyCredentialsProvider(final URI defaultTarget) {
        m_defaultTarget = defaultTarget;
    }

    /**
     * Creates a {@link URI} based on an {@link AuthScope} instance.
     *
     * @param authScope scope
     * @return constructed URI, uses the the default URI as fallback address
     */
    private URI createURIFromAuthScope(final AuthScope authScope) {
        if (authScope == AuthScope.ANY) {
            return m_defaultTarget;
        }
        final var httpHost = authScope.getOrigin() != null ? authScope.getOrigin()
            : new HttpHost(authScope.getHost(), authScope.getPort());
        try {
            return ProxyHttpRoutePlanner.createURIFromHttpHost(httpHost);
        } catch (URISyntaxException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Could not create URI target for proxy search on input \"%s\", defaulting to target: %s"
                    .formatted(httpHost, m_defaultTarget), e);
            }
            return m_defaultTarget;
        }
    }

    @Override
    public Credentials getCredentials(final AuthScope authScope) {
        final var uri = createURIFromAuthScope(authScope);
        return GlobalProxySearch.getCurrentFor(uri) //
            .filter(cfg -> !cfg.isHostExcluded(uri)) //
            .map(cfg -> cfg.forApacheHttpClient().getSecond().getCredentials(authScope)) //
            .filter(Objects::nonNull) //
            .orElseGet(() -> super.getCredentials(authScope));
    }
}
