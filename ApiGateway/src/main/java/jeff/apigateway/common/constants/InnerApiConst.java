package jeff.apigateway.common.constants;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jeff.apigateway.config.SecurityConfig;
import jeff.apigateway.serviceimpl.PingServiceImpl;


/**
 * 存放不對外開放、只用於"系統與系統間交握溝通"的API的常數。
 * 集中管理所有系統內部API的訪問路徑，並依授權劃分。</p>
 * 先將各種路徑以常數存放，集中列出管理
 * 然後下面再用Enum匹配對應的HttpMethod，
 * 分裝成Set，做成{@link AntPathRequestMatcher}陣列，
 * 方便{@link SecurityConfig#securityFilterChain}設置。
 * */
public interface InnerApiConst {
	
	/**
	 * 用於接受請求。
	 * 
	 * @see SecurityConfig#securityFilterChain
	 * */
	public interface Receive{
		
		/**
		 * 存放常數路徑。
		 * */
		public interface Path {
			
			public static final String REGISTER_BOOK = "/gateway/reg/book";
			
			public static final String HEARTBEAT_BOOK = "/gateway/hb/book";
			
			public static final String REGISTER_BOOK2 = "/gateway/reg/book2";
			
			public static final String HEARTBEAT_BOOK2 = "/gateway/hb/book2";
			
			public static final String REGISTER_AUTHN = "/gateway/reg/authn";
			
			public static final String HEARTBEAT_AUTHN = "/gateway/hb/authn";
			
			public static final String REGISTER_AUTHZ = "/gateway/reg/authz";
			
			public static final String HEARTBEAT_AUTHZ = "/gateway/hb/authz";
			
		}
		
		/**
		 * 存放各路徑對應的HttpMethod常數
		 * */
		public interface HttpMethods{
			
			public static final HttpMethod REGISTER_BOOK = HttpMethod.GET;
		
			public static final HttpMethod HEARTBEAT_BOOK = HttpMethod.GET;
			
			public static final HttpMethod REGISTER_BOOK2 = HttpMethod.GET;
			
			public static final HttpMethod HEARTBEAT_BOOK2 = HttpMethod.GET;
			
			public static final HttpMethod REGISTER_AUTHN = HttpMethod.GET;
			
			public static final HttpMethod HEARTBEAT_AUTHN = HttpMethod.GET;
			
			public static final HttpMethod REGISTER_AUTHZ = HttpMethod.GET;
			
			public static final HttpMethod HEARTBEAT_AUTHZ = HttpMethod.GET;
		
		}
		

		/**
		 * 私人的API路徑，不給客戶端訪問，只給微服務訪問。
		 * */
		public enum PriApi {
			
			/**
			 * 微服務向gateway註冊用。
			 * */
			REGISTER_BOOK(Path.REGISTER_BOOK, HttpMethods.REGISTER_BOOK),
			
			/**
			 * 微服務向gateway傳送心跳訊號用。
			 * */
			HEARTBEAT_BOOK(Path.HEARTBEAT_BOOK, HttpMethods.HEARTBEAT_BOOK),
			
			/**
			 * 微服務向gateway註冊用。
			 * */
			REGISTER_BOOK2(Path.REGISTER_BOOK2, HttpMethods.REGISTER_BOOK2),
			
			/**
			 * 微服務向gateway傳送心跳訊號用。
			 * */
			HEARTBEAT_BOOK2(Path.HEARTBEAT_BOOK2, HttpMethods.HEARTBEAT_BOOK2),
			
			/**
			 * 微服務向gateway註冊用。
			 * */
			REGISTER_AUTHN(Path.REGISTER_AUTHN, HttpMethods.REGISTER_AUTHN),
			
			/**
			 * 微服務向gateway傳送心跳訊號用。
			 * */
			HEARTBEAT_AUTHN(Path.HEARTBEAT_AUTHN, HttpMethods.HEARTBEAT_AUTHN),
			
			/**
			 * 微服務向gateway註冊用。
			 * */
			REGISTER_AUTHZ(Path.REGISTER_AUTHZ, HttpMethods.REGISTER_AUTHZ),
			
			/**
			 * 微服務向gateway傳送心跳訊號用。
			 * */
			HEARTBEAT_AUTHZ(Path.HEARTBEAT_AUTHZ, HttpMethods.HEARTBEAT_AUTHZ);

			
			private final String path;

			private final HttpMethod method;
			
			private PriApi(String path,HttpMethod method) {
				this.path = path;
				this.method = method;
			}
			
			/**
			 * 得到API的路徑。
			 * */
			public String path() {
				return this.path;
			}

			/**
			 * 得到API的Http Method。
			 * */
			public HttpMethod method() {
				return this.method;
			}
		}
		
		/**
		 * 將所有私人Api列舉包裝成{@link AntPathRequestMatcher}陣列 ，
		 * 供{@link SecurityConfig#securityFilterChain}進行設置。
		 * */
		public static AntPathRequestMatcher[] getAllPriApiMatcher() {
			ArrayList<AntPathRequestMatcher> arrList = 
					new ArrayList<AntPathRequestMatcher>();
			int length = InnerApiConst.Receive.PriApi.values().length;
			allPriApi().stream().forEach(
					pri->arrList.add(new AntPathRequestMatcher(
							pri.path,pri.method.name())));
			return arrList.toArray(new AntPathRequestMatcher[length]);
		}
		
		
		/**
		 * 得到所有私人Api列舉的Set。
		 * */
		private static Set<PriApi> allPriApi() {
			return Stream.of(InnerApiConst.Receive.PriApi.values())
					.collect(Collectors.toSet());
		}
		
		/**
		 * 可以訪問私人API的角色權限。
		 * */
		public final String[] priApiAccessibleRoles = {
			SystemRole.SYSTEM.name(),
		};
		
	}
	
	/**
	 * 用於發出請求。
	 * */
	public interface Send {
		
		/**
		 * 存放常數路徑。
		 * 
		 * @see PingServiceImpl
		 * */
		public interface Path {
			
			/**
			 * BookServer-Ping。
			 * */
			public static final String BOOK_PING = "/ping/book";

			/**
			 * BookServer2-Ping。
			 * */
			public static final String BOOK2_PING = "/ping/book2";
			
			/**
			 * AuthenticationServer-Ping。
			 * */
			public static final String AUTHN_PING = "/ping/authn";
			
			/**
			 * AuthorizationServer-Ping。
			 * */
			public static final String AUTHZ_PING = "/ping/authz";
			
			/**
			 * AuthorizationServer-Parsing-AcceccToken。
			 * */
			public static final String AUTHZ_PARSING_AT = "/authz/parsing/at";
			
		}
	}
	
}
