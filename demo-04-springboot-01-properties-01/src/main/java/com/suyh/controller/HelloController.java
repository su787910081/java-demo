package com.suyh.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    // 读配置文件中的 msg 的配置属性值，注入到msg 属性对象中
    @Value("${msg}")
    private String msg;

    // 这里本来我是想用 Integer 类型来读取配置文件中的随机数字的，但是启动的时候报错了。
    @Value("${randomNum}")
    private String randomNumValue;

    @RequestMapping("/helloMsg")
    public String helloMsg() {
        return msg;
    }

    @RequestMapping("/randomNum")
    public Integer randomNum() {
        return Integer.parseInt(randomNumValue);
    }
}
