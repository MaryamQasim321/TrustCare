package com.example.trustcare.logging;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogUtilsTest {

    @Test
    void testWarn() {
        String message = "This is a warning";
        String expected = "{\"status\": \"warn\", \"message\": \"This is a warning\"}";
        assertEquals(expected, LogUtils.warn(message));
    }

    @Test
    void testError() {
        String message = "Something went wrong";
        String expected = "{\"status\": \"error\", \"message\": \"Something went wrong\"}";
        assertEquals(expected, LogUtils.error(message));
    }

    @Test
    void testInfo() {
        String message = "Just info";
        String expected = "{\"status\": \"info\", \"message\": \"Just info\"}";
        assertEquals(expected, LogUtils.info(message));
    }

    @Test
    void testDebug() {
        String message = "Debugging";
        String expected = "{\"status\": \"debug\", \"message\": \"Debugging\"}";
        assertEquals(expected, LogUtils.debug(message));
    }

    @Test
    void testSuccess() {
        String message = "Operation successful";
        String expected = "{\"status\": \"success\", \"message\": \"Operation successful\"}";
        assertEquals(expected, LogUtils.success(message));
    }
}
