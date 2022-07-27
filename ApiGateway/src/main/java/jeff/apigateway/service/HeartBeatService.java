package jeff.apigateway.service;

import jeff.apigateway.config.ScheduleConfig;

/**
 * 以定期任務的方式來監聽各微服務還有無心跳。
 * 
 * @author Jeff Huang
 * @see {@link ScheduleConfig}
 * */
public interface HeartBeatService {

	/**
	 * BookServer的心跳設置為True。
	 * */
	public void bookServerHeartBeat();

	/**
	 * BookServer2的心跳設置為True。
	 * */
	public void bookServer2HeartBeat();
	
	/**
	 * AuthenticationServer的心跳設置為True。
	 * */
	public void authNServerHeartBeat();
	
	/**
	 * AuthorizationServer的心跳設置為True。
	 * */
	public void authZServerHeartBeat();

	
	
	/**
	 * 監聽BookServer的心跳，設置Server服務狀態。
	 * */
	public void monitorBookServerHeartBeat();

	/**
	 * 監聽BookServer2的心跳，設置Server服務狀態。
	 * */
	public void monitorBookServer2HeartBeat();
	
	
	/**
	 * 監聽AuthenticationServer的心跳，設置Server服務狀態。
	 * */
	public void monitorAuthNServerHeartBeat();
	
	/**
	 * 監聽AuthorizationServer的心跳，設置Server服務狀態。
	 * */
	public void monitorAuthZServerHeartBeat();
	
	
}
