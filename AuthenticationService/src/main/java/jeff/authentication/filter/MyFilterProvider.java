package jeff.authentication.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jeff.authentication.handler.LoginAuthenticationExceptionHandler;

/**
 * 過濾器提供者，管理過濾器的提供以及依賴元件的注入，有循環依賴問題的過濾器
 * ，其提供就不交給Spring管理，而是由這個提供者管理。</p>
 * 
 * @author Jeff Huang
 * */
@Component
public class MyFilterProvider {
	
	/**
	 * 因有循環依賴的問題，所以不交給Spring管理。
	 * */
	private CustomLoginAuthFilter customLoginAuthFilterInstance = null;

	@Autowired
	private RefreshTokenAuthenticationFilter refreshTokenAuthFilter;

	@Autowired
	private SourceAuthenticationFilter sourceAuthFilter;
	
	@Autowired
	private LoginAuthenticationExceptionHandler loginAuthExHandler;
	
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 因為{@link CustomLoginAuthFilter}依賴了{@link AuthenticationManager}(元件註冊於{@link SecurityConfig})，
	 * {@link SecurityConfig}依賴於{@link MyFilterProvider}，{@link MyFilterProvider}又依賴於{@link CustomLoginAuthFilter}......
	 * 為了避免循環依賴，不依靠Spring容器來建立{@link CustomLoginAuthFilter}，而是以呼叫{@link MyFilterProvider}來創建實例給外部。</p>
	 * 
	 * 當有人呼叫{@link MyFilterProvider #getCustomLoginAuthFilter()}時，會先檢查是否已經有人透過{@link MyFilterProvider}創建實例，若沒有，
	 * 才回傳新建物件並回傳，並且由呼叫的人注入{@link AuthenticationManager}，而非交給Spring去注入，藉此解決循環依賴。</p>
	 * 
	 * @param authManager - 
	 * 		  由呼叫者注入{@link AuthenticationManager}，而非利用{@code @Autowired}透過Spring去注入。
	 * @return {@link CustomLoginAuthFilter} - 若已有實例，回傳其參考；若無，創建實例並回傳其參考。
	 * */
	public synchronized CustomLoginAuthFilter getCustomLoginAuthFilter(AuthenticationManager authManager) {
		return this.customLoginAuthFilterInstance == null ? 
				this.customLoginAuthFilterInstance = buildCustomLoginAuthFilter(authManager) :
					this.customLoginAuthFilterInstance;
	}
	
	/**
	 * 創建{@link CustomLoginAuthFilter}並注入依賴的元件。
	 * */
	private CustomLoginAuthFilter buildCustomLoginAuthFilter(AuthenticationManager authManager) {
		CustomLoginAuthFilter customLoginAuthFilter = new CustomLoginAuthFilter(authManager,
				this.loginAuthExHandler,this.objectMapper);
		return customLoginAuthFilter;
	}
	
	
	/**
	 * 得到{@link RefreshTokenAuthenticationFilter}的單例實例。
	 * */
	public synchronized RefreshTokenAuthenticationFilter getRefreshTokenAuthFilter() {
		return this.refreshTokenAuthFilter;
	}
	
	
	/**
	 * 得到{@link SourceAuthenticationFilter}單例實例。
	 * */
	public SourceAuthenticationFilter getsourceAuthNFilter() {
		return this.sourceAuthFilter;
	}
	
}
