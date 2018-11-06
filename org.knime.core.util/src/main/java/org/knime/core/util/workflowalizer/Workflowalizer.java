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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @since 5.10
 */
public final class Workflowalizer {

    /**
     * Reads the workflowset meta file.
     *
     * @param workflowsetMetaFile path to "workflowset.meta" file
     * @return the metadata contained within the file as {@link WorkflowSetMeta}
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    public static WorkflowSetMeta readWorkflowGroup(final Path workflowsetMetaFile)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        if (isZip(workflowsetMetaFile)) {
            try (final ZipFile zip = new ZipFile(workflowsetMetaFile.toAbsolutePath().toString())) {
                final String workflowPath = findFirstWorkflowGroup(zip);
                if (workflowPath == null) {
                    throw new IllegalArgumentException(
                        "Zip file does not contain a workflow group: " + workflowsetMetaFile);
                }
                try (final InputStream is = zip.getInputStream(zip.getEntry(workflowPath))) {
                    return readWorkflowSetMeta(is);
                }
            }
        }

        if (!Files.exists(workflowsetMetaFile)) {
            throw new FileNotFoundException(workflowsetMetaFile + " does not exist");
        }
        if (Files.isDirectory(workflowsetMetaFile)) {
            throw new IllegalArgumentException(workflowsetMetaFile + " is a directory");
        }
        try (final InputStream is = Files.newInputStream(workflowsetMetaFile)) {
            return readWorkflowSetMeta(is);
        }
    }

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
    public static WorkflowMetadata readWorkflow(final Path path)
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
    public static WorkflowMetadata readWorkflow(final Path path, final WorkflowalizerConfiguration config)
        throws IOException, InvalidSettingsException, ParseException, URISyntaxException, XPathExpressionException,
        ParserConfigurationException, SAXException {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }

        if (!Files.exists(path)) {
            throw new FileNotFoundException("File does not exist at path " + path);
        }
        if (isZip(path)) {
            try (final ZipFile zip = new ZipFile(path.toAbsolutePath().toString())) {
                final String workflowPath = findFirstWorkflow(zip);
                if (workflowPath == null) {
                    throw new IllegalArgumentException("Zip file does not contain a workflow: " + path);
                }
                return readTopLevelWorkflow(workflowPath, zip, config);
            }
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
        return readTopLevelWorkflow(path.toAbsolutePath().toString(), null, config);
    }

    /**
     * Reads the template in the given directory/zip file, all the fields will be read.
     *
     * @param path the template directory, or zip file containing the template. If the zip contains multiple template
     *            only the first will be read.
     * @return the template metadata
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvalidSettingsException
     * @throws ParseException
     */
    public static TemplateMetadata readTemplate(final Path path)
        throws IOException, URISyntaxException, InvalidSettingsException, ParseException {
        return readTemplate(path, WorkflowalizerConfiguration.builder().readAll().build());
    }

