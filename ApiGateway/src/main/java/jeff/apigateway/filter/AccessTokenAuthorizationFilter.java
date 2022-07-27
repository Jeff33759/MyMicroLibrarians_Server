package jeff.apigateway.filter;

import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.common.constants.OpenApiConst;
import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.config.MyComponentConfig;
import jeff.apigateway.exception.AuthorizationFieldEmptyException;
import jeff.apigateway.handler.TokenAuthenticationExceptionHandler;


/**
 * 驗證夾帶的AccessToken是否有效，並且授權。
 * 
 * @author Jeff Huang
 * */
@Component
public class AccessTokenAuthorizationFilter extends OncePerRequestFilter {

	private static final Logger log = 
			LoggerFactory.getLogger(AccessTokenAuthorizationFilter.class);
	
	/**
	 * @see {@link MyComponentConfig}
	 * */
	@Resource(name="parseATRestTemplateBean")
	private RestTemplate restTemplate;
	
	@Autowired
	private SystemParam.AuthZ authZParam;
	
	@Autowired
	private TokenAuthenticationExceptionHandler tokenAuthExHandler;
	
	@Autowired
	private MyUtil util;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.trace("====AccessTokenAuthorizationFilter Start====");
		parseAcceccTokenAndAuthorizeUser(request, response);
		super.doFilter(request, response, filterChain);
		log.trace("====AccessTokenAuthorizationFilter End====");
	}
	
	/**
	 * 利用{@link RestTemplate}發請求給AuthorizationServer，解析AcceccToken，進行授權。</p>
	 * 為了在Token解析失敗時，回傳給前端的錯誤訊息保留前端原本想訪問的path，所以把
	 * path和method設進標頭，傳給AuthZ，這樣AuthZ回傳給gateway的錯誤訊息，path就不會是
	 * "/authz/parsing/at"，而是前端原本想訪問的路徑，然後gateway再將其原封不動回傳給前端。
	 * */
	private void parseAcceccTokenAndAuthorizeUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.trace("***parseAcceccTokenAndAuthorizeUser***");
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authHeader!=null) {
			try {
				String url = 
						"http://" + authZParam.IP + ":" + authZParam.PORT
						+ InnerApiConst.Send.Path.AUTHZ_PARSING_AT;
				HttpHeaders headers = new HttpHeaders();
				headers.setBearerAuth(request.getHeader(HttpHeaders.AUTHORIZATION));
				headers.set("path", request.getRequestURI());
				headers.set("method", request.getMethod());
				HttpEntity<?> reqObj = new HttpEntity<>(headers);
				ResponseEntity<byte[]> res = 
						restTemplate.exchange(url, HttpMethod.GET, reqObj, 
								byte[].class);
				successfulAccessTokenParsing(res);
			}catch(Exception e) {
				unSuccessfulAccessTokenParsing(e,request,response);
			}
		}else {
			unSuccessfulAccessTokenParsing(
				new AuthorizationFieldEmptyException(),
					request,response);
		}
	}
	
	/**
	 * 當JWT解析並認證成功，就將認證後的{@link Authentication}反序列化，並且授權。</p>
	 * */
	private void successfulAccessTokenParsing(ResponseEntity<byte[]> res) throws Exception {
		byte[] bytes = res.getBody();
		Authentication auth = 
				(Authentication)util.deserializeBytes(bytes);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	
	
	/**
	 * 當JWT解析失敗，可在此先進行一些預處理，
	 * 再委託給{@link TokenAuthenticationExceptionHandler}。
	 * */
	private void unSuccessfulAccessTokenParsing(Exception e,HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		tokenAuthExHandler.transformExceptionToResponse(e, req, res);
	}
	
	/**
	 * 只對受保護的開放API，執行過濾邏輯。
	 * */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
		return !Stream.of(OpenApiConst.getAllProtApiMatcher())
				.anyMatch(matcher->matcher.matches(req));
	}

}
