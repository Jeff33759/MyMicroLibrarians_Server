package jeff.apigateway.config;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.common.schedule.HandShakeTaskScheduler;
import jeff.apigateway.service.HeartBeatService;

/**
 * 為定時任務設置多執行緒。
 * 
 * @author Jeff Huang
 * @see HeartBeatService
 * @see HandShakeTaskScheduler
 * */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer{

	/**
	 * 利用反射機制，得出總共有幾個微服務，根據微服務數量設置執行緒池的長度。</p>
	 * 有幾個微服務就有幾個監聽心跳的定時任務，
	 * 任務執行時分別跑在不同的執行緒，以防無法預期的阻塞。
	 * */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Class<SystemParam> clazz = SystemParam.class;
//		減掉common
		int innerClassesLen = clazz.getClasses().length-1;
//		設定定時任務執行緒池的長度
        taskRegistrar.setScheduler(
        		Executors.newScheduledThreadPool(innerClassesLen));
	}

}
