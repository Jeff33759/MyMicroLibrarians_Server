package jeff.apigateway.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.config.SecurityConfig;
import jeff.apigateway.model.dto.send.ActionFailedRes;

/**
 * 客製化Security請求認證的進入點，以及授權訪問失敗的行為。</p>
 * 
 * @author Jeff Huang
 * @see SecurityConfig
 * */
@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler{

	@Autowired
	private MyUtil myUtil;
	
	/**
	 * 當訪問者未通過認證(未登入)時的處理。</p>
	 * 如果回應還沒有被處理過，狀態就會是200，於是在這裡處理。
	 * 在{@link SecurityConfig #securityFilterChain}中，
	 * 被設為permitAll的URL將不會導到這裡；
	 * 被設為Authenticated的URL，訪問控制器前會先跑認證流程，若沒通過認證將導到這裡；
	 * 被設為denyAll的URL，無論有無認證通過，都會導到這裡。</p>
	 * */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		if(response.getStatus()==200) {
			String errMsg = "Unauthorized request, " 
					+ "please check authentication or request URL is correct.";
			ActionFailedRes resObj = new ActionFailedRes();
			resObj.setMsg(errMsg);
			resObj.setPath(request.getRequestURI());
			resObj.setMethod(request.getMethod());
			myUtil.setFormatOfResponse(response,HttpStatus.UNAUTHORIZED.value(),resObj);
		}	
	}

	
	/**
	 * 當認證成功的訪問者欲訪問受保護的資源，但是權限不足。</p>
	 * 如果回應還沒有被處理過，狀態就會是200，於是在這裡處理。
	 * */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if(response.getStatus()==200) {
			String errMsg = "Forbidden request, " 
					+ "you do not have enough authorization to perform this operation.";
			ActionFailedRes resObj = new ActionFailedRes();
			resObj.setMsg(errMsg);
			resObj.setPath(request.getRequestURI());
			resObj.setMethod(request.getMethod());
			myUtil.setFormatOfResponse(response,HttpStatus.FORBIDDEN.value(),resObj);
		}
	}
	
	
}
