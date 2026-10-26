package io.github.allanhladki.flat2java.quarkus.demo.info;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.lang.management.ManagementFactory;
import java.util.List;

/** Logs the versions and the addresses of the application once it has started. */
@ApplicationScoped
public class StartupReport {

    private final int port;

    StartupReport(@ConfigProperty(name = "quarkus.http.port") int port) {
        this.port = port;
    }

    void onStart(@Observes StartupEvent event) {
        double seconds =
                (System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime())
                        / 1000.0;
        lines(Versions.current(), "http://localhost:" + port, seconds).forEach(Log::info);
    }

    /**
     * The lines of the report.
     *
     * @param versions the versions to show
     * @param base the address of the application, without a trailing slash
     * @param seconds the time since the JVM started
     * @return the lines
     */
    static List<String> lines(Versions versions, String base, double seconds) {
        return List.of(
                "flat2java Quarkus demo "
                        + versions.application()
                        + "  |  flat2java "
                        + versions.flat2java()
                        + "  |  Quarkus "
                        + versions.quarkus()
                        + "  |  Java "
                        + versions.java(),
                String.format("Ready in %.1f s", seconds),
                "  Swagger UI  " + base + "/swagger-ui",
                "  OpenAPI     " + base + "/swagger",
                "  Health      " + base + "/health",
                "  Try it      curl -X POST " + base + "/read --data-binary @samples/typical.txt");
    }
}
