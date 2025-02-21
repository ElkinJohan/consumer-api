package com.daicode.consumer_api.util;

import com.daicode.consumer_api.dto.SubjectLoggerDTO;
import com.daicode.consumer_api.exception.PublishLoggerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class PublishLogger {

    private final ObjectMapper objectMapper;

    public void publish(SubjectLoggerDTO subjectLoggerDTO, Object request, Object response) {
        UUID transactionId = subjectLoggerDTO.getTransactionId();

        subjectLoggerDTO.setRequest(convertToMap(request, transactionId, "request"));
        subjectLoggerDTO.setResponse(convertToMap(response, transactionId, "response"));

        try {
            subjectLoggerDTO.setHash(subjectLoggerDTO.generateHash());
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("Failed to generate Hash for subjectLoggerDTO with transactionId {}", transactionId);
            throw new PublishLoggerException("Error generating Hash for logger", e);
        }
    }

    private Map<String, Object> convertToMap(Object data, UUID transactionId, String type) {
        String rawTextKey = type.equals("request") ? "body" : "message";

        try {
            if (data instanceof String jsonString) {
                return isValidJson(jsonString)
                        ? objectMapper.readValue(jsonString, new TypeReference<>() {
                })
                        : Map.of(rawTextKey, jsonString);
            }
            return objectMapper.convertValue(data, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Failed to convert {} to Map with transactionId {}", type, transactionId);
            throw new PublishLoggerException("Error converting " + type + " to Map.", e);
        }
    }

    private boolean isValidJson(String jsonString) {
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
