FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN gradle dependencies --build-cache || true

COPY src ./src

RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY src/main/resources ./resources
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8020

ARG MONGO_URL
ENV MONGO_URL=${MONGO_URL}

ARG PROFILE
ENV PROFILE=${PROFILE}

ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=$PROFILE -jar app.jar"]