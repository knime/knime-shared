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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Builder to construct a dialog using jsonforms based on JSON encoded dialog node representations.
 *
 * @author Carsten Haubold, KNIME GmbH, Konstanz, Germany
 */
public class JsonFormsDialogBuilder {
    private JsonFormsObject m_jsonFormsData = new JsonFormsObject();

    /**
     * Add a UI component to this jsonforms dialog based on a JSON encoded dialog node representation.
     *
     * @param dialogNodeRepresentation the JSON encoded dialog node representation
     * @param parameterName the parameter that this dialog node should have in the jsonforms dialog
     * @return this builder to continue building the dialog
     * @throws IOException
     */
    public JsonFormsDialogBuilder addUiComponent(final String dialogNodeRepresentation, final String parameterName)
        throws IOException {
        try {
            var converter = UiComponentConverterRegistry.getConverter(dialogNodeRepresentation, parameterName);
            converter.insertData((ObjectNode)m_jsonFormsData.m_data.get("model"));
            converter
                .insertSchema((ObjectNode)m_jsonFormsData.m_schema.get("properties").get("model").get("properties"));
            converter.insertUiSchema((ObjectNode)m_jsonFormsData.m_uiSchema.get("elements"));
        } catch (IOException | IllegalStateException e) {
            throw new IOException("Could not add UI component " + parameterName, e);
        }
        return this;
    }

    /**
     * @return the JSON encoded jsonforms dialog
     */
    public String build() {
        return m_jsonFormsData.toString();
    }

    private static class JsonFormsObject {
        public JsonFormsObject() {
            var factory = JsonNodeFactory.instance;

            m_data = factory.objectNode();
            m_data.set("model", factory.objectNode());

            m_schema = factory.objectNode();
            var props = factory.objectNode();
            m_schema.set("properties", props);

            var model = factory.objectNode();
            props.set("model", model);

            var pmp = factory.objectNode();
            model.set("properties", pmp);

            m_uiSchema = factory.objectNode();
            m_uiSchema.put("type", "VerticalLayout");
            m_uiSchema.set("elements", factory.objectNode());
        }

        public ObjectNode m_data;

        public ObjectNode m_schema;

        public ObjectNode m_uiSchema;

        @Override
        public String toString() {
            var factory = JsonNodeFactory.instance;
            var root = factory.objectNode();
            root.set("data", m_data);
            root.set("schema", m_schema);
            root.set("ui_schema", m_uiSchema);

            root.set("flowVariableSettings", factory.objectNode());


            return root.toString();
        }
    }
}
