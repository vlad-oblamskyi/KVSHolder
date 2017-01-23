package com.intellecteu.hyperledger.json.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellecteu.hyperledger.json.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserValueFactory extends ObjectFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserKeyFactory.class);

    public static UserValue getUserValue(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, UserValue.class);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while parsing json {}", json, ex);
        }
        return null;
    }

    public static UserValue getUserValue(String password, List<Permission> permissions) {
        return new UserValue(password, permissions);
    }
}
