package com.example.trustcare.model;

import com.example.trustcare.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;

@Data
public class User {

    @Schema(description = "Unique identifier for the user", type = "integer", example = "1001")
    private Integer userId;

    @Schema(description = "Full name of the user", type = "String", example = "Maryam Qasim")
    private String fullName;

    @Schema(description = "Residential address of the user",type = "String", example = "123 Main Street, Karachi, Pakistan")
    private String address;

    @Schema(description = "Email address of the user", type = "String", example = "maryam@example.com")
    private String email;

    @Schema(description = "Password (hashed in DB)", type = "String", example = "hashedPassword123")
    private String password;

    @Schema(description = "Contact number of the user", type = "String", example = "+92-300-1234567")
    private String contact;

    @Schema(description = "Timestamp when the user account was created", type = "local date time", example = "2025-08-22 10:15:30")
    private Timestamp createdAt;

    @Schema(description = "Role assigned to the user", example = "USER")
    private Role role = Role.USER;
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
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
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
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
//    public String getContact() {
//        return contact;
//    }
//
//    public void setContact(String contact) {
//        this.contact = contact;
//    }
//
//    public Timestamp getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Timestamp createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
}
