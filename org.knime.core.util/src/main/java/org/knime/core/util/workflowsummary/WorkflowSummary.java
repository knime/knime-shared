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
import java.util.List;
import java.util.Map;

import org.knime.core.util.XMLUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * Interfaces that represents the workflow summary and also contains the serialization instructions (jackson).
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noreference This class is not intended to be referenced by clients.
 */
@JacksonXmlRootElement(localName = "WorkflowSummary")
@JsonAutoDetect(getterVisibility = Visibility.NON_PRIVATE)
@JsonPropertyOrder({"version", "summaryCreationDateTime", "environment", "workflow"})
@SuppressWarnings("javadoc")
public interface WorkflowSummary {

    @JacksonXmlProperty(isAttribute = true)
    String getVersion();

    @JacksonXmlProperty(isAttribute = true)
    String getSummaryCreationDateTime();

    Environment getEnvironment();

    Workflow getWorkflow();

    @JsonPropertyOrder({"knimeVersion", "os", "installation", "systemProperties"})
    public interface Environment {
        @JacksonXmlProperty(isAttribute = true)
        String getOS();

        @JacksonXmlProperty(isAttribute = true)
        String getKnimeVersion();

        Installation getInstallation();

        Map<String, String> getSystemProperties();
    }

    public interface Installation {
        @JacksonXmlProperty(localName = "plugin")
        @JacksonXmlElementWrapper(localName = "plugins")
        List<Plugin> getPlugins();
    }

    @JsonPropertyOrder({"name", "version"})
    public interface Plugin {
        @JacksonXmlProperty(isAttribute = true)
        String getName();

        @JacksonXmlProperty(isAttribute = true)
        String getVersion();
    }

    @JsonPropertyOrder({"nodes", "annotations", "metadata"})
    public interface Workflow {

        @JacksonXmlProperty(isAttribute = true)
        String getName();

        @JacksonXmlProperty(localName = "node")
        @JacksonXmlElementWrapper(localName = "nodes")
        List<Node> getNodes();

        @JacksonXmlProperty(localName = "annotation")
        @JacksonXmlElementWrapper(localName = "annotations")
        List<String> getAnnotations();

        WorkflowMetadata getMetadata();
    }

    @JsonPropertyOrder({"id", "name", "type", "state", "graphDepth", "annotation", "metanode", "component", "encrypted",
        "factoryKey", "nodeMessage", "settings", "outputs", "subWorkflow", "executionStatistics", "storageInformation", "jobManager",
        "deprecated", "parentId", "linkInfo", "flowVariables"})
    public interface Node {

        @JacksonXmlProperty(isAttribute = true)
        String getId();

        @JacksonXmlProperty(isAttribute = true)
        String getName();

        @JacksonXmlProperty(isAttribute = true)
        String getType();

        @JacksonXmlProperty(isAttribute = true)
        String getAnnotation();

        @JacksonXmlProperty(isAttribute = true)
        Boolean isMetanode();

        @JacksonXmlProperty(isAttribute = true)
        Boolean isComponent();

        @JacksonXmlProperty(isAttribute = true)
        Boolean isEncrypted();

        @JacksonXmlProperty(isAttribute = true)
        String getState();

        @JacksonXmlProperty(isAttribute = true)
        Integer getGraphDepth();

        @JacksonXmlProperty(isAttribute = true)
        Boolean isDeprecated();

        @JacksonXmlProperty(isAttribute = true)
        String getParentId();

        NodeFactoryKey getFactoryKey();

        NodeMessage getNodeMessage();

        @JacksonXmlProperty(localName = "setting")
        @JacksonXmlElementWrapper(localName = "settings")
        List<Setting> getSettings();

        Workflow getSubWorkflow();

        @JacksonXmlProperty(localName = "output")
        @JacksonXmlElementWrapper(localName = "outputs")
        List<OutputPort> getOutputs();

        ExecutionStatistics getExecutionStatistics();

        StorageInformation getStorageInformation();

        JobManager getJobManager();

        LinkInfo getLinkInfo();

        @JacksonXmlProperty(localName = "flowvariable")
        @JacksonXmlElementWrapper(localName = "flowvariables")
        List<FlowVariable> getFlowVariables();
    }

    @JsonPropertyOrder({"className", "settings"})
    public interface NodeFactoryKey {

        @JacksonXmlProperty(isAttribute = true)
        String getClassName();

        String getSettings();

    }

    @JsonPropertyOrder({"type", "message"})
    public interface NodeMessage {
        @JacksonXmlProperty(isAttribute = true)
        String getType();

        String getMessage();
    }

    public interface ExecutionStatistics {

        @JacksonXmlProperty(isAttribute = true)
        String getLastExecutionStartTime();

        @JacksonXmlProperty(isAttribute = true)
        long getLastExecutionDuration();

        @JacksonXmlProperty(isAttribute = true)
        long getExecutionDurationSinceReset();

        @JacksonXmlProperty(isAttribute = true)
        long getExecutionDurationSinceStart();

