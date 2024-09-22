# URL Shortener Project Setup

This document provides step-by-step instructions to set up the URL Shortener project. Users can choose to run the application using either an in-memory H2 database, a PostgreSQL database, or an H2 database via Docker containers.
 if you haven't set up the project yet, you can use the live demo at [http://url.work.gd](http://url.work.gd). [swagger-ui documentation](http://url.work.gd/api/url/swagger-ui.html)
## Setup Options

### Option 1: Using In-Memory H2 Database or with postgres

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/iamkiranrajput/url-shortener.git
   ```

2. **Navigate to the Project Directory**:
   ```bash
   cd url-shortener
   ```


3. **For Postgres Configuration**:
   Update the `application.properties` file to point to your PostgreSQL database. Here is a sample configuration:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdbname
   spring.datasource.driverClassName=org.postgresql.Driver
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```



3. **For H2 DB Configuration**:
   Update the `application.properties` file to point to your h2 database. Here is a sample configuration:
   ```properties
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.datasource.platform=h2
   ```

4. **Run the Application**:

   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:
   Open your browser and navigate to `http://localhost:8181` to access the API.


### Option 2: Run Application with PostgreSQL Database via Docker

1. **Pull the url-shortener Docker Container**:
   If you want to use a PostgreSQL database, you can pull the Docker container:
   ```bash
   docker pull iamkiranrajput/url-shortener:latest
   ```
2. **Create a Docker Network (Optional)**
   This allows both containers to communicate easily.
   ```
   docker network create url-shortener-network
   ```

3. **Run the PostgreSQL Container**:
   Start the PostgreSQL container with the following command:
   ```bash
   docker run -d --name url-shortener-db `
   --network url-shortener-network `
   -e POSTGRES_DB=url-shortener `
   -e POSTGRES_USER=postgres `
   -e POSTGRES_PASSWORD=8888 `
   -p 5432:5432 postgres:latest
   ```

4. **Run the url-Shortener Application Container**:
   Start the spring boot application container with the following command:
   ```bash
   docker run -d --name url-shortener-app `
   --network url-shortener-network `
   -e SPRING_DATASOURCE_URL=jdbc:postgresql://url-shortener-db:5432/url-shortener `
   -e SPRING_DATASOURCE_USERNAME=postgres `
   -e SPRING_DATASOURCE_PASSWORD=8888 `
   -p 8181:8181 iamkiranrajput/url-shortener:latest
   ```


### Option 3: Using H2 Database via Docker

1. **Pull the H2 Docker Container**:
   To use an H2 database with Docker, pull the Docker container:
   ```bash
   docker pull iamkiranrajput/url-shortener:dbh2
   ```

2. **Run the H2 Container**:
   Start the container with the following command:
   ```bash
   docker run -d -p 8081:8081 --name url-shortener-h2 iamkiranrajput/url-shortener:dbh2
   ```

3. **Access the Application**:
   Open your browser and navigate to `http://localhost:8181` to access the API.
    - You can also access the H2 console at `http://localhost:8181/h2-console` to view and manage your H2 database.

## Conclusion

You now have the URL Shortener application set up with your preferred database option: in-memory H2, PostgreSQL in Docker, or H2 in Docker. You can begin testing the API endpoints using tools like Postman.

For further assistance, refer to the [API Documentation](https://www.postman.com/glitch-guardians/workspace/guardians/collection/33641536-8f994d74-f358-4117-bee2-086a42aef3d0?action=share&creator=33641536&active-environment=33641536-16d30508-b9a8-46c2-8246-623ca81702a9).

--- 
