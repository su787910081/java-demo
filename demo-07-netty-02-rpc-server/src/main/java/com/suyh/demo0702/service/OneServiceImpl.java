package com.suyh.demo0702.service;

import com.suyh.demo0701.service.SomeService;

public class OneServiceImpl implements SomeService {
    @Override
    public String hello(String name) {
        return "欢迎你" + name;
    }
}
