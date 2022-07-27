package jeff.apigateway.swagger.tmpl.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jeff.apigateway.swagger.annotation.MySwaggerForAuthN.MySwaggerForRefreshAccessToken;

/**
 * authN的刷新AT API的responseBody模板，用以設定Swagger。
 * 
 * @author Jeff Huang
 * @see MySwaggerForRefreshAccessToken
 * */
public class RefreshATResTmpl {
	
	@Schema(description = "Used to authenticate when accessing protected APIs.")
	public String accessToken;

}
