package jeff.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 負責各微服務監控、派發TokenSign Key、URL轉發、權限管制、load balance...
 * 
 * @framework Spring Boot 2.7.0
 * @runtime at least JRE-17
 * @port default to run on port 8080
 * @author Jeff Huang
 * */
@SpringBootApplication
@EnableScheduling
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
