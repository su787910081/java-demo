package com.suyh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.suyh.mapper")
@EnableHystrix
public class MengHystrixProviderHaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MengHystrixProviderHaApplication.class, args);
    }
}
