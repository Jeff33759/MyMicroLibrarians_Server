package jeff.authorization.serviceimpl;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jeff.authorization.common.constants.InnerApiConst;
import jeff.authorization.common.param.SystemParam;
import jeff.authorization.exception.RestTemplateErrorResException;
import jeff.authorization.model.dto.receive.RegistrationData;
import jeff.authorization.service.HandshakeService;

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
		RegistrationData data = 
				restTemplate.getForObject(url, RegistrationData.class);
		successfulRegistration(data);
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
	 * 當獲得來自gateway的註冊成功的回應，先創建jwt解析器，
	 * 然後將必要參數賦值到公用參數裡面。
	 * */
	private void successfulRegistration(RegistrationData data) 
			throws RestTemplateErrorResException{
		PublicKey atPubKey = deserializeATPublicKey(
				data.getATPubKeyStr(),data.getATSignAlgoFamilyName());
		JwtParser atParser = genAccessTokenParser(atPubKey);
		assignRegParamToMySysParam(atParser);
	}
	
	/**
	 * 反序列化公鑰</p>
	 * 
	 * @throws RestTemplateErrorResException 
	 * 		   當創建JWT解析器的過程中發生例外則拋出，代表從gateway收到的資料有錯誤，
	 * 	 	   造成創建失敗。
	 * */
	private PublicKey deserializeATPublicKey(String atkeyStr, String atSignAlgo) 
			throws RestTemplateErrorResException {
		try {
			byte[] decodedKeyBytes = 
					Base64.getDecoder().decode(atkeyStr);
			KeyFactory kf = KeyFactory.getInstance(atSignAlgo);
			return kf.generatePublic(
					new X509EncodedKeySpec(decodedKeyBytes));
		}catch(Exception e) {
			throw new RestTemplateErrorResException(e);
		}
	}
	
	/**
	 * 並創建JWTs解析器</p>
	 * 根據{@link Jwts #parserBuilder}的註解，其創建出來的{@link JwtParser}
	 * 為執行緒安全，所以註冊為單例元件。
	 * */
	private JwtParser genAccessTokenParser(PublicKey atPubKey) {
		return Jwts.parserBuilder()
				.setSigningKey(atPubKey)
				.build();
	}
	
	/**
	 * 將註冊後得到的變數賦值給公用系統變數，供各類別使用。
	 * */
	private void assignRegParamToMySysParam(JwtParser ATParser) {
		param.AT_PARSER = ATParser;
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
