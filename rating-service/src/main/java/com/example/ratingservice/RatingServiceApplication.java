package com.example.ratingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.net.URI;

@SpringBootApplication
@EnableFeignClients
public class RatingServiceApplication {

    public static void main(String[] args) {
        // Настройка базы данных из переменных окружения Railway
        configureDatabaseFromEnvironment();
        
        SpringApplication.run(RatingServiceApplication.class, args);
    }
    
    private static void configureDatabaseFromEnvironment() {
        // Проверяем DATABASE_URL (Railway часто предоставляет его)
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("postgresql://")) {
            try {
                parseDatabaseUrl(databaseUrl);
                return;
            } catch (Exception e) {
                System.err.println("Failed to parse DATABASE_URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Используем стандартные переменные окружения Railway
        String host = System.getenv("DB_HOST");
        if (host == null || host.isEmpty()) host = System.getenv("PGHOST");
        if (host == null || host.isEmpty()) host = "localhost";
        
        String port = System.getenv("DB_PORT");
        if (port == null || port.isEmpty()) port = System.getenv("PGPORT");
        if (port == null || port.isEmpty()) port = "5432";
        
        String database = System.getenv("DB_NAME");
        if (database == null || database.isEmpty()) database = System.getenv("PGDATABASE");
        if (database == null || database.isEmpty()) database = "Java_pr";
        
        String username = System.getenv("DB_USERNAME");
        if (username == null || username.isEmpty()) username = System.getenv("PGUSER");
        if (username == null || username.isEmpty()) username = "postgres";
        
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isEmpty()) password = System.getenv("PGPASSWORD");
        if (password == null || password.isEmpty()) password = "postgres";
        
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
        
        // Устанавливаем системные свойства, которые Spring Boot будет использовать
        System.setProperty("spring.datasource.url", jdbcUrl);
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);
        
        // Логируем для отладки
        System.out.println("========================================");
        System.out.println("Database Configuration:");
        System.out.println("  Host: " + host);
        System.out.println("  Port: " + port);
        System.out.println("  Database: " + database);
        System.out.println("  Username: " + username);
        System.out.println("  JDBC URL: " + jdbcUrl);
        System.out.println("========================================");
    }
    
    private static void parseDatabaseUrl(String databaseUrl) throws Exception {
        // Формат: postgresql://user:password@host:port/database
        // Пример: postgresql://postgres:password@postgres.railway.internal:5432/railway
        
        System.out.println("Parsing DATABASE_URL: " + databaseUrl.replaceAll(":[^:@]+@", ":***@"));
        
        // Заменяем postgresql:// на postgres:// для правильного парсинга URI
        String normalizedUrl = databaseUrl.replace("postgresql://", "postgres://");
        URI dbUri = new URI(normalizedUrl);
        
        String userInfo = dbUri.getUserInfo();
        if (userInfo == null || userInfo.isEmpty()) {
            throw new IllegalArgumentException("DATABASE_URL does not contain user info");
        }
        
        String[] credentials = userInfo.split(":");
        String username = credentials[0];
        String password = credentials.length > 1 ? credentials[1] : "";
        
        String host = dbUri.getHost();
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("DATABASE_URL does not contain host");
        }
        
        int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
        String database = dbUri.getPath();
        if (database != null && database.startsWith("/")) {
            database = database.substring(1);
        }
        if (database == null || database.isEmpty()) {
            throw new IllegalArgumentException("DATABASE_URL does not contain database name");
        }
        
        // Формируем JDBC URL
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
        
        // Устанавливаем системные свойства ДО запуска Spring Boot
        System.setProperty("spring.datasource.url", jdbcUrl);
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);
        
        System.out.println("========================================");
        System.out.println("Database Configuration from DATABASE_URL:");
        System.out.println("  Host: " + host);
        System.out.println("  Port: " + port);
        System.out.println("  Database: " + database);
        System.out.println("  Username: " + username);
        System.out.println("  JDBC URL: " + jdbcUrl);
        System.out.println("========================================");
    }
}
