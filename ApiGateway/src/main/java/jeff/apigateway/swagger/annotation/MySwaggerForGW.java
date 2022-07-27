package jeff.apigateway.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jeff.apigateway.controller.management.ManagementController;
import jeff.apigateway.swagger.annotation.MySwaggerForComm.MySwaggerAnnoForAllApi;
import jeff.apigateway.swagger.annotation.MySwaggerForComm.MySwaggerAnnoForProtApi;
import jeff.apigateway.swagger.tmpl.res.MonitorResTmpl;

/**
 * 把有關於gateway-management的自做swagger annotation都做進這裡，統一管理。</p>
 * 自做swagger annotation的目的，在於讓控制器那邊可以不用塞一堆東西，
 * 也較便於維護。
 * 
 * @see ManagementController
 * */
public interface MySwaggerForGW {
	
	/**
	 * 針對ManagementController的Swagger Annotation。
	 * */
	@MySwaggerAnnoForAllApi
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface MySwaggerForAllManagementApi {
		//nothing
	}
	
	
	/**
	 * 針對ViewTheStatusOfBusinessMicroServiceClusters的Swagger Annotation。
	 * */
	@MySwaggerAnnoForProtApi
    @Operation(
            summary = "The api to monitor the status of business micro-service-clusters.",
            description = "Requires authentication to execute, and the account authority must at least 'ADMIN'.",
            responses =  {
	                @ApiResponse(
	                        responseCode = "200",
	                        description = "A JSON object containing micro-service monitor data.",
	                        content = @Content(
	                        		schema = @Schema(
	                        				implementation = MonitorResTmpl.class)))})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MySwaggerForMonitorMicroService {
		//nothing
	}
	

}
