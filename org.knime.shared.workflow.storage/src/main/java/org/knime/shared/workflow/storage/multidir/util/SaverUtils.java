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
 *   8 Apr 2022 (jasper): created
 */
package org.knime.shared.workflow.storage.multidir.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.knime.core.node.config.base.AbstractConfigEntry;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigBaseWO;
import org.knime.core.node.config.base.ConfigBooleanEntry;
import org.knime.core.node.config.base.ConfigByteEntry;
import org.knime.core.node.config.base.ConfigCharEntry;
import org.knime.core.node.config.base.ConfigDoubleEntry;
import org.knime.core.node.config.base.ConfigFloatEntry;
import org.knime.core.node.config.base.ConfigIntEntry;
import org.knime.core.node.config.base.ConfigLongEntry;
import org.knime.core.node.config.base.ConfigShortEntry;
import org.knime.core.node.config.base.ConfigStringEntry;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.AnnotationDataDef;
import org.knime.shared.workflow.def.BaseNodeDef;
import org.knime.shared.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.shared.workflow.def.BoundsDef;
import org.knime.shared.workflow.def.ComponentNodeDef;
import org.knime.shared.workflow.def.ConfigDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.ConfigValueArrayDef;
import org.knime.shared.workflow.def.ConfigValueBooleanDef;
import org.knime.shared.workflow.def.ConfigValueByteDef;
import org.knime.shared.workflow.def.ConfigValueCharDef;
import org.knime.shared.workflow.def.ConfigValueDef;
import org.knime.shared.workflow.def.ConfigValueDoubleDef;
import org.knime.shared.workflow.def.ConfigValueFloatDef;
import org.knime.shared.workflow.def.ConfigValueIntDef;
import org.knime.shared.workflow.def.ConfigValueLongDef;
import org.knime.shared.workflow.def.ConfigValueShortDef;
import org.knime.shared.workflow.def.ConfigValueStringDef;
import org.knime.shared.workflow.def.ConnectionUISettingsDef;
import org.knime.shared.workflow.def.CreatorDef;
import org.knime.shared.workflow.def.CredentialPlaceholderDef;
import org.knime.shared.workflow.def.FlowVariableDef;
import org.knime.shared.workflow.def.MetaNodeDef;
import org.knime.shared.workflow.def.NativeNodeDef;
import org.knime.shared.workflow.def.NodeAnnotationDef;
import org.knime.shared.workflow.def.PortDef;
import org.knime.shared.workflow.def.TemplateLinkDef;
import org.knime.shared.workflow.def.TemplateMetadataDef;

