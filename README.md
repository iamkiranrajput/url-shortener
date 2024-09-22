# URL Shortener Service

## Project Overview

The URL Shortener project is a Spring Boot-based application that allows users to shorten URLs, track their usage, set expiration dates, and implement usage limits. The project also features pagination and sorting for the shortened URLs, along with comprehensive logging and exception handling.

## Quick Links

- **Postman Documentation**: [Postman API Collection Documentation](https://www.postman.com/glitch-guardians/workspace/guardians/collection/33641536-8f994d74-f358-4117-bee2-086a42aef3d0?action=share&creator=33641536&active-environment=33641536-16d30508-b9a8-46c2-8246-623ca81702a9)
- **Swagger OpenAPI Documentation Runnig Appllication**: [Swagger UI](http://url.work.gd/swagger-ui.html) | [http://url.work.gd/api/url/](http://url.work.gd/api/url)
- **Docker Hub Images**:
  - [URL Shortener with H2 Database](https://hub.docker.com/r/iamkiranrajput/url-shortener) use tag :dbh2
  - [URL Shortener with PostgreSQL](https://hub.docker.com/r/iamkiranrajput/url-shortener) use tag :latest
- **Setup the Project** [setup](setup.md)

## Features
- **URL Shortening**: Create short links with customizable expiration dates and usage limits.
- **Usage Tracking**: Monitor how many times a short URL has been accessed.
- **Pagination and Sorting**: Easily navigate through shortened URLs with pagination and sorting options.
- **Logging and Exception Handling**: Keep track of errors and important events in the application.
- **OpenAPI Documentation**: Swagger UI and OpenAPI documentation for easy API exploration.

## Technology Stack
- **Java**: Core language for business logic.
- **Spring Boot**: Framework for building the application.
- **JPA/Hibernate**: ORM for data persistence.
- **PostgreSQL**: Persistent database option for production usage.
- **H2 Database**: In-memory database for development and testing.
- **Lombok**: Java library to minimize boilerplate code.
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
   git clone https://github.com/iamkiranrajput/url-shortener.git
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
 └── test/
     └── java/                      # Unit and integration tests
```

## Configuration

- The application uses an in-memory H2 database for ease of development and testing.
- You can configure the `application.properties` file to point to a persistent database like MySQL or PostgreSQL for production.

### Sample Configuration for H2:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## Usage
 refer to the [Postman API Collection Documentation ](https://www.postman.com/glitch-guardians/workspace/guardians/collection/33641536-8f994d74-f358-4117-bee2-086a42aef3d0?action=share&creator=33641536&active-environment=33641536-16d30508-b9a8-46c2-8246-623ca81702a9) or view the documentation via Swagger once the application is running.
 
### Shorten a URL

- Send a POST request to `/api/url/shorten` with the following payload:

```json
{
    "originalUrl": "https://www.x.com/iamkiranrajput",
    "expiresAt": "2025-12-31T23:59:59",
    "usageLimit": 7
}
```

### Redirect to Original URL

- Send a GET request to `/api/url/{shortUrl}`. This will redirect to the original URL if the shortened URL is valid.

### Retrieve All URLs (With Pagination and Sorting)

- Send a GET request to `/api/url/all?pageNumber=0&pageSize=10&sortBy=createdAt&dir=asc` to get a paginated and sorted list of URLs.

## Logging and Exception Handling

- **Logging**: The project uses `Slf4j` for logging critical events, errors, and debugging information. Logs are recorded for important actions like URL creation, fetching, and error handling.

- **Global Exception Handling**: The project includes a `GlobalExceptionHandler` to handle specific exceptions like `ShortUrlNotFoundException` and more general ones, ensuring a uniform error response structure.

### Custom Exceptions
- `PaginationException`: Thrown when an error occurs during pagination, handled with a custom response.
- `UrlNotFoundException`: Thrown when a URL cannot be found, handled with a custom response to inform users that the requested URL does not exist.
- `GlobalException`: Catches any general exceptions not specifically handled elsewhere, providing a standardized error message for unexpected issues.

## Pagination and Sorting

The project supports pagination and sorting when fetching URLs.

- **Pagination**: Implemented using `PageRequest` and the `Pageable` interface.
- **Sorting**: URLs can be sorted by any field, such as creation date, using the `sortBy` and `dir` (ascending/descending) parameters.

### Example Endpoint for Pagination:
```bash
GET /api/url/all?pageNumber=0&pageSize=5&sortBy=createdAt&dir=asc
```

## API Endpoints

| HTTP Method | Endpoint              | Description                      |
|-------------|-----------------------|----------------------------------|
| `POST`      | `/api/url/shorten`     | Shorten a URL                    |
| `GET`       | `/api/url/{shortUrl}`  | Redirect to the original URL     |
| `GET`       | `/api/url/all`         | Fetch all URLs (with pagination) |

## Testing the API

To test the API, you can use Postman. You can download the source code or Docker container and run it. Set the environment variable with the name `{url}` to `http://localhost:8181`. Alternatively, if you haven't set up the project yet, you can use the live demo at [http://url.work.gd](http://url.work.gd).
swagger-ui documentation [http://url.work.gd/api/url/swagger-ui.html](http://url.work.gd/api/url/swagger-ui.html)
## Reference Documentation

For a comprehensive understanding of the API, you can refer to the [Postman API Collection](https://www.postman.com/glitch-guardians/workspace/guardians/collection/33641536-8f994d74-f358-4117-bee2-086a42aef3d0?action=share&creator=33641536&active-environment=33641536-16d30508-b9a8-46c2-8246-623ca81702a9) or view the documentation via Swagger once the application is running.

## Contributing

1. Fork the repository.
2. Create a new branch.
3. Make changes and ensure the project builds successfully.
4. Submit a pull request with a detailed description of your changes.

## License
This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
