FROM gradle:8.5-jdk21 AS builder
WORKDIR /netSoc
COPY . .

# 👇 Сборка из корня, с указанием подмодуля
RUN ./gradlew :user-service:bootJar --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /netSoc/user-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
