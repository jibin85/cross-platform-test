FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the Uber JAR (Fat JAR) instead of quarkus-run.jar
COPY target/*-runner.jar /app/app.jar

EXPOSE 7453

# Use the fat JAR as the entry point
CMD ["java", "-Dquarkus.http.host=0.0.0.0", "-Dquarkus.http.port=7453", "-jar", "/app/app.jar"]
