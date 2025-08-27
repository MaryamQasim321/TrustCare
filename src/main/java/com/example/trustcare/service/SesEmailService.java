package com.example.trustcare.service;

import com.example.trustcare.config.AWSConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.List;

@Service
public class SesEmailService {

    private final SesClient sesClient;
    private final String fromAddress;

    public SesEmailService(@Value("${aws.ses.from}") String fromAddress) {
        this.sesClient = AWSConfig.sesClient();
        this.fromAddress = fromAddress;
    }

    public void sendEmail(String subject, String bodyText, List<String> toAddresses) {
        Destination destination = Destination.builder().toAddresses(toAddresses).build();
        Content subj = Content.builder().data(subject).build();
        Content text = Content.builder().data(bodyText).build();
        Body body = Body.builder().text(text).build();
        Message message = Message.builder().subject(subj).body(body).build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(fromAddress)
                .build();

        sesClient.sendEmail(request);
    }
}
