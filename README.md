<div align="center">

# flat2java-quarkus-demo

**A Quarkus REST application that reads and writes fixed-width records with flat2java.**

[![CI](https://github.com/allanhladki/flat2java-quarkus-demo/actions/workflows/ci.yml/badge.svg)](https://github.com/allanhladki/flat2java-quarkus-demo/actions/workflows/ci.yml)
[![flat2java](https://img.shields.io/maven-central/v/io.github.allanhladki.flat2java/flat2java-quarkus?label=flat2java)](https://central.sonatype.com/namespace/io.github.allanhladki.flat2java)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Quarkus](https://img.shields.io/badge/Quarkus-3-4695EB?logo=quarkus&logoColor=white)
![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)

[Quick start](#quick-start) ·
[Endpoints](#endpoints) ·
[Record layout](#record-layout) ·
[Configuration](#configuration) ·
[Changelog](CHANGELOG.md)

</div>

---

This project shows [flat2java](https://github.com/allanhladki/flat2java) in a Quarkus application.
It exposes REST endpoints that turn a fixed-width text record into JSON and JSON back into a
record, using the `flat2java-quarkus` CDI integration, and covers them with `@QuarkusTest` tests.

```text
John Doe                      00000001000000000000123456789000000000000999900000000000987654321199001012024-01-01T12:00:00.000000
└─────────── name ───────────┘└quantity┘└─ transactionId ──┘└── amount ───┘└─ accountNumber ──┘└birth─┘└────── createdAt ───────┘
```

## Contents

- [Requirements](#requirements)
- [Quick start](#quick-start)
- [Endpoints](#endpoints)
- [Record layout](#record-layout)
- [Errors](#errors)
- [Configuration](#configuration)
- [Running in a container](#running-in-a-container)
- [Tests](#tests)
- [Project layout](#project-layout)
- [Contributing](#contributing)
- [License](#license)

## Requirements

| Tool | Version |
|---|---|
| JDK | 21 |
| Maven | Not required: the repository includes the Maven Wrapper (`./mvnw`) |
| Docker or Podman | Optional, to build and run the container image |

| Component | Version |
|---|---|
| Quarkus | 3.20.0 |
| flat2java (`flat2java-core`, `flat2java-processor`, `flat2java-quarkus`) | 0.0.1-rc2 |

## Quick start

```bash
git clone https://github.com/allanhladki/flat2java-quarkus-demo.git
cd flat2java-quarkus-demo
./mvnw quarkus:dev
```

The application listens on `http://localhost:8080`. In dev mode, the Swagger UI is available at
`http://localhost:8080/swagger-ui`.

Convert a record into JSON:

```bash
curl -X POST http://localhost:8080/serialize \
  -H 'Content-Type: text/plain; charset=UTF-8' \
  --data-binary 'John Doe                      0000000100000000000012345678900000000000099990000000000098765432119900101'"2024-01-01T12:00:00.000000"'
```

```json
{
  "uuid": "c9f68bc6-9bec-4bfe-9869-98d824184049",
  "name": "John Doe",
  "quantity": 100,
  "transactionId": 1234567890,
  "amount": 99.99,
  "accountNumber": 987654321,
  "birthDate": "1990-01-01",
  "createdAt": "2024-01-01T12:00:00"
}
```

`uuid` is generated for each object and is not part of the record.

## Endpoints

| Method and path | Request | Response | Purpose |
|---|---|---|---|
| `POST /serialize` | `text/plain; charset=UTF-8`, one record | `application/json` | Reads a record into a `SampleModel` |
| `POST /deserialize` | `application/json`, a `SampleModel` | `text/plain; charset=UTF-8` | Writes a `SampleModel` as a record |
| `GET /file?path=<path>` | Query parameter `path` | `application/json` | Reads the file at `path` as one record |
| `GET /swagger` | | OpenAPI document (YAML) | API description |
| `GET /swagger-ui` | | HTML | Swagger UI, dev and test modes only |
| `GET /health`, `/health/live`, `/health/ready` | | `application/json` | SmallRye Health checks |

<details>
<summary><code>POST /deserialize</code> example</summary>

```bash
curl -X POST http://localhost:8080/deserialize \
  -H 'Content-Type: application/json' \
  -d '{
        "name": "John Doe",
        "quantity": 100,
        "transactionId": 1234567890,
        "amount": 99.99,
        "accountNumber": 987654321,
        "birthDate": "1990-01-01",
        "createdAt": "2024-01-01T12:00:00"
      }'
```

```text
John Doe                      00000001000000000000123456789000000000000999900000000000987654321199001012024-01-01T12:00:00.000000
```

</details>

> [!WARNING]
> `GET /file` reads any file that the application process can read. It exists to demonstrate
> reading a record from disk. Do not expose this application on a network you do not control.

Every request receives a request ID. It is written to the log of each line handled during the
request and returned in error responses.

## Record layout

`SampleModel` maps a record of 129 characters:

| Field | Java type | Columns | Length | Format |
|---|---|---|---|---|
| `name` | `String` | 1-30 | 30 | Left-aligned, padded with spaces |
| `quantity` | `Integer` | 31-40 | 10 | Zero-filled on the left |
| `transactionId` | `Long` | 41-60 | 20 | Zero-filled on the left |
| `amount` | `BigDecimal` | 61-75 | 15 | Two implied decimal places: `000000000009999` is `99.99` |
| `accountNumber` | `BigInteger` | 76-95 | 20 | Zero-filled on the left |
| `birthDate` | `LocalDate` | 96-103 | 8 | `yyyyMMdd` |
| `createdAt` | `LocalDateTime` | 104-129 | 26 | `yyyy-MM-dd'T'HH:mm:ss.SSSSSS` |

Accented characters such as `José María` count as one column each. Requests and responses use
UTF-8.

## Errors

Errors are returned as JSON:

```json
{
  "error": "Deserialization Error",
  "message": "Error deserializing object: Input length (5) does not match expected total length (129) for class com.github.allanhladki.flat2java.quarkus.demo.model.SampleModel",
  "id": "056c94d3-8c88-40c2-881c-3b889175a904",
  "timestamp": "2026-10-26T04:17:44.720227765Z"
}
```

`id` is the request ID, so the error can be found in the log.

| Cause | Status | `error` |
|---|---|---|
| flat2java validation, read or write failure | 400 | `Validation Error`, `Deserialization Error`, `Serialization Error` |
| Other flat2java failure | 500 | `Flat2Java Error` |
| Missing, blank or unreadable `path` | 400 | `Invalid Input` |
| File or resource not found | 404 | `Not Found` |
| `Content-Type` not accepted by the endpoint | 400 | `Validation Error` |
| HTTP method not allowed | 405 | `Method Not Allowed` |
| Any other exception | 500 | `Internal Server Error` |

## Configuration

`src/main/resources/application.properties`:

| Property | Value | Purpose |
|---|---|---|
| `quarkus.http.port` | `8080` | HTTP port |
| `quarkus.http.host` | `0.0.0.0` | Listen on all interfaces |
| `quarkus.http.non-application-root-path` | `/` | Serves health and OpenAPI at the root instead of `/q` |
| `quarkus.smallrye-openapi.path` | `/swagger` | OpenAPI document path |
| `quarkus.log.category."com.github.allanhladki".level` | `TRACE` | Application log level |
| `quarkus.log.console.format` | `%d{...} [%X{requestId}] %-5p (%c{2.}): %s%e%n` | Adds the request ID to each log line |

Any property can be overridden with a system property or an environment variable, for example
`QUARKUS_HTTP_PORT=9090 ./mvnw quarkus:dev`. Tests use `src/test/resources/application.properties`,
which sets the test port to `8089`.

## Running in a container

The [Dockerfiles](src/main/docker) are the ones generated by Quarkus.

```bash
./mvnw package
docker build -f src/main/docker/Dockerfile.jvm -t flat2java-quarkus-demo:jvm .
docker run --rm -p 8080:8080 flat2java-quarkus-demo:jvm
```

<details>
<summary>Native executable</summary>

```bash
./mvnw package -Dnative
docker build -f src/main/docker/Dockerfile.native -t flat2java-quarkus-demo:native .
docker run --rm -p 8080:8080 flat2java-quarkus-demo:native
```

Building a native executable requires GraalVM or Mandrel, or a container runtime with
`-Dquarkus.native.container-build=true`.

</details>

> [!NOTE]
> The native build is not covered by CI. The JVM image is built and started on every pull request.

## Tests

```bash
./mvnw verify
```

| Command | What it does |
|---|---|
| `./mvnw verify` | Runs the tests, checks formatting, writes the coverage report to `target/jacoco-report` |
| `./mvnw spotless:apply` | Formats the code (google-java-format, AOSP style) |
| `./mvnw test -Dtest=RestApiTest` | Runs one test class |
| `./mvnw test -Dtest='RestApiTest#testSerializeSuccess'` | Runs one test method |

The tests start the application with `@QuarkusTest` and call it with REST Assured:

| Class | Covers |
|---|---|
| `RestApiTest` | `/serialize` and `/deserialize` with valid, boundary and accented values, and invalid input; the OpenAPI document and Swagger UI |
| `FileEndpointTest` | `/file` with absolute, relative, long and accented paths, and missing, blank and unknown paths |

## Project layout

```text
src/main/java/com/github/allanhladki/flat2java/quarkus/demo/
├── api/         REST resource
├── exception/   Exception mapper that builds the JSON error responses
├── filter/      Request ID and UTF-8 request filters
├── model/       SampleModel (the mapped record) and ErrorResponse
└── service/     Calls flat2java
src/main/docker/ Dockerfiles for the JVM and native images
```

## Contributing

Bug reports and pull requests are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for the
development workflow, and [SECURITY.md](SECURITY.md) to report a vulnerability privately.
Problems with the library itself belong in the
[flat2java repository](https://github.com/allanhladki/flat2java/issues).

## License

flat2java-quarkus-demo is released under the [MIT License](LICENSE).
