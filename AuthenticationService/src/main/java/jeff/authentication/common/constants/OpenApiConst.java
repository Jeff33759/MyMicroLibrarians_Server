package jeff.authentication.common.constants;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jeff.authentication.filter.CustomLoginAuthFilter;
import jeff.authentication.filter.RefreshTokenAuthenticationFilter;


/**
 * 存放對外開放API的常數。
 * 集中管理所有對外開放API的訪問路徑，並依授權劃分。</p>
 * 先將各種微服務的路徑以常數存放，集中列出管理
 * 然後下面再用兩個Enum依照授權進行分類，並匹配對應的HttpMethod，
 * 分裝成Set，做成{@link AntPathRequestMatcher}陣列，方便後續設置。
 * */
public interface OpenApiConst {
	
	/**
	 * 存放常數路徑。
	 * */
	public interface Path {
		
		/**
		 * Auth-login，登入用API，提供正確的帳密，回傳RT與AT。
		 * */
		public static final String AUTHN_LOGIN = "/authn/login";
		
		/**
		 * Auth-Refresh-AccessToken，刷新AT的API。
		 * */
		public static final String AUTHN_RF_AT = "/authn/refreshing/at";

	}
	
	
	/**
	 * 存放各路徑對應的HttpMethod常數
	 * */
	public interface HttpMethods{
		
		public static final HttpMethod AUTHN_LOGIN = HttpMethod.POST;
		
		public static final HttpMethod AUTHN_RF_AT = HttpMethod.POST;

	}
	

	/**
	 * 受保護的API路徑，需要認證才能訪問。</p>
	 * 對於gateway而言，登入與刷新RT的API是公開的API，在他那邊無須認證即可訪問，
	 * 然後轉到這個AuthN伺服端處理；對於AuthN伺服端而言，登入與刷新RT的API是受保護API，
	 * 需要經過Security filter chain內的{@link CustomLoginAuthFilter}或
	 * {@link RefreshTokenAuthenticationFilter}認證，確認身份無誤後才能訪問。</p>
	 * */
	public enum ProtApi {
		
		/**
		 * Auth-login，認證後得到RT與AT，無須認證皆可訪問。
		 * */
		AUTH_LOGIN(Path.AUTHN_LOGIN, HttpMethods.AUTHN_LOGIN),
		
		/**
		 * Auth-Refresh-AccessToken，用RT刷新AT，無須認證皆可訪問。
		 * */
		AUTH_RF_AT(Path.AUTHN_RF_AT, HttpMethods.AUTHN_RF_AT);
		
		private final String path;

		private final HttpMethod method;
		
		private ProtApi(String path,HttpMethod method) {
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
	 * 將所有公開Api列舉包裝成AntPathRequestMatcher[]，
	 * 供{@link SecurityConfig#securityFilterChain}進行設置。
	 * */
	public static AntPathRequestMatcher[] getAllProtApiMatcher() {
		ArrayList<AntPathRequestMatcher> arrList = 
				new ArrayList<AntPathRequestMatcher>();
		allProtApi().stream().forEach(
				pub->arrList.add(new AntPathRequestMatcher(
						pub.path,pub.method.name())));
		return arrList.toArray(new AntPathRequestMatcher[arrList.size()]);
	}
	
	
	/**
	 * 得到所有公開Api列舉的Set。
	 * */
	private static Set<ProtApi> allProtApi() {
		return Stream.of(OpenApiConst.ProtApi.values())
				.collect(Collectors.toSet());
	}

}
