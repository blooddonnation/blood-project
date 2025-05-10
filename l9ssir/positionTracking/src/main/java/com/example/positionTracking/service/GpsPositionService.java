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

    @Scheduled(cron = "0 0 3 * * ?", zone = "UTC")
    public void deletePositionsOlderThanOneDay() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        gpsPositionRepository.deleteByTimestampLessThan(oneDayAgo);
        System.out.println("Deleted positions older than one day at: " + LocalDateTime.now());
    }
}