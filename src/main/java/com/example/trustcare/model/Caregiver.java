package com.example.trustcare.model;

public class Caregiver {

    private int careGiverId;
    private String fullName;
    private String email;
    private String password;
    private String CNIC;
    private int experienceYears;
    private float monthlyRate;

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    private String location;
    private String bio;
    private String contact;
    private String address;
    private boolean isVerified;

    public int getCareGiverId() {
        return careGiverId;
    }

    public void setCareGiverId(int careGiverId) {
        this.careGiverId = careGiverId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public float getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(float monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
