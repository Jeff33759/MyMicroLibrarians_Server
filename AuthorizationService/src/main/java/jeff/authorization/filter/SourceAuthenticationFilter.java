package jeff.authorization.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jeff.authorization.common.param.SystemParam;
import jeff.authorization.common.util.MyUtil;
import jeff.authorization.model.dto.send.ActionFailedRes;

/**
 * 過濾不受信任的來源，目前只信任來自gateway的請求。
 * */
@Component
public class SourceAuthenticationFilter extends OncePerRequestFilter{

	private static final Logger log = 
			LoggerFactory.getLogger(SourceAuthenticationFilter.class);
	
	@Autowired
	private SystemParam systemParam;
	
	@Autowired
	private MyUtil util;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.trace("====SourceAuthenticationFilter Start====");
		authenticateTheSource(request,response,filterChain);
		log.trace("====SourceAuthenticationFilter End====");
	}
	
	/**
	 * 任證來源可不可信，目前可信的來源只有來自gateway Server的請求。
	 * */
	private void authenticateTheSource(HttpServletRequest req, HttpServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		String ipv4 = req.getRemoteAddr();
		if(ipv4.equals(systemParam.GATEWAY_IP)) {
			successfulAuthentication(req,res,filterChain);
		}else {
			unSuccessfulAuthentication(req,res,ipv4);
		}
	}
	
	/**
	 * 當確認來源為可信來源，直接放行。
	 * */
	private void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			FilterChain filterChain) throws ServletException, IOException {
		super.doFilter(req, res, filterChain);
	}
	
	/**
	 * 當確認來源為不可信來源。
	 * */
	private void unSuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res,
			String ipv4) 
			throws IOException {
		log.warn("The server was accessed by an untrusted source. "
				+ "<IP>:" + ipv4);
		ActionFailedRes resObj = new ActionFailedRes();
		resObj.setPath(req.getRequestURI());
		resObj.setMethod(req.getMethod());
		resObj.setMsg("The source is not credible.");
		util.setFormatOfResponse(res, HttpStatus.UNAUTHORIZED.value(), resObj);
	}
	
}
