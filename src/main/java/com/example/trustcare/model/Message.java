package com.example.trustcare.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {

    @Schema(description = "Unique identifier of the message", type = "integer", example = "5001")
    private int messageId;

    @Schema(description = "Content of the message", type = "String", example = "Hello, your appointment is confirmed.")
    private String content;

    @Schema(description = "ID of the message receiver (User ID)", type = "integer", example = "102")
    private int receiverId;

    @Schema(description = "Timestamp when the message was sent", type = "LocalDateTime", example = "2025-08-22T09:15:00")
    private LocalDateTime sentAt;

    @Schema(description = "ID of the message sender (User ID)", type = "integer", example = "201")
    private int senderId;

    @Schema(description = "Timestamp when the message was received", type = "local date time", example = "2025-08-22T09:16:05")
    private LocalDateTime receivedAt;


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}
