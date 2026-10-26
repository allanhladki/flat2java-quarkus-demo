<div align="center">

# flat2java-quarkus-demo

**A Quarkus REST application that reads and writes fixed-width records with flat2java.**

[![CI](https://github.com/allanhladki/flat2java-quarkus-demo/actions/workflows/ci.yml/badge.svg)](https://github.com/allanhladki/flat2java-quarkus-demo/actions/workflows/ci.yml)
[![flat2java](https://img.shields.io/maven-central/v/io.github.allanhladki.flat2java/flat2java-quarkus?label=flat2java)](https://central.sonatype.com/namespace/io.github.allanhladki.flat2java)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Quarkus](https://img.shields.io/badge/Quarkus-3.40%20LTS-4695EB?logo=quarkus&logoColor=white)
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
record, through the `flat2java-quarkus` CDI integration. The mapped class, `SampleModel`, uses
every kind of field the library supports, and the Swagger UI has ready-to-run examples for each
endpoint.

## Contents

- [Requirements](#requirements)
- [Quick start](#quick-start)
- [Startup](#startup)
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
| Quarkus | 3.40.1.1 (LTS) |
| flat2java | 0.1.0 |

## Quick start

```bash
git clone https://github.com/allanhladki/flat2java-quarkus-demo.git
cd flat2java-quarkus-demo
./mvnw quarkus:dev
```

The application listens on `http://localhost:8080`, whose home page has the links, the versions and a
playground that reads the sample record and writes it back. Open the Swagger UI at
`http://localhost:8080/swagger-ui`: every endpoint has a list of named examples in its request body
or `path` parameter. Pick one, press **Execute**, and compare the request and the response. The
examples named `invalid-length` and `negative-number` fail on purpose, to show the error responses.

The same from the command line. `$(cat ...)` drops the line terminator at the end of the sample
file:

```bash
curl -X POST http://localhost:8080/read \
  -H 'Content-Type: text/plain; charset=UTF-8' \
  --data-binary "$(cat samples/typical.txt)"
```

```json
{
  "recordType": "SAMP",
  "schemaVersion": 1,
  "name": "John Doe",
  "reference": "REF-1",
  "priority": 7,
  "quantity": 100,
  "transactionId": 1234567890,
  "amount": 99.99,
  "accountNumber": 987654321,
  "birthDate": "1990-01-01",
  "createdAt": "2024-01-01T12:00:00",
  "address": {"street": "Rua das Flores 100", "city": "Curitiba", "state": "PR", "postalCode": "80010000"},
  "contact": {"email": "john@example.com", "phone": "554133334444"},
  "weeklyHits": [10, 20, 30, 40],
  "items": [
    {"sku": "AB-100", "units": 2, "unitPrice": 12.5},
    {"sku": "CD-200", "units": 1, "unitPrice": 99.9},
    {"sku": "EF-300", "units": 10, "unitPrice": 0.99}
  ],
  "tags": ["new", "vip"],
  "internalNote": null
}
```

## Startup

Quarkus prints the banner, and the application then logs its version and the versions of flat2java,
Quarkus and Java, followed by the addresses of the Swagger UI, the OpenAPI document and the health
check, and a `curl` command to try:

```text
  ______    ______                                                            __
 /      \  /      \                                                          |  \
|  $$$$$$\|  $$$$$$\      __           ______   __    __   ______    ______  | $$   __  __    __   _______
| $$_  \$$ \$$__| $$     |  \ ______  /      \ |  \  |  \ |      \  /      \ | $$  /  \|  \  |  \ /       \
| $$ \     /      $$      \$$|      \|  $$$$$$\| $$  | $$  \$$$$$$\|  $$$$$$\| $$_/  $$| $$  | $$|  $$$$$$$
| $$$$    |  $$$$$$      |  \ \$$$$$$| $$  | $$| $$  | $$ /      $$| $$   \$$| $$   $$ | $$  | $$ \$$    \
| $$      | $$_____      | $$        | $$__| $$| $$__/ $$|  $$$$$$$| $$      | $$$$$$\ | $$__/ $$ _\$$$$$$\
| $$      | $$     \     | $$         \$$    $$ \$$    $$ \$$    $$| $$      | $$  \$$\ \$$    $$|       $$
 \$$       \$$$$$$$$__   | $$          \$$$$$$$  \$$$$$$   \$$$$$$$ \$$       \$$   \$$  \$$$$$$  \$$$$$$$
                   |  \__/ $$              | $$
                    \$$    $$              | $$
                     \$$$$$$                \$$

                                                                                Powered by Quarkus 3.40.1.1
2026-10-26 13:36:37.937  INFO          StartupReport              | flat2java Quarkus demo 0.1.0  |  flat2java 0.1.0  |  Quarkus 3.40.1.1  |  Java 21.0.7
2026-10-26 13:36:37.943  INFO          StartupReport              | Ready in 1.6 s
2026-10-26 13:36:37.943  INFO          StartupReport              |   Swagger UI  http://localhost:8080/swagger-ui
2026-10-26 13:36:37.943  INFO          StartupReport              |   OpenAPI     http://localhost:8080/swagger
2026-10-26 13:36:37.944  INFO          StartupReport              |   Health      http://localhost:8080/health
2026-10-26 13:36:37.944  INFO          StartupReport              |   Try it      curl -X POST http://localhost:8080/read --data-binary @samples/typical.txt
```

The home page at `http://localhost:8080/` shows the same versions, links to the pages above, and has
a playground: the sample record is already filled in, **Read** turns it into JSON and **Write**
turns the JSON back into a record. The page has no external resources.

## Endpoints

| Method and path | Request | Response | Purpose |
|---|---|---|---|
| `POST /read` | `text/plain`, one record | `application/json` | Reads a record into a `SampleModel` |
| `POST /write` | `application/json`, a `SampleModel` | `text/plain; charset=UTF-8` | Writes a `SampleModel` as a record |
| `GET /file?path=<path>` | Query parameter `path` | `application/json` | Reads the file at `path` as one record |
| `GET /` | | HTML | Home page with links, versions and a read/write playground |
| `GET /info` | | `application/json` | Versions of the application, flat2java, Quarkus and Java |
| `GET /swagger` | | OpenAPI document (YAML) | API description |
| `GET /swagger-ui` | | HTML | Swagger UI |
| `GET /health`, `/health/live`, `/health/ready` | | `application/json` | SmallRye Health checks |

The names follow the library: `read` turns a record into an object, `write` turns an object into a
record.

`POST /write` takes the JSON that `POST /read` returns. The Swagger UI has its examples; from the
command line, send the object as the body:

```bash
curl -X POST http://localhost:8080/write \
  -H 'Content-Type: application/json' \
  -d '{"recordType":"SAMP","schemaVersion":1,"name":"John Doe","reference":"REF-1","priority":7,
       "quantity":100,"transactionId":1234567890,"amount":99.99,"accountNumber":987654321,
       "birthDate":"1990-01-01","createdAt":"2024-01-01T12:00:00",
       "address":{"street":"Rua das Flores 100","city":"Curitiba","state":"PR","postalCode":"80010000"},
       "contact":{"email":"john@example.com","phone":"554133334444"},
       "weeklyHits":[10,20,30,40],
       "items":[{"sku":"AB-100","units":2,"unitPrice":12.50},
                {"sku":"CD-200","units":1,"unitPrice":99.90},
                {"sku":"EF-300","units":10,"unitPrice":0.99}],
       "tags":["new","vip"]}'
```

The response is the 315-character record in `samples/typical.txt`.

`GET /file` reads a UTF-8 file with one record and one optional line terminator at its end. A
relative path is relative to the directory where the application was started. The sample files are
`samples/typical.txt`, `samples/accents.txt` and `samples/boundary.txt`:

```bash
curl 'http://localhost:8080/file?path=samples/accents.txt'
```

A file larger than one record is rejected before it is read.

> [!WARNING]
> `GET /file` reads any file that the application process can read. It exists to demonstrate
> reading a record from disk. Do not expose this application on a network you do not control.

## Record layout

`SampleModel` maps a record of 315 characters. It extends `BaseRecord`, and flat2java writes the
fields of a superclass first.

| Field | Columns | Length | Java type | Format |
|---|---|---|---|---|
| `recordType` | 1-4 | 4 | `String` (`BaseRecord`) | Left-aligned, padded with spaces |
| `schemaVersion` | 5-6 | 2 | `int` (`BaseRecord`) | Zero-filled on the left |
| `name` | 7-36 | 30 | `String` | Left-aligned, padded with spaces |
| `reference` | 37-44 | 8 | `String` | Padded with `_` (`padding = '_'`) |
| `priority` | 45-47 | 3 | `int` | Zero-filled on the left |
| `quantity` | 48-57 | 10 | `Integer` | Zero-filled on the left |
| `transactionId` | 58-77 | 20 | `Long` | Zero-filled on the left |
| `amount` | 78-92 | 15 | `BigDecimal` | Two implied decimal places: `000000000009999` is `99.99` |
| `accountNumber` | 93-112 | 20 | `BigInteger` | Zero-filled on the left |
| `birthDate` | 113-120 | 8 | `LocalDate` | `yyyyMMdd` |
| `createdAt` | 121-146 | 26 | `LocalDateTime` | `yyyy-MM-dd'T'HH:mm:ss.SSSSSS` |
| `address` | 147-191 | 45 | `Address`, a Java record | `street` 20, `city` 15, `state` 2, `postalCode` 8 |
| `contact` | 192-233 | 42 | `Contact`, a class | `phone` 12, then `email` 30: explicit `position`, the reverse of the declaration order |
| `weeklyHits` | 234-245 | 12 | `int[]` | 4 elements of 3 columns (`occurs = 4`) |
| `items` | 246-299 | 54 | `List<Item>` | 3 elements of 18 columns; `Item` is a record: `sku` 6, `units` 3, `unitPrice` 9 with two decimals |
| `tags` | 300-315 | 16 | `List<String>` | 2 elements of 8 columns |
| `internalNote` | | | `String` | Not mapped: no `@FlatField`, so it is ignored when writing |

Rules that the examples show:

- Numbers are unsigned and never truncated: a negative number or a number wider than its column
  fails.
- Lists and arrays have exactly `occurs` elements.
- A `String` longer than its column fails with a `400`, because the demo sets
  `flat2java.fail-on-string-truncation=true`. The library default is to cut the text and log a
  warning, which loses data silently.
- Accented characters count as one column each. Requests and responses use UTF-8; a `text/plain`
  body without a charset is read as UTF-8.

The full rules are in the [flat2java README](https://github.com/allanhladki/flat2java#record-format).

## Errors

Errors are returned as JSON:

```json
{
  "error": "Deserialization Error",
  "message": "SampleModel: expected a record of 315 characters, got 5",
  "requestId": "a30d30a9-9fe7-4554-9df4-25ded44e86aa",
  "timestamp": "2026-10-26T14:38:05.816839271Z"
}
```

| Cause | Status | `error` |
|---|---|---|
| A record that cannot be read: wrong length, letters in a numeric column, an invalid date | 400 | `Deserialization Error` |
| An object that cannot be written: a negative number, a number or text wider than its column, a list of the wrong size | 400 | `Serialization Error` |
| Missing or empty body, malformed JSON, missing, invalid or blank `path`, a file larger than one record or not in UTF-8 | 400 | `Bad Request` |
| No file at `path`, or a path that is a directory; unknown path | 404 | `Not Found` |
| HTTP method not allowed | 405 | `Method Not Allowed` |
| Body over 64 KB | 413 | |
| `Content-Type` not accepted by the endpoint | 415 | `Unsupported Media Type` |
| Invalid `@FlatField` mapping or any unexpected exception | 500 | `Flat2Java Error` or `Internal Server Error`, with a generic message |

Messages name the field, never its value, so they can be logged. Every response, including errors,
has an `X-Request-Id` header. The same ID is the `requestId` of the error, and its first 8
characters appear in every log line of the request:

```text
2026-10-26 13:38:05.694  WARN 2a07a8be ExceptionMappers           | Deserialization Error: SampleModel: expected a record of 315 characters, got 5
```

The columns are the time, the level, the request ID (empty outside a request), the class and the
message. Dev mode adds colors; the other profiles write plain text, so the log of a container or of
CI has no escape codes.

The application never logs the contents of a record.

## Configuration

`src/main/resources/application.properties`:

| Property | Value | Purpose |
|---|---|---|
| `quarkus.http.port` | `8080` | HTTP port |
| `quarkus.http.non-application-root-path` | `/` | Serves health and OpenAPI at the root instead of `/q` |
| `quarkus.http.limits.max-body-size` | `64K` | Largest request body |
| `quarkus.smallrye-openapi.path` | `/swagger` | OpenAPI document path |
| `quarkus.swagger-ui.always-include` | `true` | Swagger UI in the packaged application |
| `quarkus.log.console.format` | columns | Time, level, request ID, class and message; colored in dev mode only |
| `quarkus.log.category."io.quarkus".level` | `WARN` | Hides the "started" line of Quarkus, which `StartupReport` replaces |
| `quarkus.banner.path` | `banner.txt` | Banner printed at startup |
| `quarkus.log.category."io.github.allanhladki".level` | `INFO` (`DEBUG` in dev mode) | Application log level |
| `flat2java.fail-on-string-truncation` | `true` | A text longer than its column is an error; the library default is `false` |
| other `flat2java.*` | the defaults of `flat2java-quarkus` | [flat2java options](https://github.com/allanhladki/flat2java/blob/master/flat2java-quarkus/README.md#properties) |

In dev mode the application listens on `localhost` only; in the container, on all interfaces. Any
property can be overridden with a system property or an environment variable, for example
`QUARKUS_HTTP_PORT=9090 ./mvnw quarkus:dev`. Tests use `src/test/resources/application.properties`,
which sets the test port to `8089`.

## Running in a container

The [Dockerfiles](src/main/docker) are the ones generated by Quarkus. The JVM image also contains
the `samples` directory, so the `path` examples work in it: `samples/typical.txt` is relative to
`/deployments`.

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
`-Dquarkus.native.container-build=true`. The mapped classes are annotated with
`@RegisterForReflection`, as flat2java requires.

</details>

> [!NOTE]
> The native build is not covered by CI or by the tests, and the native image does not contain the
> `samples` directory. The JVM image is built and started on every pull request.

## Tests

```bash
./mvnw verify
```

| Command | What it does |
|---|---|
| `./mvnw verify` | Runs the tests, checks formatting, writes the coverage report to `target/jacoco-report` |
| `./mvnw spotless:apply` | Formats the code (google-java-format, AOSP style) |
| `./mvnw test -Dtest=RecordEndpointsTest` | Runs one test class |

The tests start the application with `@QuarkusTest` and call it with REST Assured:

| Class | Covers |
|---|---|
| `RecordEndpointsTest` | `/read` and `/write`: every example of the Swagger UI in both directions, each kind of field, accents, a text over its column, and every error response |
| `FileEndpointTest` | `/file`: relative, absolute and accented paths, line terminators, and the missing, blank, invalid, oversized, non-UTF-8 and multi-record cases |
| `OpenApiTest` | The OpenAPI document and its named examples, the Swagger UI and the health checks |

## Project layout

```text
src/main/java/io/github/allanhladki/flat2java/quarkus/demo/
├── api/         FlatFileResource (the endpoints) and ApiExamples (the Swagger examples)
├── exception/   ExceptionMappers: exceptions to JSON error responses
├── filter/      RequestIdFilter: request ID in the log, header and errors
├── info/        Versions, InfoResource (/info) and StartupReport (versions and addresses at startup)
├── model/       SampleModel, BaseRecord, Address, Contact, Item and ErrorResponse
└── service/     FlatFileService: calls flat2java and reads files
src/main/resources/
├── banner.txt                   Startup banner
└── META-INF/resources/index.html  Home page
samples/         Example records, one per file
src/main/docker/ Dockerfiles for the JVM and native images
```

## Contributing

Bug reports and pull requests are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for the
development workflow, and [SECURITY.md](SECURITY.md) to report a vulnerability privately.
Problems with the library itself belong in the
[flat2java repository](https://github.com/allanhladki/flat2java/issues).

## License

flat2java-quarkus-demo is released under the [MIT License](LICENSE).
