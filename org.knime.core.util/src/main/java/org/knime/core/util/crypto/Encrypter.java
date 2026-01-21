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
 */
package org.knime.core.util.crypto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Simple class to en-/decrypt strings with a fixed key.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 5.0
 */
public final class Encrypter implements IEncrypter {
    // This class is used for decrypting old data only
    private static final class V1Encrypter implements IEncrypter {
        // must be 16 bytes line
        private static final IvParameterSpec IV =
            new IvParameterSpec(new byte[]{-45, 34, 88, -7, 99, 41, 78, 12, 11, 120, 67, 111, 103, 65, 1, -113});

        private final Cipher m_cipher;

        private final SecretKey m_key;

        /**
         * Creates a new encrypter using the given key for en- and decryption.
         *
         * @param key the secret key. Ideally it should have at least 128bits or 16 characters
         * @throws NoSuchAlgorithmException is for some strange reason the AES cipher implementation cannot be found
         * @throws NoSuchPaddingException if the padding algorithm is unknown
         */
        V1Encrypter(final String key) throws NoSuchAlgorithmException, NoSuchPaddingException {
            // we assume that the key has already been checked by the outer class
            // NOFLUID only used to *decrypt* legacy format
            m_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            var keyBytes = key.getBytes(StandardCharsets.UTF_8);
            // NOFLUID only used to *read* legacy format
            var sha = MessageDigest.getInstance("SHA-1");
            keyBytes = sha.digest(keyBytes);
            m_key = new SecretKeySpec(Arrays.copyOf(keyBytes, 16), "AES"); // 128bits
        }

        @Override
        public synchronized String encrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException, InvalidAlgorithmParameterException {
            throw new UnsupportedOperationException("Encrypting with version 1 is not supported any more");
        }

        @Override
        public synchronized String encrypt(final String data, final int salt) throws BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
            throw new UnsupportedOperationException("Encrypting with version 1 is not supported any more");
        }

