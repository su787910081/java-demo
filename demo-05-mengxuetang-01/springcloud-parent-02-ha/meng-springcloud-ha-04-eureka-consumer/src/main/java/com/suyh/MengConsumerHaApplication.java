package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MengConsumerHaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MengConsumerHaApplication.class, args);
    }
}
