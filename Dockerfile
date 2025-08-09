# Start with a base image containing Java runtime
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven build file and code
COPY . /app

# Package the application using Maven
RUN ./mvnw clean package -DskipTests

# Run the Spring Boot application
CMD ["java", "-jar", "target/yumtea-0.0.1-SNAPSHOT.jar"]
# Replace yumtea-0.0.1-SNAPSHOT.jar with your actual .jar name after build.