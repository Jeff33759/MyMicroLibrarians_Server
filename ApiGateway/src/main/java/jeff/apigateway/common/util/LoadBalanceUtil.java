package jeff.apigateway.common.util;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.controller.business.BookController;
import jeff.apigateway.interceptor.BookInterceptor;

/**
 * 處理各微服務的負載平衡。
 * 
 * @author Jeff Huang
 * */
@Component
public class LoadBalanceUtil {
	
	@Autowired
	private SystemParam.Book bookParam;

	@Autowired
	private SystemParam.Book2 book2Param;
	
	/**
	 * 切換BookServer的訪問路徑。
	 * 使用執行緒安全的{@link ThreadLocalRandom}實現亂數切換。</p>
	 * 
	 * 1.若Book1和Book2都在服務中，就亂數切換；</p>
	 * 2.若只有Book2開，就切到Book2；</p>
	 * 3.若只有Book1開，就切到Book1。</p>
	 * 
	 * 
	 * @hidden 
	 * 第二個判斷式之所以先判斷book2Param，是因為&&會短路求值，
	 * 第一個判斷若前面是f，那後面就不會再判斷，直接跳到下一個，
	 * 所以若第一個是F，就確定bookParam.IN_SERVICE是F，那就剩
	 * book2Param.IN_SERVICE是未知數了。
	 * 
	 * PS.基本上不會出現兩個都沒開的情況，因為會執行到這個方法，
	 * 就代表進到控制器中，就代表通過{@link BookInterceptor}的檢查，
	 * 代表BookServer至少其一為運行中。
	 * 
	 * @see BookInterceptor #preHandle
	 * @see BookController
	 * */
	public String switchTheBookServerAddr() {
		if(bookParam.IN_SERVICE && book2Param.IN_SERVICE) {
		return ThreadLocalRandom.current().nextInt(0, 2) == 0 ? 
				"http://" + bookParam.IP + ":" + bookParam.PORT :
					"http://" + book2Param.IP + ":" + book2Param.PORT;
		}else if(book2Param.IN_SERVICE) {
			return "http://" + book2Param.IP + ":" + book2Param.PORT;
		}else {
			return "http://" + bookParam.IP + ":" + bookParam.PORT;
		}
	}
	
}
