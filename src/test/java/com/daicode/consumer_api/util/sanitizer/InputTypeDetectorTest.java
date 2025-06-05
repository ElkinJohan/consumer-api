package com.daicode.consumer_api.util.sanitizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputTypeDetectorTest {

    private InputTypeDetector detector;

    @BeforeEach
    void setUp() {
        detector = new InputTypeDetector(new ObjectMapper());
    }

    @Test
    void test_detect_json() {
        String json = "{\"key\":\"value\"}";
        InputType result = detector.detect(json);
        assertEquals(InputType.JSON, result);
    }

    @Test
    void test_detect_xml_withDeclaration() {
        String xml = "<?xml version=\"1.0\"?><note><body>Hello</body></note>";
        InputType result = detector.detect(xml);
        assertEquals(InputType.XML, result);
    }

    @Test
    void test_detect_html_looksLikeXml() {
        String xmlLike = "<note><body>Hello</body></note>";
        InputType result = detector.detect(xmlLike);
        assertEquals(InputType.HTML, result); // porque no tiene <?xml
    }

    @Test
    void test_detect_html() {
        String html = "<b>Hello</b>";
        InputType result = detector.detect(html);
        assertEquals(InputType.HTML, result);
    }

    @Test
    void test_detect_text() {
        String text = "This is plain text with no tags.";
        InputType result = detector.detect(text);
        assertEquals(InputType.TEXT, result);
    }

    @Test
    void test_detect_invalidJson_treatedAsText() {
        String input = "{unclosed";
        InputType result = detector.detect(input);
        assertEquals(InputType.TEXT, result);
    }
}
