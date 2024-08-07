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
 *   Jul 2, 2024 (lw): created
 */
package org.knime.core.util.auth;

import java.net.Authenticator;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knime.core.util.proxy.ProxySelectorAdapter;
import org.knime.core.util.proxy.search.GlobalProxySearch;

/**
 * Provides proxy authentication for authentication requests from proxies
 * provided by the {@link ProxySelectorAdapter}.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.4
 */
final class ProxyAuthenticatorAdapter extends DelegatingAuthenticator {

    private static final Log LOGGER = LogFactory.getLog(ProxyAuthenticatorAdapter.class);

    /**
     * @param delegate upstream authenticator
     */
    ProxyAuthenticatorAdapter(final Authenticator delegate) {
        super(delegate);
    }

    @Override
    protected OptionalAuthentication getOwnAuthentication() {
        /*
         * If the current request for authentication was made within an auth-suppressed context,
         * only the explicitly supplied authentication should be used, accepting a failure to authorize.
         * Thus, avoid interference here.
         */
        if (SuppressingAuthenticator.isInSuppressedContext()) {
            return OptionalAuthentication.empty();
        }
        final var uri = createNullableURI(getRequestingURL());
        return OptionalAuthentication.ofNullable(GlobalProxySearch.getCurrentFor(uri) //
            .filter(cfg -> !cfg.isHostExcluded(uri)) //
            .map(cfg -> cfg.forJavaNetProxy().getSecond()) //
            .map(auth -> auth.requestPasswordAuthenticationInstance( //
                getRequestingHost(), //
                getRequestingSite(), //
                getRequestingPort(), //
                getRequestingProtocol(), //
                getRequestingPrompt(), //
                getRequestingScheme(), //
                getRequestingURL(), //
                getRequestorType())) //
            .orElse(null));
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
