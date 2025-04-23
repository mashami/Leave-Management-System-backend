# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the Gradle files first (if using Gradle)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Build the application
RUN ./gradlew build -x test

# Create a new stage for running the application
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=0 /app/build/libs/*.jar app.jar

# Expose the correct port the app runs on
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
