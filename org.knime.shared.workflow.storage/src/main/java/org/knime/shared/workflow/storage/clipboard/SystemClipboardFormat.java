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
 *   9 Sep 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.clipboard;

import java.util.function.Supplier;

import org.knime.shared.workflow.storage.text.util.ObjectMapperUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Utility class for working with a system clipboard. This makes sure
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public final class SystemClipboardFormat {

    private SystemClipboardFormat() {
    }

    /**
     * Thrown if string content cannot be obfuscated or deobfuscated, e.g., when trying to deobfuscate something that is
     * not obfuscated or the required functionality for obfuscation is not available in this Java Virtual Machine.
     *
     * @author Carl Witt, KNIME AG, Zurich, Switzerland
     */
    public static class ObfuscatorException extends Exception {
        /**
         * @param cause of this exception
         * @see ObfuscatorException
         */
        public ObfuscatorException(final Exception cause) {
            super(cause);
        }

        /** @see ObfuscatorException */
        public ObfuscatorException() {
        }

        private static final long serialVersionUID = -5486253135992179659L;
    }

    private static final Supplier<IllegalArgumentException> cannotDeserialize =
        () -> new IllegalArgumentException("System clipboard does not contain KNIME workflow contents.");

    /**
     * The version of the system clipboard format.
     *
     * @author Carl Witt, KNIME AG, Zurich, Switzerland
     */
    private enum Version {
            /**
             * Completely obfuscated content, with a prefix that indicates the version of the system clipboard content.
             */
            SYSTEM_CLIPBOARD_CONTENT_V1("01");

        private String m_prefix;

        /**
         * @param versionPrefix
         */
        Version(final String versionPrefix) {
            m_prefix = versionPrefix;
        }

        /**
         *
         * @param systemClipboardContent any string
         * @return whether string looks like system clipboard content in the format version this enum member represents
         */
        boolean matches(final String systemClipboardContent) {
            return systemClipboardContent != null && systemClipboardContent.startsWith(m_prefix);
        }

        /** Add the version information to the given content. */
        String wrap(final String content) {
            return m_prefix + content;
        }

        /** Remove the version information from the given content. */
        String unwrap(final String systemClipboardContent) {
            return systemClipboardContent.substring(m_prefix.length());
        }

    }

    /**
     * @param defClipboardContent
     * @return something that can safely be copied to the system clipboard, i.e., will not expose the contents of locked
     *         metanodes or components
     * @throws JsonProcessingException
     * @throws ObfuscatorException
     */
    public static String serialize(final DefClipboardContent defClipboardContent)
        throws JsonProcessingException, ObfuscatorException {
        if (defClipboardContent == null) {
            throw new ObfuscatorException(new IllegalArgumentException("Cannot serialize null"));
        }
        var mapper = ObjectMapperUtil.getInstance().getObjectMapper();
        var serializedContent = mapper.writeValueAsString(defClipboardContent);
        return Version.SYSTEM_CLIPBOARD_CONTENT_V1.wrap(Obfuscator.obfuscate(serializedContent));
    }

    /**
     *
     * @param systemClipboardContent non-null string as produced by {@link #serialize(DefClipboardContent)}
     * @return the original input to {@link #serialize(DefClipboardContent)}
     * @throws IllegalArgumentException if the system clipboard content format version cannot be parsed
     * @throws ObfuscatorException if the obfuscated contents cannot be converted to plain text again
     * @throws InvalidDefClipboardContentVersionException if the plain text cannot be parsed as
     *             {@link DefClipboardContent}
     */
    public static DefClipboardContent deserialize(final String systemClipboardContent)
        throws InvalidDefClipboardContentVersionException, IllegalArgumentException, ObfuscatorException {
        // currently the only version, if it doesn't match, we cannot deserialize
        if (!Version.SYSTEM_CLIPBOARD_CONTENT_V1.matches(systemClipboardContent)) {
            throw cannotDeserialize.get();
        }

        String obfuscated = Version.SYSTEM_CLIPBOARD_CONTENT_V1.unwrap(systemClipboardContent);
        String plain = Obfuscator.deobfuscate(obfuscated);
        return DefClipboardContent.valueOf(plain).orElseThrow(cannotDeserialize);
    }

}
