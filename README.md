# Flat2Java Quarkus Demo

A demonstration project showcasing the integration of [Flat2Java](https://github.com/allanhladki/flat2java) with Quarkus framework for handling flat file serialization and deserialization in Java applications.

## 🚀 Overview

This project demonstrates how to use the Flat2Java library within a Quarkus application to:
- Serialize Java objects to flat file format
- Deserialize flat file content back to Java objects
- Handle special characters and UTF-8 encoding properly
- Provide REST API endpoints for flat file operations
- Include comprehensive testing with REST Assured

## ✨ Features

- **REST API Endpoints**: Serialize/deserialize flat files via HTTP
- **UTF-8 Support**: Proper handling of special characters (é, í, etc.)
- **File Operations**: Read and process flat files from the filesystem
- **OpenAPI Documentation**: Interactive API documentation via Swagger UI
- **Comprehensive Testing**: Full test coverage with REST Assured
- **Health Checks**: Application health monitoring
- **Request Tracing**: Unique request ID tracking for debugging

## 🛠️ Technology Stack

- **Java 21**: Latest LTS version
- **Quarkus 3.20.0**: Supersonic Subatomic Java framework
- **Flat2Java 0.0.1-rc2**: Flat file serialization library
- **Maven**: Build and dependency management
- **REST Assured**: API testing framework
- **Lombok**: Reduces boilerplate code
- **SLF4J**: Logging facade

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- Git

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/allanhladki/flat2java-quarkus-demo.git
cd flat2java-quarkus-demo
```

### 2. Build the Project

```bash
mvn clean compile
```

### 3. Run the Application

```bash
mvn quarkus:dev
```

The application will start on `http://localhost:8080`

### 4. Access the API

- **Swagger UI**: `http://localhost:8080/swagger-ui`
- **OpenAPI Spec**: `http://localhost:8080/swagger`

## 📚 API Endpoints

### Serialize Flat File to POJO
```http
POST /serialize
Content-Type: text/plain; charset=UTF-8

José María O'Connor-Smith     00000000420000000000098765432100000000001234500000000000123456789198506152024-06-15T14:30:00.000000
```

### Deserialize POJO to Flat File
```http
POST /deserialize
Content-Type: application/json

{
  "name": "José María O'Connor-Smith",
  "quantity": 42,
  "transactionId": 987654321,
  "amount": 123.45,
  "accountNumber": 123456789,
  "birthDate": "1985-06-15",
  "createdAt": "2024-06-15T14:30:00"
}
```

### Read File and Serialize
```http
GET /file?path=/path/to/your/flatfile.txt
```

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test
```bash
mvn test -Dtest=RestApiTest#testDeserializeSuccessWithSpecialCharsData
```

### Run Tests with Debug Logging
```bash
mvn test -Dquarkus.log.level=DEBUG
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/github/allanhladki/flat2java/quarkus/demo/
│   │       ├── api/           # REST API endpoints
│   │       ├── filter/        # HTTP filters (encoding, tracing)
│   │       ├── model/         # Data models with Flat2Java annotations
│   │       ├── service/       # Business logic services
│   │       └── exception/     # Custom exception classes
│   └── resources/
│       └── application.properties
├── test/
│   ├── java/                  # Test classes
│   └── resources/             # Test configuration
└── docs/                      # Project documentation
```

## 🔧 Configuration

### Application Properties

Key configuration options in `src/main/resources/application.properties`:

```properties
# Server configuration
quarkus.http.port=8080
quarkus.http.host=0.0.0.0

# Logging
quarkus.log.level=INFO
quarkus.log.category."com.github.allanhladki".level=TRACE

# OpenAPI/Swagger
quarkus.smallrye-openapi.path=/swagger
quarkus.http.non-application-root-path=/
```

### Test Configuration

Test-specific settings in `src/test/resources/application.properties`:

```properties
quarkus.http.test-port=8089
quarkus.log.level=INFO
quarkus.log.category."com.github.allanhladki.flat2java.quarkus.demo".level=DEBUG
```

## 🎯 Key Components

### REST API
The main API class handling HTTP requests:

- `POST /serialize` - Converts flat text to POJO
- `POST /deserialize` - Converts POJO to flat text
- `GET /file?path=...` - Reads file and serializes content

### Character Encoding Filter
Ensures proper UTF-8 handling for special characters in flat file content.

## 🧪 Testing

The project includes comprehensive testing with:

- **Unit Tests**: Individual component testing
- **Integration Tests**: Full API endpoint testing
- **Special Character Tests**: UTF-8 encoding validation
- **Boundary Tests**: Edge case handling
- **Error Handling Tests**: Invalid input scenarios

### Test Fixtures
Predefined test data including:
- Valid flat text samples
- Special character examples
- Boundary value cases
- Invalid format scenarios

## 📝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add comprehensive tests
5. Submit a pull request

## 📄 License

This project is licensed under the same license as the Flat2Java library.

## 🤝 Support

For issues and questions:
- Check the [Flat2Java documentation](https://github.com/allanhladki/flat2java)
- Review test cases for usage examples
- Check application logs for detailed error information

## 🔗 Related Links

- [Flat2Java Library](https://github.com/allanhladki/flat2java)
- [Quarkus Framework](https://quarkus.io/)
- [REST Assured Testing](https://rest-assured.io/)
