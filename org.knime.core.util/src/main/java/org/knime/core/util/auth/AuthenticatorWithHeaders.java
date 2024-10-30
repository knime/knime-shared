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
 *   Created on 8 Mar 2024 by oole
 */
package org.knime.core.util.auth;

import java.util.Map;

/**
 * Extended {@link Authenticator} which provides additional headers for authentication.
 *
 * @author Ole Ostergaard, KNIME AG, Zurich, Switzerland
 * @since 6.3
 */
public interface AuthenticatorWithHeaders extends Authenticator {

    /**
     * Returns the actual 'Authorization' header and other headers that should be be set for authorization inside the
     * hub.
     *
     * @return a map containing all headers that should be set for authorization inside the hub.
     * @throws CouldNotAuthorizeException if the authorization cannot be created
     */
    Map<String, String> getAuthorizationHeaders() throws CouldNotAuthorizeException;

}
