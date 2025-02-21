package com.daicode.consumer_api.client;

import com.daicode.consumer_api.dto.RequestClientDTO;
import com.daicode.consumer_api.dto.ResponseClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mockClient", url = "http://localhost:3001")
public interface ISomeClient {
    @PostMapping(value = "/some-url/success", consumes = "application/json")
    ResponseClientDTO callMockSuccess(@RequestBody RequestClientDTO requestDto);

    @PostMapping(value = "/some-url/fail", consumes = "application/json")
    ResponseClientDTO callMockFail(@RequestBody RequestClientDTO requestDto);
}
