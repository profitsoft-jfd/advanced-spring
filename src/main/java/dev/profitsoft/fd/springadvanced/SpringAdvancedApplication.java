package dev.profitsoft.fd.springadvanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main Spring Boot application class demonstrating advanced features.
 * Includes caching, scheduling, AOP, and JPA.
 */
@EnableCaching
@SpringBootApplication
public class SpringAdvancedApplication {

  /**
   * Application entry point.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(SpringAdvancedApplication.class, args);
  }

}
