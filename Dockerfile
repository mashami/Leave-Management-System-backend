FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the pre-built JAR
COPY build/libs/*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]

sha256:338e1a7be3fa71895c07502ba9171425c5798c0dba32b657f61c8273fdd277d1