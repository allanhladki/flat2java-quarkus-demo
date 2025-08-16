package com.github.allanhladki.flat2java.quarkus.demo.filter;

import java.util.UUID;

import org.jboss.logging.MDC;

import jakarta.enterprise.context.RequestScoped;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request-scoped identifier for tracing requests across the application.
 * This class holds a UUID that is generated for each incoming request.
 */ 
@RequestScoped
@Data
@AllArgsConstructor
public class RequestScopeId {
    
    private final UUID uuid;

    public RequestScopeId() {
        this.uuid = UUID.randomUUID();
        
        // Store in MDC for logging
        MDC.put(RequestTracingFilter.REQUEST_ID, this.uuid.toString());
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
    
}
