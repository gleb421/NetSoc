# 🛠 Стадия сборки
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app
COPY . .

RUN gradle bootJar --no-daemon
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /netSoc
COPY --from=builder /netSoc/chat-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
