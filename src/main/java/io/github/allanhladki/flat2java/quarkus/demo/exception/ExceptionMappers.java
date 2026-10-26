package io.github.allanhladki.flat2java.quarkus.demo.exception;

import io.github.allanhladki.flat2java.exception.DeserializationException;
import io.github.allanhladki.flat2java.exception.Flat2JavaException;
import io.github.allanhladki.flat2java.exception.SerializationException;
import io.github.allanhladki.flat2java.quarkus.demo.filter.RequestIdFilter;
import io.github.allanhladki.flat2java.quarkus.demo.model.ErrorResponse;
import io.quarkus.logging.Log;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.Instant;

/**
 * Turns exceptions into {@link ErrorResponse} bodies. The most specific mapper applies. Client
 * errors are logged as warnings and unexpected ones as errors; the response of an unexpected error
 * never carries the exception message.
 */
public class ExceptionMappers {

    /**
     * The record is invalid.
     *
     * @param e the exception
     * @return {@code 400}
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> deserialization(DeserializationException e) {
        return clientError(Status.BAD_REQUEST.getStatusCode(), "Deserialization Error", e);
    }

    /**
     * The object cannot be written, for example a number wider than its column.
     *
     * @param e the exception
     * @return {@code 400}
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> serialization(SerializationException e) {
        return clientError(Status.BAD_REQUEST.getStatusCode(), "Serialization Error", e);
    }

    /**
     * An error of the application, such as an invalid {@code @FlatField} mapping.
     *
     * @param e the exception
     * @return {@code 500}
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> flat2Java(Flat2JavaException e) {
        return serverError("Flat2Java Error", e);
    }

    /**
     * An error with its own status, such as {@code 404} or {@code 415}.
     *
     * @param e the exception
     * @return the status of the exception
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> webApplication(WebApplicationException e) {
        Response response = e.getResponse();
        if (response.getStatus() >= 500) {
            return serverError("Internal Server Error", e);
        }
        return clientError(response.getStatus(), response.getStatusInfo().getReasonPhrase(), e);
    }

    /**
     * Anything else.
     *
     * @param e the exception
     * @return {@code 500}
     */
    @ServerExceptionMapper
    public RestResponse<ErrorResponse> unexpected(Exception e) {
        return serverError("Internal Server Error", e);
    }

    private static RestResponse<ErrorResponse> clientError(int status, String error, Exception e) {
        Log.warnf("%s: %s", error, e.getMessage());
        return response(status, error, e.getMessage());
    }

    private static RestResponse<ErrorResponse> serverError(String error, Exception e) {
        Log.error("Request failed", e);
        return response(
                Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                error,
                "An unexpected error occurred");
    }

    private static RestResponse<ErrorResponse> response(int status, String error, String message) {
        ErrorResponse body =
                new ErrorResponse(error, message, RequestIdFilter.current(), Instant.now());
        return RestResponse.ResponseBuilder.<ErrorResponse>create(status)
                .entity(body)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
