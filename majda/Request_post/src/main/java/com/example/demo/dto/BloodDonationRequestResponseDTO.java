package com.example.demo.dto;


public class BloodDonationRequestResponseDTO {

    private Long id;
    private String bloodType;
    private String status;
    private double quantity;
    private Long requestedById; // ID of the person who made the request
    private Long bloodCenterId;
    
    public BloodDonationRequestResponseDTO(Long id, String bloodType, String status, double quantity, Long requestedById, Long bloodCenterId) {
        this.id = id;
        this.bloodType = bloodType;
        this.status = status;
        this.quantity = quantity;
        this.requestedById = requestedById;
        this.bloodCenterId=bloodCenterId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Long getBloodCenterId() {
		return bloodCenterId;
	}

	public void setBloodCenterId(Long bloodCenterId) {
		this.bloodCenterId = bloodCenterId;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Long getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(Long requestedById) {
        this.requestedById = requestedById;
    }
}
