# Todo List API - Roadmap.sh Challenge

This project is a **task management API** developed as part of the [roadmap.sh](https://roadmap.sh/projects/todo-list-api) challenge using **Spring Boot** with **Kotlin**. The API allows managing task lists with JWT authentication using **Java-JWT from Auth0**. It also includes validations through **Spring Validation** and security through **Spring Security**. The package manager is **Gradle**, and the project runs with **JDK 21**.

## Requirements

Before running this project, make sure you have the following:

- **JDK 21** or higher
- **Gradle** (optional if you use an IDE like IntelliJ that can handle it automatically)
- **MySQL** (or any other compatible database)

## Database Configuration

It is not necessary to manually create the database or tables, as the project includes the `createDatabaseIfNotExist=true` property to automatically create the database when the application starts. The tables are also automatically generated via **Spring Data JPA**.

Make sure MySQL is installed and configured correctly, then update the credentials in `application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/todolist_db?createDatabaseIfNotExist=true
spring.datasource.username=REPLACE_WITH_YOUR_USER_DB
spring.datasource.password=REPLACE_WITH_YOUR_PASSWORD_DB
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

# Security Configuration
security.jwt.key.private=REPLACE_WITH_YOUR_SIGNING_VALUE
security.jwt.user.generator=REPLACE_WITH_USER_GENERATOR
```

## Security Configuration Explanation
`security.jwt.key.private`: Provide any value that will be used to sign the JWT tokens, ensuring their validity.

`security.jwt.user.generator`: This value is used to manage user authentication. You should configure it according to how you handle users and credentials in your application.

## Running the Project
1. Clone the repository:

   ```bash
   git clone https://github.com/FedeZet/todo-list.git
   cd todo-list
   ```

2. Build and run the project using Gradle or from your favorite IDE:
   ```bash
   ./gradlew clean build
   java -jar build/libs/todo-list-0.0.1-SNAPSHOT.jar
   ```

3. Access the API at `http://localhost:8080`.

## Endpoints

The API provides the following endpoints:

- `POST /register`: Registers a new user. The request requires a JSON body with the following fields:
```json
{
  "name": "User name",
  "email": "user@example.com",
  "password": "your_password"
}
```
- `POST /login`: Authenticates a user and provides a JWT token. The request requires a JSON body with the following fields:
```json
{
  "email": "user@example.com",
  "password": "your_password"
}
```
- `POST /todos`: Creates a new task. The request requires a JSON body with the following fields:
```json
{
  "title": "Task title",
  "description": "Task description"
}
```
- `GET /todos?page=1&limit=10`: Retrieves a list of all tasks.
- `PUT /todos/{id}`: Updates a task by its ID.
- `DELETE /todos/{id}`: Deletes a task by its ID.
- `GET /user`: Retrieves the authenticated user

## Main Dependencies

- **Spring Boot**: 3.3.3
- **Spring Web**: To build the backend.
- **Spring Data JPA**: For data persistence.
- **Spring Security**: For securing the API.
- **Java-JWT from Auth0**: For JWT authentication.
- **Spring Validation**: For data validation.
- **MySQL Driver**: For connecting to the MySQL database.

## Contributions

Contributions are welcome to improve the code and help us learn more about **Spring**. If you find areas for improvement, feel free to contribute.
