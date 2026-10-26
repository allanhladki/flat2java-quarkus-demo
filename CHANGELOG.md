# Changelog

All notable changes to this project are documented in this file. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the project uses
[Semantic Versioning](https://semver.org/). Versions follow the flat2java version they demonstrate.

## [0.1.0] - 2026-10-26

Initial release. Requires Java 21. Built on flat2java 0.1.0 and Quarkus 3.40 (LTS).

### Added

**Application**

- `POST /read` reads a fixed-width record from a `text/plain` body and returns it as JSON.
- `POST /write` writes a JSON object as a record.
- `GET /file` reads a record from a UTF-8 file on the server. One trailing line terminator is
  ignored, and a file larger than one record is rejected before it is read.
- `SampleModel`, a 315-column record that uses every kind of field flat2java supports: a
  superclass, `String` with a custom padding character, `int`, `Integer`, `Long`, `BigInteger`,
  `BigDecimal` with implied decimals, `LocalDate`, `LocalDateTime`, a nested Java record, a nested
  class with explicit positions, an array, lists of nested records and of text, and a field
  without `@FlatField`.
- JSON error responses with the error type, a message without field values, the request ID and a
  timestamp. Record and object errors are `400`, an invalid mapping or an unexpected exception is
  `500` with a generic message.
- A request ID in every log line of a request, in the `X-Request-Id` response header and in error
  responses. Only lengths are logged, never the contents of a record.
- A 64 KB limit for request bodies, and `flat2java.fail-on-string-truncation=true`: a text longer
  than its column is a `400` instead of being cut.
- OpenAPI document at `/swagger` and Swagger UI at `/swagger-ui`, also in the packaged
  application. Each endpoint has named examples that work as they are, and two that fail on
  purpose. Example records are in `samples/`.
- SmallRye Health checks at `/health`.
- `@RegisterForReflection` on the mapped classes, for native executables.

**Build and tests**

- `@QuarkusTest` tests with REST Assured. They check every example of the Swagger UI in both
  directions, each kind of field, and every error response.
- Mapping errors reported when the code compiles, with the `flat2java-processor` annotation
  processor.
- Maven Wrapper, Spotless formatting check (google-java-format, AOSP style) and a JaCoCo coverage
  report.
- Dockerfiles for the JVM and native images. The JVM image includes `samples/`.
- CI that runs the tests and builds and starts the JVM image on every pull request.
- Release workflow that creates a signed tag and a GitHub release from a pull request labeled
  `release`.

[0.1.0]: https://github.com/allanhladki/flat2java-quarkus-demo/releases/tag/v0.1.0
