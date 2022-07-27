package jeff.apigateway.swagger.tmpl.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jeff.apigateway.swagger.annotation.MySwaggerForAuthN.MySwaggerForLogin;

/**
 * authN的登入API的responseBody模板，用以設定Swagger。
 * 
 * @author Jeff HUang
 * @see MySwaggerForLogin
 * */
public class LoginResTmpl {

	@Schema(description = "Used to refresh the access-token.")
	public String refreshToken;

	@Schema(description = "Used to authenticate when accessing protected APIs.")
	public String accessToken;
	
}
