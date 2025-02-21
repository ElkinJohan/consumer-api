package com.daicode.consumer_api.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NatsConnectionException extends RuntimeException {
    public NatsConnectionException(String message) {
        super(message);
        log.error(message);
    }

    public NatsConnectionException(String message, Throwable cause) {
        super(message, cause);
        log.error(message, cause);
    }
}