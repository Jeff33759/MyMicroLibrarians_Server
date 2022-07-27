package jeff.book2;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import jeff.book2.common.annotation.ProductionOnly;
import jeff.book2.common.util.MyUtil;
import jeff.book2.exception.RestTemplateErrorResException;
import jeff.book2.exception.UpdatingBookDBException;
import jeff.book2.model.po.MyBook;
import jeff.book2.service.BookService;
import jeff.book2.service.HandshakeService;
import jeff.book2.serviceimpl.BookServiceImpl;
import jeff.book2.serviceimpl.HandshakeServiceImpl;

/**
 * 在SpringBoot啟動完成後，從政府資料平台撈資料，並初始化資料庫。</p>
 * 只在正式環境(production environment)中運行。
 * 
 * @author Jeff Huang
 * */
@Component
@ProductionOnly
public class AppInitRunner implements CommandLineRunner{
	
	private static final Logger log = 
			LoggerFactory.getLogger(AppInitRunner.class);
	
	@Autowired
	private MyUtil myUtil;

	private final BookService bookService;
	
	private final HandshakeService hsService;

	@Autowired
	public AppInitRunner(BookServiceImpl bookServiceImpl,
			HandshakeServiceImpl hsServiceImpl) {
		this.bookService = bookServiceImpl;
		this.hsService = hsServiceImpl;
	}


	@Override
	public void run(String... args) throws Exception{
		updateDBFromGovSite();
		registerWithGateway();
	}

	/**
	 * 從政府資料平台要資料，更新館藏資料庫。
	 * */
	private void updateDBFromGovSite() {
		log.info("Updating DB from Gov site......");
		try {
//			bookService.cleanDB();
			List<MyBook> data = myUtil.getDataFromGovSite();
			bookService.initBookDB(data);
			log.info("DB updated successfully from Gov site!");
		}catch(UpdatingBookDBException e) {
			log.warn("DB updated failed from Gov site.");
		}
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
