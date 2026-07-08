package com.crm.service;

import com.crm.model.User;
import com.crm.repository.UserRepository;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void seedInitialUser() {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", "admin123", "ADMIN"));
            userRepository.save(new User("executive", "exec123", "EXECUTIVE"));
        }
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        
        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid password");
        }
        return user;
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
