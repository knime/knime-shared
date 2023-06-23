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

import java.util.List;
import java.util.Optional;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;

/**
 * {@code WorkflowParser} for parsing v5.1.0 files to the current version.
 *
 * @author Leonard Wörteler, KNIME GmbH, Konstanz, Germany
 */
final class WorkflowParserV510 extends AbstractWorkflowParser {

    @Override
    public Optional<String> getNodeContainerMetadataFileName(final boolean isComponent) {
        return Optional.of(isComponent ? "component-metadata.xml" : "workflow-metadata.xml");
    }

    @Override
    public NodeType getType(final ConfigBase config) throws InvalidSettingsException {
        final var type = config.getString("node_type");
        if (type.equalsIgnoreCase(NodeType.NATIVE_NODE.toString())) {
            return NodeType.NATIVE_NODE;
        } else if (type.equalsIgnoreCase(NodeType.SUBNODE.toString())) {
            return NodeType.SUBNODE;
        } else if (type.equalsIgnoreCase(NodeType.METANODE.toString())) {
            return NodeType.METANODE;
        }
        throw new IllegalArgumentException("Unrecognized node type: " + type);
    }

    @Override
    public Optional<String> getComponentTemplateDescription(final ConfigBase inputSettingsXml,
        final ConfigBase settingsXml) throws InvalidSettingsException {
        return getComponentDescriptionFromSettingsXml(settingsXml);
    }

    @Override
    public List<Optional<String>> getPortNames(final ConfigBase nodeConfiguration, final ConfigBase settingsXml,
        final boolean readInport) throws InvalidSettingsException {
        return getPortNamesFromSettingsXml(settingsXml, readInport);
    }

    @Override
    public List<Optional<String>> getPortDescriptions(final ConfigBase nodeConfiguration, final ConfigBase settingsXml,
        final boolean readInport) throws InvalidSettingsException {
        return getPortDescriptionsFromSettingsXml(settingsXml, readInport);
    }

    @Override
    public Optional<String> getIcon(final ConfigBase settingsXml) throws InvalidSettingsException {
        if (settingsXml.containsKey("metadata")) {
            final ConfigBase metadata = settingsXml.getConfigBase("metadata");
            final String icon = metadata.getString("icon", "");
            if (!icon.isEmpty()) {
                return Optional.of(icon);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getComponentType(final ConfigBase settingsXml) throws InvalidSettingsException {
        if (settingsXml.containsKey("metadata")) {
            final ConfigBase metadata = settingsXml.getConfigBase("metadata");
            final String icon = metadata.getString("type", "");
            if (!icon.isEmpty()) {
                return Optional.of(icon);
            }
        }
        return Optional.empty();
    }
}
