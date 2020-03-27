package com.suyh.service;

import com.suyh.service.fallback.ProductClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务客户端
 *
 * 添加注解(同时指定要调用的微服务名称)：@FeignClient(name = "product-service")
 * 然后添加要调用的微服务的接口，这个接口签名必须与微服务的接口签名一致，且参数也要一致。
 * 添加了这个注解之后，可以不用实现它，框架里面自己会处理实现，
 * 我们在使用的时候，把它当成一个Bean 对象注入使用就可以了。
 *
 * 这里添加了 fallback = ProductClientFallback.class 指定微服务调用异常时的处理类
 * 当微服务的API 调用异常的时候，会到该类中去调用相同的API 以处理异常情况。
 */
@FeignClient(name = "product-service", fallback = ProductClientFallback.class, path = "")
public interface ProductClient {

    // 这里的API 地址，需要与"product-service"的地址一致
    @GetMapping("/api/v1/product/find")
    // @RequestMapping(value = "", method = ) 也可以这样添加注解
    String findById(@RequestParam(value = "id") int id);
}
