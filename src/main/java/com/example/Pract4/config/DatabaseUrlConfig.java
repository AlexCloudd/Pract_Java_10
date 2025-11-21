package com.example.Pract4.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * EnvironmentPostProcessor to convert Railway's DATABASE_URL format to Spring Boot JDBC format.
 * Railway provides DATABASE_URL in format: postgresql://user:password@host:port/database
 * Spring Boot needs: jdbc:postgresql://host:port/database
 * 
 * This processor runs early in the Spring Boot lifecycle, before datasource configuration.
 */
public class DatabaseUrlConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Get DATABASE_URL from environment (not from Spring properties, as it might not be processed yet)
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty() && !databaseUrl.startsWith("jdbc:")) {
            try {
                System.out.println("Found DATABASE_URL, converting to JDBC format...");
                
                // Parse Railway's DATABASE_URL format: postgresql://user:password@host:port/database
                URI uri = new URI(databaseUrl);
                
                String scheme = uri.getScheme();
                if (scheme != null && scheme.startsWith("postgres")) {
                    String host = uri.getHost();
                    int port = uri.getPort() != -1 ? uri.getPort() : 5432;
                    String path = uri.getPath();
                    String database = path != null && path.startsWith("/") ? path.substring(1) : path;
                    
                    // Extract user info
                    String userInfo = uri.getUserInfo();
                    String username = null;
                    String password = null;
                    if (userInfo != null) {
                        String[] parts = userInfo.split(":", 2);
                        username = parts[0];
                        if (parts.length > 1) {
                            password = parts[1];
                        }
                    }
                    
                    // Build JDBC URL
                    String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
                    
                    // Add query parameters if they exist
                    String query = uri.getQuery();
                    if (query != null && !query.isEmpty()) {
                        jdbcUrl += "?" + query;
                    }
                    
                    // Set properties - these will override any defaults
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("SPRING_DATASOURCE_URL", jdbcUrl);
                    System.out.println("Converted DATABASE_URL to JDBC format: " + jdbcUrl.replaceAll(":[^:@]+@", ":***@"));
                    
                    if (username != null) {
                        properties.put("SPRING_DATASOURCE_USERNAME", username);
                        System.out.println("Extracted username from DATABASE_URL");
                    }
                    if (password != null) {
                        properties.put("SPRING_DATASOURCE_PASSWORD", password);
                        System.out.println("Extracted password from DATABASE_URL");
                    }
                    
                    // Add as first property source to ensure it takes precedence
                    environment.getPropertySources().addFirst(
                        new MapPropertySource("databaseUrlConfig", properties)
                    );
                    
                    System.out.println("Successfully configured datasource from DATABASE_URL");
                } else {
                    System.out.println("DATABASE_URL scheme is not postgresql, skipping conversion");
                }
            } catch (Exception e) {
                System.err.println("Error parsing DATABASE_URL: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Log which environment variables are available
            System.out.println("DATABASE_URL not found or already in JDBC format");
            System.out.println("Checking Railway PostgreSQL variables:");
            System.out.println("  PGHOST: " + (System.getenv("PGHOST") != null ? System.getenv("PGHOST") : "not set"));
            System.out.println("  PGPORT: " + (System.getenv("PGPORT") != null ? System.getenv("PGPORT") : "not set"));
            System.out.println("  PGDATABASE: " + (System.getenv("PGDATABASE") != null ? System.getenv("PGDATABASE") : "not set"));
            System.out.println("  PGUSER: " + (System.getenv("PGUSER") != null ? System.getenv("PGUSER") : "not set"));
            System.out.println("  PGPASSWORD: " + (System.getenv("PGPASSWORD") != null ? "***" : "not set"));
        }
    }
}

