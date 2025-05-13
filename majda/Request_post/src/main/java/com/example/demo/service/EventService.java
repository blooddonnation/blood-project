package com.example.demo.service;

import com.example.auth_service.events.UserEvent;
import com.example.demo.events.CenterEvent;
import com.example.demo.model.BloodCenter;
import com.example.demo.repository.BloodCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private BloodCenterRepository bloodCenterRepository;

    // Publish user events
    public void publishUserEvent(UserEvent event) {
        event.setTimestamp(LocalDateTime.now());
        kafkaTemplate.send("user-events", event.getUserId().toString(), event);
        logger.info("Published user event: {} for user: {}", event.getEventType(), event.getUserId());
    }

    // Publish center events
    public void publishCenterEvent(CenterEvent event) {
        event.setTimestamp(LocalDateTime.now());
        kafkaTemplate.send("center-events", event.getCenterId().toString(), event);
        logger.info("Published center event: {} for center: {}", event.getEventType(), event.getCenterId());
    }

    // Listen for center events
    @KafkaListener(topics = "center-events", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handleCenterEvent(CenterEvent event) {
        try {
            logger.info("Received center event: {} for center: {}", event.getEventType(), event.getCenterId());
            
            switch (event.getEventType()) {
                case "CREATE":
                case "UPDATE":
                    BloodCenter center = bloodCenterRepository.findById(event.getCenterId())
                        .orElse(new BloodCenter());
                    
                    center.setId(event.getCenterId());
                    center.setName(event.getName());
                    center.setLocation(event.getLocation());
                    
                    bloodCenterRepository.save(center);
                    logger.info("Center synchronized successfully: {}", event.getCenterId());
                    break;
                    
                case "DELETE":
                    bloodCenterRepository.deleteById(event.getCenterId());
                    logger.info("Center deleted successfully: {}", event.getCenterId());
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing center event: {}", e.getMessage(), e);
        }
    }
} 