package com.example.demo.repository;

import com.example.demo.model.BloodCenter;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodCenterRepository extends JpaRepository<BloodCenter, Long> {
    
    // Optional: find by name
    BloodCenter findByName(String name);
    
    // Optional: find by location
    BloodCenter findByLocation(String location);

}
