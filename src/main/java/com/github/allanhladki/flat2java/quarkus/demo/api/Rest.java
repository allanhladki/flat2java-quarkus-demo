package com.github.allanhladki.flat2java.quarkus.demo.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Rest {

    private static final Logger LOG = LoggerFactory.getLogger(Rest.class);

    @POST
    @Path("/serialize")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response serialize(String plainText) {
        LOG.info("Received serialization request: {}", plainText);
        // TODO: Serialization logic
        return Response.ok().build();
    }

    @POST
    @Path("/deserialize")
    public Response deserialize(String json) {
        LOG.info("Received deserialization request: {}", json);
        // TODO: Deserialization logic
        return Response.ok().build();
    }

    @GET
    @Path("/file")
    public Response getFile() {
        LOG.info("Received file reading request");
        // TODO: File reading logic
        return Response.ok().build();
    }
}
