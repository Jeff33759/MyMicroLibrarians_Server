package jeff.apigateway.serviceimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.exception.HasNotRegisteredException;
import jeff.apigateway.service.HeartBeatService;

@Service
public class HeartBeatServiceImpl implements HeartBeatService{
	
	@Autowired
	private SystemParam.Common commonParam;

	@Autowired
	private SystemParam.Book bookParam;

	@Autowired
	private SystemParam.Book2 book2Param;

	@Autowired
	private SystemParam.AuthN authNParam;
	
	@Autowired
	private SystemParam.AuthZ authZParam;
	
	
	@Override
	public void bookServerHeartBeat() {
		if(bookParam.IN_SERVICE) {
			bookParam.SIGN = true;
		}else {
			throw new HasNotRegisteredException(
					"The book-server has not registered "
					+ "but sent a heartbeat signal first.");
		}
	}
	
	@Override
	public void bookServer2HeartBeat() {
		if(book2Param.IN_SERVICE) {
			book2Param.SIGN = true;
		}else {
			throw new HasNotRegisteredException(
					"The book-server2 has not registered "
							+ "but sent a heartbeat signal first.");
		}
	}

	@Override
	public void authNServerHeartBeat() {
		if(authNParam.IN_SERVICE) {
			authNParam.SIGN = true;
		}else {
			throw new HasNotRegisteredException(
					"The authN server has not registered "
					+ "but sent a heartbeat signal first.");
		}
	}

	@Override
	public void authZServerHeartBeat() {
		if(authZParam.IN_SERVICE) {
			authZParam.SIGN = true;
		}else {
			throw new HasNotRegisteredException(
					"The authZ server has not registered "
					+ "but sent a heartbeat signal first.");
		}
	}

	@Override
	public void monitorBookServerHeartBeat() {
//		是否在週期內曾經送過心跳訊號，代表還活著
		if(bookParam.SIGN) {
//			重置心跳訊號
			bookParam.SIGN = false;
		}else {
//			將狀態改為未服務
			bookParam.IN_SERVICE = false;
		}
	}
	
	@Override
	public void monitorBookServer2HeartBeat() {
//		是否在週期內曾經送過心跳訊號，代表還活著
		if(book2Param.SIGN) {
//			重置心跳訊號
			book2Param.SIGN = false;
		}else {
//			將狀態改為未服務
			book2Param.IN_SERVICE = false;
		}
	}

	@Override
	public void monitorAuthNServerHeartBeat() {
		if(authNParam.SIGN) {
			authNParam.SIGN = false;
		}else {
			authNParam.IN_SERVICE = false;
//			因為是強制依賴服務項，所以gateway連帶關閉服務
			commonParam.GW_IN_SERVICE = false;
		}		
	}

	@Override
	public void monitorAuthZServerHeartBeat() {
		if(authZParam.SIGN) {
			authZParam.SIGN = false;
		}else {
			authZParam.IN_SERVICE = false;
//			因為是強制依賴服務項，所以gateway連帶關閉服務
			commonParam.GW_IN_SERVICE = false;
		}		
	}

}
