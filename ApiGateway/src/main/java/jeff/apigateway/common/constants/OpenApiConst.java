package jeff.apigateway.common.constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jeff.apigateway.config.SecurityConfig;
import jeff.apigateway.controller.management.ManagementController;
import jeff.apigateway.filter.AccessTokenAuthorizationFilter;

/**
 * 存放對外開放API的常數。
 * 集中管理所有對外開放API的訪問路徑，並依授權劃分。</p>
 * 先將各種微服務的路徑以常數存放，集中列出管理
 * 然後下面再用兩個Enum依照授權進行分類，並匹配對應的HttpMethod，
 * 分裝成Set，做成{@link AntPathRequestMatcher}陣列，方便後續設置。</p>
 * 
 * 因為gateway是一個轉接的服務端，對gateway而言，
 * 公開API的接收(來自客戶端)與發送(給各微服務)都設計成同一路徑，因此不用像
 * {@link InnerApiConst}一樣又細分成Send和Receive
 * 
 * @author Jeff Huang
 * */
public interface OpenApiConst {
	
	/**
	 * 存放常數路徑。
	 * */
	public interface Path {
		
		/**
		 * Book-Read。
		 * */
		public static final String BOOK_R = "/book";
		
		/**
		 * Book-Read-ById。
		 * */
		public static final String BOOK_R_BYID = "/book/byid/{id}";
	
		/**
		 * Book-Read-Condition。
		 * */
		public static final String BOOK_R_COND = "/book/cond";
	
		/**
		 * Book-Create。
		 * */
		public static final String BOOK_C = "/book";
	
		/**
		 * Book-Replace。
		 * */
		public static final String BOOK_REPL = "/book/{id}";
		
		/**
		 * Book-Update。
		 * */
		public static final String BOOK_U = "/book/{id}";
		
		/**
		 * Book-Delete。
		 * */
		public static final String BOOK_D = "/book/{id}";
		
		/**
		 * Auth-login。
		 * */
		public static final String AUTHN_LOGIN = "/authn/login";
		
		/**
		 * Auth-Refresh-AccessToken。
		 * */
		public static final String AUTHN_RF_AT = "/authn/refreshing/at";

		/**
		 * gateway-monitor。
		 * @see ManagementController
		 * */
		public static final String GW_MONITOR = "/gateway/monitor";
		
	}
	
	
	/**
	 * 存放各路徑對應的HttpMethod常數
	 * */
	public interface HttpMethods{
		
		public static final HttpMethod BOOK_R = HttpMethod.GET;
		
		public static final HttpMethod BOOK_R_BYID = HttpMethod.GET;
		
		public static final HttpMethod BOOK_R_COND = HttpMethod.GET;
		
		public static final HttpMethod BOOK_C = HttpMethod.POST;
		
		public static final HttpMethod BOOK_REPL = HttpMethod.PUT;
		
		public static final HttpMethod BOOK_U = HttpMethod.PATCH;
		
		public static final HttpMethod BOOK_D = HttpMethod.DELETE;
		
		public static final HttpMethod AUTHN_LOGIN = HttpMethod.POST;
		
		public static final HttpMethod AUTHN_RF_AT = HttpMethod.POST;

		public static final HttpMethod GW_MONITOR = HttpMethod.GET;
		
	}
	
	

	
	
	/**
	 * 受保護的API路徑，起碼必須認證(登入)才能訪問，有些不只要登入，還有權限限制。</p>
	 * API保護等級有1~3個等級，等級越高就需要越高級的權限才能夠訪問。
	 * */
	public enum ProtApi {
		
		/**
		 * Book-Read-ById，用ID查書，必須認證才能用。
		 * */
		BOOK_R_BYID(Path.BOOK_R_BYID, HttpMethods.BOOK_R_BYID, 1),
		
		/**
		 * Book-Read-Condition，用條件查書，必須認證才能用。
		 * */
		BOOK_R_COND(Path.BOOK_R_COND, HttpMethods.BOOK_R_COND, 1),
		
		/**
		 * Book-Create，新增書，必須認證且授權Advanced以上才能用。
		 * */
		BOOK_C(Path.BOOK_C, HttpMethods.BOOK_C, 2),

		/**
		 * Book-Replace，取代書，必須認證且授權Advanced以上才能用。
		 * */
		BOOK_REPL(Path.BOOK_REPL, HttpMethods.BOOK_REPL, 2),
		
