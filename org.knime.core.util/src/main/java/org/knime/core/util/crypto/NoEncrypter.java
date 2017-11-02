package org.knime.core.util.crypto;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Encrypter that doesn't really encrypt, it just returns the input.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 5.0
 */
public final class NoEncrypter implements IEncrypter {
    /**
     * {@inheritDoc}
     */
    @Override
    public String encrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
        InvalidKeyException, InvalidAlgorithmParameterException {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encrypt(final String data, final int salt) throws BadPaddingException, IllegalBlockSizeException,
        InvalidKeyException, InvalidAlgorithmParameterException {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decrypt(final String data) throws BadPaddingException, IllegalBlockSizeException,
        InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        return data;
    }
}