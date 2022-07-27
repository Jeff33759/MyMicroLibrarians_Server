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
		MonitorRes.ServerInstance bookServer1 = res.new ServerInstance();
		bookServer1.setName("BookServer1");
		bookServer1.setEnabled(bookParam.IN_SERVICE);
		MonitorRes.ServerInstance bookServer2 = res.new ServerInstance();
		bookServer2.setName("BookServer2");
		bookServer2.setEnabled(book2Param.IN_SERVICE);
		
		List<MonitorRes.ServerInstance> bookServerList = 
				new ArrayList<MonitorRes.ServerInstance>();
		bookServerList.add(bookServer1);
		bookServerList.add(bookServer2);
		
		MonitorRes.MicroServiceStatus bookCluster = 
				res.new MicroServiceStatus();
		bookCluster.setInService(
				bookServerList.stream().anyMatch(
						book->book.isEnabled()));
		bookCluster.setCluster(bookServerList);
		
		res.setBookService(bookCluster);
	}
	
	/**
	 * 生成authN微服務相關的監聽數據，並將其設置入回應物件。
	 * */
	private void genauthNServiceDataAndSetItIntoRes(MonitorRes res) {
		MonitorRes.ServerInstance authNServer1 = res.new ServerInstance();
		authNServer1.setName("AuthNServer1");
		authNServer1.setEnabled(authNParam.IN_SERVICE);
		
		List<MonitorRes.ServerInstance> authNServerList = 
				new ArrayList<MonitorRes.ServerInstance>();
		authNServerList.add(authNServer1);
		
		MonitorRes.MicroServiceStatus authNCluster = res.new MicroServiceStatus();
		authNCluster.setInService(
				authNServerList.stream().anyMatch(
						authN->authN.isEnabled()));
		authNCluster.setCluster(authNServerList);
		
		res.setAuthNService(authNCluster);
	}

}
