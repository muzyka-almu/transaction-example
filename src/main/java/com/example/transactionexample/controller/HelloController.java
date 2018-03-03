package com.example.transactionexample.controller;

import com.example.transactionexample.entity.User;
import com.example.transactionexample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class HelloController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello() {
        return "Hello world)!";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Iterable<User> users() {
        return userService.findAll();
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public Boolean addUsers(@RequestBody List<User> users) {
        try {
            userService.save(users);
        } catch (RuntimeException e) {
            System.out.println("RuntimeException happened: " + e);
        }

        System.out.println("Final count of users is " + ((Collection<?>)userService.findAll()).size());

        return true;
    }
}
