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
 *   Nov 17, 2020 (hornm): created
 */
package org.knime.core.util.workflowsummary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.knime.core.util.workflowsummary.WorkflowSummary.Environment;
import org.knime.core.util.workflowsummary.WorkflowSummary.ExecutionStatistics;
import org.knime.core.util.workflowsummary.WorkflowSummary.Node;
import org.knime.core.util.workflowsummary.WorkflowSummary.OutputPort;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.mrbean.AbstractTypeMaterializer;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;

/**
 * Utilities method to read and write workflow summaries.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 *
 * @noreference This class is not intended to be referenced by clients.
 */
public final class WorkflowSummaryUtil {

    private static XmlMapper xmlMapper;

    private static JsonMapper jsonMapper;

    private static XmlMapper xmlMapperWithoutExecInfo;

    private static JsonMapper jsonMapperWithoutExecInfo;

    private WorkflowSummaryUtil() {
        // utility class
    }

    /**
     * Deserializes the workflow summary from json.
     *
     * @param in the json input stream
     * @return the new summary instance
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static WorkflowSummary readJSON(final InputStream in) throws IOException {
        return getJsonMapper(true).readValue(in, WorkflowSummary.class);
    }

    /**
     * Serializes a workflow summary instance into json.
     *
     * @param out the stream to write to
     * @param summary the summary to serialize
     * @param includeExecutionInfo if <code>false</code> the execution info will not be written to file
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static void writeJSON(final OutputStream out, final WorkflowSummary summary,
        final boolean includeExecutionInfo) throws IOException {
        getJsonMapper(includeExecutionInfo).writeValue(out, summary);
    }

    /**
     * Serializes a workflow summary instance into xml.
     *
     * @param out the stream to write to
     * @param summary the summary to serialize
     * @param includeExecutionInfo if <code>false</code> the execution info will not be written to file
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static void writeXML(final OutputStream out, final WorkflowSummary summary,
        final boolean includeExecutionInfo) throws IOException {
        getXmlMapper(includeExecutionInfo).writeValue(out, summary);
    }

    private static XmlMapper getXmlMapper(final boolean includeExecInfo) {
        if (includeExecInfo) {
            if (xmlMapper == null) {
                xmlMapper = createXmlMapper();
            }
            return xmlMapper;
        } else {
            if (xmlMapperWithoutExecInfo == null) {
                xmlMapperWithoutExecInfo = createXmlMapper();
                addIgnoreMixIns(xmlMapperWithoutExecInfo);
            }
            return xmlMapperWithoutExecInfo;
        }
    }

    private static XmlMapper createXmlMapper() {
        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);
        // Woodstox library for faster xml processing
        XMLInputFactory ifactory = new WstxInputFactory();
        XMLOutputFactory ofactory = new WstxOutputFactory();
        XmlFactory xf = new XmlFactory(ifactory, ofactory);
        XmlMapper m = new XmlMapper(xf, xmlModule);
        m.setSerializationInclusion(Include.NON_NULL);
        // allows one to deserialize into pure interfaces
        m.registerModule(new MrBeanModule(new AbstractTypeMaterializer(WorkflowSummary.class.getClassLoader())));
        return m;
    }

    private static JsonMapper getJsonMapper(final boolean includeExecInfo) {
        if (includeExecInfo) {
            if (jsonMapper == null) {
                jsonMapper = createJsonMapper();
            }
            return jsonMapper;
        } else {
            if (jsonMapperWithoutExecInfo == null) {
                jsonMapperWithoutExecInfo = createJsonMapper();
                addIgnoreMixIns(jsonMapperWithoutExecInfo);
            }
            return jsonMapperWithoutExecInfo;
        }
    }

    private static JsonMapper createJsonMapper() {
        JsonMapper m = new JsonMapper();
        m.setSerializationInclusion(Include.NON_NULL);
        // allows one to deserialize into pure interfaces
        m.registerModule(new MrBeanModule(new AbstractTypeMaterializer(WorkflowSummary.class.getClassLoader())));
        return m;
    }

    private static void addIgnoreMixIns(final ObjectMapper mapper) {
        mapper.addMixIn(WorkflowSummary.class, IgnoreEnvironment.class);
        mapper.addMixIn(Node.class, IgnoreExecutionStatistics.class);
        mapper.addMixIn(OutputPort.class, IgnoreDataSummary.class);
    }

    interface IgnoreEnvironment {

        @JsonIgnore
        Environment getEnvironment();

    }

    interface IgnoreExecutionStatistics {

        @JsonIgnore
        ExecutionStatistics getExecutionStatistics();

    }

    interface IgnoreDataSummary {

        @JsonIgnore
        String getDataSummary();

    }

}
