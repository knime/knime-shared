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

import java.util.Objects;

import org.knime.core.node.config.base.json.AbstractJSONEntry;
import org.knime.core.node.config.base.json.JSONPassword;

/**
 * Config entry for password values.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 */
public final class ConfigPasswordEntry extends AbstractConfigEntry {
    private static final long serialVersionUID = -16516947852957585L;

    /** The String value. */
    private final String m_password;

    /**
     * Creates a new password entry.
     *
     * @param key the key for this value
     * @param value the password, maybe <code>null</code>
     */
    public ConfigPasswordEntry(final String key, final String value) {
        super(ConfigEntries.xpassword, key);
        m_password = value;
    }

    /**
     * Returns the password.
     *
     * @return the password or <code>null</code>
     */
    public String getPassword() {
        return m_password;
    }

    /**
     * Returns the password.
     *
     * @return the password or <code>null</code>
     */
    @Override
    public String toStringValue() {
        return m_password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasIdenticalValue(final AbstractConfigEntry ace) {
        ConfigPasswordEntry e = (ConfigPasswordEntry) ace;
        return Objects.equals(m_password, e.m_password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractJSONEntry toJSONEntry() {
        return new JSONPassword(m_password);
    }

    /* Utility methods for (no-)password validation in configurations, eee AP-15442: A system property that
     * would forbid to save weakly encrypted passwords in a user configuration.  */

    /**
     * Traverse the elements in the argument and returns <code>true</code> if any (recursive) child element is of type
     * {@link ConfigEntries#xpassword}.
     *
     * @param config To search (not null).
     * @param onlyNullPasswords Returns <code>true</code> only if there are passwords and their value is null
     *            (<code>null</code> can only be set when {@link #replacePasswordsWithNull(ConfigBase)} was called, a
     *            <code>null</code> password in the user code is encrypted to something non-null)
     * @return that property
     * @since 5.15
     * @noreference This method is not intended to be referenced by clients.
     */
    public static boolean containsPassword(final ConfigBase config, final boolean onlyNullPasswords) {
        for (String key : config) {
            AbstractConfigEntry entry = config.get(key);
            switch (entry.getType()) {
                case config:
                    ConfigBase c = (ConfigBase)entry;
                    if (containsPassword(c, onlyNullPasswords)) {
                        return true;
                    }
                    break;
                case xpassword:
                    if (!onlyNullPasswords || ((ConfigPasswordEntry)entry).getPassword() == null) {
                        return true;
                    }
                default:
            }
        }
        return false;
    }

    /**
     * Recursively visits all entries in the argument and for any password entry ({@link ConfigEntries#xpassword} it
     * will set the value to <code>null</code>.
     *
     * @param config To traverse and 'fix' (not null).
     * @since 5.15
     * @noreference This method is not intended to be referenced by clients.
     */
    public static void replacePasswordsWithNull(final ConfigBase config) {
        for (String key : config) {
            AbstractConfigEntry entry = config.get(key);
            switch (entry.getType()) {
                case config:
                    replacePasswordsWithNull((ConfigBase)entry);
                    break;
                case xpassword:
                    config.put(new ConfigPasswordEntry(key, null));
                    break;
                default:
            }
        }
    }
}
