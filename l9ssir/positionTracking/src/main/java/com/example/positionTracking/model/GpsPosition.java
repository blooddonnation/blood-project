package com.example.positionTracking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gps_positions")
public class GpsPosition {
    @Id
    private String id;
    private String deviceId;
    private double latitude;
    private double longitude;
    private java.time.LocalDateTime timestamp;
    // Constructors (no-arg and all-args)
    public GpsPosition() {}

    public GpsPosition(String deviceId, double latitude, double longitude) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    

    public java.time.LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(java.time.LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
    public String toString() {
        return "GpsPosition{" +
               "id='" + id + '\'' +
               ", deviceId='" + deviceId + '\'' +
               ", latitude=" + latitude +
               ", longitude=" + longitude +
               '}';
    }
}