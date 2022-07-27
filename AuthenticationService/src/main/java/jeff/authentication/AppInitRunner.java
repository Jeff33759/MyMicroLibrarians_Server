package jeff.authentication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import jeff.authentication.service.AccountService;
import jeff.authentication.service.HandshakeService;
import jeff.authentication.exception.RestTemplateErrorResException;
import jeff.authentication.model.po.Account;
import jeff.authentication.serviceimpl.AccountServiceImpl;
import jeff.authentication.serviceimpl.HandshakeServiceImpl;


/**
 * 在SpringBoot啟動完成後，自動檢查資料庫及創建預設帳號。
 * 
 * @author Jeff Huang
 * */
@Component
public class AppInitRunner implements CommandLineRunner{
	
	private static final Logger log = 
			LoggerFactory.getLogger(AppInitRunner.class);

	private final AccountService accountService;
	
	private final HandshakeService hsService;
	
	
	@Autowired
	public AppInitRunner(AccountServiceImpl accountServiceImpl,
			HandshakeServiceImpl hsServiceImpl) {
		this.accountService = accountServiceImpl;
		this.hsService = hsServiceImpl;
	}


	/**
	 * 若帳號資料庫初始化失敗，代表資料庫可能發生問題，程式會停止。
	 * */
	@Override
	public void run(String... args) throws Exception {
		initAccountDatabase();
		registerWithGateway();
	}
	
	/**
	 * 初始化帳號資料庫。
	 * */
	private void initAccountDatabase() {
		log.info("Initializing database......");
		try {
			List<Account> initAccounts = accountService.initDB();
			log.info("DB Initialized successfully!");
			logInfoOfInitAccounts(initAccounts);
		}catch(Exception e) {
			log.error("DB Initialized failed.");
//			若帳號資料庫初始化失敗，就拋出例外，讓程式停止。
			throw e;
		}
	}

	/**
	 * log紀錄初始帳號的資訊。
	 * */
	private void logInfoOfInitAccounts(List<Account> accounts) {
		String template = "id: %s \npassowrd: %s \nRole: %s";
		accounts.stream().forEach(account-> log.info(
				"\n-----initAccountInfo-----\n " + 
					String.format(template, account.getId(),
							account.getId(), account.getAuth_role())));
	}
	
	/**
	 * 向gateway註冊。
	 * */
	private void registerWithGateway() {
		log.info("Trying to register with the gateway......");
		try {
			hsService.registerWithTheGateway();
			log.info("Registered with the gateway successfully!");
//		連接超時、等待回應超時、收到非200的回應，都進行一樣的處理
		}catch(ResourceAccessException | RestTemplateErrorResException e) {
			log.error("Registered with the gateway failed, "
					+ "will try again in a few seconds.");
		}
	}
	
}