		/**
		 * Book-Update，更新書，必須認證且授權Advanced以上才能用。
		 * */
		BOOK_U(Path.BOOK_U, HttpMethods.BOOK_U, 2),
		
		/**
		 * Book-Delete，刪除書，必須認證且授權Advanced以上才能用。
		 * */
		BOOK_D(Path.BOOK_D, HttpMethods.BOOK_D, 2),
		
		/**
		 * 監聽gateway所管理之各微服務集群的狀態，例如某些微服務是否正在服務中...
		 * */
		GW_MONITOR(Path.GW_MONITOR, HttpMethods.GW_MONITOR,3);
		
		private final String path;

		private final HttpMethod method;
		
		private final int level;
		
		private ProtApi(String path,HttpMethod method,int level) {
			this.path = path;
			this.method = method;
			this.level = level;
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
		
		/**
		 * 得到該API的保護等級，總共設計有三級，保護等級越高，就需要越高的權限才能訪問。
		 * 1:只要登入就可訪問，任何角色不拘(預設每個會員都至少為有一個NORMAL角色)；
		 * 2:要登入，且角色要有ADVENCED才能訪問；
		 * 3:要登入，且角色要有ADMIN才能訪問。
		 * */
		public int level() {
			return this.level;
		}
		
		
	}

	/**
	 * 公開的API路徑，無須登入就能訪問。
	 * */
	public enum PubApi {
		
		/**
		 * Book-Read，查書，無須認證皆可訪問。
		 * */
		BOOK_R(Path.BOOK_R, HttpMethods.BOOK_R),
		
		/**
		 * Auth-login，認證後得到RT與AT，無須認證皆可訪問。</p>
		 * 
		 * 對gateway而言，此API無須認證皆可訪問，其意義可以解釋成:
		 * "這個請求在gateway這邊不做任何處理，原封不動地交給AuthN那邊去處理"；
		 * 而gateway轉給AuthN之後，對AuthN而言，此API是需要認證的，由AuthN
		 * 認證帳密是否正確，認證成功後才會回傳正確的TOKEN；所以以"整個系統"或者
		 * 以"外部客戶端的"的角度來看，此API其實是個需要認證的受保護API，
		 * 撰寫API文件時要注意這點。
		 * */
		AUTH_LOGIN(Path.AUTHN_LOGIN, HttpMethods.AUTHN_LOGIN),
		
		/**
		 * Auth-Refresh-AccessToken，用RT刷新AT，無須認證皆可訪問。</p>
		 * 
		 * 對gateway而言，此API無須認證皆可訪問，其意義可以解釋成:
		 * "這個請求在gateway這邊不做任何處理，原封不動地交給AuthN那邊去處理"；
		 * 而gateway轉給AuthN之後，對AuthN而言，此API是需要認證的，由AuthN
		 * 認證帳密是否正確，認證成功後才會回傳正確的TOKEN；所以以"整個系統"或者
		 * 以"外部客戶端的"的角度來看，此API其實是個需要認證的受保護API，
		 * 撰寫API文件時要注意這點。
		 * */
		AUTH_RF_AT(Path.AUTHN_RF_AT, HttpMethods.AUTHN_RF_AT);
		
		
		private final String path;

		private final HttpMethod method;
		
