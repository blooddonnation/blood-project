package com.example.donation_history_service.repositories;
import java.util.List;


import com.example.donation_history_service.entities.DonationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DonationHistoryRepository extends MongoRepository<DonationHistory, String> {
    List<DonationHistory> findByUserId(String userId);
}