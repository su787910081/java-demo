package com.suyh.controller.impl;

import com.suyh.entity.ResultModel;
import com.suyh.inner.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class LoginControllerImpl implements LoginController {

    @Override
    public ResultModel<String> login(String username, String pwd) {
        System.out.println("login, username: " + username + ", pwd: " + pwd);

        Random random = new Random();
        boolean isLogin = random.nextBoolean();
        System.out.println("is login: " + isLogin);

        ResultModel<String> res = null;
        if (isLogin) {
            res = new ResultModel<>("OK");
        } else {
            res = new ResultModel<>(-1, "login failed", "FAILED");
        }

        return res;
    }
}
