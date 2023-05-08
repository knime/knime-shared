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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.json.JsonObject;

/**
 * Interface for all {@link UiComponentConverter}s that are managed by the {@link UiComponentConverterRegistry}. A
 * {@link UiComponentConverter} can be initialized from a JSON encoded workflow representation (aka.
 * DialogNodeRepresentation) and can insert itself into the specific sections of a jsonforms dialog definition.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noreference This class is not intended to be referenced by clients.
 *
 * @author Carsten Haubold, KNIME GmbH, Konstanz, Germany
 */
public interface UiComponentConverter {

    /**
     * @param dialogNodeRepresentation The JSON string containing the workflow representation of the UI element
     * @param parameterName The name that the parameter should have in the jsonforms representation
     * @throws IOException In case the input JSON could not be parsed
     */
    void initializeFromWorkflowRepresentationJson(JsonNode dialogNodeRepresentation, String parameterName)
        throws IOException;

    /**
     * @param jsonNode The JSON node for the jsonforms model, where the model of the UI component will be inserted
     */
    void insertData(ObjectNode jsonNode);

    /**
     * @param jsonNode The JSON node for the jsonforms schema, where the schema of the UI component will be inserted
     */
    void insertSchema(ObjectNode jsonNode);

    /**
     * @param jsonNode The JSON node for the jsonforms ui schema, where the ui schema of the UI component will be
     *            inserted
     */
    void insertUiSchema(ObjectNode jsonNode);

    /**
     * Construct a {@link JsonObject} containing the value inside the provided jsonforms model returned when the
     * jsonforms dialog is closed with new settings. The {@link JsonObject} can be loaded into a DialogNodeValue to
     * apply the new value.
     *
     * Note: we return the JsonObject instead of a DialogNodeValue here so we do not need to introduce a dependency to
     * org.knime.core.ui which would lead to cyclic dependencies.
     *
     * @param jsonFormsModel The jsonforms model with new values
     * @param parameterName The name of the parameter inside the jsonforms model
     * @return A {@link JsonObject} containing the new value that can be loaded into a DialogNodeValue
     */
    JsonObject getDialogNodeValueJsonFromJsonFormsModel(JsonNode jsonFormsModel);
}
