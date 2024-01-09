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
package org.knime.cxf.core.fragment;

import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.managers.ClientLifeCycleManagerImpl;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientLifeCycleListener;
import org.apache.cxf.endpoint.ClientLifeCycleManager;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HttpClientHTTPConduit;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener on HTTP client creation and destruction.
 * <p>
 * Resolves issues
 * <ul>
 *   <li>https://issues.apache.org/jira/browse/CXF-8885 and</li>
 *   <li>https://bugs.openjdk.org/browse/JDK-8308364.</li>
 * </ul>
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 * @since 5.1
 */
final class KNIMEClientLifeCycleListener implements ClientLifeCycleListener, CXFBusExtension<ClientLifeCycleManager> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KNIMEClientLifeCycleListener.class.getName());

    /**
     * Map of {@link HTTPConduit}-derived classes that hold references to {@link HttpClient}s which have to be cleared
     * so that the corresponding {@code SelectorManager} threads can shut down. Currently all of the fields in question
     * are called "{@code client}".
     */
    private static final Map<Class<?>, Field> CLIENT_FIELDS =
            List.of(HttpClientHTTPConduit.class, AsyncHTTPConduit.class).stream() //
            .collect(Collectors.toMap(Function.identity(), KNIMEClientLifeCycleListener::getClientField));

    /**
     * Extracts the field called {@code client} declared in the given class.
     *
     * @param clazz class to extract the {@code client} field from
     * @return extracted field, may be {@code null} if not present or not accessible
     */
    private static Field getClientField(final Class<?> clazz) {
        Field clientField = null;
        try {
            clientField = clazz.getDeclaredField("client");
            clientField.setAccessible(true); // NOSONAR sadly necessary for now
        } catch (NoSuchFieldException | SecurityException ex) {
            LOGGER.warn(String.format("Could not retrieve `client` field of '%s'", clazz.getSimpleName()), ex);
        }
        return clientField;
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
        for (final var e : CLIENT_FIELDS.entrySet()) {
            final var conduitClass = e.getKey();
            final var clientField = e.getValue();
            if (conduitClass.isInstance(conduit) && clientField != null) {
                try {
                    clientField.set(conduit, null); // NOSONAR sadly necessary for now
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOGGER.warn(String.format("Could not clear `client` field on '%s', "
                        + "selector manager thread is staling", conduitClass.getSimpleName()), ex);
                }
            }
        }
    }
}
