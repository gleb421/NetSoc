# ---- Стадия сборки ----
FROM gradle:9.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle :user-service:bootJar --no-daemon

# ---- Финальный образ ----
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/chat-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]