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
import java.util.Optional;

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
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class WorkflowSetMeta {

    private static final String LINE_SEPARATOR_REGEX = "\r?\n";

    @JsonProperty("author")
    private final Optional<String> m_author;

    @JsonProperty("title")
    private final Optional<String> m_title;

    @JsonProperty("description")
    private final Optional<String> m_description;

    @JsonProperty("links")
    private final Optional<List<Link>> m_links;

    @JsonProperty("tags")
    private final Optional<List<String>> m_tags;

    WorkflowSetMeta(final Optional<String> author, final Optional<String> comments) {
        m_author = author;

        if (comments.isPresent()) {
            final String[] lines = comments.get().split(LINE_SEPARATOR_REGEX);

            String title = null;
            int start = 0;
            // If the first line is less than 20 words, followed by a blank line, then another line which isn't
            // one of the special "tags" -> assume the first line is a title
            if (lines.length > 2 && (!lines[0].isEmpty() && lines[1].trim().isEmpty() && !lines[2].isEmpty())
                && !(lines[2].startsWith("BLOG:") || lines[2].startsWith("URL:") || lines[2].startsWith("VIDEO:")
                    || lines[2].startsWith("TAG:") || lines[2].startsWith("TAGS:"))) {
                title = lines[0];
                start = 2;
            }
            m_title = Optional.ofNullable(title);

            final StringBuilder b = new StringBuilder();
            int stop = lines.length;
            for (int i = start; i < lines.length; i++) {
                if (lines[i].startsWith("BLOG:") || lines[i].startsWith("URL:") || lines[i].startsWith("VIDEO:")
                    || lines[i].startsWith("TAG:") || lines[i].startsWith("TAGS:")) {
                    stop = i;
                    break;
                } else {
                    b.append(lines[i] + "\n");
                }
            }
            m_description = Optional.of(b.toString().trim());

            final List<Link> links = new ArrayList<>();
            final List<String> tags = new ArrayList<>();
            for (int i = stop; i < lines.length; i++) {
                if (lines[i].startsWith("BLOG:") || lines[i].startsWith("URL:") || lines[i].startsWith("VIDEO:")) {
                    final Link l = createURLTag(lines[i]);
                    if (l != null) {
                        links.add(l);
                    }
                } else if (lines[i].startsWith("TAG:") || lines[i].startsWith("TAGS:")) {
                    final String[] split = lines[i].split(":");
                    final String[] ts = split[1].split(",");
                    for (final String t : ts) {
                        tags.add(t.trim());
                    }
                }
            }

            m_links = Optional.of(links);
            m_tags = Optional.of(tags);
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
        final WorkflowSetMeta other = (WorkflowSetMeta) obj;
        return new EqualsBuilder()
                .append(m_author, other.m_author)
                .append(m_title, other.m_title)
                .append(m_description, other.m_description)
                .append(m_links, other.m_links)
                .append(m_tags, other.m_tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_author)
                .append(m_title)
                .append(m_description)
                .append(m_links)
                .append(m_tags)
                .toHashCode();
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

    private static Link createURLTag(final String s) {
        final int textIndex = s.indexOf(':');
        final int urlIndex = s.indexOf("http");

        if (urlIndex < 0) {
            return null;
        }

        final String href = s.substring(urlIndex, s.length()).trim();
        String text = href;
        if (textIndex >= 0) {
            text = s.substring(textIndex + 1, urlIndex).trim();
        }
        return new Link(href, text);
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
