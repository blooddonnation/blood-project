package com.example.donation_history_service.controllers;

import com.example.donation_history_service.entities.DonationHistory;
import com.example.donation_history_service.entities.UserPoints;
import com.example.donation_history_service.services.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<DonationHistory> addDonation(@RequestBody DonationHistory donation) {
        String authenticatedUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        donation.setUserId(authenticatedUserId);

        // Add the donation using the service
        DonationHistory savedDonation = donationService.addDonation(donation);
        return ResponseEntity.status(201).body(savedDonation);
    }

    @GetMapping("/history")
    public ResponseEntity<List<DonationHistory>> getDonationHistory() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<DonationHistory> history = donationService.getDonationHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/points")
    public ResponseEntity<UserPoints> getUserPoints() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserPoints userPoints = donationService.getUserPoints(userId);
        return ResponseEntity.ok(userPoints);
    }
}