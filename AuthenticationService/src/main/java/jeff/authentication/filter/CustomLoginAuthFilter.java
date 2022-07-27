package jeff.authentication.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jeff.authentication.common.constants.OpenApiConst;
import jeff.authentication.exception.LoginJsonFormatException;
import jeff.authentication.handler.LoginAuthenticationExceptionHandler;
import jeff.authentication.model.dto.receive.LoginReq;
import jeff.authentication.serviceimpl.UserDetailsServiceImpl;



/**
 * 自製的登入認證過濾器。</p>
 * 因Security過濾鏈中的{@link #UsernamePasswordAuthenticationFilter}無法實現前後端
 * 分離的認證，因此這裡自製一個登入過濾器並插入Security過濾鏈中，從而客製化Security的認證機制。</p>
 * 
 * 為了解決循環依賴的問題，本過濾器不註冊為Spring元件，而是交給{@link MyFilterProvider}控制
 * 控制本過濾器的創建與注入。
 * 
 * @author Jeff Huang
 */
public class CustomLoginAuthFilter extends OncePerRequestFilter{

	private static final Logger log = 
			LoggerFactory.getLogger(CustomLoginAuthFilter.class);
	
	private final ObjectMapper objectMapper;
	
	/**
	 * 用來匹配本過濾器邏輯。
	 * */
	private final RequestMatcher reqMatcher = 
			new AntPathRequestMatcher(OpenApiConst.Path.AUTHN_LOGIN, 
					OpenApiConst.HttpMethods.AUTHN_LOGIN.name());
	
	private final AuthenticationManager authManage;
	
	private final LoginAuthenticationExceptionHandler loginAuthExceptionHandler;
	
	/**
	 * 使用protected修飾建構子，不讓外部隨便呼叫並創建實例。</p>
	 * 若外部想要得到實例，只能透過{@link MyFilterProvider}來創建並獲取實例，藉此控制本類別於Spring容器內的實體數量。</p>
	 * */
	protected CustomLoginAuthFilter(AuthenticationManager authManage, 
			LoginAuthenticationExceptionHandler loginAuthExceptionHandler,
			ObjectMapper objectMapper) {
		this.authManage = authManage;
		this.loginAuthExceptionHandler = loginAuthExceptionHandler;
		this.objectMapper = objectMapper;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.trace("====CustomLoginAuthFilter Start====");
		myLoginAuthenticationProcess(request, response);
		super.doFilter(request, response, filterChain);
		log.trace("====CustomLoginAuthFilter End====");
	}
	
	/**
	 * 自製認證流程，實現客製化的登入邏輯。
	 * 從Request Body中取出帳密，進行驗證。
	 * 
	 * @exception AuthenticationException 若認證失敗時發生。
	 * */
	private void myLoginAuthenticationProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		log.trace("***loginAuthenticationProcess***");
		try {
			LoginReq loginReq = getContentToSto(request);
//			生成一個未通過認證的Authentication
			Authentication auth = 
					new UsernamePasswordAuthenticationToken(loginReq.getId(), loginReq.getPassword());
//			經過認證程序，若帳密都對，即回傳通過認證的Authentication
			auth = authManage.authenticate(auth);
			successfulUserAuthentication(auth);
		}catch(Exception e) {
			unsuccessfulUserAuthentication(e,request,response);
		}
	}
	
	/**
	 * 認證失敗時，先進行一些預處理，再把例外委託給{@link LoginAuthenticationExceptionHandler}。</p>
	 * 因為想把id不存在以及id存在但密碼錯誤的情況分開，所以進行預處理。
	 * 
	 * @see {@link UserDetailsServiceImpl #loadUserByUsername}
	 * 
	 * */
	private void unsuccessfulUserAuthentication(Exception e,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if(e.getCause()!=null) {
			e = (Exception) e.getCause();
		}
		loginAuthExceptionHandler.transformExceptionToResponse(e,request,response);
	}

	/**
	 * 當認證成功要做的動作。
	 * */
	private void successfulUserAuthentication(Authentication auth) {
		SecurityContextHolder.getContext().setAuthentication(auth);
	}


	/**
	 * 獲取請求的內容，轉換成JAVA物件。
	 * 
	 * @exception LoginJsonFormatException 當客戶端傳的json格式不符，跳此例外。
	 * */
	private LoginReq getContentToSto(HttpServletRequest request) throws LoginJsonFormatException {
//		ObjectMapper mapper = new ObjectMapper();
		try {
			String contentStr = new String(request.getInputStream().readAllBytes())
					.replaceAll("[\n\t]", "");
			return objectMapper.readValue(contentStr, LoginReq.class);
		} catch (IOException e) {
//			將IOException包裝成LoginJsonFormatException。
			throw new LoginJsonFormatException();
		}
	}
	
	/**
	 * 只有登入的路徑，會執行本過濾器的邏輯(驗證帳密並授權)
	 * */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !reqMatcher.matches(request);
	}

	
}
