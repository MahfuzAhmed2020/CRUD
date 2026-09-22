# Spring Boot CRUD Product API
This procect could be run with mySQL and DOCKER DESKTOP both

to run with mySQL: mvn spring-boot:run

to run with docker desktop: docker-compose up -d                            

===========================================

To run mysql with command prompt AFTER running the docker image: docker exec -it mysql-container mysql -u root -p


===============swagger=================

http://localhost:8080/swagger-ui/index.html

============================

A simple Spring Boot REST API that demonstrates CRUD (Create, Read, Update, Delete) operations using Spring Boot, Spring Data JPA, and an H2/MySQL database.

---
## To create docker image
# ============================================================
# DOCKER IMAGE BUILD, RUN, PUSH AND NETWORK COMMANDS
# ============================================================

# 1. Build the Spring Boot application
# This cleans the previous Maven build files and creates
# a new executable JAR file inside the target/ directory.
mvn clean package


# 2. Display currently running Docker containers
# This shows containers that are currently running.
docker ps


# 3. Stop the existing CRUD application container
# This stops the container named "crud-app".
docker stop crud-app


# 4. Remove the stopped CRUD application container
# This deletes the container named "crud-app".
# It does NOT delete the Docker image.
docker rm crud-app


# 5. Display all Docker containers
# The -a option shows both running and stopped containers.
docker ps -a


# 6. Display currently running Docker containers again
# Useful to confirm that the old CRUD container is stopped/removed.
docker ps


# 7. Build a new Docker image
# "." means use the Dockerfile in the current directory.
# -t assigns the image name and tag.
docker build -t mahfuzdocker20/crud-app:latest .


# 8. Display all Docker images
# This allows you to verify that the new CRUD image was created.
docker images


# 9. Log in to Docker Hub
# Enter your Docker Hub username and password/access token
# when prompted.
docker login


# 10. Push the Docker image to Docker Hub
# This uploads your local image to the Docker Hub repository.
docker push mahfuzdocker20/crud-app:latest


# 11. Start the application using Docker Compose
# -d means run the containers in detached/background mode.
# Docker Compose reads the docker-compose.yml file.
docker-compose up -d


# 12. Display all Docker networks
# This shows the Docker networks available on your machine.
docker network ls


# 13. Stop ALL currently running Docker containers
# docker ps -q returns only the container IDs.
# The IDs are passed to "docker stop".
#
# WARNING: This stops EVERY running Docker container,
# not only your CRUD application.
docker stop $(docker ps -q)
=================================================================
# Build the Java application
mvn clean package

# Build the Docker image
docker build -t mahfuzdocker20/crud-app:latest .

# Check that the image exists
docker images

# Login to Docker Hub
docker login

# Push the image to Docker Hub
docker push mahfuzdocker20/crud-app:latest

# Start containers using Docker Compose
docker-compose up -d

# Check running containers
docker ps

# Check Docker networks
docker network ls

## Features

✅ Hello World endpoint

✅ Query parameter handling

✅ Create Product

✅ Get All Products

✅ Search Products

✅ Update Product (PUT)

✅ Partial Update Product (PATCH)

✅ Delete Product by ID

✅ Delete Product by Name

✅ RESTful API Design

---

# Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Maven
- H2 Database / MySQL
- REST API

---

# Project Structure

```text
src
 └── main
     └── java
         └── com.example.CRUD
             ├── Controller
             │     └── ProductController.java
             ├── Model
             │     └── Product.java
             ├── Reposotory
             │     └── ProductReposotory.java
             └── CrudApplication.java
```

---

# Product Model

Example Product JSON:

```json
{
  "id": 1,
  "name": "Pasta",
  "price": 10.99
}
```

---

# Running the Application

## Clone Project

```bash
git clone https://github.com/your-repository/crud-project.git
```

## Navigate to Project

```bash
cd crud-project
```

## Run Application

```bash
mvn spring-boot:run
```

