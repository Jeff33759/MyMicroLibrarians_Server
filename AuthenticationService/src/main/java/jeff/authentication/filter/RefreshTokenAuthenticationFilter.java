package jeff.authentication.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jeff.authentication.common.constants.OpenApiConst;
import jeff.authentication.controller.AuthNController;
import jeff.authentication.exception.AuthorizationFieldEmptyException;
import jeff.authentication.handler.TokenAuthenticationExceptionHandler;
import jeff.authentication.model.bo.CustomUserDetails;
import jeff.authentication.service.JWTService;

import io.jsonwebtoken.Claims;

/**
 * 驗證夾帶的RefreshToken是否有效，並且認證。
 * 
 * @author Jeff Huang
 * */
@Component
public class RefreshTokenAuthenticationFilter extends OncePerRequestFilter{
	
	private static final Logger log = 
			LoggerFactory.getLogger(RefreshTokenAuthenticationFilter.class);
	
	/**
	 * 用來匹配本過濾器邏輯。
	 * */
	private final RequestMatcher reqMatcher = 
			new AntPathRequestMatcher(OpenApiConst.Path.AUTHN_RF_AT, 
					OpenApiConst.HttpMethods.AUTHN_RF_AT.name());
	
	private final JWTService jWTService;

	private final UserDetailsService userDetailsService;

	@Autowired
	private TokenAuthenticationExceptionHandler tokenAuthExHandler;

	@Autowired
	public RefreshTokenAuthenticationFilter(JWTService jWTServiceImpl,
			UserDetailsService userDetailsServiceImpl) {
		this.jWTService = jWTServiceImpl;
		this.userDetailsService = userDetailsServiceImpl;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
		log.trace("====RefreshTokenAuthenticationFilter Start====");
		parseRefreshTokenAndAuthenticateUser(request,response);
	    filterChain.doFilter(request, response);
	    log.trace("====RefreshTokenAuthenticationFilter End====");
	}
	

	/**
	 * 從請求標頭取出Token進行解析，若JWT解析失敗，交由{@link TokenAuthenticationExceptionHandler}處理後續回應。</p>
	 * JWT解析成功後，執行授權認證，若認證失敗，同樣交由{@link TokenAuthenticationExceptionHandler}處理後續回應。</p>
	 * 認證成功後，跑{@link AuthNController}重新刷新AT給對方。</p>
	 * */
	private void parseRefreshTokenAndAuthenticateUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.trace("***parseRefreshTokenAndAuthenticateUser***");
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authHeader!=null) {
			try {
				String token = authHeader.replace("Bearer ", ""); 
//				若Token過期或驗證沒過，這裡就會跳錯
				Claims claims = 
						jWTService.parseRefreshTokenToGetClaims(token);
				String id = (String)claims.get("id");
				successfulRefreshTokenParsing(id,request,response);
			}catch(Exception e) {
				unSuccessfulRefreshTokenParsing(e,request,response);
			}
		}else {
			unSuccessfulRefreshTokenParsing(
					new AuthorizationFieldEmptyException(),request,response);
		}
	}
	
	
	/**
	 * 當JWT解析成功，接著進行身分驗證，若驗證成功，便把用戶的資料設置進{@link SecurityContext}，並在{@link AuthNController}處理；
	 * 若過程中發生例外，就委託給{@link TokenAuthenticationExceptionHandler}進行處理。
	 * */
	private void successfulRefreshTokenParsing(String id,HttpServletRequest request ,HttpServletResponse response) throws IOException {
		try {
			CustomUserDetails myUserDetails = 
					(CustomUserDetails)userDetailsService.loadUserByUsername(id);
			Authentication auth = verificateUserStatus(myUserDetails);
			successfulUserAuthentication(auth);
		}catch(Exception e) {
			unSuccessfulUserAuthentication(e,request,response);
		}
	}

	/**
	 * 當JWT解析失敗，可在此先進行一些預處理，
	 * 再委託給{@link TokenAuthenticationExceptionHandler}。
	 * */
	private void unSuccessfulRefreshTokenParsing(Exception e, HttpServletRequest request, 
			HttpServletResponse response) 
			throws IOException {
		tokenAuthExHandler.transformExceptionToResponse(e,request,response);
	}
	
	/**
	 * 當身分驗證發生例外(失敗)，委託給{@link TokenAuthenticationExceptionHandler}進行處理。
	 * */
	private void unSuccessfulUserAuthentication(Exception e, HttpServletRequest request,
			HttpServletResponse response) 
			throws IOException {
		tokenAuthExHandler.transformExceptionToResponse(e,request,response);
	}
	
	/**
	 * 當身分驗證成功，進行授權。
	 * */
	private void successfulUserAuthentication(Authentication auth) {
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	
	/**
	 * 驗證UserDetails的4個代表使用者狀態的布林值，
	 * 因為目前只有用到一個enable，所以只判斷一個。
	 * 
	 * @throws DisabledException 如果用戶被禁用，拋出此例外。
	 * @return {@link Authentication} - 若用戶沒被禁用，回傳一個認證通過的Authentication物件。
	 * */
	private Authentication verificateUserStatus(CustomUserDetails user) throws DisabledException{
		if(user.isEnabled()) {
//			生成一個認證通過的Authentication物件
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		throw new DisabledException("Account is not enabled.");
	}
	
	
	/**
	 * 只有刷新AT的路徑會執行本過濾器的邏輯(驗證RT並授權)。
	 * */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !reqMatcher.matches(request);
	}
	
}
