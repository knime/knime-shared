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
 *   Oct 29, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.util.ConfigUtils;
import org.knime.core.util.Version;

/**
 * Abstract base class for {@code WorkflowParser}s.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
abstract class AbstractWorkflowParser implements WorkflowParser {

    // -- Workflow --

    @Override
    public String getWorkflowSetMetaFileName() {
        return "workflowset.meta";
    }

    @Override
    public Collection<String> getExpectedFileNames(final ConfigBase config) throws InvalidSettingsException {
        final List<String> files = new ArrayList<>();
        for (final String node : config.getConfigBase("nodes").keySet()) {
            final String path = config.getConfigBase("nodes").getConfigBase(node).getString("node_settings_file");
            final File f = new File(path);
            files.add(f.getParent());
        }
        files.add("workflow.knime");
        files.add("workflowset.meta");
        files.add("template.knime");
        files.add("settings.xml");
        files.add(".artifacts");
        files.add(".project");
        files.add(".savedWithData");
        files.add("workflow.svg");
        return Collections.unmodifiableCollection(files);
    }

    @Override
    public String getWorkflowFileName() {
        return "workflow.knime";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version getVersion(final ConfigBase workflowConfig, final ConfigBase templateConfig)
        throws InvalidSettingsException {
        final String key = "version";
        if (templateConfig != null) {
            if (templateConfig.containsKey(key)) {
                final String v = templateConfig.getString(key);
                return new Version(v);
            }
        }
        final String v = workflowConfig.getString(key);
        return new Version(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Version getCreatedBy(final ConfigBase workflowConfig, final ConfigBase templateConfig)
        throws InvalidSettingsException {
        final String key = "created_by";
        if (templateConfig != null) {
            if (templateConfig.containsKey(key)) {
                final String v = templateConfig.getString(key);
                return new Version(v);
            }
        }
        final String v = workflowConfig.getString(key);
        return new Version(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(final ConfigBase config) throws InvalidSettingsException {
        return config.getString("name");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCustomDescription(final ConfigBase config) throws InvalidSettingsException {
        return Optional.ofNullable(config.getString("customDescription"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<String>> getAnnotations(final ConfigBase config) throws InvalidSettingsException {
        if (!config.containsKey("annotations")) {
            return Optional.empty();
        }
        final List<String> annotations = new ArrayList<>();
        final ConfigBase c = config.getConfigBase("annotations");
        for (final String key : c.keySet()) {
            annotations.add(c.getConfigBase(key).getString("text"));
        }
        return annotations.isEmpty() ? Optional.empty() : Optional.ofNullable(Collections.unmodifiableList(annotations));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NodeConnection> getConnections(final ConfigBase config, final Map<Integer, NodeMetadata> nodeMap) throws InvalidSettingsException {
        final List<NodeConnection> connections = new ArrayList<>();
        final ConfigBase c = config.getConfigBase("connections");
        for (final String key : c.keySet()) {
            final int sourceId = c.getConfigBase(key).getInt("sourceID");
            final int destId = c.getConfigBase(key).getInt("destID");
            final int sourcePort = c.getConfigBase(key).getInt("sourcePort");
            final int destPort = c.getConfigBase(key).getInt("destPort");
            final Optional<NodeMetadata> source = Optional.ofNullable(nodeMap.get(sourceId));
            final Optional<NodeMetadata> dest = Optional.ofNullable(nodeMap.get(destId));
            connections.add(new NodeConnection(sourceId, source, sourcePort, destId, dest, destPort));
        }
        return Collections.unmodifiableList(connections);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConfigBase> getNodeConfigs(final ConfigBase config) throws InvalidSettingsException {
        final List<ConfigBase> nodes = new ArrayList<>();
        final ConfigBase c = config.getConfigBase("nodes");
        for (final String key : c.keySet()) {
            nodes.add(c.getConfigBase(key));
        }
        return nodes;
    }

    // -- Nodes --

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeSettingsFilePath(final ConfigBase config) throws InvalidSettingsException {
        return config.getString("node_settings_file");
    }

    @Override
    public String getFactorySettingsHashCode(final ConfigBase config) throws InvalidSettingsException {
        if (config.containsKey("factory_settings")) {
            final ConfigBase facSettings = config.getConfigBase("factory_settings");
            return ConfigUtils.contentBasedHash(facSettings) + "";
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId(final ConfigBase config) throws InvalidSettingsException {
        return config.getInt("id");
    }

    // -- Single Nodes --

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ConfigBase> getNodeConfiguration(final ConfigBase settingsXml, final ConfigBase nodeXml)
        throws InvalidSettingsException {
        return settingsXml.containsKey("model") ? Optional.ofNullable(settingsXml.getConfigBase("model"))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getAnnotationText(final ConfigBase workflowConfig, final ConfigBase nodeConfig)
        throws InvalidSettingsException {
        // Native nodes & subnodes
        if (nodeConfig != null) {
            return nodeConfig.containsKey("nodeAnnotation")
                ? Optional.ofNullable(nodeConfig.getConfigBase("nodeAnnotation").getString("text")) : Optional.empty();
        }
        // Metanodes
        return workflowConfig.containsKey("nodeAnnotation")
            ? Optional.ofNullable(workflowConfig.getConfigBase("nodeAnnotation").getString("text")) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCustomNodeDescription(final ConfigBase config) throws InvalidSettingsException {
        final String desc = config.getString("customDescription");
        return desc == null || desc.isEmpty() ? Optional.empty() : Optional.ofNullable(desc);
    }

    // -- "Top-level" workflows --

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorName(final ConfigBase config) throws InvalidSettingsException {
        if (!config.containsKey("authorInformation")) {
            return "<unknown>";
        }
        return config.getConfigBase("authorInformation").getString("authored-by");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getAuthoredDate(final ConfigBase config) throws InvalidSettingsException, ParseException {
        if (!config.containsKey("authorInformation")) {
            return new Date(0);
        }
        final String s = config.getConfigBase("authorInformation").getString("authored-when");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return df.parse(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getEditorName(final ConfigBase config) throws InvalidSettingsException {
        if (!config.containsKey("authorInformation")) {
            return Optional.empty();
        }
        return Optional.ofNullable(config.getConfigBase("authorInformation").getString("lastEdited-by"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Date> getEditedDate(final ConfigBase config) throws InvalidSettingsException, ParseException {
        if (!config.containsKey("authorInformation")) {
            return Optional.empty();
        }
        final String s = config.getConfigBase("authorInformation").getString("lastEdited-when");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return s == null || s.isEmpty() ? Optional.empty() : Optional.ofNullable(df.parse(s));
    }

    @Override
    public String getWorkflowSVGFileName() {
        return "workflow.svg";
    }

    @Override
    public String getArtifactsDirectoryName() {
        return ".artifacts";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getWorkflowCredentialName(final ConfigBase config) throws InvalidSettingsException {
        if (!config.containsKey("workflow_credentials")) {
            return Collections.emptyList();
        }
        final ConfigBase credentials = config.getConfigBase("workflow_credentials");
        final List<String> keys = new ArrayList<>();
        for (final String key : credentials.keySet()) {
            // key and name fields are equal
            keys.add(key);
        }
        return keys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getWorkflowVariables(final ConfigBase config) throws InvalidSettingsException {
        if (!config.containsKey("workflow_variables")) {
            return Collections.emptyList();
        }
        final ConfigBase variables = config.getConfigBase("workflow_variables");
        final List<String> varNames = new ArrayList<>();
        for (final String key : variables.keySet()) {
            final ConfigBase var = variables.getConfigBase(key);
            varNames.add(var.getString("name"));

        }
        return varNames;
    }

    // -- Templates --

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRole(final ConfigBase config) throws InvalidSettingsException {
        return config.getConfigBase("workflow_template_information").getString("role");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimeStamp(final ConfigBase config) throws InvalidSettingsException, ParseException {
        final String readDate = config.getConfigBase("workflow_template_information").getString("timestamp");
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(readDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getSourceURI(final ConfigBase config) throws InvalidSettingsException {
        if (config.containsKey("workflow_template_information")) {
            final ConfigBase template = config.getConfigBase("workflow_template_information");
            if (template.containsKey("sourceURI")) {
                final String sourceURI = template.getString("sourceURI");
                return sourceURI == null || sourceURI.isEmpty() ? Optional.empty() : Optional.of(sourceURI);
            }
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateType(final ConfigBase config) throws InvalidSettingsException {
        return config.getConfigBase("workflow_template_information").getString("templateType");
    }

    // -- Metanodes --

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getTemplateLink(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("workflow_template_information")
            ? Optional.ofNullable(config.getConfigBase("workflow_template_information").getString("sourceURI"))
            : Optional.empty();
    }

    // -- Native nodes --

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getFeatureSymbolicName(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("node-feature-symbolic-name")
            ? Optional.ofNullable(config.getString("node-feature-symbolic-name")) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getFeatureName(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("node-feature-name") ? Optional.ofNullable(config.getString("node-feature-name"))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getFeatureVendor(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("node-feature-vendor") ? Optional.ofNullable(config.getString("node-feature-vendor"))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Version> getFeatureVersion(final ConfigBase config) throws InvalidSettingsException {
        if (!config.containsKey("node-feature-version")) {
            return Optional.empty();
        }
        final String v = config.getString("node-feature-version");
        return v == null || v.isEmpty() ? Optional.empty() : Optional.ofNullable(new Version(v));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getBundleSymbolicName(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("node-bundle-symbolic-name")
            ? Optional.ofNullable(config.getString("node-bundle-symbolic-name")) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getBundleName(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("node-bundle-name") ? Optional.ofNullable(config.getString("node-bundle-name"))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getBundleVendor(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("node-bundle-vendor") ? Optional.ofNullable(config.getString("node-bundle-vendor"))
            : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getNodeName(final ConfigBase settingsXml, final ConfigBase nodeXml) throws InvalidSettingsException {
        return settingsXml.containsKey("node-name") ? Optional.ofNullable(settingsXml.getString("node-name")) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getFactoryClass(final ConfigBase config) throws InvalidSettingsException {
        return config.containsKey("factory") ? Optional.ofNullable(config.getString("factory")) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Version> getBundleVersion(final ConfigBase config) throws InvalidSettingsException {
        if (!config.containsKey("node-bundle-version")) {
            return Optional.empty();
        }
        final String v = config.getString("node-bundle-version");
        return v == null || v.isEmpty() ? Optional.empty() : Optional.ofNullable(new Version(v));
    }

}
