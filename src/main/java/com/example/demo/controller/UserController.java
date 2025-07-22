package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user){
        userService.register(user);
        return "Registration successful!";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session){
        boolean success = userService.login(request.getUsername(), request.getPassword());
        if (success) {
            session.setAttribute("username", request.getUsername());
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        }
    }

    @GetMapping("/profile")
    public User getProfile(HttpSession session){
        String username = (String) session.getAttribute("username");
        return userService.findByUsername(username);
    }


}
