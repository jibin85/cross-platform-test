FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/*-runner.jar /app/app.jar
EXPOSE 7453
ENTRYPOINT ["java", "-jar", "/app/app.jar"]