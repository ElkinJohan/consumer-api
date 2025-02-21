package com.daicode.consumer_api.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishLoggerException extends RuntimeException {
    public PublishLoggerException(String message, Throwable cause) {
        super(message, cause);
        log.error("PublishLoggerException occurred: {}", message, cause);
    }
}