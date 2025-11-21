package com.example.Pract4;

import com.example.Pract4.config.ConfigServerWebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = "com.example.Pract4", exclude = {
    org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration.class
})
@EnableFeignClients
@Profile({"default", "microservice"})
public class Pract4Application {
    public static void main(String[] args) {
        SpringApplication.run(Pract4Application.class, args);
    }
}

@SpringBootApplication(scanBasePackages = "com.example.Pract4", exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration.class,
    org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration.class,
    org.springframework.cloud.gateway.config.GatewayAutoConfiguration.class,
    org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration.class
})
@EnableConfigServer
@Import({ServletWebServerFactoryAutoConfiguration.class, ConfigServerWebConfig.class})
@Profile("config")
class ConfigServerApplication {
    
    @Bean
    @Primary
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }
    
    public static void main(String[] args) {
        System.setProperty("spring.main.web-application-type", "servlet");
        new SpringApplicationBuilder(ConfigServerApplication.class)
            .web(WebApplicationType.SERVLET)
            .profiles("config")
            .run(args);
    }
}

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration.class,
    org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration.class
})
@Profile("api-gateway")
class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

@SpringBootApplication(scanBasePackages = "com.example.Pract4", exclude = {
    org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration.class
})
@Profile("microservice")
class MicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class,
    org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration.class
})
@EnableEurekaServer
@Profile("eureka")
class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}