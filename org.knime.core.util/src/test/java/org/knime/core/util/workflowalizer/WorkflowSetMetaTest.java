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
 *   May 4, 2022 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.knime.core.util.workflowalizer.WorkflowSetMeta.Link;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Tests parsing the workflowset.meta file.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 */
public class WorkflowSetMetaTest {

    /**
     * Test reading a workflowset.meta file which has all fields populated.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testAllFields()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-all-fields");
        String title = "Custom Title";
        String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr,"
            + " sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At"
            + " vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata "
            + "sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed "
            + "diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero "
            + "eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est"
            + " Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy"
            + " eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et "
            + "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem"
            + " ipsum dolor sit amet. \n";
        List<Link> links = List.of(new Link("https://knime.com", "KNIME"));
        List<String> tags = List.of("Foo", "Bar");
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(links));
        assertTags(wsm, Optional.of(tags));
    }

    /**
     * Tests reading a workflowset.meta file which has an empty comments tag.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testEmptyComments()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-empty-comments");
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.empty());
        assertDescription(wsm, Optional.empty());
        assertLinks(wsm, Optional.empty());
        assertTags(wsm, Optional.empty());
    }

    /**
     * Tests reading a workflowset.meta file whose comments field is a single line.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testSingleLine()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-single-line");
        String description = "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat,"
            + " vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui"
            + " blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor"
            + " sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore "
            + "magna aliquam erat volutpat. ";
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.empty());
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests reading a workflowset.meta file with a title and description but no tags.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testNoTags() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-title-desc-no-tags");
        String description = "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat,"
            + " vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui"
            + " blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor"
            + " sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore "
            + "magna aliquam erat volutpat. ";
        String title = "Custom Title";
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests reading a workflowset.meta file whose description starts with whitespace.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testDescriptionStartsWithWhiteSpace()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm =
            readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-title-description-starts-with-newlines");
        String description = "\n\n\nLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod"
            + " tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et"
            + " justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum "
            + "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor"
            + " invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo "
            + "duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit "
            + "amet.";
        String title = "Custom Title";
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests reading a workflowset.meta file which has additional text after the tags.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testTextAfterTags()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-text-after-tags");
        String title = "Custom Title";
        String description = "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat,"
            + " vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui "
            + "blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor"
            + " sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore "
            + "magna aliquam erat volutpat. \n\nUt wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper "
            + "suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in "
            + "hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at "
            + "vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis"
            + " dolore te feugait nulla facilisi. \n\nNam liber tempor cum soluta nobis eleifend option congue nihil "
            + "imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer "
            + "adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat "
            + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl"
            + " ut aliquip ex ea commodo consequat. \n\nDuis autem vel eum iriure dolor in hendrerit in vulputate "
            + "velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis. \n";
        List<Link> links = List.of(new Link("https://knime.com", "KNIME"));
        List<String> tags = List.of("Foo", "Bar");
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(links));
        assertTags(wsm, Optional.of(tags));
    }

    /**
     * Tests reading a workflowset.meta file whose description contains line breaks.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testLineBreaksIndDescription()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm =
            readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-title-description-contains-newlines");
        String title = "Custom Title";
        String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor"
            + " invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo"
            + " duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit"
            + " amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt"
            + " ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores"
            + " et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem"
            + " ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore "
            + "et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea "
            + "rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. \n\nDuis "
            + "autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu"
            + " feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent "
            + "luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, "
            + "consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam"
            + " erat volutpat. \n\nUt wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit"
            + " lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in "
            + "vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et"
            + " accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te "
            + "feugait nulla facilisi. \n\nNam liber tempor cum soluta nobis eleifend option congue nihil imperdiet"
            + " doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer "
            + "adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat "
            + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl "
            + "ut aliquip ex ea commodo consequat. \n\nDuis autem vel eum iriure dolor in hendrerit in vulputate velit"
            + " esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.";
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests reading a workflowset.meta file with unknown tags.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testUnknownTags()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-unknown-tag");
        String title = "Custom Title";
        String description = "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat,"
            + " vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui "
            + "blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor"
            + " sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore "
            + "magna aliquam erat volutpat. \n\nUt wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper "
            + "suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in "
            + "hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at "
            + "vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis"
            + " dolore te feugait nulla facilisi. \n\nNam liber tempor cum soluta nobis eleifend option congue nihil "
            + "imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer "
            + "adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat "
            + "volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl"
            + " ut aliquip ex ea commodo consequat. \n\nDuis autem vel eum iriure dolor in hendrerit in vulputate "
            + "velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis. \n";
        List<Link> links = List.of(new Link("https://knime.com", "KNIME"));
        List<String> tags = List.of("Foo", "Bar");
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(links));
        assertTags(wsm, Optional.of(tags));
    }

    /**
     * Tests reading a workflowset.meta file with only tags, no description or title.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testOnlyTags()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-only-tags");
        List<Link> links = List.of(new Link("https://knime.com", "KNIME"));
        List<String> tags = List.of("Foo", "Bar");
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.empty());
        assertDescription(wsm, Optional.empty());
        assertLinks(wsm, Optional.of(links));
        assertTags(wsm, Optional.of(tags));
    }

    /**
     * Tests reading a workflowset.meta file whose comments only contain a title.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testOnlyTitle()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-only-title");
        String title = "Custom Title";
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.empty());
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests reading a workflowset.meta file whose comments contains a description which ends with newlines.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testDescriptionEndsWithNewlines()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm =
            readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-title-description-ends-with-newlines");
        String title = "Custom Title";
        String description = "      Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod"
            + " tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et"
            + " justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum "
            + "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor"
            + " invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo"
            + " duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit"
            + " amet.";
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.of(title));
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests reading a workflowset.meta file whose comments only contain a description.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testOnlyDescription()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-only-description");
        String description = "Custom Description";
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.empty());
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests that the default text for title and description are ignored.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testDefaultTitleAndDescriptionIgnored()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-default-fields");
        assertAuthor(wsm, "awalter");
        assertTitle(wsm, Optional.empty());
        assertDescription(wsm, Optional.empty());
        assertLinks(wsm, Optional.of(List.of()));
        assertTags(wsm, Optional.of(List.of()));
    }

    /**
     * Tests reading a workflowset.meta file whose first line in the comments is empty and then has text.
     *
     * @throws IOException if I/O error occurs
     * @throws SAXException if SAX error occurs
     * @throws ParserConfigurationException if parser configuration error occurs
     * @throws XPathExpressionException if xpath expression error occurs
     */
    @Test
    public void testFirstLineEmpty()
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        WorkflowSetMeta wsm = readWorkflowSetMeta("/workflowSetMeta/workflowset.meta-comments-start-with-newlines");
        assertAuthor(wsm, "dim");
        String description = "\nFoo - title\n\n\nWibble wobble wubble flob. - description\n\n";
        List<Link> links = List.of(new Link("http://www.example.com", "example.com"));
        List<String> tags = List.of("Hi");
        assertTitle(wsm, Optional.empty());
        assertDescription(wsm, Optional.of(description));
        assertLinks(wsm, Optional.of(links));
        assertTags(wsm, Optional.of(tags));
    }

    // -- Helper Methods -

    @SuppressWarnings("static-method") // Cannot read in resources if the method is static
    private WorkflowSetMeta readWorkflowSetMeta(final String file)
        throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        try (InputStream is = WorkflowSetMetaTest.class.getResourceAsStream(file)) {
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
    }

    private static void assertAuthor(final WorkflowSetMeta meta, final String expectedAuthor) {
        String givenAuthor = null;
        if (meta.getAuthor().isPresent()) {
            givenAuthor = meta.getAuthor().get();
        }
        assertThat("Author in workflowset.meta should not be null", givenAuthor, notNullValue());
        assertThat("Unexpected Author in workflowset.meta", givenAuthor, is(expectedAuthor));
    }

    private static void assertTitle(final WorkflowSetMeta meta, final Optional<String> expectedTitle) {
        String errorMsg = String.format("Title should not be %s", expectedTitle.isEmpty() ? "present" : "empty");
        assertThat(errorMsg, meta.getTitle().isEmpty(), is(expectedTitle.isEmpty()));
        if (expectedTitle.isPresent()) {
            assertThat("Unexpected title", meta.getTitle().get(), is(expectedTitle.get()));
        }
    }

    private static void assertDescription(final WorkflowSetMeta meta, final Optional<String> expectedDescription) {
        String errorMsg =
            String.format("Description should not be %s", expectedDescription.isEmpty() ? "present" : "empty");
        assertThat(errorMsg, meta.getDescription().isEmpty(), is(expectedDescription.isEmpty()));
        if (expectedDescription.isPresent()) {
            assertThat("Unexpected description", meta.getDescription().get(), is(expectedDescription.get()));
        }
    }

    private static void assertTags(final WorkflowSetMeta meta, final Optional<List<String>> expectedTags) {
        String errorMsg = String.format("Tags should not be %s", expectedTags.isEmpty() ? "present" : "empty");
        assertThat(errorMsg, meta.getTags().isEmpty(), is(expectedTags.isEmpty()));
        if (expectedTags.isPresent()) {
            List<String> givenTags = meta.getTags().get();
            assertThat("Unexpected number of tags", givenTags.size(), is(expectedTags.get().size()));
            for (String tag : givenTags) {
                assertThat("Unexpected tag value", tag, isIn(expectedTags.get()));
            }
        }
    }

    private static void assertLinks(final WorkflowSetMeta meta,
        final Optional<List<WorkflowSetMeta.Link>> expectedLinks) {
        String errorMsg = String.format("Links should not be %s", expectedLinks.isEmpty() ? "present" : "empty");
        assertThat(errorMsg, meta.getLinks().isEmpty(), is(expectedLinks.isEmpty()));
        if (expectedLinks.isPresent()) {
            List<WorkflowSetMeta.Link> givenLinks = meta.getLinks().get();
            assertThat("Unexpected number of links", givenLinks.size(), is(expectedLinks.get().size()));
            for (WorkflowSetMeta.Link link : givenLinks) {
                assertThat("Unexpected link value", link, isIn(expectedLinks.get()));
            }
        }
    }
}
