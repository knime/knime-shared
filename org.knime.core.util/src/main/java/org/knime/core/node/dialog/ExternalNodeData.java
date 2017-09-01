/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
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
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
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
 *   13.05.2015 (thor): created
 */
package org.knime.core.node.dialog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents output or input of a node for provided or consumed outside the workflow, e.g. in web service
 * calls. The data can either be "small" amounts of data as in in-memory JSON object or larger amounts of data as
 * file-based resources. Additional formats may be added over time. Objects are built using the
 * {@link ExternalNodeDataBuilder} which can be acquired via {@link #builder(String)}.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @since 2.12
 */
public class ExternalNodeData {
    /**
     * Indicator that a string value can be provided but is not available yet.
     */
    public static final String NO_STRING_VALUE_YET = new String("");

    /**
     * Indicator that a JSON value can be provided but is not available yet.
     */
    public static final JsonObject NO_JSON_VALUE_YET = Json.createObjectBuilder().build();

    /**
     * Indicator that a URL value can be provided but is not available yet.
     */
    public static final URL NO_URL_VALUE_YET;

    static {
        try {
            NO_URL_VALUE_YET = new URL("file:/dev/null");
        } catch (MalformedURLException ex) {
            // doesn't happen
            throw new RuntimeException(ex);
        }
    }

    /**
     * The output's ID (resource ID).
     */
    protected String m_id;

    /**
     * The inline JSON output, may be <code>null</code>.
     */
    protected JsonValue m_jsonValue;

    /**
     * The inline plain string output, may be <code>null</code>.
     */
    protected String m_stringValue;

    /**
     * URL to a output resource, may be <code>null</code>.
     */
    protected URL m_url;

    /**
     * May be used by subclasses.
     */
    protected ExternalNodeData() {

    }

    @JsonCreator
    private ExternalNodeData(@JsonProperty("id") final String id) {
        m_id = id;
    }

    ExternalNodeData(final ExternalNodeDataBuilder builder) {
        m_id = builder.m_id;
        m_jsonValue = builder.m_jsonValue;
        m_url = builder.m_url;
        m_stringValue = builder.m_stringValue;
    }

    /**
     * Returns an ID for this data object (may be an empty string if the node is not configured yet).
     *
     * @return unique ID for the data object, never <code>null</code>
     */
    @JsonProperty("id")
    public String getID() {
        return m_id;
    }

    /**
     * Returns node data as a generic JSON value. This should only be used for small data as it is kept in memory.
     * The method may return <code>null</code> if no JSON data is provided.<br>
     * Data needs only be provided if the node/workflow is executed, but providing a template in case the node is
     * configured is beneficial. If {@link #NO_JSON_VALUE_YET} is returned this means that eventually JSON can be
     * provided. A <code>null</code> value means that JSON may never be available.
     *
     * @return a JSON value, may be <code>null</code>
     */
    @JsonProperty("jsonValue")
    @JsonInclude(Include.NON_NULL)
    public JsonValue getJSONValue() {
        return m_jsonValue;
    }

    @JsonProperty("jsonValue")
    private void setJSONValue(final JsonValue value) {
        m_jsonValue = value;
    }

    /**
     * Returns node data as a plain string. This should only be used for small data as it is kept in memory.
     * The method may return <code>null</code> if no string data is provided.<br>
     * Data needs only be provided if the node/workflow is executed, but providing a template in case the node is
     * configured is beneficial. If {@link #NO_STRING_VALUE_YET} is returned this means that eventually JSON can be
     * provided. A <code>null</code> value means that JSON may never be available.
     *
     * @return a string, may be <code>null</code>
     */
    @JsonProperty("stringValue")
    @JsonInclude(Include.NON_NULL)
    public String getStringValue() {
        return m_stringValue;
    }

    @JsonProperty("stringValue")
    private void setStringValue(final String s) {
        m_stringValue = s;
    }

    /**
     * Returns the URL to a larger resource. The URL is usually a file URL sitting in a temporary directory. The method
     * may return <code>null</code> if no external resource is provided.<br>
     * Data needs only be provided if the node/workflow is executed, but providing a template in case the node is
     * configured is beneficial. If {@link #NO_URL_VALUE_YET} is returned this means that eventually JSON can be
     * provided. A <code>null</code> value means that JSON may never be available.
     *
     * @return a URL to a resource, or <code>null</code>
     */
    @JsonProperty("resource")
    @JsonInclude(Include.NON_NULL)
    public URL getResource() {
        return m_url;
    }

    @JsonProperty("resource")
    private void setResource(final URL url) {
        m_url = url;
    }

    /**
     * Creates a new builder for an external node data.
     *
     * @param id the output id.
     * @return a builder
     */
    public static ExternalNodeDataBuilder builder(final String id) {
        return new ExternalNodeDataBuilder(id);
    }

    /**
     * Builder for {@link ExternalNodeData}.
     */
    public static class ExternalNodeDataBuilder {
        final String m_id;

        JsonValue m_jsonValue;

        String m_stringValue;

        URL m_url;

        ExternalNodeDataBuilder(final String id) {
            m_id = Objects.requireNonNull(id, "ID must not be null");
        }

        /**
         * Sets the JSON value for the external data. Either a JSON value or the string value should be set.
         *
         * @param jsonValue a JSON value; may be <code>null</code>
         * @return the updated builder
         */
        public ExternalNodeDataBuilder jsonValue(final JsonValue jsonValue) {
            m_jsonValue = jsonValue;
            return this;
        }

        /**
         * Sets the string value for the external data. Either a JSON value or the string value should be set.
         *
         * @param stringValue a string value; may be <code>null</code>
         * @return the updated builder
         */
        public ExternalNodeDataBuilder stringValue(final String stringValue) {
            m_stringValue = stringValue;
            return this;
        }

        /**
         * Sets the URL to the external resource.
         *
         * @param url a URL, may be <code>null</code>
         * @return the updated builder
         */
        public ExternalNodeDataBuilder resource(final URL url) {
            m_url = url;
            return this;
        }

        /**
         * Builds a new external node data with the values from this builder.
         *
         * @return a new external node data
         */
        public ExternalNodeData build() {
            return new ExternalNodeData(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (m_jsonValue != null) {
            return m_id + " = " + m_jsonValue;
        } else if (m_url != null) {
            return m_id + " = " + m_url;
        } else if (m_stringValue != null) {
            return m_id + " = " + m_stringValue;
        } else {
            return m_id + " (no data)";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
        result = prime * result + ((m_jsonValue == null) ? 0 : m_jsonValue.hashCode());
        result = prime * result + ((m_stringValue == null) ? 0 : m_stringValue.hashCode());
        result = prime * result + ((m_url == null) ? 0 : m_url.toString().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExternalNodeData other = (ExternalNodeData)obj;
        if (m_id == null) {
            if (other.m_id != null) {
                return false;
            }
        } else if (!m_id.equals(other.m_id)) {
            return false;
        }
        if (m_jsonValue == null) {
            if (other.m_jsonValue != null) {
                return false;
            }
        } else if (!m_jsonValue.equals(other.m_jsonValue)) {
            return false;
        }
        if (m_stringValue == null) {
            if (other.m_stringValue != null) {
                return false;
            }
        } else if (!m_stringValue.equals(other.m_stringValue)) {
            return false;
        }
        if (m_url == null) {
            if (other.m_url != null) {
                return false;
            }
        } else if (!m_url.toString().equals(other.m_url.toString())) {
            return false;
        }
        return true;
    }

    /**
     * Returns the requested external node data object from the map. The input id can either be fully qualified , i.e.
     * containing the node id at the end (json-input-1:1) or unqualified, i.e. only using the name defined in the node
     * (json-input). First an exact match is attempted and if that fails an unqualified match is attempted. If both fail
     * or don't produce a unique entry an {@link IllegalArgumentException} will be thrown.
     *
     * @param inputMap a map with input data
     * @param inputId the request input id, either fully qualified or unqualified
     * @return the requested object, never <code>null</code>
     * @throws IllegalArgumentException if no unique entry could be found
     * @since 5.7
     */
    public static ExternalNodeData getData(final Map<String, ExternalNodeData> inputMap, final String inputId) {
        ExternalNodeData extData = inputMap.get(inputId);
        if (extData == null) {
            List<ExternalNodeData> candidates = inputMap.values().stream()
                    .filter(e -> e.getID().equals(inputId)).collect(Collectors.toList());
            if (candidates.isEmpty()) {
                throw new IllegalArgumentException("No such input parameter: " + inputId);
            } else if (candidates.size() > 1) {
                throw new IllegalArgumentException(
                    "Input parameter " + inputId + " is not unique, try using a fully qualified name instead");
            } else {
                return candidates.get(0);
            }
        } else {
            return extData;
        }
    }
}