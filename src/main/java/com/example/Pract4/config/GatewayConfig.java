package com.example.Pract4.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("gateway")
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("lb://user-service"))
                .route("music-service", r -> r
                        .path("/api/music/**")
                        .uri("lb://music-service"))
                .route("playlist-service", r -> r
                        .path("/api/playlists/**")
                        .uri("lb://playlist-service"))
                .route("web-ui", r -> r
                        .path("/**")
                        .uri("http://localhost:8080"))
                .build();
    }
}
