# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src/ src/

# Grant execute permission on the Gradle wrapper
RUN chmod +x gradlew

# Build the application using the Gradle wrapper
RUN ./gradlew build

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "build/libs/RePlay-0.0.1-SNAPSHOT.jar"]
