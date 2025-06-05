package com.daicode.consumer_api.util.sanitizer;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class XmlSanitizer {

    public String sanitize(String xml) {
        if (xml == null) return "";

        // No eliminamos etiquetas válidas XML, solo eventos/scripts peligrosos
        xml = xml.replaceAll("<!\\[CDATA\\[.*?\\]\\]>", "");
        xml = xml.replaceAll("(?i)<script[^>]*>.*?</script>", "");
        xml = xml.replaceAll("(?i)on\\w+\\s*=\\s*\"[^\"]*\"", "");

        // Escapar HTML dentro del XML
        Document doc = Jsoup.parse(xml, "", org.jsoup.parser.Parser.xmlParser());
        return doc.toString();
    }
}

