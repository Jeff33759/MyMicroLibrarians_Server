package jeff.apigateway.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.config.InterceptorConfig;
import jeff.apigateway.model.dto.send.ActionFailedRes;

/**
 * Filter chain -> Dispatcher servlet -> Iterceptor chain(preHandle) -> AOP chain 
 * -> Controller -> Some business logic
 * -> AOP chain -> Iterceptor chain(postHandle) -> DispatcherServlet 
 * -> Iterceptor chain(afterCompletion) -> Filter chain.
 * 
 * @author Jeff Huang
 * @see {@link InterceptorConfig}
 * */
@Component
public class BookInterceptor implements HandlerInterceptor{

	private static final Logger log = 
			LoggerFactory.getLogger(BookInterceptor.class);

	@Autowired
	private SystemParam.Book bookParam;

	@Autowired
	private SystemParam.Book2 book2Param;
	
	@Autowired
	private MyUtil util;

	/**
	 * 在進入Book控制器前，先檢查BookService是否已經註冊，有註冊
	 * 才提供相關的服務給外部訪問。</p> 
	 * 此方法回傳true才會繼續下跑。
	 * */
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		log.trace("====BookInterceptor start====");
		
		boolean flag = bookParam.IN_SERVICE || book2Param.IN_SERVICE;
		if(!flag) {
			rejectRequest(request,response);
		}
		
		return flag;
	}
	
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.trace("====BookInterceptor end====");
	}



	/**
	 * 用503拒絕外部訪問。
	 * */
	private void rejectRequest(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		ActionFailedRes res = new ActionFailedRes();
		res.setMethod(request.getMethod());
		res.setPath(request.getRequestURI());
		res.setMsg("Book service is currently unavailable.");
		util.setFormatOfResponse(response, HttpStatus.SERVICE_UNAVAILABLE.value(), res);
	}
	
}
