FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN gradle dependencies --build-cache || true

COPY src ./src

RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8020

ARG MONGO_URL
ENV MONGO_URL=${MONGO_URL}

ARG PROFILE
ENV PROFILE=${PROFILE}

ARG AWS_ACCESS_KEY
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY}

ARG AWS_SECRET_KEY
ENV AWS_SECRET_KEY=${AWS_SECRET_KEY}

ARG AWS_REGION
ENV AWS_REGION=${AWS_REGION}

ARG AWS_S3_BUCKET_NAME
ENV AWS_S3_BUCKET_NAME=${AWS_S3_BUCKET_NAME}

ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=$PROFILE -jar app.jar"]