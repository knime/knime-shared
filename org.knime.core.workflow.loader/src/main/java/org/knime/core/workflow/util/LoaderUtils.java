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
 *   9 Mar 2022 (Dionysios Stolis): created
 */
package org.knime.core.workflow.util;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.AbstractConfigEntry;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigBaseRO;
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
import org.knime.core.workflow.def.AnnotationDataDef;
import org.knime.core.workflow.def.ConfigDef;
import org.knime.core.workflow.def.ConfigMapDef;
import org.knime.core.workflow.def.ConfigValueBooleanArrayDef;
import org.knime.core.workflow.def.CoordinateDef;
import org.knime.core.workflow.def.StyleRangeDef;
import org.knime.core.workflow.def.TemplateInfoDef;
import org.knime.core.workflow.def.impl.AnnotationDataDefBuilder;
import org.knime.core.workflow.def.impl.ConfigMapDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueBooleanArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueBooleanDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueByteArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueByteDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueCharArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueCharDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueDoubleArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueDoubleDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueFloatArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueFloatDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueIntArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueIntDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueLongArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueLongDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueShortArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueShortDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueStringArrayDefBuilder;
import org.knime.core.workflow.def.impl.ConfigValueStringDefBuilder;
import org.knime.core.workflow.def.impl.CoordinateDefBuilder;
import org.knime.core.workflow.def.impl.StyleRangeDefBuilder;
import org.knime.core.workflow.def.impl.TemplateInfoDefBuilder;
import org.knime.core.workflow.loader.ComponentLoader;
import org.knime.core.workflow.loader.MetaNodeLoader;
import org.knime.core.workflow.loader.NativeNodeLoader;
import org.knime.core.workflow.loader.WorkflowLoader;

