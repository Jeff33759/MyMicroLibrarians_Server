package jeff.apigateway;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.service.PingService;
import jeff.apigateway.serviceimpl.PingServiceImpl;



/**
 * 在SpringBoot啟動完成後，主動去ping各微服務，讓他們註冊。
 * 
 * @author Jeff Huang
 * */
@Component
public class AppInitRunner implements CommandLineRunner{

	private static final Logger log = 
			LoggerFactory.getLogger(AppInitRunner.class);
	
	@Autowired
	private SystemParam.Common param;
	
	private final PingService pingService;
	
	@Autowired
	public AppInitRunner(PingServiceImpl pingServiceImpl) {
		this.pingService = pingServiceImpl;
	}

	/**
	 * SpringBoot啟動成功之初，主動ping各已知微服務，其中
	 * 強依賴的兩個微服務為AuthN和AuthZ，若該兩個微服務
	 * 其中一個註冊失敗，那gateway就不提供對外服務。
	 * */
	@Override
	public void run(String... args) throws Exception{
		boolean flag1,flag2;
		log.info("Searching for other microservices......");
		flag1 = pingAuthNServer();
		flag2 = pingAuthZServer();
		pingBookServer();

		if(flag1 && flag2) {
			param.GW_IN_SERVICE = true;
		}else {
			log.error("The gateway will not provide external services "
					+ "until both AuthN and AuthZ are successfully registered.");
		}
		
	}
	
	/**
	 * ping一下AuthN，叫他來註冊。
	 * */
	private boolean pingAuthNServer() {
		try {
			pingService.pingAuthNServer();
			log.info("Authentication server registration successful!");
			return true;
		}catch(Exception e) {
			log.error("Authentication server registration failed.");
			return false;
		}
	}
	
	/**
	 * ping一下AuthZ，叫他來註冊。
	 * */
	private boolean pingAuthZServer() {
		try {
			pingService.pingAuthZServer();
			log.info("Authorization server registration successful!");
			return true;
		}catch(Exception e) {
			log.error("Authorization server registration failed.");
			return false;
		}
	}
	
	/**
	 * ping一下Book，叫他來註冊。
	 * */
	private void pingBookServer() {
		try {
			pingService.pingBookServer();
			log.info("Book server registration successful!");
		}catch(Exception e) {
			log.error("Book server registration failed.");
		}
	}
	
}
