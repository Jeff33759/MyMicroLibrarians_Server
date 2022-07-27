package jeff.book2.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import jeff.book2.handler.MyRestTemplateErrorHandler;

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
	 * 向gateway發送請求用。</p>
	 * 只要不要在{@link RestTemplateBuilder #build}之後，又呼叫set方法改變
	 * {@link RestTemplate}的參數(例如改成不同的ErrorHandler等等...)，
	 * 那就為執行緒安全，因此註冊為單例。</p>
	 * 
	 * {@link RestTemplate}在底層會為每個請求分配執行緒，
	 * 雖為執行緒安全，但會阻塞執行緒，所以要設定超時，
	 * 免得單個執行緒被一直等待回應的{@link RestTemplate}卡死、阻塞。</p>
	 * 非同步處理關鍵字:WebClient
	 * */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
//					連線超時時間
					.setConnectTimeout(Duration.ofSeconds(2)) 
//					資料傳輸超時時間(連線成功，但對方服務端處理太慢時)
					.setReadTimeout(Duration.ofSeconds(2))
					.errorHandler(new MyRestTemplateErrorHandler())
					.build();
    }
	
}
