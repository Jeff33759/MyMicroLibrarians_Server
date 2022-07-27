package jeff.apigateway.swagger.tmpl.req;


import io.swagger.v3.oas.annotations.media.Schema;
import jeff.apigateway.swagger.annotation.MySwaggerForAuthN.MySwaggerForLogin;

/**
 * authN的登入API的requestBody模板，用以設定Swagger。
 * 
 * @author Jeff Huang
 * @see MySwaggerForLogin
 * */
public class LoginReqTmpl {
	
	@Schema(description = "The ID of user.",
			example = "admin",
			required = true)
	public String id;

	@Schema(description = "The password of user.",
			example = "admin",
			required = true)
	public String password;

}
