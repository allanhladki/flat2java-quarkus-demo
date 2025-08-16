package com.github.allanhladki.flat2java.quarkus.demo.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
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
@Tag(name = "Flat2Java API", description = "API for handling flat file serialization and deserialization. Use /swagger-ui for interactive API documentation.")
public class Rest {

    private static final Logger logger = LoggerFactory.getLogger(Rest.class);

    @Inject
    FlatFieldService flatFieldService;

    @POST
    @Path("/serialize")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
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
        logger.info("Received serialization request for flat text: {}", flatText);
        SampleModel result = flatFieldService.toPojo(flatText, SampleModel.class);
        return Response.ok(result).build();
    }

    @POST
    @Path("/deserialize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
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
        logger.info("Received deserialization request for: {}", sampleModel);
        String flatText = flatFieldService.fromPojo(sampleModel);
        return Response.ok(flatText).type(MediaType.TEXT_PLAIN).build();
    }
}
