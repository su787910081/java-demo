package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class XdclassGateway {
    public static void main(String[] args) {
        SpringApplication.run(XdclassGateway.class, args);
    }
}
