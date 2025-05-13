package com.example.donation_history_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.donation_history_service.events.CenterEvent;

@Service
public class EventConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EventConsumerService.class);

    public EventConsumerService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @KafkaListener(topics = "user-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserEvent(String message) {
        try {
            Map<String, Object> event = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
            String eventType = (String) event.get("eventType");
            Long userId = Long.valueOf(event.get("userId").toString());
            
            logger.info("Received user event: {} for user: {}", eventType, userId);
            
            switch (eventType) {
                case "CREATE":
                case "UPDATE":
                    updateUserData(event);
                    break;
                case "DELETE":
                    deleteUserData(userId);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing user event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "center-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCenterEvent(CenterEvent event) {
        try {
            logger.info("Received center event: {} for center: {}", event.getEventType(), event.getCenterId());
            
            switch (event.getEventType()) {
                case "CREATE":
                case "UPDATE":
                    updateCenterData(event);
                    break;
                case "DELETE":
                    deleteCenterData(event.getCenterId());
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing center event: {}", e.getMessage(), e);
        }
    }

    private void updateUserData(Map<String, Object> userData) {
        try {
            Query query = new Query(Criteria.where("userId").is(userData.get("userId").toString()));
            Update update = new Update()
                .set("username", userData.get("username"))
                .set("email", userData.get("email"))
                .set("bloodType", userData.get("bloodType"))
                .set("role", userData.get("role"))
                .set("lastUpdated", userData.get("timestamp"));

            mongoTemplate.upsert(query, update, "users");
            logger.info("User data synchronized successfully for user: {}", userData.get("userId"));
        } catch (Exception e) {
            logger.error("Error updating user data: {}", e.getMessage(), e);
        }
    }

    private void deleteUserData(Long userId) {
        try {
            Query query = new Query(Criteria.where("userId").is(userId.toString()));
            mongoTemplate.remove(query, "users");
            logger.info("User data deleted successfully for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error deleting user data: {}", e.getMessage(), e);
        }
    }

    private void updateCenterData(CenterEvent event) {
        try {
            Query query = new Query(Criteria.where("id").is(event.getCenterId()));
            Update update = new Update()
                .set("id", event.getCenterId())
                .set("lastUpdated", event.getTimestamp());

            mongoTemplate.upsert(query, update, "centers");
            logger.info("Center data synchronized successfully for center: {}", event.getCenterId());
        } catch (Exception e) {
            logger.error("Error updating center data: {}", e.getMessage(), e);
        }
    }

    private void deleteCenterData(Long centerId) {
        try {
            Query query = new Query(Criteria.where("id").is(centerId));
            mongoTemplate.remove(query, "centers");
            logger.info("Center data deleted successfully for center: {}", centerId);
        } catch (Exception e) {
            logger.error("Error deleting center data: {}", e.getMessage(), e);
        }
    }
} 