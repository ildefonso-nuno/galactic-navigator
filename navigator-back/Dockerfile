# Use Amazon Corretto JDK to build and run the Spring Boot application
FROM amazoncorretto:17 as build

ENV LANG C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto

# Set the working directory in the container
WORKDIR /workspace/app

# Copy the Maven configuration file and source code
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the application
RUN ./mvnw install

# Use Amazon Corretto JRE for running the application
FROM amazoncorretto:17

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /workspace/app/target/navigator-backend-0.0.1-SNAPSHOT.jar .

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "navigator-backend-0.0.1-SNAPSHOT.jar"]
