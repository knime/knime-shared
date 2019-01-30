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
 *   Sep 12, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Meta data such as who create the workflow and who edited it last and when.
 *
 * <p>Extracted from KNIME core WorkflowManager.</p>
 *
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public final class AuthorInformation {

    /** Info for workflows created prior 2.8. */
    public static final AuthorInformation UNKNOWN = new AuthorInformation("<unknown>", new Date(0), Optional.empty(), Optional.empty());

    @JsonProperty("author")
    private final String m_author;

    @JsonProperty("authoredDate")
    private final Date m_authoredDate;

    @JsonProperty("lastDditor")
    private final Optional<String> m_lastEditor;

    @JsonProperty("lastEditedDate")
    private final Optional<Date> m_lastEditDate;

    /**
     * Creates AuthorInformatio using "user.name" property as author and current system time as authored date. The edit
     * fields are empty.
     */
    public AuthorInformation() {
        this(System.getProperty("user.name"), new Date(), Optional.empty(), Optional.empty());
    }

    /**
     * @param past used to provide author name and authored date, edit fields use system properties
     */
    public AuthorInformation(final AuthorInformation past) {
        this(past.m_author, past.m_authoredDate, System.getProperty("user.name"), new Date());
    }

    /**
     * @param author Original author.
     * @param authoredDate Original authored date.
     * @param lastEditor Name of last editor.
     * @param lastEditDate Date of last edit.
     */
    public AuthorInformation(final String author, final Date authoredDate, final String lastEditor,
        final Date lastEditDate) {
        this(author, authoredDate, Optional.ofNullable(lastEditor), Optional.ofNullable(lastEditDate));
    }

    AuthorInformation(final String author, final Date authoredDate, final Optional<String> lastEditor,
        final Optional<Date> lastEditDate) {
        m_author = author;
        m_authoredDate = authoredDate;
        m_lastEditor = lastEditor;
        m_lastEditDate = lastEditDate;
    }

    /** @return Name of the workflow author (person). Null when not saved yet. */
    public String getAuthor() {
        return m_author;
    }

    /** @return Date when the workflow was saved the first time. Can be null. */
    public Date getAuthoredDate() {
        return m_authoredDate;
    }

    /** @return Name of the person who edited the workflow last (on last save). Null when not saved yet. */
    public Optional<String> getLastEditor() {
        if (m_lastEditor == null) {
            throw new UnsupportedOperationException("getLastEditor() is not supported, field was not read");
        }
        return m_lastEditor;
    }

    /** @return Date when workflow was saved last. */
    public Optional<Date> getLastEditDate() {
        if (m_lastEditDate == null) {
            throw new UnsupportedOperationException("getLastEditDate() is not supported, field was not read");
        }
        return m_lastEditDate;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AuthorInformation)) {
            return false;
        }
        final AuthorInformation other = (AuthorInformation) obj;
        return new EqualsBuilder()
                .append(m_author, other.m_author)
                .append(m_authoredDate, other.m_authoredDate)
                .append(m_lastEditor, other.m_lastEditor)
                .append(m_lastEditDate, other.m_lastEditDate)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_author)
                .append(m_authoredDate)
                .append(m_lastEditor)
                .append(m_lastEditDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        final String ed = m_lastEditDate.isPresent() ? df.format(m_lastEditDate.get()) : null;
        return "author: " + m_author +
        ", authored_date: " + df.format(m_authoredDate) +
        ", last_editor: " + m_lastEditor.orElse(null) +
        ", last_edited_date: " + ed;
    }
}
