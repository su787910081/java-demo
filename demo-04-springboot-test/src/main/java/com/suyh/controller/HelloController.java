package com.suyh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    @RequestMapping("/hi")
    public String hi(String name) {
        return "hello " + name;
    }
}
