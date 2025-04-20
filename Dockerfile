FROM maven:latest as build
WORKDIR /build
ARG APP_VERSION=1.0.0
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DappVersion=${APP_VERSION}



FROM amazoncorretto:23-alpine
WORKDIR /app/src
COPY target/ads-*.jar /app