    /**
     * Reads the template in the given directory/zip, only the requested fields will be populated.
     *
     * @param path the template directory, or zip file containing the template. If the zip contains multiple templates
     *            only the first will be read.
     * @param config the {@link WorkflowalizerConfiguration}, this cannot be {@code null}
     * @return the template metadata
     * @throws IOException
     * @throws InvalidSettingsException
     * @throws ParseException
     * @throws URISyntaxException
     */
    public static TemplateMetadata readTemplate(final Path path, final WorkflowalizerConfiguration config)
        throws IOException, URISyntaxException, InvalidSettingsException, ParseException {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File does not exist at path " + path);
        }
        if (isZip(path)) {
            try (final ZipFile zip = new ZipFile(path.toAbsolutePath().toString())) {
                final String zipPath = findFirstTemplate(zip);
                if (zipPath == null) {
                    throw new IllegalArgumentException("Zip file does not contain a template: " + path);
                }
                return readTemplateMetadata(zipPath, zip, config);
            }
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path is not a directory: " + path);
        }
        // validate it is a template
        final Path workflowPath = path.resolve("workflow.knime");
        final Path templatePath = path.resolve("template.knime");
        if (!Files.exists(templatePath) && Files.exists(workflowPath)) {
            throw new IllegalArgumentException(path + " is a workflow, not a template");
        }
        if (!Files.exists(templatePath) || !Files.exists(workflowPath)) {
            throw new IllegalArgumentException(path + " is not a template");
        }
        return readTemplateMetadata(path.toAbsolutePath().toString(), null, config);
    }

    // -- Helper methods --

    private static WorkflowSetMeta readWorkflowSetMeta(final InputStream is)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.parse(is);
        final XPathFactory xPathfactory = XPathFactory.newInstance();
        final XPath xpath = xPathfactory.newXPath();

        Optional<String> optionalAuthor = null;
        final XPathExpression authorExpr = xpath.compile("//KNIMEMetaInfo/element[@name='Author']");
        final String author = (String)authorExpr.evaluate(doc, XPathConstants.STRING);
        optionalAuthor = author.isEmpty() ? Optional.empty() : Optional.of(author);
        Optional<String> optionalComments = null;
        final XPathExpression commentsExpr = xpath.compile("//KNIMEMetaInfo/element[@name='Comments']");
        final String comments = (String)commentsExpr.evaluate(doc, XPathConstants.STRING);
        optionalComments = comments.isEmpty() ? Optional.empty() : Optional.of(comments);
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

    private static WorkflowMetadata readTopLevelWorkflow(final String path, final ZipFile zip,
        final WorkflowalizerConfiguration wc) throws FileNotFoundException, IOException, InvalidSettingsException,
        ParseException, XPathExpressionException, ParserConfigurationException, SAXException {
        // Reading workflow.knime
        MetadataConfig workflowKnime = null;
        if (zip == null) {
            workflowKnime = readFile(Paths.get(path, "workflow.knime"));
        } else {
            workflowKnime = readFile(path + "workflow.knime", zip);
        }

        // Version checking
        final String readVersion = workflowKnime.getString("version");
        final WorkflowParser parser = getParser(readVersion);

        // Read workflow
        final WorkflowMetadataBuilder builder = new WorkflowMetadataBuilder();

        final WorkflowFields wf = wc.createWorkflowFields();
        builder.setWorkflowFields(wf);
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, path, zip);

        final String author = parser.getAuthorName(workflowKnime);
        builder.setAuthor(author);

        final Date authoredDate = parser.getAuthoredDate(workflowKnime);
        builder.setAuthorDate(authoredDate);

        final Optional<Date> lastEditedDate = parser.getEditedDate(workflowKnime);
        builder.setLastEditDate(lastEditedDate);

        final Optional<String> lastEditor = parser.getEditorName(workflowKnime);
        builder.setLastEditor(lastEditor);

        Optional<Collection<String>> artifactsFiles = null;
        if (zip == null) {
            artifactsFiles = artifactsFiles(parser, Paths.get(path));
        } else {
            artifactsFiles = artifactsFiles(parser, path, zip);
        }
        builder.setArtifactsFileNames(artifactsFiles);

        Optional<String> svg = null;
        if (zip == null) {
            svg = svgFile(parser, Paths.get(path));
        } else {
            svg = svgFile(parser, path, zip);
        }
        builder.setWorkflowSVGFile(svg);

        if (wc.parseWorkflowMeta()) {
            Optional<WorkflowSetMeta> wsa = null;
            if (zip == null) {
                final Path workflowsetPath = Paths.get(path, parser.getWorkflowSetMetaFileName());
                if (!Files.exists(workflowsetPath)) {
                    wsa = Optional.empty();
                } else {
                    try (final InputStream is = Files.newInputStream(workflowsetPath)) {
                        wsa = Optional.ofNullable(readWorkflowSetMeta(is));
                    }
                }
            } else {
                final ZipEntry entry = zip.getEntry(path + parser.getWorkflowSetMetaFileName());
                if (entry == null) {
                    wsa = Optional.empty();
                } else {
                    try (final InputStream is = zip.getInputStream(entry)) {
                        wsa = Optional.ofNullable(readWorkflowSetMeta(is));
                    }
                }
            }
            builder.setWorkflowSetMeta(wsa);
        }

        final List<String> credentials = parser.getWorkflowCredentialName(workflowKnime);
        builder.setWorkflowCredentialsNames(credentials);

        final List<String> variables = parser.getWorkflowVariables(workflowKnime);
        builder.setWorkflowVariables(variables);

        return builder.build(wc);
    }

    private static TemplateMetadata readTemplateMetadata(final String path, final ZipFile zip,
        final WorkflowalizerConfiguration wc)
        throws InvalidSettingsException, FileNotFoundException, ParseException, IOException {
        // Reading files
        // TODO: Is the template file always template.knime?
        MetadataConfig workflowKnime = null;
        MetadataConfig templateKnime = null;
        if (zip == null) {
            workflowKnime = readFile(Paths.get(path, "workflow.knime"));
            templateKnime = readFile(Paths.get(path, "template.knime"));
        } else {
            workflowKnime = readFile(path + "workflow.knime", zip);
            templateKnime = readFile(path + "template.knime", zip);
        }

        // Version checking
        final String readVersion = templateKnime.getString("version");
        final WorkflowParser parser = getParser(readVersion);

        // Read template
        final TemplateMetadataBuilder builder = new TemplateMetadataBuilder();

        final WorkflowFields wf = wc.createWorkflowFields();
        builder.setWorkflowFields(wf);
        populateWorkflowFields(wf, wc, parser, workflowKnime, templateKnime, path, zip);

        final String author = parser.getAuthorName(workflowKnime);
        builder.setAuthor(author);

        final Date authoredDate = parser.getAuthoredDate(workflowKnime);
        builder.setAuthorDate(authoredDate);

        final Optional<Date> lastEditedDate = parser.getEditedDate(workflowKnime);
        builder.setLastEditDate(lastEditedDate);

        final Optional<String> lastEditor = parser.getEditorName(workflowKnime);
        builder.setLastEditor(lastEditor);

        final String role = parser.getRole(templateKnime);
        builder.setRole(role);

        final Date timeStamp = parser.getTimeStamp(templateKnime);
        builder.setTimeStamp(timeStamp);

        final Optional<String> sourceURI = parser.getSourceURI(templateKnime);
        builder.setSourceURI(sourceURI);

        final String templateType = parser.getTemplateType(templateKnime);
        builder.setType(templateType);

        return builder.build(wc);
    }

    private static Map<Integer, NodeMetadata> readNodes(final String currentWorkflowDirectory, final ZipFile zip,
        final List<ConfigBase> configs, final WorkflowalizerConfiguration wc, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
        final Map<Integer, NodeMetadata> map = new HashMap<>();
        for (final ConfigBase config : configs) {
            final String type = parser.getType(config);
            NodeMetadata n = null;
            if (type.equals("NativeNode")) {
                n = readNativeNode(currentWorkflowDirectory, zip, config, wc, parser);
            } else if (type.equals("SubNode")) {
                n = readWrappedMetanode(currentWorkflowDirectory, zip, config, wc, parser);
            } else if (type.equals("MetaNode")) {
                n = readMetanode(currentWorkflowDirectory, zip, wc, config, parser);
            } else {
                throw new IllegalArgumentException("Unknown node type: " + type);
            }

            map.put(n.getNodeId(), n);
        }
        return map;
    }

    private static MetanodeMetadata readMetanode(final String parentDirectory, final ZipFile zip,
        final WorkflowalizerConfiguration wc, final ConfigBase configBase, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
        final String settings = parser.getNodeSettingsFilePath(configBase);
        final MetanodeMetadataBuilder builder = new MetanodeMetadataBuilder();

        MetadataConfig workflowKnime = null;
        String metaNodeDirectory = null;
        if (zip == null) {
            final Path metaNodeFile = Paths.get(parentDirectory, settings);
            metaNodeDirectory = metaNodeFile.getParent().toAbsolutePath().toString();
            workflowKnime = readFile(metaNodeFile);
        } else {
            final String nodePath = parentDirectory + settings;
            workflowKnime = readFile(nodePath, zip);
            metaNodeDirectory = nodePath.substring(0, nodePath.lastIndexOf("/") + 1);
        }

        final WorkflowFields wf = wc.createWorkflowFields();
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, metaNodeDirectory, zip);
        builder.setWorkflowFields(wf);

        final NodeFields nf = wc.createNodeFields();
        populateNodeFields(nf, parser, configBase);

        final Optional<String> annotationText = parser.getAnnotationText(workflowKnime, null);
        nf.setAnnotationText(annotationText);
        builder.setNodeFields(nf);

        final Optional<String> templateLink = parser.getTemplateLink(workflowKnime);
        builder.setTemplateLink(templateLink);

        return builder.build(wc);
    }

    private static SubnodeMetadata readWrappedMetanode(final String parentDirectory, final ZipFile zip,
        final ConfigBase configBase, final WorkflowalizerConfiguration wc, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
        final String settings = parser.getNodeSettingsFilePath(configBase);
        final SubnodeMetadataBuilder builder = new SubnodeMetadataBuilder();

        MetadataConfig workflowKnime = null;
        MetadataConfig settingsXml = null;
        String subnodeDirectory = null;
        if (zip == null) {
            final Path subnodeFile = Paths.get(parentDirectory, settings);
            final Path subnodeDirectoryPath = subnodeFile.getParent();
            subnodeDirectory = subnodeDirectoryPath.toAbsolutePath().toString();
            settingsXml = readFile(subnodeFile);
            workflowKnime =
                readFile(subnodeDirectoryPath.resolve(parser.getWorkflowFileName()));
        } else {
            final String nodePath = parentDirectory + settings;
            subnodeDirectory = nodePath.substring(0, nodePath.lastIndexOf("/") + 1);
            settingsXml = readFile(nodePath, zip);
            workflowKnime = readFile(subnodeDirectory + parser.getWorkflowFileName(), zip);
        }

        final WorkflowFields wf = wc.createWorkflowFields();
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, subnodeDirectory, zip);
        builder.setWorkflowFields(wf);

        final SingleNodeFields snf = wc.createSingleNodeFields();
        populateSingleNodeFields(snf, wc, parser, settingsXml, configBase);
        builder.setSingleNodeFields(snf);

        final Optional<String> templateLink = parser.getTemplateLink(settingsXml);
        builder.setTemplateLink(templateLink);

        return builder.build(wc);
    }

    private static NativeNodeMetadata readNativeNode(final String parentDirectory, final ZipFile zip,
        final ConfigBase configBase, final WorkflowalizerConfiguration wc, final WorkflowParser parser)
        throws InvalidSettingsException, FileNotFoundException, IOException {
        final String settings = parser.getNodeSettingsFilePath(configBase);
        final NativeNodeMetadataBuilder builder = new NativeNodeMetadataBuilder();

        MetadataConfig settingsXml = null;
        if (zip == null) {
            final Path nodeFile = Paths.get(parentDirectory, settings);
            settingsXml = readFile(nodeFile);
        } else {
            settingsXml = readFile(parentDirectory + settings, zip);
        }

        final SingleNodeFields snf = wc.createSingleNodeFields();
        populateSingleNodeFields(snf, wc, parser, settingsXml, configBase);
        builder.setSingleNodeFields(snf);

        final Optional<String> name = parser.getNodeName(settingsXml);
        builder.setNodeName(name);

        final Optional<String> factoryClass = parser.getFactoryClass(settingsXml);
        builder.setFactoryClass(factoryClass);

        final Optional<String> bundleName = parser.getBundleName(settingsXml);
        builder.setBundleName(bundleName);

        final Optional<String> bundleSymbolicName = parser.getBundleSymbolicName(settingsXml);
        builder.setBundleSymbolicName(bundleSymbolicName);

        final Optional<String> bundleVendor = parser.getBundleVendor(settingsXml);
        builder.setBundleVendor(bundleVendor);

        final Optional<Version> bundleVersion = parser.getBundleVersion(settingsXml);
        builder.setBundleVersion(bundleVersion);

        final Optional<String> featureName = parser.getFeatureName(settingsXml);
        builder.setFeatureName(featureName);

        final Optional<String> featureSymbolicName = parser.getFeatureSymbolicName(settingsXml);
        builder.setFeatureSymbolicName(featureSymbolicName);

        final Optional<String> featureVendor = parser.getFeatureVendor(settingsXml);
        builder.setFeatureVendor(featureVendor);

        final Optional<Version> featureVersion = parser.getFeatureVersion(settingsXml);
        builder.setFeatureVersion(featureVersion);

        return builder.build();
    }

    // -- Populate methods --

    private static void populateWorkflowFields(final WorkflowFields wf, final WorkflowalizerConfiguration wc,
        final WorkflowParser parser, final ConfigBase workflowKnime, final ConfigBase templateKnime, final String path,
        final ZipFile zip) throws InvalidSettingsException, ParseException, FileNotFoundException, IOException {
        final Version version = parser.getVersion(workflowKnime, templateKnime);
        wf.setVersion(version);

        final Version createdBy = parser.getCreatedBy(workflowKnime, templateKnime);
        wf.setCreatedBy(createdBy);

        final Optional<String> name = parser.getName(workflowKnime);
        wf.setName(name);

        final Optional<String> customDescription = parser.getCustomDescription(workflowKnime);
        wf.setCustomDescription(customDescription);

        final Optional<List<String>> annotations = parser.getAnnotations(workflowKnime);
        wf.setAnnotations(annotations);
        if (wc.parseNodes()) {
            final Map<Integer, NodeMetadata> nodes =
                readNodes(path, zip, parser.getNodeConfigs(workflowKnime), wc, parser);
            wf.setNodes(new ArrayList<>(nodes.values()));
            if (wc.parseConnections()) {
                final List<NodeConnection> connections = parser.getConnections(workflowKnime, nodes);
                wf.setConnections(connections);
            }
        }
        if (wc.parseUnexpectedFiles()) {
            Collection<String> files = null;
            if (zip == null) {
                files = findUnexpectedFiles(parser.getExpectedFileNames(workflowKnime), Paths.get(path));
            } else {
                files = findUnexpectedFiles(parser.getExpectedFileNames(workflowKnime), path, zip);
            }
            wf.setUnexpectedFileNames(files);
        }
    }

    private static void populateNodeFields(final NodeFields nf, final WorkflowParser parser,
        final ConfigBase nodeConfig) throws InvalidSettingsException {
        final int id = parser.getId(nodeConfig);
        nf.setId(id);

        final String type = parser.getType(nodeConfig);
        nf.setType(type);
    }

    private static void populateSingleNodeFields(final SingleNodeFields snf, final WorkflowalizerConfiguration wc,
        final WorkflowParser parser, final ConfigBase settingsXml, final ConfigBase nodeConfig)
        throws InvalidSettingsException {
        populateNodeFields(snf, parser, nodeConfig);
        if (wc.parseModelParameters()) {
            final Optional<ConfigBase> modelParameters = parser.getModelParameters(settingsXml);
            if (modelParameters.isPresent()) {
                // Remove link to parent (memory optimization)
                modelParameters.get().setParent(null);
            }
            snf.setModelParameters(modelParameters);
        }

        final Optional<String> customDescription = parser.getCustomNodeDescription(settingsXml);
        snf.setCustomDescription(customDescription);

        final Optional<String> annotationText = parser.getAnnotationText(null, settingsXml);
        snf.setAnnotationText(annotationText);
    }

    // -- File methods --

    private static MetadataConfig readFile(final Path f) throws FileNotFoundException, IOException {
        try (final InputStream s = Files.newInputStream(f)) {
            final MetadataConfig c = new MetadataConfig("ignored");
            c.load(s);
            return c;
        }
    }

    private static MetadataConfig readFile(final String entry, final ZipFile zip) throws IOException {
        final ZipEntry e = zip.getEntry(entry);
        if (e == null) {
            throw new FileNotFoundException("Zip entry does not exist: " + entry);
        }
        try (final InputStream s = zip.getInputStream(e)) {
            final MetadataConfig c = new MetadataConfig("ignored");
            c.load(s);
            return c;
        }
    }

    private static boolean isZip(final Path path) throws FileNotFoundException, IOException {
        if (!Files.exists(path)) {
            return false;
        }
        if (Files.isDirectory(path)) {
            return false;
        }

        final File file = path.toFile();
        if (!FilenameUtils.getExtension(file.getName()).equals("zip")) {
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

    private static Collection<String> findUnexpectedFiles(final Collection<String> expectedFiles, final String path,
        final ZipFile zip) {
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        final List<String> files = new ArrayList<>();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final String name = entry.getName();
            if (!entry.isDirectory() && name.startsWith(path) && !name.equals(path)) {
                final String relativePath = name.substring(path.length(), name.length());
                boolean match = false;
                for (final String expected : expectedFiles) {
                    if (relativePath.contains(expected)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    files.add(name);
                }
            }
        }
        return files;
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

    private static Optional<Collection<String>> artifactsFiles(final WorkflowParser parser, final String path,
        final ZipFile zip) {
        String end = "";
        if (!parser.getArtifactsDirectoryName().endsWith("/")) {
            end = "/";
        }
        final String entryName = path + parser.getArtifactsDirectoryName() + end;
        final ZipEntry entry = zip.getEntry(entryName);
        if (entry == null) {
            return Optional.empty();
        }
        if (!entry.isDirectory()) {
            throw new IllegalArgumentException(entryName + " is not a directory");
        }
        final Collection<String> files = new ArrayList<>();
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry e = entries.nextElement();
            final String name = e.getName();
            if (name.startsWith(entryName) && !e.isDirectory()) {
                files.add(name);
            }
        }
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

    private static Optional<String> svgFile(final WorkflowParser parser, final String path, final ZipFile zip) {
        final ZipEntry svg = zip.getEntry(path + parser.getWorkflowSVGFileName());
        if (svg == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(svg.getName());
    }

    private static String findFirstWorkflow(final ZipFile zipFile) {
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

    private static String findFirstTemplate(final ZipFile zipFile) {
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        int numSlashes = Integer.MAX_VALUE;
        String template = null;
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            final String name = entry.getName();
            if (name.endsWith("template.knime")) {
                final int matches = StringUtils.countMatches(name, "/");
                if (matches < numSlashes) {
                    numSlashes = matches;
                    template = name.substring(0, name.length() - 14);
                }
            }
        }
        return template;
    }

    private static String findFirstWorkflowGroup(final ZipFile zipFile) {
        final List<String> workflows = new ArrayList<>();
        boolean isWorkflow = false;
        String workflowgroup = null;

        do {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            int numSlashes = Integer.MAX_VALUE;
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                final String name = entry.getName();
                if (name.contains("workflowset.meta") && !workflows.contains(name)) {
                    final int matches = StringUtils.countMatches(name, "/");
                    if (matches < numSlashes) {
                        numSlashes = matches;
                        workflowgroup = name;
                    }
                }
            }

            // check if it is a workflow
            isWorkflow = false;
            if (workflowgroup != null) {
                final String workflowGroupDir = workflowgroup.substring(0, workflowgroup.length() - 16);
                entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.equals(workflowGroupDir + "workflow.knime")) {
                        isWorkflow = true;
                        workflows.add(workflowgroup);
                        break;
                    }
                }
            }
        } while (isWorkflow);

        return workflowgroup;
    }

}
