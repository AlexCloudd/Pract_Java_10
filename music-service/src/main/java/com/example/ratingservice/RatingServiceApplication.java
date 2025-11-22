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
        System.out.println("Environment Variables Check (BEFORE Spring Boot starts):");
        
        // Проверяем Railway PostgreSQL переменные
        String pghost = System.getenv("PGHOST");
        String pgport = System.getenv("PGPORT");
        String pgdatabase = System.getenv("PGDATABASE");
        String pguser = System.getenv("PGUSER");
        String pgpassword = System.getenv("PGPASSWORD");
        
        String url = System.getenv("SPRING_DATASOURCE_URL");
        String username = System.getenv("SPRING_DATASOURCE_USERNAME");
        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
        String databaseUrl = System.getenv("DATABASE_URL");
        
        System.out.println("Railway PostgreSQL Variables:");
        System.out.println("  PGHOST: " + (pghost != null ? pghost : "NOT SET"));
        System.out.println("  PGPORT: " + (pgport != null ? pgport : "NOT SET"));
        System.out.println("  PGDATABASE: " + (pgdatabase != null ? pgdatabase : "NOT SET"));
        System.out.println("  PGUSER: " + (pguser != null ? pguser : "NOT SET"));
        System.out.println("  PGPASSWORD: " + (pgpassword != null ? "SET (***)" : "NOT SET"));
        
        System.out.println("\nSpring Boot Variables:");
        System.out.println("  SPRING_DATASOURCE_URL: " + (url != null ? "SET (" + url + ")" : "NOT SET"));
        System.out.println("  SPRING_DATASOURCE_USERNAME: " + (username != null ? "SET (" + username + ")" : "NOT SET"));
        System.out.println("  SPRING_DATASOURCE_PASSWORD: " + (password != null ? "SET (***)" : "NOT SET"));
        System.out.println("  DATABASE_URL: " + (databaseUrl != null ? "SET" : "NOT SET"));
        
        // Если есть PGHOST и другие переменные, но нет SPRING_DATASOURCE_URL, конструируем URL
        if (url == null && pghost != null && pgport != null && pgdatabase != null) {
            String constructedUrl = "jdbc:postgresql://" + pghost + ":" + pgport + "/" + pgdatabase;
            System.setProperty("spring.datasource.url", constructedUrl);
            System.out.println("  ✓ Constructed spring.datasource.url from PGHOST/PGPORT/PGDATABASE: " + constructedUrl);
        } else if (url != null && !url.isEmpty()) {
            System.setProperty("spring.datasource.url", url);
            System.out.println("  ✓ Set spring.datasource.url from SPRING_DATASOURCE_URL");
        }
        
        if (username == null && pguser != null) {
            System.setProperty("spring.datasource.username", pguser);
            System.out.println("  ✓ Set spring.datasource.username from PGUSER");
        } else if (username != null && !username.isEmpty()) {
            System.setProperty("spring.datasource.username", username);
            System.out.println("  ✓ Set spring.datasource.username from SPRING_DATASOURCE_USERNAME");
        }
        
        if (password == null && pgpassword != null) {
            System.setProperty("spring.datasource.password", pgpassword);
            System.out.println("  ✓ Set spring.datasource.password from PGPASSWORD");
        } else if (password != null && !password.isEmpty()) {
            System.setProperty("spring.datasource.password", password);
            System.out.println("  ✓ Set spring.datasource.password from SPRING_DATASOURCE_PASSWORD");
        }
        
        // Проверяем системные свойства
        System.out.println("\nSystem Properties (after setting):");
        System.out.println("  spring.datasource.url: " + System.getProperty("spring.datasource.url", "NOT SET"));
        System.out.println("  spring.datasource.username: " + System.getProperty("spring.datasource.username", "NOT SET"));
        System.out.println("  spring.datasource.password: " + (System.getProperty("spring.datasource.password") != null ? "SET (***)" : "NOT SET"));
        System.out.println("========================================");
    }
}
