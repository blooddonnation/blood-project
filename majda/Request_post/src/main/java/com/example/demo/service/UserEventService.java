package com.example.demo.service;

import com.example.auth_service.events.UserEvent;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UserEventService {

    private static final Logger logger = LoggerFactory.getLogger(UserEventService.class);

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void handleUserEvent(UserEvent event) {
        try {
            switch (event.getEventType()) {
                case "CREATE":
                case "UPDATE":
                    User existingUser = userRepository.findById(event.getUserId()).orElse(null);
                    User user;
                    
                    if (existingUser != null) {
                        // Update existing user
                        existingUser.setUsername(event.getUsername());
                        existingUser.setEmail(event.getEmail());
                        existingUser.setBloodType(event.getBloodType());
                        existingUser.setRole(event.getRole());
                        existingUser.setDateOfBirth(event.getDateOfBirth().toLocalDate());
                        user = existingUser;
                    } else {
                        // Create new user
                        user = new User();
                        user.setId(event.getUserId());
                        user.setUsername(event.getUsername());
                        user.setEmail(event.getEmail());
                        user.setBloodType(event.getBloodType());
                        user.setRole(event.getRole());
                        user.setDateOfBirth(event.getDateOfBirth().toLocalDate());
                        user.setPassword("SYNC_DEFAULT_PASSWORD");
                    }
                    
                    // Use merge instead of save to handle concurrent updates
                    entityManager.merge(user);
                    logger.info("User synchronized successfully: {}", event.getUserId());
                    break;
                    
                case "DELETE":
                    userRepository.deleteById(event.getUserId());
                    logger.info("User deleted successfully: {}", event.getUserId());
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing user event: {}", e.getMessage(), e);
            throw e; // Re-throw to trigger transaction rollback
        }
    }
} 