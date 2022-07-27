package jeff.apigateway.config;

import java.net.HttpURLConnection;
import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jeff.apigateway.filter.AccessTokenAuthorizationFilter;
import jeff.apigateway.handler.MyRestTemplateErrorHandlerForForwarding;
import jeff.apigateway.handler.MyRestTemplateErrorHandlerForPingApi;
import jeff.apigateway.service.PingService;


/**
 * 將一些元件，註冊為Spring Bean。
 * 
 * @author Jeff Huang
 * */
@Configuration
public class MyComponentConfig {
	
	/**
	 * Jackson的Json解析器，可以實現POJO與JSON字串的互轉。
	 * */
	@Bean
	public ObjectMapper objectMapperBean() {
		return new ObjectMapper();
	}
	
	/**
	 * 用於ping請求的RestTemplate。
	 * 
	 * @see {@link PingService}
	 * */
    @Bean(name="pingRestTemplateBean")
    public RestTemplate restTemplateForPing(RestTemplateBuilder builder) {
    	RestTemplate template = 
    			builder
//					連線超時時間
    				.setConnectTimeout(Duration.ofSeconds(2)) 
//					資料傳輸超時時間(連線成功，但對方服務端處理太慢時)
    				.setReadTimeout(Duration.ofSeconds(4))
    				.build();
    	template.setErrorHandler(new MyRestTemplateErrorHandlerForPingApi());
        return template;
    }

    /**
     * 用於各微服務業物邏輯的請求，因為針對回傳錯誤碼的處理和{@link restTemplateForPing}不一樣，
     * ，並且底層發出請求的HTTP client也不同，所以需要再註冊一個。</p>
     * 
     * 默認情況下，RestTemplate使用標準JDK{@link HttpURLConnection}做為客戶端去發出請求，
     * 但{@link HttpURLConnection}不支持PATCH方法，所以這裡不使用{@link SimpleClientHttpRequestFactory}
     * 去配置，而是通過{@link HttpComponentsClientHttpRequestFactory}配置其他的底層實作當成Http客戶端。
     * 記得Maven先引入org.apache.httpcomponents函式庫。
     * */
    @Bean(name="businessRestTemplateBean")
    public RestTemplate restTemplateForBusinessLogic(RestTemplateBuilder builder) {
    	HttpComponentsClientHttpRequestFactory factory = 
    			new HttpComponentsClientHttpRequestFactory();
//		連線超時時間
    	factory.setConnectTimeout(2000);
//		資料傳輸超時時間(連線成功，但對方服務端處理太慢時)
    	factory.setReadTimeout(2000);
    	RestTemplate template = new RestTemplate();
    	template.setErrorHandler(new MyRestTemplateErrorHandlerForForwarding());
    	template.setRequestFactory(factory);
    	return template;
    }
    
    
	/**
	 * 用於向AuthZ送解析AccessToken請求的RestTemplate，因為使用量大，所以縮短等候時間，
	 * ，等不到就趕緊釋放資源，以免阻塞。
	 * 
	 * @see {@link AccessTokenAuthorizationFilter}
	 * */
    @Bean(name="parseATRestTemplateBean")
    public RestTemplate restTemplateForParsingAT(RestTemplateBuilder builder) {
    	RestTemplate template = 
    			builder
//					連線超時時間
    				.setConnectTimeout(Duration.ofSeconds(1)) 
//					資料傳輸超時時間(連線成功，但對方服務端處理太慢時)
    				.setReadTimeout(Duration.ofSeconds(1))
    				.build();
    	template.setErrorHandler(new MyRestTemplateErrorHandlerForForwarding());
        return template;
    }


}
