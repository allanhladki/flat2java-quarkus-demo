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
    public Response serialize(String sampleModel) {
        logger.info("Received serialization request: {}", sampleModel);
        flatFieldService.toPojo(sampleModel, SampleModel.class);
        return Response.ok().build();
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
    public Response deserialize(SampleModel sampleModel) {
        logger.info("Received deserialization request: {}", sampleModel);
        flatFieldService.fromPojo(sampleModel);
        return Response.ok().build();
    }

    @GET
    @Path("/file")
    @Operation(
        summary = "Get flat file content",
        description = "Retrieves the content of a flat file"
    )
    @APIResponse(
        responseCode = "200",
        description = "Successfully retrieved the file content",
        content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Response getFile() {
        logger.info("Received file reading request");
        // TODO: File reading logic
        return Response.ok().build();
    }
}
