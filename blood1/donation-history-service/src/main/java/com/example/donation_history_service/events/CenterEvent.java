package com.example.donation_history_service.events;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CenterEvent {
    private String eventType; // CREATE, UPDATE, DELETE
    private Long centerId;
    private LocalDateTime timestamp;
} 