		private PubApi(String path,HttpMethod method) {
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
	 * 得到公開與受保護Api之路徑字串陣列，
	 * 供{@link SecurityConfig#securityFilterChain}進行設置。</p>
	 * */
	public static String[] getAllApiPath() {
		HashSet<String> pathSet = new HashSet<String>();
		allProtApi().stream().forEach(prot->pathSet.add(prot.path));
		allPubApi().stream().forEach(pub->pathSet.add(pub.path));
		return pathSet.toArray(new String[pathSet.size()]);
	}
	
	

	
	/**
	 * 將所有受保護Api列舉包裝成AntPathRequestMatcher[]，
	 * 供{@link AccessTokenAuthorizationFilter}進行設置。
	 * 
	 * @see AccessTokenAuthorizationFilter#shouldNotFilter
	 * */
	public static AntPathRequestMatcher[] getAllProtApiMatcher() {
		ArrayList<AntPathRequestMatcher> arrList = 
				new ArrayList<AntPathRequestMatcher>();
		allProtApi().stream().forEach(
				prot->arrList.add(new AntPathRequestMatcher(
						prot.path,prot.method.name())));
		return arrList.toArray(new AntPathRequestMatcher[arrList.size()]);
	}
	
	/**
	 * 將受保護等級1的Api列舉包裝成AntPathRequestMatcher[]，
	 * 供{@link SecurityConfig}進行設置。
	 * 
	 * @see SecurityConfig#securityFilterChain
	 * */
	public static AntPathRequestMatcher[] getLv1ProtApiMatcher() {
		ArrayList<AntPathRequestMatcher> arrList = 
				new ArrayList<AntPathRequestMatcher>();
		allProtApi().stream().filter(prot->prot.level==1)
							.forEach(lv1Prot->arrList.add(
								new AntPathRequestMatcher(
									lv1Prot.path,lv1Prot.method.name())));
		return arrList.toArray(new AntPathRequestMatcher[arrList.size()]);
	}
	
	/**
	 * 將受保護等級2的Api列舉包裝成AntPathRequestMatcher[]，
	 * 供{@link SecurityConfig}進行設置。
	 * 
	 * @see SecurityConfig#securityFilterChain
	 * */
	public static AntPathRequestMatcher[] getLv2ProtApiMatcher() {
		ArrayList<AntPathRequestMatcher> arrList = 
				new ArrayList<AntPathRequestMatcher>();
		allProtApi().stream().filter(prot->prot.level==2)
							.forEach(lv2Prot->arrList.add(
								new AntPathRequestMatcher(
									lv2Prot.path,lv2Prot.method.name())));
		return arrList.toArray(new AntPathRequestMatcher[arrList.size()]);
	}
	
	/**
	 * 將受保護等級3的Api列舉包裝成AntPathRequestMatcher[]，
	 * 供{@link SecurityConfig}進行設置。
	 * 
	 * @see SecurityConfig#securityFilterChain
	 * */
	public static AntPathRequestMatcher[] getLv3ProtApiMatcher() {
		ArrayList<AntPathRequestMatcher> arrList = 
				new ArrayList<AntPathRequestMatcher>();
		allProtApi().stream().filter(prot->prot.level==3)
							.forEach(lv3Prot->arrList.add(
								new AntPathRequestMatcher(
									lv3Prot.path,lv3Prot.method.name())));
		return arrList.toArray(new AntPathRequestMatcher[arrList.size()]);
	}
	
	
	/**
	 * 將所有公開Api列舉包裝成AntPathRequestMatcher[]，
	 * 供{@link SecurityConfig#securityFilterChain}進行設置。
	 * 
	 * @see SecurityConfig#securityFilterChain
	 * */
	public static AntPathRequestMatcher[] getAllPubApiMatcher() {
		ArrayList<AntPathRequestMatcher> arrList = 
				new ArrayList<AntPathRequestMatcher>();
		allPubApi().stream().forEach(
				pub->arrList.add(new AntPathRequestMatcher(
						pub.path,pub.method.name())));
		return arrList.toArray(new AntPathRequestMatcher[arrList.size()]);
	}
	
	
	/**
	 * 得到所有受保護Api列舉的Set。
	 * */
	private static Set<ProtApi> allProtApi() {
		return Stream.of(OpenApiConst.ProtApi.values())
				.collect(Collectors.toSet());
	}
	
	/**
	 * 得到所有公開Api列舉的Set。
	 * */
	private static Set<PubApi> allPubApi() {
		return Stream.of(OpenApiConst.PubApi.values())
				.collect(Collectors.toSet());
	}
	
	
	/**
	 * 受保護等級1的API，有哪些角色可以訪問。
	 * */
	public final String[] lv1ApiAccessibleRoles = {
			UserRole.NORMAL.name(),
			UserRole.ADVANCED.name(),
			UserRole.ADMIN.name()
	};
	
	/**
	 * 受保護等級2的API，有哪些角色可以訪問。
	 * */
	public final String[] lv2ApiAccessibleRoles = {
			UserRole.ADVANCED.name(),
			UserRole.ADMIN.name()
	};
	
	/**
	 * 受保護等級3的API，有哪些角色可以訪問。
	 * */
	public final String[] lv3ApiAccessibleRoles = {
			UserRole.ADMIN.name()
	};
	
}
