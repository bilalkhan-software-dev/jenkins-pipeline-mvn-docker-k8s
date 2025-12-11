#  OpenJDK 25 as base image
FROM openjdk:25-jdk-slim

# metadata
LABEL authors="Bilal Khan"
LABEL description="Spring Boot Application"
LABEL version="1.0"

# Set working directory inside container
WORKDIR /app

COPY target/jenkins-demo.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]