#!/bin/bash

# Ensure script fails on any error
set -e

# Print environment information
echo "Current Directory: $(pwd)"

# Check for Java and Maven
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Installing Java 17..."
    apt-get update
    apt-get install -y openjdk-17-jdk
fi

if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Installing Maven..."
    apt-get update
    apt-get install -y maven
fi

# Verify Java and Maven installations
java -version
mvn --version

# Generate Maven Wrapper if not exists
if [ ! -f "./mvnw" ]; then
    mvn wrapper:wrapper
    chmod +x ./mvnw
fi

# Build the application
echo "Building Quarkus Application..."
./mvnw clean package -DskipTests

# Find and display JAR file
JAR_FILE=$(find . -name "*-runner.jar")
if [ -z "$JAR_FILE" ]; then
    echo "ERROR: No JAR file found!"
    exit 1
else
    echo "JAR file found: $JAR_FILE"
fi