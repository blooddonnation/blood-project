package com.example.demo.dto;

import java.time.LocalDateTime;

public class DonationResponseDTO {
    private Long id;
    private Long donorId;
    private Long requestId;
    private LocalDateTime createdAt;

    public DonationResponseDTO(Long id, Long donorId, Long requestId, LocalDateTime createdAt) {
        this.id = id;
        this.donorId = donorId;
        this.requestId = requestId;
        this.createdAt = createdAt;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDonorId() {
		return donorId;
	}

	public void setDonorId(Long donorId) {
		this.donorId = donorId;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
