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
 *   10 Aug 2022 (Carsten Haubold): created
 */
package org.knime.core.util.ui.converter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.knime.core.util.ui.converter.builtin.BooleanUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.DateTimeUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.DoubleUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.IntegerSliderUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.IntegerUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.StringUiComponentConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This registry manages the {@link UiComponentConverter}s that allow parsing the JSON encoded workflow representation
 * of a configuration node and converting it into the jsonforms representation that we use internally to create JS based
 * NodeDialogs.
 *
 * @author Carsten Haubold, KNIME GmbH, Konstanz, Germany
 */
public final class UiComponentConverterRegistry {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final UiComponentConverterRegistry INSTANCE = new UiComponentConverterRegistry();

    private final Map<String, Class<? extends UiComponentConverter>> m_converters = new HashMap<>();

    private UiComponentConverterRegistry() {
        registerBuiltinConverters();
    }

    /**
     * Get a the matching {@link UiComponentConverter} for the given JSON encoded DialogNodeRepresentation and
     * pre-configure it so that the UI component has the given parameterName in jsonforms.
     *
     * @param workflowRepresentationJson the JSON encoded DialogNodeRepresentation
     * @param parameterName The name of the UI element in jsonforms.
     * @return The {@link UiComponentConverter}. Never null.
     * @throws IOException if the JSON encoded string could not be parsed
     * @throws IllegalStateException if no matching converter was found
     */
    public static UiComponentConverter getConverter(final String workflowRepresentationJson, final String parameterName)
        throws IOException {
        JsonNode jsonNode;
        try {
            jsonNode = OBJECT_MAPPER.readTree(workflowRepresentationJson);
        } catch (JsonProcessingException e) {
            throw new IOException("Cannot parse the workflow representation JSON", e);
        }

        if (!jsonNode.has("@class")) {
            throw new IOException("JSON is no valid workflow representation, missing the '@class' key");
        }

        String clazz = jsonNode.get("@class").asText();
        var converter = getConverterInstance(clazz);

        if (converter == null) {
            throw new IllegalStateException("Could not find a converter for " + clazz);
        }
        converter.initializeFromWorkflowRepresentationJson(jsonNode, parameterName);
        return converter;
    }

    private static UiComponentConverter getConverterInstance(final String className) throws IOException {
        var type = INSTANCE.m_converters.get(className);
        if (type == null) {
            return null;
        }

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException ex) {
            throw new IOException("Could not create instance of converter " + type.getName(), ex);
        }
    }

    private void registerBuiltinConverters() {
        m_converters.put("org.knime.js.base.node.configuration.input.bool.BooleanDialogNodeRepresentation",
            BooleanUiComponentConverter.class);
        m_converters.put("org.knime.js.base.node.configuration.input.integer.IntegerDialogNodeRepresentation",
            IntegerUiComponentConverter.class);
        m_converters.put("org.knime.js.base.node.configuration.input.dbl.DoubleDialogNodeRepresentation",
            DoubleUiComponentConverter.class);
        m_converters.put("org.knime.js.base.node.configuration.input.string.StringDialogNodeRepresentation",
            StringUiComponentConverter.class);
        m_converters.put("org.knime.js.base.node.configuration.input.slider.IntegerSliderDialogNodeRepresentation",
            IntegerSliderUiComponentConverter.class);
        m_converters.put("org.knime.js.base.node.configuration.input.date.DateDialogNodeRepresentation",
            DateTimeUiComponentConverter.class);
    }
}
