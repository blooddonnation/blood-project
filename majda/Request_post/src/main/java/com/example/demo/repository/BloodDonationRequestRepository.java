package com.example.demo.repository;


import com.example.demo.model.BloodDonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BloodDonationRequestRepository extends JpaRepository<BloodDonationRequest, Long>{

}
