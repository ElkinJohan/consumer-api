package com.daicode.consumer_api.util.sanitizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputTypeDetector {

    private final ObjectMapper objectMapper;

    public InputType detect(String input) {
        if (isJson(input)) return InputType.JSON;
        if (isXml(input)) return InputType.XML;
        if (looksLikeHtml(input)) return InputType.HTML;
        return InputType.TEXT;
    }

    private boolean isJson(String input) {
        try {
            objectMapper.readTree(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isXml(String input) {
        return input.startsWith("<?xml");
    }

    private boolean looksLikeHtml(String input) {
        return input.matches("(?i).*<[^>]+>.*");
    }
}
