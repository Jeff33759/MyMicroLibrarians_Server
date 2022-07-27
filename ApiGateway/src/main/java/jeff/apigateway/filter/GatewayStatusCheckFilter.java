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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.model.dto.send.ActionFailedRes;

/**
 * 檢查gateway是否服務中的過濾器。
 * 若AuthN或AuthZ其中一個微服務沒註冊，gateway就不開放給外部訪問。
 * */
@Component
public class GatewayStatusCheckFilter extends OncePerRequestFilter{

	private static final Logger log = 
			LoggerFactory.getLogger(GatewayStatusCheckFilter.class);
	
	@Autowired
	private SystemParam.Common commonParam;
	
	@Autowired
	private MyUtil util;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.trace("====GatewayStatusCheckFilter Start====");
		checkGatewayIsInServiceOrNot(request,response,filterChain);
		log.trace("====GatewayStatusCheckFilter End====");
	}
	
	/**
	 * 檢查gateway是否對外服務中，若否，就503拒絕請求。
	 * */
	private void checkGatewayIsInServiceOrNot(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain) 
					throws IOException, ServletException {
		if(commonParam.GW_IN_SERVICE) {
			super.doFilter(request, response, filterChain);
		}else {
			rejectRequest(request,response);
		}
	}
	
	/**
	 * 用503拒絕外部訪問。
	 * */
	private void rejectRequest(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		ActionFailedRes res = new ActionFailedRes();
		res.setMethod(request.getMethod());
		res.setPath(request.getRequestURI());
		res.setMsg("The gateway currently does not provide external services.");
		util.setFormatOfResponse(response, HttpStatus.SERVICE_UNAVAILABLE.value(), res);
	}
	
	/**
	 * 只對非私人API，也就是公開給外部的API的路徑，執行本過濾器邏輯。
	 * */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
		return Stream.of(InnerApiConst.Receive.getAllPriApiMatcher())
				.anyMatch(matcher->matcher.matches(req));
	}
	
}
