package com.intellecteu.hyperledger.chaincode.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CharMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    public static String getJsonStringFromChaincodeResponse(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            LOGGER.warn("Response is empty: {}", jsonResponse);
            return "";
        }

        if (((!jsonResponse.contains("[") || !jsonResponse.contains("]"))) && (!jsonResponse.contains("{") || !jsonResponse.contains("}"))) {
            LOGGER.warn("Response is not in JSON format: {}", jsonResponse);
            return "";
        }
        LOGGER.debug("Response before clean up:{} length:{}", jsonResponse, jsonResponse.length());

        jsonResponse = CharMatcher.INVISIBLE.removeFrom(jsonResponse);

        LOGGER.debug("After remove invisible:{} length:{}", jsonResponse, jsonResponse.length());

        if ((jsonResponse.contains("{") && jsonResponse.contains("}"))) {
            int start = CharMatcher.is('{').indexIn(jsonResponse);
            int end = CharMatcher.is('}').lastIndexIn(jsonResponse);
            jsonResponse = jsonResponse.substring(start, end + 1);

        } else if ((jsonResponse.contains("[") && jsonResponse.contains("]"))) {
            /*jsonResponse = jsonResponse.substring(jsonResponse.indexOf("[")).substring(0, jsonResponse.lastIndexOf("]"));*/

            int start = CharMatcher.is('[').indexIn(jsonResponse);
            int end = CharMatcher.is(']').lastIndexIn(jsonResponse);
            jsonResponse = jsonResponse.substring(start, end + 1);
        }

        LOGGER.debug("After clean up:{} length:{}", jsonResponse, jsonResponse.length());

        return jsonResponse;
    }

    public static String base64Decode(String input) {
        byte[] tmpPermissions = Base64.getDecoder().decode(input);
        return new String(tmpPermissions, StandardCharsets.UTF_8);
    }

    public static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String Error(String errorMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        GeneralError generalError = new GeneralError(errorMessage);
        try {
            return objectMapper.writeValueAsString(generalError);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while writing error message {} to the output ", errorMessage, ex);
        }
        return "{\"Error\":\"Exception occurred\"}";
    }

    public static String Failure(String errorMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        Response generalError = new Response("Failure", errorMessage, null);
        try {
            return objectMapper.writeValueAsString(generalError);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while writing failure message {} to the output ", errorMessage, ex);
        }
        return "{\"status\":\"Failure\",\"message\":\"Exception occurred.\"}";
    }

    public static String AuthToken(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        Response generalError = new Response("OK", null, token);
        try {
            return objectMapper.writeValueAsString(generalError);
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while writing auth token {} to the output ", token, ex);
        }
        return "{\"status\":\"Failure\",\"message\":\"Exception occurred.\"}";
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static final class Response {
        @JsonProperty("status")
        private String status;
        @JsonProperty("message")
        private String message;
        @JsonProperty("authToken")
        private String authToken;

        public Response(String status, String message, String authToken) {
            this.status = status;
            this.message = message;
            this.authToken = authToken;
        }

    }

    private static final class GeneralError {
        @JsonProperty("Error")
        private String error;

        private GeneralError(String error) {
            this.error = error;
        }
    }
}
