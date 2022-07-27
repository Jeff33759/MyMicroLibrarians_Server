package jeff.book2.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切面程式，插入在方法運行前後，用以追蹤程式的運行軌跡。</p>
 * 若不想印出，可以至prop檔調整。</p>
 * filter和interceptor因為在AOP之前，所以無法用AOP插入紀錄軌跡的邏輯。
 * 
 * @author Jeff Huang
 * */
@Component
@Aspect
public class RunningTraceLoggingAspect {
	
	private static final Logger log = 
			LoggerFactory.getLogger(RunningTraceLoggingAspect.class);

	/**
	 * 匹配列出的目錄，用於追蹤程式軌跡。</p>
	 * */
	@Pointcut("execution(* jeff.book2.common.util..*.*(..))"
			+ "|| execution(* jeff.book2.common.schedule..*.*(..))"
			+ "|| execution(* jeff.book2.controller..*.*(..))"
			+ "|| execution(* jeff.book2.dao..*.*(..))"
			+ "|| execution(* jeff.book2.service..*.*(..))"
			+ "|| execution(* jeff.book2.handler..*.*(..))")
	public void pointcutForTracingMethod() {
	}
	
	
	/**
	 * 切入點於方法執行的初始。
	 * */
	@Before(value = "pointcutForTracingMethod()")
	public void methodBeginningAdvice(JoinPoint joinPoing) {
		String logTemplate = "<ProcessingClass>: %s <MethodName>: %s <Hint>: is in progress.";
		String className = getProcessingClassName(joinPoing);
		String methodName = getMethodName(joinPoing);
		String logging = String.format(logTemplate,className,methodName);
		log.trace(logging);
	}
	
	/**
	 * 切入點於方法執行完畢，無論有無例外都會執行。
	 * */
	@After(value = "pointcutForTracingMethod()")
	public void methodFinishAdvice(JoinPoint joinPoing) {
		String logTemplate = "<ProcessingClass>: %s <MethodName>: %s <Hint>: processed.";
		String className = getProcessingClassName(joinPoing);
		String methodName = getMethodName(joinPoing);
		String logging = String.format(logTemplate,className,methodName);
		log.trace(logging);
	}
	
	
	
	/**
	 * 得到執行中的方法名。
	 * */
	private String getMethodName(JoinPoint joinPoing) {
		return joinPoing.getSignature().getName();
	}

	/**
	 * 得到執行中的CLASS名
	 * */
	private String getProcessingClassName(JoinPoint joinPoing) {
		return joinPoing.getTarget().getClass().getName();
	}
	
}
