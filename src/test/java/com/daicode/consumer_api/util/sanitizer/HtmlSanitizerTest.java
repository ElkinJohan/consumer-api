package com.daicode.consumer_api.util.sanitizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HtmlSanitizerTest {

    @InjectMocks
    private HtmlSanitizer sanitizer;

    @Test
    void test_sanitize_preservesSafeTags() {
        String input = "<p>Hello <strong>world</strong></p>";
        String result = sanitizer.sanitize(input);
        assertEquals("<p>Hello <strong>world</strong></p>", result);
    }

    @Test
    void test_sanitize_removesUnsafeTags() {
        String input = "<script>alert('x')</script><p>ok</p>";
        String result = sanitizer.sanitize(input);
        assertEquals("<p>ok</p>", result);
    }

    @Test
    void test_sanitize_replacesSpecialCharacters() {
        String input = "a&b'c\"d+e=f@g";
        String result = sanitizer.sanitize(input);

        String expected = "a&amp;b&#39;c&#34;d&#43;e&#61;f&#64;g";
        assertEquals(expected, result);
    }

}
