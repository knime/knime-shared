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
 *   17 Aug 2022 (Carsten Haubold): created
 */
package org.knime.core.util.ui.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import java.io.IOException;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.knime.core.util.ui.converter.builtin.BooleanUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.DateTimeUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.DoubleUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.IntegerSliderUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.IntegerUiComponentConverter;
import org.knime.core.util.ui.converter.builtin.StringUiComponentConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.json.JsonObject;

/**
 *
 * @author Carsten Haubold, KNIME GmbH, Konstanz, Germany
 */
@SuppressWarnings("javadoc")
class UiConverterTests {
    private static final JsonNodeFactory JSON_FACTORY = JsonNodeFactory.instance;

    private static final String BOOLEAN_WORKFLOW_REPR = "{\n" //
        + "    \"@class\": \"org.knime.js.base.node.configuration.input.bool.BooleanDialogNodeRepresentation\",\n"//
        + "    \"label\": \"Checkbox test\",\n"//
        + "    \"description\": \"Enter Description\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.bool.BooleanDialogNodeValue\",\n"//
        + "        \"boolean\": false\n"//
        + "    },\n"//
        + "    \"currentValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.bool.BooleanDialogNodeValue\",\n"//
        + "        \"boolean\": true\n"//
        + "    }\n"//
        + "}";

    private static final String BOOLEAN_WORKFLOW_REPR_NO_CURRENT_VALUE = "{\n" //
        + "    \"@class\": \"org.knime.js.base.node.configuration.input.bool.BooleanDialogNodeRepresentation\",\n"//
        + "    \"label\": \"Checkbox test\",\n"//
        + "    \"description\": \"Enter Description\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.bool.BooleanDialogNodeValue\",\n"//
        + "        \"boolean\": false\n"//
        + "    }\n"//
        + "}";

    private static final String DOUBLE_WORKFLOW_REPR = "{\n"//
        + "    \"@class\": \"org.knime.js.base.node.configuration.input.dbl.DoubleDialogNodeRepresentation\",\n"//
        + "    \"label\": \"Double test\",\n"//
        + "    \"description\": \"Enter Description\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.dbl.DoubleDialogNodeValue\",\n"//
        + "        \"double\": 0.0\n"//
        + "    },\n"//
        + "    \"currentValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.dbl.DoubleDialogNodeValue\",\n"//
        + "        \"double\": 0.0\n"//
        + "    },\n"//
        + "    \"usemin\": false,\n"//
        + "    \"usemax\": false,\n"//
        + "    \"min\": 0.0,\n"//
        + "    \"max\": 1.0\n"//
        + "}";

    private static final String INT_WORKFLOW_REPR = "{\n"//
        + "    \"@class\": \"org.knime.js.base.node.configuration.input.integer.IntegerDialogNodeRepresentation\",\n"//
        + "    \"label\": \"num Rows\",\n"//
        + "    \"description\": \"the number of rows of data to generate\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.integer.IntegerDialogNodeValue\",\n"//
        + "        \"integer\": 100\n"//
        + "    },\n"//
        + "    \"currentValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.integer.IntegerDialogNodeValue\",\n"//
        + "        \"integer\": 42\n"//
        + "    },\n"//
        + "    \"usemin\": true,\n"//
        + "    \"usemax\": true,\n"//
        + "    \"min\": 1,\n"//
        + "    \"max\": 1000\n"//
        + "}";

    private static final String STRING_WORKFLOW_REPR = "{\n"//
        + "    \"@class\": \"org.knime.js.base.node.configuration.input.string.StringDialogNodeRepresentation\",\n"//
        + "    \"label\": \"String test\",\n"//
        + "    \"description\": \"Enter Description\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.string.StringDialogNodeValue\",\n"//
        + "        \"string\": \"\"\n"//
        + "    },\n"//
        + "    \"currentValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.string.StringDialogNodeValue\",\n"//
        + "        \"string\": \"foobar\"\n"//
        + "    },\n"//
        + "    \"regex\": \"\",\n"//
        + "    \"errorMessage\": \"\",\n"//
        + "    \"editorType\": \"Single-line\",\n"//
        + "    \"multilineEditorWidth\": 40,\n"//
        + "    \"multilineEditorHeight\": 5\n"//
        + "}";//

