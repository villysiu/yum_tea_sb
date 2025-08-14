# ---- Step 1: Build the application ----
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory inside container
WORKDIR /app

# Copy only the pom.xml and download dependencies (to cache better)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project (after dependencies cached)
COPY . .

# Package the application (skip tests, already configured in pom)
RUN mvn clean package

# ---- Step 2: Run the application ----
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your app uses
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
