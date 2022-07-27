package jeff.apigateway.common.constants;

import jeff.apigateway.config.SecurityConfig;
import jeff.apigateway.filter.AccessTokenAuthorizationFilter;

/**
 * 使用者的授權類別，用於訪問{@link OpenApiConst}的授權。
 * 
 * @author Jeff Huang
 * 
 * @see OpenApiConst
 * @see SecurityConfig
 * @see AccessTokenAuthorizationFilter
 * */
public enum UserRole {
	
	/**
	 * 可以操作使用者的CRUD(尚未規劃)、館藏的CRUD、
	 * 查看gareway有哪些微服務註冊。
	 * */
	ADMIN(),
	
	/**
	 * 可以操作館藏的CRUD。
	 * */
	ADVANCED(), 
	
	/**
	 * 可以操作館藏的進階R。
	 * */
	NORMAL();
	
}
