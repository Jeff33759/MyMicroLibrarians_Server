package jeff.authorization.aop;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jeff.authorization.common.util.MyUtil;
import jeff.authorization.controller.AuthZController;
import jeff.authorization.exception.AuthorizationFieldEmptyException;
import jeff.authorization.model.dto.send.ActionFailedRes;
import jeff.authorization.serviceimpl.AuthZServiceImpl;

/**
 * 切面程式，攔截{@link AuthZController}所發生的例外，將其處理成期望的回應格式。</p>
 * 這裡沒有攔截到的，就會交給dispatcherServlet處理例外回應。</p>
 * 
 * 因為前端會在訪問某路徑的途中，先透過此伺服端對Token進行認證授權，
 * 所以本伺服端在回應狀態碼的選擇上，Token驗證相關的錯誤都盡量回401給gateway，好讓gateway
 * 告訴直接轉交給前端，讓前端知道這次的訪問之所以失敗，是沒有認證(沒有登入)導致，看前端是要導到
 * 登入頁面還是啥的；
 * 回給gateway的path和method也要保留原本前端想訪問的路徑。
 * 
 * @author Jeff Huang
 * */
@RestControllerAdvice(basePackageClasses = {AuthZController.class})
public class AuthZControllerExResAspect {
	
	@Autowired
	private MyUtil myUtil;
	
	/**
	 * 處理驗證AccessToken的流程中，所拋出的一切例外。
	 * @see AuthZServiceImpl
	 * */
    @ExceptionHandler(value = {ExpiredJwtException.class,
		    		MalformedJwtException.class,
		    		SignatureException.class,
		    		UnsupportedJwtException.class,
		    		AuthorizationFieldEmptyException.class})
    public void handleRestTemplateErrorResException(Exception e,
    		HttpServletRequest req, HttpServletResponse res) throws IOException {
    	ActionFailedRes resObj = new ActionFailedRes();
		resObj.setPath(req.getHeader("path"));
		resObj.setMethod(req.getHeader("method"));
		if(e instanceof ExpiredJwtException) {
//			Token過期
			resObj.setMsg("Your token is expired.");
			myUtil.setFormatOfResponse(res,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof MalformedJwtException){
//			Token內容構造異常，也許有人竄改Token
			resObj.setMsg("JWT was not correctly constructed.");
			myUtil.setFormatOfResponse(res,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof SignatureException) {
//			Signature解析後與內容不符，也許有人竄改Signature
			resObj.setMsg("Your token signature failed verification.");
			myUtil.setFormatOfResponse(res,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof UnsupportedJwtException) {
//			訪問者的Token格式對，但是簽名的加密演算法與parser的不同。
//			可能傳到RT，也可能是gateway重啟過，因為每次重啟，AT的Keypair都會刷新。
			resObj.setMsg("Your token signature algorithm is not expected, "
					+ "please check if the token is the correct access token, "
					+ "or log in again.");
			myUtil.setFormatOfResponse(res,HttpStatus.UNAUTHORIZED.value(),resObj);
		}else if(e instanceof AuthorizationFieldEmptyException) {
//			當訪問的標頭並未夾帶Token(基本上已經由gateway濾除掉)
			resObj.setMsg("Authorization field of HTTP header is empty.");
			myUtil.setFormatOfResponse(res,HttpStatus.BAD_REQUEST.value(),resObj);
		}
    }
    
    /**
     * 其他無法預期的例外。
     * */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ActionFailedRes handleException(Exception e, 
    		HttpServletRequest req, HttpServletResponse res) {
    	ActionFailedRes resObj = new ActionFailedRes();
		resObj.setPath(req.getHeader("path"));
		resObj.setMethod(req.getHeader("method"));
    	resObj.setMsg("An unexpected error occurs in the AuthZ server, "
    			+ "please contact the webmaster.");
    	return resObj;
    }

}
