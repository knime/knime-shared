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
 *   Jul 25, 2023 (Leon Wenzler, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.util.proxy;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Checks which schemes are disabled for auth tunneling. Does not allow Basic authentication for a HTTPS proxy per
 * default.
 *
 * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
 * @see <a href="https://docs.oracle.com/en/java/javase/16/core/networking-properties.html">Java Networking
 *      Properties</a>
 * @since 6.2
 */
public final class DisabledSchemesChecker {

    /**
     * Property that was introduced with Java 8u111 to disabled authentication schemes on HTTPS. Disables BASIC per
     * default. See https://www.oracle.com/java/technologies/javase/8u111-relnotes.html.
     */
    public static final String DISABLED_SCHEMES = "jdk.http.auth.tunneling.disabledSchemes";

    /**
     * Message to show when the HTTP request is blocked by an auth scheme being disabled.
     */
    public static final String FAQ_MESSAGE = "An authentication scheme for proxies is currently disabled."
        + "\nTo enable it, see our FAQ: https://www.knime.com/faq#q42";

    private static final Pattern INDICATIVE_JAVA_ERROR = Pattern.compile("(IOException: )?"
        + "Unable to tunnel through proxy. Proxy returns \"HTTP/\\d\\.\\d 407 Proxy Authentication Required\"");

    private static final Pattern INDICATIVE_CXF_ERROR = Pattern
        .compile("(NullPointerException: )?" + "Cannot invoke \".+Stream\\..+()\" because \"this\\.in\" is null");

    /**
     * All possible authentication schemes, along with the keys by which they are identified in the System properties.
     *
     * @author Leon Wenzler, KNIME GmbH, Konstanz, Germany
     */
    public enum AuthenticationScheme {
            /**
             * Basic authentication.
             */
            BASIC("Basic"),
            /**
             * Digest authentication.
             */
            DIGEST("Digest"),
            /**
             * NTLM authentication.
             */
            NTLM("NTLM"),
            /**
             * Kerberos authentication.
             */
            KERBEROS("Kerberos"),
            /**
             * Negotiating authentication.
             */
            NEGOTIATE("Negotiate");

        /**
         * Basic authentication is disabled per default as of Java 8u111.
         */
        private static final String DEFAULT_DISABLED = BASIC.getKey();

        private final String m_key;

        AuthenticationScheme(final String key) {
            m_key = key;
        }

        private String getKey() {
            return m_key;
        }

        /**
         * Checks whether this authentication scheme was disabled for tunneling via HTTPS.
         *
         * @return was disabled per System property?
         */
        public boolean isDisabled() {
            return System.getProperty(DISABLED_SCHEMES, DEFAULT_DISABLED).contains(m_key);
        }
    }

    /**
     * Convenience method for checking if the given exception may be caused by a disabled authentication scheme.
     *
     * @param t exception to be checked
     * @return whether the exception might be caused by a disabled authentication scheme
     */
    public static boolean isCausedByDisabledSchemes(final Throwable t) {
        final var causeMessage = ExceptionUtils.getRootCauseMessage(t);
        if (causeMessage == null) {
            return false;
        }
        final var messageMatches = INDICATIVE_CXF_ERROR.matcher(causeMessage).matches()
            || INDICATIVE_JAVA_ERROR.matcher(causeMessage).matches();
        final var atLeastOneSchemeDisabled =
            Stream.of(AuthenticationScheme.values()).anyMatch(AuthenticationScheme::isDisabled);
        return messageMatches && atLeastOneSchemeDisabled;
    }

    /**
     * Only a utility class.
     */
    private DisabledSchemesChecker() {
    }
}
