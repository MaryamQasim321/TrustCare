package com.example.trustcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.trustcare.config.AWSConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SnsPublisherService {

    private final SnsClient snsClient;
    private final String topicArn;
    private final ObjectMapper mapper = new ObjectMapper();

    public SnsPublisherService(@Value("${aws.sns.topic-arn}") String topicArn) {
        this.snsClient = AWSConfig.snsClient();
        this.topicArn = topicArn;
    }

    public void publishEmailNotification(String subject, String body, List<String> emails) {
        try {
            // Prepare the message payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("subject", subject);
            payload.put("body", body);
            payload.put("emails", emails);
            String message = mapper.writeValueAsString(payload);
            snsClient.publish(PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .build());

        } catch (Exception ex) {
            throw new RuntimeException("Failed to publish SNS message", ex);
        }
    }
}
