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
 * Created on 22.11.2013 by thor
 */
package org.knime.core.util.crypto;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Simple class to en-/decrypt strings with a fixed key.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @since 5.0
 */
public final class Encrypter implements IEncrypter {
    // must be 16 bytes line
    private static final IvParameterSpec IV = new IvParameterSpec(new byte[]{-45, 34, 88, -7, 99, 41, 78, 12, 11, 120,
        67, 111, 103, 65, 1, -113});

    private static final String DEFAULT_ENCRYPTION_METHOD = "AES/CBC/PKCS5Padding";

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final Cipher m_cipher;

    private final SecretKey m_key;

    private final Random m_random = new Random();

    /**
     * Creates a new encrypter using the given key for en- and decryption.
     *
     * @param key the secret key. Ideally it should have at least 128bits or 16 characters
     * @throws NoSuchAlgorithmException is for some strange reason the AES cipher implementation cannot be found
     * @throws InvalidKeySpecException if the public key contained in the license is invalid
     * @throws NoSuchPaddingException if the padding algorithm is unknown
     */
    public Encrypter(final String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        if ((key == null) || key.isEmpty()) {
            throw new IllegalArgumentException("The encryption key must not be null or empty");
        }

        m_cipher = Cipher.getInstance(DEFAULT_ENCRYPTION_METHOD);

        byte[] keyBytes = key.getBytes(UTF8);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        m_key = new SecretKeySpec(Arrays.copyOf(keyBytes, 16), "AES"); // 128bits
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String encrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
        InvalidKeyException, InvalidAlgorithmParameterException {
        if (data == null) {
            return null;
        }
        m_cipher.init(Cipher.ENCRYPT_MODE, m_key, IV);
        byte[] dataBytes = data.getBytes(UTF8);
        byte[] unencrypted = new byte[dataBytes.length + 4]; // 4 bytes salt
        int salt = m_random.nextInt();
        unencrypted[0] = (byte)(salt & 0xff);
        unencrypted[1] = (byte)((salt >> 8) & 0xff);
        unencrypted[2] = (byte)((salt >> 16) & 0xff);
        unencrypted[3] = (byte)((salt >> 24) & 0xff);
        System.arraycopy(dataBytes, 0, unencrypted, 4, dataBytes.length);

        byte[] ciphertext = m_cipher.doFinal(unencrypted);
        return "01" + HexUtils.bytesToHex(ciphertext); // "01" is the version, also hex-encoded
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
            return "";
        }
        byte[] pw = HexUtils.hexToBytes(data);
        m_cipher.init(Cipher.DECRYPT_MODE, m_key, IV);
        // first byte is the version (unencrypted), the following four bytes the salt which can be ignored
        byte[] decryptedText = m_cipher.doFinal(pw, 1, pw.length - 1);
        return new String(decryptedText, 4, decryptedText.length - 4, UTF8);
    }
}
