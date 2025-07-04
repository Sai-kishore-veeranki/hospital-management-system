# Hospital Management System (HMS)

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Sai-kishore-veeranki/hospital-management-system/main?label=build&logo=github)
![GitHub issues](https://img.shields.io/github/issues/Sai-kishore-veeranki/hospital-management-system?logo=github)
![GitHub last commit](https://img.shields.io/github/last-commit/Sai-kishore-veeranki/hospital-management-system?logo=git)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
<!-- Replace YOUR_USERNAME with your actual GitHub username -->

## 🏥 Project Overview

The Hospital Management System (HMS) is a robust and scalable Spring Boot application designed to streamline the operations of a modern hospital. It provides a secure and efficient platform for managing patient records, doctor information, and appointments, with role-based access control (RBAC) to ensure data integrity and security.

This system aims to:
*   **Centralize Data:** Provide a single source of truth for patient, doctor, and appointment data.
*   **Improve Efficiency:** Automate common hospital workflows, reducing manual effort and errors.
*   **Enhance Security:** Implement JWT-based authentication and authorization to protect sensitive information.
*   **Facilitate Collaboration:** Enable seamless interaction between administrators, doctors, and patients.

## ✨ Features

*   **User  Authentication & Authorization:**
    *   Secure registration and login using JWT (JSON Web Tokens).
    *   Role-Based Access Control (RBAC) for `ADMIN`, `DOCTOR`, and `PATIENT` roles.
*   **Patient Management:**
    *   Create, retrieve, update, and delete (CRUD) patient records.
    *   Search patients by name.
*   **Doctor Management:**
    *   Create, retrieve, update, and delete (CRUD) doctor records.
    *   Search doctors by name or specialization.
*   **Appointment Management:**
    *   Schedule, view, update, and cancel appointments.
    *   Availability checks to prevent double-booking for doctors.
    *   Filter appointments by patient, doctor, or status.
*   **Data Validation:** Comprehensive input validation using `jakarta.validation` annotations.
*   **Global Exception Handling:** Centralized error handling for a consistent API response.
*   **RESTful API:** Clean and well-documented API endpoints.
*   **Swagger/OpenAPI Documentation:** Automatically generated interactive API documentation.

## 🚀 Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Ensure you have the following software installed on your system:

*   **Java Development Kit (JDK):** Version 21 or higher.
    *   [Download JDK](https://www.oracle.com/java/technologies/downloads/)
*   **Apache Maven:** Version 3.x or higher.
    *   [Download Maven](https://maven.apache.org/download.cgi)
*   **MySQL Database:** For data persistence.
    *   [Download MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
*   **Git:** For cloning the repository.
    *   [Download Git](https://git-scm.com/downloads)
*   **IDE (Recommended):** IntelliJ IDEA, Eclipse, or VS Code with Java extensions.

### Database Setup

1.  **Create a MySQL Database:**
    Open your MySQL client (e.g., MySQL Workbench, command line) and execute the following SQL command to create a new database:
    ```sql
    CREATE DATABASE hospital_management_db; -- You can choose a different name
    ```

2.  **Configure Database Connection:**
    Create or update the `application.properties` file located in `src/main/resources/`. Add or modify the following properties with your MySQL database credentials and configuration:

    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:mysql://localhost:3306/hospital_management_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update # Automatically creates/updates tables based on entities
    spring.jpa.show-sql=true             # Optional: logs SQL queries to console for debugging

    # JWT Configuration (replace with strong, unique values)
    jwt.secret=YOUR_VERY_LONG_AND_SECURE_JWT_SECRET_KEY_HERE_AT_LEAST_256_BITS_FOR_HS256_ALGORITHM
    jwt.expiration=3600000 # 1 hour in milliseconds (1000 * 60 * 60)
    ```
    **Important:**
    *   Replace `your_mysql_username` and `your_mysql_password` with your actual MySQL credentials.
    *   For `jwt.secret`, generate a strong, random Base64 encoded key. You can use an online tool or a simple Java snippet:
        ```java
        import io.jsonwebtoken.security.Keys;
        import java.util.Base64;
        
        public class KeyGenerator {
            public static void main(String[] args) {
                String secretString = Base64.getEncoder().encodeToString(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded());
                System.out.println(secretString);
            }
        }
        ```
    *   `spring.jpa.hibernate.ddl-auto=update` is suitable for development. For production environments, consider `validate` or `none` and use dedicated schema migration tools like Flyway or Liquibase for controlled database evolution.

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/YOUR_USERNAME/hospital-management-system.git
    cd hospital-management-system
    ```
    *Remember to replace `YOUR_USERNAME` with your actual GitHub username.*

2.  **Build the project:**
    Navigate to the project's root directory (where `pom.xml` is located) and build the application using Maven:
    ```bash
    mvn clean install
    ```
    This command compiles the code, runs tests, and packages the application into a JAR file in the `target/` directory.

### Running the Application

After a successful build, you can run the application:

1.  **Execute the JAR file:**
    ```bash
    java -jar target/hospital-management-system-0.0.1-SNAPSHOT.jar
    ```
    The application will start on port `8080` by default. You should see logs indicating that Spring Boot has started successfully.

    Alternatively, if using an IDE like IntelliJ IDEA, you can run the `HospitalManagementSystemApplication.java` file directly.

## 💡 API Usage and Endpoints

The HMS exposes a comprehensive set of RESTful API endpoints. You can interact with these endpoints using tools like Postman, Insomnia, `curl`, or directly through the Swagger UI.

### Base URL

All API endpoints are prefixed with `http://localhost:8080/api/`.

### API Documentation (Swagger UI)

Once the application is running, you can access the interactive API documentation at:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

This interface allows you to explore all available endpoints, their request/response schemas, and even test them directly.

### Authentication

All protected endpoints require a JWT in the `Authorization` header (e.g., `Authorization: Bearer <YOUR_JWT_TOKEN>`).

#### **Register a New User**

*   **Endpoint:** `POST /api/auth/register`
*   **Description:** Register a new user with a specified role (`ADMIN`, `DOCTOR`, or `PATIENT`).
*   **Request Body (JSON):**
    ```json
    {
        "email": "admin@example.com",
        "password": "securepassword",
        "role": "ADMIN"
    }
    ```
*   **Example `curl` command:**
    ```bash
    curl -X POST http://localhost:8080/api/auth/register \
    -H "Content-Type: application/json" \
    -d '{"email": "admin@example.com", "password": "securepassword", "role": "ADMIN"}'
    ```

#### **Authenticate User**

*   **Endpoint:** `POST /api/auth/authenticate`
*   **Description:** Authenticate an existing user and receive a JWT.
*   **Request Body (JSON):**
    ```json
    {
        "email": "admin@example.com",
        "password": "securepassword"
    }
    ```
*   **Example `curl` command:**
    ```bash
    curl -X POST http://localhost:8080/api/auth/authenticate \
    -H "Content-Type: application/json" \
    -d '{"email": "admin@example.com", "password": "securepassword"}'
    ```
    **Response:**
    ```json
    {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIiwiZXhwIjoxNjk5NzY1MjAwLCJpYXQiOjE2OTk3NjE2MDB9...."
    }
    ```
    Use this `token` for subsequent authenticated requests.

### Core Endpoints (Examples)

Below are examples of key endpoints. Refer to the Swagger UI for a complete and up-to-date list.

#### **Patient Management (`/api/patients`)**

*   **Create Patient (ADMIN only):**
    *   `POST /api/patients`
    *   Request Body: `PatientRequest` DTO
*   **Get Patient by ID (ADMIN, DOCTOR, PATIENT - for self):**
    *   `GET /api/patients/{id}`
*   **Get All Patients (ADMIN, DOCTOR only):**
    *   `GET /api/patients?page=0&size=10`
*   **Search Patients (ADMIN, DOCTOR only):**
    *   `GET /api/patients/search?query=John&page=0&size=10`
*   **Update Patient (ADMIN only):**
    *   `PUT /api/patients/{id}`
    *   Request Body: `PatientRequest` DTO
*   **Delete Patient (ADMIN only):**
    *   `DELETE /api/patients/{id}`

#### **Doctor Management (`/api/doctors`)**

*   **Create Doctor (ADMIN only):**
    *   `POST /api/doctors`
    *   Request Body: `DoctorRequest` DTO
*   **Get Doctor by ID (ADMIN, DOCTOR, PATIENT):**
    *   `GET /api/doctors/{id}`
*   **Get All Doctors (ADMIN, DOCTOR, PATIENT):**
    *   `GET /api/doctors?page=0&size=10`
*   **Search Doctors (ADMIN, DOCTOR, PATIENT):**
    *   `GET /api/doctors/search?query=Cardiology&page=0&size=10`
*   **Update Doctor (ADMIN only):**
    *   `PUT /api/doctors/{id}`
    *   Request Body: `DoctorRequest` DTO
*   **Delete Doctor (ADMIN only):**
    *   `DELETE /api/doctors/{id}`

#### **Appointment Management (`/api/appointments`)**

*   **Create Appointment (ADMIN, PATIENT):**
    *   `POST /api/appointments`
    *   Request Body: `AppointmentRequest` DTO
*   **Get Appointment by ID (ADMIN, DOCTOR, PATIENT):**
    *   `GET /api/appointments/{id}`
*   **Get All Appointments (ADMIN only):**
    *   `GET /api/appointments?page=0&size=10`
*   **Get Appointments by Patient (ADMIN, DOCTOR, PATIENT):**
    *   `GET /api/appointments/patient/{patientId}?page=0&size=10`
*   **Get Appointments by Doctor (ADMIN, DOCTOR, PATIENT):**
    *   `GET /api/appointments/doctor/{doctorId}?page=0&size=10`
*   **Update Appointment (ADMIN, PATIENT, DOCTOR):**
    *   `PUT /api/appointments/{id}`
    *   Request Body: `AppointmentRequest` DTO
*   **Delete Appointment (ADMIN only):**
    *   `DELETE /api/appointments/{id}`

## 🛠️ Technologies Used

*   **Spring Boot:** Framework for building robust, production-ready Spring applications.
*   **Spring Security:** For authentication and authorization (JWT-based).
*   **Spring Data JPA:** For database interaction and ORM.
*   **Hibernate:** JPA implementation.
*   **MySQL:** Relational database.
*   **Lombok:** Reduces boilerplate code (getters, setters, constructors).
*   **JJWT:** Java JWT library for creating and verifying JSON Web Tokens.
*   **SpringDoc OpenAPI UI:** For automatic generation of API documentation (Swagger UI).
*   **Maven:** Build automation tool.
*   **Jakarta Validation:** For declarative data validation.

## 🤝 Contributing

Contributions are welcome! If you have suggestions for improvements, new features, or bug fixes, please follow these steps:

1.  **Fork** the repository.
2.  **Create** a new branch (`git checkout -b feature/YourFeatureName` or `bugfix/FixDescription`).
3.  **Implement** your changes.
4.  **Write** clear, concise commit messages.
5.  **Push** your branch to your fork.
6.  **Open** a Pull Request (PR) to the `main` branch of this repository, describing your changes in detail.

Please ensure your code adheres to the existing coding style and passes all tests.

## 📞 Support

If you encounter any issues or have questions, please open an issue on the GitHub repository.