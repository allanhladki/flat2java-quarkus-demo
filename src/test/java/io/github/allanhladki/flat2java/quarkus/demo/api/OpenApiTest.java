package io.github.allanhladki.flat2java.quarkus.demo.api;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

@QuarkusTest
class OpenApiTest {

    @Test
    void servesTheOpenApiDocumentWithNamedExamples() {
        String document =
                given().get("/swagger")
                        .then()
                        .statusCode(200)
                        .contentType(containsString("yaml"))
                        .extract()
                        .asString();

        for (String expected :
                new String[] {
                    "/read:",
                    "/write:",
                    "/file:",
                    "typical:",
                    "accents:",
                    "boundary:",
                    "invalid-length:",
                    "negative-number:",
                    "samples/typical.txt",
                    "Error:"
                }) {
            org.junit.jupiter.api.Assertions.assertTrue(
                    document.contains(expected), "The document has no " + expected);
        }
    }

    @Test
    void servesTheSwaggerUi() {
        given().get("/swagger-ui")
                .then()
                .statusCode(200)
                .contentType(containsString("html"))
                .body(containsString("swagger"));
    }

    @Test
    void servesTheHealthChecks() {
        given().get("/health").then().statusCode(200).body("status", equalTo("UP"));
        given().get("/health/ready").then().statusCode(200);
        given().get("/health/live").then().statusCode(200);
    }
}
