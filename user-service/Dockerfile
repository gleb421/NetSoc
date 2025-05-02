# 🛠 Стадия сборки
FROM gradle:8.5-jdk21 AS builder

# Создаем директорию сборки
WORKDIR /app

# Копируем сам сервис
COPY . .
# Собираем конкретный модуль
RUN gradle bootJar --no-daemon

# 🧊 Финальный образ
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=builder /app/user-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
