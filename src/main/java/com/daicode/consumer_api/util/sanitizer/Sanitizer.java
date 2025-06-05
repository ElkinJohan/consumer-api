package com.daicode.consumer_api.util.sanitizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Sanitizer {

    private final JsonSanitizer jsonSanitizer;
    private final XmlSanitizer xmlSanitizer;
    private final HtmlSanitizer htmlSanitizer;
    private final TextSanitizer textSanitizer;
    private final InputTypeDetector inputTypeDetector;

    public String sanitize(Object input) {
        if (input == null) return "";
        String value = input.toString().trim();

        InputType type = inputTypeDetector.detect(value);

        return switch (type) {
            case JSON -> jsonSanitizer.sanitize(value);
            case XML -> xmlSanitizer.sanitize(value);
            case HTML -> htmlSanitizer.sanitize(value);
            case TEXT -> textSanitizer.sanitize(value);
        };
    }
}
