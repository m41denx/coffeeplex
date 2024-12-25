package com.m41den.coffeeplex.repository.inmemory;

import com.m41den.coffeeplex.model.User;
import com.m41den.coffeeplex.repository.UserRepository;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository, Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, User> userStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger();

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userStore.values().stream()
                .filter(user -> Objects.equals(user.getUsername(), username))
                .findFirst();
    }

    @Override
    public void deleteById(Integer id) {
        userStore.remove(id);
    }

    @Override
    public void update(User user) {
        if (user.getId() != null && userStore.containsKey(user.getId())) {
            userStore.put(user.getId(), user);
        }
    }
}
