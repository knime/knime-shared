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
 *   Aug 20, 2024 (lw): created
 */
package org.knime.core.util.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Map;

/**
 * 'Tinyproxy' context factory that creates {@link GlobalProxyConfig} objects,
 * based on running sidecars in the current test context.
 *
 * @see "https://github.com/tinyproxy/tinyproxy"
 */
public class TinyproxyTestContext {

    private static final String KNIME_TINYPROXY = "KNIME_TINYPROXY";

    private static final String KNIME_TINYPROXYAUTH = "KNIME_TINYPROXYAUTH";

    private static final String KNIME_TINYPROXYSTATS = "KNIME_TINYPROXYSTATS";

    /**
     * Using the {@link EnvironmentProxyConfigProvider} to parse environment variables.
     *
     * @param protocol the proxy protocol, necessary for constructing the config object
     * @param key the key of the proxy environment variable
     * @return {@link GlobalProxyConfig} representing the variable value
     */
    private static GlobalProxyConfig parseAsGlobalProxyConfig(final ProxyProtocol protocol, final String key) {
        return EnvironmentProxyConfigProvider
            .getConfigFromEnvironment(Map.of(protocol.name() + "_PROXY", System.getenv(key)), null, protocol)
            .orElseThrow();
    }

    private static void assertNonNullEnvVar(final String key) {
        assertThat(System.getenv(key)) //
            .as("Expected environment variable \"%s\" to be non-null", key) //
            .isNotNull();
    }

    /**
     * Returns the {@link GlobalProxyConfig} from a 'Tinyproxy' sidecar container,
     * not requiring user authentication.
     *
     * @param protocol the proxy protocol
     * @return {@link GlobalProxyConfig}
     */
    public static GlobalProxyConfig getWithoutAuth(final ProxyProtocol protocol) {
        assertNonNullEnvVar(KNIME_TINYPROXY);
        return parseAsGlobalProxyConfig(protocol, KNIME_TINYPROXY);
    }

    /**
     * Returns the {@link GlobalProxyConfig} from a 'Tinyproxy' sidecar container,
     * which *does* require user authentication.
     *
     * @param protocol the proxy protocol
     * @return {@link GlobalProxyConfig}
     */
    public static GlobalProxyConfig getWithAuth(final ProxyProtocol protocol) {
        assertNonNullEnvVar(KNIME_TINYPROXYAUTH);
        return parseAsGlobalProxyConfig(protocol, KNIME_TINYPROXYAUTH);
    }

    /**
     * Get {@link URI} of 'Tinyproxy' statistics (valid for both containers, with and without
     * authentication). Next to listing statistics, this host is useful for testing
     * since it is only available behind the proxy.
     *
     * @return {@link URI} pointing to the 'Tinyproxy' statistics host
     */
    public static URI getStatsURI() {
        assertNonNullEnvVar(KNIME_TINYPROXYSTATS);
        return URI.create(System.getenv(KNIME_TINYPROXYSTATS));
    }
}
