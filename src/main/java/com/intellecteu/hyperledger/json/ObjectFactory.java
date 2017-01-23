package com.intellecteu.hyperledger.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectFactory.class);

    public static <T> T getJsonObject(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while parsing json {}", json, ex);
        }
        return null;
    }

    public static String toJsonString(Object jsonObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(jsonObject);
    }
}
