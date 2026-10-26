package io.github.allanhladki.flat2java.quarkus.demo.api;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

@QuarkusTest
class HomePageTest {

    @Test
    void servesTheHomePageAtTheRoot() {
        given().get("/")
                .then()
                .statusCode(200)
                .contentType(containsString("html"))
                .body(containsString("flat2java Quarkus demo"))
                .body(containsString("/swagger-ui"))
                .body(containsString("/swagger"))
                .body(containsString("/health"))
                .body(containsString("/info"));
    }

    @Test
    void startsThePlaygroundWithTheTypicalRecord() {
        given().get("/").then().statusCode(200).body(containsString(ApiExamples.TYPICAL_RECORD));
    }

    @Test
    void usesNoExternalResources() {
        given().get("/")
                .then()
                .body(not(containsString("https://cdn")))
                .body(not(containsString("src=\"http")));
    }

    @Test
    void reportsTheVersionsThatThePageShows() {
        given().get("/info")
                .then()
                .statusCode(200)
                .body("versions.application", notNullValue())
                .body("versions.flat2java", equalTo("0.1.0"))
                .body("versions.quarkus", notNullValue())
                .body("versions.java", notNullValue());
    }
}
