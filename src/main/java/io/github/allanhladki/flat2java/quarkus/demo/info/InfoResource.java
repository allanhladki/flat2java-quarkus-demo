package io.github.allanhladki.flat2java.quarkus.demo.info;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.Map;

/** The versions that the home page shows. */
@Path("/info")
public class InfoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(hidden = true)
    public Map<String, Versions> info() {
        return Map.of("versions", Versions.current());
    }
}
