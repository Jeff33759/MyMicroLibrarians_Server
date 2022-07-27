package jeff.authorization.common.constants;

import jeff.authorization.controller.AuthZController;
import jeff.authorization.controller.HandshakeController;
import jeff.authorization.serviceimpl.HandshakeServiceImpl;

/**
 * 存放不對外開放、只用於系統內部的API的常數。
 * */
public interface InnerApiConst {
	
	/**
	 * 用於接收請求。
	 * @see AuthZController
	 * @see HandshakeController
	 * */
	public interface Receive {
		
		/**
		 * 存放常數路徑。
		 * */
		public interface Path {

			/**
			 * 讓gateway通知的路徑。
			 * */
			public static final String PING = "/ping/authz";
			
			/**
			 * 解析AccessToken。
			 * */
			public static final String PARSING_AT = "/authz/parsing/at";
		
		}
	}
	
	/**
	 * 用於發送請求。
	 * 
	 * @see HandshakeServiceImpl
	 * */
	public interface Send {
		
		/**
		 * 存放常數路徑。
		 * */
		public interface Path {

			/**
			 * Gateway-Register，向Gateway註冊。
			 * */
			public static final String GW_REGISTER = "/gateway/reg/authz";
			
			/**
			 * Gateway-HeartBeat，向Gateway傳送心跳訊號。
			 * */
			public static final String GW_HEARTBEAT = "/gateway/hb/authz";
			
		}
		
	}
	

}
