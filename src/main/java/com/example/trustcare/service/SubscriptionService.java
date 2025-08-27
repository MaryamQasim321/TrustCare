package com.example.trustcare.service;

import com.example.trustcare.config.AWSConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
public class SubscriptionService {

    private final SnsClient snsClient;
    private final String topicArn;
    private final String sqsQueueArn;
    private String subscriptionArn;

    public SubscriptionService(@Value("${aws.sns.topic-arn}") String topicArn,
                               @Value("${aws.sqs.queue-arn}") String sqsQueueArn
                               ) {
        this.topicArn = topicArn;
        this.sqsQueueArn = sqsQueueArn;
        this.snsClient = AWSConfig.snsClient();
    }
    @PostConstruct
    public void subscribeQueueAtStartup() {
        try {
            SubscribeResponse response = snsClient.subscribe(SubscribeRequest.builder()
                    .topicArn(topicArn)
                    .protocol("sqs")
                    .endpoint(sqsQueueArn)
                    .returnSubscriptionArn(true)
                    .build());
            subscriptionArn = response.subscriptionArn();
            System.out.println("SQS Queue subscribed to SNS. ARN: " + subscriptionArn);
        } catch (SnsException ex) {
            throw new RuntimeException("SNS SQS subscribe failed: " + ex.awsErrorDetails().errorMessage(), ex);
        }
    }
}
