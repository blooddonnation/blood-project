package com.example.donation_history_service.repositories;
import com.example.donation_history_service.entities.UserPoints;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserPointsRepository extends MongoRepository<UserPoints, String> {
    Optional<UserPoints> findById(String userId);
}