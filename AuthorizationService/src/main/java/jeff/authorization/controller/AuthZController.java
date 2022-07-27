package jeff.authorization.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jeff.authorization.aop.AuthZControllerExResAspect;
import jeff.authorization.common.constants.InnerApiConst;
import jeff.authorization.exception.AuthorizationFieldEmptyException;
import jeff.authorization.service.AuthZService;
import jeff.authorization.serviceimpl.AuthZServiceImpl;

/**
 * 業務服務API。
 * 
 * @author Jeff Huang
 * @see AuthZControllerExResAspect
 * */
@RestController
public class AuthZController {
	
	
	
	@Autowired
	private AuthZService authZService;

	public AuthZController(AuthZServiceImpl authZServiceImpl) {
		this.authZService = authZServiceImpl;
	}

	/**
	 * 解析Accesstoken。</p>
	 * 因為只有要回傳一個東西，沒有其他欄位，
	 * 所以直接把Authentication序列化回傳即可，不用再Base64編碼成字串以JSON回傳。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.PARSING_AT)
	public ResponseEntity<?> parseAccessToken(HttpServletRequest req) throws Exception {
		String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
		if(authHeader!=null) {
			String token = authHeader.replace("Bearer ", ""); 
			byte[] bytes = 
					authZService.parseATAndAuthorizateAndSerialize(token);
			return ResponseEntity.ok(bytes);
		}else {
			throw new AuthorizationFieldEmptyException();
		}
	}

}
