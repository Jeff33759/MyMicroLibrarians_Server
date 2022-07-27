package jeff.apigateway.service;

import jeff.apigateway.model.dto.send.RegistrationDataForAuthN;
import jeff.apigateway.model.dto.send.RegistrationDataForAuthZ;

public interface RegistrationService {

	/**
	 * bookServer註冊邏輯。</p>
	 * 不對重複註冊的情況做判斷篩選，因為如果這裡明明註冊過了(bookParam賦過值)，
	 * 但微服務又送了一次註冊請求，代表微服務那邊沒接收到這邊發過去的資料(也許
	 * 因為超時或是資料丟失造成)，所以再跑一次註冊流程，把該發的東西都發給微服務，
	 * 以免微服務缺失必要資料而跑不起來。
	 * */
	public void registerBookServer();

	/**
	 * bookServer2註冊邏輯。
	 * */
	public void registerBookServer2();
	
	/**
	 * AuthenticationServer註冊邏輯。</p>
	 * 不對重複註冊的情況做判斷篩選，理由同{@link #registerBookServer}。
	 * */
	public RegistrationDataForAuthN registerAuthNServer();
	
	
	/**
	 * AuthorizationServer註冊邏輯。</p>
	 * 不對重複註冊的情況做判斷篩選，理由同{@link #registerBookServer}
	 * */
	public RegistrationDataForAuthZ registerAuthZServer();
	
	
}
