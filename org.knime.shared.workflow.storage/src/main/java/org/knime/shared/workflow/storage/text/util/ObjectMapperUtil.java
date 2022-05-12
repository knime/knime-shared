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
