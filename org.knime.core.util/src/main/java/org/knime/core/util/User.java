/*
 * ------------------------------------------------------------------------
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
 *   19.08.2011 (meinl): created
 */
package org.knime.core.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class with user-related functions such as getting the current user's name.
 *
 * @author Thorsten Meinl, University of Konstanz
 */
public final class User {

    private static String userName;

    private static boolean initialized;

    private User() {}

    /**
     * Returns the name of the current user. It does not use the <tt>user.name</tt> system property but relies on the
     * authentication modules.
     *
     * @return the user name or <code>null</code> if it cannot be determined
     */
    public static synchronized String getUsername() {
        if (!initialized) {
            initialized = true;
            String osSystemClassName;
            String getUserNameMethodName;
            if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC_OSX) {
                osSystemClassName = "com.sun.security.auth.module.UnixSystem";
                getUserNameMethodName = "getUsername";
            } else if (SystemUtils.IS_OS_WINDOWS) {
                osSystemClassName = "com.sun.security.auth.module.NTSystem";
                getUserNameMethodName = "getName";
            } else if (SystemUtils.IS_OS_SOLARIS) {
                osSystemClassName = "com.sun.security.auth.module.SolarisSystem";
                getUserNameMethodName = "getUsername";
            } else {
                // unknown OS, give up
                return null;
            }

            try {
                final Class<?> osSystemClass = Class.forName(osSystemClassName);
                final Object osSystem = osSystemClass.getConstructor().newInstance();
                userName = (String)osSystemClass.getMethod(getUserNameMethodName).invoke(osSystem);
            } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException e) {
                LogFactory.getLog(User.class).error("Could not determine user name using reflection", e);
            }
        }
        return userName;
    }
}
