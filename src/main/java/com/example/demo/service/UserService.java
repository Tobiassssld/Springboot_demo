package com.example.demo.service;

import jakarta.servlet.http.HttpSession;

public interface UserService {
    void register(com.example.demo.entity.User user);
    boolean login(String username, String password);
    com.example.demo.entity.User findByUsername(String username);
    void logout(HttpSession session);
    boolean changePassword(String username, String oldPassword, String newPassword);
}
