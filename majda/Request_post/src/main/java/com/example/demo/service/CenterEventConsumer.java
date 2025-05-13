package com.example.demo.service;

import com.example.demo.events.CenterEvent;
import com.example.demo.model.BloodCenter;
import com.example.demo.repository.BloodCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CenterEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CenterEventConsumer.class);

    @Autowired
    private BloodCenterRepository bloodCenterRepository;

    @KafkaListener(topics = "center-events", groupId = "request-post-group")
    public void consumeCenterEvent(CenterEvent event) {
        try {
            logger.info("Received center event: {} for center: {}", event.getEventType(), event.getCenterId());
            
            if (event.getEventType().equals("CREATE") || event.getEventType().equals("UPDATE")) {
                BloodCenter center = bloodCenterRepository.findById(event.getCenterId())
                    .orElse(new BloodCenter());
                
                // Always set the ID from the event
                center.setId(event.getCenterId());
                center.setName(event.getName());
                center.setLocation(event.getLocation());
                
                try {
                    bloodCenterRepository.save(center);
                    logger.info("Center synchronized successfully: {}", event.getCenterId());
                } catch (Exception e) {
                    logger.error("Error saving center: {}", e.getMessage(), e);
                    throw e;
                }
            } else if (event.getEventType().equals("DELETE")) {
                try {
                    bloodCenterRepository.deleteById(event.getCenterId());
                    logger.info("Center deleted successfully: {}", event.getCenterId());
                } catch (Exception e) {
                    logger.error("Error deleting center: {}", e.getMessage(), e);
                    throw e;
                }
            }
        } catch (Exception e) {
            logger.error("Error processing center event: {}", e.getMessage(), e);
            throw e; // Re-throw to trigger Kafka's retry mechanism
        }
    }
} 