package com.rilvoo.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Executable composition root for example-project. Deliberately contains NO business logic:
 * reusable infrastructure arrives through each library's
 * {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports},
 * so this class scans only its own package. The application starts without any database,
 * Redis or SMTP server present.
 */
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
