# 🛠 Стадия сборки
FROM gradle:8.5-jdk21 AS builder

WORKDIR /netSoc

# Копируем всю монорепу в контейнер
COPY . .

# Сборка из корня — модуль notification-service будет собран вместе с зависимостями
RUN gradle :notification-service:bootJar --no-daemon

# 🧊 Финальный минимальный образ
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /netSoc
COPY --from=builder /netSoc/notification-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
