package com.example.trustcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {

    @Schema(description = "Unique identifier of the payment", type = "integer", example = "9001")
    private int paymentId;

    @Schema(description = "ID of the related booking", type = "integer", example = "3001")
    private int bookingId;

    @Schema(description = "Amount paid", type = "big decimal", example = "1500.00")
    private BigDecimal amount;

    @Schema(description = "Payment method used", type = "String", example = "Credit Card")
    private String method;

    @Schema(description = "Current status of the payment", type = "String", example = "COMPLETED")
    private String status;

    @Schema(description = "Date and time when the payment was made", type = "local date time", example = "2025-08-22T10:30:00")
    private LocalDateTime paymentDate;


}
