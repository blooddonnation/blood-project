package com.example.demo.controller;

import com.example.demo.dto.BloodDonationRequestResponseDTO;
import com.example.demo.model.BloodDonationRequest;

import com.example.demo.service.BloodDonationRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class BloodDonationRequestController {

    @Autowired
    private BloodDonationRequestService requestService;

   


    // Create a new request
    @PostMapping
    public ResponseEntity<BloodDonationRequestResponseDTO> createRequest(@RequestBody BloodDonationRequest request) {
        BloodDonationRequest created = requestService.createRequest(request);

        BloodDonationRequestResponseDTO dto = new BloodDonationRequestResponseDTO(
            created.getId(),
            created.getBloodType(),
            created.getStatus(),
            created.getQuantity(),
            created.getRequestedBy().getId(),
            created.getBloodCenter().getId()
        );

        return ResponseEntity.ok(dto);
    }

    // Get all requests
    @GetMapping
    public List<BloodDonationRequestResponseDTO> getAllRequests() {
        return requestService.getAllRequests().stream().map(req -> new BloodDonationRequestResponseDTO(
            req.getId(),
            req.getBloodType(),
            req.getStatus(),
            req.getQuantity(),
            req.getRequestedBy().getId(),
            req.getBloodCenter().getId()
        )).collect(Collectors.toList());
    }

    // Get a single request by ID
    @GetMapping("/{id}")
    public ResponseEntity<BloodDonationRequestResponseDTO> getRequestById(@PathVariable Long id) {
        BloodDonationRequest req = requestService.getRequestById(id);
        if (req == null) {
            return ResponseEntity.notFound().build();
        }

        BloodDonationRequestResponseDTO dto = new BloodDonationRequestResponseDTO(
            req.getId(),
            req.getBloodType(),
            req.getStatus(),
            req.getQuantity(),
            req.getRequestedBy().getId(),
            req.getBloodCenter().getId()
        );

        return ResponseEntity.ok(dto);
    }

    // Update request
    @PutMapping("/{id}")
    public ResponseEntity<BloodDonationRequestResponseDTO> updateRequest(
            @PathVariable Long id,
            @RequestBody BloodDonationRequest updatedRequest,
            @RequestParam(required = false) Long bloodCenterId) {

        BloodDonationRequest updated = requestService.updateRequest(id, updatedRequest);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        BloodDonationRequestResponseDTO dto = new BloodDonationRequestResponseDTO(
            updated.getId(),
            updated.getBloodType(),
            updated.getStatus(),
            updated.getQuantity(),
            updated.getRequestedBy().getId(),
            updated.getBloodCenter().getId()
        );

        return ResponseEntity.ok(dto);
    }

    // Delete request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        boolean deleted = requestService.deleteRequest(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    
    

    
      
}
