package com.example.auth_service.controllers;

import com.example.auth_service.entities.User;
import com.example.auth_service.security.JwtUtils;
import com.example.auth_service.security.RedisTokenService;
import com.example.auth_service.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List; // Added import

@RestController
@RequestMapping({"/auth", "/api/auth"}) // Support both paths
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        String normalizedUsername = registerRequest.username().toLowerCase();
        User existingUser;
        try {
            existingUser = (User) userService.loadUserByUsername(normalizedUsername);
        } catch (UsernameNotFoundException e) {
            existingUser = null;
        }

        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEmail(registerRequest.email());
        user.setBloodType(registerRequest.bloodType());
        user.setDateOfBirth(registerRequest.dateOfBirth());
        user.setRole(registerRequest.role().toUpperCase());

        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String normalizedUsername = loginRequest.username().toLowerCase();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedUsername,
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestParam String username) {
        String normalizedUsername = username.toLowerCase();
        redisTokenService.deleteToken(normalizedUsername);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/test")
    public ResponseEntity<?> testProtectedEndpoint() {
        return ResponseEntity.ok("This is a protected endpoint");
    }

    @PostMapping("/debug-password")
    public ResponseEntity<?> debugPassword(@RequestBody LoginRequest loginRequest) {
        String normalizedUsername = loginRequest.username().toLowerCase();
        User user = (User) userService.loadUserByUsername(normalizedUsername);
        boolean matches = passwordEncoder.matches(loginRequest.password(), user.getPassword());
        return ResponseEntity.ok("Password matches: " + matches);
    }

    @GetMapping("/test-cors")
    public ResponseEntity<String> testCors() {
        return ResponseEntity.ok("CORS is working!");
    }

    // hdchi new 3la kbk user management
    @GetMapping("/users/usernames")
    public ResponseEntity<List<String>> getAllUsernames() {
        List<String> usernames = userService.getAllUsernames();
        return ResponseEntity.ok(usernames);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody UpdateRoleRequest request) {
        try {
            User updatedUser = userService.updateUserRole(id, request.role());
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

record RegisterRequest(String username, String password, String email, String bloodType, LocalDate dateOfBirth, String role) {}
record LoginRequest(String username, String password) {}
record JwtResponse(String token) {}
record UpdateRoleRequest(String role) {}