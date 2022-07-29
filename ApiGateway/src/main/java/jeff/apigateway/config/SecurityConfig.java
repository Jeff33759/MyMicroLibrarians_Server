package jeff.apigateway.config;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.common.constants.OpenApiConst;
import jeff.apigateway.filter.GatewayStatusCheckFilter;
import jeff.apigateway.filter.MyFilterProvider;
import jeff.apigateway.filter.SourceAuthenticationFilter;
import jeff.apigateway.handler.SecurityExceptionHandler;


/**
* Spring Security 5.4 後，不推薦使用繼承{@link WebSecurityConfigurerAdapter}的方式設置Security。</p>
* 這裡採用新版的做法來進行設置，讓設置的動作與{@link WebSecurityConfigurerAdapter}解耦。
* </p>
* @author Jeff Huang
*/
@Configuration
public class SecurityConfig {
	
	@Autowired
	private SecurityExceptionHandler securityExceptionHandler;
	
	@Autowired
	private MyFilterProvider myFilterProvider;
	
	
	/**
	* 對Security過濾鍊的認證機制進行設置。
	* 棄用基於Cookie的Session機制，使用JWTs來進行授權驗證。</p>
	* 棄用Security預設的表單登入，實現自製的登入認證流程。</p>
	* 因為棄用cookie-Session機制，基本上不會有CSRF的風險，所以關閉CSRF防禦機制。</p>
	* 當SpringBoot程式發生不可預期的例外時，會自動導到/error，
	* 所以放行該路徑，讓SpringBoot預設的{@link BasicErrorController}處理後續回應。</p>
	* 
	* http.cors()顯式設置允許的跨來源請求(否則預設禁止)，此舉會自動放行{@link #corsConfigurationSource}
	* 所匹配路徑的OPTIONS請求(所以不用再另外設置antMatchers放行OPTIONS)，讓非簡單請求的預檢請求(preflight request)
	* 不會被擋掉，並告知瀏覽器本伺服端支持該路徑的跨來源請求；當瀏覽器發出真正的請求時，本伺服端會為回應
	* 都加上CORS所需的標頭(Access-Control-Allow-Origin:*)，讓WEB瀏覽器的JS可以取得所需的業務回應。
	*/
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.formLogin().disable()
				.logout().disable()
				.csrf().disable()
				.cors()
				.and()
				.authorizeRequests()
					.requestMatchers(OpenApiConst.getAllPubApiMatcher()).permitAll()
//					受保護API，由AccessTokenAuthorizationFilter負責認證授權邏輯，依權限劃分
					.requestMatchers(OpenApiConst.getLv1ProtApiMatcher()).hasAnyAuthority(OpenApiConst.lv1ApiAccessibleRoles)
					.requestMatchers(OpenApiConst.getLv2ProtApiMatcher()).hasAnyAuthority(OpenApiConst.lv2ApiAccessibleRoles)
					.requestMatchers(OpenApiConst.getLv3ProtApiMatcher()).hasAnyAuthority(OpenApiConst.lv3ApiAccessibleRoles)
//					私人API，只用於系統與系統溝通，由SourceAuthenticationFilter負責認證授權邏輯
					.requestMatchers(InnerApiConst.Receive.getAllPriApiMatcher()).hasAnyAuthority(InnerApiConst.Receive.priApiAccessibleRoles)
//					為對外開放的API放行OPTIONS請求，讓web瀏覽器CORS時的預檢請求(preflight request)可以通過。
//					.antMatchers(HttpMethod.OPTIONS, OpenApiConst.getAllApiPath()).permitAll()
					.antMatchers("/error").permitAll()
					.antMatchers("/swagger**/**").permitAll() //放行swagger相關檔案
					.antMatchers("/**/api-docs/**").permitAll() //放行swagger相關檔案
					.anyRequest().authenticated()
				.and()
				.addFilterBefore(myFilterProvider.getGatewayStatusCheckFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(myFilterProvider.getSourceAuthenticationFilter(),
						GatewayStatusCheckFilter.class)
				.addFilterAfter(myFilterProvider.getAccessTokenAuthorizationFilter(),
						SourceAuthenticationFilter.class)
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
//				設置自訂義的異常處理器
				.exceptionHandling()
					.authenticationEntryPoint(securityExceptionHandler)
					.accessDeniedHandler(securityExceptionHandler)
				.and()
				.build();
	}
	
	
	/**
	 * 針對跨來源請求進行相關設置，設置本伺服端支援哪些網域的哪些方法
	 * 所發出的跨來源請求。
	 * </p>
	 * 設置完畢後，將{@link CorsConfigurationSource}公開為Bean，
	 * 這樣上面的{@link HttpSecurity #cors()}會自動抓到這些設定。
	 * */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
//    	允許的跨來源網域陣列(哪些網域可以發跨域請求)，*為全部
    	String[] allowedOriginsArr = {"*"}; 
//    	允許的跨來源請求方法陣列，*為全部
    	String[] allowedMethodArr = {"*"}; 
//    	要裸露的跨來源回應標頭，*為全部
    	String[] exposedHeaders = {"*"}; 
//    	跨來源相關設定
        CorsConfiguration corsConfig = new CorsConfiguration();
//      設置允許的跨來源網域
        corsConfig.setAllowedOrigins(Arrays.asList(allowedOriginsArr));
//      設置允許的跨來源請求方法
        corsConfig.setAllowedMethods(Arrays.asList(allowedMethodArr));
//      設置回應的哪些標頭可以讓WEB瀏覽器使用於JS
        corsConfig.setExposedHeaders(Arrays.asList(exposedHeaders));
//      其餘沒設置的值，都設置預設值
        corsConfig.applyPermitDefaultValues();
//        Access-Control-Expose-Headers
        UrlBasedCorsConfigurationSource source = 
        		new UrlBasedCorsConfigurationSource();
//      本伺服端哪些路徑會吃到上面的跨域設定。
//      設置全部，這樣當前端輸入不存在的路徑時，瀏覽器可以顯示伺服端傳遞的錯誤訊息，而不是變成CORS錯誤
        source.registerCorsConfiguration("/**",corsConfig);
        
        return source;
    }
	
}
