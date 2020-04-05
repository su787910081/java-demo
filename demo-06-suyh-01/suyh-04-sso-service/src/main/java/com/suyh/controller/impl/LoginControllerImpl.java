package com.suyh.controller.impl;

import com.suyh.entity.ResultModel;
import com.suyh.inner.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginControllerImpl implements LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginControllerImpl.class);

    @Override
    public ResultModel<String> login(String username, String pwd) {
        logger.info("login, username: " + username + ", pwd: " + pwd);

        return new ResultModel<>("OK");
    }
}
