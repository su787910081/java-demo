package com.suyh.controller;

import com.suyh.domain.Product;
import com.suyh.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RefreshScope
public class ProductController {

    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @Autowired
    private ProductService productService;

    @RequestMapping("/list")
    public List<Product> list() {
        return productService.listProduct();
    }

    @RequestMapping("/find")
    public Product findById(@RequestParam("id") int id) {
        Product product = productService.findById(id);
        Product result = new Product();

        BeanUtils.copyProperties(product, result);
        result.setName(result.getName() + " data from port=" + port + ", env=" + env);
        return result;
    }


}
