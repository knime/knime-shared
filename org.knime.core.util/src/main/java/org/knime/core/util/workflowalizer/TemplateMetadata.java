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
 *   Oct 16, 2018 (awalter): created
 */
package org.knime.core.util.workflowalizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Metadata pertaining to KNIME templates.
 *
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonTypeInfo(include = As.PROPERTY, use = Id.CLASS)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TemplateMetadata extends AbstractWorkflowMetadata<TemplateMetadataBuilder> {

    @JsonProperty("author_information")
    private final AuthorInformation m_authorInfo;

    @JsonProperty("template_information")
    private final TemplateInformation m_templateInfo;

    TemplateMetadata(final TemplateMetadataBuilder builder) {
        super(builder);
        m_authorInfo = new AuthorInformation(builder.getAuthor(), builder.getAuthorDate(), builder.getLastEditor(),
            builder.getLastEditDate());
        m_templateInfo = new TemplateInformation(builder.getRole(), builder.getTimeStamp(), builder.getSourceURI(),
            builder.getType());
    }

    /**
     * @return the {@link AuthorInformation} associated with this template
     */
    public AuthorInformation getAuthorInformation() {
        return m_authorInfo;
    }

    /**
     * @return the {@link TemplateInformation} associated with this template
     */
    public TemplateInformation getTemplateInformation() {
        return m_templateInfo;
    }

    /**
     * @return an {@link ObjectMapper} configured to read TemplateMetadata
     */
    public static ObjectMapper getConfiguredObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        // don't write null fields
        mapper.setSerializationInclusion(Include.NON_NULL);

        // Jdk8Module will serialize Optional fields as their value or null
        mapper.registerModule(new Jdk8Module());

        // Add custom serializers for Version and ConfigBase fields
        final SimpleModule sm = new SimpleModule();
        sm.addSerializer(new VersionSerializer());
        sm.addSerializer(new ConfigBaseEntrySerializer());
        mapper.registerModule(sm);

        // Add format for serializing Date fields
        // @JsonFormat isn't used because it converts Date objects to UTC time
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        mapper.setDateFormat(df);

        return mapper;
    }
}
