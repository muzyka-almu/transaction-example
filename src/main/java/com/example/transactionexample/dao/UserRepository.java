package com.example.transactionexample.dao;

import com.example.transactionexample.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository  extends CrudRepository<User, Long> {
}
