package com.daicode.consumer_api.util.sanitizer;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HtmlSanitizer {

    public String sanitize(String input) {
        if (input == null) return "";
        String clean = Jsoup.clean(input, Safelist.relaxed());
        return encodeSpecialChars(clean);
    }

    private String encodeSpecialChars(String value) {
        return value
                .replace("'", "&#39;")
                .replace("\"", "&#34;")
                .replace("+", "&#43;")
                .replace("=", "&#61;")
                .replace("@", "&#64;");
    }
}

