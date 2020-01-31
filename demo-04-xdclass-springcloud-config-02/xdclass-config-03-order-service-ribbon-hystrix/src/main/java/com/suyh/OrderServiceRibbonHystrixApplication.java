package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Feign 组件
@EnableCircuitBreaker   // Hystrix 组件
public class OrderServiceRibbonHystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceRibbonHystrixApplication.class, args);
    }
}
