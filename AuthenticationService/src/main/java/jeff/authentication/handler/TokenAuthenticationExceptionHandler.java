package jeff.authentication.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;

import jeff.authentication.common.util.MyUtil;
import jeff.authentication.exception.AuthorizationFieldEmptyException;
import jeff.authentication.exception.MyAccountNotFoundException;
import jeff.authentication.filter.RefreshTokenAuthenticationFilter;
import jeff.authentication.model.dto.send.ActionFailedRes;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * 集中處理Token授權認證時發生的一些例外，方便管理。
 * 
 * @author Jeff Huang
 * */
@Component
public class TokenAuthenticationExceptionHandler {
	
	@Autowired
	private MyUtil myUtil;
	
	
	/**
	 * 處理驗證RefreshToken的流程中，所拋出的一切例外。
	 * @see RefreshTokenAuthenticationFilter
	 * */
	public void transformExceptionToResponse(Exception e, HttpServletRequest request,
			HttpServletResponse response) 
			throws IOException {
		ActionFailedRes resObj = new ActionFailedRes();
		resObj.setPath(request.getRequestURI());
		resObj.setMethod(request.getMethod());
		if(e instanceof ExpiredJwtException) {
//			Token過期
			resObj.setMsg("Your token is expired.");
			myUtil.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof MalformedJwtException){
//			Token內容構造異常，也許有人竄改Token
			resObj.setMsg("JWT was not correctly constructed.");
			myUtil.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof SignatureException) {
//			Signature解析後與內容不符，也許有人竄改Signature
			resObj.setMsg("Your token signature failed verification.");
			myUtil.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof UnsupportedJwtException) {
//			訪問者的Token格式對，但是簽名的加密演算法與parser的不同(可能傳到AT)
			resObj.setMsg("Your token signature algorithm is not expected, "
					+ "please check if the token is the correct refresh token.");
			myUtil.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof AuthorizationFieldEmptyException) {
//			當訪問的標頭並未夾帶Token
			resObj.setMsg("Authorization field of HTTP header is empty.");
			myUtil.setFormatOfResponse(response,HttpStatus.BAD_REQUEST.value(),resObj);
		}else if(e instanceof DisabledException) {
//			JWT解析正常，但帳號已是非啟用狀態
			resObj.setMsg("The account is not enabled, so the token cannot be refreshed.");
			myUtil.setFormatOfResponse(response,HttpStatus.FORBIDDEN.value(),resObj);
		}else if(e instanceof MyAccountNotFoundException) {
//			JWT解析正常，但解析出來的ID卻沒在資料庫內，也許後台人為誤刪
			resObj.setMsg("The JWTs is parsed correctly,but the account is not in the DB, please contact the webmaster.");
			myUtil.setFormatOfResponse(response,HttpStatus.INTERNAL_SERVER_ERROR.value(),resObj);
		}else {
//			其他非預期的例外
			resObj.setMsg("An unexpected error has occurred in the AuthN server, please contact the webmaster.");
			myUtil.setFormatOfResponse(response,HttpStatus.INTERNAL_SERVER_ERROR.value(),resObj);
		}
	}
	
}
