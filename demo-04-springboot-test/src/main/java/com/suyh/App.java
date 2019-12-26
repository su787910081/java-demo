package com.suyh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    static {
        System.setProperty("log4j2.dir", "demo-04-springboot-test");
    }

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class);
        logger.info("begin...");

        logger.debug("debug log.");
        logger.info("info log.");
        logger.warn("warn log.");
    }
}
