package com.example.demo.repository;
import com.example.demo.model.Donation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long>{
	List<Donation> findByDonationRequestIdOrderByCreatedAtAsc(Long donationRequestId);
}
