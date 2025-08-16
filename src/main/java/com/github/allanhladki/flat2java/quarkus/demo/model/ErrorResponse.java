package com.github.allanhladki.flat2java.quarkus.demo.model;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * Standard error response model for API error responses.
 * Contains error details and request tracing information.
 */
@Data
public class ErrorResponse {
    
    private final UUID id;
    private final String error;
    private final String message;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant timestamp;
    
    public ErrorResponse(String error, String message, UUID id) {
        this.id = id;
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now();
    }

}

