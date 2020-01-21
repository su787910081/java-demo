package com.suyh.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务客户端
 */
@FeignClient(name = "product-service")
public interface ProductClient {

    // 这里的API 地址，需要与"product-service"的地址一致
    @GetMapping("/api/v1/product/find")
    // @RequestMapping(value = "", method = ) 也可以这样添加注解
    String findById(@RequestParam(value = "id") int id);
}
