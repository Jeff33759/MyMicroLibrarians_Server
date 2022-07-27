package jeff.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;

import jeff.authentication.common.constants.InnerApiConst;
import jeff.authentication.common.constants.OpenApiConst;
import jeff.authentication.filter.MyFilterProvider;
import jeff.authentication.filter.RefreshTokenAuthenticationFilter;
import jeff.authentication.filter.SourceAuthenticationFilter;
import jeff.authentication.handler.SecurityExceptionHandler;
import jeff.authentication.serviceimpl.UserDetailsServiceImpl;

/**
* Spring Security 5.4 後，不推薦使用繼承{@link WebSecurityConfigurerAdapter}的方式設置Security。</p>
* 這裡採用新版的做法來進行設置，讓設置的動作與{@link WebSecurityConfigurerAdapter}解耦。
* </p>
* @author Jeff Huang
*/
@Configuration
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private MyFilterProvider myFilterProvider;
	
	@Autowired
	private SecurityExceptionHandler securityExHandler;
	
	@Autowired
	public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl) {
		this.userDetailsService = userDetailsServiceImpl;
	}
	

	/**
	* 對Security過濾鍊的認證機制進行設置。
	* 棄用基於Cookie的Session機制，使用JWTs來進行授權驗證。</p>
	* 棄用Security預設的表單登入，實現自製的登入認證流程。</p>
	* 因為只接受gateway的請求，基本上不會有CSRF的風險，所以關閉CSRF防禦機制。</p>
	* 不用像gateway一樣特別為服務中的API放行OPTIONS請求，因為進來這裡的都是gateway轉接過來的，
	* 而不是直接由web瀏覽器發送請求。</p>
	* 當SpringBoot程式發生不可預期的例外時，會自動導到/error，
	* 所以放行該路徑，讓SpringBoot預設的{@link BasicErrorController}處理後續回應。</p>
	*/
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.formLogin().disable()
				.logout().disable()
				.csrf().disable()
				.authorizeRequests()
//					由CustomLoginAuthFilter或RefreshTokenAuthenticationFilter來進行認證授權
					.requestMatchers(OpenApiConst.getAllProtApiMatcher()).authenticated()
//					雖說這裡看似直接放行，但其實會由SourceAuthenticationFilter驗證來源是否可信
					.requestMatchers(InnerApiConst.Receive.getAllPriApiMatcher()).permitAll()
					.antMatchers("/error").permitAll()
					.anyRequest().authenticated()
				.and()
//				驗證來源是否可信
				.addFilterBefore(myFilterProvider.getsourceAuthNFilter(), 
						UsernamePasswordAuthenticationFilter.class)
//				當刷新AT時，要認證RT是否正確
				.addFilterAfter(myFilterProvider.getRefreshTokenAuthFilter(), 
						SourceAuthenticationFilter.class)
//				登入認證
				.addFilterAfter(myFilterProvider.getCustomLoginAuthFilter(
						authenticationManager(null)), 
							RefreshTokenAuthenticationFilter.class)
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
//				設置自訂義的異常處理器
				.exceptionHandling()
					.authenticationEntryPoint(this.securityExHandler)
					.accessDeniedHandler(this.securityExHandler)
				.and()
				.build();
	}
	

	/**
	 * 藉由AuthenticationManagerBuilder全局配置自己想要的驗證機制。
	 *
	 * @param auth - 簡單地配置自己想要的登入驗證機制，可以使用JDBC或in memory或LDAP等等...
	 */
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
	}
	
	
	/**
	 * 將客製化配置完畢的{@link AuthenticationManager}公開為Bean。</p>
	 * 使Security驗證機制，採用自己寫的{@link userDetailsService}，從mongoDB中撈出驗證比對用的資料。</p>
	 * 客戶端傳來的密碼，使用{@link BCryptPasswordEncoder}加密為暗文。</p>
	 * */    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
          return authConfig.getAuthenticationManager();
    }
    

}