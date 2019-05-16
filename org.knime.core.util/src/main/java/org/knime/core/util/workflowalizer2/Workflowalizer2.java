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
 *   May 16, 2019 (hornm): created
 */
package org.knime.core.util.workflowalizer2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.JSONConfig;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.util.workflowalizer.MetadataConfig;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author hornm
 * @since 5.11
 */
public class Workflowalizer2 {

    private Workflowalizer2() {
        //utility
    }

    public static List<Object> convertAllConfigs(final List<ConfigBase> configs) {
        String wfId = UUID.randomUUID().toString();
        return configs.stream().map(config -> {
            if ("workflow.knime".equals(config.getKey())) {
                return convert(config, Workflow.class, wfId);
            } else if (config.getKey().startsWith("node")) {
                return convert(config, NodeMeta.class, null);
            } else if ("settings.xml".equals(config.getKey())) {
                return convert(config, Node.class, wfId);
            } else {
                throw new IllegalStateException("unsupported config 'complex' type");
            }
        }).collect(Collectors.toList());

    }


    public static WorkflowBundle readWorkflowBundle(final Path path) throws IOException, InvalidSettingsException {
        if (isZip(path)) {
            try (final ZipFile zip = new ZipFile(path.toAbsolutePath().toString())) {
                final String workflowPath = findFirstWorkflow(zip);
                CheckUtils.checkArgumentNotNull(workflowPath, "Zip file does not contain a workflow: " + path);
                String wfId = UUID.randomUUID().toString();
                ConfigBase workflowConfig = readFile(workflowPath + "workflow.knime", zip);
                final Workflow workflow = convert(workflowConfig, Workflow.class, wfId);
                List<ConfigBase> nodesMetaConfig = readNodesMeta(workflowConfig);
                final List<Node> nodes = nodesMetaConfig.stream().map(c -> {
                    ConfigBase cb;
                    try {
                        cb = readNode(c, workflowPath, zip);
                        return convert(cb, Node.class, wfId + "#" + c.getKey());
                    } catch (InvalidSettingsException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                return new WorkflowBundle() {

                    @Override
                    public Workflow getWorkflow() {
                        return workflow;
                    }

                    @Override
                    public List<Node> getNodes() {
                        return nodes;
                    }
                };
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static List<ConfigBase> readAllConfigs(final Path path) throws IOException, InvalidSettingsException {
        if (isZip(path)) {
            List<ConfigBase> res = new ArrayList<ConfigBase>();
            try (final ZipFile zip = new ZipFile(path.toAbsolutePath().toString())) {
                final String workflowPath = findFirstWorkflow(zip);
                CheckUtils.checkArgumentNotNull(workflowPath, "Zip file does not contain a workflow: " + path);
                ConfigBase workflowKnime = readFile(workflowPath + "workflow.knime", zip);
                res.add(workflowKnime);
                List<ConfigBase> nodesMeta = readNodesMeta(workflowKnime);
                //res.addAll(nodesMeta);
                for (ConfigBase nodeMeta : nodesMeta) {
                    res.add(readNode(nodeMeta, workflowPath, zip));
                }
                return res;
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static List<ConfigBase> readNodesMeta(final ConfigBase workflowConfig)
        throws InvalidSettingsException {
        List<ConfigBase> res = new ArrayList<ConfigBase>();
        final ConfigBase c = workflowConfig.getConfigBase("nodes");
        for (final String key : c.keySet()) {
            res.add(c.getConfigBase(key));
        }
        return res;
    }

    private static ConfigBase readNode(final ConfigBase nodeMetaConfig, final String parentDir, final ZipFile zip)
        throws InvalidSettingsException, IOException {
        String settings = nodeMetaConfig.getString("node_settings_file");
        return readFile(parentDir + settings, zip);
    }

    private static boolean isZip(final Path path) throws FileNotFoundException, IOException {
        if (!Files.exists(path)) {
            return false;
        }
        if (Files.isDirectory(path)) {
            return false;
        }

        final File file = path.toFile();
        final String ext = FilenameUtils.getExtension(file.getName());
        if (!ext.equals("zip") && !ext.equals("knar") && !ext.equals("knwf")) {
            return false;
        }
        int fileSignature = 0;
        try (final RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
            fileSignature = raf.readInt();
        }
        // Zip file signatures or "magic number"
        // see https://en.wikipedia.org/wiki/List_of_file_signatures
        return fileSignature == 0x504B0304 || fileSignature == 0x504B0506 || fileSignature == 0x504B0708;
    }

    static String findFirstWorkflow(final ZipFile zipFile) {
        final List<String> templates = new ArrayList<>();
        boolean isTemplate = false;
        String workflow = null;

        do {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            int numSlashes = Integer.MAX_VALUE;
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                final String name = entry.getName();
                if (name.contains("workflow.knime") && !templates.contains(name)) {
                    final int matches = StringUtils.countMatches(name, "/");
                    if (matches < numSlashes) {
                        numSlashes = matches;
                        workflow = name.substring(0, name.length() - 14);
                    }
                }
            }

            // check if it is a template
            entries = zipFile.entries();
            isTemplate = false;
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.equals(workflow + "template.knime")) {
                    isTemplate = true;
                    templates.add(workflow + "workflow.knime");
                    break;
                }
            }
        } while (isTemplate);

        return workflow;
    }



    private static ConfigBase readFile(final String entry, final ZipFile zip) throws IOException {
        final ZipEntry e = zip.getEntry(entry);
        CheckUtils.checkArgumentNotNull(e, "Zip entry does not exist: " + entry);
        try (final InputStream s = zip.getInputStream(e)) {
            final MetadataConfig c = new MetadataConfig("ignored");
            c.load(s);
            return c;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T convert(final Map<String, Object> map, final Class<T> pojoClass) {
        return (T)Proxy.newProxyInstance(Workflowalizer2.class.getClassLoader(), new Class[]{pojoClass},
            (proxy, method, args) -> {
                String key = method.getAnnotation(JsonProperty.class).value().replaceAll("#", ".");
                Class<?> returnType = method.getReturnType();
                if (!map.containsKey(key)) {
                    return null;
                }
                Object value = map.get(key);
                if (String.class.isAssignableFrom(returnType)) {
                    return (String)value;
                } else if (int.class.isAssignableFrom(returnType)) {
                    return Integer.parseInt((String)value);
                } else if (boolean.class.isAssignableFrom(returnType)) {
                    return Boolean.parseBoolean((String)value);
                } else if (double.class.isAssignableFrom(returnType)) {
                    return Double.parseDouble((String)value);
                } else if (Map.class.isAssignableFrom(returnType)) {
                    assert value instanceof Map;
                    ParameterizedType parametrizedType = (ParameterizedType)method.getGenericReturnType();
                    Class<?> returnParam = (Class<?>)parametrizedType.getActualTypeArguments()[1];
                    Map submap = (Map)value;
                    Map res = new HashMap();
                    submap.keySet().forEach(k -> {
                        Object v = submap.get(k);
                        assert v instanceof Map;
                        res.put(k, convert((Map)v, returnParam));
                    });
                    return res;
                } else if (ConfigBase.class.isAssignableFrom(returnType)) {
                    assert value instanceof String;
                    MetadataConfig conf = new MetadataConfig(key);
                    JSONConfig.readJSON(conf, new StringReader((String)value));
                    return conf;
                } else {
                    assert value instanceof Map;
                    //a nested pojo
                    return convert((Map) value, returnType);
                }
            });
    }


    @SuppressWarnings("unchecked")
    public static <T> T convert(final ConfigBase config, final Class<T> pojoClass, final String id) {
        return (T)Proxy.newProxyInstance(Workflowalizer2.class.getClassLoader(), new Class[]{pojoClass},
            (proxy, method, args) -> {
                if("getId".equals(method.getName())) {
                    assert id != null;
                    if (int.class.isAssignableFrom(method.getReturnType())) {
                        return config.getInt("id");
                    }
                    return id;
                }
                String key = method.getAnnotation(JsonProperty.class).value().replaceAll("#", ".");
                Class<?> returnType = method.getReturnType();
                if (!config.containsKey(key)) {
                    return null;
                }
                if (String.class.isAssignableFrom(returnType)) {
                    return config.getString(key);
                } else if (int.class.isAssignableFrom(returnType)) {
                    return config.getInt(key);
                } else if (boolean.class.isAssignableFrom(returnType)) {
                    return config.getBoolean(key);
                } else if (double.class.isAssignableFrom(returnType)) {
                    return config.getDouble(key);
                } else if (Map.class.isAssignableFrom(returnType)) {
                    ParameterizedType parametrizedType = (ParameterizedType)method.getGenericReturnType();
                    Class<?> returnParam = (Class<?>)parametrizedType.getActualTypeArguments()[1];
                    ConfigBase list = config.getConfigBase(key);
                    Map res = new HashMap();
                    for (int i = 0; i < list.getChildCount(); i++) {
                        ConfigBase child = (ConfigBase)list.getChildAt(i);
                        res.put(child.getKey(), convert(child, returnParam, null));
                    }
                    return res;
                } else if (ConfigBase.class.isAssignableFrom(returnType)) {
                    return config.getConfigBase(key);
                } else {
                    //a nested pojo
                    return convert(config.getConfigBase(key), returnType, null);
                }
            });
    }

    public static ConfigBase convert(final Object pojo, final ConfigBase initialConfigBase) {
        ConfigBase cb = initialConfigBase;
        for (Method m : pojo.getClass().getInterfaces()[0].getMethods()) {
            if("getId".equals(m.getName())) {
                continue;
            }
            String key = m.getAnnotation(JsonProperty.class).value().replaceAll("#", ".");
            Object value;
            try {
                value = m.invoke(pojo);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                throw new RuntimeException();
            }
            if (value == null) {
                continue;
            }
            Class<?> returnType = m.getReturnType();
            if (String.class.isAssignableFrom(returnType)) {
                cb.addString(key, (String)value);
            } else if (int.class.isAssignableFrom(returnType)) {
                cb.addInt(key, (int)value);
            } else if (boolean.class.isAssignableFrom(returnType)) {
                cb.addBoolean(key, (boolean)value);
            } else if (double.class.isAssignableFrom(returnType)) {
                cb.addDouble(key, (double)value);
            } else if (Map.class.isAssignableFrom(returnType)) {
                Map map = (Map)value;
                ConfigBase cmap = cb.addConfigBase(key);
                map.keySet().stream().forEach(k -> {
                    ConfigBase newCbEntry = cmap.addConfigBase((String)k);
                    ConfigBase e = convert(map.get(k), cb.getInstance((String)k));
                    e.copyTo(newCbEntry);
                });
            } else if (ConfigBase.class.isAssignableFrom(returnType)) {
                ConfigBase newCb = cb.addConfigBase(key);
                ((ConfigBase)value).copyTo(newCb);
            } else {
                //a nested pojo
                ConfigBase c = convert(value, cb.getInstance(key));
                ConfigBase newCb = cb.addConfigBase(key);
                c.copyTo(newCb);
            }
        }
        return cb;
    }

}
