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
 * -------------------------------------------------------------------
 *
 */
package org.knime.core.node.config.base;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import org.knime.core.node.InvalidSettingsException;

/**
 * Interface implements only read access functions for <code>ConfigBase</code>
 * objects providing methods for base data types only..
 *
 * @author Thomas Gabriel, University of Konstanz
 */
public interface ConfigBaseRO extends Iterable<String> {

    /**
     * Returns a <code>ConfigBase</code> for the given key.
     * @param key The identifier for the <code>ConfigBase</code>.
     * @return A new <code>ConfigBase</code> object.
     * @throws InvalidSettingsException If the ConfigBase could not be accessed.
     */
    public ConfigBase getConfigBase(String key) throws InvalidSettingsException;

    /**
     * Saves this <code>Config</code> into the given stream in XML format.
     * <em>Note that the stream will be closed when finished.</em>
     *
     * @param os The stream to write into.
     * @throws IOException If an io exception occurs during writing.
     */
    public void saveToXML(final OutputStream os) throws IOException;

    /**
     * Copies this <code>Config</code> into a write-only one.
     * @param config The <code>Config</code> to write this to.
     */
    public void copyTo(ConfigBaseWO config);

    /**
     * @return The identifier for this <code>Config</code>.
     */
    public String getKey();

    /**
     * Return int for key.
     *
     * @param key The key.
     * @return A generic int.
     * @throws InvalidSettingsException If the key is not available.
     */
    public int getInt(final String key) throws InvalidSettingsException;

    /**
     * Return double for key.
     *
     * @param key The key.
     * @return A generic double.
     * @throws InvalidSettingsException If the key is not available.
     */
    public double getDouble(final String key) throws InvalidSettingsException;

    /**
     * Return char for key.
     *
     * @param key The key.
     * @return A generic char.
     * @throws InvalidSettingsException If the key is not available.
     */
    public char getChar(final String key) throws InvalidSettingsException;

    /**
     * Return short for key.
     *
     * @param key The key.
     * @return A generic short.
     * @throws InvalidSettingsException If the key is not available.
     */
    public short getShort(final String key) throws InvalidSettingsException;

    /**
     * Return long for key.
     *
     * @param key The key.
     * @return A generic long.
     * @throws InvalidSettingsException If the key is not available.
     */
    public long getLong(final String key) throws InvalidSettingsException;

    /**
     * Return byte for key.
     *
     * @param key The key.
     * @return A generic byte.
     * @throws InvalidSettingsException If the key is not available.
     */
    public byte getByte(final String key) throws InvalidSettingsException;

    /**
     * Return String for key.
     *
     * @param key The key.
     * @return A String object.
     * @throws InvalidSettingsException If the key is not available.
     */
    public String getString(final String key) throws InvalidSettingsException;

    /**
     * Returns an unmodifiable Set of keys in this Config.
     *
     * @return A Set of keys.
     */
    public Set<String> keySet();

    /**
     * Checks if this key for a particular type is in this Config.
     *
     * @param key The key.
     * @return <b>true</b> if available, <b>false</b> if key is
     *         <code>null</code> or not available.
     */
    public boolean containsKey(final String key);

    /**
     * Return boolean for key.
     *
     * @param key The key.
     * @return A generic boolean.
     * @throws InvalidSettingsException If the key is not available.
     */
    public boolean getBoolean(final String key) throws InvalidSettingsException;

    /**
     * Return int for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic int.
     */
    public int getInt(final String key, final int def);

    /**
     * Return int array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @return An int array.
     * @throws InvalidSettingsException If the key is not available.
     */
    public int[] getIntArray(final String key) throws InvalidSettingsException;

    /**
     * Return int array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return An int array.
     */
    public int[] getIntArray(final String key, final int... def);

    /**
     * Return double for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic double.
     */
    public double getDouble(final String key, final double def);

    /**
     * Return double array for key or the default value if not available.
     *
     * @param key The key.
     * @return An array of double values.
     * @throws InvalidSettingsException If the key is not available.
     */
    public double[] getDoubleArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return double array which can be null for key, or the default array if
     * the key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A double array.
     */
    public double[] getDoubleArray(final String key, final double... def);

    /**
     * Return float for key.
     *
     * @param key The key.
     * @return A generic float.
     * @throws InvalidSettingsException If the key is not available.
     */
    public float getFloat(final String key) throws InvalidSettingsException;

    /**
     * Return float for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic float.
     */
    public float getFloat(final String key, final float def);

    /**
     * Return float array for key or the default value if not available.
     *
     * @param key The key.
     * @return An array of float values.
     * @throws InvalidSettingsException If the key is not available.
     */
    public float[] getFloatArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return float array which can be null for key, or the default array if
     * the key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A float array.
     */
    public float[] getFloatArray(final String key, final float... def);

    /**
     * Return char for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic char.
     */
    public char getChar(final String key, final char def);

    /**
     * Return char array which can be null for key.
     *
     * @param key The key.
     * @return A char array.
     * @throws InvalidSettingsException If the the key is not available.
     */
    public char[] getCharArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return byte array which can be null for key, or the default value if not
     * available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A byte array.
     */
    public byte[] getByteArray(final String key, final byte... def);

