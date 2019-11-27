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
 *   Nov 26, 2019 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.config.base.XMLConfig;
import org.knime.core.util.Version;
import org.knime.core.util.XMLUtils;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;

/**
 *
 * @author awalter
 * @since 5.13
 */
public class AbstractWorkflowalizerTest {

    // -- General --

    /**
     * Asserts that an {@link UnsupportedOperationException} was thrown.
     *
     * @param c the method to call, which should throw an UOE
     * @throws Exception
     */
    protected static void assertUOEThrown(final Callable<?> c) throws Exception {
        try {
            c.call();
            assertTrue("Expected UnsupportedOperationException was not thrown", false);
        } catch (final UnsupportedOperationException ex) {
            // Do nothing, this is expected
        }
    }

    /**
     * Unzips the given zip input to the given folder.
     *
     * @param in Non-null input stream.
     * @param folder Non-null output folder
     * @throws Exception
     */
    protected static void unzip(final InputStream in, final File folder) throws Exception {
        try (ArchiveInputStream ais =
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in)) {
            ZipArchiveEntry entry;
            while ((entry = (ZipArchiveEntry)ais.getNextEntry()) != null) {
                final File outputFile = new File(folder, entry.getName());
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    try (OutputStream os = new FileOutputStream(outputFile)) {
                        IOUtils.copy(ais, os);
                    }
                }
            }
        }
    }

    /**
     * Checks that the structure of an {@link IWorkflowMetadata} object is correct.
     *
     * @param wkfMd parsed {@link IWorkflowMetadata} object
     * @param workflowFile path to original workflow file
     * @throws Exception
     */
    protected void testStructure(final IWorkflowMetadata wkfMd, final Path workflowFile) throws Exception {
        final List<NodeMetadata> metaNodes = new ArrayList<>();
        final List<NodeMetadata> wrappedMetaNodes = new ArrayList<>();
        for (final NodeMetadata nodeMeta : wkfMd.getNodes()) {
            if (nodeMeta.getType().equals(NodeType.METANODE)) {
                metaNodes.add(nodeMeta);
            }
            if (nodeMeta.getType().equals(NodeType.SUBNODE)) {
                wrappedMetaNodes.add(nodeMeta);
            }
        }

        final List<String> lines = Files.readAllLines(workflowFile);
        final long nodeCount =
            lines.stream().filter(line -> line.contains("<entry key=\"node_settings_file\"")).count();
        final long metaNodeCount = lines.stream()
            .filter(line -> line.contains("key=\"node_type\"") && line.contains("value=\"MetaNode\"")).count();
        final long wrappedMetaNodeCount = lines.stream()
            .filter(line -> line.contains("key=\"node_type\"") && line.contains("value=\"SubNode\"")).count();

        assertEquals(nodeCount, wkfMd.getNodes().size());
        assertEquals(metaNodeCount, metaNodes.size());
        assertEquals(wrappedMetaNodeCount, wrappedMetaNodes.size());

        for (final NodeMetadata metaNode : metaNodes) {
            final String settingsLine = lines.stream()
                .filter(line -> line.contains("key=\"node_settings_file\"") && line.contains(metaNode.getNodeId() + ""))
                .findFirst().get();
            final String settingsPath =
                settingsLine.substring(settingsLine.indexOf("value=\"") + 7, settingsLine.lastIndexOf('"'));
            final File metaNodeFile = new File(workflowFile.toFile().getParentFile(), settingsPath);
            testStructure((IWorkflowMetadata)metaNode, metaNodeFile.toPath());
        }

        for (final NodeMetadata wrappedMetaNode : wrappedMetaNodes) {
            final String settingsLine = lines.stream().filter(
                line -> line.contains("key=\"node_settings_file\"") && line.contains(wrappedMetaNode.getNodeId() + ""))
                .findFirst().get();
            final String settingsPath =
                settingsLine.substring(settingsLine.indexOf("value=\"") + 7, settingsLine.lastIndexOf('"'));
            final File wMNNodeFile = new File(workflowFile.toFile().getParentFile(), settingsPath);
            final File wMNWorkflowFile = new File(wMNNodeFile.getParentFile(), "workflow.knime");
            testStructure((IWorkflowMetadata)wrappedMetaNode, wMNWorkflowFile.toPath());
        }
    }

    /**
     * Parses out the node IDs from the raw workflow.knime file.
     *
     * @param readLines the raw workflow.knime file lines
     * @return the node IDs
     */
    private static List<Integer> readNodeIds(final List<String> readLines) {
        final List<Integer> ids = new ArrayList<>();
        readLines.stream().filter(line -> line.contains("<config key=\"node_"))
            .forEach(s -> ids.add(Integer.parseInt(s.substring(s.indexOf("node_") + 5, s.lastIndexOf('"')))));
        return ids;
    }

    // -- Workflow Group Tests --

    /**
     * Asserts that the workflowset.meta information is equivalent.
     *
     * <p>
     * Specifically this assumes that the workflowset.meta has a comment and author field. But no tags, links, or title!
     * This is a legacy format from before the workflow metadata editor, which assumes if there is a long comment
     * without line breaks it is all a description.
     * </p>
     *
     * @param rawFileLines the raw lines read from the workflowset.meta file
     * @param workflowSetMeta the {@link WorkflowSetMeta} returned by the workflowalizer
     */
    protected void testWorkflowSetMetaSimple(final List<String> rawFileLines, final WorkflowSetMeta workflowSetMeta) {
        assertEquals(parseWorkflowSetMeta("Author", rawFileLines), workflowSetMeta.getAuthor().orElse(null));

        final String comments = parseWorkflowSetMeta("Comments", rawFileLines);
        assertFalse(workflowSetMeta.getTitle().isPresent());
        assertEquals(comments, workflowSetMeta.getDescription().orElse(null));
        assertTrue(workflowSetMeta.getLinks().get().size() == 0);
        assertTrue(workflowSetMeta.getTags().get().size() == 0);
    }

    // -- Workflow Tests --

    /**
     * Asserts that the annotations are equivalent.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link IWorkflowMetadata} returned by the workflowalizer
     * @throws Exception
     */
    protected void testAnnotations(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd)
        throws Exception {
        assertEquivalentAnnotations(readAnnotations(workflowFileLines), wkfMd.getAnnotations());
    }

    /**
     * Asserts that the {@link AuthorInformation} is equivalent.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link AbstractRepositoryItemMetadata} returned by the workflowalizer
     * @throws IOException
     * @throws ParseException
     */
    protected void testAuthorInformation(final List<String> workflowFileLines,
        final AbstractRepositoryItemMetadata<?, ?> wkfMd) throws IOException, ParseException {
        assertEquals(readAuthorInformation(workflowFileLines), wkfMd.getAuthorInformation());
    }

    /**
     * Asserts that the connections are equivalent.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link IWorkflowMetadata} returned by the workflowalizer
     * @throws Exception
     */
    protected void testConnections(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd)
        throws Exception {
        assertEquivalentConnections(readConnectionIds(workflowFileLines), wkfMd.getConnections());
    }

    /**
     * Asserts that the created by dates are equivalent.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link IWorkflowMetadata} returned by the workflowalizer
     */
    protected void testCreatedBy(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd) {
        assertEquals(readCreatedBy(workflowFileLines), wkfMd.getCreatedBy());
    }

    /**
     * Asserts that the custom descriptions are equivalent.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link IWorkflowMetadata} returned by the workflowalizer
     */
    protected void testCustomDescription(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd) {
        assertEquals(readCustomDescription(workflowFileLines), wkfMd.getCustomDescription());
    }

    /**
     * Asserts that the workflow names are equivalent.
     *
     * @param workflowDir the {@link Path} to the workflow directory.
     * @param wkfMd the parsed {@link WorkflowMetadata} returned by the workflowalizer
     */
    protected void testWorkflowName(final Path workflowDir, final WorkflowMetadata wkfMd) {
        assertEquals(workflowDir.getFileName().toString(), wkfMd.getName());
    }

    /**
     * Asserts that the workflow names match.
     *
     * @param workflowFileLines the lines from the raw workflow file
     * @param wkfMd the {@link IWorkflowMetadata} returned by the workflowalizer
     */
    protected void testWorkflowName(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd) {
        assertEquals(readName(workflowFileLines), wkfMd.getName());
    }

    /**
     * Asserts that the node IDs match.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link IWorkflowMetadata} returned by the workflowalizer
     * @param prefix an optional prefix to append when check that the IDs are the same, this is usually used when
     *            ensuring nested metanode/component IDs have been prepended to the node ID string
     */
    protected void testNodeIds(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd,
        final String prefix) {
        final List<Integer> nodeIds = readNodeIds(workflowFileLines);
        final List<NodeMetadata> nodes = wkfMd.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
            if (prefix != null) {
                assertEquals(prefix + id, node.getNodeId());
            }
        }
    }

    /**
     * Asserts that the node IDs match, and that there is the right number of each "type" of node.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link IWorkflowMetadata} returned by the workflowalizer
     * @param trueNativeNodeCount the expected native node count
     * @param trueMetaNodeCount the expected metanode count
     * @param trueComponentCount the expected component count
     */
    protected void testNodeIds(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd,
        final int trueNativeNodeCount, final int trueMetaNodeCount, final int trueComponentCount) {
        List<Integer> nodeIds = readNodeIds(workflowFileLines);
        List<NodeMetadata> nodes = wkfMd.getNodes();
        assertEquals(nodeIds.size(), nodes.size());
        int nativeNodeCount = 0;
        int subnodeCount = 0;
        int metanodeCount = 0;
        for (final NodeMetadata node : nodes) {
            String id = node.getNodeId();
            final int index = id.lastIndexOf(':');
            if (index >= 0) {
                id = id.substring(index + 1, id.length());
            }
            assertTrue(nodeIds.contains(Integer.parseInt(id)));
            if (node.getType().equals(NodeType.NATIVE_NODE)) {
                nativeNodeCount++;
            }
            if (node.getType().equals(NodeType.SUBNODE)) {
                subnodeCount++;
            }
            if (node.getType().equals(NodeType.METANODE)) {
                metanodeCount++;
            }
        }
        assertEquals(trueNativeNodeCount, nativeNodeCount);
        assertEquals(trueComponentCount, subnodeCount);
        assertEquals(trueMetaNodeCount, metanodeCount);
    }

    /**
     * Asserts that the versions match.
     *
     * @param workflowFileLines the raw lines read from the workflow.knime file
     * @param wkfMd the parsed {@link IWorkflowMetadata} returned by the workflowalizer
     */
    protected void testVersion(final List<String> workflowFileLines, final IWorkflowMetadata wkfMd) {
        assertEquals(readVersion(workflowFileLines), wkfMd.getVersion());
    }

    /**
     * Asserts that the workflow SVG is present, and has the given width and height.
     *
     * @param width the SVG width
     * @param height the SVG height
     * @param wkfMd the parsed {@link WorkflowMetadata} returned by the workflowalizer
     */
    protected void testSvg(final int width, final int height, final WorkflowMetadata wkfMd) {
        assertTrue(wkfMd.getWorkflowSvg().isPresent());
        assertEquals(width, wkfMd.getWorkflowSvg().get().getWidth().intValue());
        assertEquals(height, wkfMd.getWorkflowSvg().get().getHeight().intValue());
    }

    // -- Node Tests --

    /**
     * Asserts that the node annotation texts match.
     *
     * @param rawNodeFileLines the lines read from the raw node file
     * @param nm the {@link NodeMetadata} returned by the workflowalizer
     */
    protected void testAnnotationText(final List<String> rawNodeFileLines, final NodeMetadata nm) {
        assertEquals(readAnnotationText(rawNodeFileLines), nm.getAnnotationText());
    }

    /**
     * Asserts that the custom node descriptions match.
     *
     * @param rawNodeFileLines the lines read from the raw node file
     * @param snm the {@link SingleNodeMetadata} returned by the workflowalizer
     */
    protected void testCustomNodeDescription(final List<String> rawNodeFileLines, final SingleNodeMetadata snm) {
        assertEquals(readCustomNodeDescription(rawNodeFileLines), snm.getCustomNodeDescription());
    }

    /**
     * Asserts that the node configurations match.
     *
     * @param nodeDir the {@link Path} to the node directory containing the settings.xml file
     * @param snm the {@link SingleNodeMetadata} returned by the workflowalizer
     * @throws Exception
     */
    protected void testNodeConfiguration(final Path nodeDir, final SingleNodeMetadata snm) throws Exception {
        assertTrue(readNodeConfiguration(new File(nodeDir.toFile(), "settings.xml")).get()
            .isIdentical(snm.getNodeConfiguration().get()));
    }

    /**
     * Asserts that the {@link NodeAndBundleInformation} matches.
     *
     * @param rawNodeFileLines the lines read from the raw node file
     * @param nnm the {@link NativeNodeMetadata} returned by the workflowalizer
     */
    protected void testNodeAndBundleInformation(final List<String> rawNodeFileLines, final NativeNodeMetadata nnm) {
        assertEquals(readNodeAndBundleInformation(false, rawNodeFileLines), nnm.getNodeAndBundleInformation());
    }

    /**
     * Asserts that the template links match.
     *
     * @param rawFileLines the lines from the raw file containing the template link information
     * @param sm the {@link SubnodeMetadata} returned by the workflowalizer
     */
    protected void testTemplateLink(final List<String> rawFileLines, final SubnodeMetadata sm) {
        assertEquals(readTemplateLink(rawFileLines), sm.getTemplateLink());
    }

    /**
     * Asserts that the template links match.
     *
     * @param rawFileLines the lines from the raw file containing the template link information
     * @param mm the {@link MetanodeMetadata} returned by the workflowalizer
     */
    protected void testTemplateLink(final List<String> rawFileLines, final MetanodeMetadata mm) {
        assertEquals(readTemplateLink(rawFileLines), mm.getTemplateLink());
    }

    // -- Template Tests --

    /**
     * Asserts that the two {@link TemplateInformation}s match.
     *
     * @param rawTemplateFileLines the lines from the raw template metadata file
     * @param tm the {@link TemplateMetadata} returned by the workflowalizer
     * @throws IOException
     * @throws ParseException
     */
    protected void testTemplateInformation(final List<String> rawTemplateFileLines, final TemplateMetadata tm)
        throws IOException, ParseException {
        assertEquals(readTemplateInformation(rawTemplateFileLines), tm.getTemplateInformation());
    }

    // -- Component Tests --

    // -- Helper methods --

    private static Optional<String> readAnnotationText(final List<String> readLines) {
        for (int i = 0; i < readLines.size(); i++) {
            final String line = readLines.get(i);
            if (line.contains("nodeAnnotation")) {
                final String annotation = readLines.get(i + 1);
                return Optional.ofNullable(XMLUtils
                    .unescape(annotation.substring(annotation.indexOf("value=\"") + 7, annotation.lastIndexOf('"'))));
            }
        }
        return Optional.empty();
    }

    private static Optional<String> readCustomNodeDescription(final List<String> readLines) {
        return Optional.ofNullable(XMLUtils.unescape(parseValue(readLines, "customDescription")));
    }

    private static Optional<ConfigBase> readNodeConfiguration(final File f) throws Exception {
        final ConfigBase params = new MetadataConfig("settings.xml");
        try (final FileInputStream in = new FileInputStream(f)) {
            XMLConfig.load(params, in);
        }
        if (!params.containsKey("model")) {
            return Optional.empty();
        }
        final ConfigBase model = params.getConfigBase("model");
        model.setParent(null);
        return Optional.ofNullable(model);
    }

    private static NodeAndBundleInformation readNodeAndBundleInformation(final boolean isMetanode,
        final List<String> readLines) {
        if (isMetanode) {
            return new NodeAndBundleInformation(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty());
        }
        final Optional<String> nodeName = Optional.ofNullable(parseValue(readLines, "node-name"));
        final Optional<String> nodeFactory = Optional.ofNullable(parseValue(readLines, "key=\"factory\""));
        final Optional<String> nodeBundleName = Optional.ofNullable(parseValue(readLines, "node-bundle-name"));
        final Optional<String> nodeBundleSymbolicName =
            Optional.ofNullable(parseValue(readLines, "node-bundle-symbolic-name"));
        final Optional<String> nodeBundleVendor = Optional.ofNullable(parseValue(readLines, "node-bundle-vendor"));
        final String nodeBundleVersionString = parseValue(readLines, "node-bundle-version");
        final Optional<Version> nodeBundleVersion = nodeBundleVersionString == null || nodeBundleVersionString.isEmpty()
            ? Optional.empty() : Optional.ofNullable(new Version(nodeBundleVersionString));
        final Optional<String> nodeFeatureName = Optional.ofNullable(parseValue(readLines, "node-feature-name"));

        String fsn = parseValue(readLines, "node-feature-symbolic-name");
        if (fsn != null && fsn.endsWith(".feature.group")) {
            fsn = fsn.substring(0, fsn.length() - 14);
        }
        final Optional<String> nodeFeatureSymbolicName = Optional.ofNullable(fsn);

        final Optional<String> nodeFeatureVendor = Optional.ofNullable(parseValue(readLines, "node-feature-vendor"));
        final String nodeFeatureVersionString = parseValue(readLines, "node-feature-version");
        final Optional<Version> nodeFeatureVersion =
            nodeFeatureVersionString == null || nodeFeatureVersionString.isEmpty() ? Optional.empty()
                : Optional.ofNullable(new Version(nodeFeatureVersionString));
        return new NodeAndBundleInformation(nodeFactory, nodeBundleSymbolicName, nodeBundleName, nodeBundleVendor,
            nodeName, nodeBundleVersion, nodeFeatureSymbolicName, nodeFeatureName, nodeFeatureVendor,
            nodeFeatureVersion);
    }

    private static Optional<String> readTemplateLink(final List<String> readLines) {
        final String r = parseValue(readLines, "sourceURI");
        if (!StringUtils.isEmpty(r)) {
            try {
                final String decoded = java.net.URLDecoder.decode(r, StandardCharsets.UTF_8.name());
                return Optional.of(decoded);
            } catch (UnsupportedEncodingException e) {
                // string can't be decoded, return encoded string
                return Optional.ofNullable(r);
            }
        }
        return Optional.empty();
    }

    private static Version readVersion(final List<String> readLines) {
        final String versionString = parseValue(readLines, "key=\"version\"");
        return new Version(versionString);
    }

    private static String readName(final List<String> readLines) {
        return parseValue(readLines, "key=\"name\"");
    }

    private static Optional<String> readCustomDescription(final List<String> readLines) {
        return Optional.ofNullable(parseValue(readLines, "customDescription"));
    }

    private static Version readCreatedBy(final List<String> readLines) {
        final String createdByString = parseValue(readLines, "created_by");
        return new Version(createdByString);
    }

    private static void assertEquivalentConnections(final List<Integer> expectedIds,
        final List<NodeConnection> actual) {
        assertEquals(expectedIds.size() / 4, actual.size());
        for (int i = 0, j = 0; i < actual.size(); i++, j += 4) {
            final int sourceId = expectedIds.get(j).intValue();
            final int destId = expectedIds.get(j + 1).intValue();
            String parsedSource = actual.get(i).getSourceId();
            String parsedDest = actual.get(i).getDestinationId();
            final int indexSource = parsedSource.lastIndexOf(':');
            final int indexDest = parsedDest.lastIndexOf(':');
            if (indexSource >= 0) {
                parsedSource = parsedSource.substring(indexSource + 1, parsedSource.length());
            }
            if (indexDest >= 0) {
                parsedDest = parsedDest.substring(indexDest + 1, parsedDest.length());
            }
            assertEquals(sourceId, Integer.parseInt(parsedSource));
            assertEquals(expectedIds.get(j + 1).intValue(), Integer.parseInt(parsedDest));
            assertEquals(expectedIds.get(j + 2).intValue(), actual.get(i).getSourcePort());
            assertEquals(expectedIds.get(j + 3).intValue(), actual.get(i).getDestinationPort());
            if (sourceId == -1) {
                assertFalse(actual.get(i).getSourceNode().isPresent());
            } else {
                assertTrue(actual.get(i).getSourceNode().isPresent());
            }
            if (destId == -1) {
                assertFalse(actual.get(i).getDestinationNode().isPresent());
            } else {
                assertTrue(actual.get(i).getDestinationNode().isPresent());
            }
        }
    }

    private static List<Integer> readConnectionIds(final List<String> readLines) throws Exception {
        final List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < readLines.size(); i++) {
            final String line = readLines.get(i);
            if (line.contains("connection_")) {
                final String sourceId = readLines.get(i + 1).substring(readLines.get(i + 1).indexOf("value=\"") + 7,
                    readLines.get(i + 1).lastIndexOf('"'));
                final String destId = readLines.get(i + 2).substring(readLines.get(i + 2).indexOf("value=\"") + 7,
                    readLines.get(i + 2).lastIndexOf('"'));
                final String sourcePort = readLines.get(i + 3).substring(readLines.get(i + 3).indexOf("value=\"") + 7,
                    readLines.get(i + 3).lastIndexOf('"'));
                final String destPort = readLines.get(i + 4).substring(readLines.get(i + 4).indexOf("value=\"") + 7,
                    readLines.get(i + 4).lastIndexOf('"'));
                ids.add(Integer.parseInt(sourceId));
                ids.add(Integer.parseInt(destId));
                ids.add(Integer.parseInt(sourcePort));
                ids.add(Integer.parseInt(destPort));
                i += 4;
            }
        }
        return ids;
    }

    private static AuthorInformation readAuthorInformation(final List<String> readWorkflowLines)
        throws IOException, ParseException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        final String author = XMLUtils.unescape(parseValue(readWorkflowLines, "authored-by"));
        final String authoredString = parseValue(readWorkflowLines, "authored-when");
        final String lastEditor = XMLUtils.unescape(parseValue(readWorkflowLines, "lastEdited-by"));
        final String lastEditedString = parseValue(readWorkflowLines, "lastEdited-when");
        Date lastEdited = null;
        if (lastEditedString != null && !lastEditedString.isEmpty()) {
            lastEdited = df.parse(lastEditedString);
        }

        if (author == null || authoredString == null) {
            return AuthorInformation.UNKNOWN;
        }
        return new AuthorInformation(author, df.parse(authoredString), Optional.ofNullable(lastEditor),
            Optional.ofNullable(lastEdited));
    }

    private static TemplateInformation readTemplateInformation(final List<String> readTemplateLines)
        throws IOException, ParseException {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String role = parseValue(readTemplateLines, "role");
        final Date timeStamp = df.parse(parseValue(readTemplateLines, "timestamp"));
        final Optional<String> sourceURI = Optional.ofNullable(parseValue(readTemplateLines, "sourceURI"));
        final String type = parseValue(readTemplateLines, "templateType");

        return new TemplateInformation(role, timeStamp, sourceURI, type);
    }

    private static void assertEquivalentAnnotations(final Optional<List<String>> expected,
        final Optional<List<String>> actual) {
        assertEquals(expected.isPresent(), actual.isPresent());
        if (!expected.isPresent() || !actual.isPresent()) {
            return;
        }
        final List<String> e = expected.get();
        final List<String> a = actual.get();
        assertEquals(e.size(), a.size());
        for (int i = 0; i < e.size(); i++) {
            assertEquals(e.get(i), a.get(i));
        }
    }

    private static Optional<List<String>> readAnnotations(final List<String> readLines) throws Exception {
        final List<String> annotations = new ArrayList<>();
        for (int i = 0; i < readLines.size(); i++) {
            final String line = readLines.get(i);
            if (line.contains("key=\"annotation_")) {
                i++;
                final String text = readLines.get(i).substring(readLines.get(i).indexOf("value=\"") + 7,
                    readLines.get(i).indexOf("\"/>"));
                annotations.add(XMLUtils.unescape(text));
            }
        }
        return annotations.isEmpty() ? Optional.empty() : Optional.ofNullable(annotations);
    }

    private static String parseValue(final List<String> lines, final String filter) {
        final Optional<String> potentialMatch = lines.stream().filter(line -> line.contains(filter)).findFirst();
        if (!potentialMatch.isPresent()) {
            return null;
        }
        final String matchedLine = potentialMatch.get();
        final String value = matchedLine.substring(matchedLine.indexOf("value=\"") + 7, matchedLine.lastIndexOf('"'));
        return value.isEmpty() ? null : StringEscapeUtils.unescapeXml(value);
    }

    private static String parseWorkflowSetMeta(final String fieldName, final List<String> lines) {
        String value = "";
        boolean read = false;
        for (String line : lines) {
            line += "\n";
            int start = 0;
            int end = line.length();
            if (line.contains("name=\"" + fieldName + "\"")) {
                read = true;
            }
            if (line.contains("<element")) {
                start = line.indexOf(">") + 1;
            }
            if (line.contains("</element>")) {
                end = line.indexOf("</");
            }
            if (read) {
                String piece = line.substring(start, end);
                value += piece;
                if (end != line.length()) {
                    return value;
                }
            }
        }
        return null;
    }
}
