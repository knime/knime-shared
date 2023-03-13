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
 *   13.05.2015 (thor): created
 */
package org.knime.core.node.dialog;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonValue;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.knime.core.node.util.CheckUtils;

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
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 2.12
 */
@NonNullByDefault
public class ExternalNodeData {
    /**
     * A pattern to parse a URL or REST parameter or a batch argument. It reads the
     * In/OutputNode parameter name and an optional node id suffix, which the user may or
     * may not specify (to guarantee uniqueness). For instance, it splits "foobar-123-xy-2" into "foobar-123-xy"
     * (parameter name) and 2 (node id suffix).
     * @since 5.7
     * @noreference This field is not intended to be referenced by clients.
     */
    public static final Pattern PARAMETER_NAME_PATTERN = Pattern.compile("^(?:(.+)-)?(\\d+(?:\\:\\d+)*)$");

    /**
     * Indicator that a string value can be provided but is not available yet.
     */
    public static final String NO_STRING_VALUE_YET = "";

    /**
     * Indicator that a JSON value can be provided but is not available yet.
     */
    public static final JsonObject NO_JSON_VALUE_YET = JsonValue.EMPTY_JSON_OBJECT;

    /**
     * Indicator that a URL value can be provided but is not available yet.
     * @since 5.7
     */
    public static final URI NO_URI_VALUE_YET;

    static {
        URI uri;
        try {
            uri = new URI("file:/dev/null");
        } catch (URISyntaxException e) {
            // doesn't happen
            uri = null;
        }
        NO_URI_VALUE_YET = uri;
    }

    /**
     * The output's ID (resource ID).
     */
    protected String m_id;

    /**
     * The inline JSON output, may be <code>null</code>.
     */
    @Nullable
    protected JsonValue m_jsonValue;

    /**
     * The inline plain string output, may be <code>null</code>.
     */
    @Nullable
    protected String m_stringValue;

    /**
     * URI to a output resource, may be <code>null</code>.
     * @since 5.7
     */
    @Nullable
    protected URI m_uri;


    /**
     * Description, may be <code>null</code>.
     * @since 5.7
     */
    @Nullable
    protected String m_description;

    /**
     * The content type.
     *
     * @see ContentType
     * @since 5.18
     */
    @Nullable
    protected String m_contentType;

    /**
     * May be used by subclasses.
     */
    protected ExternalNodeData() {

    }

    @JsonCreator
    private ExternalNodeData(@JsonProperty("id") final String id) {
        m_id = Objects.requireNonNull(id, "ID must not be null");
    }

    private ExternalNodeData(final ExternalNodeDataBuilder builder) {
        m_id = builder.m_id;
        m_jsonValue = builder.m_jsonValue;
        m_uri = builder.m_uri;
        m_stringValue = builder.m_stringValue;
        m_description = builder.m_description;
        m_contentType = builder.m_contentType;
    }

    /**
     * Returns an ID for this data object (may be an empty string if the node is not configured yet).
     *
     * @return unique ID for the data object, never <code>null</code>
     */
    @JsonProperty("id") @NonNull
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
    @JsonInclude(Include.NON_NULL) @Nullable
    public JsonValue getJSONValue() {
        return m_jsonValue;
    }

