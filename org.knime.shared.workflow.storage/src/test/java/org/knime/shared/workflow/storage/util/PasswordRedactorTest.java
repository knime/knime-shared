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

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.ConfigValuePasswordDef;
import org.knime.shared.workflow.storage.multidir.util.LoaderUtils;
import org.knime.shared.workflow.storage.text.util.ObjectMapperUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Tests around serialization of sensitive information such as passwords.
 *
 * When copying and pasting node settings (e.g., as part of a server connector node), passwords should be handled
 * according to use case, e.g., removed before being inserted into the system clipboard.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
class PasswordRedactorTest {

    // this should never be stored unencrypted
    private static final String PASSWORD = "zebra";

    private static final String ENCRYPTION_KEY = "random encryption key";

    private static final String KEY = "database password";

    // configuration with sensitive information
    private static final SimpleConfig SENSITIVE_SETTINGS = new SimpleConfig("sensitive");
    static {
        SENSITIVE_SETTINGS.addPassword(KEY, ENCRYPTION_KEY, PASSWORD);
    }

    static private ConfigValuePasswordDef getPasswordNode(final ConfigMapDef def) {
        return (ConfigValuePasswordDef)def.getChildren().get(KEY);
    }

    /**
     * For sharing a def across JVMs, passwords should be removed using a {@link PasswordRedactor}.
     */
    @Test
    void testRedactToNull() throws InvalidSettingsException, JsonProcessingException {
        // ConfigBase -> Def
        var def = LoaderUtils.toConfigMapDef(SENSITIVE_SETTINGS, PasswordRedactor.asNull());
        // the value in the def is removed
        assertThat(getPasswordNode(def).getValue()).isEmpty();

        // Def -> String
        var json = ObjectMapperUtil.toString(def);
        assertThat(json)
            .as("Password leaked into string representation. This exposes password information to the system clipboard "
                + "if no further precautionary measures are met when converting copy content to string via its "
                + "intermediate workflow format (def) representation.")
            // password not leaked
            .doesNotContain(PASSWORD);

        // String -> Def
        var restoredDef = ObjectMapperUtil.fromString(json, ConfigMapDef.class);
        // the value in the def is nulled
        assertThat(getPasswordNode(restoredDef))//
            // and is restored as password entry
            .asInstanceOf(InstanceOfAssertFactories.type(ConfigValuePasswordDef.class))//
            // but contains no value
            .extracting("value").isNull();

        // Def -> ConfigBase
        var config = LoaderUtils.toConfigBase(restoredDef, PasswordRedactor.asNull());
        assertThat(config.getPassword(KEY, ENCRYPTION_KEY))
            // the key is present (to avoid InvalidSettingsException due to removal of the key-value pair)
            .asInstanceOf(InstanceOfAssertFactories.STRING)
            // but the associated value is redacted to an empty string
            .isEqualTo("");
    }

    /**
     * For copying and pasting within the same JVM , conversion between ConfigBase and ConfigMapDef is lossless. This
     * requires the use of a password redactor that leaves passwords unchanged.
     *
     * @throws InvalidSettingsException
     */
    @Test
    void testRedactWeaklyEncryptedInternal() throws InvalidSettingsException {
        // ConfigBase -> Def
        var def = LoaderUtils.toConfigMapDef(SENSITIVE_SETTINGS, PasswordRedactor.unsafe());
        assertThat(getPasswordNode(def))//
            .asInstanceOf(InstanceOfAssertFactories.type(ConfigValuePasswordDef.class))//
            // is not null
            .extracting("value").asInstanceOf(InstanceOfAssertFactories.STRING)
            // does not contain plaintext
            .doesNotContain(PASSWORD);

        // Def -> ConfigBase
        var restoredConfig = LoaderUtils.toConfigBase(def, PasswordRedactor.unsafe());
        assertThat(restoredConfig.getPassword(KEY, ENCRYPTION_KEY)).isEqualTo(PASSWORD);
    }

    /**
     * For copying and pasting between JVMs via the system clipboard, passwords are removed. For instance, copying a
     * KNIME Server Connector Node will copy its node settings, which contains the password, that must not be exposed to
     * the system clipboard.
     *
     * @throws InvalidSettingsException
     * @throws JsonProcessingException
     */
    @Test
    void testWeaklyEncryptedExternal() throws InvalidSettingsException, JsonProcessingException {
        var def = LoaderUtils.toConfigMapDef(SENSITIVE_SETTINGS, PasswordRedactor.unsafe());

        // Def -> String
        var string = ObjectMapperUtil.toString(def);
        assertThat(string)
            .as("Password leaked into string representation. This exposes password information to the system clipboard "
                + "if no further precautionary measures are met when converting copy content to string via its "
                + "intermediate workflow format (def) representation.")
            // password not leaked
            .doesNotContain(PASSWORD);

        // String -> Def
        // when reading them back, the value is null
        var restoredDef = ObjectMapperUtil.fromString(string, ConfigMapDef.class);
        // the value in the def is nulled
        assertThat(getPasswordNode(restoredDef))//
            // and is restored as password entry
            .asInstanceOf(InstanceOfAssertFactories.type(ConfigValuePasswordDef.class))//
            .extracting("value").asInstanceOf(InstanceOfAssertFactories.STRING)
            // password not leaked
            .doesNotContain(PASSWORD);

        // Def -> ConfigBase
        var config = LoaderUtils.toConfigBase(restoredDef, PasswordRedactor.unsafe());
        assertThat(config.getPassword(KEY, ENCRYPTION_KEY))
            // the key is present (to avoid InvalidSettingsException due to removal of the key-value pair)
            .isEqualTo(PASSWORD);
    }
}
