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
 *   Jan 10, 2017 (wiswedel): created
 */
package org.knime.core.node.config.base;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.knime.core.node.config.base.json.JSONRoot;
import org.knime.core.node.util.CheckUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Serialization/Deserialization of {@link ConfigBase} type objects into JSON.
 * @author Bernd Wiswedel
 * @since 5.7
 */
public final class JSONConfig {

    /** A writer configuration object describing how to write, e.g. 'indented' printing.
     * List of options may grow over time. Reasonsable defaults available as static singletons. */
    public static final class WriterConfig {

        /** Default writing, no pretty printing, no line breaks, no extra spaces. */
        public static final WriterConfig DEFAULT = new WriterConfig();
        /** Enabled {@link #setPrettyPrinting(boolean)}. */
        public static final WriterConfig PRETTY = new WriterConfig().setPrettyPrinting(true);

        private boolean m_prettyPrinting;

        /**
         * Enable pretty printing (indent, line breaks).
         * @param prettyPrinting the argument
         * @return this
         */
        public WriterConfig setPrettyPrinting(final boolean prettyPrinting) {
            m_prettyPrinting = prettyPrinting;
            return this;
        }

    }

    /** Writes the argument to JSON and returns the string represenation.
     * @param config What to write, not null.
     * @param writerConf The config, not null.
     * @return The string representation of <code>config</code>
     * @throws IllegalArgumentException If config is not a subclass of {@link ConfigBase} (which all
     * KNIME core implemenentations are).
     */
    public static String toJSONString(final ConfigBaseRO config, final WriterConfig writerConf) {
        StringWriter writer = new StringWriter();
        try {
            writeJSON(config, writer, writerConf);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't serialize as JSON: " + e.getMessage(), e);
        }
        return writer.toString();
    }

    /** Writes the argument to the argument writer
     * @param config What to write, not null.
     * @param writer The output writer (will be closed by this call).
     * @param writerConf The config, not null.
     * @throws IOException On I/O problem
     * @throws IllegalArgumentException If config is not a subclass of {@link ConfigBase} (which all
     * KNIME core implemenentations are).
     */
    public static void writeJSON(final ConfigBaseRO config, final Writer writer,
        final WriterConfig writerConf) throws IOException {
        ConfigBase configBase = getChecked(config);
        JSONRoot root = configBase.toJSONRoot();
        ObjectMapper mapper = new ObjectMapper();
        if (writerConf.m_prettyPrinting) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        mapper.registerModule(new Jdk8Module());
        mapper.writeValue(writer, root);
    }

    public static <C extends ConfigBaseWO> C readJSON(final C emptyConfig, final Reader reader) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JSONRoot root = mapper.readValue(reader, JSONRoot.class);
        root.addToConfigBase(getChecked(emptyConfig), (conf, entry) -> conf.put(entry));
        return emptyConfig;
    }

    /** Performs validation and case of argument.
     * @param config Non-null, cast to ConfigBaseRO
     * @return Cast object
     */
    private static ConfigBase getChecked(final Object config) {
        CheckUtils.checkArgumentNotNull(config);
        CheckUtils.checkState(config instanceof ConfigBase, "Argument not subclass of %s but %s",
            ConfigBase.class.getSimpleName(), config.getClass().getName());
        ConfigBase configBase = (ConfigBase)config;
        return configBase;
    }


}
