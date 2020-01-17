package com.suyh.controller;

import com.suyh.pojo.User;
import com.suyh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/consumer")
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