    @JsonProperty("jsonValue")
    private void setJSONValue(@Nullable final JsonValue value) {
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
    @JsonInclude(Include.NON_NULL) @Nullable
    public String getStringValue() {
        return m_stringValue;
    }

    @JsonProperty("stringValue")
    private void setStringValue(@Nullable final String s) {
        m_stringValue = s;
    }

    /**
     * Returns the URI to a larger resource. The URI is usually a file URI sitting in a temporary directory. The method
     * may return <code>null</code> if no external resource is provided.<br>
     * Data needs only be provided if the node/workflow is executed, but providing a template in case the node is
     * configured is beneficial. If {@link #NO_URI_VALUE_YET} is returned this means that eventually JSON can be
     * provided. A <code>null</code> value means that JSON may never be available.
     *
     * @return a URL to a resource, or <code>null</code>
     * @since 5.7
     */
    @JsonProperty("resource")
    @JsonInclude(Include.NON_NULL) @Nullable
    public URI getResource() {
        return m_uri;
    }

    @JsonProperty("resource")
    private void setResource(@Nullable final URI uri) {
        m_uri = uri;
    }


    @JsonProperty("description")
    private void setDescription(@Nullable final String s) {
        m_description = s;
    }

    /**
     * Returns an optional description for this external node data.
     *
     * @return a description or an empty optional
     * @since 5.7
     */
    @JsonProperty("description")
    public Optional<String> getDescription() {
        return Optional.ofNullable(m_description);
    }

    @JsonProperty("contentType")
    private void setContentType(@Nullable final String contentType) {
        m_contentType = contentType;
    }

    /**
     * Returns an optional content type for this external node data.
     *
     * @see ContentType
     *
     * @return the content type or an empty optional
     * @since 5.18
     */
    @JsonProperty("contentType")
    public Optional<String> getContentType() {
        return Optional.ofNullable(m_contentType);
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

        @Nullable
        JsonValue m_jsonValue;

        @Nullable
        String m_stringValue;

        @Nullable
        URI m_uri;

        @Nullable
        String m_description;

        @Nullable
        String m_contentType;

        ExternalNodeDataBuilder(final String id) {
            m_id = Objects.requireNonNull(id, "ID must not be null");
        }

        /**
         * Sets the JSON value for the external data. Either a JSON value or the string value should be set.
         *
         * @param jsonValue a JSON value; may be <code>null</code>
         * @return the updated builder
         */
        public ExternalNodeDataBuilder jsonValue(@Nullable final JsonValue jsonValue) {
            m_jsonValue = jsonValue;
            return this;
        }

        /**
         * Sets the string value for the external data. Either a JSON value or the string value should be set.
         *
         * @param stringValue a string value; may be <code>null</code>
         * @return the updated builder
         */
        public ExternalNodeDataBuilder stringValue(@Nullable final String stringValue) {
            m_stringValue = stringValue;
            return this;
        }

        /**
         * Sets the URI to the external resource.
         *
         * @param uri a URI, may be <code>null</code>
         * @return the updated builder
         * @since 5.7
         */
        public ExternalNodeDataBuilder resource(@Nullable final URI uri) {
            m_uri = uri;
            return this;
        }

        /**
         * Sets a description for the external data.
         *
         * @param description a description, may be <code>null</code>
         * @return the updated builder
         * @since 5.7
         */
        public ExternalNodeDataBuilder description(@Nullable final String description) {
            m_description = description;
            return this;
        }

        /**
         * Sets the content type for the external data.
         *
         * @param contentType the content type, may be <code>null</code>
         * @return the updated builder
         * @since 5.18
         */
        public ExternalNodeDataBuilder contentType(@Nullable final String contentType) {
            m_contentType = contentType;
            return this;
        }

        /**
         * Builds a new external node data with the values from this builder.
         *
         * @return a new external node data
         */
        @NonNull
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
        } else if (m_uri != null) {
            return m_id + " = " + m_uri;
        } else if (m_stringValue != null) {
            return m_id + " = " + m_stringValue;
        } else {
            return m_id + " (no data)";
        }
    }

    @Override
    public int hashCode() {
        final var prime = 31;
        var result = 1;
        result = prime * result + m_id.hashCode();
        result = prime * result + ((m_jsonValue == null) ? 0 : m_jsonValue.hashCode());
        result = prime * result + ((m_stringValue == null) ? 0 : m_stringValue.hashCode());
        result = prime * result + ((m_uri == null) ? 0 : m_uri.hashCode());
        result = prime * result + ((m_description == null) ? 0 : m_description.hashCode());
        result = prime * result + ((m_contentType == null) ? 0 : m_contentType.hashCode());
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
        if (!m_id.equals(other.m_id)) {
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
        if (m_uri == null) {
            if (other.m_uri != null) {
                return false;
            }
        } else if (!m_uri.equals(other.m_uri)) {
            return false;
        }
        if (!Objects.equals(this.m_description, other.m_description)) {
            return false;
        }
        return Objects.equals(m_contentType, other.m_contentType);
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

    /**
     * If the argument is fully qualified (such as "foo-bar-17:3") it will return the simple name ("foo-bar"). Otherwise
     * it returns the argument.
     *
     * @param idPossiblyFullyQualified The parameter ID, must not be null.
     * @return the simple parameter name or the argument if already "simple".
     * @since 5.7
     */
    public static String getSimpleIDFrom(final String idPossiblyFullyQualified) {
        var nameMacher = PARAMETER_NAME_PATTERN.matcher(CheckUtils.checkArgumentNotNull(idPossiblyFullyQualified));
        return (nameMacher.matches() && (nameMacher.group(1) != null)) ? nameMacher.group(1) : idPossiblyFullyQualified;
    }

    /**
     * {@inheritDoc}
     * @since 5.10
     */
    @Override
    public ExternalNodeData clone() {
        var clone = new ExternalNodeData();
        clone.m_description = this.m_description;
        clone.m_id = this.m_id;
        clone.m_jsonValue = this.m_jsonValue;
        clone.m_uri = this.m_uri;
        clone.m_contentType = this.m_contentType;
        return clone;
    }
}
