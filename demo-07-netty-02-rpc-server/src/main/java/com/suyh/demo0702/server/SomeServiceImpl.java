package com.suyh.demo0702.server;

import com.suyh.demo0701.SomeService;

public class SomeServiceImpl implements SomeService {
    @Override
    public String doSome(String depart) {
        return depart + "欢迎你";
    }
}
