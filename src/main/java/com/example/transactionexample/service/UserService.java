package com.example.transactionexample.service;

import com.example.transactionexample.dao.UserRepository;
import com.example.transactionexample.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> findAll() {
        return  userRepository.findAll();
    }

    public Iterable<User> save(Iterable<User> users) {
        return userRepository.saveAll(users);
    }

    @Transactional
    public boolean saveWithException(List<User> users) {
        System.out.println("Users count before saving: " + getUserCount());

        save(users);

        System.out.println("Users count after saving: " + getUserCount());

        if (getUserCount() > 0) {
            throw new RuntimeException("Something happened");
        }

        return true;
    }

    public void save1and2Exec() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                save1stExec();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                save2ndExec();
            }
        });

        t1.start();

        Thread.sleep(1000);

        t2.start();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save1stExec() {
        try {
            execLogic();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save2ndExec() {
        try {
            execLogic();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Integer getUserCount() {
        return ((Collection<?>)findAll()).size();
    }

    private void execLogic() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ">> users count before changes: " + getUserCount());

        save(getFakeUsers());
        System.out.println(Thread.currentThread().getName() + ">> saved, users count: " + getUserCount());

        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName() + ">> user count after changes: " + getUserCount());
    }

    private List<User> getFakeUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("Petia", "Petrov"));

        return users;
    }
}
