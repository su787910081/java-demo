package com.suyh.service.controller;


import com.suyh.ResourceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = ResourceApplication.class)
public class ExampleDemoResourceControllerTest {
    private static Logger logger = LoggerFactory.getLogger(ExampleDemoResourceControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getNameAutowired() {
        String autowiredName = restTemplate.getForObject(
                "/autowired/name", String.class);

        logger.info("test, getNameAutowired result: " + autowiredName);

    }

    @Test
    public void getNameResource01() {

        String resourceName = restTemplate.getForObject(
                "/resource/01/name", String.class);

        logger.info("test, getNameResource01 result: " + resourceName);
    }

    @Test
    public void getNameAutowired01() {
        String autowiredName = restTemplate.getForObject(
                "/autowired/01/name", String.class);

        logger.info("test, getNameAutowired01 result: " + autowiredName);

    }
}
