# Use the official Java 21 runtime
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file from the target folder
COPY target/*.jar app.jar

# Expose the web port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]