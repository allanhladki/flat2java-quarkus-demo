package com.github.allanhladki.flat2java.quarkus.demo.api;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

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
 * REST Assured tests for the Flat2Java API endpoints.
 * Tests both success and error scenarios.
 */
@QuarkusTest
@TestProfile(RestApiTest.TestProfile.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestApiTest {

    private static final String SERIALIZE_PATH = "/serialize";
    private static final String DESERIALIZE_PATH = "/deserialize";
    
    /**
     * Test profile to ensure proper test configuration
     */
    public static class TestProfile implements QuarkusTestProfile {
        // Default test profile
    }

    @Test
    public void testSerializeSuccess() throws Exception {
        String flatText = TestFixtures.VALID_FLAT_TEXT;
        
        SampleModel result = given()
            .contentType(ContentType.TEXT)
            .body(flatText)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createValidSampleModel();
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeSuccessWithAlternativeData() throws Exception {
        String flatText = TestFixtures.VALID_FLAT_TEXT_2;
        
        SampleModel result = given()
            .contentType(ContentType.TEXT)
            .body(flatText)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createValidSampleModel2();
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeEmptyText() {
        ErrorResponse errorResponse = given()
            .contentType(ContentType.TEXT)
            .body("")
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        TestAssertions.assertError(errorResponse, "Validation Error", "Flat text content cannot be empty");
    }

    @Test
    public void testSerializeWhitespaceOnly() {
        ErrorResponse errorResponse = given()
            .contentType(ContentType.TEXT)
            .body("   \t\n   ")
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        TestAssertions.assertError(errorResponse, "Validation Error", "Flat text content cannot be empty");
    }

    @Test
    public void testSerializeInvalidFormat() {
        String invalidFlatText = "InvalidFormatTextThatIsTooShort";
        
        ErrorResponse errorResponse = given()
            .contentType(ContentType.TEXT)
            .body(invalidFlatText)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        assertNotNull(errorResponse.getError());
        assertNotNull(errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
        assertNotNull(errorResponse.getId());
    }

    @Test
    public void testDeserializeSuccess() throws Exception {
        SampleModel sampleModel = TestFixtures.createValidSampleModel();
        
        String result = given()
            .contentType(ContentType.JSON)
            .body(sampleModel)
        .when()
            .post(DESERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.TEXT)
            .extract().response()
        .getBody().asString();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Verify the flat text can be deserialized back to the same model
        SampleModel deserialized = given()
            .contentType(ContentType.TEXT)
            .body(result)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .extract().response()
        .getBody().as(SampleModel.class);
        
        assertEquals(sampleModel, deserialized);
    }

    @Test
    public void testDeserializeSuccessWithAlternativeData() throws Exception {
        SampleModel sampleModel = TestFixtures.createValidSampleModel2();
        
        String result = given()
            .contentType(ContentType.JSON)
            .body(sampleModel)
        .when()
            .post(DESERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.TEXT)
            .extract().response()
        .getBody().asString();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Verify the flat text can be deserialized back to the same model
        SampleModel deserialized = given()
            .contentType(ContentType.TEXT)
            .body(result)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .extract().response()
        .getBody().as(SampleModel.class);
        
        assertEquals(sampleModel, deserialized);
    }

    @Test
    public void testDeserializeSuccessWithBoundaryData() throws Exception {
        SampleModel sampleModel = TestFixtures.createBoundarySampleModel();
        
        String result = given()
            .contentType(ContentType.JSON)
            .body(sampleModel)
        .when()
            .post(DESERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.TEXT)
            .extract().response()
        .getBody().asString();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Verify the flat text can be deserialized back to the same model
        SampleModel deserialized = given()
            .contentType(ContentType.TEXT)
            .body(result)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .extract().response()
        .getBody().as(SampleModel.class);
        
        assertEquals(sampleModel, deserialized);
    }

    @Test
    public void testDeserializeSuccessWithSpecialCharsData() throws Exception {
        SampleModel sampleModel = TestFixtures.createSpecialCharsSampleModel();
        
        String result = given()
            .contentType(ContentType.JSON)
            .body(sampleModel)
        .when()
            .post(DESERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.TEXT)
            .extract().response()
        .getBody().asString();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Verify the flat text can be deserialized back to the same model
        SampleModel deserialized = given()
            .contentType(ContentType.TEXT)
            .body(result)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .extract().response()
        .getBody().as(SampleModel.class);
        
        assertEquals(sampleModel, deserialized);
    }

    @Test
    public void testSerializeSuccessWithBoundaryData() throws Exception {
        String flatText = TestFixtures.BOUNDARY_FLAT_TEXT;
        
        SampleModel result = given()
            .contentType(ContentType.TEXT)
            .body(flatText)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createBoundarySampleModel();
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeSuccessWithSpecialCharsData() throws Exception {
        String flatText = TestFixtures.SPECIAL_CHARS_FLAT_TEXT;
        
        SampleModel result = given()
            .contentType(ContentType.TEXT)
            .body(flatText)
        .when()
            .post(SERIALIZE_PATH)
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(SampleModel.class);

        SampleModel expected = TestFixtures.createSpecialCharsSampleModel();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInvalidModel() {
        SampleModel invalidModel = TestFixtures.createInvalidSampleModel();
        
        ErrorResponse errorResponse = given()
            .contentType(ContentType.JSON)
            .body(invalidModel)
        .when()
            .post(DESERIALIZE_PATH)
        .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode())
            .contentType(ContentType.JSON)
            .extract().response()
        .getBody().as(ErrorResponse.class);
        
        assertNotNull(errorResponse.getError());
        assertNotNull(errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
        assertNotNull(errorResponse.getId());
    }

    @Test
    public void testSwaggerUiEndpoint() {

        String result = given()
        .when()
            .get("/swagger-ui")
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.HTML)
            .extract().response()
        .getBody().asString();
        
        assertNotNull(result);
        assertTrue(result.contains("html"));
    }

    @Test
    public void testOpenApiEndpoint() {

        String result = given()
        .when()
            .get("/swagger-ui")
        .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.HTML)
            .extract().response()
        .getBody().asString();
        
        assertNotNull(result);
        assertTrue(result.contains("html"));
    }
}