or

```bash
mvn clean package
java -jar target/*.jar
```

Application runs on:

```text
http://localhost:8080
```

---

# API Endpoints

---

## 1. Hello Endpoint

### Request

```http
GET /api/hello
```

### URL

```text
http://localhost:8080/api/hello
```

### Response

```text
Hello, World! And welcome to Spring Boot CRUD API.
```

---

## 2. Hello Details

### Request

```http
GET /api/hello/details
```

### Example

```text
http://localhost:8080/api/hello/details?name=mahfuz&product=pasta&id=5
```

### Response

```json
{
  "message": "hello there",
  "name": "MAHFUZ",
  "id": 5,
  "product": "PASTA"
}
```

---

## 3. Browser Endpoint

### Request

```http
GET /api/user/browser
```

### Example

```text
http://localhost:8080/api/user/browser?name=mahfuz&product=pasta&id=500
```

### Response

```text
hello there MAHFUZ (ID: 500). You are looking for: PASTA
```

---

# Product APIs

---

## 4. Create Product

### Request

```http
POST /api/products
```

### URL

```text
http://localhost:8080/api/products
```

### Request Body

```json
{
  "name": "Pasta",
  "price": 25.99
}
```

### Response

```json
{
  "id": 1,
  "name": "Pasta",
  "price": 25.99
}
```

### Status

```text
201 Created
```

---

## 5. Get All Products

### Request

```http
GET /api/products
```

### Response

```json
[
  {
    "id": 1,
    "name": "Pasta",
    "price": 25.99
  }
]
```

### Status

```text
200 OK
```

---

## 6. Search Products

### Request

```http
GET /api/products/search
```

### Examples

Search by ID

```text
http://localhost:8080/api/products/search?id=1
```

Search by Name

```text
http://localhost:8080/api/products/search?name=Pasta
```

Search by Price

```text
http://localhost:8080/api/products/search?price=25.99
```

Search using Multiple Parameters

```text
http://localhost:8080/api/products/search?id=1&name=Pasta&price=25.99
```

### Response

```json
[
  {
    "id": 1,
    "name": "Pasta",
    "price": 25.99
  }
]
```

---

## 7. Update Product

### Request

```http
PUT /api/products/{id}
```

### Example

```text
PUT http://localhost:8080/api/products/1
```

### Request Body

```json
{
  "name": "Pizza",
  "price": 40.00
}
```

### Response

```json
{
  "id": 1,
  "name": "Pizza",
  "price": 40.0
}
```

### Status

```text
200 OK
```

---

## 8. Partial Update Product

### Request

```http
PATCH /api/products/{id}
```

### Example

```text
PATCH http://localhost:8080/api/products/1
```

### Request Body

```json
{
  "price": 50
}
```

### Response

```json
{
  "message": "Product updated successfully",
  "oldValues": {
    "name": "Pizza",
    "price": 40.0
  },
  "newValues": {
    "name": "Pizza",
    "price": 50.0
  }
}
```

---

## 9. Delete Product by ID

### Request

```http
DELETE /api/products/{id}
```

### Example

```text
DELETE http://localhost:8080/api/products/1
```

### Status

```text
204 No Content
```

---

## 10. Delete Product by Name

### Request

```http
DELETE /api/products?name=Pasta
```

### Example

```text
http://localhost:8080/api/products?name=Pasta
```

### Status

```text
204 No Content
```

---

# Sample Postman Requests

## Create

```json
{
  "name": "Burger",
  "price": 15.99
}
```

## Update

```json
{
  "name": "Chicken Burger",
  "price": 18.99
}
```

## Patch

```json
{
  "price": 20
}
```

---

# Common HTTP Status Codes

| Status Code | Meaning |
|------------|----------|
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 404 | Not Found |
| 500 | Internal Server Error |

---

# Author

**Md Mahfuz**

Spring Boot CRUD REST API Demo Project


