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

/**
 * Write-only interface for <code>ConfigBase</code> objects providing only
 * only write access functions for base data types..
 *
 * @author Thomas Gabriel, University of Konstanz
 */
public interface ConfigBaseWO {

    /**
     * Creates and adds a new <code>ConfigBase</code> to this
     * <code>ConfigBase</code> by the given <i>key</i>.
     * @param key The key for the config to add.
     * @return A new <code>ConfigBase</code> object.
     */
    ConfigBase addConfigBase(String key);

    /**
     * @return The identifier for this <code>Config</code>.
     */
    String getKey();

    /**
     * Adds an int.
     *
     * @param key The key.
     * @param value The int value.
     */
    public void addInt(final String key, final int value);

    /**
     * Adds a double by the given key.
     *
     * @param key The key.
     * @param value The double value to add.
     */
    public void addDouble(final String key, final double value);

    /**
     * Adds a float by the given key.
     *
     * @param key The key.
     * @param value The float value to add.
     */
    public void addFloat(final String key, final float value);

    /**
     * Adds this char value to the Config by the given key.
     *
     * @param key The key.
     * @param value The char to add.
     */
    public void addChar(final String key, final char value);

    /**
     * Adds this short value to the Config by the given key.
     *
     * @param key The key.
     * @param value The short to add.
     */
    public void addShort(final String key, final short value);

    /**
     * Adds this long value to the Config by the given key.
     *
     * @param key The key.
     * @param value The long to add.
     */
    public void addLong(final String key, final long value);

    /**
     * Adds this byte value to the Config by the given key.
     *
     * @param key The key.
     * @param value The byte to add.
     */
    public void addByte(final String key, final byte value);

    /**
     * Adds this String object to the Config by the given key. The String can be
     * null.
     *
     * @param key The key.
     * @param value The boolean to add.
     */
    public void addString(final String key, final String value);

    /**
     * Adds this boolean value to the Config by the given key.
     *
     * @param key The key.
     * @param value The boolean to add.
     */
    public void addBoolean(final String key, final boolean value);

    /**
     * Adds this int array to the Config by the given key.
     *
     * @param key The key.
     * @param values The int array to add.
     */
    public void addIntArray(final String key, final int... values);

    /**
     * Adds this double array value to the Config by the given key. The array
     * can be null
     *
     * @param key The key.
     * @param values The double array to add.
     */
    public void addDoubleArray(final String key, final double... values);

    /**
     * Adds this float array value to the Config by the given key. The array
     * can be null.
     *
     * @param key The key.
     * @param values The float array to add.
     */
    public void addFloatArray(final String key, final float... values);

    /**
     * Adds this byte array to the Config by the given key. The array can be
     * null.
     *
     * @param key The key.
     * @param values The byte array to add.
     */
    public void addByteArray(final String key, final byte... values);

    /**
     * Adds this short array to the Config by the given key.
     *
     * @param key The key.
     * @param values The short to add.
     */
    public void addShortArray(final String key, final short... values);

    /**
     * Adds this long array to the Config by the given key.
     *
     * @param key The key.
     * @param values The long array to add.
     */
    public void addLongArray(final String key, final long... values);

    /**
     * Adds this char array to the Config by the given key.
     *
     * @param key The key.
     * @param values The char array to add.
     */
    public void addCharArray(final String key, final char... values);

    /**
     * Adds this boolean values to the Config by the given key. The array can be
     * null.
     *
     * @param key The key.
     * @param values The boolean array to add.
     */
    public void addBooleanArray(final String key, final boolean... values);

    /**
     * Adds this array of String object to the Config by the given key. The
     * array and the elements can be null.
     *
     * @param key The key.
     * @param values The String array to add.
     */
    public void addStringArray(final String key, final String... values);

}
