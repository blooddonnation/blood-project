package com.example.demo.service;

import com.example.demo.model.BloodCenter;
import com.example.demo.model.BloodDonationRequest;
import com.example.demo.model.User;
import com.example.demo.repository.BloodCenterRepository;
import com.example.demo.repository.BloodDonationRequestRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BloodDonationRequestService {

    @Autowired
    private BloodDonationRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodCenterRepository bloodCenterRepository;

    // Create new blood donation request
    public BloodDonationRequest createRequest(BloodDonationRequest request) {
        Long requesterId = request.getRequestedBy() != null ? request.getRequestedBy().getId() : null;
        Long centerId = request.getBloodCenter() != null ? request.getBloodCenter().getId() : null;

        if (requesterId == null || centerId == null) {
            throw new IllegalArgumentException("Requester ID and Blood Center ID must be provided.");
        }

        Optional<User> requesterOpt = userRepository.findById(requesterId);
        Optional<BloodCenter> centerOpt = bloodCenterRepository.findById(centerId);

        if (requesterOpt.isPresent() && centerOpt.isPresent()) {
            request.setRequestedBy(requesterOpt.get());
            request.setBloodCenter(centerOpt.get());
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            return requestRepository.save(request);
        }

        throw new IllegalArgumentException("Invalid requester or blood center ID.");
    }

    // Get all requests
    public List<BloodDonationRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    // Get request by ID
    public BloodDonationRequest getRequestById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }

    // Update a request
public BloodDonationRequest updateRequest(Long id, BloodDonationRequest updatedRequest) {
    Optional<BloodDonationRequest> existingOpt = requestRepository.findById(id);
    if (!existingOpt.isPresent()) {
        return null;
    }

    BloodDonationRequest existing = existingOpt.get();

    existing.setBloodType(updatedRequest.getBloodType());
    existing.setStatus(updatedRequest.getStatus());
    existing.setQuantity(updatedRequest.getQuantity());
    existing.setUpdatedAt(LocalDateTime.now());

    if (updatedRequest.getBloodCenter() != null && updatedRequest.getBloodCenter().getId() != null) {
        Optional<BloodCenter> newCenterOpt = bloodCenterRepository.findById(updatedRequest.getBloodCenter().getId());
        newCenterOpt.ifPresent(existing::setBloodCenter);
    }

    return requestRepository.save(existing);
}

    // Delete a request
    public boolean deleteRequest(Long id) {
        if (requestRepository.existsById(id)) {
            requestRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
