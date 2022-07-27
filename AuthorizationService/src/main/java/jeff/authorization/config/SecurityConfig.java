package jeff.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jeff.authorization.filter.SourceAuthenticationFilter;


/**
* Spring Security 5.4 後，不推薦使用繼承{@link WebSecurityConfigurerAdapter}的方式設置Security。</p>
* 這裡採用新版的做法來進行設置，讓設置的動作與{@link WebSecurityConfigurerAdapter}解耦。
* </p>
* @author Jeff Huang
*/
@Configuration
public class SecurityConfig {
	
	@Autowired
	private SourceAuthenticationFilter sourceAuthFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.formLogin().disable()
				.logout().disable()
				.csrf().disable()
				.authorizeRequests()
//					看似允許所有請求，但其實會由SourceAuthenticationFilter過濾不信任來源
					.anyRequest().permitAll()
				.and()
//				驗證來源是否可信
				.addFilterBefore(sourceAuthFilter, 
						UsernamePasswordAuthenticationFilter.class)
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.build();
	}
	
}
