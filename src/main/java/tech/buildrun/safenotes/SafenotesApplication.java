package tech.buildrun.safenotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.buildrun.safenotes.config.JwtConfig;

@EnableConfigurationProperties(JwtConfig.class)
@SpringBootApplication
public class SafenotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafenotesApplication.class, args);
	}

}
