package com.example.demo.dto;

public class BloodDonationRequestCreateDTO {
    private String bloodType;
    private double quantity;
    private Long requestedById;
    private Long bloodCenterId;

    // Default constructor
    public BloodDonationRequestCreateDTO() {}

    // Parameterized constructor
    public BloodDonationRequestCreateDTO(String bloodType, double quantity, Long requestedById, Long bloodCenterId) {
        this.bloodType = bloodType;
        this.quantity = quantity;
        this.requestedById = requestedById;
        this.bloodCenterId = bloodCenterId;
    }

    // Getters and Setters
    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
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

    public Long getBloodCenterId() {
        return bloodCenterId;
    }

    public void setBloodCenterId(Long bloodCenterId) {
        this.bloodCenterId = bloodCenterId;
    }
}