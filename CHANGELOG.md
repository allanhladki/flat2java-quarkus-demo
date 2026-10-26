# Changelog

All notable changes to this project are documented in this file. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the project uses
[Semantic Versioning](https://semver.org/). Versions follow the flat2java version they demonstrate.

## [0.1.0] - Unreleased

Initial release. Requires Java 21. Built on flat2java 0.1.0 and Quarkus 3.40.

### Added

**Application**

- `SampleModel`, a 129-character record with `String`, `Integer`, `Long`, `BigDecimal`,
  `BigInteger`, `LocalDate` and `LocalDateTime` fields mapped with `@FlatField`.
- `POST /serialize` reads a record from a `text/plain` body and returns it as JSON.
- `POST /deserialize` writes a JSON `SampleModel` as a record.
- `GET /file` reads a record from a file on the server.
- JSON error responses with the error type, message, request ID and timestamp. flat2java errors
  are returned as `400`.
- A request ID in every log line of a request, and UTF-8 as the default charset of `text/plain`
  requests.
- OpenAPI document at `/swagger`, Swagger UI in dev mode, and SmallRye Health checks at `/health`.

**Build and tests**

- `@QuarkusTest` tests with REST Assured for every endpoint, including accented and boundary
  values.
- Maven Wrapper, Spotless formatting check (google-java-format, AOSP style) and JaCoCo coverage
  report.
- Dockerfiles for the JVM and native images.
- CI that runs the tests and builds and starts the JVM image on every pull request.
- Release workflow that creates a signed tag and a GitHub release from a pull request labeled
  `release`.

[0.1.0]: https://github.com/allanhladki/flat2java-quarkus-demo/releases/tag/v0.1.0
