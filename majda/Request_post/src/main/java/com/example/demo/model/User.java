package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String bloodType;
    
    

    @Column(nullable = false)
    private String role = "USER";

    // Blood requests made by this user
    @OneToMany(mappedBy = "requestedBy", cascade = CascadeType.ALL)
    private List<BloodDonationRequest> bloodRequests;

    // Blood donations made by this user
    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    private List<Donation> bloodDonations;

    
    
	public User() {
		super();
	}

	public User(Long id, String username, String password, String email, LocalDate dateOfBirth, String bloodType,
			String role, List<BloodDonationRequest> bloodRequests, List<Donation> bloodDonations) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.bloodType = bloodType;
		this.role = role;
		this.bloodRequests = bloodRequests;
		this.bloodDonations = bloodDonations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<BloodDonationRequest> getBloodRequests() {
		return bloodRequests;
	}

	public void setBloodRequests(List<BloodDonationRequest> bloodRequests) {
		this.bloodRequests = bloodRequests;
	}

	public List<Donation> getBloodDonations() {
		return bloodDonations;
	}

	public void setBloodDonations(List<Donation> bloodDonations) {
		this.bloodDonations = bloodDonations;
	}

	public User(Long id) {
		super();
		this.id = id;
	}


}
