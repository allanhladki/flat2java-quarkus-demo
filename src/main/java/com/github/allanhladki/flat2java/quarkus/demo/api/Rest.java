package com.github.allanhladki.flat2java.quarkus.demo.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    // TODO: Serialization logic
    return Response.ok().build();
  }

  @POST
  @Path("/deserialize")
  public Response deserialize(String json) {
    // TODO: Deserialization logic
    return Response.ok().build();
  }

  @GET
  @Path("/file")
  public Response getFile() {
    // TODO: File reading logic
    return Response.ok().build();
  }
}
