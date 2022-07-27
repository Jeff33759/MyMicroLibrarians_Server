package jeff.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jeff.authentication.common.constants.OpenApiConst;
import jeff.authentication.filter.RefreshTokenAuthenticationFilter;
import jeff.authentication.model.bo.CustomUserDetails;
import jeff.authentication.model.dto.receive.LoginReq;
import jeff.authentication.model.dto.send.LoginSuccessRes;
import jeff.authentication.model.dto.send.RefreshATSuccessRes;
import jeff.authentication.service.AuthService;
import jeff.authentication.serviceimpl.AuthServiceImpl;
import jeff.authentication.serviceimpl.HandshakeServiceImpl;

/**
 * 業務邏輯API。
 * 
 * @author Jeff Huang
 * */
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
public class AuthNController {
	
	private final AuthService authService;
	
	@Autowired
	public AuthNController(AuthServiceImpl authServiceImpl,
			HandshakeServiceImpl commServiceImpl) {
		this.authService = authServiceImpl;
	}
	

	/**
	 * 使用者登入的API，通過身份驗證後，回傳含有AT、RT與簽名的完整JWTs
	 * 
	 * @param {@link LoginReq}
	 *		  
	 * */
	@PostMapping(path = OpenApiConst.Path.AUTHN_LOGIN)
	public ResponseEntity<LoginSuccessRes> login() {
		try {
			CustomUserDetails authenticatedUser = 
					getAuthenticatedUserDetailsFromSecurityContext();
			LoginSuccessRes res = authService.generateFullJWTs(authenticatedUser);
			return ResponseEntity.ok(res);
		}catch(Exception e) {
//			製作Token過程中發生無法預期的例外
			String errMsg = "An unexpected error has occurred when "
					+ "generating refresh token and access token, please contact the webmaster.";
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, errMsg);
		}
	}
	
	
	/**
	 * 刷新Access Token的API。
	 * 經{@link RefreshTokenAuthenticationFilter}認證RefreshToken有效後，
	 * 回傳刷新後的AccessToken。
	 * 
	 * @exception ResponseStatusException 
	 * 			  若製作AT途中發生例外，就拋出。
	 * */
	@PostMapping(path = OpenApiConst.Path.AUTHN_RF_AT)
	public ResponseEntity<RefreshATSuccessRes> refreshAccessToken() 
			throws ResponseStatusException{
		try {
			CustomUserDetails authenticatedUser = 
					getAuthenticatedUserDetailsFromSecurityContext();
			RefreshATSuccessRes res = authService.refreshAT(authenticatedUser);
			return ResponseEntity.ok(res);
		}catch(Exception e) {
//			製作Token過程中發生無法預期的例外
			String errMsg = "An unexpected error has occurred when "
					+ "generating access token, please contact the webmaster.";
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, errMsg);
		}
	}
	
	

	
	/**
	 * 取出認證完成的UserDetails物件，內有所需的使用者資訊。
	 * SecurityContext底層默認模式是ThreadLocal，是執行緒安全的，
	 * 每個請求都只會取用到自己的資料。
	 * */
	private CustomUserDetails getAuthenticatedUserDetailsFromSecurityContext() {
		return (CustomUserDetails)SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
	}

}
