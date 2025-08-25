package com.jojolapa.cafemanagement.restcontroller;

import com.jojolapa.cafemanagement.model.AuthRequest;
import com.jojolapa.cafemanagement.model.ErrorResponse;
import com.jojolapa.cafemanagement.model.LoginResponse;
import com.jojolapa.cafemanagement.model.User;
import com.jojolapa.cafemanagement.repository.UserRepository;
import com.jojolapa.cafemanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            User user = authService.authenticate(authRequest.getEmail(), authRequest.getPassword());

            LoginResponse response = new LoginResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid credentials"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = authService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/test-db")
    public ResponseEntity<String> testDatabase() {
        try {
            long count = userRepository.count();
            return ResponseEntity.ok("Database connected! Total users: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Database connection failed: " + e.getMessage());
        }
    }
}