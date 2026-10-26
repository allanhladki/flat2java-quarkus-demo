package io.github.allanhladki.flat2java.quarkus.demo.filter;

import io.quarkus.logging.Log;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;

import org.jboss.logging.MDC;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import java.util.UUID;

/**
 * Gives every request an ID. The ID is written in each log line (the {@code requestId} MDC key),
 * returned in the {@code X-Request-Id} response header and in error responses.
 */
public class RequestIdFilter {

    /** MDC key and request property that hold the request ID. */
    public static final String REQUEST_ID = "requestId";

    /** Response header that carries the request ID. */
    public static final String HEADER = "X-Request-Id";

    /**
     * Runs before the resource is matched, so that requests for unknown paths get an ID too.
     *
     * @param request the request
     */
    @ServerRequestFilter(preMatching = true)
    public void assign(ContainerRequestContext request) {
        String id = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, id);
        request.setProperty(REQUEST_ID, id);
        Log.debugf("%s %s", request.getMethod(), request.getUriInfo().getPath());
    }

    /**
     * Adds the header and clears the MDC.
     *
     * @param request the request
     * @param response the response
     */
    @ServerResponseFilter
    public void publish(ContainerRequestContext request, ContainerResponseContext response) {
        Object id = request.getProperty(REQUEST_ID);
        if (id != null) {
            response.getHeaders().putSingle(HEADER, id);
        }
        MDC.remove(REQUEST_ID);
    }

    /**
     * The ID of the current request.
     *
     * @return the ID, or {@code null} outside a request
     */
    public static String current() {
        Object id = MDC.get(REQUEST_ID);
        return id == null ? null : id.toString();
    }
}
