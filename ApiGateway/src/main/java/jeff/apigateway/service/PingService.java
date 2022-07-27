package jeff.apigateway.service;

/**
 * 當定期任務偵測到有微服務尚未註冊，由gateway嘗試ping一下
 * 微服務，叫他趕快來註冊。
 * */
public interface PingService {

	/**
	 * 告訴BookServer快來註冊。
	 * */
	public void pingBookServer();

	/**
	 * 告訴BookServer2快來註冊。
	 * */
	public void pingBookServer2();

	/**
	 * 告訴AuthNServer快來註冊。
	 * */
	public void pingAuthNServer();
	
	/**
	 * 告訴AuthZServer快來註冊。
	 * */
	public void pingAuthZServer();
	
}
