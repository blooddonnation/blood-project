package com.example.demo.controller;

import com.example.demo.dto.DonationResponseDTO;
import com.example.demo.service.BloodDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    @Autowired
    private BloodDonationService donationService;

    // Create donation
    @PostMapping
    public ResponseEntity<DonationResponseDTO> createDonation(@RequestBody Map<String, Long> body) {
        try {
            Long donorId = body.get("donorId");
            Long requestId = body.get("donationRequest");
            DonationResponseDTO response = donationService.createDonation(donorId, requestId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Get all donations
    @GetMapping
    public ResponseEntity<List<DonationResponseDTO>> getAllDonations() {
        return ResponseEntity.ok(donationService.getAllDonations());
    }

    // Get donation by ID
    @GetMapping("/{id}")
    public ResponseEntity<DonationResponseDTO> getDonationById(@PathVariable Long id) {
        DonationResponseDTO dto = donationService.getDonationById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // Update donation (with compatibility check)
    @PutMapping("/{id}")
    public ResponseEntity<DonationResponseDTO> updateDonation(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        try {
            Long donorId = body.get("donorId");
            Long requestId = body.get("donationRequest");
            DonationResponseDTO response = donationService.updateDonation(id, donorId, requestId);
            return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Delete donation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        return donationService.deleteDonation(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Get donation queue by request ID
    @GetMapping("/queue/{requestId}")
    public ResponseEntity<List<DonationResponseDTO>> getQueue(@PathVariable("requestId") Long donationRequest) {
        return ResponseEntity.ok(donationService.getDonationsQueueByRequestId(donationRequest));
    }
}
