package com.example.demo.service;

import com.example.demo.model.BloodCenter;
import com.example.demo.repository.BloodCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodCenterService {

    @Autowired
    private BloodCenterRepository bloodCenterRepository;

    public BloodCenter createCenter(BloodCenter center) {
        return bloodCenterRepository.save(center);
    }

    public List<BloodCenter> getAllCenters() {
        return bloodCenterRepository.findAll();
    }

    public BloodCenter getCenterById(Long id) {
        return bloodCenterRepository.findById(id).orElse(null);
    }

    public BloodCenter updateCenter(Long id, BloodCenter centerDetails) {
        return bloodCenterRepository.findById(id)
            .map(existingCenter -> {
                existingCenter.setName(centerDetails.getName());
                existingCenter.setLocation(centerDetails.getLocation());
                existingCenter.setManager(centerDetails.getManager());
                return bloodCenterRepository.save(existingCenter);
            })
            .orElse(null);
    }

    public boolean deleteCenter(Long id) {
        if (bloodCenterRepository.existsById(id)) {
            bloodCenterRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 