package com.demo.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main application class.
 *
 * Extends SpringBootServletInitializer — this is REQUIRED for WAR deployment
 * on an external Tomcat server. It replaces the web.xml and allows Tomcat
 * to bootstrap the Spring context on startup.
 */
@SpringBootApplication
public class HelloWorldApplication extends SpringBootServletInitializer {

    /**
     * Entry point when running as a standalone JAR (local dev).
     */
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

    /**
     * Entry point when deployed as a WAR on an external Tomcat.
     * Tomcat calls configure() to initialise the application.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HelloWorldApplication.class);
    }
}
