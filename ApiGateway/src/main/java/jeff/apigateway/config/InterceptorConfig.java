package jeff.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jeff.apigateway.interceptor.BookInterceptor;

/**
 * 進行攔截器相關設置。
 * 
 * @author Jeff Huang
 * */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	@Autowired
	private BookInterceptor bookInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(bookInterceptor)
				.addPathPatterns("/book/**");
	}

}
