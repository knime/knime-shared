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
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.SimpleConfig;
import org.knime.core.workflow.def.AnnotationDataDef;
import org.knime.core.workflow.def.BaseNodeDef.NodeTypeEnum;
import org.knime.core.workflow.def.ConfigDef;
import org.knime.core.workflow.def.ConfigMapDef;
import org.knime.core.workflow.def.ConfigValueArrayDef;
import org.knime.core.workflow.def.ConfigValueBooleanArrayDef;
import org.knime.core.workflow.def.ConfigValueBooleanDef;
import org.knime.core.workflow.def.ConfigValueByteArrayDef;
import org.knime.core.workflow.def.ConfigValueByteDef;
import org.knime.core.workflow.def.ConfigValueCharArrayDef;
import org.knime.core.workflow.def.ConfigValueCharDef;
import org.knime.core.workflow.def.ConfigValueDef;
import org.knime.core.workflow.def.ConfigValueDoubleArrayDef;
import org.knime.core.workflow.def.ConfigValueDoubleDef;
import org.knime.core.workflow.def.ConfigValueFloatArrayDef;
import org.knime.core.workflow.def.ConfigValueFloatDef;
import org.knime.core.workflow.def.ConfigValueIntArrayDef;
import org.knime.core.workflow.def.ConfigValueIntDef;
import org.knime.core.workflow.def.ConfigValueLongArrayDef;
import org.knime.core.workflow.def.ConfigValueLongDef;
import org.knime.core.workflow.def.ConfigValueShortArrayDef;
import org.knime.core.workflow.def.ConfigValueShortDef;
import org.knime.core.workflow.def.ConfigValueStringArrayDef;
import org.knime.core.workflow.def.ConfigValueStringDef;

/**
 * Utility class for the workflow saver
 *
 * @author Jasper Krauter, KNIME GmbH, Konstanz, Germany
 */
public final class SaverUtils {

    private SaverUtils() {
    }

    /**
     * Invokes {@link ConfigBase#addEntry} only if the entry is not null
     *
     * @param settings The ConfigBase to which the entry might be added
     * @param config The entry to add
     * @param key The key under which the new entry should be inserted
     */
    public static void addEntryIfNotNull(final ConfigBase settings, final ConfigDef config, final String key) {
        if (config != null) {
            settings.addEntry(toConfigBase(config, key));
        }
    }

    /**
     * Helper function that recursively converts a {@link ConfigDef}-object to a {@link ConfigBase} that can be written
     * to an XML file.
     *
     * @param configDef The def object containing configuration key-value pairs.
     * @param key The root key for the to-be-created config.
     * @return A new ConfigBase instance filled with items from the input def.
     */
    public static ConfigBase toConfigBase(final ConfigDef configDef, final String key) {
        // These methods use the same logic as org.knime.core.workflow.def.DefToCoreUtil#toNodeSettings etc.
        if (configDef instanceof ConfigValueArrayDef) {
            return toConfigBaseArray((ConfigValueArrayDef)configDef, key);
        } else if (configDef instanceof ConfigMapDef) {
            var configBase = new SimpleConfig(key);
            var configMapDef = (ConfigMapDef)configDef;
            configMapDef.getChildren().forEach((k, v) -> {
                if (v instanceof ConfigValueDef) {
                    addLeafToConfigBase(configBase, k, v);
                } else {
                    var child = toConfigBase(v, k);
                    configBase.addEntry(child);
                }
            });
            return configBase;
        } else {
            throw new IllegalStateException(
                String.format("Could not parse ConfigDef (is %snull)", (configDef == null) ? " " : "not "));
        }
    }

    private static void addLeafToConfigBase(final ConfigBase configBase, final String key, final ConfigDef leafDef) {
        if (leafDef instanceof ConfigValueBooleanDef) {
            boolean value = ((ConfigValueBooleanDef)leafDef).isValue();
            configBase.addBoolean(key, value);
        } else if (leafDef instanceof ConfigValueCharDef) {
            char value = (char)((ConfigValueCharDef)leafDef).getValue().intValue();
            configBase.addChar(key, value);
        } else if (leafDef instanceof ConfigValueDoubleDef) {
            double value = ((ConfigValueDoubleDef)leafDef).getValue();
            configBase.addDouble(key, value);
        } else if (leafDef instanceof ConfigValueFloatDef) {
            float value = ((ConfigValueFloatDef)leafDef).getValue();
            configBase.addFloat(key, value);
        } else if (leafDef instanceof ConfigValueIntDef) {
            int value = ((ConfigValueIntDef)leafDef).getValue();
            configBase.addInt(key, value);
        } else if (leafDef instanceof ConfigValueLongDef) {
            long value = ((ConfigValueLongDef)leafDef).getValue();
            configBase.addLong(key, value);
        } else if (leafDef instanceof ConfigValueByteDef) {
            byte value = (byte)((ConfigValueByteDef)leafDef).getValue().intValue();
            configBase.addByte(key, value);
        } else if (leafDef instanceof ConfigValueShortDef) {
            short value = (short)((ConfigValueShortDef)leafDef).getValue().intValue();
            configBase.addShort(key, value);
        } else if (leafDef instanceof ConfigValueStringDef) {
            String value = ((ConfigValueStringDef)leafDef).getValue();
            configBase.addString(key, value);
        }
        // TODO password need passphrase to decode //NOSONAR
    }

