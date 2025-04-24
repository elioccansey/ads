# --- STAGE 1: Build, test, and package ---
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

ARG APP_VERSION=1.0.0
COPY pom.xml .
RUN mvn versions:set -DnewVersion=${APP_VERSION} -DgenerateBackupPoms=false && \
    mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime image ---
FROM eclipse-temurin:21-jdk
WORKDIR /app

ARG APP_VERSION=1.0.0
ARG APP_PORT=8080

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/ads \
    SPRING_DATASOURCE_USERNAME=username \
    SPRING_DATASOURCE_PASSWORD=password \
    SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    SERVER_PORT=${APP_PORT}

COPY --from=build /app/target/ads-backend-${APP_VERSION}.jar app.jar

EXPOSE ${APP_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
