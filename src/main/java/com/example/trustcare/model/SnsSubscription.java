package com.example.trustcare.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SnsSubscription {

        private int userId;
        private String email;
        private String subscriptionArn;
        private LocalDate lastUsedDate;
        private boolean active;

        public SnsSubscription(int userId, String email, String subscriptionArn, LocalDate lastUsedDate, boolean active) {
                this.userId = userId;
                this.email = email;
                this.subscriptionArn = subscriptionArn;
                this.lastUsedDate = lastUsedDate;
                this.active = active;

        }
        public SnsSubscription() {

        }

        public int getUserId() {
                return userId;
        }

        public void setUserId(int userId) {
                this.userId = userId;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getSubscriptionArn() {
                return subscriptionArn;
        }

        public void setSubscriptionArn(String subscriptionArn) {
                this.subscriptionArn = subscriptionArn;
        }

        public LocalDate getLastUsedDate() {
                return lastUsedDate;
        }

        public void setLastUsedDate(LocalDate lastUsedDate) {
                this.lastUsedDate = lastUsedDate;
        }

        public boolean isActive() {
                return active;
        }

        public void setActive(boolean active) {
                this.active = active;
        }
}