    @SuppressWarnings("squid:S6212")
    private static ConfigBase toConfigBaseArray(final ConfigValueArrayDef configArrayDef, final String key) {//NOSONAR
        ConfigBase temp = new SimpleConfig("dummy");
        if (configArrayDef instanceof ConfigValueBooleanArrayDef) {
            List<Boolean> values = ((ConfigValueBooleanArrayDef)configArrayDef).getArray();
            boolean[] array = new boolean[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = values.get(i);
            }
            temp.addBooleanArray(key, array);
        } else if (configArrayDef instanceof ConfigValueByteArrayDef) {
            temp.addByteArray(key, ((ConfigValueByteArrayDef)configArrayDef).getArray());
        } else if (configArrayDef instanceof ConfigValueCharArrayDef) {
            List<Integer> values = ((ConfigValueCharArrayDef)configArrayDef).getArray();
            char[] array = new char[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = (char)values.get(i).intValue();
            }
            temp.addCharArray(key, array);
        } else if (configArrayDef instanceof ConfigValueDoubleArrayDef) {
            List<Double> values = ((ConfigValueDoubleArrayDef)configArrayDef).getArray();
            double[] array = new double[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = values.get(i);
            }
            temp.addDoubleArray(key, array);
        } else if (configArrayDef instanceof ConfigValueFloatArrayDef) {
            List<Float> values = ((ConfigValueFloatArrayDef)configArrayDef).getArray();
            float[] array = new float[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = values.get(i);
            }
            temp.addFloatArray(key, array);
        } else if (configArrayDef instanceof ConfigValueIntArrayDef) {
            List<Integer> values = ((ConfigValueIntArrayDef)configArrayDef).getArray();
            int[] array = new int[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = values.get(i);
            }
            temp.addIntArray(key, array);
        } else if (configArrayDef instanceof ConfigValueLongArrayDef) {
            List<Long> values = ((ConfigValueLongArrayDef)configArrayDef).getArray();
            long[] array = new long[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = values.get(i);
            }
            temp.addLongArray(key, array);
        } else if (configArrayDef instanceof ConfigValueShortArrayDef) {
            List<Integer> values = ((ConfigValueShortArrayDef)configArrayDef).getArray();
            short[] array = new short[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = (short)values.get(i).intValue();
            }
            temp.addShortArray(key, array);
        } else if (configArrayDef instanceof ConfigValueStringArrayDef) {
            List<String> values = ((ConfigValueStringArrayDef)configArrayDef).getArray();
            String[] array = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                array[i] = values.get(i);
            }
            temp.addStringArray(key, array);
        }

        if (!temp.containsKey(key)) {
            throw new IllegalStateException();
        }

        return (ConfigBase)temp.getEntry(key);
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
        annotation.addInt("defFontSize", annotationDataDef.getDefaultFontSize());
        annotation.addInt("annotation-version", annotationDataDef.getAnnotationVersion());

        var styles = new SimpleConfig("styles");
        var styleIndex = 0;
        for (var styleRangeDef : annotationDataDef.getStyles()) {
            var style = new SimpleConfig(String.format("style_%d", styleIndex));
            style.addInt("start", styleRangeDef.getStart());
            style.addInt("length", styleRangeDef.getLength());
            style.addString("fontname", styleRangeDef.getFontName());
            style.addInt("fontstyle", styleRangeDef.getFontStyle());
            style.addInt("fontsize", styleRangeDef.getFontSize());
            style.addInt("fgcolor", styleRangeDef.getColor());
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
     * @param safeName A name which has already been freed from illegal characters (and probably trimmed)
     * @param nodeID The numerical ID of the node
     * @return The node directory
     * @throws FileNotFoundException If the parent directory doesn't exist or cannot be written to
     */
    public static File createNodeDir(final File parent, final String safeName, final int nodeID)
        throws FileNotFoundException {
        if (!(parent.exists() && parent.isDirectory() && parent.canWrite())) {
            throw new FileNotFoundException(
                "The directory " + parent.getAbsolutePath() + " does not exist or cannot be written to.");
        }
        var dirName = String.format("%s (#%d)", safeName, nodeID);
        var directory = new File(parent, dirName);
        directory.mkdir();
        return directory;
    }

    /**
     * Determines if a node type is meta (yes, except native node). This should probably be a function of the
     * {@link NodeTypeEnum}, but we shouldn't mess with generated sources...
     *
     * @param type The type to check for
     * @return If the input type is meta
     */
    public static boolean isNodeTypeMeta(final NodeTypeEnum type) {
        switch (type) {
            case COMPONENT:
            case METANODE:
                return true;
            default:
                return false;
        }
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
