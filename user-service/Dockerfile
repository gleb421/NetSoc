# 🛠 Стадия сборки
FROM gradle:8.5-jdk21 AS builder

# Перейдём в корень проекта
WORKDIR /app

# Копируем весь монорепозиторий (чтобы были settings.gradle и другие модули)
COPY . .

# Собираем только user-service
RUN ./gradlew :user-service:bootJar --no-daemon

# 🧊 Финальный образ
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=builder /app/user-service/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
