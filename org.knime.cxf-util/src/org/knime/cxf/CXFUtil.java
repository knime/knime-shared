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
 *   24.05.2023 (thor): created
 */
package org.knime.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;

/**
 * Utilities for working with Apache CXF.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 */
public final class CXFUtil {
    private CXFUtil() {
        // empty
    }

    /**
     * Initializes the Apache CXF JAX-RS classes for usage in OSGi.
     *
     * @param classFromBundle any class from the bundle that wants to use JAX-RS functionality
     * @deprecated JAX-RS runtime is configured via a CXF fragment
     */
    @Deprecated(since = "5.2.0", forRemoval = true)
    public static void initializeJAXRSRuntime(final Class<?> classFromBundle) { // NOSONAR
        // Method no longer in use, see AP-20749 - the jaxrs runtime + CXF is configured via a fragment.
        // This method will be removed in a future version.
    }

    /**
     * Returns and possibly creates a bus for the current thread. It makes sure that the correct class loader is set so
     * that extensions from dependencies are loaded.
     *
     * @param classFromBundle any class from the bundle that want to use CXF funcationality
     * @return the thread's bus
     * @deprecated JAX-RS runtime is configured via a CXF fragment
     */
    @Deprecated(since = "5.2.0", forRemoval = true)
    public static Bus getThreadDefaultBus(final Class<?> classFromBundle) { // NOSONAR
        // Method no longer in use, see AP-20749 - the jaxrs runtime + CXF is configured via a fragment.
        // This method will be removed in a future version.
        return BusFactory.getThreadDefaultBus();
    }
}
