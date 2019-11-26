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

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;

/**
 * {@code WorkflowParser} for parsing v2.3.0 through v2.6.0 files
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
final class WorkflowParserV230 extends AbstractWorkflowParser {

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeType getType(final ConfigBase config) throws InvalidSettingsException {
        final boolean isMetaNode = config.getBoolean("node_is_meta");
        return isMetaNode ? NodeType.METANODE : NodeType.NATIVE_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorName(final ConfigBase config) throws InvalidSettingsException {
        return "<unknown>";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getAuthoredDate(final ConfigBase config) throws InvalidSettingsException, ParseException {
        return new Date(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getEditorName(final ConfigBase config) throws InvalidSettingsException {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Date> getEditedDate(final ConfigBase config) throws InvalidSettingsException, ParseException {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ConfigBase> getNodeConfiguration(final ConfigBase settingsXml, final ConfigBase nodeXml)
        throws InvalidSettingsException {
        return nodeXml.containsKey("model") ? Optional.ofNullable(nodeXml.getConfigBase("model"))
            : Optional.empty();
    }

    @Override
    public Optional<String> getNodeName(final ConfigBase settingsXml, final ConfigBase nodeXml)
        throws InvalidSettingsException {
        // version 2.6.0 has the node name in both settings.xml and node.xml
        if (settingsXml.containsKey("node-name")) {
            return Optional.ofNullable(settingsXml.getString("node-name"));
        }
        return nodeXml.containsKey("name") ? Optional.ofNullable(nodeXml.getString("name")) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getComponentTemplateDescription(final ConfigBase inputSettingsXml,
        final ConfigBase settingsXml) throws InvalidSettingsException {
        return getComponentDescriptionFromVirtualInputNode(inputSettingsXml);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Optional<String>> getPortNames(final ConfigBase nodeConfiguration, final ConfigBase settingsXml,
        final boolean readInport) throws InvalidSettingsException {
        return getPortNamesFromVirtualNodes(nodeConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Optional<String>> getPortDescriptions(final ConfigBase nodeConfiguration, final ConfigBase settingsXml,
        final boolean readInport) throws InvalidSettingsException {
        return getPortDescriptionsFromVirtualNodes(nodeConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getIcon(final ConfigBase settingsXml) throws InvalidSettingsException {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getComponentType(final ConfigBase settingsXml) throws InvalidSettingsException {
        return Optional.empty();
    }

}
