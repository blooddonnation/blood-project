package com.example.demo.Repository;


import com.example.demo.model.EventEntity;
import com.example.demo.model.CenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByCenterAndDate(CenterEntity center, String date);
}

