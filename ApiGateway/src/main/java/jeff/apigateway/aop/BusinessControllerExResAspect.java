package jeff.apigateway.aop;


import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.controller.business.AuthenticationController;
import jeff.apigateway.controller.business.BookController;
import jeff.apigateway.exception.PathVarIsBlankException;
import jeff.apigateway.exception.RestTemplateErrorResException;
import jeff.apigateway.handler.MyRestTemplateErrorHandlerForForwarding;
import jeff.apigateway.model.dto.send.ActionFailedRes;

/**
 * 切面程式，攔截{@link jeff.apigateway.controller.business}底下
 * 的控制器所發生的例外，將其處理成期望的回應格式。</p>
 * 這裡沒有攔截到的，就會交給dispatcherServlet處理例外回應。
 * 
 * @author Jeff Huang
 * */
@RestControllerAdvice(basePackages = {"jeff.apigateway.controller.business"})
public class BusinessControllerExResAspect {
	
	@Autowired
	private MyUtil util;
	
	/**
     * 當路徑參數為空或者純空白。</p>
     * 原本此驗證是打算做在各微服務，但礙於restTemplate的特性，
     * 只能將此驗證做在gateway裡面，詳細說明看see also。
     * 
     * @see BookController #queryBookById
     * */
    @ExceptionHandler(value = {PathVarIsBlankException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ActionFailedRes handlePathVarIsBlankException(
    		PathVarIsBlankException pvibe, HttpServletRequest req,
    			HttpServletResponse res) {
    	ActionFailedRes resObj = new ActionFailedRes();
    	resObj.setMethod(req.getMethod());
    	resObj.setPath(req.getRequestURI());
    	resObj.setMsg(pvibe.getMessage());
    	return resObj;
    }
	
	
    /**
     * 攔截轉接請求時，從各微服務得到非200回應時，gateway不做其他處理(例如重新包裝成DTO)，
     * 而是直接把回應丟給客戶端。</p>
     * 因為{@code rterE.getErrMsg()}得出來是字串，我們又沒將其重新包裝成DTO，
     * 所以設置{@link @RestControllerAdvice}也不會自動轉換，
     * 要手動設置contentType為JSON，客戶端才會得到JSON。
     * 
     * @see AuthenticationController
     * @see BookController
     * @see MyRestTemplateErrorHandlerForForwarding
     * */
    @ExceptionHandler(value = {RestTemplateErrorResException.class})
    public ResponseEntity<?> handleRestTemplateErrorResException(
    		RestTemplateErrorResException rterE, HttpServletResponse res) {
    	return ResponseEntity
    			.status(rterE.getCode())
    			.contentType(MediaType.APPLICATION_JSON)
    			.body(rterE.getErrMsg());
    }
    
    
    /**
     * 處理連線超時以及等待回應超時的情況。
     * */
    @ExceptionHandler(value = {ResourceAccessException.class})
    public void handleResourceAccessException(
    		ResourceAccessException rae, HttpServletRequest req, 
    		HttpServletResponse res) throws IOException {
    	Throwable cause = rae.getCause();
    	ActionFailedRes resObj = new ActionFailedRes();
    	resObj.setMethod(req.getMethod());
    	resObj.setPath(req.getRequestURI());
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
    
    /**
     * gateway本身發生了其他無法預期的例外。
     * */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ActionFailedRes handleException(
    		Exception e, HttpServletRequest req,
    			HttpServletResponse res) {
    	ActionFailedRes resObj = new ActionFailedRes();
    	resObj.setMethod(req.getMethod());
    	resObj.setPath(req.getRequestURI());
    	resObj.setMsg("An unexpected error occurs in the gateway server, "
    			+ "please contact the webmaster.");
    	return resObj;
    }
    
}
