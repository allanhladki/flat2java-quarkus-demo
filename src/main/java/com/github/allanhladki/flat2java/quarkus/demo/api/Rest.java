package com.github.allanhladki.flat2java.quarkus.demo.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Rest {

    @POST
    @Path("/serialize")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response serialize(String plainText) {
        // TODO: Implementar a lógica de serialização
        return Response.ok().build();
    }

    @POST
    @Path("/deserialize")
    public Response deserialize(String json) {
        // TODO: Implementar a lógica de desserialização
        return Response.ok().build();
    }

    @GET
    @Path("/file")
    public Response getFile() {
        // TODO: Implementar a lógica de leitura do arquivo
        return Response.ok().build();
    }
} 