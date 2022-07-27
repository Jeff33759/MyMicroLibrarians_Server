package jeff.apigateway.filter;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.common.constants.SystemRole;
import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.handler.SecurityExceptionHandler;
import jeff.apigateway.model.dto.send.ActionFailedRes;

/**
 * 對只開放給系統內部其他Server訪問的API(微服務註冊請求、心跳請求等等...)
 * 進行來源認證並且授權。
 * 
 * @author Jeff Huang
 * */
@Component
public class SourceAuthenticationFilter extends OncePerRequestFilter{

	private static final Logger log = 
			LoggerFactory.getLogger(SourceAuthenticationFilter.class);

	@Autowired
	private SystemParam.Common commonParam;
	
	@Autowired
	private MyUtil util;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
		log.trace("====MicroServiceAuthorizationFilter Start====");
		authenticateTheSource(request,response,filterChain);
		log.trace("====MicroServiceAuthorizationFilter End====");
	}
	
	
	/**
	 * 先認證請求是否為可信任的服務端所發出，目前先以IPv4字串比對驗證。
	 * */
	private void authenticateTheSource(HttpServletRequest req, HttpServletResponse res,
			FilterChain chain) throws ServletException, IOException{
		String ipv4 = req.getRemoteAddr();
		if(commonParam.ALL_IP_SET.contains(ipv4)) {
			successfulAuthentication(req,res,chain);
		}else {
			unSuccessfulAuthentication(req,res,ipv4);
		}
	}
	
	/**
	 * 當認證成功，進行授權並放行。</p>
	 * 因為目前沒對各微服務的註冊與心跳進行授權分類(也許可以用在微服務優先級)，
	 * 因此帳、密隨便取個undefined；
	 * 目前也沒設計黑名單機制(可暫時禁止讓某些微服務註冊)，所以{@link User}物件
	 * 四個布林值都設為True。</p>
	 * */
	private void successfulAuthentication(HttpServletRequest req, 
			HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
		User identity  = new User("undefined","undefined",true,true,true,true,
				AuthorityUtils.commaSeparatedStringToAuthorityList(SystemRole.SYSTEM.name()));
//		生成一個認證通過的Authentication物件
		Authentication auth = 
				new UsernamePasswordAuthenticationToken(
						identity, null, identity.getAuthorities());
//		讓之後的業務邏輯可以取出(有需要的話)
		SecurityContextHolder.getContext().setAuthentication(auth);
		super.doFilter(req, res, chain);
	}
	
	/**
	 * 當認證失敗就不放行，並由這裡處理回應給客戶端。</p>
	 * 
	 * 若這邊用doFilter放行了，就會因為未授權而由{@link SecurityExceptionHandler #commence}
	 * 處理後續回應，那樣做好處是可以偽裝成一般請求授權失敗的模樣，至少不讓攻擊者知道他輸入的路徑
	 * 就是內部系統API的路徑，這裡先不做。</p>
	 * */
	private void unSuccessfulAuthentication(HttpServletRequest req, 
			HttpServletResponse res,String ipv4) throws IOException {
		log.warn("System internal APIs were accessed by an untrusted source. "
				+ "<IP>:" + ipv4);
		ActionFailedRes resObj = new ActionFailedRes();
		resObj.setPath(req.getRequestURI());
		resObj.setMethod(req.getMethod());
		resObj.setMsg("The source is not credible.");
		util.setFormatOfResponse(res, HttpStatus.UNAUTHORIZED.value(), resObj);
	}
	
	

	/**
	 * 只對已知的內部系統API的路徑，執行本過濾器邏輯。
	 * */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
		return !Stream.of(InnerApiConst.Receive.getAllPriApiMatcher())
				.anyMatch(matcher->matcher.matches(req));
	}

}
