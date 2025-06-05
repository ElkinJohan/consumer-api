package com.daicode.consumer_api.util.sanitizer;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextSanitizer {

    public String sanitize(String value) {
        if (value == null) return "";

        String sanitized = Jsoup.clean(value, Safelist.none());

        return escapeSpecialChars(sanitized);
    }

    private String escapeSpecialChars(String value) {
        return value
                .replace("'", "&#39;")
                .replace("\"", "&#34;")
                .replace("+", "&#43;")
                .replace("=", "&#61;")
                .replace("@", "&#64;");
    }
}
