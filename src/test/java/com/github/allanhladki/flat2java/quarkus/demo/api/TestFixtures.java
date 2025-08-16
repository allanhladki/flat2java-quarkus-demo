package com.github.allanhladki.flat2java.quarkus.demo.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.github.allanhladki.flat2java.quarkus.demo.model.SampleModel;

/**
 * Test fixtures and helper methods for REST Assured tests.
 * Provides consistent test data across all test scenarios.
 */
public class TestFixtures {

    // Valid flat text that matches the SampleModel structure (total length: 129 characters)
    // Field lengths: name(30) + quantity(10) + transactionId(20) + amount(15) + accountNumber(20) 
    //+ birthDate(8) + createdAt(26)
    public static final String VALID_FLAT_TEXT = 
        "John Doe                      " + // 30 characters
        "0000000100" + // 10 characters
        "00000000001234567890" + // 20 characters
        "000000000009999" + // 15 characters
        "00000000000987654321" + // 20 characters
        "19900101" + // 8 characters
        "2024-01-01T12:00:00.000000"; // 26 characters
    
    // Alternative valid flat text with different values
    public static final String VALID_FLAT_TEXT_2 = 
        "Jane Smith                    " + // 30 characters
        "0000000200" + // 10 characters
        "00000000009876543210" + // 20 characters
        "000000000015075" + // 15 characters
        "00000000000123456789" + // 20 characters
        "19850515" + // 8 characters
        "2024-06-15T14:30:00.000000"; // 26 characters

    public static final String BOUNDARY_FLAT_TEXT = 
        "A                             " + // 30 characters
        "2147483647" + // 10 characters
        "09223372036854775807" + // 20 characters
        "999999999999999" + // 15 characters
        "99999999999999999999" + // 20 characters
        "20001231" + // 8 characters
        "2024-12-31T23:59:59.999999"; // 26 characters
    
    public static final String SPECIAL_CHARS_FLAT_TEXT = 
        "José María O'Connor-Smith     " + // 30 characters
        "0000000042" + // 10 characters
        "00000000009876543210" + // 20 characters
        "000000000012345" + // 15 characters
        "00000000000123456789" + // 20 characters
        "19850615" + // 8 characters
        "2024-06-15T14:30:00.000000"; // 26 characters

    /**
     * Creates a valid SampleModel for testing using setter methods
     */
    public static SampleModel createValidSampleModel() {
        SampleModel model = new SampleModel();
        model.setName("John Doe");
        model.setQuantity(100);
        model.setTransactionId(1234567890L);
        model.setAmount(new BigDecimal("99.99"));
        model.setAccountNumber(new BigInteger("987654321"));
        model.setBirthDate(LocalDate.of(1990, 1, 1));
        model.setCreatedAt(LocalDateTime.of(2024, 1, 1, 12, 0, 0, 0));
        return model;
    }

    /**
     * Creates an alternative valid SampleModel for testing
     */
    public static SampleModel createValidSampleModel2() {
        SampleModel model = new SampleModel();
        model.setName("Jane Smith");
        model.setQuantity(200);
        model.setTransactionId(9876543210L);
        model.setAmount(new BigDecimal("150.75"));
        model.setAccountNumber(new BigInteger("123456789"));
        model.setBirthDate(LocalDate.of(1985, 5, 15));
        model.setCreatedAt(LocalDateTime.of(2024, 6, 15, 14, 30, 0, 0));
        return model;
    }

    /**
     * Creates an invalid SampleModel for testing error scenarios
     */
    public static SampleModel createInvalidSampleModel() {
        SampleModel model = new SampleModel();
        // Set some invalid values that might cause serialization issues
        model.setName(""); // Empty name
        model.setQuantity(null); // Null quantity
        model.setTransactionId(0L); // Zero transaction ID
        model.setAmount(new BigDecimal("-1.00")); // Negative amount
        model.setAccountNumber(BigInteger.ZERO); // Zero account number
        model.setBirthDate(LocalDate.of(1900, 1, 1)); // Very old date
        model.setCreatedAt(LocalDateTime.now()); // Current time
        return model;
    }

    /**
     * Creates a SampleModel with boundary values for testing
     */
    public static SampleModel createBoundarySampleModel() {
        SampleModel model = new SampleModel();
        model.setName("A"); // Minimum name length
        model.setQuantity(Integer.MAX_VALUE); // Maximum integer value
        model.setTransactionId(Long.MAX_VALUE); // Maximum long value
        model.setAmount(new BigDecimal("999999999.99")); // Large amount
        model.setAccountNumber(new BigInteger("9999999999999999999")); // Large account number
        model.setBirthDate(LocalDate.of(2000, 12, 31)); // End of year
        model.setCreatedAt(LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999999)); // End of year with nanoseconds
        return model;
    }

    /**
     * Creates a SampleModel with special characters for testing
     */
    public static SampleModel createSpecialCharsSampleModel() {
        SampleModel model = new SampleModel();
        model.setName("José María O'Connor-Smith"); // Special characters and spaces
        model.setQuantity(42);
        model.setTransactionId(987654321L);
        model.setAmount(new BigDecimal("123.45"));
        model.setAccountNumber(new BigInteger("123456789"));
        model.setBirthDate(LocalDate.of(1985, 6, 15));
        model.setCreatedAt(LocalDateTime.of(2024, 6, 15, 14, 30, 0, 0));
        return model;
    }

    /**
     * Creates a flat text string with specific length for testing
     */
    public static String createFlatTextWithLength(int length) {
        if (length <= 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('A');
        }
        return sb.toString();
    }

    /**
     * Creates a flat text string that's too short for the expected format
     */
    public static String createShortFlatText() {
        return "ShortText";
    }

    /**
     * Creates a flat text string that's too long for the expected format
     */
    public static String createLongFlatText() {
        return createFlatTextWithLength(1000);
    }
}
