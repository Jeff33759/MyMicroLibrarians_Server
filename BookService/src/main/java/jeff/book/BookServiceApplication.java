package jeff.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 提供館藏CRUD的RESTful服務。
 * 
 * @framework Spring Boot 2.7.0
 * @runtime at least JRE-17
 * @port default to run on port 8082
 * @author Jeff Huang
 * */
@SpringBootApplication
@EnableScheduling
public class BookServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);
	}

}
