package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// 解除自动加载数据库连接配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SsoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoServiceApplication.class, args);
    }
}
