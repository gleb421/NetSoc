package org.example.userservice.util;

public class JwtUtils {
    // Пример утилиты
    public static boolean isValid(String token) {
        // Тестовая заглушка
        return token != null && token.startsWith("Bearer ");
    }
}
