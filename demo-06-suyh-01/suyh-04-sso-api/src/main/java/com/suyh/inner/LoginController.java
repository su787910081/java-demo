package com.suyh.inner;


import com.suyh.entity.ResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 如果不想实现这个接口，但是又要通过spring bean 使用它的话，那么需要注解 @EnableFeignClients 的支持
@FeignClient(name = "sso-service", path = "/")
public interface LoginController {

    @RequestMapping("/login")
    ResultModel<String> login(@RequestParam("username") String username,
                              @RequestParam("pwd") String pwd);
}
