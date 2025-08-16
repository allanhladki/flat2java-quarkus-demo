package com.github.allanhladki.flat2java.quarkus.demo.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Request filter that generates a unique identifier for each request and stores it in MDC
 * for logging purposes. This allows tracing requests across the entire application.
 */
@Provider
@Priority(Priorities.ENTITY_CODER)
public class RequestTracingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    RequestScopeId requestScopeId;
    
    private static final Logger logger = LoggerFactory.getLogger(RequestTracingFilter.class);
    public static final String REQUEST_ID = "requestId";
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        // Store in MDC for logging
        MDC.put(REQUEST_ID, requestScopeId.toString());
        
        // Store in request context for potential use in other parts of the application
        requestContext.setProperty(REQUEST_ID, requestScopeId);
        
        // Log the start of the request
        logger.info("Request started - ID: {}", requestScopeId.toString());
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // Clean up MDC after response is sent
        String requestId = MDC.get(REQUEST_ID);
        if (requestId != null) {
            logger.info("Request completed - ID: {}", requestId);
            MDC.remove(REQUEST_ID);
        }
    }
}
