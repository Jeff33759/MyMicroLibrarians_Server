package jeff.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 解析AccessToken的服務端。
 * 
 * @framework Spring Boot 2.7.0
 * @runtime at least JRE-17
 * @port default to run on port 8083
 * @author Jeff Huang
 * */
@SpringBootApplication
@EnableScheduling
public class AuthorizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServiceApplication.class, args);
	}

}
