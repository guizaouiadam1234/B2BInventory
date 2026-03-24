package com.guizaouiadam1234.auth_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import com.guizaouiadam1234.auth_api.model.User;
import com.guizaouiadam1234.auth_api.service.UserService;
import com.guizaouiadam1234.auth_api.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; 
    
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // 1. REGISTER - Expects JSON: {"username": "...", "password": "..."}
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // We pull the strings out of the User object sent in the JSON body
            User savedUser = userService.registerUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User loginRequest) {
    // 1. Check the database for the user
    User user = userService.findByUsername(loginRequest.getUsername());
    
    // 2. Use BCrypt to see if the typed password matches the hashed password in H2
    if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        
        // 3. Generate the token!
        String token = jwtUtil.generateToken(user.getUsername());
        
        // 4. Send the token back to the user
        return ResponseEntity.ok(token); 
    } else {
        return ResponseEntity.status(401).body("Invalid username or password");
    }
}

    // 3. EDIT - Expects JSON: {"id": 1, "username": "new", "password": "new"}
    // Note: You can also use @PathVariable for the ID if you prefer /edit/1
    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody User editRequest) {
        try {
            User user = userService.editUser(editRequest.getId(), editRequest.getUsername(), editRequest.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. DELETE - Using a PathVariable is more standard for Deletes
    // URL: DELETE http://localhost:8080/api/auth/delete/1
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. VALIDATE - Called by other microservices to verify a JWT token
    // Body: {"token": "<jwt>"}
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
        try {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                return ResponseEntity.ok(Map.of("valid", true, "username", username));
            }
        } catch (Exception ignored) {}
        return ResponseEntity.ok(Map.of("valid", false));
    }
}