# Trello Clone Backend

## Overview

This document describes the backend architecture, technologies for the Trello clone project, built using Java, Spring Framework, and PostgreSQL.

## Technologies Used

- **Java**: The primary programming language used for the backend.
- **Spring Boot**: The framework used to build and manage the backend. It simplifies the creation of production-ready applications with Spring.
- **Spring Data JPA**: For database interactions, making it easy to work with relational data in Java applications.
- **PostgreSQL**: The relational database used to store all data, including projects, task states, and tasks.

## API Endpoints

The backend exposes a RESTful API for interacting with the Trello clone application. Below is a list of the main API endpoints:

### Projects
- **GET** `/api/projects` - Retrieve a list of all projects or specifically with project name.
- **POST** `/api/projects` - Create a new project.
- **PATCH** `/api/projects/{project_id}` - Edit a specific project.
- **PUT** `/api/projects` - Create or update a specific project
- **DELETE** `/api/projects/{project_id}` - Delete a specific project.

### Task States
- **GET** `/api/projects/{project_id}/task-states` - Retrieve a list of all task states within a project.
- **POST** `/api/projects/{project_id}/task-states` - Create a new task state within a project.
- **PATCH** `/api/task-states/{task_state_id}` - Update/Edit a task state within a project.
- **PATCH** `/api/task-states/{task_state_id}/position/change` - Change a task state position within a project when we grab it and relocate.
- **DELETE** `/api/task-states/{task_state_id}` - Delete a specific task state.


## Project Structure

The project is organized into several packages, each serving a specific purpose:

- **kz.com.task.tracker**: The root package containing all the application logic.

### **api.controllers**

This package contains the REST controllers that handle incoming HTTP requests.

- **ProjectController**: Manages projects;create, update , delete, update projects.
- **TaskStateController**: Manages task states; change their position, create, update , delete, update task states.

### **api.controllers.helpers**

Helpers that provide common utilities and functions used across different controllers.

- **ControllerHelper**: A helper class providing common functionality for controllers.

### **dto**

This package contains Data Transfer Objects (DTOs) used to encapsulate data and transfer it between layers of the application.

### **exceptions**

Defines custom exceptions used throughout the application to handle various error scenarios.

### **factories**

This package contains factory classes that are responsible for creating instances of different entities and DTOs.

### **store**

Contains classes related to data persistence, including repository interfaces and database interaction logic.

### **Application**

- **Application.java**: The main entry point of the Spring Boot application. This class is annotated with `@SpringBootApplication` and starts the entire Spring context.

## Database Configuration

In `src/main/resources/application.yml`, configure your PostgreSQL database connection:

```properties
server:
  port: 9120
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database_name
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
