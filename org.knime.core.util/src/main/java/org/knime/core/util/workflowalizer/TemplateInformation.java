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
 * Represents metadata specific to KNIME templates, such as role, type etc.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public final class TemplateInformation {

    @JsonProperty("role")
    private final String m_role;

    @JsonProperty("timeStamp")
    private final Date m_timeStamp;

    @JsonProperty("sourceURI")
    private final Optional<String> m_sourceURI;

    @JsonProperty("templateType")
    private final String m_type;

    TemplateInformation(final String role, final Date timeStamp, final Optional<String> sourceURI, final String type) {
        m_role = role;
        m_timeStamp = timeStamp;
        m_sourceURI = sourceURI;
        m_type = type;
    }

    /**
     * @return the template role
     */
    public String getRole() {
        if (m_role == null) {
            throw new UnsupportedOperationException("getRole() is not supported, field was not read");
        }
        return m_role;
    }

    /**
     * @return the template time stamp
     */
    public Date getTimeStamp() {
        if (m_timeStamp == null) {
            throw new UnsupportedOperationException("getTimeStamp() is not supported, field was not read");
        }
        return m_timeStamp;
    }

    /**
     * @return the template source URI, if present
     */
    public Optional<String> getSourceURI() {
        if (m_sourceURI == null) {
            throw new UnsupportedOperationException("getSourceURI() is not supported, field was not read");
        }
        return m_sourceURI;
    }

    /**
     * @return the template type
     */
    public String getType() {
        if (m_type == null) {
            throw new UnsupportedOperationException("getType() is not supported, field was not read");
        }
        return m_type;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TemplateInformation)) {
            return false;
        }
        final TemplateInformation other = (TemplateInformation)obj;
        return new EqualsBuilder()
                .append(m_role, other.m_role)
                .append(m_timeStamp, other.m_timeStamp)
                .append(m_sourceURI, other.m_sourceURI)
                .append(m_type, other.m_type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_role)
                .append(m_timeStamp)
                .append(m_sourceURI)
                .append(m_type)
                .toHashCode();
    }

    @Override
    public String toString() {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return "role: " + m_role +
        ", time_stamp: " + df.format(m_timeStamp) +
        ", source_URI: " +  m_sourceURI.orElse(null) +
        ", template_type: " + m_type;
    }
}
