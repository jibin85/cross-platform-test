FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*-runner.jar app.jar
EXPOSE 7453
ENTRYPOINT ["java", "-jar", "app.jar"]