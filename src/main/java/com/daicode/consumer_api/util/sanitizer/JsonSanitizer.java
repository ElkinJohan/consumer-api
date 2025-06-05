package com.daicode.consumer_api.util.sanitizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JsonSanitizer {

    private final ObjectMapper mapper;
    private final HtmlSanitizer htmlSanitizer;
    private final TextSanitizer textSanitizer;
    private final XmlSanitizer xmlSanitizer;

    public String sanitize(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            sanitizeNode(root);
            return mapper.writeValueAsString(root);
        } catch (IOException e) {
            return textSanitizer.sanitize(json);
        }
    }

    private void sanitizeNode(JsonNode node) {
        if (node == null || node.isValueNode()) return;

        if (node.isObject()) {
            ((ObjectNode) node).fieldNames().forEachRemaining(field -> {
                JsonNode child = node.get(field);
                ((ObjectNode) node).set(field, sanitizeValue(child));
            });
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                arrayNode.set(i, sanitizeValue(arrayNode.get(i)));
            }
        }
    }

    private JsonNode sanitizeValue(JsonNode node) {
        if (node.isTextual()) {
            String value = node.asText();
            if (value.trim().startsWith("<?xml")) { // Detecta si el valor textual parece XML
                return new TextNode(xmlSanitizer.sanitize(value));
            } else if (value.matches("(?i).*<[^>]+>.*")) {
                return new TextNode(htmlSanitizer.sanitize(value));
            } else {
                return new TextNode(textSanitizer.sanitize(value));
            }
        }

        sanitizeNode(node); // Recursion para objetos/arrays
        return node;
    }
}
