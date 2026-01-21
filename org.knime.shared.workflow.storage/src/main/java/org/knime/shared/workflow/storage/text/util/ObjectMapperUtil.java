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
 */
package org.knime.shared.workflow.storage.text.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Utility class around Jackson's {@link ObjectMapper}. It set's up an {@link ObjectMapper}.
 *
 * @author Martin Horn, KNIME GmbH, Konstanz, Germany
 */
public final class ObjectMapperUtil {
    private static final ObjectMapperUtil INSTANCE = new ObjectMapperUtil();

    /**
     * Returns the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static ObjectMapperUtil getInstance() {
        return INSTANCE;
    }

    private ObjectMapper m_mapper;

    private ObjectMapperUtil() {
        //utility class
    }

    private static void configureObjectMapper(final ObjectMapper mapper) {
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());

        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        mapper.setSerializationInclusion(Include.NON_NULL);

        // TODO create mixins for intermediate workflow format
//        JsonUtil.addWebUIMixIns(mapper);
//        JsonUtil.addIDEntityDeSerializer(mapper);
//        JsonUtil.addDateTimeDeSerializer(mapper);
//        JsonUtil.addBitSetDeSerializer(mapper);
    }

    /**
     * Returns the shared object mapper.
     *
     * @return an object mapper
     */
    public ObjectMapper getObjectMapper() {
        if (m_mapper == null) {
            m_mapper = createObjectMapper();
        }
        return m_mapper;
    }

    private static ObjectMapper createObjectMapper() {
        var mapper = new ObjectMapper();
        configureObjectMapper(mapper);
        return mapper;
    }

    /**
     * Use the {@link ObjectMapper} returned by {@link #getObjectMapper()} to convert the given object to string.
     *
     * @param object object to represent as JSON
     * @return JSON representation of the given object
     * @throws JsonProcessingException if there are problems generating the JSON
     */
    public static String toString(final Object object) throws JsonProcessingException {
        return getInstance().getObjectMapper().writeValueAsString(object);
    }

    /**
     * Use the {@link ObjectMapper} returned by {@link #getObjectMapper()} to parse the given JSON back into an object.
     *
     * @param json the string to parse
     * @param clazz the class of the object to return
     * @return the parsed string
     * @throws JsonProcessingException if the content cannot be parsed
     * @throws JsonMappingException if the JSON structure does not match the structure defined by the given object class
     */
    public static <T> T fromString(final String json, final Class<T> clazz) throws JsonProcessingException {
        return getInstance().getObjectMapper().readValue(json, clazz);
    }

}
