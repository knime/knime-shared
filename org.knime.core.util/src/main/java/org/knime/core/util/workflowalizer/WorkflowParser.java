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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.util.Version;

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
    Optional<String> getName(final ConfigBase config) throws InvalidSettingsException;

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
     * @return the node id
     * @throws InvalidSettingsException
     */
    int getId(final ConfigBase config) throws InvalidSettingsException;

    /**
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's type
     * @throws InvalidSettingsException
     */
    String getType(final ConfigBase config) throws InvalidSettingsException;

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
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's model parameters
     * @throws InvalidSettingsException
     */
    Optional<ConfigBase> getModelParameters(final ConfigBase config) throws InvalidSettingsException;

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

    // -- MetaNodes --

    /**
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
     *
     * @param config a pre-loaded {@link ConfigBase} to be parsed
     * @return the node's name
     * @throws InvalidSettingsException
     */
    Optional<String> getNodeName(final ConfigBase config) throws InvalidSettingsException;

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
}
