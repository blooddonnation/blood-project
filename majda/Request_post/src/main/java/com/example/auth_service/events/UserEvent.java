package com.example.auth_service.events;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserEvent {
    private String eventType; // CREATE, UPDATE, DELETE
    private Long userId;
    private String username;
    private String email;
    private String bloodType;
    private String role;
    private LocalDateTime dateOfBirth;
    private LocalDateTime timestamp;
} 