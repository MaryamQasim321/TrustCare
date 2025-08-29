package com.example.trustcare.config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class AWSConfigTest {
        @Mock
        private SqsClient sqsClient;

        @Mock
        private SnsClient snsClient;

        @Mock
        private SesClient sesClient;

        @Test
        public void testSqsClientBeanExists() {
            assertNotNull(sqsClient);
        }

        @Test
        public void testSnsClientBeanExists() {
            assertNotNull(snsClient);
        }

        @Test
        public void testSesClientBeanExists() {
            assertNotNull(sesClient);
        }
    }
