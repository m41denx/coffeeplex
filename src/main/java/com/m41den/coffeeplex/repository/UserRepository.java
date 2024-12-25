package com.m41den.coffeeplex.repository;

import com.m41den.coffeeplex.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Integer id);
    List<User> findAll();
    Optional<User> findByUsername(String username);
    void deleteById(Integer id);
    void update(User user);
}
