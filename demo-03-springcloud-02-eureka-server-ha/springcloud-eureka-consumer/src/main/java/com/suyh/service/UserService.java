package com.suyh.service;

import com.suyh.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    // spring cloud 中 ribbon 提供的一个负载均衡器
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 到eureka-provider 微服务去做数据请求
     * @return
     */
    public List<User> getUsers() {
        // 到注册中心去找到指定服务实例
        ServiceInstance si = loadBalancerClient.choose("eureka-provider");
        // 拼接访问服务的URL
        StringBuffer sb = new StringBuffer();
        sb.append("http://").append(si.getHost()).append(":").append(si.getPort()).append("/user");
        String url = sb.toString();

        // SpringMVC RestTemplate
        RestTemplate rt = new RestTemplate();

        // 抽象类对象，这里没有重写新方法。
        ParameterizedTypeReference<List<User>> type =
                new ParameterizedTypeReference<List<User>>() {
                };

        // ResponseEntity 封装了返回值信息
        ResponseEntity<List<User>> response = rt.exchange(url, HttpMethod.GET, null/*请求参数*/, type/*返回值*/);
        List<User> list = response.getBody();
        return list;
    }
}
