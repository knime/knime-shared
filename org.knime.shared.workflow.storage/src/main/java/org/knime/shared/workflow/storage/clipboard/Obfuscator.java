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
 *   8 Sep 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.clipboard;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.knime.core.util.crypto.Encrypter;
import org.knime.core.util.crypto.IEncrypter;
import org.knime.shared.workflow.storage.clipboard.SystemClipboardFormat.ObfuscatorException;

/**
 * Utility class to make string contents unreadable. Called from GUI code before inserting {@link DefClipboardContent}
 * into the system clipboard.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
final class Obfuscator {

    private Obfuscator() {
    }

    private static final String OBFUSCATE_WITH = "601817e1f7859114b3d50a7e80f90bab";

    /**
     * Should not be null, but in the unlikely event that an instance cannot be created, calls to
     * {@link #obfuscate(String)} and {@link #deobfuscate(String)} will throw an {@link ObfuscatorException}.
     */
    private static IEncrypter encrypter;

    static {
        try {
            encrypter = new Encrypter(OBFUSCATE_WITH);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException ex) {
            encrypter = null;
            Logger.getLogger(Obfuscator.class.getName()).log(Level.WARNING, "Cannot initialize Obfuscator", ex);
        }
    }

    /**
     * @param plainText some text that should not be easily readable anymore
     * @return a obscure string. Can be passed to {@link #deobfuscate(String)} in order to retrieve the original input
     * @throws ObfuscatorException if the encryption algorithm could not be initialized.
     */
    static String obfuscate(final String plainText) throws ObfuscatorException {
        try {
            if (encrypter == null) {
                throw new ObfuscatorException();
            }
            return encrypter.encrypt(plainText);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException ex) {
            throw new ObfuscatorException(ex);
        }
    }

    /**
     *
     * @param string any string, needs to be a {@link Base64} encoded string that contains the {@link SecretKey} as last
     *            characters.
     * @return the original input to {@link #obfuscate(String)}
     * @throws ObfuscatorException if the input is not in the format produced by {@link #obfuscate(String)}
     */
    static String deobfuscate(final String string) throws ObfuscatorException {
        try {
            if (encrypter == null) {
                throw new ObfuscatorException();
            }
            return encrypter.decrypt(string);
        } catch (IllegalArgumentException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException | IOException ex) {
            throw new ObfuscatorException(ex);
        }
    }

}
