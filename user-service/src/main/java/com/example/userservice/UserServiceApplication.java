package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {

    public static void main(String[] args) {
        // Логируем переменные окружения для отладки
        logEnvironmentVariables();
        
        SpringApplication.run(UserServiceApplication.class, args);
    }
    
    private static void logEnvironmentVariables() {
        System.out.println("========================================");
        System.out.println("Environment Variables Check (BEFORE Spring Boot starts):");
        String url = System.getenv("SPRING_DATASOURCE_URL");
        String username = System.getenv("SPRING_DATASOURCE_USERNAME");
        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
        
        System.out.println("  SPRING_DATASOURCE_URL: " + (url != null ? "SET (" + url + ")" : "NOT SET"));
        System.out.println("  SPRING_DATASOURCE_USERNAME: " + (username != null ? "SET (" + username + ")" : "NOT SET"));
        System.out.println("  SPRING_DATASOURCE_PASSWORD: " + (password != null ? "SET (***)" : "NOT SET"));
        System.out.println("  DATABASE_URL: " + (System.getenv("DATABASE_URL") != null ? "SET" : "NOT SET"));
        
        // Проверяем системные свойства
        System.out.println("\nSystem Properties:");
        System.out.println("  spring.datasource.url: " + System.getProperty("spring.datasource.url", "NOT SET"));
        System.out.println("  spring.datasource.username: " + System.getProperty("spring.datasource.username", "NOT SET"));
        System.out.println("========================================");
    }
}
