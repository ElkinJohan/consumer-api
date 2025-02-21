package com.daicode.consumer_api.controller;

import com.daicode.consumer_api.service.IAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AppController {

    private final IAppService iAppService;
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{flag}")
    public String publishLogger(@PathVariable boolean flag) {
        return this.iAppService.useSomeOperation(flag);
    }
}
