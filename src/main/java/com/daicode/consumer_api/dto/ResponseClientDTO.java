package com.daicode.consumer_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseClientDTO {

    @JsonProperty("attribute01")
    private String attribute01;

    @JsonProperty("attribute02")
    private Attribute02 attribute02;

    @JsonProperty("attribute03")
    private String attribute03;

    @Data
    public static class Attribute02 {
        @JsonProperty("sub-attribut02")
        private String subAttribut02;
    }
}
