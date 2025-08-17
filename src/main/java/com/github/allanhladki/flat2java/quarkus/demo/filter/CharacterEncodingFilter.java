package com.github.allanhladki.flat2java.quarkus.demo.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter that ensures proper UTF-8 character encoding for all requests and responses.
 * This is particularly important for handling special characters in flat file content.
 */
@Provider
@Priority(Priorities.ENTITY_CODER + 1)
public class CharacterEncodingFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CharacterEncodingFilter.class);
    private static final String UTF8_CHARSET = "UTF-8";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Ensure request has proper encoding
        String contentType = requestContext.getHeaderString("Content-Type");
        if (contentType != null && contentType.contains("text/plain") && !contentType.contains("charset")) {
            logger.debug("Adding UTF-8 charset to text/plain request");
            requestContext.getHeaders().putSingle("Content-Type", contentType + "; charset=" + UTF8_CHARSET);
        }
    }
}
