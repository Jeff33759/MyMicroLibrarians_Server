package jeff.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jeff.apigateway.config.SecurityConfig;

/**
 * 集中管理所有的過濾器，讓{@link SecurityConfig}可以不用
 * 注入一堆東西。
 * */
@Component
public class MyFilterProvider {
	
	@Autowired
	private final SourceAuthenticationFilter sourceAuthenticationFilter;
	
	@Autowired
	private final GatewayStatusCheckFilter gatewayStatusCheckFilter;
	
	@Autowired
	private final AccessTokenAuthorizationFilter accessTokenAuthorizationFilter;

	public MyFilterProvider(SourceAuthenticationFilter sourceAuthenticationFilter,
			GatewayStatusCheckFilter gatewayStatusCheckFilter,
			AccessTokenAuthorizationFilter accessTokenAuthorizationFilter) {
		this.sourceAuthenticationFilter = sourceAuthenticationFilter;
		this.gatewayStatusCheckFilter = gatewayStatusCheckFilter;
		this.accessTokenAuthorizationFilter = accessTokenAuthorizationFilter;
	}

	public SourceAuthenticationFilter getSourceAuthenticationFilter() {
		return sourceAuthenticationFilter;
	}

	public GatewayStatusCheckFilter getGatewayStatusCheckFilter() {
		return gatewayStatusCheckFilter;
	}

	public AccessTokenAuthorizationFilter getAccessTokenAuthorizationFilter() {
		return accessTokenAuthorizationFilter;
	}
	
	
}
