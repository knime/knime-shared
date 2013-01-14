/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 * ---------------------------------------------------------------------
 *
 * History
 *   04.05.2011 (meinl): created
 */
package org.knime.core.util.crypto;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;

/**
 * This stream decrypts data that has been encrypted with the secret KNIME key.
 * Is also offers direct access to the public KNIME key.
 *
 * @since 2.4
 * @author Thorsten Meinl, University of Konstanz
 */
public class KNIMEDecryptionStream extends CipherInputStream {
    private static final BigInteger MODULUS =
            new BigInteger(
                    "***REMOVED***");

    private static final BigInteger EXPONENT = new BigInteger("65537");

    /** KNIME's public key. */
    public static final RSAPublicKey DEFAULT_PUBLIC_KEY;

    static {
        RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(MODULUS, EXPONENT);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("rsa");
            DEFAULT_PUBLIC_KEY =
                    (RSAPublicKey)keyFactory.generatePublic(rsaSpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Cipher getDefaultDecryptCipher() throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("rsa");
        cipher.init(Cipher.DECRYPT_MODE, DEFAULT_PUBLIC_KEY);
        return cipher;
    }

    /**
     * Creates a new decryption stream using KNIME's public key.
     *
     * @param is the input stream to read from
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public KNIMEDecryptionStream(final InputStream is)
            throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeySpecException {
        super(is, getDefaultDecryptCipher());
    }
}
