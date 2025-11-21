package com.example.Pract4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
public class DataSourceLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
            
            System.out.println("========================================");
            System.out.println("DATASOURCE CONFIGURATION (ACTUAL VALUES):");
            System.out.println("  URL from @Value: " + datasourceUrl);
            System.out.println("  Username from @Value: " + datasourceUsername);
            System.out.println("  Password from @Value: " + (datasourceUsername != null ? "***" : "NULL"));
            
            try {
                Connection connection = dataSource.getConnection();
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("  Actual JDBC URL: " + metaData.getURL());
                System.out.println("  Actual Username: " + metaData.getUserName());
                connection.close();
            } catch (Exception e) {
                System.err.println("  Error getting connection info: " + e.getMessage());
            }
            
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("Error: DataSource bean not found or not available: " + e.getMessage());
        }
    }
}

