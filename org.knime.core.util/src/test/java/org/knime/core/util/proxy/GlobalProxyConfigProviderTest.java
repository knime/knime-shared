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
 *   May 29, 2024 (lw): created
 */
package org.knime.core.util.proxy;

import java.net.URI;
import java.util.Optional;
import java.util.Properties;

/**
 * Tests the {@link GlobalProxyConfigProvider}.
 */
class GlobalProxyConfigProviderTest extends AbstractProxyConfigProviderTest<Properties> {

    @Override
    Properties toProviderFormat(final GlobalProxyConfig config) {
        final var protocol = config.protocol();
        final var properties = new Properties();
        if (config.host() != null) {
            properties.put(protocol.createPropertyKey(GlobalProxyConfigProvider.HOST_KEY), config.host());
        }
        if (config.port() != null) {
            properties.put(protocol.createPropertyKey(GlobalProxyConfigProvider.PORT_KEY), config.port());
        }
        if (config.useAuthentication()) {
            properties.put(protocol.createPropertyKey(GlobalProxyConfigProvider.USER_KEY), config.username());
            properties.put(protocol.createPropertyKey(GlobalProxyConfigProvider.PASSWORD_KEY), config.password());
        }
        if (config.useExcludedHosts()) {
            properties.put(protocol.createPropertyKey(GlobalProxyConfigProvider.EXCLUDED_HOSTS_KEY),
                config.excludedHosts());
        }
        return properties;
    }

    @Override
    Optional<GlobalProxyConfig> toGlobalProxyConfig(final Properties format, final URI uri,
        final ProxyProtocol... protocols) {
        return GlobalProxyConfigProvider.getConfigFromSystemProperties(format, protocols);
    }
}
