# Build stage
FROM maven:3.9.11-eclipse-temurin-17 AS build
COPY . /plantix
WORKDIR /plantix

RUN mvn clean package -DskipTests

# Final stage
FROM eclipse-temurin:17-jre
WORKDIR /plantix
COPY --from=build /plantix/target/plantix-0.0.1-SNAPSHOT.jar plantix.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "plantix.jar"]