package com.suyh.controller;

import com.suyh.App;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(value = SpringRunner.class)
@SpringBootTest(
        // 这里是启动类吧
        classes = App.class,
        // 使用默认端口
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HelloControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    // get 请求
    @Test
    public void getHi() {
        String hi = restTemplate.getForObject("/hello/hi?name=suyh01", String.class);
        Assert.assertEquals("hello suyh01", hi);
    }

    // post 请求
    @Test
    public void postHi() {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("name", "suyh02");
        String hi = restTemplate.postForObject("/hello/hi", multiValueMap, String.class);
        Assert.assertEquals("hello suyh02", hi);
    }
}
