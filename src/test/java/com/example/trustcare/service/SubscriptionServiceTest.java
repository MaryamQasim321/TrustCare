package com.example.trustcare.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SnsClient snsClient;

    private SubscriptionService subscriptionService;

    private final String topicArn = "arn:aws:sns:us-east-1:123456789012:MyTopic";
    private final String sqsQueueArn = "arn:aws:sqs:us-east-1:123456789012:MyQueue";

    @BeforeEach
    void setUp() throws Exception {
        subscriptionService = new SubscriptionService(topicArn, sqsQueueArn);

        // Inject mock snsClient into subscriptionService via reflection
        Field clientField = SubscriptionService.class.getDeclaredField("snsClient");
        clientField.setAccessible(true);
        clientField.set(subscriptionService, snsClient);
    }

    @Test
    void testSubscribeQueueAtStartup_Success() {
        // Arrange
        SubscribeResponse mockResponse = SubscribeResponse.builder()
                .subscriptionArn("arn:aws:sns:sub:123")
                .build();

        when(snsClient.subscribe(any(SubscribeRequest.class))).thenReturn(mockResponse);

        // Act
        subscriptionService.subscribeQueueAtStartup();

        // Assert
        ArgumentCaptor<SubscribeRequest> requestCaptor =
                ArgumentCaptor.forClass(SubscribeRequest.class);
        verify(snsClient, times(1)).subscribe(requestCaptor.capture());

        SubscribeRequest request = requestCaptor.getValue();
        assertEquals(topicArn, request.topicArn());
        assertEquals(sqsQueueArn, request.endpoint());
        assertEquals("sqs", request.protocol());
    }

    @Test
    void testSubscribeQueueAtStartup_Failure() {
        // Arrange
        AwsErrorDetails errorDetails = AwsErrorDetails.builder()
                .errorMessage("Access Denied")
                .build();

        SnsException snsException = (SnsException) SnsException.builder()
                .awsErrorDetails(errorDetails)
                .message("Access Denied")
                .build();

        when(snsClient.subscribe(any(SubscribeRequest.class))).thenThrow(snsException);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> subscriptionService.subscribeQueueAtStartup());

        assertTrue(thrown.getMessage().contains("SNS SQS subscribe failed"));
    }
}
