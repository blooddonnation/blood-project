package com.example.auth_service.events;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CenterEvent {
    private String eventType; // CREATE, UPDATE, DELETE
    private Long centerId;
    private String name;
    private String location;
    private Long managerId;
    private LocalDateTime timestamp;
} 