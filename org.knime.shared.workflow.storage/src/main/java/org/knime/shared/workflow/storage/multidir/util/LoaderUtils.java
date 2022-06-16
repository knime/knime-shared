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
package org.knime.shared.workflow.storage.multidir.util;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.AbstractConfigEntry;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.ConfigBaseRO;
import org.knime.core.node.config.base.ConfigBooleanEntry;
import org.knime.core.node.config.base.ConfigByteEntry;
import org.knime.core.node.config.base.ConfigCharEntry;
import org.knime.core.node.config.base.ConfigDoubleEntry;
import org.knime.core.node.config.base.ConfigEntries;
import org.knime.core.node.config.base.ConfigFloatEntry;
import org.knime.core.node.config.base.ConfigIntEntry;
import org.knime.core.node.config.base.ConfigLongEntry;
import org.knime.core.node.config.base.ConfigPasswordEntry;
import org.knime.core.node.config.base.ConfigShortEntry;
import org.knime.core.node.config.base.ConfigStringEntry;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.util.LoadVersion;
import org.knime.shared.workflow.def.AnnotationDataDef;
import org.knime.shared.workflow.def.ConfigDef;
import org.knime.shared.workflow.def.ConfigMapDef;
import org.knime.shared.workflow.def.ConfigValueArrayDef;
import org.knime.shared.workflow.def.ConfigValueBooleanArrayDef;
import org.knime.shared.workflow.def.ConfigValueBooleanDef;
import org.knime.shared.workflow.def.ConfigValueByteArrayDef;
import org.knime.shared.workflow.def.ConfigValueByteDef;
import org.knime.shared.workflow.def.ConfigValueCharArrayDef;
import org.knime.shared.workflow.def.ConfigValueCharDef;
import org.knime.shared.workflow.def.ConfigValueDef;
import org.knime.shared.workflow.def.ConfigValueDoubleArrayDef;
import org.knime.shared.workflow.def.ConfigValueDoubleDef;
import org.knime.shared.workflow.def.ConfigValueFloatArrayDef;
import org.knime.shared.workflow.def.ConfigValueFloatDef;
import org.knime.shared.workflow.def.ConfigValueIntArrayDef;
import org.knime.shared.workflow.def.ConfigValueIntDef;
import org.knime.shared.workflow.def.ConfigValueLongArrayDef;
import org.knime.shared.workflow.def.ConfigValueLongDef;
import org.knime.shared.workflow.def.ConfigValuePasswordDef;
import org.knime.shared.workflow.def.ConfigValueShortArrayDef;
import org.knime.shared.workflow.def.ConfigValueShortDef;
import org.knime.shared.workflow.def.ConfigValueStringArrayDef;
import org.knime.shared.workflow.def.ConfigValueStringDef;
import org.knime.shared.workflow.def.CoordinateDef;
import org.knime.shared.workflow.def.StyleRangeDef;
import org.knime.shared.workflow.def.TemplateLinkDef;
import org.knime.shared.workflow.def.TemplateMetadataDef;
import org.knime.shared.workflow.def.impl.AnnotationDataDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigMapDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueBooleanArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueBooleanDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueByteArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueByteDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueCharArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueCharDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueDoubleArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueDoubleDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueFloatArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueFloatDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueIntArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueIntDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueLongArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueLongDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValuePasswordDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueShortArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueShortDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueStringArrayDefBuilder;
import org.knime.shared.workflow.def.impl.ConfigValueStringDefBuilder;
import org.knime.shared.workflow.def.impl.CoordinateDefBuilder;
import org.knime.shared.workflow.def.impl.StyleRangeDefBuilder;
import org.knime.shared.workflow.def.impl.TemplateLinkDefBuilder;
import org.knime.shared.workflow.def.impl.TemplateMetadataDefBuilder;
import org.knime.shared.workflow.storage.util.PasswordRedactor;

/**
 * //TODO We can add all the read from file methods for the files workflow.knime, settings.xml, template.knime.
 *
 * @author Dionysios Stolis, KNIME GmbH, Berlin, Germany
 */
