package com.example.demo.consumer;

import com.example.auth_service.events.UserEvent;
import com.example.demo.service.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);

    @Autowired
    private UserEventService userEventService;

    @KafkaListener(topics = "user-events", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserEvent(UserEvent event) {
        try {
            logger.info("Received user event: {} for user: {}", event.getEventType(), event.getUserId());
            userEventService.handleUserEvent(event);
        } catch (Exception e) {
            logger.error("Error processing user event: {}", e.getMessage(), e);
        }
    }
} 