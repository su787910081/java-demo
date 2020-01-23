package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MengEurekaServerHaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MengEurekaServerHaApplication.class, args);
    }
}
