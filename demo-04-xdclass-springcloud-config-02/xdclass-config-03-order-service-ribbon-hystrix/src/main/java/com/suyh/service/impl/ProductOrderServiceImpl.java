package com.suyh.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.suyh.domain.ProductOrder;
import com.suyh.service.ProductClient;
import com.suyh.service.ProductOrderService;
import com.suyh.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    // 注入feign 客户端
    // 然后就可以通过它去调用远程微服务的API 接口了。
    @Autowired
    private ProductClient productClient;

    @Override
    public ProductOrder save(int userId, int productId) {
        String response = productClient.findById(productId);
        System.out.println("ProductOrderServiceImpl::save, response: " + response);
        JsonNode jsonNode = JsonUtils.str2JsonNode(response);

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());
        productOrder.setProductName(jsonNode.get("name").toString());
        productOrder.setPrice(Integer.parseInt(jsonNode.get("price").toString()));

        return productOrder;
    }
}
