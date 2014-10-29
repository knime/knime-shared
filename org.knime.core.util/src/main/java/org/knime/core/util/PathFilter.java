/* ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright by KNIME.com, Zurich, Switzerland
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
 * Created on 01.11.2013 by thor
 */
package com.knime.enterprise.repository.util;

import java.nio.file.Path;

/**
 * An interface that is implemented by objects that decide if a path should be accepted or filtered.
 * @since 3.8
 */
public interface PathFilter {
    /**
     * Decides if the given path should be accepted or filtered.
     *
     * @param path the path to be tested
     *
     * @return <tt>true</tt> if the path should be accepted, <code>false</code> otherwise
     */
    boolean accept(Path path);
}
