/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
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
 * -------------------------------------------------------------------
 *
 */
package org.knime.core.node.config.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.ParserConfigurationException;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.util.crypto.Encrypter;
import org.knime.core.util.crypto.IEncrypter;
import org.xml.sax.SAXException;

/**
 * Supports a mechanism to save settings by their type and a key. Furthermore,
 * it provides a method to recursively add new sub <code>ConfigBase</code>
 * objects to this ConfigBase object, which then results in a tree-like
 * structure.
 * <p>
 * This class provides several types of settings which are int, double, char,
 * short, byte, boolean, java.lang.String, and ConfigBase. For these supported
 * elements, methods to add either a single or an array or retrieve them back
 * by throwing an <code>InvalidSettingsException</code> or passing a default
 * valid in advance have been implemented.
 *
 * @author Thomas Gabriel, University of Konstanz
 */
public abstract class ConfigBase extends AbstractConfigEntry
        implements ConfigBaseRO, ConfigBaseWO {
    private static IEncrypter createEncrypter(final String key ) {
        try {
            return new Encrypter(key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException ex) {
            throw new RuntimeException("Could not create encrypter: " + ex.getMessage(), ex);
        }
    }


    private static final String CFG_ARRAY_SIZE = "array-size";

    private final LinkedHashMap<String, AbstractConfigEntry> m_map;

    protected void put(final AbstractConfigEntry e) {
        m_map.put(e.getKey(), e);
        e.setParent(this); // (tg)
    }

    protected AbstractConfigEntry get(final String key) {
        return m_map.get(key);
    }

    /**
     * Creates a new, empty config object with the given key.
     *
     * @param key The key for this ConfigBase.
     */
    public ConfigBase(final String key) {
        super(ConfigEntries.config, key);
        m_map = new LinkedHashMap<String, AbstractConfigEntry>(1, 0.8f);
    }

    /**
     * Creates a new ConfigBase of this type.
     *
     * @param key The new ConfigBase's key.
     * @return A new instance of this ConfigBase.
     */
    public abstract ConfigBase getInstance(final String key);

    /**
     * Creates a new ConfigBase with the given key and returns it.
     *
     * @param key An identifier.
     * @return A new ConfigBase object.
     */
    @Override
    public final ConfigBase addConfigBase(final String key) {
        final ConfigBase config = getInstance(key);
        put(config);
        return config;
    }

    /**
     * Appends the given ConfigBase to this ConfigBase which has to directly derived
     * from this class.
     *
     * @param config The ConfigBase to append.
     * @throws NullPointerException If <code>config</code> is null.
     * @throws IllegalArgumentException If <code>config</code> is not instance
     *         of this class.
     */
    protected final void addConfigBase(final ConfigBase config) {
        if (getClass() != config.getClass()) {
            throw new IllegalArgumentException("This " + getClass()
                    + " is not equal to " + config.getClass());
        }
        put(config);
    }

    /**
     * Retrieves ConfigBase by key.
     *
     * @param key The key.
     * @return A ConfigBase object.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public final ConfigBase getConfigBase(final String key)
        throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigBase)) {
            throw new InvalidSettingsException(
                    "ConfigBase for key \"" + key + "\" not found.");
        }
        return (ConfigBase) o;
    }

    /**
     * Adds an int.
     *
     * @param key The key.
     * @param value The int value.
     */
    @Override
    public void addInt(final String key, final int value) {
        put(new ConfigIntEntry(key, value));
    }

    /**
     * Return int for key.
     *
     * @param key The key.
     * @return A generic int.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public int getInt(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigIntEntry)) {
            throw new InvalidSettingsException(
                    "Int for key \"" + key + "\" not found.");
        }
        return ((ConfigIntEntry)o).getInt();
    }

    /**
     * Adds a double by the given key.
     *
     * @param key The key.
     * @param value The double value to add.
     */
    @Override
    public void addDouble(final String key, final double value) {
        put(new ConfigDoubleEntry(key, value));
    }

    /**
     * Return double for key.
     *
     * @param key The key.
     * @return A generic double.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public double getDouble(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigDoubleEntry)) {
            throw new InvalidSettingsException(
                    "Double for key \"" + key + "\" not found.");
        }
        return ((ConfigDoubleEntry)o).getDouble();
    }

    /**
     * Adds a float by the given key.
     *
     * @param key The key.
     * @param value The float value to add.
     */
    @Override
    public void addFloat(final String key, final float value) {
        put(new ConfigFloatEntry(key, value));
    }

    /**
     * Return float for key.
     *
     * @param key The key.
     * @return A generic float.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public float getFloat(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigFloatEntry)) {
            throw new InvalidSettingsException(
                    "Float for key \"" + key + "\" not found.");
        }
        return ((ConfigFloatEntry)o).getFloat();
    }

    /**
     * Adds this char value to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param value The char to add.
     */
    @Override
    public void addChar(final String key, final char value) {
        put(new ConfigCharEntry(key, value));
    }

    /**
     * Return char for key.
     *
     * @param key The key.
     * @return A generic char.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public char getChar(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigCharEntry)) {
            throw new InvalidSettingsException(
                    "Char for key \"" + key + "\" not found.");
        }
        return ((ConfigCharEntry)o).getChar();
    }

    /**
     * Adds this short value to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param value The short to add.
     */
    @Override
    public void addShort(final String key, final short value) {
        put(new ConfigShortEntry(key, value));
    }

    /**
     * Return short for key.
     *
     * @param key The key.
     * @return A generic short.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public short getShort(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigShortEntry)) {
            throw new InvalidSettingsException(
                    "Short for key \"" + key + "\" not found.");
        }
        return ((ConfigShortEntry)o).getShort();
    }

    /**
     * Adds this long value to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param value The long to add.
     */
    @Override
    public void addLong(final String key, final long value) {
        put(new ConfigLongEntry(key, value));
    }

    /**
     * Return long for key.
     *
     * @param key The key.
     * @return A generic long.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public long getLong(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigLongEntry)) {
            throw new InvalidSettingsException(
                    "Long for key \"" + key + "\" not found.");
        }
        return ((ConfigLongEntry)o).getLong();
    }


    /**
     * Adds this byte value to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param value The byte to add.
     */
    @Override
    public void addByte(final String key, final byte value) {
        put(new ConfigByteEntry(key, value));
    }

    /**
     * Return byte for key.
     *
     * @param key The key.
     * @return A generic byte.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public byte getByte(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigByteEntry)) {
            throw new InvalidSettingsException(
                    "Byte for key \"" + key + "\" not found.");
        }
        return ((ConfigByteEntry)o).getByte();
    }

    /**
     * Adds this String object to the ConfigBase by the given key. The String can be
     * null.
     *
     * @param key The key.
     * @param value The boolean to add.
     */
    @Override
    public void addString(final String key, final String value) {
        put(new ConfigStringEntry(key, value));
    }

    /**
     * Return String for key.
     *
     * @param key The key.
     * @return A String object.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public String getString(final String key) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigStringEntry)) {
            throw new InvalidSettingsException(
                    "String for key \"" + key + "\" not found.");
        }
        return ((ConfigStringEntry)o).getString();
    }

    /**
     * Returns an unmodifiable Set of keys in this Config.
     *
     * @return A Set of keys.
     */
    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(m_map.keySet());
    }

    /**
     * @param otherConfig The other ConfigBase to check.
     * @return true if both ConfigBase objects store identical entries.
     */
    @Override
    public boolean hasIdenticalValue(final AbstractConfigEntry otherConfig) {

        // this should be save as the super ensures identical classes
        ConfigBase otherCfg = (ConfigBase)otherConfig;

        if (this.m_map.size() != otherCfg.m_map.size()) {
           return false;
        }

        for (String myKey : this.m_map.keySet()) {
            // The other config must contain all keys we've stored.
            if (!otherCfg.m_map.containsKey(myKey)) {
                return false;
            }
            AbstractConfigEntry ce = this.m_map.get(myKey);
            AbstractConfigEntry otherCe = otherCfg.m_map.get(myKey);
            if (ce == null) {
                if (otherCe != null) {
                    return false;
                }
            } else {
                // and must map an identical value with it.
                if (!ce.isIdentical(otherCe)) {
                    return false;
                }
            }
        }

        return true;

    }

    /**
     * Checks if this key for a particular type is in this Config.
     *
     * @param key The key.
     * @return <b>true</b> if available, <b>false</b> if key is
     *         <code>null</code> or not available.
     */
    @Override
    public boolean containsKey(final String key) {
        return m_map.containsKey(key);
    }

    /**
     * Return boolean for key.
     *
     * @param key The key.
     * @return A generic boolean.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public boolean getBoolean(final String key)
            throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigBooleanEntry)) {
            throw new InvalidSettingsException(
                    "Boolean for key \"" + key + "\" not found.");
        }
        return ((ConfigBooleanEntry)o).getBoolean();
    }

    /**
     * Adds this boolean value to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param value The boolean to add.
     */
    @Override
    public void addBoolean(final String key, final boolean value) {
        put(new ConfigBooleanEntry(key, value));
    }

    /**
     * Return int for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic int.
     */
    @Override
    public int getInt(final String key, final int def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigIntEntry)) {
            return def;
        }
        return ((ConfigIntEntry)o).getInt();
    }

    /**
     * Return int array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @return An int array.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public int[] getIntArray(final String key) throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        int[] ret = new int[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getInt(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return int array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return An int array.
     */
    @Override
    public int[] getIntArray(final String key, final int... def) {
        try {
            return getIntArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Adds this int array to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param values The int array to add.
     */
    @Override
    public void addIntArray(final String key, final int... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addInt(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Return double for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic double.
     */
    @Override
    public double getDouble(final String key, final double def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigDoubleEntry)) {
            return def;
        }
        return ((ConfigDoubleEntry)o).getDouble();
    }

    /**
     * Return double array for key or the default value if not available.
     *
     * @param key The key.
     * @return An array of double values.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public double[] getDoubleArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        double[] ret = new double[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getDouble(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return double array which can be null for key, or the default array if
     * the key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A double array.
     */
    @Override
    public double[] getDoubleArray(final String key, final double... def) {
        try {
            return getDoubleArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Return float for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic float.
     */
    @Override
    public float getFloat(final String key, final float def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigFloatEntry)) {
            return def;
        }
        return ((ConfigFloatEntry)o).getFloat();
    }

    /**
     * Return float array for key or the default value if not available.
     *
     * @param key The key.
     * @return An array of float values.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public float[] getFloatArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        float[] ret = new float[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getFloat(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return float array which can be null for key, or the default array if
     * the key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A float array.
     */
    @Override
    public float[] getFloatArray(final String key, final float... def) {
        try {
            return getFloatArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Adds this double array value to the ConfigBase by the given key. The array
     * can be null.
     *
     * @param key The key.
     * @param values The double array to add.
     */
    @Override
    public void addDoubleArray(final String key, final double... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addDouble(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Adds this float array value to the ConfigBase by the given key. The array
     * can be null.
     *
     * @param key The key.
     * @param values The float array to add.
     */
    @Override
    public void addFloatArray(final String key, final float... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addFloat(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Return char for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic char.
     */
    @Override
    public char getChar(final String key, final char def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigCharEntry)) {
            return def;
        }
        return ((ConfigCharEntry)o).getChar();
    }

    /**
     * Return char array which can be null for key.
     *
     * @param key The key.
     * @return A char array.
     * @throws InvalidSettingsException If the the key is not available.
     */
    @Override
    public char[] getCharArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        char[] ret = new char[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getChar(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return byte array which can be null for key, or the default value if not
     * available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A byte array.
     */
    @Override
    public byte[] getByteArray(final String key, final byte... def) {
        try {
            return getByteArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Return byte array which can be null for key.
     *
     * @param key The key.
     * @return A byte array.
     * @throws InvalidSettingsException If the the key is not available.
     */
    @Override
    public byte[] getByteArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        byte[] ret = new byte[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getByte(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Adds this byte array to the ConfigBase by the given key. The array can be
     * null.
     *
     * @param key The key.
     * @param values The byte array to add.
     */
    @Override
    public void addByteArray(final String key, final byte... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addByte(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Return byte for key.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic byte.
     */
    @Override
    public byte getByte(final String key, final byte def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigByteEntry)) {
            return def;
        }
        return ((ConfigByteEntry)o).getByte();
    }

    /**
     * Return a short array which can be null for key, or the default value if
     * not available.
     *
     * @param key The key.
     * @return A short array.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public short[] getShortArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        short[] ret = new short[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getShort(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return short array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A short array.
     */
    @Override
    public short[] getShortArray(final String key, final short... def) {
        try {
            return getShortArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Return a long array which can be null for key, or the default value if
     * not available.
     *
     * @param key The key.
     * @return A long array.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public long[] getLongArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        long[] ret = new long[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getLong(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return long array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A long array.
     */
    @Override
    public long[] getLongArray(final String key, final long... def) {
        try {
            return getLongArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Adds this short array to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param values The short to add.
     */
    @Override
    public void addShortArray(final String key, final short... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addShort(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Return short value for key or the default if the key is not available.
     *
     * @param key The key.
     * @param def The default values returned if the key is not available.
     * @return A short value.
     */
    @Override
    public short getShort(final String key, final short def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigShortEntry)) {
            return def;
        }
        return ((ConfigShortEntry)o).getShort();
    }

    /**
     * Adds this long array to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param values The long arry to add.
     */
    @Override
    public void addLongArray(final String key, final long... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addLong(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Return long value for key or the default if the key is not available.
     *
     * @param key The key.
     * @param def The default values returned if the key is not available.
     * @return A long value.
     */
    @Override
    public long getLong(final String key, final long def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigLongEntry)) {
            return def;
        }
        return ((ConfigLongEntry)o).getLong();
    }

    /**
     * Return char array which can be null for key, or the default array if the
     * key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A char array.
     */
    @Override
    public char[] getCharArray(final String key, final char... def) {
        try {
            return getCharArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Adds this char array to the ConfigBase by the given key.
     *
     * @param key The key.
     * @param values The char array to add.
     */
    @Override
    public void addCharArray(final String key, final char... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addChar(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Return boolean for key or the default value if not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A generic boolean.
     */
    @Override
    public boolean getBoolean(final String key, final boolean def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigBooleanEntry)) {
            return def;
        }
        return ((ConfigBooleanEntry)o).getBoolean();
    }

    /**
     * Return a boolean array for key which can be null.
     *
     * @param key The key.
     * @return A boolean or null.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public boolean[] getBooleanArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        boolean[] ret = new boolean[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getBoolean(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return a boolean array which can be null for key, or the default value if
     * not available.
     *
     * @param key The key.
     * @param def Returned if no value available for the given key.
     * @return A boolean array.
     */
    @Override
    public boolean[] getBooleanArray(final String key, final boolean... def) {
        try {
            return getBooleanArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Adds this boolean values to the ConfigBase by the given key. The array can be
     * null.
     *
     * @param key The key.
     * @param values The boolean array to add.
     */
    @Override
    public void addBooleanArray(final String key, final boolean... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addBoolean(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Return String object which can be null, or the default array if the key
     * is not available.
     *
     * @param key The key.
     * @param def The default String returned if the key is not available.
     * @return A String.
     */
    @Override
    public String getString(final String key, final String def) {
        Object o = m_map.get(key);
        if (o == null || !(o instanceof ConfigStringEntry)) {
            return def;
        }
        return ((ConfigStringEntry)o).getString();
    }

    /**
     * Return String array which can be null for key.
     *
     * @param key The key.
     * @return A String array.
     * @throws InvalidSettingsException If the key is not available.
     */
    @Override
    public String[] getStringArray(final String key)
            throws InvalidSettingsException {
        ConfigBase config = this.getConfigBase(key);
        int size = config.getInt(CFG_ARRAY_SIZE, -1);
        if (size == -1) {
            return null;
        }
        String[] ret = new String[size];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = config.getString(Integer.toString(i));
        }
        return ret;
    }

    /**
     * Return String array which can be null for key, or the default array if
     * the key is not available.
     *
     * @param key The key.
     * @param def The default array returned if the key is not available.
     * @return A String array.
     */
    @Override
    public String[] getStringArray(final String key, final String... def) {
        try {
            return getStringArray(key);
        } catch (InvalidSettingsException ise) {
            return def;
        }
    }

    /**
     * Adds this array of String object to the ConfigBase by the given key. The
     * array and the elements can be null.
     *
     * @param key The key.
     * @param values The String array to add.
     */
    @Override
    public void addStringArray(final String key, final String... values) {
        ConfigBaseWO config = this.addConfigBase(key);
        if (values != null) {
            config.addInt(CFG_ARRAY_SIZE, values.length);
            for (int i = 0; i < values.length; i++) {
                config.addString(Integer.toString(i), values[i]);
            }
        }
    }

    /**
     * Returns ConfigBase entry for a key.
     *
     * @param key The key.
     * @return The ConfigBase entry for the key.
     */
    public AbstractConfigEntry getEntry(final String key) {
        return m_map.get(key);
    }

    /**
     * Adds the given ConfigBase entry to this Config.
     *
     * @param entry The ConfigBase entry to add.
     */
    public void addEntry(final AbstractConfigEntry entry) {
        put(entry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Iterator<String> iterator() {
        return keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toStringValue() {
        return super.getKey();
    }

    /**
     * Adds this and all children String representations to the given buffer.
     * @param buf The string buffer to which this Config's String all all
     *        children String representation is added.
     */
    public final void toString(final StringBuffer buf) {
        toString(0, buf);
        buf.trimToSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.getKey();
    }

    private static final int TAB_SIZE = 2;

    private static final String SPACE = " ";
    private static final String KEYEQ = "key=";
    private static final String COMMA_TYPEEQ = ",type=";
    private static final String LINE_BREAK = "\n";
    private static final String DOT_LINE_BREAK = ":\n";
    private static final String ARROW_NULL = "->null";
    private static final String ARROW = "->";

    private void toString(final int indent, final StringBuffer sb) {
        assert (indent >= 0);
        sb.ensureCapacity(1000);
        for (String key : m_map.keySet()) {
            for (int t = 0; t < indent * TAB_SIZE; t++) {
                sb.append(SPACE);
            }
            AbstractConfigEntry e = getEntry(key);
            sb.append(KEYEQ);
            sb.append(key);
            sb.append(COMMA_TYPEEQ);
            sb.append(e.getType());
            if (e instanceof ConfigBase) {
                int myindent = indent;
                sb.append(DOT_LINE_BREAK);
                ConfigBase ms = (ConfigBase)e;
                ms.toString(++myindent, sb);
            } else {
                String value = e.toStringValue();
                if (value == null) {
                    sb.append(ARROW_NULL);
                } else {
                    sb.append(ARROW);
                    sb.append(value);
                }
                sb.append(LINE_BREAK);
            }
        }
    }

    /* --- write and read from file --- */

    /**
     * Writes this ConfigBase into the given stream.
     *
     * @param oos Write ConfigBase to this stream.
     * @throws IOException If the file can not be accessed.
     */
    public final void writeToFile(final ObjectOutputStream oos)
            throws IOException {
        oos.writeObject(this);
        oos.close();
    }

    /**
     * Creates new ConfigBase from the given file using the serialized object
     * stream.
     *
     * @param ois Read ConfigBase from this stream.
     * @return The new Config.
     * @throws IOException Problem opening the file or content is not a Config.
     */
    protected static ConfigBase readFromFile(
            final ObjectInputStream ois) throws IOException {
        try {
            ConfigBase config = (ConfigBase)ois.readObject();
            ois.close();
            return config;
        } catch (ClassNotFoundException cnfe) {
            IOException e = new IOException(cnfe.getMessage());
            e.initCause(cnfe);
            throw e;
        }
    }

    /**
     * Writes this ConfigBase to the given stream as XML. <em>Note that the stream will be closed when finished.</em>
     *
     * @param os The stream to write into.
     * @throws IOException If this ConfigBase could be stored to the stream.
     */
    @Override
    public final void saveToXML(final OutputStream os)
            throws IOException {
        if (os == null) {
            throw new NullPointerException();
        }
        XMLConfig.save(this, os);
    }

    /**
     * Reads ConfigBase from XML into a new ConfigBase object.
     *
     * @param config Depending on the readRoot, we write into this ConfigBase and
     *            return it.
     * @param in The stream to read XML ConfigBase from.
     * @return A new ConfigBase filled with the content read from XML.
     * @throws IOException If the ConfigBase could not be load from stream.
     */
    protected static ConfigBase loadFromXML(final ConfigBase config,
            final InputStream in) throws IOException {
        if (in == null) {
            throw new NullPointerException();
        }
        config.load(in);
        return config;
    }

    /**
     * Read config entries from an XML file into this object.
     * @param is The XML inputstream storing the configuration to read
     * @throws IOException If the stream could not be read.
     */
    public void load(final InputStream is) throws IOException {
        try {
            XMLConfig.load(this, is);
        } catch (SAXException se) {
            IOException ioe = new IOException(se.getMessage());
            ioe.initCause(se);
            throw ioe;
        } catch (ParserConfigurationException pce) {
            IOException ioe = new IOException(pce.getMessage());
            ioe.initCause(pce);
            throw ioe;
        } finally {
            is.close();
        }
    }

    /**
     * Makes a deep copy of this ConfigBase and all sub ConfigBase objects.
     * @param dest the destination this ConfigBase object is copied to.
     */
    @Override
    public void copyTo(final ConfigBaseWO dest) {
        for (Map.Entry<String, AbstractConfigEntry> e : m_map.entrySet()) {
            AbstractConfigEntry ace = e.getValue();
            if (ace instanceof ConfigBase) {
                ConfigBase config = dest.addConfigBase(ace.getKey());
                ((ConfigBase) ace).copyTo(config);
            } else {
                ((ConfigBase) dest).addEntry(ace);
            }
        }
    }

    // tree node methods

    /**
     * The TreeNode for the given index.
     * @param childIndex The index to retrieve the TreeNode for.
     * @return The associated TreeNode.
     */
    @Override
    public TreeNode getChildAt(final int childIndex) {
        Iterator<String> it = m_map.keySet().iterator();
        for (int i = 0; i < childIndex; i++) {
            it.next();
        }
        TreeNode node = m_map.get(it.next());
        return node;
    }

    /**
     * @return The number of entries in this Config.
     * @see javax.swing.tree.TreeNode#getChildCount()
     */
    @Override
    public int getChildCount() {
        return m_map.size();
    }

    /**
     * Returns the index for a given TreeNode.
     * @param node The TreeNode to get the index for.
     * @return The index of the given node.
     * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    @Override
    public int getIndex(final TreeNode node) {
        int i = 0;
        for (Map.Entry<String, AbstractConfigEntry> e : m_map.entrySet()) {
            if (e.getValue().equals(node)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * @return true, only if the map is empty.
     * @see javax.swing.tree.TreeNode#isLeaf()
     */
    @Override
    public final boolean isLeaf() {
        return m_map.isEmpty();
    }

    /**
     * An enumeration of a values.
     * @return All elements of this Config.
     * @see javax.swing.tree.TreeNode#children()
     */
    @Override
    public final Enumeration<TreeNode> children() {
        return new Vector<TreeNode>(m_map.values()).elements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPassword(final String key, final String encryptionKey, final String value) {
        try {
            put(new ConfigPasswordEntry(key,
                createEncrypter(encryptionKey).encrypt(value, value == null ? 0 : value.length())));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException ex) {
            throw new RuntimeException("Error while encrypting password: " + ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword(final String key, final String encryptionKey) throws InvalidSettingsException {
        Object o = m_map.get(key);
        if (!(o instanceof ConfigPasswordEntry)) {
            throw new InvalidSettingsException("Password for key \"" + key + "\" not found.");
        }
        try {
            return createEncrypter(encryptionKey).decrypt(((ConfigPasswordEntry)o).getPassword());
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException | IOException ex) {
            throw new InvalidSettingsException("Error while decrypting password: " + ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword(final String key, final String encryptionKey, final String def) {
        Object o = m_map.get(key);
        if (!(o instanceof ConfigPasswordEntry)) {
            return def;
        }
        try {
            return createEncrypter(encryptionKey).decrypt(((ConfigPasswordEntry)o).getPassword());
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException | IOException ex) {
            throw new RuntimeException("Error while decrypting password: " + ex.getMessage(), ex);
        }
    }
}
