package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.suyh.service")    // 扫描指定包下面使用@FeignClient 标识 的接口
public class MengConsumerFeignHaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MengConsumerFeignHaApplication.class, args);
    }
}
