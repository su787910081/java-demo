package com.suyh.demo0702.server;

import com.suyh.demo0701.service.SomeService;

public class SomeServiceImpl implements SomeService {
    @Override
    public String hello(String name) {
        return name + "欢迎你";
    }
}
