package jeff.apigateway.handler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.exception.AuthorizationFieldEmptyException;
import jeff.apigateway.exception.RestTemplateErrorResException;
import jeff.apigateway.filter.AccessTokenAuthorizationFilter;
import jeff.apigateway.model.dto.send.ActionFailedRes;

/**
 * 集中處理Token授權認證時發生的一些例外，方便管理。
 * 
 * @author Jeff Huang
 * */
@Component
public class TokenAuthenticationExceptionHandler {
	
	@Autowired
	private MyUtil util;
	
	/**
	 * 處理驗證RefreshToken的流程中，所拋出的一切例外。
	 * @see AccessTokenAuthorizationFilter
	 * @see MyRestTemplateErrorHandlerForForwarding
	 * */
	public void transformExceptionToResponse(Exception e, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
//		當收到AuthZ回應非200的狀態碼
		if(e instanceof RestTemplateErrorResException) {
			RestTemplateErrorResException castE = 
					(RestTemplateErrorResException) e;
//			設置CORS用的標頭，讓此錯誤回應可以用於WEB瀏覽器的JS
//			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setStatus(castE.getCode().value());
			response.setContentType("application/json");
			response.getWriter().print(castE.getErrMsg());
//		除了RestTemplateErrorResException以外的例外，代表是真正的例外而非正常行為
		}else {
			ActionFailedRes resObj = new ActionFailedRes();
	    	resObj.setMethod(request.getMethod());
	    	resObj.setPath(request.getRequestURI());
//			連線相關例外，與AuthZ連接不上或者等候回應超時
			if(e instanceof ResourceAccessException){
				handleResourceAccessException(e,resObj,response);
//			標頭沒放Token
			}else if(e instanceof AuthorizationFieldEmptyException) {
				resObj.setMsg("Authorization field of HTTP header is empty, "
						+ "you have to login and use the access token to access the api.");
				util.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
			}else {
//				其他非預期的例外
				resObj.setMsg("An unexpected error has occurred in the gateway server, "
						+ "please contact the webmaster.");
				util.setFormatOfResponse(response,HttpStatus.INTERNAL_SERVER_ERROR.value(),resObj);
			}
		} 
	}
	
	/**
	 * 將有關{@link ResourceAccessException}的例外處理成期望的回應。
	 * */
	private void handleResourceAccessException(Exception e,
			ActionFailedRes resObj, HttpServletResponse res) throws IOException {
    	Throwable cause = e.getCause();
//    	連線超時(網路不穩或微服務突然關閉)
    	if(cause instanceof ConnectException) {
    		resObj.setMsg("The gateway is not connected to the business server, "
    				+ "please contact the webmaster.");
    		util.setFormatOfResponse(res, HttpStatus.BAD_GATEWAY.value(), resObj);
//    	有連接上，但等待回應超時，可能微服務太忙或當掉
    	}else if(cause instanceof SocketTimeoutException) {
    		resObj.setMsg("Business server response timed out, "
    				+ "please try again later");
    		util.setFormatOfResponse(res, HttpStatus.GATEWAY_TIMEOUT.value(), resObj);
//    	其他連線相關的無法預期的例外
    	}else {
    		resObj.setMsg("An unexpected error occurs "
    				+ "when the gateway connects to the business server, "
    				+ "please contact the webmaster.");
    		util.setFormatOfResponse(res, HttpStatus.BAD_GATEWAY.value(), resObj);
    	}
	}

}
