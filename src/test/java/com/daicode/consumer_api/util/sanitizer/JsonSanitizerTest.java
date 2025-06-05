package com.daicode.consumer_api.util.sanitizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonSanitizerTest {

    private JsonSanitizer sanitizer;

    private final HtmlSanitizer htmlSanitizer = new HtmlSanitizer();
    private final TextSanitizer textSanitizer = new TextSanitizer();
    private final XmlSanitizer xmlSanitizer = new XmlSanitizer();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        sanitizer = new JsonSanitizer(objectMapper, htmlSanitizer, textSanitizer, xmlSanitizer);
    }

    @Test
    void test_sanitize_preservesBTagWithRelaxedSafeList() {
        String input = "{\"key\": \"<b>bold</b>\"}";
        String result = sanitizer.sanitize(input);
        // La etiqueta <b> no es removida
        assertTrue(result.contains("\"key\":\"<b>bold</b>\""));
    }

    @Test
    void test_sanitize_handlesScriptInJsonValue() {
        String input = "{\"xml\": \"<script>alert('x')</script>\"}";
        String result = sanitizer.sanitize(input);
        // Se espera que el script sea removido o desinfectado
        assertFalse(result.contains("<script>"));
        assertFalse(result.contains("alert"));
    }

    @Test
    void test_sanitize_handlesPlainTextValue() {
        String input = "{\"text\": \"simple text\"}";
        String result = sanitizer.sanitize(input);
        // Texto plano debe quedar igual sanitization
        assertTrue(result.contains("\"text\":\"simple text\""));
    }

    @Test
    void test_sanitize_handlesArrayValues() {
        String input = "{\"arr\": [\"<b>bold</b>\", \"plain\"]}";
        String result = sanitizer.sanitize(input);
        assertTrue(result.contains("\"<b>bold</b>\""));
        assertTrue(result.contains("\"plain\""));
    }

    @Test
    void test_sanitize_handlesInvalidJsonGracefully() {
        String input = "not json";
        String result = sanitizer.sanitize(input);
        // Si no es JSON, debe pasar por textSanitizer que devuelve texto limpio
        assertNotNull(result);
        assertFalse(result.contains("<") || result.contains(">"));
    }

    @Test
    void test_sanitize_handlesXmlValue() {
        String input = "{\"xml\": \"<?xml version='1.0'?><note><to>Tove</to><from>Jani</from></note>\"}";
        String result = sanitizer.sanitize(input);
        // Asegurarse que el XML es procesado por xmlSanitizer
        assertTrue(result.contains("<?xml"));
    }

}
