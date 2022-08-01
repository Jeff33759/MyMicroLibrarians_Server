package jeff.apigateway.model.dto.send;

import java.util.ArrayList;
import java.util.List;

import jeff.apigateway.controller.management.ManagementController;

/**
 * 顯示各微服務狀態。
 * 
 * @see ManagementController
 * */
public class MonitorRes {
	
	/**
	 * 對外公開的微服務的列表。
	 * */
	private List<MicroService> serviceList = 
			new ArrayList<MicroService>();

	public List<MicroService> getServiceList() {
		return serviceList;
	}

	
	/**
	 * 某個微服務的狀態。
	 * */
	public class MicroService {
		
		/**
		 * 微服務的名字。
		 * */
		private String name;

		/**
		 * 微服務是否正對外提供業務服務，只要集群內
		 * 其中一個實例有開，就會對外提供服務。
		 * */
		private boolean inService;

		/**
		 * 微服務的集群，由數個伺服端組成。
		 * */
		private List<ServiceInstance> cluster;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isInService() {
			return inService;
		}

		public void setInService(boolean inService) {
			this.inService = inService;
		}

		public List<ServiceInstance> getCluster() {
			return cluster;
		}

		public void setCluster(List<ServiceInstance> cluster) {
			this.cluster = cluster;
		}
		
	}
	
	/**
	 * 某個微服務集群內的其中一個實例的資訊。
	 * */
	public class ServiceInstance {
		
		/**
		 * 伺服端名字。
		 * */
		private String name;
		
		/**
		 * 目前是否啟用中。
		 * */
		private boolean enabled;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public boolean isEnabled() {
			return enabled;
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
	}
	
}
