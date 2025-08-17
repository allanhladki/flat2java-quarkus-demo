package com.github.allanhladki.flat2java.quarkus.demo.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.allanhladki.flat2java.quarkus.demo.model.SampleModel;
import com.github.allanhladki.flat2java.quarkus.demo.service.FlatFieldService;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/")
@Tag(name = "Flat2Java API", description = "API for handling flat file serialization and deserialization. Use /swagger-ui for interactive API documentation.")
public class Rest {

    private static final Logger logger = LoggerFactory.getLogger(Rest.class);

    @Inject
    FlatFieldService flatFieldService;

    @POST
    @Path("/serialize")
    @Consumes(MediaType.TEXT_PLAIN + "; charset=UTF-8")
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
    @Produces(MediaType.TEXT_PLAIN + "; charset=UTF-8")
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
        return Response.ok(flatText).build();
    }

    @GET
    @Path("/file")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Read flat file from path and serialize to POJO",
        description = "Reads a text file from the specified file path and converts it to a SampleModel object"
    )
    @APIResponse(
        responseCode = "200",
        description = "Successfully read and serialized the file content",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = SampleModel.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Invalid file path or processing error"
    )
    @APIResponse(
        responseCode = "404",
        description = "File not found at the specified path"
    )
    @APIResponse(
        responseCode = "500",
        description = "Error reading file or internal server error"
    )
    public Response readFileAndSerialize(
        @Parameter(description = "Path to the text file to read", required = true)
        @QueryParam("path") String filePath
    ) {
        logger.info("Received file read request for path: {}", filePath);
        
        if (filePath == null || filePath.isBlank()) {
            logger.error("File path parameter is required");
            throw new IllegalArgumentException("File path parameter is required");
        }
        
        java.nio.file.Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            logger.error("File not found at path: {}", path);
            throw new NotFoundException("File not found at path: " + path);
        }
        
        if (!Files.isReadable(path)) {
            logger.error("File is not readable at path: {}", path);
            throw new IllegalArgumentException("File is not readable at path: " + path);
        }
        
        String fileContent;

        try {
            fileContent = Files.readString(path);
            logger.info("Successfully read file content, length: {}", fileContent.length());
            
        } catch (Exception e) {
            logger.error("Error reading file at path: {}", filePath, e);
            throw new IllegalArgumentException("Error reading file: " + e.getMessage());
        } 

        SampleModel result = flatFieldService.toPojo(fileContent, SampleModel.class);
        return Response.ok(result).build();
    }
}
