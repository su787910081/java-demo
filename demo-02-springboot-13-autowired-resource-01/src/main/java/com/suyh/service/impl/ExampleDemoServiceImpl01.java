package com.suyh.service.impl;

import com.suyh.service.ExampleDemoService;
import org.springframework.stereotype.Service;

@Service
//@Service("exampleDemoServiceImpl01")
public class ExampleDemoServiceImpl01 implements ExampleDemoService {
    @Override
    public String getClassName() {
        return "Example01";
    }
}
