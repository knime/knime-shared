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
 *   Aug 29, 2023 (wiswedel): created
 */
package org.knime.cxf.core.fragment;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.common.util.SystemPropertyAction;
import org.slf4j.LoggerFactory;

/**
 * Added as part of AP-20749 - a KNIME custom bus factory serving two purposes:
 *
 * <ol>
 * <li>Register custom CXF extension to configure custom behavior such as http/1.1, and a client life cycle listener to
 * work around CXF bugs (CXF-8885)
 * <li>Amending the class path of the host fragment (org.apache.cxf.cxf-core) by the plug-ins added
 * as bundle dependencies, e.g. cxf-rt-transports-http.
 * </ol>
 *
 * @author Bernd Wiswedel
 * @since 5.1
 */
public final class KNIMECXFBusFactory extends CXFBusFactory {

    private static final Collection<CXFBusExtension<?>> CXF_BUS_EXTENSIONS =
            List.of(new KNIMEClientLifeCycleListener(), new KNIMEConduitConfigurer());

    static {
        LoggerFactory.getLogger(KNIMECXFBusFactory.class)
            .debug("Initialized {}", KNIMECXFBusFactory.class.getSimpleName());
    }

    @Override
    public Bus createBus(final Map<Class<?>, Object> e, final Map<String, Object> properties) {
        updateBusProperties(properties);
        final var currentThread = Thread.currentThread();
        final var oldClassLoader = currentThread.getContextClassLoader();
        currentThread.setContextClassLoader(KNIMECXFBusFactory.class.getClassLoader());
        try {
            final var bus = super.createBus(e, properties);
            CXF_BUS_EXTENSIONS.forEach(ext -> ext.setOnBus(bus));
            return bus;
        } finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }
    }

    /**
     * The Apache CXF bus creation via this {@link CXFBusFactory} happens so early that CXF's own
     * system properties are not read in at this point. Hence, busses would not be initialized with
     * the configured system properties.
     *
     * Here, we explicitly propagate all properties relevant to us.
     *
     * @param properties how the {@link Bus} (and all {@link WebClient}s using the bus) should be configured
     */
    private static void updateBusProperties(final Map<String, Object> properties) {
        final var systemToBusPropertyMap = Map.of(//
            "org.apache.cxf.transport.http.forceURLConnection", "force.urlconnection.http.conduit"//
        );
        // propagating system properties to bus properties - currently only booleans
        systemToBusPropertyMap.forEach((systemName, busName) -> {
            if (Boolean.parseBoolean(SystemPropertyAction.getProperty(systemName))) {
                properties.put(busName, Boolean.TRUE);
            }
        });
    }
}
