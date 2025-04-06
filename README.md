# auth-servive

## Description
This is a Spring Boot application for managing user data and authentication.

## Project Structure

```
auth-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── auth_service/
│   │   │           ├── config/
│   │   │           ├── controller/
│   │   │           ├── exception/
│   │   │           ├── filter/
│   │   │           ├── model/
│   │   │           ├── repository/
│   │   │           ├── service/
│   │   │           └── util/
│   │   └── resources/
│   └── test/
├── pom.xml
└── README.md
```

## Endpoints
- `POST /api/login`: Authenticates a user and returns a JWT token.
- `POST /api/refresh-token`: Refreshes the JWT token using a refresh token.
- `GET /users/{id}`: Retrieve a user by ID.
- `POST /users`: Create a new user.
- `DELETE /users/{id}`: Delete a user by ID.
- `GET /users`: Retrieve all users.
- `PUT /users/{id}/activate`: Activate a user by ID.

## Custom Error Codes
- GEN_001: Internal server error.
- USR_001: User not found.
- USR_002: Person not found.
- USR_003: Email already exists.
- USR_004: Username already exists.
- USR_005: Username not found.
- AUTH_001: Access denied.
- AUTH_002: Invalid credentials.
- AUTH_003: Invalid signature.
- AUTH_005: Expired JWT.
- AUTH_006: Invalid JWT.

## Requirements
- Java 21
- Maven 3.6+
- Docker

## How to Run

### Using Maven
1. Navigate to the project directory.
2. Run the application:
   ```sh
   mvn spring-boot:run
   ```

### Using Docker
1. Build the Docker image:
   ```sh
   docker build -t auth-service .
   ```
2. Run the Docker container:
   ```sh
   docker run -p 3002:3002 auth-service
   ```

### Using Docker Compose
1. Run the Docker Compose:
   ```sh
   docker-compose up
   ```

### Deploying on kind

To deploy the application on a local Kubernetes cluster using kind, you need to fill in the values in the helm/values-dev.yaml file.

Requirements:
- kind
- kubectl
- Helm
- AWS CLI

1. Run the script to deploy the application on kind:
   ```sh
   ./deploy_local.sh
   ```

## Links
- [Actuator](http://localhost:3002/actuator)
- [Swagger](http://localhost:3002/v1/swagger-ui)
- [API](http://localhost:3002/v1/api-docs)
- [Health](http://localhost:3002/health)
