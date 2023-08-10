/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME AG, Zurich, Switzerland
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.com
 * email: contact@knime.com
 * ---------------------------------------------------------------------
 *
 * History
 *   Created on Aug 8, 2023 by Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
package org.knime.cxf;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.managers.ClientLifeCycleManagerImpl;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientLifeCycleListener;
import org.apache.cxf.endpoint.ClientLifeCycleManager;
import org.apache.cxf.transport.http.HttpClientHTTPConduit;

/**
 * Listener on HTTP client creation and destruction.
 * Resolves issues
 *   - https://issues.apache.org/jira/browse/CXF-8885 and
 *   - https://bugs.openjdk.org/browse/JDK-8308364.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 5.1
 */
public class KNIMEClientLifeCycleListener implements ClientLifeCycleListener, CXFBusExtension<ClientLifeCycleManager> {

    private static final Logger LOGGER = Logger.getLogger(KNIMEClientLifeCycleListener.class.getName());

    private static final Field CLIENT_FIELD;
    static {
        Field clientField = null;
        try {
            clientField = HttpClientHTTPConduit.class.getDeclaredField("client");
            clientField.setAccessible(true); // NOSONAR sadly necessary for now
        } catch (NoSuchFieldException | SecurityException ex) {
            LOGGER.log(Level.WARNING, "Could not retrieve `client` field of `HttpClientHTTPConduit`", ex);
        }
        CLIENT_FIELD = clientField;
    }

    @Override
    public ClientLifeCycleManager getExtension() {
        return new ClientLifeCycleManagerImpl();
    }

    @Override
    public Class<ClientLifeCycleManager> getRegistrationType() {
        return ClientLifeCycleManager.class;
    }

    @Override
    public void setOnBus(final Bus bus) {
        CXFBusExtension.super.setOnBus(bus);
        final var registrationType = getRegistrationType();
        final var lifeCycleManager = registrationType.cast(bus.getExtension(registrationType));
        lifeCycleManager.registerListener(this);
    }

    @Override
    public void clientCreated(final Client client) {
        // no-op
    }

    @Override
    public void clientDestroyed(final Client client) {
        final var conduit = client.getConduit();
        if (CLIENT_FIELD != null && conduit instanceof HttpClientHTTPConduit) {
            try {
                CLIENT_FIELD.set(conduit, null); // NOSONAR sadly necessary for now
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                LOGGER.log(Level.WARNING,
                    "Could not nullify client field on HttpClientHTTPConduit, selector manager thread is staling", ex);
            }
        }
    }
}
