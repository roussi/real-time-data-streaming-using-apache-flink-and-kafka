package com.abdelrani.stockstreams.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class JacksonConfiguration {

    public static final ObjectMapper MAPPER = defaultObjectMapper();


    private static ObjectMapper defaultObjectMapper() {
        return JsonMapper.builder()
                .addModules(new JavaTimeModule(), new Jdk8Module())
                .serializationInclusion(NON_NULL)
                .serializationInclusion(NON_EMPTY)
                .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy())
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
    }
}
