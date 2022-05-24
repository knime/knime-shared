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
 *   Nov 16, 2021 (hornm): created
 */
package org.knime.core.node.config.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;

/**
 * Tests for {@link ConfigBase}.
 *
 * @author Carl Witt, KNIME AG, Zurich, Switzerland
 */
public class ConfigBaseTest {

    /**
     * Opens a config that has been written using Java serialization. Tries to deserialize. Prior to AP-18979,
     * {@link ConfigBase} did not have a serialVersionUID. Adding a method would change the implicitly generated UID and
     * break downstream code relying on java serialization of instances.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InvalidSettingsException
     */
    @Test
    public void testObjectStreamSerialization() throws IOException, ClassNotFoundException, InvalidSettingsException {
        SimpleConfig config;

        var is = ConfigBaseTest.class.getResourceAsStream("/ConfigBaseTest/configBase");
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            config = (SimpleConfig)ois.readObject();
        }
        assertEquals("12345", config.getPassword("database", "secret"));
    }

    /**
     * This is a failsafe to remind developers that other classes rely on java serialization of the ConfigBase class
     * (although it is ugly and they should not). So remember that when making java serialization incompatible changes
     * to change the serialVersionUID of {@link ConfigBase}.
     */
    @Test
    public void testSchemaUnchanged() {
        var expFields = new String[]{"$assertionsDisabled", "ARROW", "ARROW_NULL", "CFG_ARRAY_SIZE", "COMMA_TYPEEQ",
            "DOT_LINE_BREAK", "KEYEQ", "LINE_BREAK", "SPACE", "TAB_SIZE", "m_map", "serialVersionUID"};

        var expMethods = new String[]{"addBoolean", "addBooleanArray", "addByte", "addByteArray", "addChar",
            "addCharArray", "addConfigBase", "addConfigBase", "addDouble", "addDoubleArray", "addEncryptedPassword",
            "addEntry", "addFloat", "addFloatArray", "addInt", "addIntArray", "addLong", "addLongArray", "addPassword",
            "addShort", "addShortArray", "addString", "addStringArray", "addTransientString", "children", "containsKey",
            "copyTo", "createEncrypter", "get", "getBoolean", "getBoolean", "getBooleanArray", "getBooleanArray",
            "getByte", "getByte", "getByteArray", "getByteArray", "getChar", "getChar", "getCharArray", "getCharArray",
            "getChildAt", "getChildCount", "getConfigBase", "getDouble", "getDouble", "getDoubleArray",
            "getDoubleArray", "getEntry", "getFloat", "getFloat", "getFloatArray", "getFloatArray", "getIndex",
            "getInstance", "getInt", "getInt", "getIntArray", "getIntArray", "getLong", "getLong", "getLongArray",
            "getLongArray", "getPassword", "getPassword", "getShort", "getShort", "getShortArray", "getShortArray",
            "getString", "getString", "getStringArray", "getStringArray", "getTransientString", "hasIdenticalValue",
            "isLeaf", "iterator", "keySet", "lambda$1", "lambda$3", "load", "loadFromXML", "put", "readFromFile",
            "saveToXML", "toJSONEntry", "toJSONRoot", "toString", "toString", "toString", "toStringValue",
            "writeToFile"};

        var actualFields =
            Arrays.stream(ConfigBase.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        assertThat(actualFields).as("The fields of class ConfigBase have changed.").containsOnly(expFields);

        var actualMethods =
            Arrays.stream(ConfigBase.class.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());
        assertThat(actualMethods).as("The methods of class ConfigBase have changed.").containsOnly(expMethods);
    }

    /**
     * This has been used to write the test configuration.
     *
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private static void writeTestConfiguration() throws IOException {
        SimpleConfig config = new SimpleConfig("");
        config.addPassword("database", "secret", "12345");
        File targetFile = new File("/ConfigBaseTest", "configBase");
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(targetFile))) {
            os.writeObject(config);
        }
    }

}
