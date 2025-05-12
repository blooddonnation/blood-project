package com.example.donation_history_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;

@Service
public class EventConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserEvent(String message) {
        try {
            Map<String, Object> event = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            String eventType = (String) event.get("eventType");
            Long userId = Long.valueOf(event.get("userId").toString());
            
            // Handle different event types
            switch (eventType) {
                case "CREATE":
                case "UPDATE":
                    // Update or create user data in local storage
                    updateUserData(event);
                    break;
                case "DELETE":
                    // Handle user deletion
                    deleteUserData(userId);
                    break;
            }
        } catch (Exception e) {
            // Log error and handle appropriately
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "center-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCenterEvent(String message) {
        try {
            Map<String, Object> event = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            String eventType = (String) event.get("eventType");
            Long centerId = Long.valueOf(event.get("centerId").toString());
            
            // Handle different event types
            switch (eventType) {
                case "CREATE":
                case "UPDATE":
                    // Update or create center data in local storage
                    updateCenterData(event);
                    break;
                case "DELETE":
                    // Handle center deletion
                    deleteCenterData(centerId);
                    break;
            }
        } catch (Exception e) {
            // Log error and handle appropriately
            e.printStackTrace();
        }
    }

    private void updateUserData(Map<String, Object> userData) {
        // Implement user data synchronization logic
        // This could involve updating a local cache or database
    }

    private void deleteUserData(Long userId) {
        // Implement user deletion logic
        // This could involve removing user data from local storage
    }

    private void updateCenterData(Map<String, Object> centerData) {
        // Implement center data synchronization logic
        // This could involve updating a local cache or database
    }

    private void deleteCenterData(Long centerId) {
        // Implement center deletion logic
        // This could involve removing center data from local storage
    }
} 