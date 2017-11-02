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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
 * ------------------------------------------------------------------------
 *
 * History
 *   Oct 13, 2014 (meinl): created
 */
package org.knime.core.util.crypto;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Interface for encrypters.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 5.0
 */
public interface IEncrypter {

    /**
     * Encrypts strings and returns a Base64 string. If the input string is <code>null</code>, the output will be
     * <code>null</code>, too. The implementation will add a random salt so that different encryptions of the same
     * data result in different result.
     *
     * @param data as string
     * @return the encrypted data or <code>null</code>
     * @throws IllegalBlockSizeException {@link IllegalBlockSizeException}
     * @throws BadPaddingException {@link BadPaddingException}
     * @throws InvalidKeyException {@link InvalidKeyException}
     * @throws InvalidAlgorithmParameterException {@link InvalidAlgorithmParameterException}
     */
    String encrypt(final String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException,
        InvalidAlgorithmParameterException;

    /**
     * Encrypts strings and returns a Base64 string. If the input string is <code>null</code>, the output will be
     * <code>null</code>, too. This implementation uses the argument salt (some random number) - the same
     * method arguments will return the same encrypted data.
     *
     * @param data as string
     * @param salt The (random or fixed) salt.
     * @return the encrypted data or <code>null</code>
     * @throws IllegalBlockSizeException {@link IllegalBlockSizeException}
     * @throws BadPaddingException {@link BadPaddingException}
     * @throws InvalidKeyException {@link InvalidKeyException}
     * @throws InvalidAlgorithmParameterException {@link InvalidAlgorithmParameterException}
     * @since 2.12
     */
    String encrypt(final String data, final int salt) throws BadPaddingException, IllegalBlockSizeException,
        InvalidKeyException, InvalidAlgorithmParameterException;

    /**
     * Decrypts strings. If the input string is <code>null</code>, the output will be <code>null</code>, too.
     *
     * @param data the data to decrypt, encoded with Base64
     * @return the decrypted data or <code>null</code>
     * @throws IllegalBlockSizeException {@link IllegalBlockSizeException}
     * @throws BadPaddingException {@link BadPaddingException}
     * @throws InvalidKeyException {@link InvalidKeyException}
     * @throws IOException {@link IOException}
     * @throws InvalidAlgorithmParameterException {@link InvalidAlgorithmParameterException}
     */
    String decrypt(final String data) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException,
        IOException, InvalidAlgorithmParameterException;

}
