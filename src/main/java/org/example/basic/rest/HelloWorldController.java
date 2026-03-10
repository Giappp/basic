package org.example.basic.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class HelloWorldController {
    @GetMapping("/hello")
    public String helloMethod() {
        return "Hello World!";
    }
}