public final class LoaderUtils {

    private LoaderUtils() {
    }

    public static final int DEFAULT_NEGATIVE_INDEX = -1;

    public static final String DEFAULT_EMPTY_STRING = "";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    public static final ConfigMapDef DEFAULT_CONFIG_MAP =
        new ConfigMapDefBuilder().strict().setConfigType("ConfigMap").setKey("default").build();

    static final OffsetDateTime DEFAULT_DATE_TIME = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    public static OffsetDateTime parseDate(final String s) {
        synchronized (DATE_FORMAT) {
            return OffsetDateTime.parse(s, DATE_FORMAT);
        }
    }

    public static ConfigBaseRO readNodeConfigFromFile(final File directory) throws IOException {
        var nodeSettingsFile = new File(directory, IOConst.NODE_SETTINGS_FILE_NAME.get());
        try {
            return SimpleConfig.parseConfig(nodeSettingsFile.getAbsolutePath(), nodeSettingsFile);
        } catch (IOException e) {
            throw new IOException("Cannot load the " + IOConst.NODE_SETTINGS_FILE_NAME.get(), e);
        }
    }

    public static ConfigBaseRO readWorkflowConfigFromFile(final File directory) throws IOException {
        var workflowSettingsFile = new File(directory, IOConst.WORKFLOW_FILE_NAME.get());
        try {
            return SimpleConfig.parseConfig(workflowSettingsFile.getAbsolutePath(), workflowSettingsFile);
        } catch (IOException e) {
            throw new IOException("Cannot load the " + IOConst.WORKFLOW_FILE_NAME.get(), e);
        }
    }

