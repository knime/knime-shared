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
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.base.ConfigBase;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.util.LoadVersion;
import org.knime.core.util.Version;
import org.knime.core.util.workflowalizer.NodeMetadata.NodeType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Reader for workflow metadata.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
public final class Workflowalizer {

    private static final Logger LOGGER = Logger.getLogger(Workflowalizer.class.getName());

    /**
     * Reads the repository item at the given path. All fields for the given item will be read.
     *
     * @param repoItem path to the directory of the repository item or zip file containing the item. If the zip contains
     *            multiple repository items only the first highest level item is read. In the event of ties the priority
     *            is workflow group, workflow, template
     * @return repository item pojo
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidSettingsException
     * @throws ParseException
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static RepositoryItemMetadata readRepositoryItem(final Path repoItem)
        throws FileNotFoundException, IOException, InvalidSettingsException, ParseException, XPathExpressionException,
        ParserConfigurationException, SAXException {
        return readRepositoryItem(repoItem, WorkflowalizerConfiguration.builder().readAll().build());
    }

    /**
     * Reads the repository item at the given path, only the requested fields will be populated.
     *
     * @param repoItem path to the directory of the repository item or zip file containing the item. If the zip contains
     *            multiple repository items only the first highest level item is read. In the event of ties the priority
     *            is workflow group, workflow, template
     * @param config the {@link WorkflowalizerConfiguration}, this cannot be {@code null}
     * @return repository item pojo
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidSettingsException
     * @throws ParseException
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static RepositoryItemMetadata readRepositoryItem(final Path repoItem,
        final WorkflowalizerConfiguration config) throws FileNotFoundException, IOException, InvalidSettingsException,
        ParseException, XPathExpressionException, ParserConfigurationException, SAXException {
        if (isZip(repoItem)) {
            try (final ZipFile zip = new ZipFile(repoItem.toAbsolutePath().toString())) {
                final String workflowGroupPath = findFirstWorkflowGroup(zip);
                final String workflowPath = findFirstWorkflow(zip);
                final String templatePath = findFirstTemplate(zip);

                CheckUtils.checkArgument(!(workflowGroupPath == null && workflowPath == null && templatePath == null),
                    "No template, workflow, or workflow group found in zip file at: " + repoItem);

                final int groupLevel =
                    workflowGroupPath == null ? Integer.MAX_VALUE : StringUtils.countMatches(workflowGroupPath, "/");
                final int workflowLevel =
                    workflowPath == null ? Integer.MAX_VALUE : StringUtils.countMatches(workflowPath, "/");
                final int templateLevel =
                    templatePath == null ? Integer.MAX_VALUE : StringUtils.countMatches(templatePath, "/");

                if (groupLevel <= workflowLevel && groupLevel <= templateLevel) {
                    try (final InputStream is = zip.getInputStream(zip.getEntry(workflowGroupPath))) {
                        return new WorkflowGroupMetadata(readWorkflowSetMeta(is));
                    }
                } else if (workflowLevel < groupLevel && workflowLevel <= templateLevel) {
                    return readTopLevelWorkflow(workflowPath, zip, config);
                }
                return readTemplateMetadata(templatePath, zip, config);
            }
        }
        CheckUtils.checkArgument(Files.exists(repoItem), repoItem + " does not exist");
        CheckUtils.checkArgument(Files.isDirectory(repoItem), repoItem + " is not a directory");

        final Path workflowSetMeta = repoItem.resolve("workflowset.meta");
        final boolean hasWorkflowKnime = Files.exists(repoItem.resolve("workflow.knime"));
        final boolean hasWorkflowSetMeta = Files.exists(workflowSetMeta);
        final boolean hasTemplateKnime = Files.exists(repoItem.resolve("template.knime"));
        if (hasTemplateKnime && hasWorkflowKnime) {
            return readTemplateMetadata(repoItem.toAbsolutePath().toString(), null, config);
        } else if (hasWorkflowKnime && !hasTemplateKnime) {
            return readTopLevelWorkflow(repoItem.toAbsolutePath().toString(), null, config);
        } else if (hasWorkflowSetMeta && !hasTemplateKnime && !hasWorkflowKnime) {
            try (final InputStream is = Files.newInputStream(workflowSetMeta)) {
                return new WorkflowGroupMetadata(readWorkflowSetMeta(is));
            }
        }
        throw new IllegalArgumentException("No template, workflow, or workflow group found at path: " + repoItem);
    }

    /**
     * Reads the "workflowset.meta" file at the given path.
     *
     * @param workflowsetmeta path to "workflowset.meta" file or directory/zip containing this file.
     * @return the metadata contained within the file as {@link WorkflowSetMeta}
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    public static WorkflowGroupMetadata readWorkflowGroup(final Path workflowsetmeta)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        if (isZip(workflowsetmeta)) {
            try (final ZipFile zip = new ZipFile(workflowsetmeta.toAbsolutePath().toString())) {
                final String workflowPath = findFirstWorkflowGroup(zip);
                CheckUtils.checkArgumentNotNull(workflowPath,
                    "Zip file does not contain a workflow group: " + workflowsetmeta);
                try (final InputStream is = zip.getInputStream(zip.getEntry(workflowPath))) {
                    return new WorkflowGroupMetadata(readWorkflowSetMeta(is));
                }
            }
        }

        CheckUtils.checkArgument(Files.exists(workflowsetmeta), workflowsetmeta + " does not exist");
        Path metafile = workflowsetmeta;
        if (Files.isDirectory(workflowsetmeta)) {
            metafile = workflowsetmeta.resolve("workflowset.meta");
            CheckUtils.checkArgument(Files.exists(metafile),
                "workflowset.meta file not found at path: " + workflowsetmeta);
        }
        try (final InputStream is = Files.newInputStream(metafile)) {
            return new WorkflowGroupMetadata(readWorkflowSetMeta(is));
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
        CheckUtils.checkArgumentNotNull(config, "Configuration cannot be null");
        CheckUtils.checkArgument(Files.exists(path), "File does not exist at path " + path);
        if (isZip(path)) {
            try (final ZipFile zip = new ZipFile(path.toAbsolutePath().toString())) {
                final String workflowPath = findFirstWorkflow(zip);
                CheckUtils.checkArgumentNotNull(workflowPath, "Zip file does not contain a workflow: " + path);
                return readTopLevelWorkflow(workflowPath, zip, config);
            }
        }

        CheckUtils.checkArgument(Files.isDirectory(path), "Path is not a directory: " + path);

        // Validate if it is a workflow
        final Path workflowPath = path.resolve("workflow.knime");
        CheckUtils.checkArgument(!Files.exists(path.resolve("template.knime")),
            path + " is a template, not a workflow");
        CheckUtils.checkArgument(Files.exists(workflowPath), path + " is not a workflow");

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
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     */
    public static TemplateMetadata readTemplate(final Path path) throws IOException, URISyntaxException,
        InvalidSettingsException, ParseException, XPathExpressionException, ParserConfigurationException, SAXException {
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
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     */
    public static TemplateMetadata readTemplate(final Path path, final WorkflowalizerConfiguration config)
        throws IOException, URISyntaxException, InvalidSettingsException, ParseException, XPathExpressionException,
        ParserConfigurationException, SAXException {
        CheckUtils.checkArgumentNotNull(config, "Configuration cannot be null");
        CheckUtils.checkArgument(Files.exists(path), "File does not exist at path " + path);
        if (isZip(path)) {
            try (final ZipFile zip = new ZipFile(path.toAbsolutePath().toString())) {
                final String zipPath = findFirstTemplate(zip);
                CheckUtils.checkArgumentNotNull(zipPath, "Zip file does not contain a template: " + path);
                return readTemplateMetadata(zipPath, zip, config);
            }
        }

        CheckUtils.checkArgument(Files.isDirectory(path), "Path is not a directory: " + path);

        // validate it is a template
        final Path workflowPath = path.resolve("workflow.knime");
        final Path templatePath = path.resolve("template.knime");
        CheckUtils.checkArgument(Files.exists(workflowPath), "workflow.knime was not found at " + path);
        CheckUtils.checkArgument(Files.exists(templatePath), path + " is a workflow, not a template");
        return readTemplateMetadata(path.toAbsolutePath().toString(), null, config);
    }

    // -- Helper methods --

    private static Optional<WorkflowSetMeta> readWorkflowSetMeta(final String path, final ZipFile zip,
        final WorkflowParser parser)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        if (zip == null) {
            final Path workflowsetPath = Paths.get(path, parser.getWorkflowSetMetaFileName());
            if (!Files.exists(workflowsetPath)) {
                return Optional.empty();
            }

            try (final InputStream is = Files.newInputStream(workflowsetPath)) {
                return Optional.ofNullable(readWorkflowSetMeta(is));
            }
        }

        final ZipEntry entry = zip.getEntry(path + parser.getWorkflowSetMetaFileName());
        if (entry == null) {
            return Optional.empty();
        }

        try (final InputStream is = zip.getInputStream(entry)) {
            return Optional.ofNullable(readWorkflowSetMeta(is));
        }
    }

