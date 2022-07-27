package jeff.authentication.service;

import org.springframework.web.client.RestTemplate;

import jeff.authentication.handler.MyRestTemplateErrorHandler;


/**
 * 與Gateway的交握，例如心跳或註冊...
 * 
 * @author Jeff Huang
 * */
public interface HandshakeService {

	/**
	 * 向Gateway註冊。
	 * 
	 * @exception RestTemplateErrorResException
	 * 			  若{@link RestTemplate}向Gateway要資料的途中，
	 * 			  或者要到資料後將密鑰反序列化的過程中遭遇例外。
	 * @see {@link MyRestTemplateErrorHandler}
	 * */
	public void registerWithTheGateway();
	
	
	/**
	 * 向Gateway發送心跳訊號。
	 * */
	public void sendHeartbeatToGateway();
	
}
