# Todolist API

This is a To-do List API developed using **Spring Boot** and **Java 21**. The project is in its early stages and aims to provide a simple and efficient platform for personal task management.

## 🚀 Technologies

- **Java 21**: The latest LTS version of Java with modern feature support.
- **Spring Boot 4.0.5**: Framework to streamline Java application development.
- **Maven**: Dependency management and build automation.

## 🛠️ Current Features

The project is currently in the initial development phase. The following features are being implemented:

- **User Management**:
  - User registration (Base endpoint: `/users`).

## ⚙️ How to Run

To run the application locally, follow these steps:

### Prerequisites
- JDK 21 installed.
- Maven installed (or use the included Maven Wrapper).

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/todolist.git
   ```
2. Navigate to the project directory:
   ```bash
   cd todolist
   ```
3. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The API will be available at `http://localhost:8080`.

## 📌 Endpoints (In Development)

### Users
- `POST /users/user`: Creates a new user.
  - **Sample Body (JSON):**
    ```json
    {
      "username": "tobias",
      "name": "Tobias Silva"
    }
    ```

## 🛣️ Roadmap

- [ ] Database configuration (H2/PostgreSQL).
- [ ] Implementation of persistence with Spring Data JPA.
- [ ] Full CRUD for Tasks.
- [ ] User Authentication.
- [ ] Data validation.

---
Developed by [Tobias](https://github.com/your-username).
