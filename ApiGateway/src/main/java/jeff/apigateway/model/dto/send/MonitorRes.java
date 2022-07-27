package jeff.apigateway.model.dto.send;

import java.util.List;

import jeff.apigateway.controller.management.ManagementController;

/**
 * 顯示各微服務狀態。
 * 
 * @see ManagementController
 * */
public class MonitorRes {
	
	/**
	 * authN的微服務
	 * */
	private MicroServiceStatus authNService;
	
	/**
	 * book的微服務
	 * */
	private MicroServiceStatus bookService;
	

	public MicroServiceStatus getAuthNService() {
		return authNService;
	}

	public void setAuthNService(MicroServiceStatus authNService) {
		this.authNService = authNService;
	}

	public MicroServiceStatus getBookService() {
		return bookService;
	}

	public void setBookService(MicroServiceStatus bookService) {
		this.bookService = bookService;
	}

	/**
	 * 某個微服務的狀態。
	 * */
	public class MicroServiceStatus {
		
		/**
		 * 微服務是否正對外提供業務服務，只要集群內
		 * 其中一個server有開，就會對外提供服務。
		 * */
		private boolean inService;

		/**
		 * 微服務的集群，由數個微服務的server組成。
		 * */
		private List<ServerInstance> cluster;

		
		public boolean isInService() {
			return inService;
		}

		public void setInService(boolean inService) {
			this.inService = inService;
		}

		public List<ServerInstance> getCluster() {
			return cluster;
		}

		public void setCluster(List<ServerInstance> cluster) {
			this.cluster = cluster;
		}

		
	}
	
	/**
	 * 某個微服務的其中一個server實例的資訊。
	 * */
	public class ServerInstance {
		
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
