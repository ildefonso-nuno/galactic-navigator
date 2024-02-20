package org.nildefonso.navigatorback.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorDetailsTests {

    @Test
    void testGettersAndSetters(){
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails errorDetails = new ErrorDetails();

        errorDetails.setTimestamp(now);
        errorDetails.setMessage("Test Message");
        errorDetails.setPath("/test/path");
        errorDetails.setErrorCode("404");

        assertEquals(now,errorDetails.getTimestamp());
        assertEquals("Test Message",errorDetails.getMessage());
        assertEquals("/test/path",errorDetails.getPath());
        assertEquals("404",errorDetails.getErrorCode());
    }

    @Test
    void testConstructor(){
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails errorDetails = new ErrorDetails(now, "Test Message", "/test/path", "404");

        assertEquals(now,errorDetails.getTimestamp());
        assertEquals("Test Message",errorDetails.getMessage());
        assertEquals("/test/path",errorDetails.getPath());
        assertEquals("404",errorDetails.getErrorCode());
    }
}
