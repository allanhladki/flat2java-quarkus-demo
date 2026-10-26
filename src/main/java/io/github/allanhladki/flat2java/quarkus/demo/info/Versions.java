package io.github.allanhladki.flat2java.quarkus.demo.info;

import org.eclipse.microprofile.config.ConfigProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The versions that the startup report and {@code /info} show. The flat2java and Quarkus versions
 * come from the {@code pom.properties} of their jars.
 *
 * @param application the version of this application
 * @param flat2java the version of flat2java
 * @param quarkus the version of Quarkus
 * @param java the version of Java
 */
public record Versions(String application, String flat2java, String quarkus, String java) {

    private static final String FLAT2JAVA_POM =
            "/META-INF/maven/io.github.allanhladki.flat2java/flat2java-core/pom.properties";
    private static final String QUARKUS_POM =
            "/META-INF/maven/io.quarkus/quarkus-core/pom.properties";

    /**
     * The versions of the running application.
     *
     * @return the versions
     */
    public static Versions current() {
        return new Versions(
                ConfigProvider.getConfig()
                        .getOptionalValue("quarkus.application.version", String.class)
                        .orElse("unknown"),
                pomVersion(FLAT2JAVA_POM),
                pomVersion(QUARKUS_POM),
                System.getProperty("java.version"));
    }

    private static String pomVersion(String resource) {
        Properties properties = new Properties();
        try (InputStream in = Versions.class.getResourceAsStream(resource)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            // The version shows as unknown.
        }
        return properties.getProperty("version", "unknown");
    }
}
