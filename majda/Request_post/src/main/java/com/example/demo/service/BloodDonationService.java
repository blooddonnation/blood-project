package com.example.demo.service;


import com.example.demo.dto.DonationResponseDTO;
import com.example.demo.model.BloodDonationRequest;
import com.example.demo.model.Donation;
import com.example.demo.model.User;
import com.example.demo.repository.BloodDonationRequestRepository;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BloodDonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodDonationRequestRepository requestRepository;

    // Create a donation
    public DonationResponseDTO createDonation(Long donorId, Long donationRequest) {
        User donor = userRepository.findById(donorId).orElse(null);
        BloodDonationRequest request = requestRepository.findById(donationRequest).orElse(null);

        if (donor == null || request == null) return null;

        // Check blood type compatibility
        if (!isCompatible(donor.getBloodType(), request.getBloodType())) {
            throw new IllegalArgumentException("Incompatible blood types: donor " + donor.getBloodType() + " → recipient " + request.getBloodType());
        }

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setDonationRequest(request);

        Donation saved = donationRepository.save(donation);
        return new DonationResponseDTO(saved.getId(), donor.getId(), request.getId(), saved.getCreatedAt());
    }

    // Get all donations
    public List<DonationResponseDTO> getAllDonations() {
        List<Donation> donations = donationRepository.findAll();
        return donations.stream()
                .map(donation -> new DonationResponseDTO(
                        donation.getId(),
                        donation.getDonor().getId(),
                        donation.getDonationRequest().getId(),
                        donation.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
    // Get donation by ID
    public DonationResponseDTO getDonationById(Long id) {
        Donation donation = donationRepository.findById(id).orElse(null);
        if (donation == null) {
            return null;
        }
        return new DonationResponseDTO(
                donation.getId(),
                donation.getDonor().getId(),
                donation.getDonationRequest().getId(),
                donation.getCreatedAt()
        );
    }

    // Update donation
    public DonationResponseDTO updateDonation(Long id, Long donorId, Long donationRequest) {
        return donationRepository.findById(id).map(existing -> {
            User donor = userRepository.findById(donorId).orElse(null);
            BloodDonationRequest request = requestRepository.findById(donationRequest).orElse(null);

            if (donor == null || request == null) return null;
            if (!isCompatible(donor.getBloodType(), request.getBloodType())) {
                throw new IllegalArgumentException("Incompatible blood types.");
            }

            existing.setDonor(donor);
            existing.setDonationRequest(request);
            existing.setUpdatedAt(LocalDateTime.now());

            Donation updated = donationRepository.save(existing);
            return new DonationResponseDTO(updated.getId(), donor.getId(), request.getId(), updated.getCreatedAt());
        }).orElse(null);
    }
    // Delete donation
    public boolean deleteDonation(Long id) {
        return donationRepository.findById(id).map(d -> {
            donationRepository.delete(d);
            return true;
        }).orElse(false);
    }

    // Get donation queue by request ID
    public List<DonationResponseDTO> getDonationsQueueByRequestId(Long donationRequestId) {
        List<Donation> donations = donationRepository.findByDonationRequestIdOrderByCreatedAtAsc(donationRequestId);
        return donations.stream()
                .map(donation -> new DonationResponseDTO(
                        donation.getId(),
                        donation.getDonor().getId(),
                        donation.getDonationRequest().getId(),
                        donation.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // Compatibility map
    private static final Map<String, List<String>> COMPATIBILITY_MAP = Map.ofEntries(
            Map.entry("O-", List.of("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+")),
            Map.entry("O+", List.of("O+", "A+", "B+", "AB+")),
            Map.entry("A-", List.of("A-", "A+", "AB-", "AB+")),
            Map.entry("A+", List.of("A+", "AB+")),
            Map.entry("B-", List.of("B-", "B+", "AB-", "AB+")),
            Map.entry("B+", List.of("B+", "AB+")),
            Map.entry("AB-", List.of("AB-", "AB+")),
            Map.entry("AB+", List.of("AB+"))
    );

    // Check blood type compatibility
    public boolean isCompatible(String donorType, String recipientType) {
        return COMPATIBILITY_MAP.getOrDefault(donorType, Collections.emptyList()).contains(recipientType);
    }
}
