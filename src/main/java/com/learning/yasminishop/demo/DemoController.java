package com.learning.yasminishop.demo;


import com.learning.yasminishop.auth.dto.response.APIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/demo")
public class DemoController {

    @Value("${spring.profiles.active}")
    private String profile;

    @GetMapping("/hello")
    public APIResponse<String> hello() {
        return APIResponse.<String>builder()
                .result("Hello World!. Active profile: " + profile)
                .build();
    }
}
