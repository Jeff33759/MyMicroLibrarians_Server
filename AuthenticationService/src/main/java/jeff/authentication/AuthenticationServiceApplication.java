package jeff.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 提供登入認證與生成JWTs的RESTful服務。
 * 
 * @framework Spring Boot 2.7.0
 * @runtime at least JRE-17
 * @port default to run on port 8081
 * @author Jeff Huang
 * */
@SpringBootApplication
@EnableScheduling
public class AuthenticationServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

}
