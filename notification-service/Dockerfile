# 🛠 Стадия сборки
FROM gradle:8.5-jdk21 AS builder

# Создаем рабочую директорию
WORKDIR /netSoc/notification-service

COPY . .

# Переходим в notification-service и собираем
WORKDIR /netSoc/notification-service
RUN gradle bootJar --no-daemon

# 🧊 Финальный минимальный образ
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /netSoc
COPY --from=builder /netSoc/notification-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
