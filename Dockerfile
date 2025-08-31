# Use a base image with JDK installed
FROM openjdk:17-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory inside the container
WORKDIR /app

# Copy the Maven build file
COPY pom.xml .

# Download Maven dependencies
RUN mvn dependency:go-offline -B

# Copy the entire project source
COPY src ./src

# Package the application using Maven
RUN mvn clean package -DskipTests

# Use a smaller base image for the runtime
FROM openjdk:17-jdk-slim

# Set working directory for runtime container
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port (adjust if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
