package com.suyh.controller.impl;

import com.suyh.entity.ResultModel;
import com.suyh.inner.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginControllerImpl implements LoginController {

    @Override
    public ResultModel<String> login(String username, String pwd) {
        System.out.println("login, username: " + username + ", pwd: " + pwd);

        return new ResultModel<>("OK");
    }
}
