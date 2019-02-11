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
public final class WorkflowSetMeta {

    @JsonProperty("author")
    private final Optional<String> m_author;

    @JsonProperty("title")
    private final Optional<String> m_title;

    @JsonProperty("description")
    private final Optional<String> m_description;

    @JsonProperty("blogLinks")
    private final Optional<String[]> m_blogLinks;

    @JsonProperty("videoLinks")
    private final Optional<String[]> m_videoLinks;

    @JsonProperty("additionalLinks")
    private final Optional<String[]> m_additionalLinks;

    @JsonProperty("tags")
    private final Optional<String[]> m_tags;

    WorkflowSetMeta(final Optional<String> author, final Optional<String> comments) {
        m_author = author;

        if (comments.isPresent()) {
            final String[] lines = comments.get().split(System.getProperty("line.separator"));

            String title = null;
            int start = 0;
            // If the first line is less than 20 words, followed by a blank line, then another line which isn't
            // one of the special "tags" -> assume the first line is a title
            if (lines.length > 2 && (!lines[0].isEmpty() && lines[1].isEmpty() && !lines[2].isEmpty())
                && !(lines[2].startsWith("BLOG:") || lines[2].startsWith("URL:") || lines[2].startsWith("VIDEO:")
                    || lines[2].startsWith("TAG:") || lines[2].startsWith("TAGS:")) && lines[0].split(" ").length < 20) {
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
            m_description = Optional.ofNullable(b.toString().trim());

            final List<String> blogs = new ArrayList<>();
            final List<String> urls = new ArrayList<>();
            final List<String> videos = new ArrayList<>();
            final List<String> tags = new ArrayList<>();
            for (int i = stop; i < lines.length; i++) {
                if (lines[i].startsWith("BLOG:")) {
                    blogs.add(createURLTag(lines[i]));
                } else if (lines[i].startsWith("URL:")) {
                    urls.add(createURLTag(lines[i]));
                } else if (lines[i].startsWith("VIDEO:")) {
                    videos.add(createURLTag(lines[i]));
                } else if (lines[i].startsWith("TAG:") || lines[i].startsWith("TAGS:")) {
                    final String[] split = lines[i].split(":");
                    final String[] ts = split[1].split(",");
                    for (final String t : ts) {
                        tags.add(t.trim());
                    }
                }
            }

            m_blogLinks = Optional.ofNullable(blogs.toArray(new String[blogs.size()]));
            m_additionalLinks = Optional.ofNullable(urls.toArray(new String[urls.size()]));
            m_videoLinks = Optional.ofNullable(videos.toArray(new String[videos.size()]));
            m_tags = Optional.ofNullable(tags.toArray(new String[tags.size()]));
        } else {
            m_title = Optional.empty();
            m_description = Optional.empty();
            m_blogLinks = Optional.empty();
            m_videoLinks = Optional.empty();
            m_additionalLinks = Optional.empty();
            m_tags = Optional.empty();
        }
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
     * Returns the list of blog links associated with this workflow, as listed in the workflowset.meta file, if present.
     *
     * @return list of blog links
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<String[]> getBlogLinks() {
        if (m_blogLinks == null) {
            throw new UnsupportedOperationException("getBlogLinks() is not supported, field was not read");
        }
        return m_blogLinks;
    }

    /**
     * Returns the the list of video links associated with this workflow, as listed in the workflowset.meta file, if
     * present.
     *
     * @return list of video links
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<String[]> getVideoLinks() {
        if (m_videoLinks == null) {
            throw new UnsupportedOperationException("getVideoLinks() is not supported, field was not read");
        }
        return m_videoLinks;
    }

    /**
     * Returns the the list of additional information links associated with this workflow, as listed in the
     * workflowset.meta file, if present.
     *
     * @return list of additional information links
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<String[]> getAdditionalLinks() {
        if (m_additionalLinks == null) {
            throw new UnsupportedOperationException("getAdditionalLinks() is not supported, field was not read");
        }
        return m_additionalLinks;
    }

    /**
     * Returns the list of tags associated with this workflow, as listed in the workflowset.meta file, if present.
     *
     * @return list of workflow tags
     * @throws UnsupportedOperationException if field is null, and therefore wasn't read
     */
    public Optional<String[]> getTags() {
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
                .append(m_blogLinks, other.m_blogLinks)
                .append(m_videoLinks, other.m_videoLinks)
                .append(m_additionalLinks, other.m_additionalLinks)
                .append(m_tags, other.m_tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_author)
                .append(m_title)
                .append(m_description)
                .append(m_blogLinks)
                .append(m_videoLinks)
                .append(m_additionalLinks)
                .append(m_tags)
                .toHashCode();
    }

    @Override
    public String toString() {
        String blogs = null;
        if (m_blogLinks.isPresent()) {
            blogs = StringUtils.join(m_blogLinks.get(), ", ");
        }

        String videos = null;
        if (m_videoLinks.isPresent()) {
            videos = StringUtils.join(m_videoLinks.get(), ", ");
        }

        String urls = null;
        if (m_additionalLinks.isPresent()) {
            urls = StringUtils.join(m_additionalLinks.get(), ", ");
        }

        String tags = null;
        if (m_tags.isPresent()) {
            tags = StringUtils.join(m_tags.get(), ", ");
        }
        return "author: " + m_author.orElse(null) + "\n" +
                "title: " + m_title.orElse(null) + "\n" +
                "description: " + m_description.orElse(null) + "\n" +
                "blogLinks: " + blogs + "\n" +
                "videoLinks: " + videos + "\n" +
                "additionalLinks: " + urls + "\n" +
                "tags: " + tags;

    }

    // -- Helper methods --

    private static String createURLTag(final String s) {
        final int textIndex = s.indexOf(':');
        final int urlIndex = s.indexOf("http");
        final String text = s.substring(textIndex + 1, urlIndex).trim();
        final String href = s.substring(urlIndex, s.length()).trim();
        return "<a href=\"" + href + "\">" + text + "</a>";
    }
}
