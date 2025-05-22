package com.example.positionTracking.service;

import com.example.positionTracking.model.GpsPosition;
import com.example.positionTracking.repository.GpsPositionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class GpsPositionService {

    @Autowired
    private GpsPositionRepository gpsPositionRepository;

    public List<GpsPosition> getAllGpsPositions() {
        return gpsPositionRepository.findAll();
    }

    public GpsPosition saveOrUpdatePosition(GpsPosition newPosition) {
        Optional<GpsPosition> existingPositionOptional = gpsPositionRepository.findByDeviceId(newPosition.getDeviceId());

        if (existingPositionOptional.isPresent()) {
            GpsPosition existingPosition = existingPositionOptional.get();
            existingPosition.setLatitude(newPosition.getLatitude());
            existingPosition.setLongitude(newPosition.getLongitude());
            existingPosition.setTimestamp(LocalDateTime.now()); // Update timestamp
            return gpsPositionRepository.save(existingPosition);
        } else {
            newPosition.setTimestamp(LocalDateTime.now());
            return gpsPositionRepository.save(newPosition);
        }
    }

    @Scheduled(cron = "0 0 * * * ?", zone = "UTC")
    public void deletePositionsOlderThanOneDay() {
        try {
            LocalDateTime oneDayAgo = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
            long countBefore = gpsPositionRepository.count();
            gpsPositionRepository.deleteByTimestampLessThan(oneDayAgo);
            long countAfter = gpsPositionRepository.count();
            long deletedCount = countBefore - countAfter;
            System.out.println("Cleanup job executed at: " + LocalDateTime.now());
            System.out.println("Deleted " + deletedCount + " positions older than " + oneDayAgo);
        } catch (Exception e) {
            System.err.println("Error during cleanup job: " + e.getMessage());
            e.printStackTrace();
        }
    }
}