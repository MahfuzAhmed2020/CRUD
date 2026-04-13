#  https://www.youtube.com/watch?v=sjuexyPnzN8
# mvn clean package
# inside dockerfile directory, run:
# docker build .
# docker build -t mahfu/crud-app .
# docker images 
# docker run -p 8080:8080 mahfu/crud-app
# 
# docker run -p 8080:8080 mahfuzdocker20/crud-app

# cd C:\Users\mahfu\Desktop\CRUD
# docker-compose up -d

# =========================================
# Pro tip (very useful)

# If you ever want to run compose from another folder:
# docker-compose -f src/main/resources/docker-compose.yml up -d

# docker login 
# docker push mahfuzdocker20/crud-app

# Use Amazon Corretto (Java 22) as the base image
# This image already has Java installed, so we don’t need to install it manually
FROM amazoncorretto:17

# Add metadata (optional but good practice)
# Helps identify version of your image
LABEL version="1.0"

# Set working directory inside the container
# All commands below will run inside /app
WORKDIR /app

# Copy your built JAR file from local machine → container
# Left side: path on your computer
# Right side: path inside container
COPY target/CRUD-0.0.1-SNAPSHOT.jar CRUD.jar


# Tell Docker that this container uses port 8080
# NOTE: This does NOT expose it to host, just documentation for Docker
EXPOSE 8080

# Command that runs when container starts
# Runs your Spring Boot application
ENTRYPOINT ["java", "-jar", "CRUD.jar"]