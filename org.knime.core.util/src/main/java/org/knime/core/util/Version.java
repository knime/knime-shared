/*
 * ------------------------------------------------------------------------
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
 * Created on 01.10.2013 by thor
 */
package org.knime.core.util;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Simple class the represents a three digit version.
 *
 * @author Thorsten Meinl, KNIME AG, Zurich, Switzerland
 * @since 4.5
 */
public final class Version implements Comparable<Version>, Serializable {

    /** Empty version 0.0.0
     * @since 5.10*/
    public static final Version EMPTY_VERSION = new Version(0, 0, 0);

    private final int m_major;

    private final int m_minor;

    private final int m_revision;

    private final String m_qualifier;

    /**
     * Creates a new version.
     *
     * @param major the major version
     * @param minor the minor version
     * @param revision the revision
     */
    public Version(final int major, final int minor, final int revision) {
        m_major = major;
        m_minor = minor;
        m_revision = revision;
        m_qualifier = "";
    }

    /**
     * Creates a new version.
     *
     * @param major the major version
     * @param minor the minor version
     * @param revision the revision
     * @param qualifier the qualifier String
     * @since 5.10
     */
    @JsonCreator
    public Version(@JsonProperty("major") final int major, @JsonProperty("minor") final int minor,
        @JsonProperty("revision") final int revision, @JsonProperty("qualifier") final String qualifier) {
        m_major = major;
        m_minor = minor;
        m_revision = revision;
        m_qualifier = qualifier == null ? "" : qualifier;
    }

    /**
     * Create a new version from a string. The three version parts must be separated by dots (e.g. 1.2.3). At least the
     * major version must be present, minor and revision may be omitted.
     *
     * @param version a version string
     * @throws IllegalArgumentException if the version string is invalid
     * @throws NumberFormatException if a version part is not numeric
     */
    public Version(final String version) {
        final String[] parts = version.split("\\.");
        if (parts.length < 1) {
            throw new IllegalArgumentException("Wrong version format: " + version);
        }

        // Major version
        m_major = Integer.parseInt(parts[0]);

        // Minor version
        if (parts.length > 1) {
            m_minor = Integer.parseInt(parts[1]);
        } else {
            m_minor = 0;
        }

        // Revision version
        String revision = null;
        String qualifier = "";

        if (parts.length > 2) {
            int split = 0;
            for (int i = 0; i < parts[2].length(); i++) {
                if (!Character.isDigit(parts[2].charAt(i))) {
                    split = i;
                    break;
                }
            }
            if (split == 0) {
                revision = parts[2];
                qualifier = "";
            }
            else {
                revision = parts[2].substring(0, split);
                qualifier = parts[2].substring(split, parts[2].length());
            }
            m_revision = Integer.parseInt(revision);
        } else {
            m_revision = 0;
        }

        // Qualifier string
        if (parts.length > 3) {
            for (int i = 3; i < parts.length; i++) {
                qualifier = qualifier + "." +  parts[i];
            }
        }
        m_qualifier = qualifier;
    }

    /**
     * @return the major
     */
    @JsonProperty("major")
    public int getMajor() {
        return m_major;
    }

    /**
     * @return the minor
     */
    @JsonProperty("minor")
    public int getMinor() {
        return m_minor;
    }

    /**
     * @return the revision
     */
    @JsonProperty("revision")
    public int getRevision() {
        return m_revision;
    }

    /**
     * @return the qualifier String
     * @since 5.10
     */
    @JsonProperty("qualifier")
    public String getQualifier() {
        return m_qualifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Version o) {
        int diff = this.m_major - o.m_major;
        if (diff != 0) {
            return diff;
        }
        diff = this.m_minor - o.m_minor;
        if (diff != 0) {
            return diff;
        }
        diff = this.m_revision - o.m_revision;
        if (diff != 0) {
            return diff;
        }
        return m_qualifier.compareTo(o.m_qualifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return m_major + "." + m_minor + "." + m_revision + m_qualifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_major)
                .append(m_minor)
                .append(m_revision)
                .append(m_qualifier)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Version other = (Version)obj;
        return new EqualsBuilder()
                .append(m_major, other.getMajor())
                .append(m_minor, other.getMinor())
                .append(m_revision, other.getRevision())
                .append(m_qualifier, other.getQualifier())
                .isEquals();
    }

    /**
     * Checks whether the version is the same or newer than the reference version.
     *
     * @param version the version to check against
     * @return <code>true</code> if version is the same or newer than the reference version, <code>false</code>
     *         otherwise
     */
    public boolean isSameOrNewer(final Version version) {
        return compareTo(version) >= 0;
    }
}
