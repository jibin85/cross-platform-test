FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the JAR file
COPY quarkus-run.jar /app/

# Verify the JAR file exists after copying (for debugging)
RUN ls -la /app/

EXPOSE 7453

# Run the application
CMD ["java", "-Dquarkus.http.host=0.0.0.0", "-Dquarkus.http.port=7453", "-jar", "quarkus-run.jar"]