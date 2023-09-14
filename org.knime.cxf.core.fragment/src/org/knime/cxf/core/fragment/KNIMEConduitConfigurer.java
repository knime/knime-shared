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
 *   Created on Jul 25, 2023 by Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
package org.knime.cxf.core.fragment;

import java.util.Objects;
import java.util.function.Consumer;

import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HTTPConduitConfigurer;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * Automatic configurer for HTTP clients by Apache CXF.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 */
final class KNIMEConduitConfigurer implements HTTPConduitConfigurer, CXFBusExtension<HTTPConduitConfigurer> {
    @Override
    public HTTPConduitConfigurer getExtension() {
        return this;
    }

    @Override
    public Class<HTTPConduitConfigurer> getRegistrationType() {
        return HTTPConduitConfigurer.class;
    }

    @Override
    public void configure(final String name, final String address, final HTTPConduit c) {
        final var conduit = Objects.requireNonNull(c);
        configureHTTP1OnConduit(conduit);
    }

    /**
     * Avoid using HTTP/2 because it is not well supported, e.g. by MinIO (as of 2023-08-25, see AP-20900).
     *
     * @param conduit of an HTTP client
     */
    static void configureHTTP1OnConduit(final HTTPConduit conduit) {
        modifyClientPolicy(conduit, policy -> policy.setVersion("1.1"));
    }

    private static void modifyClientPolicy(final HTTPConduit conduit, final Consumer<HTTPClientPolicy> setter) {
        final var client = Objects.requireNonNullElseGet(conduit.getClient(), HTTPClientPolicy::new);
        setter.accept(client);
        conduit.setClient(client);

    }
}