    private static final String INT_SLIDER_WORKFLOW_REPR = "{\n"//
        + "    \"@class\": \"org.knime.js.base.node.configuration.input.slider.IntegerSliderDialogNodeRepresentation\",\n"//
        + "    \"label\": \"Slider test\",\n"//
        + "    \"description\": \"Enter Description\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.slider.IntegerSliderDialogNodeValue\",\n"//
        + "        \"double\": 50.0\n"//
        + "    },\n"//
        + "    \"currentValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.slider.IntegerSliderDialogNodeValue\",\n"//
        + "        \"double\": 29.0\n"//
        + "    },\n"//
        + "    \"useCustomMin\": true,\n"//
        + "    \"useCustomMax\": true,\n"//
        + "    \"customMin\": 0.0,\n"//
        + "    \"customMax\": 100.0\n"//
        + "}";

    private static final String DATE_WORKFLOW_REPR = "{\n"//
        + "    \"@class\": \"org.knime.js.base.node.configuration.input.date.DateDialogNodeRepresentation\",\n"//
        + "    \"label\": \"Date and time test\",\n"//
        + "    \"description\": \"Enter Description\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.date.DateDialogNodeValue\",\n"//
        + "        \"datestring\": \"2022-08-01T09:03:18+02:00[Europe/Berlin]\"\n"//
        + "    },\n"//
        + "    \"currentValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.date.DateDialogNodeValue\",\n"//
        + "        \"datestring\": \"2022-08-18T09:03:18+02:00[Europe/Berlin]\"\n"//
        + "    },\n"//
        + "    \"shownowbutton\": true,\n"//
        + "    \"granularity\": \"show_minutes\",\n"//
        + "    \"usemin\": false,\n"//
        + "    \"usemax\": false,\n"//
        + "    \"useminexectime\": false,\n"//
        + "    \"usemaxexectime\": false,\n"//
        + "    \"usedefaultexectime\": false,\n"//
        + "    \"min\": \"2022-08-01T09:03:18+02:00[Europe/Berlin]\",\n"//
        + "    \"max\": \"2022-08-01T09:03:18+02:00[Europe/Berlin]\",\n"//
        + "    \"type\": \"LDT\"\n"//
        + "}";

    private static final String BROKEN_WORKFLOW_REPR = "{12./346q23.a3thnslna3";

    private static final String UNKNOWN_WORKFLOW_REPR = "{\n"//
        + "    \"@class\": \"org.my.imaginary.IntegerDialogNodeRepresentation\",\n"//
        + "    \"label\": \"num Rows\",\n"//
        + "    \"description\": \"the number of rows of data to generate\",\n"//
        + "    \"required\": true,\n"//
        + "    \"defaultValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.integer.IntegerDialogNodeValue\",\n"//
        + "        \"integer\": 100\n"//
        + "    },\n"//
        + "    \"currentValue\": {\n"//
        + "        \"@class\": \"org.knime.js.base.node.configuration.input.integer.IntegerDialogNodeValue\",\n"//
        + "        \"integer\": 42\n"//
        + "    }\n"//
        + "}";

