package com.github.allanhladki.flat2java.quarkus.demo.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.allanhladki.flat2java.exception.DeserializationException;
import com.github.allanhladki.flat2java.exception.Flat2JavaException;
import com.github.allanhladki.flat2java.exception.SerializationException;
import com.github.allanhladki.flat2java.exception.ValidationException;
import com.github.allanhladki.flat2java.quarkus.demo.filter.RequestScopeId;
import com.github.allanhladki.flat2java.quarkus.demo.model.ErrorResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception mapper that handles all exceptions and provides consistent error responses.
 * Maps flat2java specific exceptions to appropriate HTTP status codes.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Inject
    RequestScopeId requestScopeId;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {

        // This is just to make sure the requestScopeId is initialized
        requestScopeId.getUuid();

        logger.error("Unhandled exception occurred: {}", exception.getMessage(), exception);
        
        // Handle Jakarta REST exceptions first
        if (exception instanceof NotSupportedException) {
            return createErrorResponse(Response.Status.BAD_REQUEST, "Validation Error", "The content-type header value did not match the value in @Consumes");
        }
        
        if (exception instanceof NotAllowedException) {
            return createErrorResponse(Response.Status.METHOD_NOT_ALLOWED, "Method Not Allowed", "HTTP method not allowed for this endpoint");
        }
        
        if (exception instanceof NotFoundException) {
            return createErrorResponse(Response.Status.NOT_FOUND, "Not Found", "The requested resource was not found");
        }
        
        // Handle Flat2Java specific exceptions
        if (exception instanceof ValidationException) {
            return createErrorResponse(Response.Status.BAD_REQUEST, "Validation Error", exception.getMessage());
        }
        
        if (exception instanceof DeserializationException) {
            return createErrorResponse(Response.Status.BAD_REQUEST, "Deserialization Error", exception.getMessage());
        }
        
        if (exception instanceof SerializationException) {
            return createErrorResponse(Response.Status.BAD_REQUEST, "Serialization Error", exception.getMessage());
        }
        
        if (exception instanceof Flat2JavaException) {
            return createErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, "Flat2Java Error", exception.getMessage());
        }
        
        if (exception instanceof IllegalArgumentException) {
            return createErrorResponse(Response.Status.BAD_REQUEST, "Invalid Input", exception.getMessage());
        }
        
        // Default case for any other exception
        return createErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred");
    }
    
    
    private Response createErrorResponse(Response.Status status, String errorType, String message) {
        ErrorResponse errorResponse = new ErrorResponse(errorType, message, requestScopeId.getUuid());
        return Response.status(status)
            .entity(errorResponse)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
