package jeff.book2.aop;

import java.util.HashSet;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jeff.book2.exception.MyDataNotFoundException;
import jeff.book2.exception.UpdatingBookDBException;

/**
 * 針對Exception logging的AOP，將拋出例外時的logging邏輯
 * 與業務邏輯切分開來。</p>
 * 若不想印出debut級別，可以至prop檔調整。
 * 
 * @author Jeff Huang
 * */
@Component
@Aspect
@Order(0)
public class ExceptionLoggingAspect {
	
	private static final Logger log = 
			LoggerFactory.getLogger(ExceptionLoggingAspect.class);

	/**
	 * 需要debug等級(開發階段才log，正式上線不用)的例外，統一存放在此陣列。
	 * */
	private final Class<?>[] debugExTypes = new Class[]{
			MyDataNotFoundException.class
	};

	/**
	 * 需要info等級(用於系統通知)紀錄的無風險例外，統一存放在此陣列。
	 * */
	private final Class<?>[] infoExTypes = new Class[]{
			
	};

	/**
	 * 需要warn等級紀錄的有潛在風險的例外，統一存放在此陣列。
	 * */
	private final Class<?>[] warnExTypes = new Class[]{
			UpdatingBookDBException.class
	};
	
	
	/**
	 * 用來比對的HasSet，{@link #debugExTypes}內的元素。
	 * */
	private HashSet<Class<?>> debugExHashSet = 
			new HashSet<Class<?>>();

	/**
	 * 用來比對的HasSet，{@link #infoExTypes}內的元素。
	 * */
	private HashSet<Class<?>> infoExHashSet = 
			new HashSet<Class<?>>();
	
	/**
	 * 用來比對的HasSet，{@link #warnExTypes}內的元素。
	 * */
	private HashSet<Class<?>> warnExHashSet = 
			new HashSet<Class<?>>();

	
	public ExceptionLoggingAspect() {
		for(Class<?> c : debugExTypes) {
			this.debugExHashSet.add(c);
		}
		for(Class<?> c : infoExTypes) {
			this.infoExHashSet.add(c);
		}
		for(Class<?> c : warnExTypes) {
			this.warnExHashSet.add(c);
		}
	}

	/**
	 * 匹配jeff.book2.service底下子目錄的所有類別的所有0~多個參數的方法。</p>
	 * */
	@Pointcut("execution(* jeff.book2.service..*.*(..))")
	public void pointcutForService() {
	}
	
	/**
	 * 匹配jeff.book2.common.util底下子目錄的所有類別的所有0~多個參數的方法。
	 * */
	@Pointcut("execution(* jeff.book2.common.util..*.*(..))")
	public void pointcutForMyUtil() {
	}

	/**
	 * 匹配jeff.book2.common.schedule底下子目錄的所有類別的所有0~多個參數的方法。
	 * */
	@Pointcut("execution(* jeff.book2.common.schedule..*.*(..))")
	public void pointcutForSchedule() {
	}
	
	/**
	 * 因為Service與資料庫互動相關，可能會因為DAO異常或Service本身邏輯異常造成某個業務服務當掉，
	 * 因此需要log記錄下來。
	 * */
	@AfterThrowing(pointcut = "pointcutForService() || pointcutForMyUtil() || pointcutForSchedule()", 
			throwing = "throwable")
	public void exceptionLoggingAdvice(JoinPoint joinPoing,Throwable throwable) {
		String logTemplate = "<ProcessingClass>: %s <MethodName>: %s <Hint>: %s";
		String className = getProcessingClassName(joinPoing);
		String methodName = getMethodName(joinPoing);
		String logging = String.format(logTemplate,className , methodName,throwable);
		if(isDebugLevel(throwable.getClass())) {
			log.debug(logging);
		}else if(isInfoLevel(throwable.getClass())) {
			log.info(logging);
		}else if(isWarnLevel(throwable.getClass())){
			log.warn(logging);
		}else {
			log.error(logging);
		}
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
	
	/**
	 * 判斷例外是否為測試階段的例外。
	 * */
	private boolean isDebugLevel(Class<?> c) {
		return this.debugExHashSet.contains(c);
	}

	/**
	 * 判斷例外是否為無風險的例外。
	 * */
	private boolean isInfoLevel(Class<?> c) {
		return this.infoExHashSet.contains(c);
	}

	/**
	 * 判斷例外是否為有潛在風險的例外。
	 * */
	private boolean isWarnLevel(Class<?> c) {
		return this.warnExHashSet.contains(c);
	}
	
}
