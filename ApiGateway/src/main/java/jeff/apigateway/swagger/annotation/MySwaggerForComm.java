package jeff.apigateway.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jeff.apigateway.model.dto.send.ActionFailedRes;

/**
 * 集中管理一些自做的swagger annotation。
 * 
 * @see MySwaggerForBook
 * @see MySwaggerForAuthN
 * @author Jeff Huang
 * */
public interface MySwaggerForComm {

	/**
	 * 關於所有受保護API所共有的說明文件。
	 * */
	@ApiResponses(value = {
			@ApiResponse(
			        responseCode = "401",
			        description = "Unauthorized request, the client has not been authenticated or request URL isn't correct.",
			        content = @Content(
			        		schema = @Schema(
			        				implementation = ActionFailedRes.class))),
			@ApiResponse(
			        responseCode = "403",
			        description = "Forbidden request, the client do not have enough authorization to access this API.",
			        content = @Content(
			        		schema = @Schema(
			        				implementation = ActionFailedRes.class
			        		)))})
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.ANNOTATION_TYPE})
	public @interface MySwaggerAnnoForProtApi{
		//nothing
	}
	
	
	/**
	 * 關於所有業務邏輯API共有的說明文件。
	 * */
	@ApiResponses(value = {
			@ApiResponse(
			        responseCode = "400",
			        description = "When the required query string or path variable or body params is not expected.",
			        content = @Content(
			        		schema = @Schema(
			        				implementation = ActionFailedRes.class))),
			@ApiResponse(
			        responseCode = "500",
			        description = "When an unexpected error occurs in the system.",
			        content = @Content(
			        		schema = @Schema(
			        				implementation = ActionFailedRes.class
			        		))),
			@ApiResponse(
					responseCode = "502",
					description = "An error occurred when the gateway connected to the business server.",
					content = @Content(
							schema = @Schema(
									implementation = ActionFailedRes.class))),
			@ApiResponse(
					responseCode = "504",
					description = "Business server response timed out.",
					content = @Content(
							schema = @Schema(
									implementation = ActionFailedRes.class
									)))})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface MySwaggerAnnoForAllApi{
		//nothing
	}
	
}
