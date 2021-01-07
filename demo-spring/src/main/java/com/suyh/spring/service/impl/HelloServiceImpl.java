package com.suyh.spring.service.impl;


import com.suyh.spring.service.HelloService;

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
