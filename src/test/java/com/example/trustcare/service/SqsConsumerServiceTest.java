package com.example.trustcare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SqsConsumerServiceTest {

    private SqsClient sqsClient;
    private SesEmailService sesEmailService;
    private SqsConsumerService sqsConsumerService;
    private final String queueUrl = "https://sqs.us-east-1.amazonaws.com/123456789012/MyQueue";
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        sqsClient = mock(SqsClient.class);
        sesEmailService = mock(SesEmailService.class);
        sqsConsumerService = new SqsConsumerService(sqsClient, sesEmailService, queueUrl);
    }

    @Test
    void testPoll_ValidMessage_ProcessesAndDeletes() throws Exception {
        // given SNS-style SQS message
        String payloadJson = mapper.writeValueAsString(Map.of(
                "subject", "Hello",
                "body", "This is a test email",
                "emails", List.of("user1@example.com", "user2@example.com")
        ));

        String snsWrapped = mapper.writeValueAsString(Map.of("Message", payloadJson));

        Message message = Message.builder()
                .body(snsWrapped)
                .receiptHandle("abc123")
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(message)
                .build();

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .thenReturn(response);

        // when
        sqsConsumerService.poll();

        // then: verify SES email service is called
        verify(sesEmailService).sendEmail(
                eq("Hello"),
                eq("This is a test email"),
                eq(List.of("user1@example.com", "user2@example.com"))
        );

        // verify deleteMessage called with correct receipt handle
        ArgumentCaptor<DeleteMessageRequest> deleteCaptor =
                ArgumentCaptor.forClass(DeleteMessageRequest.class);

        verify(sqsClient).deleteMessage(deleteCaptor.capture());
        assertEquals("abc123", deleteCaptor.getValue().receiptHandle());
        assertEquals(queueUrl, deleteCaptor.getValue().queueUrl());
    }

    @Test
    void testPoll_EmptyMessages_NoProcessing() {
        // given no messages
        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(List.of())
                .build();

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .thenReturn(response);

        // when
        sqsConsumerService.poll();

        // then: no emails, no deletes
        verify(sesEmailService, never()).sendEmail(any(), any(), any());
        verify(sqsClient, never()).deleteMessage(any(DeleteMessageRequest.class));
    }

    @Test
    void testPoll_InvalidJson_MessageSkipped() {
        // given invalid JSON
        Message badMessage = Message.builder()
                .body("not a json")
                .receiptHandle("bad123")
                .build();

        ReceiveMessageResponse response = ReceiveMessageResponse.builder()
                .messages(badMessage)
                .build();

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .thenReturn(response);

        // when
        sqsConsumerService.poll();

        // then: no email sent, no delete attempted
        verify(sesEmailService, never()).sendEmail(any(), any(), any());
        verify(sqsClient, never()).deleteMessage(any(DeleteMessageRequest.class));
    }
}
