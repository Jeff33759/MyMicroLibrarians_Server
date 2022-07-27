package jeff.authentication.common.constants;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jeff.authentication.config.SecurityConfig;


/**
 * 存放不對外開放、只用於系統內部的API的常數。
 * */
public interface InnerApiConst {
	
	/**
	 * 接收請求
	 * */
	public interface Receive{
		
		/**
		 * 存放常數路徑。
		 * */
		public interface Path {
			/**
			 * 讓gateway通知的路徑。
			 * */
			public static final String PING = "/ping/authn";
		}
		
		/**
		 * 存放各路徑對應的HttpMethod常數
		 * */
		public interface HttpMethods{
			public static final HttpMethod PING = HttpMethod.GET;
		}
		
		
		/**
		 * 私人的API路徑。
		 * */
		public enum PriApi {
			
			/**
			 * 被gateway通知。
			 * */
			PING(Path.PING, HttpMethods.PING);
			
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
		
	}
	
	
	
	/**
	 * 發送請求。
	 * */
	public interface Send{
		
		/**
		 * 存放常數路徑。
		 * */
		public interface Path {
			
			/**
			 * Gateway-Register，向Gateway註冊。
			 * */
			public static final String GW_REGISTER = "/gateway/reg/authn";
			
			/**
			 * Gateway-HeartBeat，向Gateway傳送心跳訊號。
			 * */
			public static final String GW_HEARTBEAT = "/gateway/hb/authn";
		}
		
	}

}
