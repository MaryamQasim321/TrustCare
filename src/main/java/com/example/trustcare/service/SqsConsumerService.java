package com.example.trustcare.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import java.util.List;

@Service
public class SqsConsumerService {
    private final SqsClient sqsClient;
    private final SesEmailService sesEmailService;
    private final String queueUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SqsConsumerService(SqsClient sqsClient,
                              SesEmailService sesEmailService,
                              @Value("${aws.sqs.queue-url}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.sesEmailService = sesEmailService;
        this.queueUrl = queueUrl;
    }

    @Scheduled(fixedDelay = 5000)
    public void poll() {
        ReceiveMessageRequest req = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();
        List<Message> messages = sqsClient.receiveMessage(req).messages();
        for (Message msg : messages) {
            try {
                JsonNode root = objectMapper.readTree(msg.body());
                String snsMessage = root.path("Message").asText();
                JsonNode payload = objectMapper.readTree(snsMessage);
                String subject = payload.path("subject").asText();
                String body = payload.path("body").asText();
                List<String> recipients = objectMapper.convertValue(
                        payload.path("emails"),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                );
                if (!recipients.isEmpty()) {
                    sesEmailService.sendEmail(subject, body, recipients);
                }

                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(msg.receiptHandle())
                        .build());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
