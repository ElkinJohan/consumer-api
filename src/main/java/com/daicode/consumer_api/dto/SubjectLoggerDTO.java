package com.daicode.consumer_api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectLoggerDTO {

    private UUID transactionId;
    private String originService;
    @JsonSerialize
    private Map<String, Object> request;
    @JsonSerialize
    private Map<String, Object> response;
    private double duration;
    private String subscriberNumber;
    private String originUrl;
    private Boolean status;
    private OffsetDateTime date;
    private String hash;

    public String generateHash() throws NoSuchAlgorithmException, IOException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(transactionId)
                .append(originService)
                .append(convertMapToString(request))
                .append(subscriberNumber)
                .append(originUrl)
                .append(status);

        byte[] hashBytes = digest.digest(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();

    }

    private String convertMapToString(Map<String, Object> map) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }
}
