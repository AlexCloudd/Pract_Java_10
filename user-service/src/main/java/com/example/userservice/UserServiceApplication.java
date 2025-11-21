package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.net.URI;

@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {

    public static void main(String[] args) {
        // Настройка базы данных из переменных окружения Railway
        configureDatabaseFromEnvironment();
        
        SpringApplication.run(UserServiceApplication.class, args);
    }
    
    private static void configureDatabaseFromEnvironment() {
        // Приоритет 1: Проверяем SPRING_DATASOURCE_URL (рекомендуемый способ для Railway)
        String springUrl = System.getenv("SPRING_DATASOURCE_URL");
        if (springUrl != null && !springUrl.isEmpty()) {
            System.setProperty("spring.datasource.url", springUrl);
            String username = System.getenv("SPRING_DATASOURCE_USERNAME");
            String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
            if (username != null && !username.isEmpty()) {
                System.setProperty("spring.datasource.username", username);
            }
            if (password != null && !password.isEmpty()) {
                System.setProperty("spring.datasource.password", password);
            }
            System.out.println("========================================");
            System.out.println("Using SPRING_DATASOURCE_URL from environment");
            System.out.println("  URL: " + springUrl);
            System.out.println("========================================");
            return;
        }
        
        // Приоритет 2: Проверяем DATABASE_URL (Railway часто предоставляет его)
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
        
        // Приоритет 3: Используем стандартные переменные окружения Railway
        // Если они не установлены, Spring Boot использует значения по умолчанию из application.yml
        System.out.println("========================================");
        System.out.println("Using default database configuration from application.yml");
        System.out.println("  (Variables: PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD)");
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
