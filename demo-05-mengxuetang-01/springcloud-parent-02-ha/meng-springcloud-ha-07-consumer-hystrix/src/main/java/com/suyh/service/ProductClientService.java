package com.suyh.service;

import com.suyh.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

// 指定调用微服务名称
@FeignClient(value = "microservice-product",
        // 所有方法级映射要使用的前缀。
        // 我的理解就是下面所有的方法的 @RequestMapping 都自动添加一个  "/product" 的访问前缀
        // 相当于在类上加 requestMapping
        path = "/product",
        fallback = ProductClientServiceFallback.class)
public interface ProductClientService {

    // 因为@FeignClient 注解中添加了 path="/product" 所以这里的访问路径是: /product/add
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    // @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    boolean add(@RequestBody Product product);

    // 因为@FeignClient 注解中添加了 path="/product" 所以这里的访问路径是: /product/get/{id}
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    // @RequestMapping(value = "/product/get/{id}", method = RequestMethod.GET)
    Product get(@PathVariable("id") Long id);

    // 因为@FeignClient 注解中添加了 path="/product" 所以这里的访问路径是: /product/list
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    // @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    List<Product> list();

}
