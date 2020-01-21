package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class XdclassEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(XdclassEurekaServerApplication.class, args);
    }

}
