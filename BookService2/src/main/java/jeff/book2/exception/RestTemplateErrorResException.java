package jeff.book2.exception;

import org.springframework.web.client.RestTemplate;


/**
 * 當用{@link RestTemplate}向gateway提出心跳或註冊請求時，
 * 獲得非200狀態碼的回應時拋出。
 * 
 * @see {@link MyRestTemplateErrorHandler}
 * */
public class RestTemplateErrorResException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RestTemplateErrorResException(Throwable cause) {
		super(cause);
	}

	public RestTemplateErrorResException(String msg) {
		super(msg);
	}
	
}
