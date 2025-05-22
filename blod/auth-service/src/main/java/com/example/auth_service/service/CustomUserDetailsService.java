package com.example.auth_service.service;

import com.example.auth_service.entities.User;
import com.example.auth_service.events.UserEvent;
import com.example.auth_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List; // Added import
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventProducerService eventProducerService;

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
        User savedUser = userRepository.save(user);
        
        // Publish user creation event
        UserEvent event = new UserEvent();
        event.setEventType("CREATE");
        event.setUserId(savedUser.getId());
        event.setUsername(savedUser.getUsername());
        event.setEmail(savedUser.getEmail());
        event.setBloodType(savedUser.getBloodType());
        event.setRole(savedUser.getRole());
        event.setDateOfBirth(savedUser.getDateOfBirth().atStartOfDay());
        eventProducerService.publishUserEvent(event);
        
        return savedUser;
    }

    @Transactional
    public User updateUser(User user) {
        validateUser(user);
        User updatedUser = userRepository.save(user);
        
        // Publish user update event
        UserEvent event = new UserEvent();
        event.setEventType("UPDATE");
        event.setUserId(updatedUser.getId());
        event.setUsername(updatedUser.getUsername());
        event.setEmail(updatedUser.getEmail());
        event.setBloodType(updatedUser.getBloodType());
        event.setRole(updatedUser.getRole());
        event.setDateOfBirth(updatedUser.getDateOfBirth().atStartOfDay());
        eventProducerService.publishUserEvent(event);
        
        return updatedUser;
    }

    // New method to fetch all usernames
    public List<String> getAllUsernames() {
        return userRepository.findAllUsernames();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        String[] validRoles = {"ADMIN", "USER", "CENTERMANAGER"};
        if (!Arrays.asList(validRoles).contains(newRole.toUpperCase())) {
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }

        user.setRole(newRole.toUpperCase());
        User updatedUser = userRepository.save(user);
        
        // Publish user update event
        UserEvent event = new UserEvent();
        event.setEventType("UPDATE");
        event.setUserId(updatedUser.getId());
        event.setUsername(updatedUser.getUsername());
        event.setEmail(updatedUser.getEmail());
        event.setBloodType(updatedUser.getBloodType());
        event.setRole(updatedUser.getRole());
        event.setDateOfBirth(updatedUser.getDateOfBirth().atStartOfDay());
        eventProducerService.publishUserEvent(event);
        
        return updatedUser;
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        // Publish user deletion event
        UserEvent event = new UserEvent();
        event.setEventType("DELETE");
        event.setUserId(user.getId());
        event.setUsername(user.getUsername());
        event.setEmail(user.getEmail());
        event.setBloodType(user.getBloodType());
        event.setRole(user.getRole());
        event.setDateOfBirth(user.getDateOfBirth().atStartOfDay());
        eventProducerService.publishUserEvent(event);
        
        userRepository.delete(user);
    }
}