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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Metadata pertaining to KNIME templates.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Alison Walter, KNIME GmbH, Konstanz, Germany
 * @since 5.10
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TemplateMetadata extends AbstractWorkflowMetadata<TemplateMetadataBuilder> {

    @JsonProperty("authorInformation")
    private final AuthorInformation m_authorInfo;

    @JsonProperty("templateInformation")
    private final TemplateInformation m_templateInfo;

    TemplateMetadata(final TemplateMetadataBuilder builder) {
        super(builder);
        m_authorInfo = new AuthorInformation(builder.getAuthor(), builder.getAuthorDate(), builder.getLastEditor(),
            builder.getLastEditDate());
        m_templateInfo = new TemplateInformation(builder.getRole(), builder.getTimeStamp(), builder.getSourceURI(),
            builder.getType());
    }

    /**
     * For internal use only! This constructor creates a copy of the given {@code TemplateMetadata}, but sets the
     * connections to {@code null} and flattens the node tree
     *
     * @param template the {@code TemplateMetadata} to copy
     */
    private TemplateMetadata(final TemplateMetadata template) {
        super(template);
        m_authorInfo = template.m_authorInfo;
        m_templateInfo = template.m_templateInfo;
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
     * Returns whether this "workflow" is a metanode template or not.
     *
     * @return {@code true} if the workflow is a template
     */
    @JsonProperty("template")
    public boolean isTemplate() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", " + m_authorInfo +
                ", " + m_templateInfo;
    }

    /**
     * @return an {@link ObjectMapper} configured to read TemplateMetadata
     */
    public static ObjectMapper getConfiguredObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        // don't write null fields
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

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

    /**
     * Creates a {@code TemplateMetadata} with a flattened node list for writing to JSON.
     *
     * @return a {@code TemplateMetadata} whose node list is flat (i.e. depth = 1), and whose connections have been set
     *         to {@code null}
     */
    public TemplateMetadata flatten() {
        return new TemplateMetadata(this);
    }
}
