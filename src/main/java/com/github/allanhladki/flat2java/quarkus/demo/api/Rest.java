package com.github.allanhladki.flat2java.quarkus.demo.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.allanhladki.flat2java.quarkus.demo.model.SampleModel;
import com.github.allanhladki.flat2java.quarkus.demo.service.FlatFieldService;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Flat2Java API", description = "API for handling flat file serialization and deserialization")
public class Rest {

    private static final Logger logger = LoggerFactory.getLogger(Rest.class);

    @Inject
    FlatFieldService flatFieldService;

    @POST
    @Path("/serialize")
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Serialize flat file content to POJO",
        description = "Converts a flat file string into a SampleModel object"
    )
    @APIResponse(
        responseCode = "200",
        description = "Successfully serialized the flat file content",
        content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
        responseCode = "400",
        description = "Invalid flat file format or processing error"
    )
    public Response serialize(String flatText) {
        try {
            logger.info("Received serialization request for flat text: {}", flatText);
            
            if (flatText == null || flatText.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Flat text content cannot be empty\"}")
                    .build();
            }
            
            SampleModel result = flatFieldService.toPojo(flatText, SampleModel.class);
            
            if (result != null) {
                return Response.ok(result).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to deserialize flat text to POJO\"}")
                    .build();
            }
            
        } catch (Exception e) {
            logger.error("Error during serialization: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Serialization failed: " + e.getMessage() + "\"}")
                .build();
        }
    }

    @POST
    @Path("/deserialize")
    @Operation(
        summary = "Deserialize POJO to flat file content",
        description = "Converts a SampleModel object into a flat file string"
    )
    @APIResponse(
        responseCode = "200",
        description = "Successfully deserialized the POJO",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    @APIResponse(
        responseCode = "400",
        description = "Invalid POJO or processing error"
    )
    public Response deserialize(SampleModel sampleModel) {
        try {
            logger.info("Received deserialization request for: {}", sampleModel);
            
            if (sampleModel == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"SampleModel cannot be null\"}")
                    .build();
            }
            
            String flatText = flatFieldService.fromPojo(sampleModel);
            
            if (flatText != null && !flatText.isEmpty()) {
                return Response.ok(flatText).type(MediaType.TEXT_PLAIN).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Failed to serialize POJO to flat text\"}")
                    .build();
            }
            
        } catch (Exception e) {
            logger.error("Error during deserialization: {}", e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"Deserialization failed: " + e.getMessage() + "\"}")
                .build();
        }
    }

    @GET
    @Path("/health")
    @Operation(
        summary = "Health check endpoint",
        description = "Simple health check to verify the service is running"
    )
    @APIResponse(
        responseCode = "200",
        description = "Service is healthy"
    )
    public Response health() {
        return Response.ok("{\"status\": \"healthy\", \"service\": \"Flat2Java Quarkus Demo\"}").build();
    }

    @GET
    @Path("/info")
    @Operation(
        summary = "Service information",
        description = "Get information about the Flat2Java service"
    )
    @APIResponse(
        responseCode = "200",
        description = "Service information retrieved successfully"
    )
    public Response info() {
        String info = "{\"service\": \"Flat2Java Quarkus Demo\", \"version\": \"0.0.1-rc1\", \"endpoints\": [\"/serialize\", \"/deserialize\", \"/health\", \"/info\"]}";
        return Response.ok(info).build();
    }
}
