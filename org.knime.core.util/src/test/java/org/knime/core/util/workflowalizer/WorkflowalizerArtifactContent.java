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
 *   Nov 11, 2022 (Dmytro Karimov): created
 */
package org.knime.core.util.workflowalizer;

/**
 * Content constants for workflowalizer-test workflow
 *
 * @author Dmytro Karimov, KNIME GmbH, Konstanz, Germany
 */
public enum WorkflowalizerArtifactContent {

    /**
     * This constant is needed to check the contents
     * of the workflowalizer-test.zip/Testing_Workflowalizer_360Pre/.artifacts/workflow-configuration.json file
     */
    WORKFLOW_CONFIGURATION("{\n"
            + "  \"test\": \"value\",\n"
            + "  \"test\": \"two\"\n"
            + "}\n"),
    /**
     * This constant is needed to check the contents
     * of the workflowalizer-test.zip/Testing_Workflowalizer_360Pre/.artifacts/workflow-configuration-representation file
     */
    WORKFLOW_CONFIGURATION_REPRESENTATION("{\n" +
            "  \"test\": \"value\",\n" +
            "  \"test\": \"two\"\n" +
            "}\n"),
    /**
     * This constant is needed to check the contents
     * of the workflowalizer-test.zip/Testing_Workflowalizer_360Pre/.artifacts/openapi-input-parameters.json file
     */
    OPENAPI_INPUT_PARAMETERS("{\n" +
            "    \"type\":\"object\",\n" +
            "    \"properties\":{\n" +
            "        \"convert-int-16:17\":{\n" +
            "            \"type\":\"boolean\",\n" +
            "            \"default\":\"true\",\n" +
            "            \"description\":\"Enter Description\",\n" +
            "            \"example\":true\n" +
            "        }\n" +
            "    }\n" +
            "}"),
    /**
     * This constant is needed to check the contents
     * of the workflowalizer-test.zip/Testing_Workflowalizer_360Pre/.artifacts/openapi-input-resources.json file
     */
    OPENAPI_INPUT_RESOURCES("{\n" +
            "    \"schema\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "            \"input-file\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"format\": \"binary\"\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    \"encoding\": {\n" +
            "        \"input-file\": {\n" +
            "            \"contentType\": \"application/octet-stream\"\n" +
            "        }\n" +
            "    }\n" +
            "}"),
    /**
     * This constant is needed to check the contents
     * of the workflowalizer-test.zip/Testing_Workflowalizer_360Pre/.artifacts/openapi-output-parameters.json file
     */
    OPENAPI_OUTPUT_PARAMETERS("{\n" +
            "    \"type\": \"object\",\n" +
            "    \"properties\": {\n" +
            "        \"row-output\": {\n" +
            "            \"description\": \"\",\n" +
            "            \"example\": null\n" +
            "        }\n" +
            "    }\n" +
            "}"),
    /**
     * This constant is needed to check the contents
     * of the workflowalizer-test.zip/Testing_Workflowalizer_360Pre/.artifacts/openapi-output-resources.json file
     */
    OPENAPI_OUTPUT_RESOURCES("{\n" +
            "    \"schema\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "            \"output-file\": {\n" +
            "                \"type\": \"string\",\n" +
            "                \"format\": \"binary\"\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    \"encoding\": {\n" +
            "        \"output-file\": {\n" +
            "            \"contentType\": \"application/octet-stream\"\n" +
            "        }\n" +
            "    }\n" +
            "}"),
    /**
     * This constant is needed to check the contents
     * of the workflowalizer-test.zip/Testing_Workflowalizer_360Pre/.artifacts/hub-event-input-parameters.json file
     */
    HUB_EVENT_INPUT_RESOURCES("{\n" +
            "    \"types\": [\n" +
            "        \"repository\"\n" +
            "    ]\n" +
            "}"),
    ;

    private final String value;

    WorkflowalizerArtifactContent(String value) {
        this.value = value;
    }

    /**
     * @return expected content int the artifact file
     */
    public String value() {
        return value;
    }
}
