package io.bani.buddy_secret;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "io.bani")
public class BuddySecretApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuddySecretApplication.class, args);
	}

}
