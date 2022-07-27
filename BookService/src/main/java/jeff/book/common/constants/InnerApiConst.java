package jeff.book.common.constants;

import jeff.book.controller.HandshakeController;
import jeff.book.serviceimpl.HandshakeServiceImpl;

/**
 * 存放不對外開放、只用於系統內部的API的常數。
 * */
public interface InnerApiConst {
	
	/**
	 * 用於接收請求。
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
			public static final String PING = "/ping/book";
		
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
			public static final String GW_REGISTER = "/gateway/reg/book";
			
			/**
			 * Gateway-HeartBeat，向Gateway傳送心跳訊號。
			 * */
			public static final String GW_HEARTBEAT = "/gateway/hb/book";
			
		}
		
	}
	
}
