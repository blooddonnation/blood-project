package com.example.donation_history_service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_points")
public class UserPoints {
    @Id
    private String userId;
    private int totalPoints;
    private String lastUpdated;
}