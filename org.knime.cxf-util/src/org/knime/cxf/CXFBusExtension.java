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

import org.apache.cxf.Bus;
import org.apache.cxf.extension.BusExtension;

/**
 * Classes that can be used as CXF bus extension, using <code>busExtension.setOnBus(bus)</code>.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @param <T> extension type
 * @since 5.2
 */
public interface CXFBusExtension<T> extends BusExtension {

    /**
     * Returns the extension value that will be injected into the {@link Bus}.
     *
     * @return new instance of the bus extension
     */
    T getExtension();

    /**
     * Returns the registration type that identifies a {@link Bus} extension.
     *
     * @return Class<?> registration type
     */
    @Override
    Class<T> getRegistrationType();

    /**
     * Configures the given bus using this bus extension.
     * Checks whether the bus is already using this extension, and if so, it won't re-set it.
     *
     * @param bus org.apache.cxf.Bus
     */
    default void setOnBus(final Bus bus) {
        final var registrationType = getRegistrationType();
        final var currentExtension = bus.getExtension(registrationType);
        if (currentExtension == null || !registrationType.isInstance(currentExtension)) {
            bus.setExtension(getExtension(), registrationType);
        }
    }
}
