package com.suyh.controller;

import com.suyh.entities.Product;
import com.suyh.service.ProductClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: 梦学谷
 */
@RestController
public class ProductControllerConsumer {

    // private static final String REST_URL_PREFIX = "http://localhost:8001";
    // private static final String REST_URL_PREFIX = "http://microservice-product";

    ProductClientService service;

    // 构造注入
    @Autowired
    ProductControllerConsumer(ProductClientService service) {
        this.service = service;
    }

    @RequestMapping(value = "/consumer/product/add")
    public boolean add(Product product) {
        return service.add(product);
    }

    @RequestMapping(value = "/consumer/product/get/{id}")
    public Product get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @RequestMapping(value = "/consumer/product/list")
    public List<Product> list() {
        return service.list();
    }



}
