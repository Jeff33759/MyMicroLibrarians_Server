package jeff.apigateway.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jeff.apigateway.exception.HasNotRegisteredException;
import jeff.apigateway.model.dto.send.ActionFailedRes;
import jeff.apigateway.serviceimpl.HeartBeatServiceImpl;


/**
 * 切面程式，攔截{@link jeff.apigateway.controller.handshake}底下
 * 的控制器所發生的例外，將其處理成期望的回應格式。</p>
 * 這裡沒有攔截到的，就會交給dispatcherServlet處理例外回應。
 * 
 * @author Jeff Huang
 * */
@RestControllerAdvice(basePackages = {"jeff.apigateway.controller.handshake"})
public class HandshakeControllerExResAspect {

	/**
	 * 攔截有關於微服務傳送心跳請求時，還沒有先註冊的例外。
	 * 
	 * @see {@link HeartBeatServiceImpl}
	 * */
    @ExceptionHandler(value = {HasNotRegisteredException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ActionFailedRes handleHasNotRegisteredException(
    		Exception ex, HttpServletRequest request) {
    	ActionFailedRes res = new ActionFailedRes();
    	res.setMsg(ex.getMessage());
    	res.setPath(request.getRequestURI());
    	res.setMethod(request.getMethod());
        return res;
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
