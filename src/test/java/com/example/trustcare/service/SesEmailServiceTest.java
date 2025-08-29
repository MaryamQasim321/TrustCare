package com.example.trustcare.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SesEmailServiceTest {

    @Mock
    private SesClient sesClient;

    private SesEmailService sesEmailService;

    @BeforeEach
    void setUp() throws Exception {
        // Construct with dummy fromAddress
        sesEmailService = new SesEmailService("no-reply@example.com");

        // Inject mocked sesClient via reflection
        Field clientField = SesEmailService.class.getDeclaredField("sesClient");
        clientField.setAccessible(true);
        clientField.set(sesEmailService, sesClient);
    }

    @Test
    void testSendEmail() {
        String subject = "Test Subject";
        String body = "Hello, this is a test";
        List<String> recipients = List.of("recipient@example.com");

        sesEmailService.sendEmail(subject, body, recipients);

        ArgumentCaptor<SendEmailRequest> requestCaptor =
                ArgumentCaptor.forClass(SendEmailRequest.class);

        verify(sesClient, times(1)).sendEmail(requestCaptor.capture());

        SendEmailRequest request = requestCaptor.getValue();

        // Assert request values
        assertEquals("no-reply@example.com", request.source());
        assertEquals("Test Subject", request.message().subject().data());
        assertEquals("Hello, this is a test", request.message().body().text().data());
        assertEquals("recipient@example.com", request.destination().toAddresses().get(0));
    }
}
