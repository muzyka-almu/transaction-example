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

    /*
    OUT:
        Users count before saving: 0
        Users count after saving: 2
        RuntimeException happened: java.lang.RuntimeException: Something happened
        Final count of users is 0
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public Boolean addUsers(@RequestBody List<User> users) {
        try {
            userService.saveWithException(users);
        } catch (RuntimeException e) {
            System.out.println("RuntimeException happened: " + e);
        }

        System.out.println("Final count of users is " + ((Collection<?>)userService.findAll()).size());

        return true;
    }

    // Isolation testing --------------------------------------------------------------------- start
    /*
    @Transactional(isolation = Isolation.READ_UNCOMMITTED) public void save1stExec()
    @Transactional(isolation = Isolation.READ_UNCOMMITTED) public void save2ndExec()
    OUT:
        http-nio-8080-exec-1>> users count before changes: 6
        http-nio-8080-exec-1>> saved, users count: 7
        http-nio-8080-exec-2>> users count before changes: 7
        http-nio-8080-exec-2>> saved, users count: 8
        http-nio-8080-exec-1>> user count after changes: 8
        http-nio-8080-exec-2>> user count after changes: 8

    @Transactional(isolation = Isolation.READ_COMMITTED) public void save1stExec()
    @Transactional(isolation = Isolation.READ_COMMITTED) public void save2ndExec()
    OUT:
        http-nio-8080-exec-1>> users count before changes: 8
        http-nio-8080-exec-1>> saved, users count: 9
        http-nio-8080-exec-2>> users count before changes: 8
        http-nio-8080-exec-2>> saved, users count: 9
        http-nio-8080-exec-1>> user count after changes: 9
        http-nio-8080-exec-2>> user count after changes: 10

    @Transactional(isolation = Isolation.REPEATABLE_READ) public void save1stExec()
    @Transactional(isolation = Isolation.REPEATABLE_READ) public void save2ndExec()
    OUT:
        http-nio-8080-exec-1>> users count before changes: 10
        http-nio-8080-exec-1>> saved, users count: 11
        http-nio-8080-exec-2>> users count before changes: 10
        http-nio-8080-exec-2>> saved, users count: 11
        http-nio-8080-exec-1>> user count after changes: 11
        http-nio-8080-exec-2>> user count after changes: 11
    INFO: real in db at the end we have 12 rows

    Isolation.DEFAULT == Isolation.REPEATABLE_READ

    @Transactional(isolation = Isolation.SERIALIZABLE) public void save1stExec()
    @Transactional(isolation = Isolation.SERIALIZABLE) public void save2ndExec()
    OUT:
        http-nio-8080-exec-1>> users count before changes: 12
        http-nio-8080-exec-1>> saved, users count: 13
        http-nio-8080-exec-1>> user count after changes: 13
        http-nio-8080-exec-2>> users count before changes: 13
        http-nio-8080-exec-2>> saved, users count: 14
        http-nio-8080-exec-2>> user count after changes: 14
    INFO: 2nd request will start executable when 1st will finished
     */
    @RequestMapping(value = "/users-1", method = RequestMethod.GET)
    public Boolean save1stExec() {
        userService.save1stExec();

        return true;
    }

    @RequestMapping(value = "/users-2", method = RequestMethod.GET)
    public Boolean save2ndExec() {
        userService.save2ndExec();

        return true;
    }
    // Isolation testing --------------------------------------------------------------------- end

    /*
    DESCRIPTION: run 2 methods(marked @Transactional(isolation = Isolation.SERIALIZABLE)) in different threads
    OUT:
        Thread-16>> users count before changes: 22
        Thread-16>> saved, users count: 23
        Thread-17>> users count before changes: 23
        Thread-17>> saved, users count: 24
        Thread-16>> user count after changes: 24
        Thread-17>> user count after changes: 24
    INFO: in threads @Transactional don't work
     */
    @RequestMapping(value = "/save1and2Exec", method = RequestMethod.POST)
    public Boolean save1and2Exec(@RequestBody List<User> users) throws InterruptedException {
        userService.save1and2Exec();

        return true;
    }


    // TODO set transaction in thread
}
