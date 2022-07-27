package jeff.apigateway.common.constants;

import jeff.apigateway.config.SecurityConfig;
import jeff.apigateway.filter.SourceAuthenticationFilter;

/**
 * 內部系統的授權類別，用於訪問{@link InnerApiConst}的授權。
 * 
 * @author Jeff Huang
 * @see InnerApiConst
 * @see SecurityConfig
 * @see SourceAuthenticationFilter #successfulAuthentication
 * */
public enum SystemRole {

	/**
	 * 代表訪問者是別的微服務伺服端。
	 * */
	SYSTEM

}
