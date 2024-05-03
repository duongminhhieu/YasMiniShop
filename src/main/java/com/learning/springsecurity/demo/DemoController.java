package com.learning.springsecurity.demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/demo")
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
