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

import java.util.LinkedHashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.util.node.config.display.AbstractDisplayOption;

/** Registry for configuration parameters.
 *
 * @author Bernd Wiswedel, KNIME.com, Zurich, Switzerland
 */
public class ConfigRegistry {

    private final String m_name;
    private final Map<String, AbstractConfigElement> m_elements;

    private boolean m_disallowElementAdding;

    /**
     * @param name Name/ID of config.
     */
    ConfigRegistry(final String name) {
        if (name == null) {
            throw new NullPointerException("Argument must not be null.");
        }
        m_name = name;
        m_elements = new LinkedHashMap<String, AbstractConfigElement>(2);
        m_disallowElementAdding = false;
    }

    /**
     * Disallow further invocations of any of the add methods. It's called from
     * the factory to end construction time.
     */
    public void setDisallowElementAdding() {
        m_disallowElementAdding = true;
    }

    /** Get (read-through!) iterable on all registered config parameters.
     * @return The config elements added on this registry.
     */
    Iterable<AbstractConfigElement> getConfigElements() {
        return m_elements.values();
    }

    /** @return the name */
    String getName() {
        return m_name;
    }

    /**
     * Adds a new Boolean attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getBoolean(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementBoolean addBoolean(final String id,
            final Boolean defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementBoolean(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Boolean Array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>,
     * e.g. {@link NodeConfiguration#getBooleanArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementBooleanArray addBooleanArray(final String id,
            final boolean[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementBooleanArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Byte attribute to the registry. The attribute will be globally
     * registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g. {@link NodeConfiguration#getByte(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementByte addByte(final String id, final Byte defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementByte(
                id, defaultValue, displayOption));
    }

    /**
     * Adds a new Byte Array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getByteArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementByteArray addByteArray(final String id,
            final byte[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementByteArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Double attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>,
     * e.g. {@link NodeConfiguration#getDouble(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementDouble addDouble(final String id,
            final Double defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementDouble(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Double Array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getDoubleArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementDoubleArray addDoubleArray(final String id,
            final double[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementDoubleArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Float attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g. {@link NodeConfiguration#getFloat(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementFloat addFloat(final String id,
            final Float defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementFloat(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Float Array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getFloatArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementFloatArray addFloatArray(final String id,
            final float[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementFloatArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new int attribute to the registry. The attribute will be globally
     * registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g. {@link NodeConfiguration#getInt(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementInt addInteger(final String id,
            final Integer defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementInt(
                id, defaultValue, displayOption));
    }

    /**
     * Adds a new int array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getIntArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementIntArray addIntegerArray(final String id,
            final int[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementIntArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Long attribute to the registry. The attribute will be globally
     * registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g. {@link NodeConfiguration#getLong(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementLong addLong(final String id, final Long defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementLong(
                id, defaultValue, displayOption));
    }

    /**
     * Adds a new Long Array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getLongArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementLongArray addLongArray(final String id,
            final long[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementLongArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Short attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g. {@link NodeConfiguration#getShort(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementShort addShort(final String id,
            final Short defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementShort(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new Short Array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getShortArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementShortArray addShortArray(final String id,
            final short[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementShortArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new String attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>,
     * e.g. {@link NodeConfiguration#getString(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementString addString(final String id,
            final String defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementString(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a new String Array attribute to the registry. The attribute will be
     * globally registered and will be saved and loaded as part of the node
     * configuration. This attribute can later be accessed under the given
     * <code>id</code>, e.g.
     * {@link NodeConfiguration#getStringArray(String)}.
     *
     * @param id The unique ID in this configuration tree.
     * @param defaultValue The default value.
     * @param displayOption Optional display options (may be <code>null</code>)
     * @return An config element that represents the attribute. Most
     *         implementations will ignore this return value.
     */
    public ConfigElementStringArray addStringArray(final String id,
            final String[] defaultValue,
            final AbstractDisplayOption displayOption) {
        return addConfigElement(new ConfigElementStringArray(id, defaultValue,
                displayOption));
    }

    /**
     * Adds a configuration parameter to the registry and returns it.
     *
     * @param <T> The type of parameter
     * @param configElement The element itself.
     * @return The method argument. Allows for inline instantiation, e.g.
     *         {@link ConfigElementInt} c = registry.addConfigElement(new
     *         ConfigElementInt(...);
     * @throws IllegalStateException If called after configuration tree
     *             construction.
     * @throws IllegalArgumentException If there is a previously added config
     *             element with the same {@link AbstractConfigElement#getID()
     *             ID}.
     * @throws NullPointerException If the argument is <code>null</code>.
     */
    public <T extends AbstractConfigElement> T addConfigElement(
            final T configElement) {
        if (m_disallowElementAdding) {
            throw new IllegalStateException("Configuration parameters can "
                    + "only be added at construction time");
        }
        String id = configElement.getID();
        if (m_elements.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate configuration "
                    + "parameter ID \"" + id + "\"");
        }
        m_elements.put(id, configElement);
        return configElement;
    }

    /** Create child entry.
     * @param name Name of child.
     * @return New child.
     */
    public ConfigRegistry createChild(final String name) {
        return new ConfigRegistry(name);
    }

    /**
     * Queries a config parameter previously registered with one of the add
     * methods.
     *
     * @param <T> The type of the parameter
     * @param id The ID under which the parameter is registered
     * @param cl The class to <code>T</code>.
     * @return The config element, never <code>null</code>.
     * @throws InvalidSettingsException If the config parameter has not been
     *             registered or was registered under a different type.
     * @throws NullPointerException If either argument is <code>null</code>.
     */
    <T extends AbstractConfigElement> T getConfigElement(final String id,
            final Class<T> cl) throws InvalidSettingsException {
        if (cl == null) {
            throw new NullPointerException("Class argument must not be null.");
        }
        if (id == null) {
            throw new NullPointerException("ID argument must not be null.");
        }
        AbstractConfigElement ace = m_elements.get(id);
        if (ace == null) {
            throw new InvalidSettingsException("No node configuration "
                    + "parameter under id \"" + id + "\"; configuration "
                    + "parameters must be registered at construction time "
                    + "(coding problem)");
        }
        if (!cl.isInstance(ace)) {
            String expectedTypeName = extractTypeFromClassName(cl);
            String actualTypeName = extractTypeFromClassName(ace.getClass());
            throw new InvalidSettingsException("Configuration parameter "
                    + "with id \"" + id + "\" is not of expected type "
                    + "(expected " + expectedTypeName + "; actual: "
                    + actualTypeName + ") - this typically indicates a "
                    + "coding problem");
        }
        return cl.cast(ace);
    }

    private final String extractTypeFromClassName(
            final Class<? extends AbstractConfigElement> e) {
        if (e == null) {
            return "<null>";
        }
        final String base = "ConfigElement";
        String name = e.getSimpleName();
        if (name.startsWith(base)) {
            String subName = name.substring(base.length());
            if (subName.length() > 0) {
                return subName;
            }
        }
        return name;
    }

    /**
     * Factory method used by the framework to create a new empty config
     * registry. This method must not be used by client code and may change
     * in future versions.
     *
     * @param name The internally used ID for this config registry.
     * @return A new config registry.
     */
    public static final ConfigRegistry internalCreateNew(final String name) {
        return new ConfigRegistry(name);
    }

}
