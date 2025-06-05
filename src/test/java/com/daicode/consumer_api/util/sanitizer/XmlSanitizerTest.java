package com.daicode.consumer_api.util.sanitizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class XmlSanitizerTest {
    @InjectMocks
    private XmlSanitizer sanitizer;

    @Test
    void test_sanitize_removesScriptAndEvents() {
        String xml = "<root><script>alert('x')</script><tag onload=\"alert()\">ok</tag></root>";
        String result = sanitizer.sanitize(xml);
        assertFalse(result.contains("<script>"));
        assertFalse(result.contains("onload="));
    }

    @Test
    void test_sanitize_preservesXmlTags() {
        String xml = "<root><value>123</value></root>";
        assertEquals(xml, sanitizer.sanitize(xml));
    }
}
