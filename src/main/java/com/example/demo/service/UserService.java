package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
    void register(User user);
    boolean login(String username, String password);
}
