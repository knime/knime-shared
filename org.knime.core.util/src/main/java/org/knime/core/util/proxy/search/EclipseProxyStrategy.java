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
 *   Apr 30, 2024 (lw): created
 */
package org.knime.core.util.proxy.search;

import static java.util.Objects.requireNonNullElse;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.knime.core.util.proxy.GlobalProxyConfig;
import org.knime.core.util.proxy.ProxyProtocol;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Uses the {@link IProxyService} OSGi service by Eclipse to retrieve proxies
 * configured in this platform.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
final class EclipseProxyStrategy implements GlobalProxyStrategy {

    private final ServiceTracker<IProxyService, IProxyService> m_proxyServiceTracker;

    EclipseProxyStrategy() {
        // using proxy service class name as String to avoid initialization of the class
        m_proxyServiceTracker = new ServiceTracker<>(FrameworkUtil.getBundle(this.getClass()).getBundleContext(),
            "org.eclipse.core.net.proxy.IProxyService", null);
        m_proxyServiceTracker.open();
    }

    /**
     * Maps an instance of {@link IProxyData} to a {@link GlobalProxyConfig}.
     *
     * @param data IProxyData
     * @param nonProxiedHosts separate string array of excluded proxy hosts
     * @return GlobalProxyConfig instance
     */
    private static GlobalProxyConfig toGlobalProxyConfig(final IProxyData data, final String[] nonProxiedHosts) {
        final var username = data.getUserId();
        final var password = data.getPassword();
        final var excludedHosts = String.join("|", requireNonNullElse(nonProxiedHosts, new String[0]));
        return new GlobalProxyConfig( //
            ProxyProtocol.valueOf(data.getType()), //
            data.getHost(), //
            String.valueOf(data.getPort()), //
            StringUtils.isNotBlank(username) && StringUtils.isNotEmpty(password), //
            username, //
            password, //
            StringUtils.isNotEmpty(excludedHosts), //
            excludedHosts);
    }

    @Override
    public Optional<GlobalProxyConfig> getCurrentFor(final URI uri, final ProxyProtocol... protocols) {
        // we do not initialize the service here, this is the responsibility of other bundles
        // some applications using the 'GlobalProxySearch' API may not initialize the service at all
        final var service = m_proxyServiceTracker.getService();
        if (service == null || !service.isProxiesEnabled()) {
            return Optional.empty();
        }

        // if the URI is null, choose proxy data independently (retrieve any configuration)
        final var validData = uri != null ? service.select(uri) : service.getProxyData();
        final var validProtocols = Arrays.stream(protocols) //
                .map(ProxyProtocol::name) //
                .toArray(String[]::new);
        for (var data : validData) {
            if (StringUtils.equalsAnyIgnoreCase(data.getType(), validProtocols)
                    && StringUtils.isNotBlank(data.getHost())) {
                return Optional.of(toGlobalProxyConfig(data, service.getNonProxiedHosts()));
            }
        }
        return Optional.empty();
    }
}
