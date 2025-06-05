package com.daicode.consumer_api.util.sanitizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SanitizerTest {

    @Mock
    private JsonSanitizer jsonSanitizer;

    @Mock
    private XmlSanitizer xmlSanitizer;

    @Mock
    private HtmlSanitizer htmlSanitizer;

    @Mock
    private TextSanitizer textSanitizer;

    @Mock
    private InputTypeDetector inputTypeDetector;

    @InjectMocks
    private Sanitizer sanitizer;

    @BeforeEach
    void setUp() {
        // No setup required since we're using @InjectMocks
    }

    @Test
    void test_sanitize_nullInput_returnsNull() {
        assertEquals("", sanitizer.sanitize(null));
    }

    @Test
    void test_sanitize_jsonInput_delegatesToJsonSanitizer() {
        String input = "{\"key\": \"value\"}";
        when(inputTypeDetector.detect(input)).thenReturn(InputType.JSON);
        when(jsonSanitizer.sanitize(input)).thenReturn("json-clean");

        String result = sanitizer.sanitize(input);
        assertEquals("json-clean", result);
    }

    @Test
    void test_sanitize_xmlInput_delegatesToXmlSanitizer() {
        String input = "<note><body>Hello</body></note>";
        when(inputTypeDetector.detect(input)).thenReturn(InputType.XML);
        when(xmlSanitizer.sanitize(input)).thenReturn("xml-clean");

        String result = sanitizer.sanitize(input);
        assertEquals("xml-clean", result);
    }

    @Test
    void test_sanitize_htmlInput_delegatesToHtmlSanitizer() {
        String input = "<b>bold</b>";
        when(inputTypeDetector.detect(input)).thenReturn(InputType.HTML);
        when(htmlSanitizer.sanitize(input)).thenReturn("html-clean");

        String result = sanitizer.sanitize(input);
        assertEquals("html-clean", result);
    }

    @Test
    void test_sanitize_textInput_delegatesToTextSanitizer() {
        String input = "just text";
        when(inputTypeDetector.detect(input)).thenReturn(InputType.TEXT);
        when(textSanitizer.sanitize(input)).thenReturn("text-clean");

        String result = sanitizer.sanitize(input);
        assertEquals("text-clean", result);
    }

    @Test
    void test_sanitize_trimsInputBeforeDetectingType() {
        String input = "   <b>html</b>   ";
        when(inputTypeDetector.detect("<b>html</b>")).thenReturn(InputType.HTML);
        when(htmlSanitizer.sanitize("<b>html</b>")).thenReturn("html-clean");

        String result = sanitizer.sanitize(input);
        assertEquals("html-clean", result);
    }
}
