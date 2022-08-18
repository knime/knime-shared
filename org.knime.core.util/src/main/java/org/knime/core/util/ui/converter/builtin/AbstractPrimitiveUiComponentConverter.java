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
package org.knime.core.util.ui.converter.builtin;

import java.io.IOException;

import org.knime.core.util.ui.converter.UiComponentConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Abstract base class for {@link UiComponentConverter}s that represent primitive type values.
 *
 * @author Carsten Haubold, KNIME GmbH, Konstanz, Germany
 * @param <T> The primitive paramter type of this {@link UiComponentConverter}
 */
public abstract class AbstractPrimitiveUiComponentConverter<T> implements UiComponentConverter {

    private static final String CURRENT_VALUE = "currentValue";

    private static final String DEFAULT_VALUE = "defaultValue";

    /**
     * The {@link JsonNodeFactory} to use when constructing this UI component's jsonforms representation
     */
    protected static final JsonNodeFactory JSON_FACTORY = JsonNodeFactory.instance;

    /**
     * @param value The String representation of a value of type T
     * @return the value converted to its real type T
     */
    protected abstract T convertValue(String value);

    /**
     * The name of the parameter in the jsonforms representation
     */
    protected String m_parameterName;

    private String m_label;

    private String m_description;

    private T m_value;

    /**
     * @return the description of this UI component, displayed as help text
     */
    protected String getDescription() {
        return m_description;
    }

    /**
     * @return the label of this UI component
     */
    protected String getLabel() {
        return m_label;
    }

    /**
     * @return the JSON key to access the current value in the workflow representation of this UI component, depends on
     *         the data type
     */
    protected abstract String getCurrentValueString();

    /**
     * @return the type description for the jsonforms representation
     */
    protected abstract String getTypeString();

    /**
     * @return the format specifier for this UI component's value for the jsonforms schema representation. May be null.
     */
    protected String getFormatString() {
        return null;
    }

    /**
     * @return the options format specifier for this UI component's value for the jsonforms UI schema representation.
     *         May be null.
     */
    protected String getOptionsFormatString() {
        return null;
    }

    /**
     * Specify whether this UI component is a slider element, because in that case we need to set a special option in
     * jsonforms. The default implementation returns false, overload this method in slider UI components.
     *
     * @return true if this element is a slider, false by default
     */
    protected boolean isSlider() {
        return false;
    }

    /**
     * Override this in derived classes to extract more data from the workflow representation JSON.
     *
     * @param jsonNode The parsed JSON workflow representation
     * @throws IOException
     */
    protected void initializeInternals(final JsonNode jsonNode) throws IOException {
        // by default, nothing to do.
    }

    @Override
    public void initializeFromWorkflowRepresentationJson(final JsonNode workflowRepresentation,
        final String parameterName) throws IOException {
        m_label = workflowRepresentation.get("label").asText();
        m_description = workflowRepresentation.get("description").asText();
        m_parameterName = parameterName;

        if (workflowRepresentation.has(CURRENT_VALUE)) {
            m_value = convertValue(workflowRepresentation.get(CURRENT_VALUE).get(getCurrentValueString()).asText());
        } else if (workflowRepresentation.has(DEFAULT_VALUE)) {
            m_value = convertValue(workflowRepresentation.get(DEFAULT_VALUE).get(getCurrentValueString()).asText());
        } else {
            throw new IOException("Cannot parse workflow representation without current and default value");
        }

        initializeInternals(workflowRepresentation);

    }

    @Override
    public void insertData(final ObjectNode jsonNode) {
        jsonNode.putPOJO(m_parameterName, m_value);
    }

    @Override
    public void insertSchema(final ObjectNode jsonNode) {
        var objNode = JSON_FACTORY.objectNode();
        objNode.put("description", getDescription());
        objNode.put("type", getTypeString());
        if (getFormatString() != null) {
            objNode.put("format", getFormatString());
        }
        jsonNode.set(m_parameterName, objNode);
    }

    @Override
    public void insertUiSchema(final ObjectNode jsonNode) {
        var objNode = JSON_FACTORY.objectNode();
        objNode.put("type", "Control");
        objNode.put("label", getLabel());
        // do we have groups/sections in component configuration nodes?
        objNode.put("scope", "#/properties/model/properties/" + m_parameterName);
        var optionsNode = JSON_FACTORY.objectNode();
        if (getOptionsFormatString() != null) {
            optionsNode.put("format", getOptionsFormatString());
        }
        if (isSlider()) {
            optionsNode.put("slider", true);
        }
        objNode.set("options", optionsNode);
        jsonNode.set(m_parameterName, objNode);
    }
}