    public static Optional<ConfigBaseRO> readTemplateConfigFromFile(final File directory) throws IOException {
        var templateSettingsFile = new File(directory, IOConst.TEMPLATE_FILE_NAME.get());
        if (templateSettingsFile.exists()) {
            try {
                return Optional
                    .of(SimpleConfig.parseConfig(templateSettingsFile.getAbsolutePath(), templateSettingsFile));
            } catch (IOException e) {
                throw new IOException("Cannot load the " + IOConst.TEMPLATE_FILE_NAME.get(), e);
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
        return new File(workflowDirectory, IOConst.WORKFLOW_FILE_NAME.get());
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
        var fileString = workflowNodeConfig.getString(IOConst.NODE_SETTINGS_FILE.get());
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

        if(annotationConfig.containsKey("styles")) {
            ConfigBaseRO styleConfigs = annotationConfig.getConfigBase("styles");
            for (String key : styleConfigs.keySet()) {
                builder.addToStyles(() -> loadStyleRangeDef(styleConfigs.getConfigBase(key)),
                    new StyleRangeDefBuilder().build());
            }
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
    public static Optional<TemplateLinkDef> loadTemplateLink(final ConfigBaseRO templateSettings) throws InvalidSettingsException {
        if (!templateSettings.containsKey(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get())) {
            return Optional.empty();
        }

        var templateInformationSettings = templateSettings.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        return Optional.of(new TemplateLinkDefBuilder()
            .setUri(templateInformationSettings.getString(IOConst.SOURCE_URI_KEY.get(), DEFAULT_EMPTY_STRING)) //
            .setVersion(() -> parseDate(templateInformationSettings.getString(IOConst.TIMESTAMP.get())),
                DEFAULT_DATE_TIME) //
            .build());
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
    public static Optional<TemplateMetadataDef> loadTemplateMetadata(final ConfigBaseRO templateSettings) throws InvalidSettingsException {
        if (!templateSettings.containsKey(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get())) {
            return Optional.empty();
        }

        var templateInformationSettings = templateSettings.getConfigBase(IOConst.WORKFLOW_TEMPLATE_INFORMATION_KEY.get());
        return Optional.of(new TemplateMetadataDefBuilder()
            .setVersion(() -> parseDate(templateInformationSettings.getString(IOConst.TIMESTAMP.get())),
                DEFAULT_DATE_TIME) //
            .build());
    }

    /**
     * @param settings recursive key-value map
     * @param passwordRedactor post-processing for all {@link ConfigValuePasswordDef} entries, e.g., to replace with null
     * @return the recursive key-value map in a different representation
     * @throws InvalidSettingsException
     */
    public static ConfigMapDef toConfigMapDef(final ConfigBaseRO settings, final PasswordRedactor passwordRedactor)
        throws InvalidSettingsException {

        if (settings == null) {
            return null;
        }

        return (ConfigMapDef)toConfigDef((AbstractConfigEntry)settings, settings.getKey(), passwordRedactor);
    }

    /**
     * Version for internal use that does not redact passwords.
     * @param settings recursive key-value map
     * @return the recursive key-value map in a different representation
     * @throws InvalidSettingsException
     */
    public static ConfigMapDef toConfigMapDef(final ConfigBaseRO settings) throws InvalidSettingsException {
        return toConfigMapDef(settings, PasswordRedactor.unsafe());
    }


    /**
     * Create a {@link ConfigDef} tree from a {@link ConfigBase} tree.
     *
     * @param settings either a primitive value (float, boolean, int, etc.) or a recursive key-value map.
     * @param key the name of this subtree
     * @param passwordHandler post-processing for all {@link ConfigValuePasswordDef} entries, e.g., to replace with null
     * @return a POJO representation of the key-value map
     * @throws InvalidSettingsException
     */
    public static ConfigDef toConfigDef(final AbstractConfigEntry settings, final String key,
        final PasswordRedactor passwordHandler) throws InvalidSettingsException {

        if (settings instanceof ConfigBase) {
            // this is a subtree, because every class that extends AbstractConfigEntry and is not a subclass of
            // ConfigBase is a leaf class
            ConfigBase subTree = (ConfigBase)settings;

            final Map<String, ConfigDef> children = new LinkedHashMap<>();
            for (String childKey : subTree.keySet()) {
                // some subtrees are arrays in disguise, don't recurse into those
                ConfigDef asArrayDef = tryNodeSettingsAsArray(subTree, childKey);
                if (asArrayDef != null) {
                    children.put(childKey, asArrayDef);
                } else {
                   // recurse
                   ConfigDef subTreeDef = toConfigDef(subTree.getEntry(childKey), childKey, passwordHandler);
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
           // if the settings are not recursive, they are a primitive value
           return abstractConfigurationEntryToTypedLeaf(settings, passwordHandler)//
               .orElseThrow(() -> new IllegalStateException(settings.getKey() + settings.toString()));
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

   /**
    *
    * @param child
    * @param passwordHandler post-processing for all {@link ConfigValuePasswordDef} entries, e.g., to replace with null
    * @return
    */
   private static Optional<ConfigDef> abstractConfigurationEntryToTypedLeaf(final AbstractConfigEntry child,
       final PasswordRedactor passwordHandler) {
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
       } else if (child instanceof ConfigPasswordEntry) {
           final var defaultDef = new ConfigValuePasswordDefBuilder()//
               .setValue(((ConfigPasswordEntry)child).getPassword())//
               .setConfigType("ConfigValuePassword")//
               .build();
           // apply logic to protected password, e.g., remove or leave unchanged
           final var redacted = passwordHandler.apply(defaultDef);
           return Optional.of(redacted);
       } else if (child instanceof ConfigShortEntry) {
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

   /**
    * @param def a description of a key-value map containing configuration data
    * @param passwordRedactor restores potentially redacted passwords. If passwords are to be restored, this must match
    *            the {@link PasswordRedactor} passed to {@link #toConfigMapDef(ConfigBase, PasswordRedactor)}. If
    *            passwords are not expected to be restored, any handler can be used; {@link ConfigPasswordEntry} doesn't
    *            mind if the password is null.
    * @return an instance that implements {@link ConfigBase}
    *
    * @see PasswordRedactor#restore(ConfigBase, String, ConfigValuePasswordDef)
    */
   public static SimpleConfig toConfigBase(final ConfigDef def, final PasswordRedactor passwordRedactor) {
       return toSettings(def, "", SimpleConfig::new, passwordRedactor);
   }

   /**
    * Recursive function to create a node settings tree (comprising {@link AbstractConfigEntry}s) from a
    * {@link ConfigDef} tree.
    *
    * @param <T> can be used to create instances of NodeSettings (used in org.knime.core) or ConfigBase.
    *
    * @param def an entity containing the recursive settings
    * @param key the name of this subtree
    * @param constructor either NodeSettings::new or ConfigBase::new
    * @param passwordHandler
    * @return parsed key-value map as an implementation of {@link ConfigBase}
    */
   public static <T extends ConfigBase> T toSettings(final ConfigDef def, final String key,
       final Function<String, T> constructor, final PasswordRedactor passwordHandler) {
       // recursion anchor
       if (def instanceof ConfigValueArrayDef) {
           // this is an array that needs to be coded in the legacy hacky format (NodeSettings with N+1 children,
           // 1 for array size, N for items
           return toSettingsArray((ConfigValueArrayDef)def, key, constructor);
       } else if (def instanceof ConfigMapDef) { // this is a subtree, because it has a children map
           final T settings = constructor.apply(key);

           ConfigMapDef subtree = (ConfigMapDef)def;
           for (Map.Entry<String, ConfigDef> childEntry : subtree.getChildren().entrySet()) {
               final ConfigDef child = childEntry.getValue();
               // This recursion lookahead is useful because we want to do parentSettings.addBoolean(val) instead of
               // creating and returning a ConfigBooleanEntry. This way, we don't have to pass the parent down.
               if (child instanceof ConfigValueDef) {
                   addLeafToSettings(settings, childEntry.getKey(), child, passwordHandler);
               } else {
                   // recurse
                   T subTree = toSettings(child, childEntry.getKey(), constructor, passwordHandler);
                   ConfigBase childConfigTree = settings.addConfigBase(subTree.getKey());
                   subTree.copyTo(childConfigTree);
               }
           }
           return settings;
       } else {
           throw new IllegalStateException();
       }
   }

   /**
    * ConfigBase allows everything for arrays: non-empty, empty, and null.
    *
    * To avoid accidental conversions, we add null when the def array is Optional.empty (this will create the key but no
    * config entry that describes the size of the array). For Optional.present, we just convert to primitive array and
    * add. ArrayUtils is handy because it can handle a null input (although it will throw an exception when one of the
    * elements in the array is null).
    *
    * @param def describes an array of configuration values (booleans, strings, chars, etc.)
    * @return
    */
   static <T extends ConfigBase> T toSettingsArray(final ConfigValueArrayDef def, final String arrayKey,
       final Function<String, T> constructor) {
       T temp = constructor.apply("");
       if (def instanceof ConfigValueBooleanArrayDef) {
           Boolean[] boxed = ((ConfigValueBooleanArrayDef)def).getArray()
               .map(list -> list.stream().toArray(Boolean[]::new)).orElse(null);
           temp.addBooleanArray(arrayKey, ArrayUtils.toPrimitive(boxed));
       } else if (def instanceof ConfigValueByteArrayDef) {
           temp.addByteArray(arrayKey, ((ConfigValueByteArrayDef)def).getArray().orElse(null));
       } else if (def instanceof ConfigValueCharArrayDef) {
           Character[] boxed = ((ConfigValueCharArrayDef)def).getArray()
               .map(list -> list.stream().toArray(Character[]::new)).orElse(null);
           temp.addCharArray(arrayKey, ArrayUtils.toPrimitive(boxed));
       } else if (def instanceof ConfigValueDoubleArrayDef) {
           double[] values = ((ConfigValueDoubleArrayDef)def).getArray()
               .map(l -> l.stream().mapToDouble(d -> d).toArray()).orElse(null);
           temp.addDoubleArray(arrayKey, values);
       } else if (def instanceof ConfigValueFloatArrayDef) {
           Float[] boxed =
               ((ConfigValueFloatArrayDef)def).getArray().map(list -> list.stream().toArray(Float[]::new)).orElse(null);
           temp.addFloatArray(arrayKey, ArrayUtils.toPrimitive(boxed));
       } else if (def instanceof ConfigValueIntArrayDef) {
           int[] values =
               ((ConfigValueIntArrayDef)def).getArray().map(l -> l.stream().mapToInt(d -> d).toArray()).orElse(null);
           temp.addIntArray(arrayKey, values);
       } else if (def instanceof ConfigValueLongArrayDef) {
           long[] values =
               ((ConfigValueLongArrayDef)def).getArray().map(l -> l.stream().mapToLong(d -> d).toArray()).orElse(null);
           temp.addLongArray(arrayKey, values);
       } else if (def instanceof ConfigValueShortArrayDef) {
           Short[] boxed =
               ((ConfigValueShortArrayDef)def).getArray().map(list -> list.stream().toArray(Short[]::new)).orElse(null);
           temp.addShortArray(arrayKey, ArrayUtils.toPrimitive(boxed));
       } else if (def instanceof ConfigValueStringArrayDef) {
           String[] values = ((ConfigValueStringArrayDef)def).getArray()
               .map(list -> list.stream().toArray(String[]::new)).orElse(null);
           temp.addStringArray(arrayKey, values);
       }

       if (!temp.containsKey(arrayKey)) {
           throw new IllegalStateException();
       }
       return (T)temp.getEntry(arrayKey);
   }

   /**
    * Convert a {@link ConfigValueDef} to {@link NodeSettings} and add it to the given node settings.
    *
    * @param settings parent node settings to add child to
    * @param key name of the child
    * @param encryptionKey the key that was used to encrypt password entries
    * @param configuration a string representation of the value with type annotation (saying, e.g., "xdouble"), see
    *            {@link ConfigEntries}
    */
   private static void addLeafToSettings(final ConfigBase settings, final String key, final ConfigDef leafDef,
       final PasswordRedactor passwordHandler) {
       if (leafDef instanceof ConfigValueBooleanDef) {
           boolean value = ((ConfigValueBooleanDef)leafDef).isValue();
           settings.addBoolean(key, value);
       }
       if (leafDef instanceof ConfigValueByteDef) {
           byte value = (byte)((ConfigValueByteDef)leafDef).getValue().intValue();
           settings.addByte(key, value);
       }
       if (leafDef instanceof ConfigValueCharDef) {
           char value = (char)((ConfigValueCharDef)leafDef).getValue().intValue();
           settings.addChar(key, value);
       }
       if (leafDef instanceof ConfigValueDoubleDef) {
           double value = ((ConfigValueDoubleDef)leafDef).getValue();
           settings.addDouble(key, value);
       }
       if (leafDef instanceof ConfigValueFloatDef) {
           float value = ((ConfigValueFloatDef)leafDef).getValue();
           settings.addFloat(key, value);
       }
       if (leafDef instanceof ConfigValueIntDef) {
           int value = ((ConfigValueIntDef)leafDef).getValue();
           settings.addInt(key, value);
       }
       if (leafDef instanceof ConfigValueLongDef) {
           long value = ((ConfigValueLongDef)leafDef).getValue();
           settings.addLong(key, value);
       }
       if (leafDef instanceof ConfigValuePasswordDef) {
           // this is not necessarily redacted, e.g., if the PasswordRedactor used in toConfigDef doesn't do anything
           var redactedPasswordDef = ((ConfigValuePasswordDef)leafDef);
           // the password may have been redacted, in which case we make no attempt to restore it
           passwordHandler.restore(settings, key, redactedPasswordDef);
       }
       if (leafDef instanceof ConfigValueShortDef) {
           short value = (short)((ConfigValueShortDef)leafDef).getValue().intValue();
           settings.addShort(key, value);
       }
       if (leafDef instanceof ConfigValueStringDef) {
           String value = ((ConfigValueStringDef)leafDef).getValue().orElse(null);
           settings.addString(key, value);
       }
   }
}
