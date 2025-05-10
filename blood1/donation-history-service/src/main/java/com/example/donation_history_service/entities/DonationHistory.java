package com.example.donation_history_service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "donation_history")
public class DonationHistory {
    @Id
    private String donationId;
    private String userId; // The donor's userId (from JWT)
    private String recipientType; // "bloodcenter" or "user"
    private String recipientId; // ID of the blood center or recipient user
    private String recipientName; // Name of the blood center (if applicable)
    private int volume; // Volume in mL
    private String donationDate; // ISO format: 2025-04-25T18:50:00Z
    private String location; // Where the donation occurred
    private int pointsEarned;
}