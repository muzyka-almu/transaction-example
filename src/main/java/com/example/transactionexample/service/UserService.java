package com.example.transactionexample.service;

import com.example.transactionexample.dao.UserRepository;
import com.example.transactionexample.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> findAll() {
        return  userRepository.findAll();
    }


    @Transactional
    public Iterable<User> save(List<User> users) {
        System.out.println("Users count before saving: " + ((Collection<?>)findAll()).size());

        Iterable<User> newUsers = userRepository.saveAll(users);

        System.out.println("Users count after saving: " + ((Collection<?>)findAll()).size());

        if (((Collection<?>)findAll()).size() > 0) {
            throw new RuntimeException("Something happened");
        }

        return newUsers;
    }
}
