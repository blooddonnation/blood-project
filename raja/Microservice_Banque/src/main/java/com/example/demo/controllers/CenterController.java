package com.example.demo.controllers;

import com.example.demo.model.CenterEntity;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;
import com.example.demo.events.CenterEvent;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/centers")
public class CenterController {

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping
    public ResponseEntity<List<CenterEntity>> getAllCenters() {
        List<CenterEntity> centers = centerRepository.findAll();
        return new ResponseEntity<>(centers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CenterEntity> createCenter(@RequestBody CenterEntity center) {
        if (center == null) {
            return ResponseEntity.badRequest().build();
        }
        CenterEntity createdCenter = centerRepository.save(center);
        
        // Publish center creation event
        CenterEvent event = new CenterEvent();
        event.setEventType("CREATE");
        event.setCenterId(createdCenter.getId());
        event.setName(createdCenter.getName());
        event.setLocation(createdCenter.getLocation());
        event.setTimestamp(LocalDateTime.now());
        kafkaTemplate.send("center-events", event.getCenterId().toString(), event);
        
        return new ResponseEntity<>(createdCenter, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CenterEntity> getCenterById(@PathVariable Long id) {
        Optional<CenterEntity> center = centerRepository.findById(id);
        if (center.isPresent()) {
            return new ResponseEntity<>(center.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CenterEntity> updateCenter(@PathVariable Long id, @RequestBody CenterEntity centerDetails) {
        Optional<CenterEntity> optionalCenter = centerRepository.findById(id);
        if (optionalCenter.isPresent()) {
            CenterEntity center = optionalCenter.get();
            center.setIdAdmin(centerDetails.getIdAdmin());
            center.setLatitude(centerDetails.getLatitude());
            center.setLongitude(centerDetails.getLongitude());
            center.setName(centerDetails.getName());
            center.setLocation(centerDetails.getLocation());
            centerRepository.save(center);
            
            // Publish center update event
            CenterEvent event = new CenterEvent();
            event.setEventType("UPDATE");
            event.setCenterId(center.getId());
            event.setName(center.getName());
            event.setLocation(center.getLocation());
            event.setTimestamp(LocalDateTime.now());
            kafkaTemplate.send("center-events", event.getCenterId().toString(), event);
            
            return new ResponseEntity<>(center, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long id) {
        if (centerRepository.existsById(id)) {
            centerRepository.deleteById(id);
            
            // Publish center deletion event
            CenterEvent event = new CenterEvent();
            event.setEventType("DELETE");
            event.setCenterId(id);
            event.setTimestamp(LocalDateTime.now());
            kafkaTemplate.send("center-events", event.getCenterId().toString(), event);
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}