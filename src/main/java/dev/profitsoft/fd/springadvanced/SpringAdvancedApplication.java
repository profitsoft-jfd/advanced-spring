package dev.profitsoft.fd.springadvanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringAdvancedApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAdvancedApplication.class, args);
  }

}
