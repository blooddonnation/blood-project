package com.example.demo.controller;

import com.example.demo.model.BloodCenter;
import com.example.demo.service.BloodCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centers")
public class BloodCenterController {

    @Autowired
    private BloodCenterService bloodCenterService;

    @PostMapping
    public ResponseEntity<BloodCenter> createCenter(@RequestBody BloodCenter center) {
        BloodCenter createdCenter = bloodCenterService.createCenter(center);
        return ResponseEntity.ok(createdCenter);
    }

    @GetMapping
    public ResponseEntity<List<BloodCenter>> getAllCenters() {
        return ResponseEntity.ok(bloodCenterService.getAllCenters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodCenter> getCenterById(@PathVariable Long id) {
        BloodCenter center = bloodCenterService.getCenterById(id);
        return center != null ? ResponseEntity.ok(center) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodCenter> updateCenter(@PathVariable Long id, @RequestBody BloodCenter centerDetails) {
        BloodCenter updatedCenter = bloodCenterService.updateCenter(id, centerDetails);
        return updatedCenter != null ? ResponseEntity.ok(updatedCenter) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long id) {
        if (bloodCenterService.deleteCenter(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 