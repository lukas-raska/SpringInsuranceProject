package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hlavní třída sloužící pro spuštění Spring Boot aplikace
 */
@SpringBootApplication
@EnableJpaRepositories
public class ApplicationMain {

    /**
     * Main metoda pro spuštění aplikace
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
