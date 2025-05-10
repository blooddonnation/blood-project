package com.example.auth_service.service;

import com.example.auth_service.entities.User;
import com.example.auth_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizedUsername = username.toLowerCase();
        Optional<User> user = userRepository.findByUsername(normalizedUsername);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return (UserDetails) user.get();
    }

    public void validateUser(User user) {
        String[] validBloodTypes = {
            "A_POSITIVE", "A_NEGATIVE",
            "B_POSITIVE", "B_NEGATIVE",
            "AB_POSITIVE", "AB_NEGATIVE",
            "O_POSITIVE", "O_NEGATIVE"
        };
        if (user.getBloodType() == null || !Arrays.asList(validBloodTypes).contains(user.getBloodType())) {
            throw new IllegalArgumentException("Invalid blood type: " + user.getBloodType());
        }

        String[] validRoles = {"ADMIN", "USER", "CENTERMANAGER"};
        if (user.getRole() == null || !Arrays.asList(validRoles).contains(user.getRole())) {
            throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }
    }

    @Transactional
    public User registerUser(User user) {
        validateUser(user);
        return userRepository.save(user);
    }
}