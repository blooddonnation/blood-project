package com.example.positionTracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PositionTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PositionTrackingApplication.class, args);
	}
	
	

}
