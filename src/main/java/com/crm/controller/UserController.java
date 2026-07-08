package com.crm.controller;

import com.crm.model.User;
import com.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        User user = userService.login(username, password);
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", "dummy-token-" + user.getRole().toLowerCase());
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (user.getRole() == null) {
            user.setRole("EXECUTIVE");
        }
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