        @Override
        public synchronized String decrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException, IOException, InvalidAlgorithmParameterException {
            var decoded = HexUtils.hexToBytes(data);
            m_cipher.init(Cipher.DECRYPT_MODE, m_key, IV);
            // first byte is the version (unencrypted), the following four bytes the salt which can be ignored
            byte[] decryptedText = m_cipher.doFinal(decoded, 1, decoded.length - 1);
            return new String(decryptedText, 4, decryptedText.length - 4, StandardCharsets.UTF_8);
        }
    }

    private static final class V2Encrypter implements IEncrypter {
        private final Cipher m_cipher;

        private final SecretKey m_key;

        private final Random m_random = new SecureRandom();

        /**
         * Creates a new encrypter using the given key for en- and decryption.
         *
         * @param key the secret key. Ideally it should have at least 128bits or 16 characters
         * @param keyInitIterCount Number of iterations for {@link PBEKeySpec} initialization
         * @throws NoSuchAlgorithmException is for some strange reason the AES cipher implementation cannot be found
         * @throws NoSuchPaddingException if the padding algorithm is unknown
         * @throws InvalidKeySpecException if they key specification is invalid
         */
        V2Encrypter(final String key, final int keyInitIterCount)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
            // we assume that the key has already been checked by the outer class
            m_cipher = Cipher.getInstance("AES/CFB/NoPadding");

            // we can not use a random salt here otherwise we would not be able to decrypt other data
            var spec = new PBEKeySpec(key.toCharArray(), new byte[] {1, -6, 127, 98}, keyInitIterCount, 256); // AES-256
            var keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            m_key = new SecretKeySpec(keyFactory.generateSecret(spec).getEncoded(), "AES");
        }

        @Override
        public synchronized String encrypt(final String data, final byte[] salt) throws InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
            if (data == null) {
                return null;
            }

            var iv = new byte[]{-45, 34, 28, -7, 99, 42, -3, 12, 111, 120, -67, 111, 103, 65, 1, -113};
            System.arraycopy(salt, 0, iv, 0, Math.min(iv.length, salt.length));
            var ivSpec = new IvParameterSpec(iv);
            m_cipher.init(Cipher.ENCRYPT_MODE, m_key, ivSpec);

            var input = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
            var output = ByteBuffer.allocate(iv.length + input.capacity() + 4); // IV + data + auth tag
            output.put(iv);

            try {
                m_cipher.update(input, output);
                m_cipher.doFinal(ByteBuffer.wrap(new byte[] {0, 0, 0, 0}), output);
            } catch (ShortBufferException ex) {
                throw new IllegalStateException(ex); // this should never happen because the buffer has the right size
            }
            return "02" + Base64.getUrlEncoder().encodeToString(output.array());
        }

        @Override
        public synchronized String encrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException, InvalidAlgorithmParameterException {
            var salt = new byte[16];
            m_random.nextBytes(salt);
            return encrypt(data, salt);
        }

        @SuppressWarnings("deprecation")
        @Override
        public synchronized String encrypt(final String data, final int salt) throws BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
            return encrypt(data, new byte[]{(byte)(salt & 255), (byte)((salt >> 8) & 255), (byte)((salt >> 16) & 255),
                (byte)((salt >> 24) & 255)});
        }

        @Override
        public synchronized String decrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException, IOException, InvalidAlgorithmParameterException {
            // we assume the correct version has already been checked by the outer class
            var decoded = Base64.getUrlDecoder().decode(data.substring(2));

            // first 16 bytes are the IV
            var iv = new IvParameterSpec(decoded, 0, 16);

            try {
                var cipher = Cipher.getInstance("AES/CFB/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, m_key, iv);
                // first 16 bytes are the IV, then the actual data, then four bytes of auth tag
                var decryptedData = cipher.doFinal(decoded, 16, decoded.length - 16);
                var decryptedText = new String(decryptedData, 0, decryptedData.length - 4, StandardCharsets.UTF_8);

                if ((decryptedData[decryptedData.length - 4] | decryptedData[decryptedData.length - 3]
                    | decryptedData[decryptedData.length - 2] | decryptedData[decryptedData.length - 1]) != 0) {
                    throw new InvalidKeyException("Could not decrypt data. Maybe it's not a valid encrypted string"
                        + " or the decryption key is wrong.");
                }
                return decryptedText;
            } catch (NoSuchPaddingException | NoSuchAlgorithmException ex) {
                // does not happen in reality
                throw new IllegalArgumentException(ex);
            }
        }
    }

    private final IEncrypter[] m_encrypters = new IEncrypter[2];

    /**
     * Creates a new encrypter using the given key for en- and decryption.
     *
     * @param key the secret key. Ideally it should have at least 128bits or 16 characters
     * @throws NoSuchAlgorithmException is for some strange reason the AES cipher implementation cannot be found
     * @throws NoSuchPaddingException if the padding algorithm is unknown
     * @throws InvalidKeySpecException if they key specification is invalid
     */
    public Encrypter(final String key)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        this(key, 100000);
    }

    /**
     * Creates a new encrypter using the given key for en- and decryption. The number of iterations for secret key
     * initialization can be specified.
     *
     * @param key the secret key. Ideally it should have at least 128bits or 16 characters
     * @param keyInitIterationCount The number of iterations for secret key initialization (as per
     *            {@link PBEKeySpec#PBEKeySpec(char[], byte[], int, int)}). Some client will use a low number (100) here
     *            as they only use weak password encryption
     * @throws NoSuchAlgorithmException is for some strange reason the AES cipher implementation cannot be found
     * @throws NoSuchPaddingException if the padding algorithm is unknown
     * @throws InvalidKeySpecException if they key specification is invalid
     * @since 5.19
     */
    public Encrypter(final String key, final int keyInitIterationCount)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        if ((key == null) || key.isEmpty()) {
            throw new IllegalArgumentException("The encryption key must not be null or empty");
        }

        m_encrypters[0] = new V1Encrypter(key);
        m_encrypters[1] = new V2Encrypter(key, keyInitIterationCount);
    }

    @Override
    public synchronized String encrypt(final String data)
        throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return m_encrypters[m_encrypters.length - 1].encrypt(data);
    }

    @Deprecated
    @Override
    public synchronized String encrypt(final String data, final int salt)
        throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return m_encrypters[m_encrypters.length - 1].encrypt(data, salt);
    }

    @Override
    public synchronized String encrypt(final String data, final byte[] salt)
        throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        return m_encrypters[m_encrypters.length - 1].encrypt(data, salt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String decrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
        InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        if (data == null) {
            return null;
        } else if (data.isEmpty()) {
            // This is for backwards compatibility with V1. V2 does not create empty strings even for empty input.
            return "";
        }

        // one-based encryption scheme version number
        int version = data.length() >= 2 ? HexUtils.hexToBytes(data.substring(0, 2))[0] : -1;
        if (version < 1 || version > m_encrypters.length) {
            throw new IllegalArgumentException("Could not decrypt data. It is not a valid encrypted string.");
        }
        return m_encrypters[version - 1].decrypt(data);
    }

}
