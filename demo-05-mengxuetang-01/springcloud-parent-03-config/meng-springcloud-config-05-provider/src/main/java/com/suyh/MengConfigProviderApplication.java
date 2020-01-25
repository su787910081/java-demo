package com.suyh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.suyh.mapper")
public class MengConfigProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MengConfigProviderApplication.class, args);
    }
}
