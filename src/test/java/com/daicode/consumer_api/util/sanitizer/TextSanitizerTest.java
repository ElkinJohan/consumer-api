package com.daicode.consumer_api.util.sanitizer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class TextSanitizerTest {

    @InjectMocks
    private TextSanitizer sanitizer;

    @ParameterizedTest(name = "{index}: sanitize(\"{0}\") = \"{1}\"")
    @MethodSource("provideInputsForSanitization")
    void test_sanitize_variousCases(String input, String expected) {
        String result = sanitizer.sanitize(input);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideInputsForSanitization() {
        return Stream.of(
                arguments("", ""),
                arguments("<script>alert('XSS')</script>Safe", "Safe"),
                arguments("a&b'c\"d+e=f@g", "a&amp;b&#39;c&#34;d&#43;e&#61;f&#64;g"),
                arguments("normal text 123", "normal text 123"),
                arguments("<b>Hello</b> & 'world' + @", "Hello &amp; &#39;world&#39; &#43; &#64;")
        );
    }
}