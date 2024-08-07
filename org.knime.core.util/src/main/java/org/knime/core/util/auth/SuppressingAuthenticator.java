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
 *   Dec 6, 2023 (leon.wenzler): created
 */
package org.knime.core.util.auth;

import java.awt.GraphicsEnvironment;
import java.io.Closeable;
import java.net.Authenticator;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Essentially a copy of {@code org.knime.core.util.ThreadLocalHTTPAuthenticator}, but with wider accessibility.
 * {@code ThreadLocalHTTPAuthenticator#suppressAuthenticationPopups} redirects here.
 *
 * This authenticator is only installed once in the initialization of the CorePlugin and allows us to
 * suppress Eclipse's authentication popups while not interfering with other libraries, such as CXF.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @since 6.3
 */
public final class SuppressingAuthenticator extends DelegatingAuthenticator {

    private static final Log LOGGER = LogFactory.getLog(SuppressingAuthenticator.class.getName());

    private static final ThreadLocal<MutableInt> SUPPRESS_POPUP = ThreadLocal.withInitial(MutableInt::new); // NOSONAR

    /**
     * Java property for suppressing authentication popups by Eclipse when
     * authentication cannot be found for an {@link Authenticator} request.
     * Is {@code false} per default.
     *
     * @since 6.4
     */
    public static final String PROPERTY_AUTH_POPUPS_ALLOWED = "knime.auth.popups.allowed";

    /**
     * Flag indicating whether a warning about the authenticator's inactivity has already been logged.
     */
    private static volatile boolean hasWarnedAboutInactivity;

    /**
     * @param delegate upstream authenticator
     */
    SuppressingAuthenticator(final Authenticator delegate) {
        super(delegate);
    }

    @Override
    protected OptionalAuthentication getOwnAuthentication() {
        if (GraphicsEnvironment.isHeadless() || !Boolean.getBoolean(PROPERTY_AUTH_POPUPS_ALLOWED)
            || isInSuppressedContext()) {
            // cannot be an Optional.empty, a null value is what we want
            return OptionalAuthentication.of(null);
        }
        return OptionalAuthentication.empty();
    }

    // -- AUTHENTICATOR SUPPRESSING CONTROL --

    static boolean isInSuppressedContext() {
        return SUPPRESS_POPUP.get().intValue() > 0;
    }

    /**
     * <p>Turns suppression of authentication popups on (*). Make sure to close the returned closeable after you have
     * performed all HTTP operations (hint: use try-with-resources).
     * This method never instantiates a new {@link SuppressingAuthenticator}.
     *
     * <p>(*) technically does not suppress popups, but all authenticators to delegate to.
     * See {@link #installAuthenticators()} for details.
     *
     * @see #installAuthenticators()
     * @return a closeable that enables popups again (if there were any before)
     */
    public static NewAuthenticationCloseable suppressDelegate() {
        if (!isInstalledGlobally() && !hasWarnedAboutInactivity && LOGGER.isWarnEnabled()) {
            LOGGER.warn(String.format("%s was not installed yet, authentication popups are not suppressed",
                SuppressingAuthenticator.class.getSimpleName()));
            hasWarnedAboutInactivity = true;
        }
        SUPPRESS_POPUP.get().increment();
        return () -> SUPPRESS_POPUP.get().decrement();
    }

    /**
     * Copy of {@code org.knime.core.util.ThreadLocalHTTPAuthenticator.AuthenticationCloseable}
     * for creating the same functionality here. The ThreadLocalHTTPAuthenticator will wrap it in
     * its own AuthenticationCloseable to preserve the API.
     *
     * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
     */
    @FunctionalInterface
    public interface NewAuthenticationCloseable extends Closeable {
        @Override
        void close();
    }
}
