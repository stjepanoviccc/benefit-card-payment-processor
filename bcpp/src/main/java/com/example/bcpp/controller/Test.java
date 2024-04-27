package com.example.bcpp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/asdasd/asdasdsad")
    public String helloWorld() {
        return "Hello World";
    }

}