    @Test
    void testConverterRegistry() throws IOException {
        assertThat(UiComponentConverterRegistry.getConverter(BOOLEAN_WORKFLOW_REPR,
                "param") instanceof BooleanUiComponentConverter).isTrue();
        assertThat(UiComponentConverterRegistry.getConverter(INT_WORKFLOW_REPR,
                "param") instanceof IntegerUiComponentConverter).isTrue();
        assertThat(UiComponentConverterRegistry.getConverter(DOUBLE_WORKFLOW_REPR,
                "param") instanceof DoubleUiComponentConverter).isTrue();
        assertThat(UiComponentConverterRegistry.getConverter(STRING_WORKFLOW_REPR,
                "param") instanceof StringUiComponentConverter).isTrue();
        assertThat(UiComponentConverterRegistry.getConverter(INT_SLIDER_WORKFLOW_REPR,
                "param") instanceof IntegerSliderUiComponentConverter).isTrue();
        assertThat(UiComponentConverterRegistry.getConverter(DATE_WORKFLOW_REPR,
                "param") instanceof DateTimeUiComponentConverter).isTrue();
        assertThatExceptionOfType(IOException.class).isThrownBy(() -> {
            UiComponentConverterRegistry.getConverter(BROKEN_WORKFLOW_REPR, "test");
        });
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> {
            UiComponentConverterRegistry.getConverter(UNKNOWN_WORKFLOW_REPR, "test");
        });
    }

    @Test
    void testBooleanConverter() throws IOException {
        testConverter(BOOLEAN_WORKFLOW_REPR, "boolean", v -> v.asBoolean(), true, v -> v.getBoolean("boolean"), false);
    }

    @Test
    void testIntegerConverter() throws IOException {
        testConverter(INT_WORKFLOW_REPR, "integer", v -> v.asInt(), 42, v -> v.getInt("integer"), true);
    }

    @Test
    void testDoubleConverter() throws IOException {
        testConverter(DOUBLE_WORKFLOW_REPR, "number", v -> v.asDouble(), 0.0,
            v -> v.getJsonNumber("double").doubleValue(), false);
    }

    @Test
    void testStringConverter() throws IOException {
        testConverter(STRING_WORKFLOW_REPR, "string", v -> v.asText(), "foobar", v -> v.getString("string"), false);
    }

    @Test
    void testDateConverter() throws IOException {
        testConverter(DATE_WORKFLOW_REPR, "string", v -> v.asText(), "2022-08-18T09:03:18+02:00[Europe/Berlin]",
            v -> v.getString("date&time"), false);
    }

    @Test
    void testIntSliderConverter() throws IOException {
        testConverter(INT_SLIDER_WORKFLOW_REPR, "number", v -> v.asDouble(), 29.0,
            v -> v.getJsonNumber("integer").doubleValue(), true);
    }

    private static <T> void testConverter(final String jsonWorkflowRepr, final String typeStr,
        final Function<JsonNode, T> valueExtractor, final T expectedValue,
        final Function<JsonObject, T> dialogValueExtractor, final boolean shouldHaveMinMax) throws IOException {

        var converter = UiComponentConverterRegistry.getConverter(jsonWorkflowRepr, "param");
        var model = JSON_FACTORY.objectNode();
        converter.insertData(model);
        assertThat(model.has("param")).isTrue();
        // This gets the value from the "currentValue" field
        assertThat(valueExtractor.apply(model.get("param"))).isEqualTo(expectedValue);

        var schema = JSON_FACTORY.objectNode();
        converter.insertSchema(schema);
        assertThat(schema.has("param")).isTrue();
        var schemaNode = (ObjectNode)schema.get("param");
        assertThat(schemaNode.get("type").asText()).isEqualTo(typeStr);
        assertThat(schemaNode.has("description")).isTrue();

        assertThat(schemaNode.has("minimum")).isEqualTo(shouldHaveMinMax);
        assertThat(schemaNode.has("maximum")).isEqualTo(shouldHaveMinMax);

        var uiSchema = JSON_FACTORY.objectNode();
        converter.insertUiSchema(uiSchema);
        assertThat(uiSchema.has("param")).isTrue();
        var uiSchemaNode = (ObjectNode)uiSchema.get("param");
        assertThat(uiSchemaNode.get("type").asText()).isEqualTo("Control");
        assertThat(uiSchemaNode.has("label")).isTrue();
        assertThat(uiSchemaNode.has("options")).isTrue();
        var scope = uiSchemaNode.get("scope").asText();
        assertThat(scope).isEqualTo("#/properties/model/properties/param");

        var jsonFormsModel = JSON_FACTORY.objectNode();
        jsonFormsModel.putPOJO("param", expectedValue);
        var output = converter.getDialogNodeValueJsonFromJsonFormsModel(jsonFormsModel);
        assertThat(dialogValueExtractor.apply(output)).isEqualTo(expectedValue);
    }

    @Test
    void testBooleanConverterNoCurrentValue() throws IOException {
        var converter = UiComponentConverterRegistry.getConverter(BOOLEAN_WORKFLOW_REPR_NO_CURRENT_VALUE, "param");
        var model = JSON_FACTORY.objectNode();
        converter.insertData(model);
        assertThat(model.has("param")).isTrue();
        // This gets the value from the "defaultValue" field
        assertThat(model.get("param").asBoolean()).isFalse();
    }

    @Test
    void testJsonFormsDialogBuilder() throws IOException {
        var builder = new JsonFormsDialogBuilder();
        builder.addUiComponent(INT_WORKFLOW_REPR, "int");
        builder.addUiComponent(BOOLEAN_WORKFLOW_REPR, "bool");
        builder.addUiComponent(DOUBLE_WORKFLOW_REPR, "double");
        var jsonforms = builder.build();

        var forms = new ObjectMapper().readTree(jsonforms);
        var model = forms.get("data").get("model");
        assertThat(model.get("int").isInt()).isTrue();
        assertThat(model.get("bool").isBoolean()).isTrue();
        assertThat(model.get("double").isDouble()).isTrue();

        var schema = forms.get("schema").get("properties").get("model").get("properties");
        assertThat(schema.get("int").get("type").asText()).isEqualTo("integer");
        assertThat(schema.get("bool").get("type").asText()).isEqualTo("boolean");
        assertThat(schema.get("double").get("type").asText()).isEqualTo("number");

        var uiSchema = forms.get("ui_schema").get("elements");
        assertThat(uiSchema.has("int")).isTrue();
        assertThat(uiSchema.has("bool")).isTrue();
        assertThat(uiSchema.has("double")).isTrue();
    }
}
