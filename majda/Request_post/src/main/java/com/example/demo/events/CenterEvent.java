package com.example.demo.events;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CenterEvent {
    private String eventType; // CREATE, UPDATE, DELETE
    private Long centerId;
    private String name;
    private String location;
    private LocalDateTime timestamp;
} 