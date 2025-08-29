package com.example.trustcare.model;

import com.example.trustcare.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Caregiver {

    @Schema(description = "Unique identifier of caregiver",type = "integer", example = "201")
    private int careGiverId;

    @Schema(description = "Full name of the caregiver", type = "String", example = "Sarah Khan")
    private String fullName;

    @Schema(description = "Email of caregiver",type = "String", example = "sarah.khan@example.com")
    private String email;

    @Schema(description = "Password (hashed in database)", type = "String", example = "encryptedPassword123")
    private String password;

    @Schema(description = "Role of the caregiver (default CAREGIVER)",  example = "CAREGIVER")
    private Role role = Role.CAREGIVER;

    @Schema(description = "National CNIC of caregiver", type = "String", example = "35202-1234567-8")
    private String CNIC;

    @Schema(description = "Years of caregiving experience", type = "integer", example = "5")
    private Integer experienceYears;

    @Schema(description = "Monthly rate charged by caregiver", type = "big decimal", example = "50000.00")
    private BigDecimal monthlyRate;

    @Schema(description = "Account creation timestamp", type = "local date time", example = "2025-08-22T09:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "City or area of caregiver", type = "String", example = "Lahore, Pakistan")
    private String location;

    @Schema(description = "Short bio or description", type = "String", example = "Experienced elderly care specialist with 5+ years of experience.")
    private String bio;

    @Schema(description = "Phone number of caregiver", type = "String", example = "+92-300-1234567")
    private String phoneNumber;

    @Schema(description = "Complete address of caregiver", type = "String", example = "123 Street, Model Town, Lahore")
    private String address;

    @Schema(description = "Whether caregiver profile is verified", type = "boolean", example = "true")
    private boolean isVerified;

//    public int getCareGiverId() {
//
//        return careGiverId;
//    }
//
//    public void setCareGiverId(int careGiverId) {
//        this.careGiverId = careGiverId;
//    }
//
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    public String getCNIC() {
//        return CNIC;
//    }
//
//    public void setCNIC(String CNIC) {
//        this.CNIC = CNIC;
//    }
//
//    public Integer getExperienceYears() {
//        return experienceYears;
//    }
//
//    public void setExperienceYears(Integer experienceYears) {
//        this.experienceYears = experienceYears;
//    }
//
//    public BigDecimal getMonthlyRate() {
//        return monthlyRate;
//    }
//
//    public void setMonthlyRate(BigDecimal monthlyRate) {
//        this.monthlyRate = monthlyRate;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getBio() {
//        return bio;
//    }
//
//    public void setBio(String bio) {
//        this.bio = bio;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public boolean isVerified() {
//        return isVerified;
//    }
//
//    public void setVerified(boolean verified) {
//        isVerified = verified;
//    }
}
