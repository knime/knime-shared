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
 *   Sep 14, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.util.LoadVersion;
import org.knime.core.util.Version;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Reader for workflow metadata.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
public class Workflowalizer {

    /**
     * Reads the workflow in the given directory. All node and workflow fields will be read.
     *
     * @param path the workflow directory
     * @return the workflow metadata
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidSettingsException
     * @throws ParseException
     * @throws URISyntaxException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     */
    public static TopLevelWorkflowMetadata readWorkflow(final Path path)
        throws FileNotFoundException, IOException, InvalidSettingsException, ParseException, URISyntaxException,
        XPathExpressionException, ParserConfigurationException, SAXException {
        return readWorkflow(path, WorkflowalizerConfiguration.builder().readAll().build());
    }

    /**
     * Reads the workflow in the given directory, only the requested fields will be populated.
     *
     * @param path the workflow directory, or zip file containing the workflow. If the zip contains multiple workflows
     *            only the first will be read.
     * @param config the {@link WorkflowalizerConfiguration}, this cannot be {@code null}
     * @return the workflow metadata
     * @throws IOException
     * @throws InvalidSettingsException
     * @throws ParseException
     * @throws URISyntaxException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     */
    public static TopLevelWorkflowMetadata readWorkflow(final Path path, final WorkflowalizerConfiguration config)
        throws IOException, InvalidSettingsException, ParseException, URISyntaxException, XPathExpressionException,
        ParserConfigurationException, SAXException {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }

