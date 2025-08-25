package com.jojolapa.cafemanagement.service;

import com.jojolapa.cafemanagement.model.User;
import com.jojolapa.cafemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set admin role for specific email
        if ("admin@gmail.com".equals(user.getEmail())) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }

        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent() &&
                passwordEncoder.matches(password, userOptional.get().getPassword())) {
            return userOptional.get();
        }

        throw new RuntimeException("Invalid credentials");
    }
}