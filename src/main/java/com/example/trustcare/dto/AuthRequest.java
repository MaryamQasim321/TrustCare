package com.example.trustcare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication request containing user credentials")
public class AuthRequest {

    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @Schema(description = "User password", example = "securePassword123")
    private String password;

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
}
