package com.example.ratingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RatingServiceApplication {

    public static void main(String[] args) {
        // Логируем переменные окружения для отладки
        logEnvironmentVariables();
        
        SpringApplication.run(RatingServiceApplication.class, args);
    }
    
    private static void logEnvironmentVariables() {
        System.out.println("========================================");
        System.out.println("Environment Variables Check:");
        System.out.println("  SPRING_DATASOURCE_URL: " + (System.getenv("SPRING_DATASOURCE_URL") != null ? "SET" : "NOT SET"));
        System.out.println("  SPRING_DATASOURCE_USERNAME: " + (System.getenv("SPRING_DATASOURCE_USERNAME") != null ? "SET" : "NOT SET"));
        System.out.println("  SPRING_DATASOURCE_PASSWORD: " + (System.getenv("SPRING_DATASOURCE_PASSWORD") != null ? "SET" : "NOT SET"));
        System.out.println("  DATABASE_URL: " + (System.getenv("DATABASE_URL") != null ? "SET" : "NOT SET"));
        System.out.println("========================================");
    }
}
