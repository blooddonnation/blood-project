package com.example.demo.controllers;

import com.example.demo.model.CenterEntity;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/centers")
@CrossOrigin(origins = "*") // Allows access from any frontend
public class CenterController {

    @Autowired
    private CenterRepository centerRepository;

    // Retrieve all centers
    @GetMapping
    public ResponseEntity<List<CenterEntity>> getAllCenters() {
        List<CenterEntity> centers = centerRepository.findAll();
        return new ResponseEntity<>(centers, HttpStatus.OK);
    }

    // Create a new center
    @PostMapping
    public ResponseEntity<CenterEntity> createCenter(@RequestBody CenterEntity center) {
        if (center == null) {
            return ResponseEntity.badRequest().build();  // Return 400 Bad Request if input is invalid
        }
        CenterEntity createdCenter = centerRepository.save(center);
        return new ResponseEntity<>(createdCenter, HttpStatus.CREATED);
    }

    // Retrieve a center by ID
    @GetMapping("/{id}")
    public ResponseEntity<CenterEntity> getCenterById(@PathVariable Long id) {
        Optional<CenterEntity> center = centerRepository.findById(id);
        if (center.isPresent()) {
            return new ResponseEntity<>(center.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
        }
    }

    // Update a center
    @PutMapping("/{id}")
    public ResponseEntity<CenterEntity> updateCenter(@PathVariable Long id, @RequestBody CenterEntity centerDetails) {
        Optional<CenterEntity> optionalCenter = centerRepository.findById(id);
        if (optionalCenter.isPresent()) {
            CenterEntity center = optionalCenter.get();
            // Updating center fields with new values
            center.setIdAdmin(centerDetails.getIdAdmin());
            center.setLatitude(centerDetails.getLatitude());
            center.setLongitude(centerDetails.getLongitude());
            centerRepository.save(center);
            return new ResponseEntity<>(center, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
        }
    }

    // Delete a center
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long id) {
        if (centerRepository.existsById(id)) {
            centerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
        }
    }
}
