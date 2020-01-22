package com.suyh.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.suyh.domain.ProductOrder;
import com.suyh.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @RequestMapping("save")
    // 指定出现熔段时的处理方法(saveOrderFail)。框架会去调用这个方法并且传入相同的参数，并返回相同的返回值
    // 所以这个方法的签名除了方法名不同，其他的参数与返回值都要相同才可以。
    // 如果这个方法调用正常，没有出现抛出异常情况，那么正常返回此方法的返回值结果。
    // 否则返回 fallbackMethod 方法的结果
    // 所以这里处理的是这个方法异常了的处理。
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object save(@RequestParam("user_id") int userId, @RequestParam("product_id") int productId) {

        // 模拟方法出异常的情况。
        // String msg = null;
        // msg.length();

        ProductOrder order = productOrderService.save(userId, productId);
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("data", order);
        return data;
    }

    // 注意：方法签名一定要和api 方法一致
    // 这个方法是上面 save 接口的 fallbackMethod
    // 当save 接口调用异常时，此方法会被调用
    private Object saveOrderFail(int userId, int productId) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("code", -1);
        msg.put("msg", "抢购人数太多，您被挤出来了，稍等重试");
        return msg;
    }


}