        if (!Files.exists(path)) {
            throw new FileNotFoundException("File does not exist at path " + path);
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path is not a directory: " + path);
        }
        // Validate if it is a workflow
        final Path workflowPath = path.resolve("workflow.knime");
        if (Files.exists(path.resolve("template.knime"))) {
            throw new IllegalArgumentException(path + " is a template, not a workflow");
        }
        if (!Files.exists(workflowPath)) {
            throw new IllegalArgumentException(path + " is not a workflow");
        }
        return readTopLevelWorkflow(path.toAbsolutePath().toString(), config);
    }

    // -- Helper methods --

    private static WorkflowSetMeta readWorkflowSetMeta(final InputStream is, final WorkflowalizerConfiguration config)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.parse(is);
        final XPathFactory xPathfactory = XPathFactory.newInstance();
        final XPath xpath = xPathfactory.newXPath();

        Optional<String> optionalAuthor = null;
        if (config.parseWorkflowSetAuthor()) {
            final XPathExpression authorExpr = xpath.compile("//KNIMEMetaInfo/element[@name='Author']");
            final String author = (String)authorExpr.evaluate(doc, XPathConstants.STRING);
            optionalAuthor = author.isEmpty() ? Optional.empty() : Optional.of(author);
        }
        Optional<String> optionalComments = null;
        if (config.parseWorkflowSetComments()) {
            final XPathExpression commentsExpr = xpath.compile("//KNIMEMetaInfo/element[@name='Comments']");
            final String comments = (String)commentsExpr.evaluate(doc, XPathConstants.STRING);
            optionalComments = comments.isEmpty() ? Optional.empty() : Optional.of(comments);
        }
        return new WorkflowSetMeta(optionalAuthor, optionalComments);
    }

    private static WorkflowParser getParser(final String version) {
        final Optional<LoadVersion> versionOptional = LoadVersion.get(version);
        if (!versionOptional.isPresent()) {
            throw new IllegalArgumentException("Unknown version: " + version);
        }
        final LoadVersion loadVersion = versionOptional.get();

        WorkflowParser parser = null;
        if (loadVersion.equals(LoadVersion.V3060Pre)) {
            parser = new WorkflowParserV3060Pre();
        } else {
            throw new IllegalArgumentException("Unsupported workflow version: " + loadVersion.getVersionString());
        }
        return parser;
    }

    private static TopLevelWorkflowMetadata readTopLevelWorkflow(final String path,
        final WorkflowalizerConfiguration wc) throws FileNotFoundException, IOException, InvalidSettingsException,
        ParseException, XPathExpressionException, ParserConfigurationException, SAXException {
        // Reading workflow.knime
        MetadataConfig workflowKnime = readFile(Paths.get(path, "workflow.knime"), "workflow.knime");

        // Version checking
        final String readVersion = workflowKnime.getString("version");
        final WorkflowParser parser = getParser(readVersion);

        // Read workflow
        final TopLevelWorkflowMetadataBuilder builder = new TopLevelWorkflowMetadataBuilder();

        final WorkflowFields wf = wc.createWorkflowFields();
        builder.setWorkflowFields(wf);
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, path);

        if (wc.parseAuthor()) {
            final String author = parser.getAuthorName(workflowKnime);
            builder.setAuthor(author);
        }
        if (wc.parseAuthorDate()) {
            final Date authoredDate = parser.getAuthoredDate(workflowKnime);
            builder.setAuthorDate(authoredDate);
        }
        if (wc.parseLastEditedDate()) {
            final Optional<Date> lastEditedDate = parser.getEditedDate(workflowKnime);
            builder.setLastEditDate(lastEditedDate);
        }
        if (wc.parseLastEditor()) {
            final Optional<String> lastEditor = parser.getEditorName(workflowKnime);
            builder.setLastEditor(lastEditor);
        }
        if (wc.parseArtifacts()) {
            final Optional<Collection<String>> artifactsFiles = artifactsFiles(parser, Paths.get(path));
            builder.setArtifactsFileNames(artifactsFiles);
        }
        if (wc.parseSVG()) {
            final Optional<String> svg = svgFile(parser, Paths.get(path));
            builder.setWorkflowSVGFile(svg);
        }
        if ((wc.parseWorkflowSetAuthor() || wc.parseWorkflowSetComments())) {
            Optional<WorkflowSetMeta> wsa = null;
            final Path workflowsetPath = Paths.get(path, parser.getWorkflowSetMetaFileName());
            if (!Files.exists(workflowsetPath)) {
                wsa = Optional.empty();
            } else {
                try (final InputStream is = Files.newInputStream(workflowsetPath)) {
                    wsa = Optional.ofNullable(readWorkflowSetMeta(is, wc));
                }
            }
            builder.setWorkflowSetMeta(wsa);
        }

        return builder.build(wc);
    }

    private static Map<Integer, NodeMetadata> readNodes(final String currentWorkflowDirectory,
        final List<ConfigBase> configs, final WorkflowalizerConfiguration wc, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
        final Map<Integer, NodeMetadata> map = new HashMap<>();
        for (final ConfigBase config : configs) {
            final String type = parser.getType(config);
            NodeMetadata n = null;
            int id = -1;
            if (type.equals("NativeNode")) {
                n = readNativeNode(currentWorkflowDirectory, config, wc, parser);
                id = wc.parseId() ? n.getNodeId() : parser.getId(config);
            } else if (type.equals("SubNode")) {
                n = readWrappedMetanode(currentWorkflowDirectory, config, wc, parser);
                id = wc.parseId() ? n.getNodeId() : parser.getId(config);
            } else if (type.equals("MetaNode")) {
                n = readMetanode(currentWorkflowDirectory, wc, config, parser);
                id = wc.parseId() ? n.getNodeId() : parser.getId(config);
            } else {
                throw new IllegalArgumentException("Unknown node type: " + type);
            }

            map.put(id, n);
        }
        return map;
    }

    private static MetanodeMetadata readMetanode(final String parentDirectory, final WorkflowalizerConfiguration wc,
        final ConfigBase configBase, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
        final String settings = parser.getNodeSettingsFilePath(configBase);
        final MetanodeMetadataBuilder builder = new MetanodeMetadataBuilder();

        final Path metaNodeFile = Paths.get(parentDirectory, settings);
        final String metaNodeDirectory = metaNodeFile.getParent().toAbsolutePath().toString();
        final MetadataConfig workflowKnime = readFile(metaNodeFile, metaNodeFile.getFileName().toString());

        final WorkflowFields wf = wc.createWorkflowFields();
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, metaNodeDirectory);
        builder.setWorkflowFields(wf);

        final NodeFields nf = wc.createNodeFields();
        populateNodeFields(nf, wc, parser, configBase);
        if (wc.parseAnnotationText()) {
            final Optional<String> annotationText = parser.getAnnotationText(workflowKnime, null);
            nf.setAnnotationText(annotationText);
        }
        builder.setNodeFields(nf);

        if (wc.parseTemplateLink()) {
            final Optional<String> templateLink = parser.getTemplateLink(workflowKnime);
            builder.setTemplateLink(templateLink);
        }

        return builder.build(wc);
    }

    private static SubnodeMetadata readWrappedMetanode(final String parentDirectory,
        final ConfigBase configBase, final WorkflowalizerConfiguration wc, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
        final String settings = parser.getNodeSettingsFilePath(configBase);
        final SubnodeMetadataBuilder builder = new SubnodeMetadataBuilder();

        final Path subnodeFile = Paths.get(parentDirectory, settings);
        final Path subnodeDirectoryPath = subnodeFile.getParent();
        final String subnodeDirectory = subnodeDirectoryPath.toAbsolutePath().toString();
        final MetadataConfig settingsXml = readFile(subnodeFile, subnodeFile.getFileName().toString());
        final MetadataConfig workflowKnime =
            readFile(subnodeDirectoryPath.resolve(parser.getWorkflowFileName()), parser.getWorkflowFileName());

        final WorkflowFields wf = wc.createWorkflowFields();
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, subnodeDirectory);
        builder.setWorkflowFields(wf);

        final SingleNodeFields snf = wc.createSingleNodeFields();
        populateSingleNodeFields(snf, wc, parser, settingsXml, configBase);
        builder.setSingleNodeFields(snf);

        if (wc.parseTemplateLink()) {
            final Optional<String> templateLink = parser.getTemplateLink(settingsXml);
            builder.setTemplateLink(templateLink);
        }

        return builder.build(wc);
    }

    private static NativeNodeMetadata readNativeNode(final String parentDirectory,
        final ConfigBase configBase, final WorkflowalizerConfiguration wc, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException {
        final String settings = parser.getNodeSettingsFilePath(configBase);
        final NativeNodeMetadataBuilder builder = new NativeNodeMetadataBuilder();

        final Path nodeFile = Paths.get(parentDirectory, settings);
        final MetadataConfig settingsXml = readFile(nodeFile, nodeFile.getFileName().toString());

        final SingleNodeFields snf = wc.createSingleNodeFields();
        populateSingleNodeFields(snf, wc, parser, settingsXml, configBase);
        builder.setSingleNodeFields(snf);

        // read other fields
        if (wc.parseNodeName()) {
            final Optional<String> name = parser.getNodeName(settingsXml);
            builder.setNodeName(name);
        }
        if (wc.parseFactoryClass()) {
            final Optional<String> factoryClass = parser.getFactoryClass(settingsXml);
            builder.setFactoryClass(factoryClass);
        }
        if (wc.parseBundleName()) {
            final Optional<String> bundleName = parser.getBundleName(settingsXml);
            builder.setBundleName(bundleName);
        }
        if (wc.parseBundleSymbolicName()) {
            final Optional<String> bundleSymbolicName = parser.getBundleSymbolicName(settingsXml);
            builder.setBundleSymbolicName(bundleSymbolicName);
        }
        if (wc.parseBundleVendor()) {
            final Optional<String> bundleVendor = parser.getBundleVendor(settingsXml);
            builder.setBundleVendor(bundleVendor);
        }
        if (wc.parseBundleVersion()) {
            final Optional<Version> bundleVersion = parser.getBundleVersion(settingsXml);
            builder.setBundleVersion(bundleVersion);
        }
        if (wc.parseFeatureName()) {
            final Optional<String> featureName = parser.getFeatureName(settingsXml);
            builder.setFeatureName(featureName);
        }
        if (wc.parseFeatureSymbolicName()) {
            final Optional<String> featureSymbolicName = parser.getFeatureSymbolicName(settingsXml);
            builder.setFeatureSymbolicName(featureSymbolicName);
        }
        if (wc.parseFeatureVendor()) {
            final Optional<String> featureVendor = parser.getFeatureVendor(settingsXml);
            builder.setFeatureVendor(featureVendor);
        }
        if (wc.parseFeatureVersion()) {
            final Optional<Version> featureVersion = parser.getFeatureVersion(settingsXml);
            builder.setFeatureVersion(featureVersion);
        }

        return builder.build(wc);
    }

    // -- Populate methods --

    private static void populateWorkflowFields(final WorkflowFields wf, final WorkflowalizerConfiguration wc,
        final WorkflowParser parser, final ConfigBase workflowKnime, final ConfigBase templateKnime, final String path) throws InvalidSettingsException, ParseException, FileNotFoundException, IOException {
        if (wc.parseVersion()) {
            final Version version = parser.getVersion(workflowKnime, templateKnime);
            wf.setVersion(version);
        }
        if (wc.parseCreatedBy()) {
            final Version createdBy = parser.getCreatedBy(workflowKnime, templateKnime);
            wf.setCreatedBy(createdBy);
        }
        if (wc.parseName()) {
            final Optional<String> name = parser.getName(workflowKnime);
            wf.setName(name);
        }
        if (wc.parseCustomDescription()) {
            final Optional<String> customDescription = parser.getCustomDescription(workflowKnime);
            wf.setCustomDescription(customDescription);
        }
        if (wc.parseAnnotations()) {
            final Optional<List<String>> annotations = parser.getAnnotations(workflowKnime);
            wf.setAnnotations(annotations);
        }
        if (wc.parseNodes()) {
            final Map<Integer, NodeMetadata> nodes =
                readNodes(path, parser.getNodeConfigs(workflowKnime), wc, parser);
            wf.setNodes(new ArrayList<>(nodes.values()));
            if (wc.parseConnections()) {
                final List<NodeConnection> connections = parser.getConnections(workflowKnime, nodes);
                wf.setConnections(connections);
            }
        }
        if (wc.parseUnexpectedFiles()) {
            final Collection<String> files = findUnexpectedFiles(parser.getExpectedFileNames(workflowKnime), Paths.get(path));
            wf.setUnexpectedFileNames(files);
        }
    }

    private static void populateNodeFields(final NodeFields nf, final WorkflowalizerConfiguration wc,
        final WorkflowParser parser, final ConfigBase nodeConfig) throws InvalidSettingsException {
        if (wc.parseId()) {
            final int id = parser.getId(nodeConfig);
            nf.setId(id);
        }
        if (wc.parseType()) {
            final String type = parser.getType(nodeConfig);
            nf.setType(type);
        }
    }

    private static void populateSingleNodeFields(final SingleNodeFields snf, final WorkflowalizerConfiguration wc,
        final WorkflowParser parser, final ConfigBase settingsXml, final ConfigBase nodeConfig)
        throws InvalidSettingsException {
        populateNodeFields(snf, wc, parser, nodeConfig);
        if (wc.parseModelParameters()) {
            final Optional<ConfigBase> modelParameters = parser.getModelParameters(settingsXml);
            if (modelParameters.isPresent()) {
                // Remove link to parent
                modelParameters.get().setParent(null);
            }
            snf.setModelParameters(modelParameters);
        }
        if (wc.parseNodeCustomDescription()) {
            final Optional<String> customDescription = parser.getCustomNodeDescription(settingsXml);
            snf.setCustomDescription(customDescription);
        }
        if (wc.parseAnnotationText()) {
            final Optional<String> annotationText = parser.getAnnotationText(null, settingsXml);
            snf.setAnnotationText(annotationText);
        }
    }

    // -- File methods --

    private static MetadataConfig readFile(final Path f, final String key) throws FileNotFoundException, IOException {
        try (final InputStream s = Files.newInputStream(f)) {
            final MetadataConfig c = new MetadataConfig(key);
            c.load(s);
            return c;
        }
    }

    private static Collection<String> findUnexpectedFiles(final Collection<String> expectedFiles, final Path path)
        throws InvalidSettingsException, IOException {
        final List<String> unexpectedFiles = new ArrayList<>();
        final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                throws IOException {
                if (expectedFiles.contains(dir.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                if (!expectedFiles.contains(file.getFileName().toString())) {
                    unexpectedFiles.add(file.toAbsolutePath().toString());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(path, visitor);
        return unexpectedFiles;
    }

    private static Optional<Collection<String>> artifactsFiles(final WorkflowParser parser,
        final Path workflowDirectory) throws IOException {
        final Path artifactsDir = workflowDirectory.resolve(parser.getArtifactsDirectoryName());
        if (!Files.exists(artifactsDir)) {
            return Optional.empty();
        }
        if (!Files.isDirectory(artifactsDir)) {
            throw new IllegalArgumentException(parser.getArtifactsDirectoryName() + " is not a directory");
        }
        if (Files.list(artifactsDir).count() <= 0) {
            return Optional.of(Collections.emptyList());
        }
        final Collection<String> files = new ArrayList<>();
        final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                files.add(file.toAbsolutePath().toString());
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(artifactsDir, visitor);
        return Optional.of(files);
    }

    private static Optional<String> svgFile(final WorkflowParser parser, final Path workflowDirectory)
        throws IOException {
        final Path svg = workflowDirectory.resolve(parser.getWorkflowSVGFileName());
        if (!Files.exists(svg, LinkOption.NOFOLLOW_LINKS)) {
            return Optional.empty();
        }
        if (!Files.probeContentType(svg).toLowerCase().contains("svg")) {
            throw new IllegalArgumentException(parser.getWorkflowSVGFileName() + " is not an SVG");
        }
        return Optional.of(svg.toAbsolutePath().toString());
    }

}
