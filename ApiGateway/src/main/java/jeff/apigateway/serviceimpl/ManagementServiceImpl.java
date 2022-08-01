package jeff.apigateway.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.model.dto.send.MonitorRes;
import jeff.apigateway.service.ManagementService;

@Service
public class ManagementServiceImpl implements ManagementService{

	@Autowired
	private SystemParam.AuthN authNParam;

	@Autowired
	private SystemParam.Book bookParam;
	
	@Autowired
	private SystemParam.Book2 book2Param;
	
	@Override
	public MonitorRes genMonitorRes() {
		MonitorRes res = new MonitorRes();
		genBookServiceDataAndSetItIntoRes(res);
		genauthNServiceDataAndSetItIntoRes(res);
		return res;
	}
	
	/**
	 * 生成Book微服務相關的監聽數據，並將其設置入回應物件。
	 * */
	private void genBookServiceDataAndSetItIntoRes(MonitorRes res) {
		MonitorRes.ServiceInstance bookInstance1 = res.new ServiceInstance();
		bookInstance1.setName("BookServer");
		bookInstance1.setEnabled(bookParam.IN_SERVICE);
		MonitorRes.ServiceInstance bookInstance2 = res.new ServiceInstance();
		bookInstance2.setName("BookServer2");
		bookInstance2.setEnabled(book2Param.IN_SERVICE);
		
		List<MonitorRes.ServiceInstance> bookInstanceList = 
				new ArrayList<MonitorRes.ServiceInstance>();
		bookInstanceList.add(bookInstance1);
		bookInstanceList.add(bookInstance2);
		
		MonitorRes.MicroService bookService = 
				res.new MicroService();
		bookService.setName("Book-Service");
		bookService.setInService(
				bookInstanceList.stream().anyMatch(
						book->book.isEnabled()));
		bookService.setCluster(bookInstanceList);
		
		res.getServiceList().add(bookService);
	}
	
	/**
	 * 生成authN微服務相關的監聽數據，並將其設置入回應物件。
	 * */
	private void genauthNServiceDataAndSetItIntoRes(MonitorRes res) {
		MonitorRes.ServiceInstance authNInstance1 = res.new ServiceInstance();
		authNInstance1.setName("AuthNServer");
		authNInstance1.setEnabled(authNParam.IN_SERVICE);
		
		List<MonitorRes.ServiceInstance> authNInstanceList = 
				new ArrayList<MonitorRes.ServiceInstance>();
		authNInstanceList.add(authNInstance1);
		
		MonitorRes.MicroService authNService = res.new MicroService();
		authNService.setName("AuthN-Service");
		authNService.setInService(
				authNInstanceList.stream().anyMatch(
						authN->authN.isEnabled()));
		authNService.setCluster(authNInstanceList);
		
		res.getServiceList().add(authNService);
	}

}
