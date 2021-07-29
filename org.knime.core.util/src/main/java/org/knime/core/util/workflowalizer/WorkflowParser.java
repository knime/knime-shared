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
 *   Oct 8, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.util.Version;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;

/**
 * Parses metadata for workflows from a pre-loaded {@link MetadataConfig}.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
interface WorkflowParser {

    // -- Workflow --

    /**
     * @return the workflowset meta file name for this version
     */
    String getWorkflowSetMetaFileName();

    /**
     * @param config workflow config
     * @return a {@code Collection} of file names which are expected to exist in the workflow directory. This should
     *         include node directories, which can be parsed from the {@link ConfigBase}.
     * @throws InvalidSettingsException
     */
    Collection<String> getExpectedFileNames(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @return the workflow file to read (i.e. "workflow.knime")
     */
    String getWorkflowFileName();

    /**
     * @param workflowConfig the {@link ConfigBase} the workflow file was read into
     * @param templateConfig the {@link ConfigBase} the template file was read into, if this is not null it will be
     *            checked for the version field first
     * @return the version
     * @throws InvalidSettingsException
     */
    Version getVersion(final ConfigBase workflowConfig, final ConfigBase templateConfig) throws InvalidSettingsException;

    /**
     * @param workflowConfig the {@link ConfigBase} the workflow file was read into
     * @param templateConfig the {@link ConfigBase} the template file was read into, if this is not null it will be
     *            checked for the version field first
     * @return the created-by version
     * @throws InvalidSettingsException
     */
    Version getCreatedBy(final ConfigBase workflowConfig, final ConfigBase templateConfig) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the name
     * @throws InvalidSettingsException
     */
    String getName(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the custom description
     * @throws InvalidSettingsException
     */
    Optional<String> getCustomDescription(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the annotations' text
     * @throws InvalidSettingsException
     */
    Optional<List<String>> getAnnotations(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @param nodeMap a map of node ID to node
     * @return the connections
     * @throws InvalidSettingsException
     */
    List<NodeConnection> getConnections(final ConfigBase config, final Map<Integer, NodeMetadata> nodeMap)
        throws InvalidSettingsException;

    /**
     * @param config the config the xml was read into
     * @return a list of child configs which represent the node data in the workflow file
     * @throws InvalidSettingsException
     */
    List<ConfigBase> getNodeConfigs(final ConfigBase config) throws InvalidSettingsException;

    // -- Node --

    /**
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the content based hash code of the factory_settings as a {@code String}
     * @throws InvalidSettingsException
     */
    String getFactorySettingsHashCode(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node id
     * @throws InvalidSettingsException
     */
    int getId(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's type
     * @throws InvalidSettingsException
     */
    NodeType getType(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param workflowConfig the config containing the workflow xml
     * @param nodeConfig the config containing the node xml
     * @return the node's annotation text
     * @throws InvalidSettingsException
     */
    Optional<String> getAnnotationText(final ConfigBase workflowConfig, final ConfigBase nodeConfig) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} for a specific, single, node entry (ex: the result of
     *            {@code config.getConfigBase("nodes").getConfigBase("node_1")})
     * @return the path to the settings file for the given node
     * @throws InvalidSettingsException
     */
    String getNodeSettingsFilePath(final ConfigBase config) throws InvalidSettingsException;

    // -- Single Node --

    /**
     * @param settingsXml {@link ConfigBase} "settings.xml" was read into
     * @param nodeXml {@link ConfigBase} "node.xml" was read into
     * @return the node's model parameters
     * @throws InvalidSettingsException
     */
    Optional<ConfigBase> getNodeConfiguration(final ConfigBase settingsXml, final ConfigBase nodeXml) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's custom description
     * @throws InvalidSettingsException
     */
    Optional<String> getCustomNodeDescription(final ConfigBase config) throws InvalidSettingsException;

    // -- "Top-level" workflow --

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the author name
     * @throws InvalidSettingsException
     */
    String getAuthorName(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the authored/creation date
     * @throws InvalidSettingsException
     * @throws ParseException
     */
    Date getAuthoredDate(final ConfigBase config) throws InvalidSettingsException, ParseException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the last editor name
     * @throws InvalidSettingsException
     */
    Optional<String> getEditorName(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the last edited date
     * @throws InvalidSettingsException
     * @throws ParseException
     */
    Optional<Date> getEditedDate(final ConfigBase config) throws InvalidSettingsException, ParseException;

    /**
     * @return the file name for the workflow svg
     */
    String getWorkflowSVGFileName();

    /**
     * @return the directory name of the artifacts directory (i.e. ".artifacts")
     */
    String getArtifactsDirectoryName();

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the workflow credentials' names
     * @throws InvalidSettingsException
     */
    List<String> getWorkflowCredentialName(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the workflow's variable names
     * @throws InvalidSettingsException
     */
    List<String> getWorkflowVariables(final ConfigBase config) throws InvalidSettingsException;

    /**
     * Returns the workflow configuration file name.
     *
     * @return the workflow configuration file name
     */
    String getWorkflowConfiguration();

    /**
     * Returns the workflow configuration representations file name.
     *
     * @return the workflow configuration representations file name
     */
    String getWorkflowConfigurationRepresentation();

    // -- Template --

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the template role
     * @throws InvalidSettingsException
     */
    String getRole(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the template time stamp
     * @throws InvalidSettingsException
     * @throws ParseException
     *
     * @deprecated use {@link #getComponentTimestamp(ConfigBase)} instead, it supports time zones properly
     */
    @Deprecated
    Date getTimeStamp(final ConfigBase config) throws InvalidSettingsException, ParseException;


    /**
     * Reads the timestamp from a metanode/component template. If no timezone is available (for metanodes/components
     * created before 4.4.1) UTC will be assumed.
     *
     * @param config the metanode/component configuration
     * @return the timestamp
     * @throws InvalidSettingsException if an expected configuration value is missing
     * @throws ParseException if the timestamp could not be parsed
     */
    OffsetDateTime getComponentTimestamp(final ConfigBase config) throws InvalidSettingsException, ParseException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the template's source URI, if non-null
     * @throws InvalidSettingsException
     */
    Optional<String> getSourceURI(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config the {@link ConfigBase} the xml was read into
     * @return the template's type
     * @throws InvalidSettingsException
     */
    String getTemplateType(final ConfigBase config) throws InvalidSettingsException;

    // -- Component Template --

    /**
     * Returns the ID of the virtual input node.
     *
     * @param config the {@link ConfigBase} the settings.xml was read into
     * @return the ID
     * @throws InvalidSettingsException
     */
    int getVirtualInId(final ConfigBase config) throws InvalidSettingsException;

    /**
     * Returns the ID of the virtual output node.
     *
     * @param config the {@link ConfigBase} the settings.xml was read into
     * @return the ID
     * @throws InvalidSettingsException
     */
    int getVirtualOutId(final ConfigBase config) throws InvalidSettingsException;

    /**
     * Returns the description for this shared component.
     *
     * @param inputSettingsXml the {@link ConfigBase} which represents settings.xml of the component's virtual input
     *            node.
     * @param settingsXml the {@link ConfigBase} which represents the settings.xml of the component.
     * @return the description
     * @throws InvalidSettingsException
     */
    Optional<String> getComponentTemplateDescription(final ConfigBase inputSettingsXml, final ConfigBase settingsXml)
        throws InvalidSettingsException;

    /**
     * Returns {@code true} if the node is a dialog, {@code false} otherwise.
     *
     * @param nodeConfiguration the {@link ConfigBase} the node's model configuration was read into
     * @return if the node is a dialog node or not
     * @throws InvalidSettingsException
     */
    boolean isDialogNode(final ConfigBase nodeConfiguration) throws InvalidSettingsException;

    /**
     * Returns {@code true} if the node produces an interactive view, {@code false} otherwise.
     *
     * @param nodeConfiguration the {@link ConfigBase} the node's model configuration was read into
     * @return if the node produces an interactive view or not
     * @throws InvalidSettingsException
     */
    boolean isInteractiveViewNode(final ConfigBase nodeConfiguration) throws InvalidSettingsException;

    /**
     * Returns the port names for a component.
     *
     * <p>
     * For load versions {@code <= 3.8.0}, if these are the names of the inports or outports depends on the
     * {@code nodeConfiguration} passed in. If the virtual input node's model configuration was passed in, then the port
     * names are for the inports.
     * </p>
     * <p>
     * For load versions greater than 3.8.0, the port names can be read from the component's settings.xml file. And
     * whether or not the inport/outport names are read depends on the value of {@code readInport}
     * </p>
     *
     * @param nodeConfiguration the {@link ConfigBase} the components virtual input/output node model configuration was
     *            read into
     * @param settingsXml the {@link ConfigBase} representing the component's settings.xml.
     * @param readInport if {@code true} the inport names will be read, otherwise the outport names are read. This
     *            variable is <b>only</b> respected for components with {@code loadVersion > 3.8.0}.
     * @return port names for the component
     * @throws InvalidSettingsException
     */
    List<Optional<String>> getPortNames(final ConfigBase nodeConfiguration, final ConfigBase settingsXml, final boolean readInport) throws InvalidSettingsException;

    /**
     * Returns the port descriptions for a component.
     *
     * <p>
     * For load versions {@code <= 3.8.0}, if these are the descriptions of the inports or outports depends on the
     * {@code nodeConfiguration} passed in. If the virtual input node's model configuration was passed in, then the port
     * names are for the inports.
     * </p>
     * <p>
     * For load versions {@code > 3.8.0}, the port descriptions can be read from the component's settings.xml file. And
     * whether or not the inport/outport names are read depends on the value of {@code readInport}
     * </p>
     *
     * @param nodeConfiguration the {@link ConfigBase} the components virtual input/output node model configuration was
     *            read into
     * @param settingsXml the {@link ConfigBase} representing the component's settings.xml.
     * @param readInport if {@code true} the inport names will be read, otherwise the outport descriptions are read.
     *            This variable is <b>only</b> respected for components with {@code loadVersion > 3.8.0}.
     * @return port descriptions for the component
     * @throws InvalidSettingsException
     */
    List<Optional<String>> getPortDescriptions(final ConfigBase nodeConfiguration, final ConfigBase settingsXml, final boolean readInport) throws InvalidSettingsException;

    /**
     * Returns a list of the inport object types, where the indices of the list correspond to the indices of the
     * inports.
     *
     * @param settingsXml the {@link ConfigBase} the component template's settings.xml was read into
     * @return inport object types
     * @throws InvalidSettingsException
     */
    List<String> getInPortObjects(final ConfigBase settingsXml) throws InvalidSettingsException;

    /**
     * Returns a list of the outport object types, where the indices of the list correspond to the indices of the
     * outports.
     *
     * @param settingsXml the {@link ConfigBase} the component template's settings.xml was read into
     * @return outport object types
     * @throws InvalidSettingsException
     */
    List<String> getOutPortObjects(final ConfigBase settingsXml) throws InvalidSettingsException;

    /**
     * Returns the name of the dialog option which is provided by the dialog node whose node model configuration is
     * provided.
     *
     * @param nodeConfiguration the {@link ConfigBase} the dialog node's model configuration was read into
     * @return name of dialog option
     * @throws InvalidSettingsException
     */
    Optional<String> getDialogFieldName(final ConfigBase nodeConfiguration) throws InvalidSettingsException;

    /**
     * Returns the description of the dialog option which is provided by the dialog node whose node model configuration
     * is provided.
     *
     * @param nodeConfiguration the {@link ConfigBase} the dialog node's model configuration was read into
     * @return description of dialog option
     * @throws InvalidSettingsException
     */
    Optional<String> getDialogFieldDescription(final ConfigBase nodeConfiguration) throws InvalidSettingsException;

    /**
     * Returns the component's icon.
     *
     * @param settingsXml the {@link ConfigBase} the component's settings.xml was read into
     * @return the component's icon as a base64 encoded String
     * @throws InvalidSettingsException
     */
    Optional<String> getIcon(final ConfigBase settingsXml) throws InvalidSettingsException;

    /**
     * Returns the component type.
     *
     * @param settingsXml the {@link ConfigBase} the component's settings.xml was read into
     * @return the component type
     * @throws InvalidSettingsException
     */
    Optional<String> getComponentType(final ConfigBase settingsXml) throws InvalidSettingsException;

    // -- MetaNodes --

    /**
     * Returns the node's template link, if present.
     *
     * <p>
     * This method will attempt to decode the given link. However, if decoding the link fails it will still return the
     * encoded link.
     * </p>
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's template link
     * @throws InvalidSettingsException
     */
    Optional<String> getTemplateLink(final ConfigBase config) throws InvalidSettingsException;

    // -- Native Node --

    /**
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's feature symbolic name
     * @throws InvalidSettingsException
     */
    Optional<String> getFeatureSymbolicName(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's feature name
     * @throws InvalidSettingsException
     */
    Optional<String> getFeatureName(final ConfigBase config) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's feature vendor
     * @throws InvalidSettingsException
     */
    Optional<String> getFeatureVendor(final ConfigBase config) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's feature version
     * @throws InvalidSettingsException
     */
    Optional<Version> getFeatureVersion(final ConfigBase config) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's bundle symbolic name
     * @throws InvalidSettingsException
     */
    Optional<String> getBundleSymbolicName(final ConfigBase config) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's bundle name
     * @throws InvalidSettingsException
     */
    Optional<String> getBundleName(final ConfigBase config) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's bundle vendor
     * @throws InvalidSettingsException
     */
    Optional<String> getBundleVendor(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param settingsXml {@link ConfigBase} "settings.xml" was read into
     * @param nodeXml {@link ConfigBase} "node.xml" was read into
     * @return the node's name
     * @throws InvalidSettingsException
     */
    Optional<String> getNodeName(final ConfigBase settingsXml, final ConfigBase nodeXml) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's factory class name
     * @throws InvalidSettingsException
     */
    Optional<String> getFactoryClass(final ConfigBase config) throws InvalidSettingsException;

    /**
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's bundle version
     * @throws InvalidSettingsException
     */
    Optional<Version> getBundleVersion(final ConfigBase config) throws InvalidSettingsException;

    /**
     * Determine if the workflow contains a report file.
     *
     * @param files a {@link Stream} of files in the workflow directory. In the case of a directory this should be a
     *            {@code Stream<Path>} and if it is a zip file this should be {@code Stream<? extends ZipEntry>}.
     * @return true if the workflow has a report, false otherwise
     */
    <T> boolean getHasReport(final Stream<T> files);
}
