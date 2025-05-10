package com.example.positionTracking.controller;

import com.example.positionTracking.model.GpsPosition;
import com.example.positionTracking.repository.GpsPositionRepository;
import com.example.positionTracking.service.GpsPositionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/positions")
public class GpsPositionController {

    private final GpsPositionService gpsPositionService;

    @Autowired
    public GpsPositionController(GpsPositionService gpsPositionService) {
        this.gpsPositionService = gpsPositionService;
    }

    @GetMapping
    public List<GpsPosition> getAllPositions() {
        return gpsPositionService.getAllGpsPositions();
    }

    @PostMapping
    public ResponseEntity<GpsPosition> updateOrSavePosition(@RequestBody GpsPosition gpsPosition) {
        GpsPosition savedPosition = gpsPositionService.saveOrUpdatePosition(gpsPosition);
        return new ResponseEntity<>(savedPosition, HttpStatus.OK);
    }
    
    
   
}