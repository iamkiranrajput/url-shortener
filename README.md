# URL Shortener Service

## Project Overview

The URL Shortener project is a Spring Boot-based application that allows users to shorten URLs, track their usage, set expiration dates, and implement usage limits. The project also features pagination and sorting for the shortened URLs, and comprehensive logging and exception handling.

## Feature
- URL shortening with customizable expiration dates and usage limits.
- Detailed tracking of URL usage.
- Support for pagination and sorting.
- Logging and exception handling for error tracking.

## Technology Stack
- **Java**: Core language for business logic.
- **Spring Boot**: Framework for the application.
- **JPA/Hibernate**: ORM for data persistence.
- **H2 Database**: In-memory database for testing.
- **Lombok**: Reduces boilerplate code.
- **Maven**: Dependency management.

## Installation

### Prerequisites
- Java 11 or higher.
- Maven 3.6 or higher.
- A Spring-compatible IDE (IntelliJ, Eclipse, etc.).
- Git.

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/url-shortener.git
   ```

2. Navigate to the project directory:
   ```bash
   cd url-shortener
   ```

3. Install dependencies and build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Project Structure
```
src/
 ├── main/
 │   ├── java/
 │   │   └── com/guardians/
 │   │       ├── controller/        # Controllers for REST endpoints
 │   │       ├── dto/               # Data Transfer Objects (DTOs)
 │   │       ├── exception/         # Custom exception handlers
 │   │       ├── model/             # Entity classes
 │   │       ├── repository/        # Data access layer
 │   │       └── service/           # Business logic layer
 │   └── resources/
 │       ├── application.properties # Application configuration
 │       └── data.sql               # SQL seed data (if needed)
 └── test/
     └── java/                      # Unit and integration tests
```

## Configuration

- The application uses an in-memory H2 database for ease of development and testing.
- You can configure the `application.properties` file to point to a persistent database like MySQL or PostgreSQL for production.

Here is a sample configuration for H2:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

For production, you can configure OAuth2 settings like this:

```properties
spring.security.oauth2.client.registration.google.client-id=<client-id>
spring.security.oauth2.client.registration.google.client-secret=<client-secret>
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/google
spring.security.oauth2.client.registration.google.scope=email,profile
```

## Usage

### Shorten a URL

- Send a POST request to `/api/url/shorten` with the following payload:

```json
{
    "originalUrl": "https://www.example.com",
    "expiresAt": "2024-12-31T23:59:59",
    "usageLimit": 100
}
```

### Redirect to Original URL

- Send a GET request to `/api/url/{shortUrl}`. This will redirect to the original URL if the shortened URL is valid.

### Retrieve All URLs (With Pagination and Sorting)

- Send a GET request to `/api/url/all?pageNumber=0&pageSize=10&sortBy=createdAt&dir=asc` to get a paginated and sorted list of URLs.

## Logging and Exception Handling

- **Logging**: The project uses `Slf4j` for logging critical events, errors, and debugging information. Logs are recorded for important actions like URL creation, fetching, and error handling.

- **Global Exception Handling**: The project includes a `GlobalExceptionHandler` to handle specific exceptions like `ShortUrlNotFoundException` and more general ones, ensuring a uniform error response structure.

Example of logging in the service layer:
```java
log.info("Shortened URL created successfully: {}", savedUrl.getShortUrl());
log.error("An error occurred during URL shortening: {}", ex.getMessage());
```

### Custom Exception

- `PaginationException`: Thrown when an error occurs during pagination, handled with a custom response.

```java
@ExceptionHandler(PaginationException.class)
public ResponseEntity<UrlResponse> handlePaginationException(PaginationException ex) {
    log.error("Pagination error: {}", ex.getMessage());
    UrlResponse response = new UrlResponse();
    response.setMessage("Pagination failed: " + ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}
```

## Pagination and Sorting

The project supports pagination and sorting when fetching URLs.

- **Pagination**: Implemented using `PageRequest` and the `Pageable` interface.
- **Sorting**: URLs can be sorted by any field, such as creation date, using the `sortBy` and `dir` (ascending/descending) parameters.

Example endpoint:

```bash
GET /api/url/all?pageNumber=0&pageSize=5&sortBy=createdAt&dir=asc
```

## OAuth2 Authentication

The application integrates OAuth2 for secure access to specific endpoints. Users must authenticate using OAuth2 providers like Google to shorten or manage URLs.

### Steps for OAuth2 Integration:

1. Register your application with an OAuth2 provider like Google.
2. Add OAuth2 configurations in `application.properties`.
3. Secure specific endpoints using `@PreAuthorize` or `@Secured`.

Example:

```java
@PreAuthorize("hasAuthority('SCOPE_email')")
@PostMapping("/shorten")
public ResponseEntity<UrlResponse> createShortUrl(@RequestBody UrlRequest urlRequest) {
    UrlResponse response = urlShortenerService.createShortUrl(urlRequest);
    return ResponseEntity.ok(response);
}
```

## API Endpoints

| HTTP Method | Endpoint              | Description                      |
|-------------|-----------------------|----------------------------------|
| `POST`      | `/api/url/shorten`     | Shorten a URL                    |
| `GET`       | `/api/url/{shortUrl}`  | Redirect to the original URL     |
| `GET`       | `/api/url/all`         | Fetch all URLs (with pagination) |
| `GET`       | `/api/url/stats/{id}`  | Get URL statistics               |

## Contributing

1. Fork the repository.
2. Create a new branch.
3. Make changes and ensure the project builds successfully.
4. Submit a pull request with a detailed description of your changes.

## License

This project is licensed under the MIT License.

---

This `README.md` should provide a comprehensive guide for developers and users to understand and contribute to your URL Shortener project.
