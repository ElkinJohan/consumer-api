package com.daicode.consumer_api.util;

import com.daicode.consumer_api.dto.SubjectLoggerDTO;
import com.daicode.consumer_api.exception.PublishLoggerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class PublishLogger {

    private final ObjectMapper objectMapper;

    public void publish(SubjectLoggerDTO subjectLoggerDTO, Object request, Object response) {

        Map<String, Object> body = Map.of("body", request);
        Map<String, Object> responseClient = Map.of("message", response);
        subjectLoggerDTO.setRequest(body);
        subjectLoggerDTO.setResponse(responseClient);

        try {
            subjectLoggerDTO.setHash(subjectLoggerDTO.generateHash());
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("Failed to generate Hash for subjectLoggerDTO with transactionId {}", subjectLoggerDTO.getTransactionId());
            throw new PublishLoggerException("Error generating Hash for logger", e);
        }
        log.info("Publish in logger {}", subjectLoggerDTO.toString());

    }
}
