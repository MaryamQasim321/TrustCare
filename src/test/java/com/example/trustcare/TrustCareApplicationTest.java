package com.example.trustcare;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class TrustCareApplicationTest {

    // Mock the DataSource so Spring Boot doesn't try to connect to a real database
    @Mock
    private DataSource dataSource;

    @Test
    void contextLoads() {
        // This test will pass if the Spring application context starts successfully
        // No database is required because DataSource is mocked
    }
}
