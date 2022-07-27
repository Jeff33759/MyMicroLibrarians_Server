package jeff.book2.service;


/**
 * 與Gateway之間的溝通，例如心跳或註冊...
 * */
public interface HandshakeService {
	
	/**
	 * 向Gateway註冊。
	 * */
	public void registerWithTheGateway();

	/**
	 * 向Gateway發送心跳訊號。
	 * */
	public void sendHeartbeatToGateway();

}
