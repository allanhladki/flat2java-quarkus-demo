package io.github.allanhladki.flat2java.quarkus.demo.api;

import io.github.allanhladki.flat2java.quarkus.demo.model.ErrorResponse;
import io.github.allanhladki.flat2java.quarkus.demo.model.SampleModel;
import io.github.allanhladki.flat2java.quarkus.demo.service.FlatFileService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/")
@Tag(name = "Records", description = "Convert fixed-width records to JSON and back with flat2java.")
public class FlatFileResource {

    private static final String UTF_8_TEXT = MediaType.TEXT_PLAIN + ";charset=UTF-8";

    private final FlatFileService service;

    FlatFileResource(FlatFileService service) {
        this.service = service;
    }

    @POST
    @Path("/read")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Read a record",
            description = "Converts one fixed-width record of 315 characters into a SampleModel.")
    @RequestBody(
            required = true,
            content =
                    @Content(
                            mediaType = MediaType.TEXT_PLAIN,
                            examples = {
                                @ExampleObject(
                                        name = "typical",
                                        summary = "A typical record",
                                        value = ApiExamples.TYPICAL_RECORD),
                                @ExampleObject(
                                        name = "accents",
                                        summary = "Accented characters",
                                        value = ApiExamples.ACCENTS_RECORD),
                                @ExampleObject(
                                        name = "boundary",
                                        summary = "Every column filled with its largest value",
                                        value = ApiExamples.BOUNDARY_RECORD),
                                @ExampleObject(
                                        name = "invalid-length",
                                        summary = "Fails with 400: 5 characters instead of 315",
                                        value = ApiExamples.SHORT_RECORD)
                            }))
    @APIResponse(responseCode = "200", description = "The record, as an object")
    @APIResponse(
            responseCode = "400",
            description = "The record is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(
            responseCode = "415",
            description = "The request is not text/plain",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public SampleModel read(String line) {
        return service.read(line);
    }

    @POST
    @Path("/write")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(UTF_8_TEXT)
    @Operation(
            summary = "Write a record",
            description = "Converts a SampleModel into one fixed-width record of 315 characters.")
    @RequestBody(
            required = true,
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = {
                                @ExampleObject(
                                        name = "typical",
                                        summary = "A typical object",
                                        value = ApiExamples.TYPICAL_JSON),
                                @ExampleObject(
                                        name = "accents",
                                        summary = "Accented characters",
                                        value = ApiExamples.ACCENTS_JSON),
                                @ExampleObject(
                                        name = "boundary",
                                        summary = "Every column filled with its largest value",
                                        value = ApiExamples.BOUNDARY_JSON),
                                @ExampleObject(
                                        name = "negative-number",
                                        summary = "Fails with 400: numbers are unsigned",
                                        value = ApiExamples.NEGATIVE_NUMBER_JSON)
                            }))
    @APIResponse(
            responseCode = "200",
            description = "The record",
            content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(
            responseCode = "400",
            description = "The object cannot be written",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public String write(SampleModel model) {
        return service.write(model);
    }

    @GET
    @Path("/file")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Read a record from a file",
            description =
                    "Reads the UTF-8 file at the given path on the server as one record. The"
                        + " relative paths of the examples are relative to the directory where the"
                        + " application was started, which is the repository root when it runs from"
                        + " a clone.")
    @APIResponse(responseCode = "200", description = "The record, as an object")
    @APIResponse(
            responseCode = "400",
            description = "The path is missing or invalid, or the record is invalid",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(
            responseCode = "404",
            description = "There is no file at the path",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public SampleModel file(
            @Parameter(
                            description = "Path of the file to read",
                            required = true,
                            example = ApiExamples.TYPICAL_PATH,
                            examples = {
                                @ExampleObject(name = "typical", value = ApiExamples.TYPICAL_PATH),
                                @ExampleObject(name = "accents", value = ApiExamples.ACCENTS_PATH),
                                @ExampleObject(
                                        name = "boundary",
                                        value = ApiExamples.BOUNDARY_PATH),
                                @ExampleObject(
                                        name = "missing",
                                        summary = "Fails with 404",
                                        value = "samples/missing.txt")
                            })
                    @QueryParam("path")
                    String path) {
        return service.readFile(path);
    }
}