    /**
     * Return byte array which can be null for key.
     *
     * @param key The key.
     * @return A byte array.
     * @throws InvalidSettingsException If the the key is not available.
     */
    public byte[] getByteArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return byte for key.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic byte.
     */
    public byte getByte(final String key, final byte def);

    /**
     * Return a short array which can be null for key, or the default value if
     * not available.
     *
     * @param key The key.
     * @return A short array.
     * @throws InvalidSettingsException If the key is not available.
     */
    public short[] getShortArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return short array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A short array.
     */
    public short[] getShortArray(final String key, final short... def);

    /**
     * Return a long array which can be null for key, or the default value if
     * not available.
     *
     * @param key The key.
     * @return A long array.
     * @throws InvalidSettingsException If the key is not available.
     */
    public long[] getLongArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return long array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A long array.
     */
    public long[] getLongArray(final String key, final long... def);

    /**
     * Return short value for key or the default if the key is not available.
     *
     * @param key The key.
     * @param def The default values returned if the key is not available.
     * @return A short value.
     */
    public short getShort(final String key, final short def);

    /**
     * Return long value for key or the default if the key is not available.
     *
     * @param key The key.
     * @param def The default values returned if the key is not available.
     * @return A long value.
     */
    public long getLong(final String key, final long def);

    /**
     * Return char array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A char array.
     */
    public char[] getCharArray(final String key, final char... def);

    /**
     * Return boolean for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic boolean.
     */
    public boolean getBoolean(final String key, final boolean def);

    /**
     * Return a boolean array for key which can be null.
     *
     * @param key The key.
     * @return A boolean or null.
     * @throws InvalidSettingsException If the key is not available.
     */
    public boolean[] getBooleanArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return a boolean array which can be null for key, or the default value if
     * not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A boolean array.
     */
    public boolean[] getBooleanArray(final String key, final boolean... def);

    /**
     * Return String object which can be null, or the default value if the key
     * is not available.
     *
     * @param key The key.
     * @param def The default String returned if the key is not available.
     * @return A String.
     */
    public String getString(final String key, final String def);

    /**
     * Return String array which can be null for key.
     *
     * @param key The key.
     * @return A String array.
     * @throws InvalidSettingsException If the key is not available.
     */
    public String[] getStringArray(final String key)
            throws InvalidSettingsException;

    /**
     * Return String array which can be null for key, or the default array if
     * the key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A String array.
     */
    public String[] getStringArray(final String key, final String... def);

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<String> iterator();

    /**
     * Returns a decrypted password. If no password is stored under the key an exception is thrown.
     *
     * @param key the key, must not be <code>null</code>
     * @param encryptionKey key used for encrypting the password
     * @return a decrypted password, may be <code>null</code>
     * @throws InvalidSettingsException if no password for the given key exists
     */
    public String getPassword(final String key, final String encryptionKey) throws InvalidSettingsException;

    /**
     * Returns a decrypted password. If no password is stored under the key the given default is returned instead. The
     * default value is not decrypted, it's returned as provided.
     *
     * @param key the key, must not be <code>null</code>
     * @param encryptionKey key used for encrypting the password
     * @param def the (decrypted) default value
     * @return a decrypted password, may be <code>null</code>
     */
    public String getPassword(final String key, final String encryptionKey, final String def);

    /**
     * Returns a transient string if present or null if no longer available (setting restored from disc).
     * @param key the key, must not be <code>null</code>
     * @return that string or null.
     * @since 5.3
     * @see ConfigBaseWO#addTransientString(String, String)
     */
    public String getTransientString(final String key);

    default Optional<ConfigBase> getOptionalConfigBase(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getConfigBase(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default OptionalInt getOptionalInt(final String key) {
        try {
            return containsKey(key) ? OptionalInt.of(getInt(key)) : OptionalInt.empty();
        } catch (InvalidSettingsException ex) {
            return OptionalInt.empty();
        }
    }

    default Optional<Double> getOptionalDouble(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getDouble(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<Character> getOptionalChar(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getChar(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<Byte> getOptionalByte(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getByte(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<String> getOptionalString(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getString(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<int[]> getOptionalIntArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getIntArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<double[]> getOptionalDoubleArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getDoubleArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<Float> getOptionalFloat(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getFloat(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<float[]> getOptionalFloatArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getFloatArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<char[]> getOptionalCharArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getCharArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<byte[]> getOptionalByteArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getByteArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<short[]> getOptionalShortArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getShortArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<long[]> getOptionalLongArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getLongArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<Short> getOptionalShort(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getShort(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<Long> getOptionalLong(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getLong(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<Boolean> getOptionalBoolean(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getBoolean(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<boolean[]> getOptionalBooleanArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getBooleanArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<String[]> getOptionalStringArray(final String key) {
        try {
            return Optional.ofNullable(containsKey(key) ? getStringArray(key) : null);
        } catch (InvalidSettingsException ex) {
            return Optional.empty();
        }
    }

    default Optional<String> getOptionalTransientString(final String key) {
        return Optional.ofNullable(containsKey(key) ? getTransientString(key) : null);
    }
}
