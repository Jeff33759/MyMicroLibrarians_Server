package jeff.apigateway.common.param;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import jeff.apigateway.controller.business.AuthenticationController;
import jeff.apigateway.controller.business.BookController;
import jeff.apigateway.filter.SourceAuthenticationFilter;
import jeff.apigateway.serviceimpl.HeartBeatServiceImpl;

/**
 * 統一存放管理各微服務所需的變數，依照不同的用途而分類</p>
 * 用volatile修飾可能會有多個執行緒同時修改的變數，確保資料可見性。</p>
 * 
 * @author Jeff Huang
 * */
public interface SystemParam {
	
	/**
	 * 一些管理Server的共同變數。
	 * */
	@Component
	@PropertySource("classpath:MicroServiceParam.properties")
	public class Common implements SystemParam{
		
		/**
		 * 將所有微服務的位址裝填成Set，方便驗證來源是否安全。
		 * 
		 * @see {@link SourceAuthenticationFilter}
		 * */
		@Value("${ip.set}")
		public Set<String> ALL_IP_SET;
		
		/**
		 * gateway是否提供服務，預設false。
		 * 一些強制依賴的微服務(例如登入或是驗證)若註冊失敗，
		 * 那gateway就暫時不對外提供服務。
		 * */
		public volatile boolean GW_IN_SERVICE = false;
		
	}
	
	
	
	/**
	 * BookServer相關的數據。
	 * @see {@link BookController}
	 * @see {@link HeartBeatServiceImpl}
	 * @see {@link SourceAuthenticationFilter}
	 * */
	@Component
	@PropertySource("classpath:MicroServiceParam.properties")
	public class Book implements SystemParam{
		

		/**
		 * 目前運行的位址。
		 * */
		@Value("${ip.book}")
		public String IP;

		/**
		 * 目前運行的port。
		 * */
		@Value("${port.book}")
		public String PORT;
		
		
		/**
		 * 微服務的心跳訊號，預設false。
		 * */
		public volatile boolean SIGN = false; 
		

		/**
		 * 服務端是否正在服務中，預設false。
		 * */
		public volatile boolean IN_SERVICE = false;
		
	}
	
	/**
	 * BookServer2相關的數據。
	 * @see {@link BookController}
	 * @see {@link HeartBeatServiceImpl}
	 * @see {@link SourceAuthenticationFilter}
	 * */
	@Component
	@PropertySource("classpath:MicroServiceParam.properties")
	public class Book2 implements SystemParam{
		
		/**
		 * 目前運行的位址。
		 * */
		@Value("${ip.book2}")
		public String IP;
		
		/**
		 * 目前運行的port。
		 * */
		@Value("${port.book2}")
		public String PORT;
		
		
		/**
		 * 微服務的心跳訊號，預設false。
		 * */
		public volatile boolean SIGN = false; 
		
		
		/**
		 * 服務端是否正在服務中，預設false。
		 * */
		public volatile boolean IN_SERVICE = false;
		
	}
	
	/**
	 * AuthenticationServer相關的數據。
	 * @see {@link AuthenticationController}
	 * @see {@link HeartBeatServiceImpl}
	 * @see {@link SourceAuthenticationFilter}
	 * */
	@Component
	@PropertySource("classpath:MicroServiceParam.properties")
	public class AuthN implements SystemParam{
		
		/**
		 * 目前運行的位址。
		 * */
		@Value("${ip.authn}")
		public String IP;
		
		/**
		 * 目前運行的port。
		 * */
		@Value("${port.authn}")
		public String PORT;
		
		
		/**
		 * 微服務的心跳訊號，預設false。
		 * */
		public volatile boolean SIGN = false; 
		
		/**
		 * 服務端是否正在服務中，預設false。
		 * */
		public volatile boolean IN_SERVICE = false;
	}
	
	/**
	 * AuthorizationServer相關的數據。
	 * @see {@link HeartBeatServiceImpl}
	 * @see {@link SourceAuthenticationFilter}
	 * */
	@Component
	@PropertySource("classpath:MicroServiceParam.properties")
	public class AuthZ implements SystemParam{
		
		/**
		 * 目前運行的位址。
		 * */
		@Value("${ip.authz}")
		public String IP;
		
		/**
		 * 目前運行的port。
		 * */
		@Value("${port.authz}")
		public String PORT;
		
		
		/**
		 * 微服務的心跳訊號，預設false。
		 * */
		public volatile boolean SIGN = false; 
		
		/**
		 * 服務端是否正在服務中，預設false。
		 * */
		public volatile boolean IN_SERVICE = false;
	}
	
}