    private static WorkflowSetMeta readWorkflowSetMeta(final InputStream is)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        final Document doc = parseXMLDocument(is, "workflowset.meta");
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
        CheckUtils.checkArgument(versionOptional.isPresent(), "Unknown version: " + version);
        final LoadVersion loadVersion = versionOptional.get();

        WorkflowParser parser = null;
        if (loadVersion.equals(LoadVersion.V4010) || loadVersion.ordinal() > LoadVersion.V4010.ordinal()) {
            parser = new WorkflowParserV410();
        } else if (loadVersion.equals(LoadVersion.V3080) || loadVersion.equals(LoadVersion.V3070)
            || loadVersion.equals(LoadVersion.V3060Pre) || loadVersion.equals(LoadVersion.V3010)
            || loadVersion.equals(LoadVersion.V2100) || loadVersion.equals(LoadVersion.V2100Pre)) {
            parser = new WorkflowParserV2100Pre();
        } else if (loadVersion.equals(LoadVersion.V280)) {
            parser = new WorkflowParserV280();
        } else if (loadVersion.equals(LoadVersion.V260) || loadVersion.equals(LoadVersion.V250)
            || loadVersion.equals(LoadVersion.V240) || loadVersion.equals(LoadVersion.V230)) {
            parser = new WorkflowParserV230();
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
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, path, zip, null);
        final String name = new File(path).getName();
        wf.setName(name);

