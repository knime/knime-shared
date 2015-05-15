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

import java.net.URL;
import java.util.Objects;

import javax.json.JsonObject;

/**
 * This object represents output of a node for workflow-external usage, e.g. in web service calls. Results can either be
 * "small" amounts of data as in in-memory JSON object or larger amounts of data as file-based resources. Additional
 * formats may be added over time. Objects are built using the {@link ExternalNodeOutputBuilder} which can be acquired via
 * {@link #builder(String)}.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @since 2.12
 */
public class ExternalNodeOutput {
    /**
     * The output's ID (resource ID).
     */
    protected String m_id;

    /**
     * The inline JSON output, may be <code>null</code>.
     */
    protected JsonObject m_jsonObject;

    /**
     * URL to a output resource, may be <code>null</code>.
     */
    protected URL m_url;

    /**
     * May be used by subclasses.
     */
    protected ExternalNodeOutput() {

    }

    ExternalNodeOutput(final ExternalNodeOutputBuilder builder) {
        m_id = builder.m_id;
        m_jsonObject = builder.m_jsonObject;
        m_url = builder.m_url;
    }

    /**
     * Returns an ID for this output (may be an empty string if the node is not configured yet).
     *
     * @return unique ID for the result, never <code>null</code>
     */
    public String getID() {
        return m_id;
    }

    /**
     * Returns output data as a generic JSON object. This should only be used for small data as it is kept in memory.
     * The method may return <code>null</code> if no JSON output is provided.<br />
     * Output need only be provided if the node/workflow is executed.
     *
     *
     * @return a JSON object, may be <code>null</code>
     */
    public JsonObject getJSONObject() {
        return m_jsonObject;
    }

    /**
     * Returns the URL to a larger resource. The URL is usually a file URL sitting in a temporary directory. The method
     * may return <code>null</code> if no external resource is provided.<br />
     * Output need only be provided if the node/workflow is executed.
     *
     * @return a URL to a resource, or <code>null</code>
     */
    public URL getResource() {
        return m_url;
    }

    /**
     * Creates a new builder for an external node output.
     *
     * @param id the output id.
     * @return a builder
     */
    public static ExternalNodeOutputBuilder builder(final String id) {
        return new ExternalNodeOutputBuilder(id);
    }

    /**
     * Builder for {@link ExternalNodeOutput}.
     */
    public static class ExternalNodeOutputBuilder {
        final String m_id;

        JsonObject m_jsonObject;

        URL m_url;

        ExternalNodeOutputBuilder(final String id) {
            m_id = Objects.requireNonNull(id, "ID must not be null");
        }

        /**
         * Sets the JSON object for the output.
         *
         * @param jsonObject a JSON object; may be <code>null</code>
         * @return the updated builder
         */
        public ExternalNodeOutputBuilder jsonObject(final JsonObject jsonObject) {
            m_jsonObject = jsonObject;
            return this;
        }

        /**
         * Sets the URL to the output resource.
         *
         * @param url a URL, may be <code>null</code>
         * @return the updated builder
         */
        public ExternalNodeOutputBuilder resource(final URL url) {
            m_url = url;
            return this;
        }

        /**
         * Builds a new external node output with the values from this builder.
         *
         * @return a new external node output
         */
        public ExternalNodeOutput build() {
            return new ExternalNodeOutput(this);
        }
    }
}