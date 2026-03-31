package com.demo.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Simple REST controller for CI/CD demo showcase.
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * GET /api/hello
     * Basic hello world response.
     */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of(
            "message", "Hello, Armaan!!",
            "status",  "success"
        );
    }

    /**
     * GET /api/health
     * Health-check endpoint — useful for Tomcat / load balancer checks.
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status",    "UP",
            "timestamp", LocalDateTime.now()
                             .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            "app",       "hello-world",
            "version",   "1.0.0"
        );
    }

    /**
     * GET /api/info
     * Returns basic app info — handy for verifying the deployed build.
     */
    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of(
            "application",   "Hello World - CI/CD Demo",
            "description",   "Spring Boot WAR deployment showcase",
            "java_version",  System.getProperty("java.version"),
            "server",        "Apache Tomcat"
        );
    }
}
