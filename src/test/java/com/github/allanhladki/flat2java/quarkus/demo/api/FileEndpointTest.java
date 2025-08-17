package com.github.allanhladki.flat2java.quarkus.demo.api;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.github.allanhladki.flat2java.quarkus.demo.model.ErrorResponse;
import com.github.allanhladki.flat2java.quarkus.demo.model.SampleModel;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;

/**
 * REST Assured tests for the file reading endpoint.
 * Tests both success and error scenarios for reading flat files from the filesystem.
 */
@QuarkusTest
@TestProfile(FileEndpointTest.TestProfile.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileEndpointTest {

    private static final String FILE_PATH = "/file";
    
    private Path tempDir;
    private Path validFlatFile;
    private Path invalidFlatFile;
    private Path emptyFile;
    private Path nonExistentFile;
    
    /**
     * Test profile to ensure proper test configuration
     */
    public static class TestProfile implements QuarkusTestProfile {
        // Default test profile
    }
    
    @BeforeEach
    void setUp() throws IOException {
        // Create temporary directory manually
        tempDir = Files.createTempDirectory("file-test-");
        
        // Create test files in temporary directory
        validFlatFile = tempDir.resolve("valid_flat.txt");
        invalidFlatFile = tempDir.resolve("invalid_flat.txt");
        emptyFile = tempDir.resolve("empty.txt");
        nonExistentFile = tempDir.resolve("nonexistent.txt");
        
        // Write valid flat text content
        Files.writeString(validFlatFile, TestFixtures.VALID_FLAT_TEXT);
        
        // Write invalid flat text content
        Files.writeString(invalidFlatFile, TestFixtures.createShortFlatText());
        
        // Create empty file
        Files.writeString(emptyFile, "");
    }
    
    @AfterEach
    void tearDown() throws IOException {
        // Clean up temporary files and directory
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        // Log but don't fail test
                        System.err.println("Failed to delete: " + path + " - " + e.getMessage());
                    }
                });
        }
    }
    
    @Test
    public void testReadFileSuccess() throws Exception {
        SampleModel result = given()
            .queryParam("path", validFlatFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createValidSampleModel();
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadFileSuccessWithAlternativeData() throws Exception {
        // Create file with alternative valid data
        Path altFile = tempDir.resolve("alt_flat.txt");
        Files.writeString(altFile, TestFixtures.VALID_FLAT_TEXT_2);
        
        SampleModel result = given()
            .queryParam("path", altFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createValidSampleModel2();
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadFileWithSpecialCharacters() throws Exception {
        // Create file with special characters
        Path specialFile = tempDir.resolve("special_flat.txt");
        Files.writeString(specialFile, TestFixtures.SPECIAL_CHARS_FLAT_TEXT);
        
        SampleModel result = given()
            .queryParam("path", specialFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createSpecialCharsSampleModel();
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadFileWithBoundaryValues() throws Exception {
        // Create file with boundary values
        Path boundaryFile = tempDir.resolve("boundary_flat.txt");
        Files.writeString(boundaryFile, TestFixtures.BOUNDARY_FLAT_TEXT);
        
        SampleModel result = given()
            .queryParam("path", boundaryFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createBoundarySampleModel();
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadFileMissingPathParameter() {
        ErrorResponse errorResponse = given()
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        TestAssertions.assertError(errorResponse, "Invalid Input", "File path parameter is required");
    }
    
    @Test
    public void testReadFileEmptyPathParameter() {
        ErrorResponse errorResponse = given()
            .queryParam("path", "")
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        TestAssertions.assertError(errorResponse, "Invalid Input", "File path parameter is required");
    }
    
    @Test
    public void testReadFileBlankPathParameter() {
        ErrorResponse errorResponse = given()
            .queryParam("path", "   ")
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        TestAssertions.assertError(errorResponse, "Invalid Input", "File path parameter is required");
    }
    
    @Test
    public void testReadFileNotFound() {
        ErrorResponse errorResponse = given()
            .queryParam("path", nonExistentFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.NOT_FOUND.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        TestAssertions.assertError(errorResponse, "Not Found", "The requested resource or file was not found");
    }
    
    @Test
    public void testReadFileWithAbsolutePath() throws Exception {
        // Test with absolute path
        Path absFile = tempDir.resolve("abs_flat.txt").toAbsolutePath();
        Files.writeString(absFile, TestFixtures.VALID_FLAT_TEXT);
        
        SampleModel result = given()
            .queryParam("path", absFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createValidSampleModel();
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadFileWithRelativePath() throws Exception {
        // Test with relative path
        Path relFile = Paths.get("test_relative.txt");
        Files.writeString(relFile, TestFixtures.VALID_FLAT_TEXT);
        
        try {
            SampleModel result = given()
                .queryParam("path", relFile.toString())
            .when()
                .get(FILE_PATH)
            .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .extract().response()
            .getBody().as(SampleModel.class);

            SampleModel expected = TestFixtures.createValidSampleModel();
            assertEquals(expected, result);
        } finally {
            // Clean up the file created in current directory
            Files.deleteIfExists(relFile);
        }
    }
    
    @Test
    public void testReadFileWithSpecialCharactersInPath() throws Exception {
        // Test with special characters in filename
        Path specialPathFile = tempDir.resolve("test-file_with.special_chars.txt");
        Files.writeString(specialPathFile, TestFixtures.VALID_FLAT_TEXT);
        
        SampleModel result = given()
            .queryParam("path", specialPathFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createValidSampleModel();
        assertEquals(expected, result);
    }
    
    @Test
    public void testReadFileWithLongPath() throws Exception {
        // Test with a very long path
        StringBuilder longPath = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            longPath.append("very_long_directory_name_").append(i).append("/");
        }
        longPath.append("long_filename.txt");
        
        Path longPathFile = tempDir.resolve(longPath.toString());
        Files.createDirectories(longPathFile.getParent());
        Files.writeString(longPathFile, TestFixtures.VALID_FLAT_TEXT);
        
        SampleModel result = given()
            .queryParam("path", longPathFile.toString())
        .when()
            .get(FILE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createValidSampleModel();
        assertEquals(expected, result);
    }
}