        final String author = parser.getAuthorName(workflowKnime);
        builder.setAuthor(author);

        final OffsetDateTime authoredDate = parser.getAuthoredDate(workflowKnime);
        builder.setAuthorDate(authoredDate);

        final Optional<OffsetDateTime> lastEditedDate = parser.getEditedDate(workflowKnime);
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

        final Path artifatsDir = Paths.get(path).resolve(parser.getArtifactsDirectoryName());
        if (wc.parseWorkflowConfiguration()) {
            builder.setWorkflowConfiguration(
                readFileIntoString(artifatsDir.resolve(parser.getWorkflowConfiguration()), zip));
        }
        if (wc.parseWorkflowConfigurationRepresentation()) {
            builder.setWorkflowConfigurationRepresentation(
                readFileIntoString(artifatsDir.resolve(parser.getWorkflowConfigurationRepresentation()), zip));
        }
        if (wc.parseOpenapiInputParameters()) {
            builder.setOpenapiInputParameters(
                readFileIntoString(artifatsDir.resolve(parser.getOpenapiInputParameters()), zip));
        }
        if (wc.parseOpenapiInputResources()) {
            builder.setOpenapiInputResources(
                readFileIntoString(artifatsDir.resolve(parser.getOpenapiInputResources()), zip));
        }
        if (wc.parseOpenapiOutputParameters()) {
            builder.setOpenapiOutputParameters(
                readFileIntoString(artifatsDir.resolve(parser.getOpenapiOutputParameters()), zip));
        }
        if (wc.parseOpenapiOutputResources()) {
            builder.setOpenapiOutputResources(
                readFileIntoString(artifatsDir.resolve(parser.getOpenapiOutputResources()), zip));
        }

        if (zip == null) {
            svgFile(parser, Paths.get(path), builder);
        } else {
            svgFile(parser, path, zip, builder);
        }

        if (wc.parseWorkflowMeta()) {
            final Optional<WorkflowSetMeta> wsa = readWorkflowSetMeta(path, zip, parser);
            builder.setWorkflowSetMeta(wsa);
        }

        final List<String> credentials = parser.getWorkflowCredentialName(workflowKnime);
        builder.setWorkflowCredentialsNames(credentials);

        final List<String> variables = parser.getWorkflowVariables(workflowKnime);
        builder.setWorkflowVariables(variables);

        try (final Stream<?> files = zip == null ? Files.list(Paths.get(path)) : zip.stream()) {
            final boolean hasReport = parser.getHasReport(files);
            builder.setHasReport(hasReport);
        }

