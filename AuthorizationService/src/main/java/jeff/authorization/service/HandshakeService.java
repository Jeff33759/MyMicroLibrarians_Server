package jeff.authorization.service;

import org.springframework.web.client.RestTemplate;

import jeff.authorization.handler.MyRestTemplateErrorHandler;

/**
 * 與Gateway之間的溝通，例如心跳或註冊...
 * */
public interface HandshakeService {
	
	/**
	 * 向Gateway註冊。
	 * 
	 * @exception RestTemplateErrorResException
	 * 			  若{@link RestTemplate}向Gateway要資料的途中，
	 * 			  或者要到資料後將公鑰反序列化的過程中遭遇例外，則拋出。
	 * @see {@link MyRestTemplateErrorHandler}
	 * */
	public void registerWithTheGateway();

	/**
	 * 向Gateway發送心跳訊號。
	 * */
	public void sendHeartbeatToGateway();
	
}