/**
 * //TODO We can add all the read from file methods for the files workflow.knime, settings.xml, template.knime.
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
public final class LoaderUtils {

    private LoaderUtils() {
    }

    public enum Const {
            /** @see NativeNodeLoader#load */
            NODE_NAME_KEY("node-name"),
            /** @see NativeNodeLoader#loadBundle */
            NODE_BUNDLE_NAME_KEY("node-bundle-name"), //
            NODE_BUNDLE_SYMBOLIC_NAME_KEY("node-bundle-symbolic-name"), //
            NODE_BUNDLE_VENDOR_KEY("node-bundle-vendor"), //
            NODE_BUNDLE_VERSION_KEY("node-bundle-version"),
            /** @see NativeNodeLoader#loadFeature */
            NODE_FEATURE_NAME_KEY("node-feature-name"), //
            NODE_FEATURE_SYMBOLIC_NAME_KEY("node-feature-symbolic-name"), //
            NODE_FEATURE_VENDOR_KEY("node-feature-vendor"), //
            NODE_FEATURE_VERSION_KEY("node-feature-version"),
            /** @see NativeNodeLoader#loadCreationConfig */
            NODE_CREATION_CONFIG_KEY("node_creation_config"),
            /** @see NativeNodeLoader#loadFactory */
            FACTORY_KEY("factory"),
            /** @see NativeNodeLoader#loadFactorySettings */
            FACTORY_SETTINGS_KEY("factory_settings"),
            /** @see NativeNodeLoader#loadFilestore */
            FILESTORES_KEY("filestores"), //
            FILESTORES_LOCATION_KEY("file_store_location"), FILESTORES_ID_KEY("file_store_id"),

            /** @see MetaNodeLoader#setInPorts */
            META_IN_PORTS_KEY("meta_in_ports"),
            /** @see MetaNodeLoader#setOutPorts */
            META_OUT_PORTS_KEY("meta_out_ports"),
            /** @see MetaNodeLoader#loadPortsSettingsEnum */
            PORT_ENUM_KEY("port_enum"),
            /** @see MetaNodeLoader#loadNodeUIInformation */
            UI_SETTINGS_KEY("ui_settings"),

            /** @see MetaNodeLoader#loadPort */
            /** @see ComponentLoader#loadPort */
            PORT_INDEX_KEY("index"), //
            PORT_NAME_KEY("name"), //
            PORT_TYPE_KEY("type"), //
            PORT_OBJECT_CLASS_KEY("object_class"),

            /** @see ComponentLoader#loadMetadata */
            DESCRIPTION_KEY("description"), //
            METADATA_KEY("metadata"), //
            METADATA_NAME_KEY("name"), //
            INPORTS_KEY("inports"), //
            OUTPORTS_KEY("outports"),
            /** @see ComponentLoader#loadDialogSettings */
            LAYOUT_JSON_KEY("layoutJSON"),
            CONFIGURATION_LAYOUT_JSON_KEY("configurationLayoutJSON"),
            CUSTOM_CSS_KEY("customCSS"),
            HIDE_IN_WIZARD_KEY("hideInWizard"),

            /** @see LoaderUtils#loadTemplateLink */
            WORKFLOW_TEMPLATE_INFORMATION_KEY("workflow_template_information"), //
            SOURCE_URI_KEY("sourceURI"), //
            TIMESTAMP("timestamp"),
            /** @see ComponentLoader#loadVirtualInNodeId */
            VIRTUAL_IN_ID_KEY("virtual-in-ID"),
            /** @see ComponentLoader#loadVirtualInNodeId */
            VIRTUAL_OUT_ID_KEY("virtual-out-ID"),
            /** @see ComponentLoader#loadIcon */
            ICON_KEY("icon"),

            /** @see ConfigurableNodeLoader#loadInternalNodeSubSettings */
            INTERNAL_NODE_SUBSETTINGS("internal_node_subsettings"),
            /** @see ConfigurableNodeLoader#loadVariableSettings */
            VARIABLES_KEY("variables"),
            /** @see ConfigurableNodeLoader#loadModelSettings */
            MODEL_KEY("model"),
            /** @see ConfigurableNodeLoader#loadFlowStackObjects */
            SCOPE_STACK_KEY("scope_stack"), FLOW_STACK_KEY("flow_stack"),
            /** @see ConfigurableNodeLoader#loadFlowObjectDef */
            TYPE_KEY("type"), VARIABLE("variable"),
            /** @see ConfigurableNodeLoader#loadFlowContextDef */
            INACTIVE("_INACTIVE"),
            /** @see ConfigurableNodeLoader#loadFlowContextType */
            LOOP("LOOP"), FLOW("FLOW"), SCOPE("SCOPE"),

            /** @see BaseNodeLoader#loadNodeId */
            ID_KEY("id"),
            /** @see BaseNodeLoader#loadAnnotation */
            CUSTOM_NAME_KEY("customName"), //
            NODE_ANNOTATION_KEY("nodeAnnotation"),
            /** @see BaseNodeLoader#loadJobManager */
            JOB_MANAGER_KEY("job.manager"), //
            JOB_MANAGER_FACTORY_ID_KEY("job.manager.factory.id"), //
            JOB_MANAGER_SETTINGS_KEY("job.manager.settings"),
            /** @see NodeLoader#loadLocks */
            IS_DELETABLE_KEY("isDeletable"), //
            HAS_RESET_LOCK_KEY("hasResetLock"), //
            HAS_CONFIGURE_LOCK_KEY("hasConfigureLock"),
            /** @see NodeLoader#loadCustomDescription */
            CUSTOM_DESCRIPTION_KEY("customDescription"),
            /** @see NodeLoader#loadBoundsDef */
            EXTRA_NODE_INFO_BOUNDS_KEY("extrainfo.node.bounds"),

            /** @see LoaderUtils#readWorkflowConfigFromFile */
            WORKFLOW_FILE_NAME("workflow.knime"),
            /** @see LoaderUtils#loadNodeFile */
            NODE_SETTINGS_FILE("node_settings_file"),
            /** @see LoaderUtils#readNodeConfigFromFile */
            NODE_SETTINGS_FILE_NAME("settings.xml"),
            /** @see LoaderUtils#readTemplateConfigFromFile */
            TEMPLATE_FILE_NAME("template.knime"),

            /** @see StandAloneLoader#loadTableBackendSettings */
            TABLE_BACKEND_KEY("tableBackend"),
            /** @see StandAloneLoader#setWorkflowVariables */
            WORKFLOW_VARIABLES_KEY("workflow_variables"),
            /** @see StandAloneLoader#loadFlowVariable */
            FLOW_VARIABLE_NAME_KEY("name"),
            FLOW_VARIABLE_VALUE_KEY("value"),
            FLOW_VARIABLE_CLASS_KEY("class"),

            /** @see StandAloneLoader#setCredentialPlaceholders */
            CREDENTIAL_PLACEHOLDERS_KEY("workflow_credentials"),
            CREDENTIAL_PLACEHOLDER_NAME_KEY("name"), //
            CREDENTIAL_PLACEHOLDER_LOGIN_KEY("login"), //

            /** @see WorkflowLoader#setConnections */
            WORKFLOW_CONNECTIONS_KEY("connections"),
            /** @see WorkflowLoader#setNodes */
            WORKFLOW_NODES_KEY("nodes"),
            /** @see WorkflowLoader#loadAuthorInformation */
            AUTHOR_INFORMATION_KEY("authorInformation"), AUTHORED_BY_KEY("authored-by"),
            AUTHORED_WHEN_KEY("authored-when"), LAST_EDITED_BY_KEY("lastEdited-by"),
            LAST_EDITED_WHEN_KEY("lastEdited-when"),
            /** @see WorkflowLoader#setAnnotations */
            WORKFLOW_ANNOTATIONS_KEY("annotations"),
            /** @see WorkflowLoader#loadWorkflowUISettings @since 2.6 */
            WORKFLOW_EDITOR_SETTINGS_KEY("workflow_editor_settings"), //
            WORKFLOW_EDITOR_SNAPTOGRID_KEY("workflow.editor.snapToGrid"), //
            WORKFLOW_EDITOR_SHOWGRID_KEY("workflow.editor.ShowGrid"), //
            WORKFLOW_EDITOR_GRID_X_KEY("workflow.editor.gridX"), //
            WORKFLOW_EDITOR_GRID_Y_KEY("workflow.editor.gridY"), //
            WORKFLOW_EDITOR_ZOOM_LEVEL_KEY("workflow.editor.zoomLevel"), //
            UI_CLASSNAME_KEY("ui_classname"), //
            WORKFLOW_EDITOR_CONNECTION_WIDTH_KEY("workflow.editor.connectionWidth"), //
            WORKFLOW_EDITOR_CURVED_CONNECTIONS_KEY("workflow.editor.curvedConnections"),
            /** @see WorkflowLoader#loadConnectionUISettings */
            EXTRA_INFO_CLASS_NAME_KEY("extraInfoClassName"),
            /** @see WorkflowLoader#loadConnectionUISettings */
            EXTRA_INFO_CONNECTION_BENDPOINTS_KEY("extrainfo.conn.bendpoints");

        /**
         * @param string
         */
        Const(final String string) {
            m_key = string;
        }

        /**
         * @return the key
         */
        public String get() {
            return m_key;
        }

        final String m_key;
    }

    public static final int DEFAULT_NEGATIVE_INDEX = -1;

    public static final String DEFAULT_EMPTY_STRING = "";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    public static final ConfigMapDef DEFAULT_CONFIG_MAP = new ConfigMapDefBuilder().build();

    public static final TemplateInfoDef DEFAULT_TEMPLATE_LINK = new TemplateInfoDefBuilder().setUri("TEST").build();

    public static OffsetDateTime parseDate(final String s) {
        synchronized (DATE_FORMAT) {
            return OffsetDateTime.parse(s, DATE_FORMAT);
        }
    }

    public static ConfigBaseRO readNodeConfigFromFile(final File directory) throws IOException {
        var nodeSettingsFile = new File(directory, Const.NODE_SETTINGS_FILE_NAME.get());
        try {
            return SimpleConfig.parseConfig(nodeSettingsFile.getAbsolutePath(), nodeSettingsFile);
        } catch (IOException e) {
            throw new IOException("Cannot load the " + Const.NODE_SETTINGS_FILE_NAME.get(), e);
        }
    }

    public static ConfigBaseRO readWorkflowConfigFromFile(final File directory) throws IOException {
        var workflowSettingsFile = new File(directory, Const.WORKFLOW_FILE_NAME.get());
        try {
            return SimpleConfig.parseConfig(workflowSettingsFile.getAbsolutePath(), workflowSettingsFile);
        } catch (IOException e) {
            throw new IOException("Cannot load the " + Const.WORKFLOW_FILE_NAME.get(), e);
        }
    }

    public static Optional<ConfigBaseRO> readTemplateConfigFromFile(final File directory) throws IOException {
        var templateSettingsFile = new File(directory, Const.TEMPLATE_FILE_NAME.get());
        if (templateSettingsFile.exists()) {
            try {
                return Optional
                    .of(SimpleConfig.parseConfig(templateSettingsFile.getAbsolutePath(), templateSettingsFile));
            } catch (IOException e) {
                throw new IOException("Cannot load the " + Const.TEMPLATE_FILE_NAME.get(), e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Also validates the directory argument.
     *
     * @param workflowDirectory from which to load the workflow.
     * @return the workflow.knime file in the given directory.
     */
    static File getWorkflowDotKnimeFile(final File workflowDirectory) throws IOException {
        if (workflowDirectory == null) {
            throw new IllegalArgumentException("Directory must not be null.");
        }
        if (!workflowDirectory.isDirectory()) {
            throw new IOException("Not a directory: " + workflowDirectory);
        }
        if (!workflowDirectory.canRead()) {
            throw new IOException("Cannot read from directory: " + workflowDirectory);
        }

        // template.knime or workflow.knime
        return new File(workflowDirectory, Const.WORKFLOW_FILE_NAME.get());
//        var dotKNIMERef = new File(workflowDirectory, Const.WORKFLOW_FILE_NAME.get();
//        var dotKNIME = dotKNIMERef.getFile();
//
//        if (!dotKNIME.isFile()) {
//            throw new IOException(String.format("No %s file in directory %s", Const.WORKFLOW_FILE_NAME.get(),
//                workflowDirectory.getAbsolutePath()));
//        }
//        return dotKNIME;
    }

    /**
     * Parses the file (usually workflow.knime) that describes the workflow.
     *
     * @param workflowDirectory containing the workflow
     */
    public static ConfigBaseRO parseWorkflowConfig(final File workflowDirectory) throws IOException {
        var workflowDotKnime = LoaderUtils.getWorkflowDotKnimeFile(workflowDirectory);
        return SimpleConfig.parseConfig(workflowDotKnime.getAbsolutePath(), workflowDotKnime);
    }

    /**
     * The node settings file is typically named settings.xml (for native nodes and components) and workflow.knime for
     * meta nodes. However, the actual name is stored in the parent workflow's entry that describes the node.
     *
     * @param workflowNodeConfig the configuration tree in the workflow description (workflow.knime) that describes the
     *            node
     * @param workflowDir the directory that contains the node directory
     * @return
     * @throws InvalidSettingsException
     */
    public static File loadNodeFile(final ConfigBaseRO workflowNodeConfig, final File workflowDir)
        throws InvalidSettingsException {
        // relative path to node configuration file
        var fileString = workflowNodeConfig.getString(Const.NODE_SETTINGS_FILE.get());
        if (fileString == null) {
            throw new InvalidSettingsException(
                "Unable to read settings " + "file for node " + workflowNodeConfig.getKey());
        }
        // fileString is something like "File Reader(#1)/settings.xml", thus
        // it contains two levels of the hierarchy. We leave it here to the
        // java.io.File implementation to resolve these levels
        var nodeFile = new File(workflowDir, fileString);
        if (!nodeFile.isFile() || !nodeFile.canRead()) {
            throw new InvalidSettingsException("Unable to read settings " + "file " + nodeFile.getAbsolutePath());
        }
        return nodeFile;
    }

    /**
     * @param annotationConfig
     * @param workflowFormatVersion
     * @return a {@link AnnotationDataDef}
     * @throws InvalidSettingsException
     */
    public static AnnotationDataDef loadAnnotation(final ConfigBaseRO annotationConfig,
        final LoadVersion workflowFormatVersion) throws InvalidSettingsException {
        var builder = new AnnotationDataDefBuilder()//
            .setText(annotationConfig.getString("text"))//
            .setBgcolor(annotationConfig.getInt("bgcolor"))//
            .setLocation(loadCoordinate(annotationConfig.getInt("x-coordinate"),
                annotationConfig.getInt("y-coordinate")))//
            .setWidth(annotationConfig.getInt("width"))//
            .setHeight(annotationConfig.getInt("height"))//
            .setBorderSize(annotationConfig.getInt("borderSize", 0)) // default to 0 for backward compatibility
            .setBorderColor(annotationConfig.getInt("borderColor", 0)) // default for backward compatibility
            .setDefaultFontSize(annotationConfig.getInt("defFontSize", -1)) // default for backward compatibility
            .setAnnotationVersion(annotationConfig.getInt("annotation-version", -1)) // default to VERSION_OLD
            .setTextAlignment(workflowFormatVersion.ordinal() >= LoadVersion.V250.ordinal()
                ? annotationConfig.getString("alignment") : "LEFT");

        ConfigBaseRO styleConfigs = annotationConfig.getConfigBase("styles");
        for (String key : styleConfigs.keySet()) {
            builder.addToStyles(() -> loadStyleRangeDef(styleConfigs.getConfigBase(key)),
                new StyleRangeDefBuilder().build());
        }
        return builder.build();
    }

    public static CoordinateDef loadCoordinate(final int x, final int y) {
        return new CoordinateDefBuilder().setX(x).setY(y).build();
    }

    /**
     * @param styleConfig
     */
    private static StyleRangeDef loadStyleRangeDef(final ConfigBaseRO styleConfig) throws InvalidSettingsException {
        return new StyleRangeDefBuilder()//
            .setStart(styleConfig.getInt("start"))//
            .setLength(styleConfig.getInt("length"))//
            .setFontName(styleConfig.getString("fontname"))//
            .setFontStyle(styleConfig.getInt("fontstyle"))//
            .setFontSize(styleConfig.getInt("fontsize"))//
            .setColor(styleConfig.getInt("fgcolor"))//
            .build();
    }

    /**
     * Loads the template link of a Component or MetaNode from the {@code templateSettings}. The only usage of
     * template.knime is to read the template information for the stand alone MetaNodes (Template role). Components, and
     * MetaNodes as NativeNodes, have the template information in their settings.xml and workflow.knime accordingly.
     *
     * @param templateSettings a read only representation either of template.knime, settings.xml or workflow.knime.
     * @return a {@link TemplateLinkDef}.
     * @throws InvalidSettingsException
     */
    public static TemplateInfoDef loadTemplateLink(final ConfigBaseRO templateSettings) throws InvalidSettingsException {
        if (!templateSettings.containsKey(Const.WORKFLOW_TEMPLATE_INFORMATION_KEY.get())) {
            return DEFAULT_TEMPLATE_LINK;
        }

        var templateInformationSettings = templateSettings.getConfigBase(Const.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        return new TemplateInfoDefBuilder()
            .setUri(templateInformationSettings.getString(Const.SOURCE_URI_KEY.get(), DEFAULT_EMPTY_STRING)) //
            .setUpdatedAt(() -> parseDate(templateInformationSettings.getString(Const.TIMESTAMP.get())),
                OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)) //
            .build();
    }

    /**
    *
    * TODO use {@link NodeSettingsRO}. The problem is the typed leaf entries (e.g., {@link ConfigBooleanEntry} inherit
    * from {@link AbstractConfigEntry}, whereas {@link NodeSettingsRO}/ {@link ConfigRO}/ {@link ConfigBaseRO} only
    * start from {@link ConfigBase}.
    *
    * @param settings TODO read-only access to a ConfigBase
    * @return the node settings in a representation that can be converted to various formats
    * @throws InvalidSettingsException
    */
   public static ConfigMapDef toConfigMapDef(final ConfigBaseRO settings) throws InvalidSettingsException {

       if (settings == null) {
           return null;
       }

       // TODO don't cast
       ConfigBase config = (ConfigBase)settings;
       return (ConfigMapDef)toConfigDef(config, settings.getKey());
   }

   /**
    * Recursive function to create a node settings tree (comprising {@link AbstractConfigEntry}s) from a
    * {@link ConfigDef} tree.
    *
    * @param settings an entity containing the recursive node settings
    * @param key the name of this subtree
    * @throws InvalidSettingsException TODO what about {@link ModelContent}? It's a sibling of {@link NodeSettings}.
    */
   private static ConfigDef toConfigDef(final AbstractConfigEntry settings, final String key)
       throws InvalidSettingsException {

       if (settings instanceof ConfigBase) {
           // this is a subtree, because every class that extends AbstractConfigEntry and is not a subclass of
           // ConfigBase is a leaf class
           ConfigBase subTree = (ConfigBase) settings;

           final Map<String, ConfigDef> children = new LinkedHashMap<>();
           for (String childKey : subTree.keySet()) {
               // some subtrees are arrays in disguise, don't recurse into those
               ConfigDef asArrayDef = tryNodeSettingsAsArray(subTree, childKey);
               if(asArrayDef != null) {
                   children.put(childKey, asArrayDef);
               } else {
                   // recurse
                   ConfigDef subTreeDef = toConfigDef(subTree.getEntry(childKey), childKey);
                   children.put(childKey, subTreeDef);
               }
           }
           return new ConfigMapDefBuilder()//
                   .setKey(key)//
                   .setChildren(children)//
                   .setConfigType("ConfigMap")//
                   .build();
       } else {
           // recursion anchor
           return abstractConfigurationEntryToTypedLeaf(settings)//
                   .orElseThrow(() -> new IllegalStateException(settings.getKey() + settings.toStringValue()));
       }

   }

   /**
    * @param innerNode
    * @return null if no sensible conversion could be made, otherwise an array representation of the matching type,
    *         like {@link ConfigValueBooleanArrayDef}.
    */
   private static ConfigDef tryNodeSettingsAsArray(final ConfigBase innerNode, final String childKey) { // NOSONAR
       // NOSONAR: recommended number of return statements is <= 5 but we just have to cover all the types.

       boolean[] booleanValues = innerNode.getBooleanArray(childKey, null);
       if (booleanValues != null) {
           List<Boolean> asList = IntStream.range(0, booleanValues.length)//
               .mapToObj(idx -> booleanValues[idx])//
               .collect(Collectors.toList());
           return new ConfigValueBooleanArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueBooleanArray")//
               .build();
       }
       byte[] byteValues = innerNode.getByteArray(childKey, null);
       if (byteValues != null) {
           return new ConfigValueByteArrayDefBuilder()//
               .setArray(byteValues)//
               .setConfigType("ConfigValueByteArray")//
               .build();
       }
       char[] charValues = innerNode.getCharArray(childKey, null);
       if (charValues != null) {
           List<Integer> asList = IntStream.range(0, charValues.length)
               //
               .mapToObj(idx -> Integer.valueOf(charValues[//
               idx])).collect(Collectors.toList());
           return new ConfigValueCharArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueCharArray")//
               .build();
       }
       double[] doubleValues = innerNode.getDoubleArray(childKey, null);
       if (doubleValues != null) {
           List<Double> asList = IntStream.range(0, doubleValues.length)//
               .mapToObj(idx -> doubleValues[idx])//
               .collect(Collectors.toList());
           return new ConfigValueDoubleArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueDoubleArray")//
               .build();
       }
       float[] floatValues = innerNode.getFloatArray(childKey, null);
       if (floatValues != null) {
           List<Float> asList = IntStream.range(0, floatValues.length)//
               .mapToObj(idx -> floatValues[idx])//
               .collect(Collectors.toList());
           return new ConfigValueFloatArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueFloatArray")//
               .build();
       }
       int[] intValues = innerNode.getIntArray(childKey, null);
       if (intValues != null) {
           List<Integer> asList = IntStream.range(0, intValues.length)//
               .mapToObj(idx -> intValues[idx])//
               .collect(Collectors.toList());
           return new ConfigValueIntArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueIntArray")//
               .build();
       }
       long[] longValues = innerNode.getLongArray(childKey, null);
       if (longValues != null) {
           List<Long> asList = IntStream.range(0, longValues.length)//
               .mapToObj(idx -> longValues[idx])//
               .collect(Collectors.toList());
           return new ConfigValueLongArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueLongArray")//
               .build();
       }
       short[] shortValues = innerNode.getShortArray(childKey, null);
       if (shortValues != null) {
           List<Integer> asList = IntStream.range(0, shortValues.length)
               //
               .mapToObj((idx -> Integer.valueOf(shortValues[//
               idx]))).collect(Collectors.toList());
           return new ConfigValueShortArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueShortArray")//
               .build();
       }
       String[] stringValues = innerNode.getStringArray(childKey, (String[])null);
       if (stringValues != null) {
           List<String> asList = IntStream.range(0, stringValues.length)//
               .mapToObj(idx -> stringValues[idx])//
               .collect(Collectors.toList());
           return new ConfigValueStringArrayDefBuilder()//
               .setArray(asList)//
               .setConfigType("ConfigValueStringArray")//
               .build();
       }
       return null;
   }

   private static Optional<ConfigDef> abstractConfigurationEntryToTypedLeaf(final AbstractConfigEntry child) {
       // for children: check whether they are leafs by testing on all leaf types
       if (child instanceof ConfigBooleanEntry) {
           return Optional.of(new ConfigValueBooleanDefBuilder()//
           .setValue(((ConfigBooleanEntry)child).getBoolean())//
           .setConfigType("ConfigValueBoolean")//
           .build());
       } else if (child instanceof ConfigByteEntry) {
           return Optional.of(new ConfigValueByteDefBuilder()//
           .setValue((int)((ConfigByteEntry)child).getByte())//
           .setConfigType("ConfigValueByte")//
           .build());
       } else if (child instanceof ConfigCharEntry) {
           return Optional.of(new ConfigValueCharDefBuilder()//
           .setValue((int)((ConfigCharEntry)child).getChar())//
           .setConfigType("ConfigValueChar")//
           .build());
       } else if (child instanceof ConfigDoubleEntry) {
           return Optional.of(new ConfigValueDoubleDefBuilder()//
           .setValue(((ConfigDoubleEntry)child).getDouble())//
           .setConfigType("ConfigValueDouble")//
           .build());
       } else if (child instanceof ConfigFloatEntry) {
           return Optional.of(new ConfigValueFloatDefBuilder()//
           .setValue(((ConfigFloatEntry)child).getFloat())//
           .setConfigType("ConfigValueFloat")//
           .build());
       } else if (child instanceof ConfigIntEntry) {
           return Optional.of(new ConfigValueIntDefBuilder()//
           .setValue(((ConfigIntEntry)child).getInt())//
           .setConfigType("ConfigValueInt")//
           .build());
       } else if (child instanceof ConfigLongEntry) {
           return Optional.of(new ConfigValueLongDefBuilder()//
           .setValue(((ConfigLongEntry)child).getLong())//
           .setConfigType("ConfigValueLong")//
           .build());
       } /*else if (child instanceof ConfigPasswordEntry) {
           return Optional
               .of(DefaultConfigValuePasswordDef.builder()//
           //
           .setConfigType("ConfigValuePassword")//
           .build());
         } */ else if (child instanceof ConfigShortEntry) {
           return Optional.of(new ConfigValueShortDefBuilder()//
           .setValue((int)((ConfigShortEntry)child).getShort())//
           .setConfigType("ConfigValueShort")//
           .build());
       } else if (child instanceof ConfigStringEntry) {
           return Optional.of(new ConfigValueStringDefBuilder()//
           .setValue(((ConfigStringEntry)child).getString())//
           .setConfigType("ConfigValueString")//
           .build());
       }
       return Optional.empty();
   }
}
