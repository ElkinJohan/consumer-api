package com.daicode.consumer_api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublishLoggerExceptionTest {

    @Test
    void testExceptionMessageAndCause() {
        String message = "Test error message";
        Throwable cause = new RuntimeException("Root cause");

        PublishLoggerException exception = new PublishLoggerException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
