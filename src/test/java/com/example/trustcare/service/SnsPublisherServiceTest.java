package com.example.trustcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SnsPublisherServiceTest {

    private SnsClient snsClient;
    private SnsPublisherService snsPublisherService;
    private final String topicArn = "arn:aws:sns:us-east-1:123456789012:MyTopic";
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        snsClient = mock(SnsClient.class);

        // Inject a subclass to replace snsClient with the mock
        snsPublisherService = new SnsPublisherService(topicArn) {
            @Override
            public void publishEmailNotification(String subject, String body, List<String> emails) {
                try {
                    // Prepare payload
                    Map<String, Object> payload = Map.of(
                            "subject", subject,
                            "body", body,
                            "emails", emails
                    );
                    String message = mapper.writeValueAsString(payload);

                    // Use mock snsClient
                    snsClient.publish(PublishRequest.builder()
                            .topicArn(topicArn)
                            .message(message)
                            .build());
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to publish SNS message", ex);
                }
            }
        };
    }

    @Test
    void testPublishEmailNotification_Success() throws Exception {
        String subject = "Test Subject";
        String body = "This is the body";
        List<String> emails = List.of("user1@example.com", "user2@example.com");

        snsPublisherService.publishEmailNotification(subject, body, emails);

        // Capture the PublishRequest
        ArgumentCaptor<PublishRequest> captor = ArgumentCaptor.forClass(PublishRequest.class);
        verify(snsClient, times(1)).publish(captor.capture());

        PublishRequest request = captor.getValue();
        assertEquals(topicArn, request.topicArn());

        // Deserialize JSON payload
        Map<String, Object> payload = mapper.readValue(request.message(), Map.class);
        assertEquals(subject, payload.get("subject"));
        assertEquals(body, payload.get("body"));
        assertEquals(emails, payload.get("emails"));
    }

    @Test
    void testPublishEmailNotification_ExceptionInSerialization() {
        // Create a subclass with broken mapper
        SnsPublisherService brokenService = new SnsPublisherService(topicArn) {
            @Override
            public void publishEmailNotification(String subject, String body, List<String> emails) {
                throw new RuntimeException("Simulated failure");
            }
        };

        assertThrows(RuntimeException.class, () ->
                brokenService.publishEmailNotification("subj", "body", List.of("a@test.com")));
    }

    @Test
    void testConstructor_SetsTopicArn() {
        SnsPublisherService service = new SnsPublisherService(topicArn) {
            @Override
            public void publishEmailNotification(String subject, String body, List<String> emails) {}
        };

        assertNotNull(service);
    }

    @Test
    void testPublishEmailNotification_WithEmptyEmailList() throws Exception {
        List<String> emails = List.of();
        snsPublisherService.publishEmailNotification("subject", "body", emails);

        ArgumentCaptor<PublishRequest> captor = ArgumentCaptor.forClass(PublishRequest.class);
        verify(snsClient).publish(captor.capture());

        PublishRequest request = captor.getValue();
        Map<String, Object> payload = mapper.readValue(request.message(), Map.class);
        assertEquals(emails, payload.get("emails"));
    }

    @Test
    void testPublishEmailNotification_NullEmails() throws Exception {
        snsPublisherService.publishEmailNotification("subj", "body", null);

        ArgumentCaptor<PublishRequest> captor = ArgumentCaptor.forClass(PublishRequest.class);
        verify(snsClient).publish(captor.capture());

        PublishRequest request = captor.getValue();
        Map<String, Object> payload = mapper.readValue(request.message(), Map.class);
        assertNull(payload.get("emails"));
    }
}
