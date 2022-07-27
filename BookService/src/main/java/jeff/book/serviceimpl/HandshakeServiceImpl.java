package jeff.book.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jeff.book.common.constants.InnerApiConst;
import jeff.book.common.param.SystemParam;
import jeff.book.service.HandshakeService;


@Service
public class HandshakeServiceImpl implements HandshakeService{
	
	@Autowired
	private SystemParam param;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@Override
	public void registerWithTheGateway() {
		String url = 
				"http://" + param.GATEWAY_IP + ":" + param.GATEWAY_PORT
				+ InnerApiConst.Send.Path.GW_REGISTER;
		restTemplate.getForObject(url, String.class);
		successfulRegistration();
	}

	@Override
	public void sendHeartbeatToGateway() {
		String url = 
				"http://" + param.GATEWAY_IP + ":" + param.GATEWAY_PORT 
				+ InnerApiConst.Send.Path.GW_HEARTBEAT;
		restTemplate.getForObject(url, String.class);
		successfulHeartbeat();
	}
	
	/**
	 * 當獲得來自gateway的註冊成功的回應。
	 * */
	private void successfulRegistration() {
		assignRegParamToMySysParam();
	}
	
	/**
	 * 將註冊後得到的變數賦值給公用系統變數，供各類別使用。
	 * */
	private void assignRegParamToMySysParam() {
		param.HAS_REGISTERED = true;
	}
	
	/**
	 * 當獲得來自gateway的心跳確認成功的回應，將必要的數值賦值給系統變數。
	 * */
	private void successfulHeartbeat() {
		assignHBParamToMySysParam();
	}
	

	private void assignHBParamToMySysParam() {
//		心跳成功，重置超時額度
		param.HB_TIMEOUT_QUATA = 3;
	}
	

}
