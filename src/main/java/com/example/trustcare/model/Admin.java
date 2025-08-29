package com.example.trustcare.model;

import com.example.trustcare.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Admin entity representing an administrator in trustcare system.")
public class Admin {

    @Schema(description = "Unique identifier for the admin", type = "integer", example = "1")
    private int adminId;

    @Schema(description = "Full name of the admin",type = "String", example = "Alice Johnson")
    private String fullName;

    @Schema(description = "Email address of the admin", type = "String", example = "admin@trustcare.com")
    private String email;

    @Schema(description = "Password for authentication (stored securely)", type = "String", example = "P@ssw0rd")
    private String password;

    @Schema(description = "Role of the admin (default = ADMIN)",  example = "ADMIN")
    private Role role = Role.ADMIN;

//
//    public int getAdminId() {
//        return adminId;
//    }
//
//    public void setAdminId(int adminId) {
//        this.adminId = adminId;
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
}