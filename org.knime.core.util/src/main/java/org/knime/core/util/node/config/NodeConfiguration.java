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
 * ------------------------------------------------------------------------
 *
 */
package org.knime.core.util.node.config;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.node.config.base.ConfigBaseWO;

/**
 * Represents a node configuration (parameters). In the simplest case it
 * consists of a a list of simple configuration elements, such as string, int,
 * double, etc., which are all identified by a unique identifier.
 *
 * @author Bernd Wiswedel, KNIME.com, Zurich, Switzerland
 */
public class NodeConfiguration extends AbstractConfigElement {

    private ConfigRegistry m_registry;

    /**
     * Creates a new configuration object from the configuration registry. The
     * registry defines the fields that can be accessed using the various
     * getters and setters for the supported types. Attempts to read fields that
     * are undefined or have wrong types according to the registry will cause an
     * {@link InvalidSettingsException} to be thrown.
     *
     * @param registry The registry with all valid fields.
     */
    public NodeConfiguration(final ConfigRegistry registry) {
        super(registry.getName());
        m_registry = registry;
    }

    /**
     * Get value of the boolean configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public boolean getBoolean(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementBoolean.class).getValue();
    }

    /**
     * Set value of the boolean configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setBoolean(final String id, final boolean value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementBoolean.class).setValue(value);
    }

    /**
     * Get value of the boolean array configuration parameter registered under
     * the given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public boolean[] getBooleanArray(final String id)
            throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementBooleanArray.class).getValue();
    }

    /**
     * Set value of the boolean array configuration parameter registered under
     * the given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setBooleanArray(final String id, final boolean[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementBooleanArray.class).setValue(value);
    }

    /**
     * Get value of the byte configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public byte getByte(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementByte.class).getValue();
    }

    /**
     * Set value of the byte configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setByte(final String id, final byte value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementByte.class).setValue(value);
    }

    /**
     * Get value of the byte array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public byte[] getByteArray(final String id)
            throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementByteArray.class).getValue();
    }

    /**
     * Set value of the byte array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setByteArray(final String id, final byte[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementByteArray.class).setValue(value);
    }

    /**
     * Get value of the double configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public double getDouble(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementDouble.class).getValue();
    }

    /**
     * Set value of the double configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setDouble(final String id, final double value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementDouble.class).setValue(value);
    }

    /**
     * Get value of the double array configuration parameter registered under
     * the given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public double[] getDoubleArray(final String id)
            throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementDoubleArray.class).getValue();
    }

    /**
     * Set value of the double array configuration parameter registered under
     * the given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setDoubleArray(final String id, final double[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementDoubleArray.class).setValue(value);
    }

    /**
     * Get value of the float configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public float getFloat(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementFloat.class).getValue();
    }

    /**
     * Set value of the float configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setFloat(final String id, final float value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementFloat.class).setValue(value);
    }

    /**
     * Get value of the float array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public float[] getFloatArray(final String id)
            throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementFloatArray.class).getValue();
    }

    /**
     * Set value of the float array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setFloatArray(final String id, final float[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementFloatArray.class).setValue(value);
    }

    /**
     * Get value of the int configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public int getInt(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementInt.class).getValue();
    }

    /**
     * Set value of the int configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setInt(final String id, final int value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementInt.class).setValue(value);
    }

    /**
     * Get value of the int array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public int[] getIntArray(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementIntArray.class).getValue();
    }

    /**
     * Set value of the int array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setIntArray(final String id, final int[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementIntArray.class).setValue(value);
    }

    /**
     * Get value of the long configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public long getLong(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementLong.class).getValue();
    }

    /**
     * Set value of the long configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setLong(final String id, final long value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementLong.class).setValue(value);
    }

    /**
     * Get value of the long array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public long[] getLongArray(final String id)
            throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementLongArray.class).getValue();
    }

    /**
     * Set value of the long array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setLongArray(final String id, final long[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementLongArray.class).setValue(value);
    }

    /**
     * Get value of the short configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public short getShort(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementShort.class).getValue();
    }

    /**
     * Set value of the short configuration parameter registered under the given
     * ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setShort(final String id, final short value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementShort.class).setValue(value);
    }

    /**
     * Get value of the short array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public short[] getShortArray(final String id)
            throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementShortArray.class).getValue();
    }

    /**
     * Set value of the short array configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setShortArray(final String id, final short[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementShortArray.class).setValue(value);
    }

    /**
     * Get value of the String configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public String getString(final String id) throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementString.class).getValue();
    }

    /**
     * Set value of the String configuration parameter registered under the
     * given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setString(final String id, final String value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementString.class).setValue(value);
    }

    /**
     * Get value of the String array configuration parameter registered under
     * the given ID.
     *
     * @param id The ID of the parameter.
     * @return The assigned value.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public String[] getStringArray(final String id)
            throws InvalidSettingsException {
        return getConfigElement(id, ConfigElementStringArray.class).getValue();
    }

    /**
     * Set value of the String array configuration parameter registered under
     * the given ID.
     *
     * @param id The ID of the parameter.
     * @param value The new value to set.
     * @throws InvalidSettingsException If the configuration parameter has not
     *             been registered or was registered under a different type.
     * @throws NullPointerException If the id argument is null.
     */
    public void setStringArray(final String id, final String[] value)
            throws InvalidSettingsException {
        getConfigElement(id, ConfigElementStringArray.class).setValue(value);
    }

    /**
     * Queries a config parameter that was registered during construction time.
     *
     * @param <T> The type of the parameter
     * @param id The ID under which the parameter is registered
     * @param cl The class to <code>T</code>.
     * @return The config element, never <code>null</code>.
     * @throws InvalidSettingsException If the config parameter has not been
     *             registered or was registered under a different type.
     * @throws NullPointerException If either argument is <code>null</code>.
     */
    protected <T extends AbstractConfigElement> T getConfigElement(
            final String id, final Class<T> cl)
            throws InvalidSettingsException {
        return m_registry.getConfigElement(id, cl);
    }

    /** Saves the current values to the argument.
     * @param config To save to.
     */
    public void saveConfiguration(final ConfigBaseWO config) {
        for (AbstractConfigElement e : m_registry.getConfigElements()) {
            e.save(config);
        }
    }

    /** Reads the values from the argument and sets them in the internal fields.
     * @param config To read from.
     * @throws InvalidSettingsException If individual fields don't exist or
     *         cause problems during reading.
     */
    public void loadConfiguration(final ConfigBaseRO config)
            throws InvalidSettingsException {
        InvalidSettingsException firstException = null;
        for (AbstractConfigElement e : m_registry.getConfigElements()) {
            try {
                e.load(config);
            } catch (InvalidSettingsException exc) {
                if (firstException == null) {
                    firstException = exc;
                }
            }
        }
        if (firstException != null) {
            throw firstException;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void save(final ConfigBaseWO settings) {
        ConfigBaseWO subSettings = settings.addConfigBase(getID());
        saveConfiguration(subSettings);
    }

    /** {@inheritDoc} */
    @Override
    protected void load(final ConfigBaseRO settings)
    throws InvalidSettingsException {
        ConfigBaseRO subSettings = settings.getConfigBase(getID());
        loadConfiguration(subSettings);
    }

}
