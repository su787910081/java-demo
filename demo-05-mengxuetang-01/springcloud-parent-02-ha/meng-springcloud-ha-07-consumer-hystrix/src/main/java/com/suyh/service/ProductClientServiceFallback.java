package com.suyh.service;

import com.suyh.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientServiceFallback implements ProductClientService {
    @Override
    public boolean add(Product product) {
        return false;
    }

    @Override
    public Product get(Long id) {
        return new Product(id, "id=" + id + "无数据--feign&hystrix",
                "无有效数据库");
    }

    @Override
    public List<Product> list() {
        return null;
    }
}
