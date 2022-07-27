package jeff.apigateway.controller.management;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jeff.apigateway.common.constants.OpenApiConst;
import jeff.apigateway.service.ManagementService;
import jeff.apigateway.serviceimpl.ManagementServiceImpl;
import jeff.apigateway.swagger.annotation.MySwaggerForGW.MySwaggerForAllManagementApi;
import jeff.apigateway.swagger.annotation.MySwaggerForGW.MySwaggerForMonitorMicroService;

/**
 * 人為控管gateway的一些API，例如查看目前微服務的狀態，
 * 或者暫時禁止那些微服務的註冊等等...</p>
 * 
 * 將來也許可以針對控管gateway寫個前端頁面，實現較人性化的控制行為，
 * 本控制器就是在存放那樣的API。
 * */
@MySwaggerForAllManagementApi
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class ManagementController {
	
	private final ManagementService mgmtService;
	
	@Autowired
	public ManagementController(ManagementServiceImpl mgmtServiceImpl) {
		this.mgmtService = mgmtServiceImpl;
	}


	/**
	 * 監聽各業務微服務集群目前的狀態。
	 * */
	@MySwaggerForMonitorMicroService
	@GetMapping(path = OpenApiConst.Path.GW_MONITOR)
	public ResponseEntity<?> monitorMicroService() 
			throws Exception {
		return ResponseEntity.ok(mgmtService.genMonitorRes());
	}
	
	
}
