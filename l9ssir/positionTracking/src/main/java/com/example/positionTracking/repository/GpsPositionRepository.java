package com.example.positionTracking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.positionTracking.model.GpsPosition;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface GpsPositionRepository extends MongoRepository<GpsPosition, String> {
    Optional<GpsPosition> findByDeviceId(String deviceId);

    void deleteByTimestampLessThan(LocalDateTime cutoff);
}