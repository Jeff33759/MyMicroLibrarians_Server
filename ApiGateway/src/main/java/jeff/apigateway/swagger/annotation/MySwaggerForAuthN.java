package jeff.apigateway.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jeff.apigateway.swagger.annotation.MySwaggerForComm.MySwaggerAnnoForAllApi;
import jeff.apigateway.swagger.annotation.MySwaggerForComm.MySwaggerAnnoForProtApi;
import jeff.apigateway.swagger.tmpl.req.LoginReqTmpl;
import jeff.apigateway.swagger.tmpl.res.LoginResTmpl;
import jeff.apigateway.swagger.tmpl.res.RefreshATResTmpl;

/**
 * 把有關於authN的自做swagger annotation都做進這裡，統一管理。</p>
 * 自做swagger annotation的目的，在於讓控制器那邊可以不用塞一堆東西，
 * 也較便於維護。
 * */
public interface MySwaggerForAuthN {
	
	/**
	 * 針對AuthN的所有API都共有的文件。
	 * */
	@MySwaggerAnnoForAllApi
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface MySwaggerForAllAuthNApi {
		
	}

	/**
	 * 針對登入API的文件。
	 * */
	@MySwaggerAnnoForProtApi
	@Operation(
    		summary = "Login.",
    		description = "Login with user-id and password to get refresh-token and access-token.</p>" 
    					+ "There are currently three different levels of accounts available for demo. "
    					+ "Authority from high to low are 'ADMIN', 'ADVANCED' and 'NORMAL'.",
    		requestBody = @RequestBody(
    				content = @Content(
    						schema = @Schema(
    								implementation = LoginReqTmpl.class
    								)
    						),
    				required = true),
    		responses =  {
    				@ApiResponse(
    						responseCode = "200",
    						description = "A JSON object with refresh-token and access-token.",
    						content = @Content(
    								schema = @Schema(
    										implementation = LoginResTmpl.class)))})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MySwaggerForLogin {
		//nothing
	}

	
	/**
	 * 針對刷新AccessToken的API的文件。
	 * */
	@MySwaggerAnnoForProtApi
	@Operation(
    		summary = "Refresh the access-token.",
    		description = "Refresh expired access-token to access APIs.</p>"
    				+ "Have to put the bearer type refresh-token into the HTTP authorization header to authenticate.",
    		responses =  {
    				@ApiResponse(
    						responseCode = "200",
    						description = "A JSON object with access-token.",
    						content = @Content(
    								schema = @Schema(
    										implementation = RefreshATResTmpl.class)))})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MySwaggerForRefreshAccessToken {
		//nothing
	}

}
