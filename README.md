# Task Manager Backend

A Spring Boot backend application for managing users and tasks.

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- PostgreSQL
- Docker & Docker Compose
- Maven

## Features
- User registration & authentication (JWT)
- Role-based access control (ADMIN / USER)
- Task creation and assignment
- Secure password storage (BCrypt)
- RESTful APIs
- PostgreSQL database with Docker volume persistence

## Project Structure
- Controller layer
- Service layer
- Repository layer
- Entity (Model) layer
- Security (JWT, Filters, Config)

## How to Run (Docker)
1. Clone the repository
2. Create `.env` file (DB credentials)
3. Build and start containers
   ```bash
   docker-compose up --build