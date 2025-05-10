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
    public BloodDonationRequest createRequest(BloodDonationRequest request, Long bloodCenterId) {
        // Hardcoded example of assigning requester. You can replace with authentication context
        Optional<User> requesterOpt = userRepository.findById(request.getRequestedBy().getId());
        Optional<BloodCenter> centerOpt = bloodCenterRepository.findById(bloodCenterId);

        if (requesterOpt.isPresent() && centerOpt.isPresent()) {
            request.setRequestedBy(requesterOpt.get());
            request.setBloodCenter(centerOpt.get());
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            return requestRepository.save(request);
        }

        throw new IllegalArgumentException("Invalid requester or blood center ID");
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
    public BloodDonationRequest updateRequest(Long id, BloodDonationRequest updatedRequest, Long bloodCenterId) {
        Optional<BloodDonationRequest> existingOpt = requestRepository.findById(id);
        if (!existingOpt.isPresent()) {
            return null;
        }

        BloodDonationRequest existing = existingOpt.get();

        existing.setBloodType(updatedRequest.getBloodType());
        existing.setStatus(updatedRequest.getStatus());
        existing.setQuantity(updatedRequest.getQuantity());
        existing.setUpdatedAt(LocalDateTime.now());

        if (bloodCenterId != null) {
            Optional<BloodCenter> newCenterOpt = bloodCenterRepository.findById(bloodCenterId);
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
