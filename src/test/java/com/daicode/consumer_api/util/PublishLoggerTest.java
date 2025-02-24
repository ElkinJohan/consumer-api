package com.daicode.consumer_api.util;

import com.daicode.consumer_api.dto.SubjectLoggerDTO;
import com.daicode.consumer_api.exception.PublishLoggerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PublishLoggerTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private Connection mockNatsConnection;

    @InjectMocks
    private PublishLogger publishLogger;
    @Mock
    private SubjectLoggerDTO mockSubjectLoggerDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        publishLogger.setSubjectLogger("test-subject");
    }

    @Test
    void testPublishSuccess_request_response_stringJson() throws IOException, NoSuchAlgorithmException {
        when(mockSubjectLoggerDTO.getTransactionId()).thenReturn(UUID.randomUUID());
        when(mockSubjectLoggerDTO.generateHash()).thenReturn("someHash");
        when(mockObjectMapper.writeValueAsString(any())).thenReturn("jsonString");

        publishLogger.publish(mockSubjectLoggerDTO, "{\"test\":\"test-response\"}", "{\"test\":\"test-response\"}");

        verify(mockNatsConnection, times(1)).publish(eq("test-subject"), any(byte[].class));
    }

    @Test
    void testPublishSuccess_request_response_rawTextKey() throws IOException, NoSuchAlgorithmException {
        when(mockSubjectLoggerDTO.getTransactionId()).thenReturn(UUID.randomUUID());
        when(mockSubjectLoggerDTO.generateHash()).thenReturn("someHash");
        when(mockObjectMapper.readTree(anyString())).thenThrow(JsonProcessingException.class);
        when(mockObjectMapper.writeValueAsString(any())).thenReturn("textPlain");

        publishLogger.publish(mockSubjectLoggerDTO, "rawTextKey", "rawTextKey");

        verify(mockNatsConnection, times(1)).publish(eq("test-subject"), any(byte[].class));
    }

    @Test
    void testPublishSuccess_request_response_isDTO() throws IOException, NoSuchAlgorithmException {
        Object someDTO = mock(Object.class);
        when(mockSubjectLoggerDTO.getTransactionId()).thenReturn(UUID.randomUUID());
        when(mockSubjectLoggerDTO.generateHash()).thenReturn("someHash");
        when(mockObjectMapper.writeValueAsString(any())).thenReturn("{\"string\":\"json\"}");

        publishLogger.publish(mockSubjectLoggerDTO, someDTO, someDTO);

        verify(mockNatsConnection, times(1)).publish(eq("test-subject"), any(byte[].class));
    }

    @Test
    void testPublishThrowsException() throws IOException, NoSuchAlgorithmException {
        SubjectLoggerDTO dto = mock(SubjectLoggerDTO.class);
        when(dto.getTransactionId()).thenReturn(UUID.randomUUID());
        when(dto.generateHash()).thenThrow(NoSuchAlgorithmException.class);

        assertThrows(PublishLoggerException.class, () -> publishLogger.publish(dto, "{}", "{}"));
    }

    @Test
    void testPublish_ThrowsPublishLoggerExceptionOnJsonProcessingError() throws Exception {
        String jsonString = "{\"key\":\"value\"}";

        when(mockObjectMapper.writeValueAsString(any(SubjectLoggerDTO.class))).thenThrow(new JsonProcessingException("Error") {
        });

        assertThrows(PublishLoggerException.class, () -> publishLogger.publish(mock(SubjectLoggerDTO.class), jsonString, "response"));
    }
    /*@Test
    void testPublish_JsonProcessingException() throws IOException {
        when(mockSubjectLoggerDTO.getTransactionId()).thenReturn(UUID.randomUUID());
        when(mockObjectMapper.readValue(anyString(), eq(TypeReference.class))).thenThrow(JsonProcessingException.class);

        assertThrows(PublishLoggerException.class, () -> publishLogger.publish(mockSubjectLoggerDTO, "{}", "{}"));
    }*/
}
