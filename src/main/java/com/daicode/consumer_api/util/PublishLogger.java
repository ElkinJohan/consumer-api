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

        Map<String, Object> body = convertToMap(request, transactionId);
        Map<String, Object> responseClient = Map.of("message", response);

        subjectLoggerDTO.setRequest(body);
        subjectLoggerDTO.setResponse(responseClient);

        try {
            subjectLoggerDTO.setHash(subjectLoggerDTO.generateHash());
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("Failed to generate Hash for subjectLoggerDTO with transactionId {}", transactionId);
            throw new PublishLoggerException("Error generating Hash for logger", e);
        }
    }

    private Map<String, Object> convertToMap(Object request, UUID transactionId) {
        try {
            if (request instanceof String jsonString) {
                if (isValidJson(jsonString)) {
                    return objectMapper.readValue(jsonString, new TypeReference<>() {
                    });
                } else {
                    return Map.of("body", jsonString);
                }
            } else {
                return objectMapper.convertValue(request, new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to convert loggerDTO request to Map with transactionId {}", transactionId);
            throw new PublishLoggerException("Error converting logger request to Map", e);
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
