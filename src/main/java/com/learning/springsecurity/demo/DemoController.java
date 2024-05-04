package com.learning.springsecurity.demo;


import com.learning.springsecurity.auth.dto.response.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/demo")
public class DemoController {

    @GetMapping("/hello")
    public APIResponse<String> hello() {
        return APIResponse.<String>builder()
                .result("Hello World")
                .build();
    }
}
