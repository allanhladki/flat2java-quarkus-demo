package io.github.allanhladki.flat2java.quarkus.demo.info;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;

import org.junit.jupiter.api.Test;

import java.util.List;

class StartupReportTest {

    private static final Versions VERSIONS = new Versions("1.2.3", "0.1.0", "3.40.1.1", "21.0.7");

    @Test
    void showsTheVersionsOnTheFirstLine() {
        String line = StartupReport.lines(VERSIONS, "http://localhost:8080", 1.234).get(0);

        assertThat(line, containsString("1.2.3"));
        assertThat(line, containsString("flat2java 0.1.0"));
        assertThat(line, containsString("Quarkus 3.40.1.1"));
        assertThat(line, containsString("Java 21.0.7"));
    }

    @Test
    void listsTheAddressesOfTheApplication() {
        List<String> lines = StartupReport.lines(VERSIONS, "http://localhost:9090", 1.234);

        assertThat(lines, hasItem("Ready in 1.2 s"));
        assertThat(lines, hasItem(endsWith("http://localhost:9090/swagger-ui")));
        assertThat(lines, hasItem(endsWith("http://localhost:9090/swagger")));
        assertThat(lines, hasItem(endsWith("http://localhost:9090/health")));
        assertThat(lines, hasItem(containsString("curl -X POST http://localhost:9090/read")));
    }
}
