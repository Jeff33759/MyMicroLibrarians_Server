package jeff.book2.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jeff.book2.config.InterceptorConfig;

/**
 * 將業務邏輯執行成功時，回應標頭加上本伺服端名稱的攔截器。
 * 
 * @author Jeff Huang
 * @see InterceptorConfig
 * */
@Component
public class AddResHeaderInterceptor implements HandlerInterceptor{
	
	private static final Logger log = 
			LoggerFactory.getLogger(AddResHeaderInterceptor.class);
	
	/**
	 * 此方法回傳true才會繼續下跑。
	 * */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.trace("====AddResHeaderInterceptor start====");
		response.setHeader("Server-Name", "BookService2");
		return true;
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.trace("====AddResHeaderInterceptor end====");
	}
	
}
