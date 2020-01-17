package com.suyh.controller;

import com.suyh.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @RequestMapping("/user")
    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User(1, "zhangsan", 20));
        list.add(new User(2, "lisi", 30));

        return list;
    }
}
