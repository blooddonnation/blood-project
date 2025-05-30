Description
This project is a microservices-based platform for managing blood donation activities. It leverages Spring Boot, Spring Cloud, Eureka Service Discovery, PostgreSQL, MongoDB, Redis, and Docker Compose for orchestration. The system is composed of several independent services, each responsible for a specific domain:
Eureka Server: Service registry for microservices discovery.
Auth Service: Handles user registration, authentication, and JWT-based security.
Donation History Service: Manages donation records and user points.
Request Post Service: Allows users to post and manage blood donation requests.
Microservice Banque: Manages blood bank operations and inventory.
Position Tracking Service: Tracks the location of users and donation centers.
PostgreSQL: Main relational database for user and transactional data.
MongoDB: NoSQL database for donation history.
Redis: In-memory data store for caching and token management.


How to Run

Build and Run the Dockerfile (for Maven Build)
First, build all Maven projects using the provided Dockerfile:

# Build the Docker image
```bash
docker build -t maven-builder .
```
# Run the Docker container
```bash
docker run maven-builder
```
This will compile and package all Java services so their JAR files are ready for the next step.

Next, use Docker Compose to build and start all services and dependencies:
```bash
docker-compose up --build
```
This will:
- Build Docker images for each microservice
- Start all services (including databases and Eureka)
- Set up the network so services can communicate

Access the Services
- Eureka Dashboard: [http://localhost:8761](http://localhost:8761)
- Auth Service: [http://localhost:8080](http://localhost:8080)
- Donation History Service: [http://localhost:8081](http://localhost:8081)
- Request Post Service: [http://localhost:8084](http://localhost:8084)
- Microservice Banque: [http://localhost:8083](http://localhost:8083)
- Position Tracking Service: [http://localhost:8082](http://localhost:8082)

Note:
If you encounter database errors, ensure the databases are created in PostgreSQL (see the database initialization section above).