        @JacksonXmlProperty(isAttribute = true)
        int getExecutionCountSinceReset();

        @JacksonXmlProperty(isAttribute = true)
        int getExecutionCountSinceStart();

    }

    @JsonPropertyOrder({"savedToDisk", "path", "sizeOnDisk"})
    public interface StorageInformation {

        @JacksonXmlProperty(isAttribute = true)
        boolean isSavedToDisk();

        @JacksonXmlProperty(isAttribute = true)
        String getPath();

        @JacksonXmlProperty(isAttribute = true)
        Long getSizeOnDisk();

    }

    @JsonPropertyOrder({"name", "id", "settings"})
    public interface JobManager {

        @JacksonXmlProperty(isAttribute = true)
        String getId();

        @JacksonXmlProperty(isAttribute = true)
        String getName();

        @JacksonXmlProperty(localName = "setting")
        @JacksonXmlElementWrapper(localName = "settings")
        List<Setting> getSettings();
    }

    @JsonPropertyOrder({"author", "creationDate", "description", "lastUploaded", "lastEdited"})
    public interface WorkflowMetadata {

        @JacksonXmlProperty(isAttribute = true)
        String getAuthor();

        @JacksonXmlProperty(isAttribute = true)
        String getCreationDate();

        @JacksonXmlProperty(isAttribute = true)
        String getLastUploaded();

        @JacksonXmlProperty(isAttribute = true)
        String getLastEdited();

        String getDescription();
    }

    @JsonPropertyOrder({"index", "type", "inactive", "tableSpec", "dataSummary", "successors"})
    public interface OutputPort {

        @JacksonXmlProperty(isAttribute = true)
        int getIndex();

        @JacksonXmlProperty(isAttribute = true)
        String getType();

        @JacksonXmlProperty(isAttribute = true)
        Boolean isInactive();

        TableSpec getTableSpec();

        String getDataSummary();

        @JacksonXmlProperty(localName = "successor")
        @JacksonXmlElementWrapper(localName = "successors")
        List<Successor> getSuccessors();

    }

    @JsonPropertyOrder({"id", "index"})
    public interface Successor extends Comparable<Successor> {

        @JacksonXmlProperty(isAttribute = true)
        String getId();

        @JacksonXmlProperty(isAttribute = true)
        int getPortIndex();
    }

    public interface TableSpec {
        @JacksonXmlProperty(localName = "column")
        @JacksonXmlElementWrapper(localName = "columns")
        List<Column> getColumns();
    }

    @JsonPropertyOrder({"name", "type", "index", "columnDomain"})
    public interface Column {
        @JacksonXmlProperty(isAttribute = true)
        String getName();

        @JacksonXmlProperty(isAttribute = true)
        String getType();

        @JacksonXmlProperty(isAttribute = true)
        int getIndex();

        ColumnDomain getColumnDomain();

        @JacksonXmlProperty(localName = "columnproperty")
        @JacksonXmlElementWrapper(localName = "columnproperties")
        List<ColumnProperty> getColumnProperties();

    }

    @JsonPropertyOrder({"values", "lowerBound", "upperBound"})
    public interface ColumnDomain {

        @JacksonXmlProperty(localName = "value")
        @JacksonXmlElementWrapper(localName = "values")
        List<String> getValues();

        String getLowerBound();

        String getUpperBound();

    }

    @JsonPropertyOrder({"key", "value"})
    public interface ColumnProperty {

        @JacksonXmlProperty(isAttribute = true)
        String getKey();

        String getValue();
    }

    @JsonPropertyOrder({"name", "type", "value"})
    public interface FlowVariable {

        @JacksonXmlProperty(isAttribute = true)
        String getName();

        @JacksonXmlProperty(isAttribute = true)
        String getType();

        String getValue();

    }

    @JsonPropertyOrder({"key", "value", "type", "settings"})
    public interface Setting {

        @JacksonXmlProperty(isAttribute = true)
        String getKey();

        @JacksonXmlProperty(isAttribute = true)
        @JsonSerialize(using = SettingValueSerializer.class)
        String getValue();

        @JacksonXmlProperty(isAttribute = true)
        String getType();

        @JacksonXmlProperty(localName = "setting")
        @JacksonXmlElementWrapper(localName = "settings")
        List<Setting> getSettings();
    }

    @JsonPropertyOrder({"sourceURI", "timeStamp", "updateStatus"})
    public interface LinkInfo {

        @JacksonXmlProperty(isAttribute = true)
        String getSourceURI();

        @JacksonXmlProperty(isAttribute = true)
        String getTimeStamp();

        @JacksonXmlProperty(isAttribute = true)
        String getUpdateStatus();
    }

    static class SettingValueSerializer extends StdSerializer<String> {

        protected SettingValueSerializer() {
            super(String.class);
        }

        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(final String value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
            if (gen instanceof ToXmlGenerator) {
                gen.writeString(XMLUtils.escape(value));
            } else {
                gen.writeString(value);
            }
        }

    }

}

