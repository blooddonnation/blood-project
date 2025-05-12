package com.example.auth_service.service;

import com.example.auth_service.events.UserEvent;
import com.example.auth_service.events.CenterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserEvent(UserEvent event) {
        event.setTimestamp(LocalDateTime.now());
        kafkaTemplate.send("user-events", event.getUserId().toString(), event);
    }

    public void publishCenterEvent(CenterEvent event) {
        event.setTimestamp(LocalDateTime.now());
        kafkaTemplate.send("center-events", event.getCenterId().toString(), event);
    }
} 