        return builder.build(wc);
    }

    private static TemplateMetadata readTemplateMetadata(final String path, final ZipFile zip,
        final WorkflowalizerConfiguration wc) throws InvalidSettingsException, FileNotFoundException, ParseException,
        IOException, XPathExpressionException, ParserConfigurationException, SAXException {
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

        // Read template type (MetaNode or Component template)
        final String templateType = parser.getTemplateType(templateKnime);

        // Read template
        TemplateMetadataBuilder builder = null;
        if (templateType.equalsIgnoreCase("subnode")) {
            final ComponentMetadataBuilder bc = new ComponentMetadataBuilder();
            builder = bc;

            final WorkflowFields wfTemp = wc.createWorkflowFields();
            populateWorkflowFields(wfTemp,
                WorkflowalizerConfiguration.builder().readNodeConfiguration().readNodesAndConnections().build(), parser,
                workflowKnime, templateKnime, path, zip, null);
            populateComponentFields(path, zip, parser, bc, wfTemp, wc);
        } else {
            builder = new TemplateMetadataBuilder();
        }

        builder.setType(templateType);

        final WorkflowFields wf = wc.createWorkflowFields();
        populateWorkflowFields(wf, wc, parser, workflowKnime, templateKnime, path, zip, null);
        builder.setWorkflowFields(wf);

        final String name = new File(path).getName();
        wf.setName(name);

        final String author = parser.getAuthorName(workflowKnime);
        builder.setAuthor(author);

        final OffsetDateTime authoredDate = parser.getAuthoredDate(workflowKnime);
        builder.setAuthorDate(authoredDate);

        final Optional<OffsetDateTime> lastEditedDate = parser.getEditedDate(workflowKnime);
        builder.setLastEditDate(lastEditedDate);

        final Optional<String> lastEditor = parser.getEditorName(workflowKnime);
        builder.setLastEditor(lastEditor);

        final String role = parser.getRole(templateKnime);
        builder.setRole(role);

        final OffsetDateTime timeStamp = parser.getComponentTimestamp(templateKnime);
        builder.setTimeStamp(timeStamp);

        final Optional<String> sourceURI = parser.getSourceURI(templateKnime);
        builder.setSourceURI(sourceURI);

        return builder.build(wc);
    }

    private static Map<Integer, NodeMetadata> readNodes(final String currentWorkflowDirectory, final ZipFile zip,
        final List<ConfigBase> configs, final WorkflowalizerConfiguration wc, final WorkflowParser parser,
        final String nodeId) throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
        final Map<Integer, NodeMetadata> map = new HashMap<>();
        for (final ConfigBase config : configs) {
            final NodeType type = parser.getType(config);
            NodeMetadata n = null;
            switch (type) {
                case NATIVE_NODE:
                    n = readNativeNode(currentWorkflowDirectory, zip, config, wc, parser, nodeId);
                    break;
                case SUBNODE:
                    n = readWrappedMetanode(currentWorkflowDirectory, zip, config, wc, parser, nodeId);
                    break;
                case METANODE:
                    n = readMetanode(currentWorkflowDirectory, zip, wc, config, parser, nodeId);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown node type: " + type);
            }

            String nId = n.getNodeId();
            if (nId.lastIndexOf(':') >= 0) {
                nId = nId.substring(nId.lastIndexOf(':') + 1, nId.length());
            }
            map.put(Integer.parseInt(nId), n);
        }
        return map;
    }

    private static MetanodeMetadata readMetanode(final String parentDirectory, final ZipFile zip,
        final WorkflowalizerConfiguration wc, final ConfigBase configBase, final WorkflowParser parser,
        final String nodeId) throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
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

        final NodeFields nf = wc.createNodeFields();
        populateNodeFields(nf, parser, configBase, nodeId);

        final WorkflowFields wf = wc.createWorkflowFields();
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, metaNodeDirectory, zip, nf.getId());
        builder.setWorkflowFields(wf);

        final Optional<String> annotationText = parser.getAnnotationText(workflowKnime, null);
        nf.setAnnotationText(annotationText);
        builder.setNodeFields(nf);

        final Optional<String> templateLink = parser.getTemplateLink(workflowKnime);
        builder.setTemplateLink(templateLink);

        return builder.build(wc);
    }

    private static SubnodeMetadata readWrappedMetanode(final String parentDirectory, final ZipFile zip,
        final ConfigBase configBase, final WorkflowalizerConfiguration wc, final WorkflowParser parser,
        final String nodeId) throws InvalidSettingsException, FileNotFoundException, IOException, ParseException {
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
            workflowKnime = readFile(subnodeDirectoryPath.resolve(parser.getWorkflowFileName()));
        } else {
            final String nodePath = parentDirectory + settings;
            subnodeDirectory = nodePath.substring(0, nodePath.lastIndexOf("/") + 1);
            settingsXml = readFile(nodePath, zip);
            workflowKnime = readFile(subnodeDirectory + parser.getWorkflowFileName(), zip);
        }

        final SingleNodeFields snf = wc.createSingleNodeFields();
        // Subnodes were not supported when node.xml files were used
        populateSingleNodeFields(snf, wc, parser, settingsXml, null, configBase, nodeId);
        builder.setSingleNodeFields(snf);

        final WorkflowFields wf = wc.createWorkflowFields();
        populateWorkflowFields(wf, wc, parser, workflowKnime, null, subnodeDirectory, zip, snf.getId());
        builder.setWorkflowFields(wf);

        final Optional<String> templateLink = parser.getTemplateLink(settingsXml);
        builder.setTemplateLink(templateLink);

        final int virtualInputId = parser.getVirtualInId(settingsXml);
        builder.setInputId(virtualInputId);

        final int virtualOutputId = parser.getVirtualOutId(settingsXml);
        builder.setOutputId(virtualOutputId);

        return builder.build(wc);
    }

    private static NativeNodeMetadata readNativeNode(final String parentDirectory, final ZipFile zip,
        final ConfigBase configBase, final WorkflowalizerConfiguration wc, final WorkflowParser parser,
        final String nodeId) throws InvalidSettingsException, FileNotFoundException, IOException {
        final String settings = parser.getNodeSettingsFilePath(configBase);
        final NativeNodeMetadataBuilder builder = new NativeNodeMetadataBuilder();

        MetadataConfig settingsXml = null;
        MetadataConfig nodeXml = null;
        if (zip == null) {
            final Path nodeFile = Paths.get(parentDirectory, settings);
            settingsXml = readFile(nodeFile);
            if (settingsXml.containsKey("node_file")) {
                final String fileName = settingsXml.getString("node_file");
                if (!fileName.equals("settings.xml")) {
                    final Path p = Paths.get(nodeFile.getParent().toAbsolutePath().toString(), fileName);
                    nodeXml = readFile(p);
                }
            }
        } else {
            final String node = parentDirectory + settings;
            settingsXml = readFile(node, zip);
            if (settingsXml.containsKey("node_file")) {
                final String fileName = settingsXml.getString("node_file");
                if (!fileName.equals("settings.xml")) {
                    final String dir = node.substring(0, node.lastIndexOf("/") + 1);
                    nodeXml = readFile(dir + fileName, zip);
                }
            }
        }

        final SingleNodeFields snf = wc.createSingleNodeFields();
        populateSingleNodeFields(snf, wc, parser, settingsXml, nodeXml, configBase, nodeId);
        builder.setSingleNodeFields(snf);

        final String factorySettings = parser.getFactorySettingsHashCode(settingsXml);
        builder.setFactorySettingsHashCode(factorySettings);

        final Optional<String> name = parser.getNodeName(settingsXml, nodeXml);
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

        Optional<String> featureSymbolicName = parser.getFeatureSymbolicName(settingsXml)
            // Remove extraneously added .feature.group
            .map(s -> s.replaceAll("\\.feature\\.group$", ""));
        // HACK: See https://knime-com.atlassian.net/browse/AP-13547 for details
        if (featureSymbolicName.isPresent() && featureSymbolicName.get().equals("org.knime.features.testing.core")) {
            featureSymbolicName = Optional.of("org.knime.features.testing.application");
        }
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
        final ZipFile zip, final String nodeId)
        throws InvalidSettingsException, ParseException, FileNotFoundException, IOException {
        final Version version = parser.getVersion(workflowKnime, templateKnime);
        wf.setVersion(version);

        final Version createdBy = parser.getCreatedBy(workflowKnime, templateKnime);
        wf.setCreatedBy(createdBy);

        final String name = parser.getName(workflowKnime);
        wf.setName(name);

        final Optional<String> customDescription = parser.getCustomDescription(workflowKnime);
        wf.setCustomDescription(customDescription);

        final Optional<List<String>> annotations = parser.getAnnotations(workflowKnime);
        wf.setAnnotations(annotations);
        if (wc.parseNodes() && wf.getNodes() == null) {
            final Map<Integer, NodeMetadata> nodes =
                readNodes(path, zip, parser.getNodeConfigs(workflowKnime), wc, parser, nodeId);
            wf.setNodes(new ArrayList<>(nodes.values()));
            if (wc.parseConnections() && wf.getConnections() == null) {
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
        final ConfigBase nodeConfig, final String nodeId) throws InvalidSettingsException {
        final int id = parser.getId(nodeConfig);
        String nid = nodeId;
        if (nid == null) {
            nid = id + "";
        } else {
            nid = nid + ":" + id;
        }
        nf.setId(nid);

        final NodeType type = parser.getType(nodeConfig);
        nf.setType(type);
    }

    private static void populateSingleNodeFields(final SingleNodeFields snf, final WorkflowalizerConfiguration wc,
        final WorkflowParser parser, final ConfigBase settingsXml, final ConfigBase nodeXml,
        final ConfigBase nodeConfig, final String nodeId) throws InvalidSettingsException {
        populateNodeFields(snf, parser, nodeConfig, nodeId);
        if (wc.parseNodeConfiguration()) {
            final Optional<ConfigBase> nodeConfiguration = parser.getNodeConfiguration(settingsXml, nodeXml);
            if (nodeConfiguration.isPresent()) {
                // Remove link to parent (memory optimization)
                nodeConfiguration.get().setParent(null);
            }
            snf.setNodeConfiguration(nodeConfiguration);
        }

        final Optional<String> customDescription = parser.getCustomNodeDescription(settingsXml);
        snf.setCustomDescription(customDescription);

        final Optional<String> annotationText = parser.getAnnotationText(null, settingsXml);
        snf.setAnnotationText(annotationText);
    }

    private static void populateComponentFields(final String path, final ZipFile zip, final WorkflowParser parser,
        final ComponentMetadataBuilder builder, final WorkflowFields wf, final WorkflowalizerConfiguration config)
        throws FileNotFoundException, IOException, InvalidSettingsException, XPathExpressionException,
        ParserConfigurationException, SAXException {
        // workflowset.meta
        if (config.parseWorkflowMeta()) {
            final Optional<WorkflowSetMeta> wsa = readWorkflowSetMeta(path, zip, parser);
            builder.setWorkflowSetMeta(wsa);
        }

        // node.xml and settings.xml were combined into one file BEFORE SubNodes/Components existed
        final MetadataConfig settingsXml = readFile(path, "settings.xml", zip);
        final Optional<String> type = parser.getComponentType(settingsXml);
        builder.setComponentType(type);

        final Optional<String> icon = parser.getIcon(settingsXml);
        builder.setIcon(icon);

        final int inputId = parser.getVirtualInId(settingsXml);
        final int outputId = parser.getVirtualOutId(settingsXml);

        final List<ComponentDialogSection.Field> fields = new ArrayList<>();
        final List<String> viewNodes = new ArrayList<>();
        Optional<String> description = Optional.empty();
        List<Optional<String>> inportNames = Collections.emptyList();
        List<Optional<String>> inportDescriptions = Collections.emptyList();
        List<Optional<String>> outportNames = Collections.emptyList();
        List<Optional<String>> outportDescriptions = Collections.emptyList();

        for (final NodeMetadata n : wf.getNodes()) {
            if (n instanceof NativeNodeMetadata) {
                final NativeNodeMetadata nn = (NativeNodeMetadata)n;
                if (nn.getNodeConfiguration().isPresent()) {
                    final ConfigBase nodeConfig = nn.getNodeConfiguration().get();
                    if (n.getNodeId().equals(inputId + "")) {
                        description = parser.getComponentTemplateDescription(nodeConfig, settingsXml);
                        inportNames = parser.getPortNames(nodeConfig, settingsXml, true);
                        inportDescriptions = parser.getPortDescriptions(nodeConfig, settingsXml, true);
                    } else if (n.getNodeId().equals(outputId + "")) {
                        outportNames = parser.getPortNames(nodeConfig, settingsXml, false);
                        outportDescriptions = parser.getPortDescriptions(nodeConfig, settingsXml, false);
                    } else {
                        if (parser.isDialogNode(nodeConfig)) {
                            final Optional<String> fieldName = parser.getDialogFieldName(nodeConfig);
                            final Optional<String> fieldDescription = parser.getDialogFieldDescription(nodeConfig);
                            // does not appear as though component dialogs support optional fields
                            fields.add(new ComponentDialogSection.Field(fieldName, fieldDescription, false));
                        }
                        if (parser.isInteractiveViewNode(nodeConfig)) {
                            viewNodes.add(nn.getFactoryName());
                        }
                    }
                }
            } else if (n instanceof IWorkflowMetadata) {
                populateViewNodes(((IWorkflowMetadata)n).getNodes(), viewNodes, parser);
            } else {
                throw new IllegalArgumentException("Unrecognized node type: " + n.getType());
            }
        }
        builder.setViewNodes(viewNodes);
        builder.setDialog(
            Collections.singletonList(new ComponentDialogSection(Optional.empty(), Optional.empty(), fields)));
        builder.setDescription(description);

        // ports
        final List<String> inportObjects = parser.getInPortObjects(settingsXml);
        final List<ComponentPortInfo> inports = new ArrayList<>(inportObjects.size());
        for (int i = 0; i < inportObjects.size(); i++) {
            Optional<String> desc = inportDescriptions.size() > i ? inportDescriptions.get(i) : Optional.empty();
            Optional<String> name = inportNames.size() > i ? inportNames.get(i) : Optional.empty();
            inports.add(new ComponentPortInfo(desc, name, inportObjects.get(i)));
        }
        builder.setInPorts(inports);

        final List<String> outportObjects = parser.getOutPortObjects(settingsXml);
        final List<ComponentPortInfo> outports = new ArrayList<>(outportObjects.size());
        for (int i = 0; i < outportObjects.size(); i++) {
            Optional<String> desc = outportDescriptions.size() > i ? outportDescriptions.get(i) : Optional.empty();
            Optional<String> name = outportNames.size() > i ? outportNames.get(i) : Optional.empty();
            outports.add(new ComponentPortInfo(desc, name, outportObjects.get(i)));
        }
        builder.setOutPorts(outports);
    }

    private static void populateViewNodes(final List<NodeMetadata> nodes, final List<String> viewNodes,
        final WorkflowParser parser) throws InvalidSettingsException, FileNotFoundException, IOException {
        if (nodes.isEmpty()) {
            return;
        }

        for (final NodeMetadata node : nodes) {
            if (node instanceof NativeNodeMetadata) {
                final NativeNodeMetadata nn = (NativeNodeMetadata)node;
                if (nn.getNodeConfiguration().isPresent()
                    && parser.isInteractiveViewNode(nn.getNodeConfiguration().get())) {
                    viewNodes.add(nn.getFactoryName());
                }
            } else if (node instanceof IWorkflowMetadata) {
                populateViewNodes(((IWorkflowMetadata)node).getNodes(), viewNodes, parser);
            } else {
                throw new IllegalArgumentException("Unrecognized node type: " + node.getType());
            }
        }
    }

    // -- File methods --

    private static MetadataConfig readFile(final String currentDir, final String relativeFilePath, final ZipFile zip)
        throws FileNotFoundException, IOException {
        if (zip == null) {
            return readFile(Paths.get(currentDir, relativeFilePath));
        }
        final String cd = currentDir.endsWith("/") ? currentDir : currentDir + "/";
        final String rp = relativeFilePath.startsWith("/") ? relativeFilePath.substring(1, relativeFilePath.length())
            : relativeFilePath;
        return readFile(cd + rp, zip);
    }

    private static MetadataConfig readFile(final Path f) throws FileNotFoundException, IOException {
        try (final InputStream s = Files.newInputStream(f)) {
            final MetadataConfig c = new MetadataConfig("ignored");
            c.load(s);
            return c;
        }
    }

    private static MetadataConfig readFile(final String entry, final ZipFile zip) throws IOException {
        final ZipEntry e = zip.getEntry(entry);
        CheckUtils.checkArgumentNotNull(e, "Zip entry does not exist: " + entry);
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
                    final Path relativePath = path.relativize(file);
                    unexpectedFiles.add(relativePath.toString());
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
                    files.add(relativePath);
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
        CheckUtils.checkArgument(Files.isDirectory(artifactsDir),
            parser.getArtifactsDirectoryName() + " is not a directory");

        final Collection<String> files = new ArrayList<>();
        final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                final Path relativePath = workflowDirectory.relativize(file);
                files.add(relativePath.toString());
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
        CheckUtils.checkArgument(entry.isDirectory(), entryName + " is not a directory");

        final Collection<String> files = new ArrayList<>();
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry e = entries.nextElement();
            final String name = e.getName();
            if (name.startsWith(entryName) && !e.isDirectory()) {
                files.add(name.substring(path.length()));
            }
        }
        return Optional.of(files);
    }

    private static void svgFile(final WorkflowParser parser, final Path workflowDirectory,
        final WorkflowMetadataBuilder builder) throws IOException {
        final Path svg = workflowDirectory.resolve(parser.getWorkflowSVGFileName());
        if (!Files.exists(svg, LinkOption.NOFOLLOW_LINKS)) {
            return;
        }
        CheckUtils.checkArgument(Files.probeContentType(svg).toLowerCase().contains("svg"),
            parser.getWorkflowSVGFileName() + " is not an SVG");
        builder.setSvgFile(svg);

        try {
            final Document doc = parseXMLDocument(svg);

            final Node svgItem = doc.getElementsByTagName("svg").item(0);
            if (svgItem != null) {
                final Node widthNode = svgItem.getAttributes().getNamedItem("width");
                final Node heightNode = svgItem.getAttributes().getNamedItem("height");
                if (widthNode != null) {
                    builder.setSvgWidth(Integer.parseInt(widthNode.getNodeValue()));
                }
                if (heightNode != null) {
                    builder.setSvgHeight(Integer.parseInt(heightNode.getNodeValue()));
                }
            }
        } catch (SAXException ex) {
            throw new IOException(ex);
        }
    }

    private static void svgFile(final WorkflowParser parser, final String path, final ZipFile zip,
        final WorkflowMetadataBuilder builder) throws IOException {
        final ZipEntry svg = zip.getEntry(path + parser.getWorkflowSVGFileName());
        if (svg == null) {
            return;
        }
        builder.setSvgFile(Paths.get(zip.getName()));
        builder.setSvgZipEntry(path + parser.getWorkflowSVGFileName());
        try (final InputStream stream = zip.getInputStream(svg)) {
            final Document doc = parseXMLDocument(stream, parser.getWorkflowSVGFileName());
            final Node svgItem = doc.getElementsByTagName("svg").item(0);
            if (svgItem != null) {
                final Node widthNode = svgItem.getAttributes().getNamedItem("width");
                final Node heightNode = svgItem.getAttributes().getNamedItem("height");
                if (widthNode != null) {
                    builder.setSvgWidth(Integer.parseInt(widthNode.getNodeValue()));
                }
                if (heightNode != null) {
                    builder.setSvgHeight(Integer.parseInt(heightNode.getNodeValue()));
                }
            }
        } catch (SAXException ex) {
            throw new IOException(ex);
        }
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
                if (name.endsWith("workflow.knime") && !templates.contains(name)) {
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
                    workflow = null;
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
                        workflowgroup = null;
                        break;
                    }
                }
            }
        } while (isWorkflow);

        return workflowgroup;
    }

    /**
     * Reads the passed file into a {@link String} and returns it.
     *
     * @param path the {@link Path} to the file
     * @param zip the {@link ZipFile} of the associated path, or {@code null} if this is not a zip file
     * @return the contents of the file
     * @throws IOException
     * @throws IllegalArgumentException
     */
    private static String readFileIntoString(final Path path, final ZipFile zip) throws IOException {
        if (zip == null) {
            return readFileIntoString(path);
        }
        return readZipFileIntoString(path, zip);
    }

    /**
     * Reads the passed file into a {@link String} and returns it. If the file does not exist, null is returned
     *
     * @param path the {@link Path} to the file
     * @return the contents of the file
     * @throws IOException
     */
    private static String readFileIntoString(final Path path) throws IOException {
        if (Files.exists(path)) {
            byte[] encoded = Files.readAllBytes(path);
            return new String(encoded, StandardCharsets.UTF_8);
        }
        LOGGER.log(Level.WARNING, () -> "File does not exist: " + path.toString());
        return null;
    }

    /**
     * Reads the passed file into a {@link String} and returns it. If the zip entry does not exist, null is returned. If
     * the entry is a directory a {@link IllegalArgumentException} is thrown.
     *
     * @param path the {@link Path} to the file
     * @param zip the {@link ZipFile} of the associated path
     * @return the contents of the file
     * @throws IOException
     * @throws IllegalArgumentException
     */
    private static String readZipFileIntoString(final Path path, final ZipFile zip) throws IOException {
        final String entryName = path.toString();
        final ZipEntry entry = zip.getEntry(entryName);
        if (entry == null) {
            return null;
        }
        CheckUtils.checkArgument(!entry.isDirectory(), entryName + " is a directory");

        String contents;
        try (InputStream is = zip.getInputStream(entry)) {
            contents = IOUtils.toString(is, StandardCharsets.UTF_8);
        }
        return contents;
    }

    private static Document parseXMLDocument(final InputStream is, final String filename)
        throws IOException, SAXException {
        try {
            DocumentBuilder docBuilder = getConfiguredDocumentBuilder();
            return docBuilder.parse(is);
        } catch (SAXParseException ex) {
            if (ex.getMessage().contains("is disallowed when the feature")) {
                throw new IllegalArgumentException(
                    String.format("Cannot parse the given file '%s', as it contains XML elements which are not allowed",
                        filename),
                    ex);
            }
            throw ex;
        }
    }

    private static Document parseXMLDocument(final Path pathToFile) throws IOException, SAXException {
        DocumentBuilder docBuilder = getConfiguredDocumentBuilder();
        try {
            return docBuilder.parse(pathToFile.toFile());
        } catch (SAXParseException ex) {
            if (ex.getMessage().contains("is disallowed when the feature")) {
                throw new IllegalArgumentException(
                    String.format("Cannot parse the given file '%s', as it contains XML elements which are not allowed",
                        pathToFile.getFileName()),
                    ex);
            }
            throw ex;
        }
    }

    private static DocumentBuilder getConfiguredDocumentBuilder() throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Configure DocumentBuilderFactory to prevent XXE
            // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#java
            // https://knime-com.atlassian.net/browse/HUB-3756
            String feature = "http://apache.org/xml/features/disallow-doctype-decl";
            factory.setFeature(feature, true);

            feature = "http://xml.org/sax/features/external-general-entities";
            factory.setFeature(feature, false);

            feature = "http://xml.org/sax/features/external-parameter-entities";
            factory.setFeature(feature, false);

            feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            factory.setFeature(feature, false);

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            // additional configuration
            factory.setValidating(true);
            factory.setIgnoringElementContentWhitespace(true);
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new IOException("Unable to create XML parser", ex);
        }
    }
}
