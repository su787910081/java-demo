package com.suyh.service.impl;

import com.suyh.service.HelloService;

/**
 * @author 苏雲弘
 * @since 2021-01-07
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String say() {
        return "hello";
    }
}
