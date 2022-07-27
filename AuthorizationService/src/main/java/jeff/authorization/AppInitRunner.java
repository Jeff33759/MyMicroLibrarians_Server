package jeff.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import jeff.authorization.exception.RestTemplateErrorResException;
import jeff.authorization.service.HandshakeService;
import jeff.authorization.serviceimpl.HandshakeServiceImpl;


/**
 * 在SpringBoot啟動完成後，主動向gateway註冊。
 * 
 * @author Jeff Huang
 * */
@Component
public class AppInitRunner implements CommandLineRunner{
	
	private static final Logger log = 
			LoggerFactory.getLogger(AppInitRunner.class);
	
	private final HandshakeService hsService;

	@Autowired
	public AppInitRunner(HandshakeServiceImpl hsServiceImpl) {
		this.hsService = hsServiceImpl;
	}


	@Override
	public void run(String... args) throws Exception {
		registerWithGateway();
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
