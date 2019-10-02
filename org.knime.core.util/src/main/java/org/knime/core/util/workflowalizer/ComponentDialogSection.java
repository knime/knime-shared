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
 *   Jul 1, 2019 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for a component dialog section.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.13
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public final class ComponentDialogSection {

    @JsonProperty("sectionHeader")
    private final Optional<String> m_sectionHeader;

    @JsonProperty("sectionDescription")
    private final Optional<String> m_sectionDescription;

    @JsonProperty("fields")
    private final List<Field> m_fields;

    /**
     * Creates a new dialog section with the given header, description, and fields.
     *
     * @param sectionHeader the optional section header
     * @param sectionDescription the optional section description
     * @param fields the fields
     */
    ComponentDialogSection(final Optional<String> sectionHeader, final Optional<String> sectionDescription,
        final List<Field> fields) {
        m_sectionHeader = sectionHeader;
        m_sectionDescription = sectionDescription;
        m_fields = fields.stream().filter(f -> !f.isEmpty())
            .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Returns the name of this dialog section.
     *
     * <p>
     * This cannot be set from KNIME AP.
     * </p>
     *
     * @return the section header
     */
    public Optional<String> getSectionHeader() {
        return m_sectionHeader;
    }

    /**
     * Returns the description of this dialog section.
     *
     * <p>
     * This cannot be set from KNIME AP.
     * </p>
     *
     * @return the section description
     */
    public Optional<String> getSectionDescription() {
        return m_sectionDescription;
    }

    /**
     * Returns the fields included in this dialog section.
     *
     * @return the fields
     */
    public List<Field> getFields() {
        return m_fields;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();
        if (m_sectionHeader.isPresent()) {
            builder.append("header: " + m_sectionHeader.get() + ", ");
        }

        if (m_sectionDescription.isPresent()) {
            builder.append("description: " + m_sectionDescription.get() + ", ");
        }

        builder.append("fields: [");
        if (!m_fields.isEmpty()) {
            for (final Field f : m_fields) {
                builder.append("{" + f.toString() + "}, ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }

    /**
     * Returns {@code true} if this dialog section is empty, {@code false} otherwise. A {@code ComponentDialogSection}
     * is empty if it has no header, description, or fields.
     *
     * @return if dialog section is empty
     */
    boolean isEmpty() {
        return !m_sectionHeader.isPresent() && !m_sectionDescription.isPresent() && m_fields.isEmpty();
    }

    /**
     * POJO for representing a single component dialog field.
     */
    @JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE)
    public static final class Field {

        @JsonProperty("name")
        private final Optional<String> m_name;

        @JsonProperty("description")
        private final Optional<String> m_description;

        @JsonProperty("optional")
        private final boolean m_optional;

        /**
         * Creates a new dialog field.
         *
         * @param name optional name of the field
         * @param description optional description of the field
         * @param optional if the field is optional or not
         */
        Field(final Optional<String> name, final Optional<String> description, final boolean optional) {
            // Replace optionals containing empty Strings with empty optionals
            m_name = name.isPresent() && StringUtils.isEmpty(name.get()) ? Optional.empty() : name;
            m_description =
                description.isPresent() && StringUtils.isEmpty(description.get()) ? Optional.empty() : description;
            m_optional = optional;
        }

        /**
         * Returns the name of this field/option.
         *
         * @return the name
         */
        public Optional<String> getName() {
            return m_name;
        }

        /**
         * Returns the description of this field/option.
         *
         * @return the description
         */
        public Optional<String> getDescription() {
            return m_description;
        }

        /**
         * Returns {@code true} if this field/option is optional, otherwise returns {@code false}.
         *
         * <p>
         * This cannot be set from KNIME AP.
         * </p>
         *
         * @return if the field/optional is optional
         */
        public boolean isOptional() {
            return m_optional;
        }

        @Override
        public String toString() {
            if (isEmpty()) {
                return "";
            }

            final StringBuilder builder = new StringBuilder();
            if (m_name.isPresent()) {
                builder.append("name: " + m_name.get() + ", ");
            }

            if (m_description.isPresent()) {
                builder.append("description: " + m_description + ", ");
            }

            builder.append("optional: " + m_optional);
            return builder.toString();
        }

        /**
         * Returns {@code true} if this field is empty, {@code false} otherwise. A {@code Field} is empty if it has an
         * empty name and description.
         *
         * @return if dialog section is empty
         */
        boolean isEmpty() {
            return !m_name.isPresent() && !m_description.isPresent();
        }

    }

}
