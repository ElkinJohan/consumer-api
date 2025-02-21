package com.daicode.consumer_api.util;

import com.daicode.consumer_api.dto.SubjectLoggerDTO;
import com.daicode.consumer_api.exception.PublishLoggerException;
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
    private ObjectMapper objectMapper;

    @Mock
    private Connection natsConnection;

    @InjectMocks
    private PublishLogger publishLogger;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        publishLogger.setSubjectLogger("test-subject");
    }

    @Test
    void testPublishSuccess() throws IOException, NoSuchAlgorithmException {
        SubjectLoggerDTO dto = mock(SubjectLoggerDTO.class);
        when(dto.getTransactionId()).thenReturn(UUID.randomUUID());
        when(dto.generateHash()).thenReturn("someHash");
        when(objectMapper.writeValueAsString(any())).thenReturn("jsonString");

        publishLogger.publish(dto, "{}", "{}");

        verify(natsConnection, times(1)).publish(eq("test-subject"), any(byte[].class));
    }

    @Test
    void testPublishThrowsException() throws IOException, NoSuchAlgorithmException {
        SubjectLoggerDTO dto = mock(SubjectLoggerDTO.class);
        when(dto.getTransactionId()).thenReturn(UUID.randomUUID());
        when(dto.generateHash()).thenThrow(NoSuchAlgorithmException.class);

        assertThrows(PublishLoggerException.class, () -> {
            publishLogger.publish(dto, "{}", "{}");
        });
    }
}