package jeff.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import jeff.apigateway.handler.MyRestTemplateErrorHandlerForPingApi;


/**
 * 當用{@link RestTemplate}向各微服務發出請求時，
 * 獲得非200狀態碼的回應時拋出。
 * 
 * @see {@link MyRestTemplateErrorHandlerForPingApi}
 * */
public class RestTemplateErrorResException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private HttpStatus code = HttpStatus.BAD_GATEWAY;
	
	private String errMsg = null;

	public RestTemplateErrorResException(Throwable cause) {
		super(cause);
	}

	public RestTemplateErrorResException(String msg) {
		super(msg);
	}
	
	public RestTemplateErrorResException(HttpStatus status,String msg) {
		this.code = status;
		this.errMsg = msg;
	}
	
	public HttpStatus getCode() {
		return code;
	}

	public String getErrMsg() {
		return errMsg;
	}
	
}
