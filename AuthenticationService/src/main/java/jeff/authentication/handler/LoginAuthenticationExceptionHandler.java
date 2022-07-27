package jeff.authentication.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;

import jeff.authentication.common.util.MyUtil;
import jeff.authentication.exception.MyAccountNotFoundException;
import jeff.authentication.exception.LoginJsonFormatException;
import jeff.authentication.filter.CustomLoginAuthFilter;
import jeff.authentication.model.dto.send.ActionFailedRes;


/**
 * 登入認證處理器，在此撰寫認證失敗後的行為。
 * 當{@link CustomLoginAuthFilter}認證失敗後，會呼叫此處理器進行處理。
 * </p>
 * 
 * @author Jeff Huang
 * @see MyFilterProvider
 * */
@Component
public class LoginAuthenticationExceptionHandler {

	@Autowired
	private MyUtil myUtil;
	
	/**
	 * 當認證失敗的時候。
	 * 
	 * @apiNote 認證過程中可能拋出的例外，可以參照{@link AuthenticationManager}。
	 * */
	public void transformExceptionToResponse(Exception e, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ActionFailedRes resObj = new ActionFailedRes();
		resObj.setPath(request.getRequestURI());
		resObj.setMethod(request.getMethod());
		if(e instanceof MyAccountNotFoundException) {
//			帳號不存在
			resObj.setMsg("Id doesn't exsist in DB.");
			myUtil.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof BadCredentialsException) {
//			帳號存在，但密碼錯誤
			resObj.setMsg("Wrong password.");
			myUtil.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof DisabledException) {
//			帳密都對，但User物件的enable狀態為false，可能被軟刪除或黑名單
			resObj.setMsg("Account is not enabled.");
			myUtil.setFormatOfResponse(response,HttpStatus.FORBIDDEN.value(),resObj);
		}else if(e instanceof LoginJsonFormatException) {
//			客戶端傳來的參數錯誤
			resObj.setMsg("The JSON is not expected.");
			myUtil.setFormatOfResponse(response,HttpStatus.BAD_REQUEST.value(),resObj);
		}else {
//			其它不可預期的錯誤
			resObj.setMsg("An unexpected error has occurred in the AuthN server, please contact the webmaster.");
			myUtil.setFormatOfResponse(response,HttpStatus.INTERNAL_SERVER_ERROR.value(),resObj);
		}
	}
	
}