/**
 * Utility class for the workflow saver
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
public final class SaverUtils {

    public static final String DEFAULT_WORKFLOW_NAME = "Workflow";

    public static final int DEFAULT_DEF_FONT_SIZE = 11;

    public static final int DEFAULT_FONT_SIZE = 11;

    private SaverUtils() {
    }

    /**
     * Maps a node type to the appropriate value for the config key "is_node_meta"
     */
    public static final Map<NodeTypeEnum, Boolean> isNodeTypeMeta = Map.of( //NOSONAR
        NodeTypeEnum.NATIVENODE, false, //
        NodeTypeEnum.COMPONENT, true, //
        NodeTypeEnum.METANODE, true //
    );

    /**
     * Maps a node type to the appropriate String for the config key "node_type"
     */
    public static final Map<NodeTypeEnum, String> nodeTypeString = Map.of( //
        NodeTypeEnum.NATIVENODE, "NativeNode", //
        NodeTypeEnum.COMPONENT, "SubNode", //
        NodeTypeEnum.METANODE, "MetaNode" //
    );

    /**
     * Invokes {@link ConfigBase#addEntry} only if the entry is not null
     *
     * @param settings The ConfigBase to which the entry might be added
     * @param config The entry to add
     * @param key The key under which the new entry should be inserted
     */
    public static void addEntryIfNotNull(final ConfigBase settings, final ConfigDef config, final String key) {
        if (config != null) {
            settings.addEntry(toConfigEntry(config, key));
        }
    }

    /**
     * Adds creator info to a given {@link ConfigBase}, if not null
     *
     * @param settings
     * @param creator
     */
    public static void addCreatorInfo(final ConfigBaseWO settings, final CreatorDef creator) {
        if (creator != null) {
            creator.getSavedWithVersion()
                .ifPresent(swv -> settings.addString(IOConst.WORKFLOW_HEADER_CREATED_BY_KEY.get(), swv));
            creator.isNightly().ifPresent(
                isNightly -> settings.addBoolean(IOConst.WORKFLOW_HEADER_CREATED_BY_NIGHTLY_KEY.get(), isNightly));
            settings.addString(IOConst.WORKFLOW_HEADER_VERSION_KEY.get(), LoadVersion.latest().getVersionString());
        }
    }

    /**
     * Add UI Information to a given {@link ConfigBase}
     *
     * @param settings The configBase in which to add the UI Info
     * @param bounds location and size
     */
    public static void addUiInfo(final ConfigBase settings, final BoundsDef bounds) {
        settings.addString(IOConst.UI_CLASSNAME_KEY.get(), IOConst.NODE_UI_INFORMATION_CLASSNAME.get());
        ConfigBase nodeUIConfig = new SimpleConfig(IOConst.UI_SETTINGS_KEY.get());
        var boundsArray = new int[4];
        boundsArray[0] = (bounds.getLocation() == null) ? 0 : bounds.getLocation().getX();
        boundsArray[1] = (bounds.getLocation() == null) ? 0 : bounds.getLocation().getY();
        boundsArray[2] = (bounds.getHeight() == null) ? 0 : bounds.getHeight();
        boundsArray[3] = (bounds.getWidth() == null) ? 0 : bounds.getWidth();
        nodeUIConfig.addIntArray(IOConst.EXTRA_NODE_INFO_BOUNDS_KEY.get(), boundsArray);
        settings.addEntry(nodeUIConfig);
    }

    /**
     * Add Connection UI Settings to a given {@link ConfigBase}
     *
     * @param settings The ConfigBase in which to add the UI info
     * @param uiSettings The definition of the UI settings
     */
    public static void addConnectionUISettings(final ConfigBase settings, final ConnectionUISettingsDef uiSettings) {
        if (uiSettings != null) {
            settings.addString(IOConst.UI_CLASSNAME_KEY.get(), IOConst.CONNECTION_UI_INFORMATION_CLASSNAME.get());

            var connectionUIConfig = new SimpleConfig(IOConst.UI_SETTINGS_KEY.get());
            var bendpoints = uiSettings.getBendPoints();
            connectionUIConfig.addInt(IOConst.EXTRA_INFO_CONNECTION_BENDPOINTS_SIZE_KEY.get(), bendpoints.size());
            bendpoints.forEach(coordinate -> connectionUIConfig.addIntArray(
                IOConst.EXTRA_INFO_CONNECTION_BENDPOINTS_KEY.get() + "_" + (connectionUIConfig.getChildCount() + 1),
                coordinate.getX(), coordinate.getY()));
            settings.addEntry(connectionUIConfig);
        }
    }

    /**
     * Adds the port definition to a given array of ports
     *
     * @param ports The parent port array
     * @param prefix The prefix of the key of a port
     * @param port The port description
     */
    public static void addPort(final ConfigBase ports, final String prefix, final PortDef port) {
        var portSettings = new SimpleConfig(prefix + port.getIndex());
        portSettings.addInt(IOConst.PORT_INDEX_KEY.get(), port.getIndex());
        port.getName().ifPresent(name -> portSettings.addString(IOConst.PORT_NAME_KEY.get(), name));

        var portType = new SimpleConfig(IOConst.PORT_TYPE_KEY.get());
        portType.addString(IOConst.PORT_OBJECT_CLASS_KEY.get(), port.getPortType().getPortObjectClass());
        portSettings.addEntry(portType);

        ports.addEntry(portSettings);
    }

    /**
     * Add a nodeAnnotation to a configBase
     *
     * @param settings The {@code ConfigBase} that the entry will be added to
     * @param annotation The annotation description
     *
     */
    public static void addAnnotationData(final ConfigBase settings, final NodeAnnotationDef annotation) {
        var annotationData = annotation.getData();
        if (annotationData.isEmpty()) {
            return;
        }
        if (!annotation.isAnnotationDefault()) {
            settings.addEntry(annotationToConfig(annotationData.get(), IOConst.NODE_ANNOTATION_KEY.get()));
        }
    }

    /**
     * Add templateInfo to a configBase. Does nothing if neither the link not the template metadata is present.
     *
     * @param settings the object to be modified
     * @param templateLink refers to a template. If set, templateMetadata must be empty.
     * @param templateMetadata a template. If set, templateLink must be empty (components/metanodes cannot be both)
     * @param nodeType
     */
    public static void addTemplateInfo(final ConfigBase settings, final Optional<TemplateLinkDef> templateLink,
        final Optional<TemplateMetadataDef> templateMetadata, final NodeTypeEnum nodeType) {

        // regular metanodes and components don't store a template information section in their workflow.knime
        if(templateLink.isEmpty() && templateMetadata.isEmpty()) {
            return;
        }

        var templateSettings = new SimpleConfig(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());

        // always store a role
        var role = templateMetadata.isPresent() ? IOConst.WORKFLOW_TEMPLATE_ROLE_TEMPLATE.get()
            : IOConst.WORKFLOW_TEMPLATE_ROLE_LINK.get();
        templateSettings.addString(IOConst.WORKFLOW_TEMPLATE_ROLE_KEY.get(), role);

        // always store a version
        var version = templateLink.map(TemplateLinkDef::getVersion).orElse(templateMetadata.get().getVersion()); // NOSONAR
        templateSettings.addString(IOConst.TIMESTAMP.get(), version.format(LoaderUtils.DATE_FORMAT));

        // for links, store the source URI
        templateSettings.addString(IOConst.SOURCE_URI_KEY.get(),
            templateLink.map(TemplateLinkDef::getUri).orElse(null));

        // for templates, store the type of template (metanode or component)
        templateMetadata.ifPresent(
            m -> templateSettings.addString(IOConst.WORKFLOW_TEMPLATE_TYPE_KEY.get(), nodeTypeString.get(nodeType)));

        settings.addEntry(templateSettings);
    }

    /**
     * Add credential placeholders to a configBase
     *
     * @param settings
     * @param credentials
     */
    public static void addCredentials(final ConfigBase settings,
        final Iterable<? extends CredentialPlaceholderDef> credentials) {
        ConfigBase credentialSettings = new SimpleConfig(IOConst.CREDENTIAL_PLACEHOLDERS_KEY.get());
        credentials.forEach(c -> {
            var credential = new SimpleConfig(c.getName());
            credential.addString(IOConst.CREDENTIAL_PLACEHOLDER_NAME_KEY.get(), c.getName());
            credential.addString(IOConst.CREDENTIAL_PLACEHOLDER_LOGIN_KEY.get(), c.getLogin());
            credentialSettings.addEntry(credential);
        });
        settings.addEntry(credentialSettings);
    }

    /**
     * Add global flow variables to a configBase
     *
     * @param settings
     * @param variables
     */
    public static void addFlowVariables(final ConfigBase settings,
        final Collection<? extends FlowVariableDef> variables) {
        if (!variables.isEmpty()) {
            ConfigBase variablesSettings = new SimpleConfig(IOConst.WORKFLOW_VARIABLES_KEY.get());
            variables.forEach(v -> {
                var index = variablesSettings.getChildCount();
                var variable = new SimpleConfig(IOConst.WORKFLOW_VARIABLE_PREFIX.get() + index);
                variable.addString(IOConst.FLOW_VARIABLE_NAME_KEY.get(), v.getName());
                variable.addString(IOConst.FLOW_VARIABLE_CLASS_KEY.get(), v.getPropertyClass());
                variable.addEntry(toConfigEntry(v.getValue(), IOConst.FLOW_VARIABLE_VALUE_KEY.get()));
                variablesSettings.addEntry(variable);
            });
            settings.addEntry(variablesSettings);
        }
    }

    /**
     * Helper function that recursively converts a {@link ConfigDef}-object to a {@link ConfigBase} that can be written
     * to an XML file. **Inspired** by DefToCoreUtil#toNodeSettings.
     *
     * @param configDef The def object containing configuration key-value pairs.
     * @param key The root key for the to-be-created config.
     * @return A new ConfigBase instance filled with items from the input def.
     */
    public static AbstractConfigEntry toConfigEntry(final ConfigDef configDef, final String key) {
        if (configDef instanceof ConfigValueDef) {
            return toConfigValue((ConfigValueDef)configDef, key);
        }
        if (configDef instanceof ConfigValueArrayDef) {
            return LoaderUtils.toSettingsArray((ConfigValueArrayDef)configDef, key, SimpleConfig::new);
        }
        if (configDef instanceof ConfigMapDef) {
            var configBase = new SimpleConfig(key);
            var configMapDef = (ConfigMapDef)configDef;
            configMapDef.getChildren().forEach((k, v) -> {
                if (v instanceof ConfigValueDef) {
                    configBase.addEntry(toConfigValue((ConfigValueDef)v, k));
                } else {
                    var child = toConfigEntry(v, k);
                    configBase.addEntry(child);
                }
            });
            return configBase;
        } else {
            throw new IllegalStateException(
                String.format("Could not parse ConfigDef (is %snull)", (configDef == null) ? " " : "not "));
        }
    }

    @SuppressWarnings({"squid:S1541", "squid:S1142"})
    private static AbstractConfigEntry toConfigValue(final ConfigValueDef valueDef, final String key) {
        if (valueDef instanceof ConfigValueBooleanDef) {
            boolean value = ((ConfigValueBooleanDef)valueDef).isValue();
            return new ConfigBooleanEntry(key, value);
        }
        if (valueDef instanceof ConfigValueCharDef) {
            char value = (char)((ConfigValueCharDef)valueDef).getValue().intValue();
            return new ConfigCharEntry(key, value);
        }
        if (valueDef instanceof ConfigValueDoubleDef) {
            double value = ((ConfigValueDoubleDef)valueDef).getValue();
            return new ConfigDoubleEntry(key, value);
        }
        if (valueDef instanceof ConfigValueFloatDef) {
            float value = ((ConfigValueFloatDef)valueDef).getValue();
            return new ConfigFloatEntry(key, value);
        }
        if (valueDef instanceof ConfigValueIntDef) {
            int value = ((ConfigValueIntDef)valueDef).getValue();
            return new ConfigIntEntry(key, value);
        }
        if (valueDef instanceof ConfigValueLongDef) {
            long value = ((ConfigValueLongDef)valueDef).getValue();
            return new ConfigLongEntry(key, value);
        }
        if (valueDef instanceof ConfigValueByteDef) {
            byte value = (byte)((ConfigValueByteDef)valueDef).getValue().intValue();
            return new ConfigByteEntry(key, value);
        }
        if (valueDef instanceof ConfigValueShortDef) {
            short value = (short)((ConfigValueShortDef)valueDef).getValue().intValue();
            return new ConfigShortEntry(key, value);
        }
        if (valueDef instanceof ConfigValueStringDef) {
            String value = ((ConfigValueStringDef)valueDef).getValue().orElse(null);
            return new ConfigStringEntry(key, value);
        }
        // TODO password need passphrase to decode //NOSONAR
        throw new IllegalStateException(
            String.format("Could not parse ConfigValueDef (is %snull)", (valueDef == null) ? " " : "not "));
    }

    /**
     * Converter from an {@link AnnotationDataDef} object to a config entry
     *
     * @param annotationDataDef The to-be-converted annotation Data
     * @param key The key of the resulting Config entry
     * @return A config encoding the annotation data
     */
    public static ConfigBase annotationToConfig(final AnnotationDataDef annotationDataDef, final String key) {
        var annotation = new SimpleConfig(key);
        annotation.addString("text", annotationDataDef.getText());
        annotation.addInt("bgcolor", annotationDataDef.getBgcolor());
        annotation.addInt("x-coordinate", annotationDataDef.getLocation().getX());
        annotation.addInt("y-coordinate", annotationDataDef.getLocation().getY());
        annotation.addInt("width", annotationDataDef.getWidth());
        annotation.addInt("height", annotationDataDef.getHeight());
        annotation.addString("alignment", annotationDataDef.getTextAlignment());
        annotation.addInt("borderSize", annotationDataDef.getBorderSize());
        annotation.addInt("borderColor", annotationDataDef.getBorderColor());
        annotation.addInt("defFontSize", annotationDataDef.getDefaultFontSize().orElse(DEFAULT_DEF_FONT_SIZE));
        annotation.addInt("annotation-version", annotationDataDef.getAnnotationVersion());

        var stylesList = annotationDataDef.getStyles().orElse(List.of());
        var styles = new SimpleConfig("styles");
        var styleIndex = 0;
        for (var styleRangeDef : stylesList) {
            var style = new SimpleConfig(String.format("style_%d", styleIndex));
            style.addInt("start", styleRangeDef.getStart());
            style.addInt("length", styleRangeDef.getLength());
            styleRangeDef.getFontName().ifPresent(f -> style.addString("fontname", f));
            styleRangeDef.getFontStyle().ifPresent(f -> style.addInt("fontstyle", f));
            style.addInt("fontsize", styleRangeDef.getFontSize().orElse(DEFAULT_FONT_SIZE));
            styleRangeDef.getColor().ifPresent(c -> style.addInt("fgcolor", c));
            styles.addEntry(style);
            ++styleIndex;
        }
        annotation.addEntry(styles);
        return annotation;
    }

    /**
     * Methods that creates a directory for a {@code NativeNode}, {@code MetaNode} or {@code Component} within a parent
     * directory with the name (e.g.) {@code parentDir/Node Name (#123)/}
     *
     * @param parent The parent directory (usually that of a workflow)
     * @param node The node for which the directory will be created (relevant for node name and id)
     * @return The newly created node directory
     * @throws FileNotFoundException If the parent directory doesn't exist or cannot be written to
     */
    public static File createNodeDir(final File parent, final BaseNodeDef node) throws FileNotFoundException {
        if (!(parent.exists() && parent.isDirectory() && parent.canWrite())) {
            throw new FileNotFoundException(
                "The directory " + parent.getAbsolutePath() + " does not exist or cannot be written to.");
        }
        String safeNodeName = null;
        switch (node.getNodeType()) {
            case COMPONENT:
                safeNodeName = SaverUtils.getValidFileName(((ComponentNodeDef)node).getWorkflow().getName().orElse(DEFAULT_WORKFLOW_NAME), 12);
                break;
            case METANODE:
                safeNodeName = SaverUtils.getValidFileName(((MetaNodeDef)node).getWorkflow().getName().orElse(DEFAULT_WORKFLOW_NAME), 12);
                break;
            case NATIVENODE:
                safeNodeName = SaverUtils.getValidFileName(((NativeNodeDef)node).getNodeName(), -1);
                break;
        }
        var dirName = String.format("%s (#%d)", safeNodeName, node.getId());
        var directory = new File(parent, dirName);
        directory.mkdir();
        return directory;
    }

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win");

    private static final Pattern FORBIDDEN_WINDOWS_NAMES =
        Pattern.compile("^(?:(?:CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\.[^.]*)?|[ \\.])$");

    private static final Pattern ILLEGAL_PATH_CHARACTERS = Pattern.compile("[^a-zA-Z0-9 ]");//NOSONAR

    /**
     * For some suggested name the returned string can be used to create a file. All unsupported characters are replaced
     * by '_'. Used when a workflow is saved to derive the folder name for a node. The returned string may change
     * between version (as we allow more special characters).
     *
     * @param strWithWeirdChars Some string (not null, length &gt; 0)
     * @param maxLength If name should be truncated, specify some value &gt; 0 (&lt;= 0 means no truncation)
     * @return the name
     * @since 2.8
     */
    public static String getValidFileName(final String strWithWeirdChars, final int maxLength) {
        var m = ILLEGAL_PATH_CHARACTERS.matcher(strWithWeirdChars);
        String result = m.replaceAll("_");
        if ((maxLength > 0) && (result.length() > maxLength)) {
            result = result.substring(0, maxLength).trim();
        }

        if (IS_WINDOWS) {
            m = FORBIDDEN_WINDOWS_NAMES.matcher(result);
            if (m.matches()) {
                result = "_" + result.substring(1);
            }
        }
        return result;
    }

}
