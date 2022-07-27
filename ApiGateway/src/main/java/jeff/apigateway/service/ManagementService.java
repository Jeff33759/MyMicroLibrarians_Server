package jeff.apigateway.service;

import jeff.apigateway.controller.management.ManagementController;
import jeff.apigateway.model.dto.send.MonitorRes;

/**
 * 一些管理gateway的方法。
 * 
 * @author Jeff Huang
 * @see ManagementController
 * */
public interface ManagementService {
	
	/**
	 * 生成一個紀錄各微服務集群狀態的物件。
	 * */
	public MonitorRes genMonitorRes();
	
}
