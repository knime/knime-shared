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
 *   Oct 17, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Metadata extracted from "workflowset.meta".
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public class WorkflowSetMeta {

    // Copied from org.knime.workbench.ui.workflow.metadata.MetaInfoFile
    private static final String NO_TITLE_PLACEHOLDER_TEXT = "There has been no title set for this workflow's metadata.";

    // Copied from org.knime.workbench.ui.workflow.metadata.MetaInfoFile
    private static final String NO_DESCRIPTION_PLACEHOLDER_TEXT =
        "There has been no description set for this workflow's metadata.";

    private static final Pattern SPLIT_COMMENTS_REGEX = Pattern.compile("\r?\n");

    @JsonProperty("author")
    private final Optional<String> m_author;

    @JsonProperty("title")
    private Optional<String> m_title;

    @JsonProperty("description")
    private Optional<String> m_description;

    @JsonProperty("links")
    private Optional<List<Link>> m_links;

    @JsonProperty("tags")
    private Optional<List<String>> m_tags;

    WorkflowSetMeta(final Optional<String> author, final Optional<String> comments) {
        m_author = author;

        if (comments.isPresent() && StringUtils.isNotEmpty(comments.get())) {
            parseCommentsField(comments.get());
        } else {
            m_title = Optional.empty();
            m_description = Optional.empty();
            m_links = Optional.empty();
            m_tags = Optional.empty();
        }
    }

    /**
     * Constructor which creates a shallow duplicate of the given POJO.
     *
     * @param workflowSetMeta POJO to copy
     */
    protected WorkflowSetMeta(final WorkflowSetMeta workflowSetMeta) {
        m_author = workflowSetMeta.m_author;
        m_title = workflowSetMeta.m_title;
        m_description = workflowSetMeta.m_description;
        m_links = workflowSetMeta.m_links;
        m_tags = workflowSetMeta.m_tags;
    }

    /**
     * Returns the author name as set in the workflowset.meta file.
     *
     * @return the author name
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<String> getAuthor() {
        if (m_author == null) {
            throw new UnsupportedOperationException("getAuthor() is not supported, field was not read");
        }
        return m_author;
    }

    /**
     * Returns the workflow's title, as listed in the workflowset.meta file, if present.
     *
     * @return workflow's title
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<String> getTitle() {
        if (m_title == null) {
            throw new UnsupportedOperationException("getTitle() is not supported, field was not read");
        }
        return m_title;
    }

    /**
     * Returns the workflow's description, as listed in the workflowset.meta file, if present.
     *
     * @return workflow's description
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<String> getDescription() {
        if (m_description == null) {
            throw new UnsupportedOperationException("getDescription() is not supported, field was not read");
        }
        return m_description;
    }

    /**
     * Returns the the list of links associated with this workflow, as listed in the workflowset.meta file, if present.
     *
     * @return list of links
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<List<Link>> getLinks() {
        if (m_links == null) {
            throw new UnsupportedOperationException("getLinks() is not supported, field was not read");
        }
        return m_links;
    }

    /**
     * Returns the list of tags associated with this workflow, as listed in the workflowset.meta file, if present.
     *
     * @return list of workflow tags
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<List<String>> getTags() {
        if (m_tags == null) {
            throw new UnsupportedOperationException("getTags() is not supported, field was not read");
        }
        return m_tags;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WorkflowSetMeta)) {
            return false;
        }
        final WorkflowSetMeta other = (WorkflowSetMeta)obj;
        return new EqualsBuilder().append(m_author, other.m_author).append(m_title, other.m_title)
            .append(m_description, other.m_description).append(m_links, other.m_links).append(m_tags, other.m_tags)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(m_author).append(m_title).append(m_description).append(m_links)
            .append(m_tags).toHashCode();
    }

    @Override
    public String toString() {
        String links = "";
        if (m_links.isPresent()) {
            for (Link l : m_links.get()) {
                links += l.toString() + ", ";
            }
        }

        String tags = null;
        if (m_tags.isPresent()) {
            tags = StringUtils.join(m_tags.get(), ", ");
        }
        return "author: " + m_author.orElse(null) + "\n" + "title: " + m_title.orElse(null) + "\n" + "description: "
            + m_description.orElse(null) + "\n" + "links: " + links + "\n" + "tags: " + tags;
    }

    // -- Helper methods --

    // Copied from
    // org.knime.workbench.descriptionview.metadata.workflow.MetadataModelFacilitator
    // #potentiallyParseOldStyleDescription(String)
    // The above code is how the AP parses this field, and the code below is a close match to that though modified
    // slightly to work in this repository
    private void parseCommentsField(final String description) {
        String[] lines = SPLIT_COMMENTS_REGEX.split(description);
        boolean isOldStyle = isOldStyle(lines);

        if (isOldStyle) {
            int lineIndex = setTitle(lines);
            lineIndex = setDescription(lineIndex, lines);
            setTagsAndLines(lineIndex, lines);
        } else {
            m_title = Optional.empty();
            m_tags = Optional.of(List.of());
            m_links = Optional.of(List.of());
            m_description = Optional.of(description);
        }
    }

    private static boolean isOldStyle(final String[] lines) {
        boolean isOldStyle = (lines.length > 2);
        if (isOldStyle) {
            isOldStyle = false;
            for (var i = 1; i < (lines.length - 1); i++) {
                if ((lines[i].trim().length() == 0) && (lines[i + 1].trim().length() > 0)) {
                    isOldStyle = true;
                    break;
                }
            }
        }
        return isOldStyle;
    }

    private int setTitle(final String[] lines) {
        var lineIndex = 0;
        if (NO_TITLE_PLACEHOLDER_TEXT.equals(lines[lineIndex])) {
            m_title = Optional.empty();
            lineIndex++;
        } else {
            var title = new StringBuilder(lines[lineIndex]);

            lineIndex++;
            while (lines[lineIndex].trim().length() > 0) {
                title.append('\n').append(lines[lineIndex]);
                lineIndex++;
            }

            var actualTitle = title.toString();
            if (StringUtils.isAllBlank(actualTitle)) {
                m_title = Optional.empty();
            } else {
                m_title = Optional.of(title.toString());
            }
        }
        lineIndex++;
        return lineIndex;
    }

    private int setDescription(final int startIndex, final String[] lines) {
        if (startIndex >= lines.length) {
            m_description = Optional.empty();
            return startIndex;
        }

        int lineIndex = startIndex;
        var actualDescription = new StringBuilder();
        if (!NO_DESCRIPTION_PLACEHOLDER_TEXT.equals(lines[lineIndex])) {
            actualDescription.append(lines[lineIndex]);
        }
        lineIndex++;
        while (lineIndex < lines.length) {
            String line = lines[lineIndex];
            int index = line.indexOf(':');
            if ((index != -1) && (index < (line.length() - 2))) {
                // This line is probably a tag, so it is the end of the description
                break;
            }
            actualDescription.append('\n').append(line);
            lineIndex++;
        }
        var desc = actualDescription.toString();
        if (StringUtils.isAllBlank(desc)) {
            m_description = Optional.empty();
        } else {
            m_description = Optional.of(actualDescription.toString());
        }
        return lineIndex;
    }

    private void setTagsAndLines(final int startIndex, final String[] lines) {
        if (startIndex >= lines.length) {
            m_tags = Optional.of(List.of());
            m_links = Optional.of(List.of());
        }

        int lineIndex = startIndex;
        List<Link> linksList = new ArrayList<>();
        List<String> tagsList = new ArrayList<>();
        while (lineIndex < lines.length) {
            String line = lines[lineIndex];
            int index = line.indexOf(':');

            if ((index != -1) && (index < (line.length() - 2))) {
                final String initialText = line.substring(0, index).toUpperCase(Locale.US);

                if (isTextTag(initialText)) {
                    addTag(line, index, tagsList);
                } else if (isLinkTag(initialText)) {
                    addLink(line, index, linksList);
                } else {
                    // Do nothing, unknown tag
                }
            }
            lineIndex++;
        }
        m_tags = Optional.of(tagsList);
        m_links = Optional.of(linksList);
    }

    private static boolean isTextTag(final String tag) {
        return tag.equals("TAG") || tag.equals("TAGS");
    }

    private static boolean isLinkTag(final String tag) {
        return tag.equals("BLOG") || tag.equals("URL") || tag.equals("VIDEO");
    }

    private static void addTag(final String line, final int index, final List<String> tagsList) {
        String tagsConcatenated = line.substring(index + 1).trim();
        String[] tags = tagsConcatenated.split(",");

        for (String tag : tags) {
            tagsList.add(tag.trim());
        }
    }

    private static void addLink(final String line, final int index, final List<Link> linksList) {
        String lowercaseLine = line.toLowerCase(Locale.ROOT);
        int urlStart = lowercaseLine.indexOf("http:");
        if (urlStart == -1) {
            urlStart = lowercaseLine.indexOf("https:");
        }

        var url = line.substring(urlStart);
        String title = line.substring((index + 1), urlStart).trim();
        linksList.add(new Link(url, title));
    }

    // -- Helper classes --

    /**
     * POJO for representing links.
     */
    @JsonAutoDetect
    public static final class Link {

        private final String m_url;

        private final String m_text;

        /**
         * Creates a new link.
         *
         * @param url the link's URL
         * @param text the link's text
         */
        public Link(final String url, final String text) {
            m_url = url;
            m_text = text;
        }

        /**
         * Return this link's URL.
         *
         * @return the url
         */
        public String getUrl() {
            return m_url;
        }

        /**
         * Return this link's text.
         *
         * @return the text
         */
        public String getText() {
            return m_text;
        }

        @Override
        public String toString() {
            return "url: " + m_url + " text: " + m_text;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Link)) {
                return false;
            }
            final Link other = (Link)obj;
            return new EqualsBuilder().append(m_url, other.m_url).append(m_text, other.m_text).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(m_url).append(m_text).toHashCode();
        }
    }
}
