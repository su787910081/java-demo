package com.suyh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrderServiceRibbonApplication {
    public static void main(String[] args) {
//        SpringApplication.run(OrderServiceApplication.class, args);
        SpringApplication.run(OrderServiceRibbonApplication.class, args);
    }

    // 添加了@LoadBalanced 注解之后，注入的RestTemplate 就拥有了(客户端)负载均衡的能力
    @Bean
    @LoadBalanced   // 这个注解只有添加在 RestTemplate 上才有用
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
