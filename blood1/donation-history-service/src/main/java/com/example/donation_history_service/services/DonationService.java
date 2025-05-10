package com.example.donation_history_service.services;

import com.example.donation_history_service.entities.DonationHistory;
import com.example.donation_history_service.entities.UserPoints;
import com.example.donation_history_service.repositories.DonationHistoryRepository;
import com.example.donation_history_service.repositories.UserPointsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class DonationService {
    private final DonationHistoryRepository donationHistoryRepository;
    private final UserPointsRepository userPointsRepository;

    public DonationService(DonationHistoryRepository donationHistoryRepository, UserPointsRepository userPointsRepository) {
        this.donationHistoryRepository = donationHistoryRepository;
        this.userPointsRepository = userPointsRepository;
    }

    public DonationHistory addDonation(DonationHistory donation) {
        // Set donation ID and date
        donation.setDonationId(UUID.randomUUID().toString());
        if (donation.getDonationDate() == null) {
            donation.setDonationDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        }

        // Validate recipientType
        if (!"bloodcenter".equalsIgnoreCase(donation.getRecipientType()) && 
            !"user".equalsIgnoreCase(donation.getRecipientType())) {
            throw new IllegalArgumentException("recipientType must be 'bloodcenter' or 'user'");
        }

        // Calculate points
        int points = calculatePoints(donation);
        donation.setPointsEarned(points);

        // Save donation
        donationHistoryRepository.save(donation);

        // Update user points
        UserPoints userPoints = userPointsRepository.findById(donation.getUserId())
                .orElseGet(() -> {
                    UserPoints newUserPoints = new UserPoints();
                    newUserPoints.setUserId(donation.getUserId());
                    newUserPoints.setTotalPoints(0);
                    newUserPoints.setLastUpdated(donation.getDonationDate());
                    return newUserPoints;
                });
        userPoints.setTotalPoints(userPoints.getTotalPoints() + points + calculateBonuses(donation.getUserId()));
        userPoints.setLastUpdated(donation.getDonationDate());
        userPointsRepository.save(userPoints);

        return donation;
    }

    public List<DonationHistory> getDonationHistory(String userId) {
        return donationHistoryRepository.findByUserId(userId);
    }

    public UserPoints getUserPoints(String userId) {
        return userPointsRepository.findById(userId)
                .orElseGet(() -> {
                    UserPoints newUserPoints = new UserPoints();
                    newUserPoints.setUserId(userId);
                    newUserPoints.setTotalPoints(0);
                    newUserPoints.setLastUpdated(null);
                    return newUserPoints;
                });
    }

    private int calculatePoints(DonationHistory donation) {
        int points = 0;
        String recipientType = donation.getRecipientType().toLowerCase();
        if ("bloodcenter".equals(recipientType)) {
            points = (donation.getVolume() / 450) * 50;
        } else if ("user".equals(recipientType)) {
            points = (donation.getVolume() / 450) * 70;
        } else {
            points = 10;
        }
        return points;
    }

    private int calculateBonuses(String userId) {
        List<DonationHistory> history = getDonationHistory(userId);
        int totalDonations = history.size();
        int yearlyDonations = (int) history.stream()
                .filter(d -> LocalDateTime.parse(d.getDonationDate(), DateTimeFormatter.ISO_DATE_TIME)
                        .isAfter(LocalDateTime.now().minusYears(1)))
                .count();

        int bonus = 0;
        if (yearlyDonations >= 3) bonus += 20; // Frequent donor bonus
        if (totalDonations >= 10) bonus += 100; // Milestone bonus
        return bonus;
    }
}