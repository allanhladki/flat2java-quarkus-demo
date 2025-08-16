package com.github.allanhladki.flat2java.quarkus.demo.api;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

import com.github.allanhladki.flat2java.quarkus.demo.model.ErrorResponse;

/**
 * Utility class for common test assertions.
 * Provides reusable assertion methods for testing API responses.
 */
public class TestAssertions {
    
    /**
     * Asserts that an error response has the expected error type and message,
     * and validates that the timestamp and UUID are valid.
     * 
     * @param errorResponse the error response to validate
     * @param expectedError the expected error type
     * @param expectedMessage the expected error message
     */
    public static void assertError(ErrorResponse errorResponse, String expectedError, String expectedMessage) {
        assertNotNull(errorResponse, "Error response should not be null");
        
        // Verify error and message fields
        assertEquals(expectedError, errorResponse.getError(), "Error type should match expected value");
        assertEquals(expectedMessage, errorResponse.getMessage(), "Error message should match expected value");
        
        // Verify timestamp is valid (not null and not in the future)
        assertNotNull(errorResponse.getTimestamp(), "Timestamp should not be null");
        assertTrue(errorResponse.getTimestamp().isBefore(Instant.now().plusSeconds(1)), 
                  "Timestamp should not be in the future");
        
        // Verify UUID is valid
        assertNotNull(errorResponse.getId(), "Request ID should not be null");
        assertTrue(isValidUUID(errorResponse.getId().toString()), "Request ID should be a valid UUID");
    }
    
    /**
     * Asserts that an error response has the expected error type and message,
     * and validates that the timestamp and UUID are valid.
     * 
     * @param errorResponse the error response to validate
     * @param expectedError the expected error type
     * @param expectedMessage the expected error message
     * @param expectedId the expected request ID
     */
    public static void assertError(ErrorResponse errorResponse, String expectedError, String expectedMessage, UUID expectedId) {
        assertError(errorResponse, expectedError, expectedMessage);
        
        // Verify the specific request ID
        assertEquals(expectedId, errorResponse.getId(), "Request ID should match expected value");
    }
    
    /**
     * Validates if a string is a valid UUID format.
     * 
     * @param uuidString the string to validate
     * @return true if the string is a valid UUID, false otherwise
     */
    private static boolean isValidUUID(String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
