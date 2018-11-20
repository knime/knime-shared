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
 *   Oct 31, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.io.IOException;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.AbstractConfigEntry;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigBooleanEntry;
import org.knime.core.node.config.base.ConfigByteEntry;
import org.knime.core.node.config.base.ConfigCharEntry;
import org.knime.core.node.config.base.ConfigDoubleEntry;
import org.knime.core.node.config.base.ConfigFloatEntry;
import org.knime.core.node.config.base.ConfigIntEntry;
import org.knime.core.node.config.base.ConfigLongEntry;
import org.knime.core.node.config.base.ConfigPasswordEntry;
import org.knime.core.node.config.base.ConfigShortEntry;
import org.knime.core.node.config.base.ConfigStringEntry;
import org.knime.core.node.config.base.ConfigTransientStringEntry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * JSON serializer for {@code ConfigBase} objects used in pojos produced by the {@link Workflowalizer}.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public class ConfigBaseEntrySerializer extends StdSerializer<ConfigBase> {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a serializer for ConfigBase objects
     */
    protected ConfigBaseEntrySerializer() {
        super(ConfigBase.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final ConfigBase value, final JsonGenerator gen, final SerializerProvider provider)
        throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeStartObject();
            for (final String key : value.keySet()) {
                final AbstractConfigEntry entry = value.getEntry(key);
                gen.writeFieldName(key);
                final boolean printed = printType(entry, gen);
                if (!printed) {
                    final ConfigBase cb = (ConfigBase) entry;
                    if (cb.containsKey("array-size")) {
                        gen.writeStartArray();
                        printArray(cb, gen);
                        gen.writeEndArray();
                    } else {
                        gen.writeObject(cb);
                    }
                }
            }
            gen.writeEndObject();
        }
    }

    private static boolean printType(final AbstractConfigEntry entry, final JsonGenerator gen)
        throws IOException {
        if (entry instanceof ConfigBooleanEntry) {
            gen.writeBoolean(((ConfigBooleanEntry)entry).getBoolean());
        } else if (entry instanceof ConfigByteEntry) {
            gen.writeNumber(((ConfigByteEntry)entry).getByte());
        } else if (entry instanceof ConfigCharEntry) {
            gen.writeString(((ConfigCharEntry)entry).getChar() + "");
        } else if (entry instanceof ConfigDoubleEntry) {
            gen.writeNumber(((ConfigDoubleEntry)entry).getDouble());
        } else if (entry instanceof ConfigFloatEntry) {
            gen.writeNumber(((ConfigFloatEntry)entry).getFloat());
        } else if (entry instanceof ConfigIntEntry) {
            gen.writeNumber(((ConfigIntEntry)entry).getInt());
        } else if (entry instanceof ConfigLongEntry) {
            gen.writeNumber(((ConfigLongEntry)entry).getLong());
        } else if (entry instanceof ConfigPasswordEntry) {
            gen.writeString(((ConfigPasswordEntry)entry).getPassword());
        } else if (entry instanceof ConfigShortEntry) {
            gen.writeNumber(((ConfigShortEntry)entry).getShort());
        } else if (entry instanceof ConfigStringEntry) {
            gen.writeString(((ConfigStringEntry)entry).getString());
        } else if (entry instanceof ConfigTransientStringEntry) {
            gen.writeString(((ConfigTransientStringEntry)entry).getTransientString());
        } else {
            return false;
        }
        return true;
    }

    private static void printArray(final ConfigBase array, final JsonGenerator gen) throws IOException {
        int arrayLength;
        try {
            arrayLength = array.getInt("array-size");
        } catch (InvalidSettingsException e) {
            return;
        }
        for (int i = 0; i < arrayLength; i++) {
            printType(array.getEntry(Integer.toString(i)), gen);
        }
    }
}
