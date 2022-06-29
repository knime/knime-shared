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
 *
 * History
 *   17 May 2022 (carlwitt): created
 */
package org.knime.shared.workflow.storage.util;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigPasswordEntry;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.ConfigValuePasswordDef;
import org.knime.shared.workflow.def.ConfigValueTransientStringDef;
import org.knime.shared.workflow.def.impl.ConfigValuePasswordDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueTransientStringDefBuilder;

/**
 * Exchangeable logic for handling passwords in {@link ConfigBase} instances.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public interface PasswordRedactor {

    /**
     * @return a redactor that replaces password values with <code>null</code>.
     */
    public static PasswordRedactor asNull() {
        return new PasswordRedactor() {
            @Override
            public ConfigValuePasswordDef apply(final ConfigValuePasswordDef t) {
                return new ConfigValuePasswordDefBuilder(t).setValue(null).build();
            }

            @Override
            public void restore(final ConfigBase settings, final String key, final ConfigValuePasswordDef redacted) {
                // add an entry (to avoid errors due to missing keys in the node settings)
                // but replace the password with an empty string
                settings.addEncryptedPassword(key, "");
            }

            @Override
            public ConfigValueTransientStringDef apply(final ConfigValueTransientStringDef t) {
                return new ConfigValueTransientStringDefBuilder(t).setValue(null).build();
            }

            @Override
            public void restore(final ConfigBase settings, final String key,
                final ConfigValueTransientStringDef redacted) {
                settings.addTransientString(key, null);
            }
        };
    }

    /**
     * @return a redactor that leaves passwords unchanged.
     */
    public static PasswordRedactor unsafe() {
        return new PasswordRedactor() {
            @Override
            public ConfigValuePasswordDef apply(final ConfigValuePasswordDef t) {
                return t;
            }

            @Override
            public void restore(final ConfigBase settings, final String key, final ConfigValuePasswordDef redacted) {
                settings.addEncryptedPassword(key, redacted.getValue());
            }

            @Override
            public ConfigValueTransientStringDef apply(final ConfigValueTransientStringDef t) {
                return t;
            }

            @Override
            public void restore(final ConfigBase settings, final String key,
                final ConfigValueTransientStringDef redacted) {
                settings.addTransientString(key, redacted.getValue());
            }
        };
    }

    /**
     * This will be called when converting {@link ConfigBase} instances to {@link ConfigMapDef} instances.
     *
     * @param t the password value def containing the information from the {@link ConfigPasswordEntry} from the
     *            underlying {@link ConfigBase} instance.
     * @return the returned value will be inserted into the {@link ConfigMapDef} to represent the
     *         {@link ConfigPasswordEntry}
     */
    ConfigValuePasswordDef apply(final ConfigValuePasswordDef t);

    /**
     * This will be called when converting {@link ConfigMapDef} to {@link ConfigBase}.
     *
     * Instead of returning the entry to add, the method itself will add the entry to the given settings instance (due
     * to the design of the {@link ConfigBase} API).
     *
     * @param settings the instance currently being constructed
     * @param key the key of the entry currently being added
     * @param redacted the def, as previously produced by {@link #apply(ConfigValuePasswordDef)}.
     */
    void restore(ConfigBase settings, String key, ConfigValuePasswordDef redacted);

    /**
     * Analogous to {@link #apply(ConfigValuePasswordDef)}
     *
     * @param t contains the string value of a transient string entry in a {@link ConfigBase} object
     * @return value unchanged when def is used only internally (internal clipboard) or value set to null when def is
     *         used externally (e.g., copied to system clipboard)
     */
    ConfigValueTransientStringDef apply(ConfigValueTransientStringDef t);

    /**
     * Analogous to {@link #restore(ConfigBase, String, ConfigValuePasswordDef)}
     *
     * @param settings the instance currently being constructed
     * @param key the key of the entry currently being added
     * @param redacted the def, as previously produced by {@link #apply(ConfigValueTransientStringDef)}.
     */
    void restore(ConfigBase settings, String key, ConfigValueTransientStringDef redacted);
}
