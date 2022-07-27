package jeff.authentication.serviceimpl;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jeff.authentication.common.constants.InnerApiConst;
import jeff.authentication.common.param.SystemParam;
import jeff.authentication.exception.RestTemplateErrorResException;
import jeff.authentication.model.dto.receive.RegistrationData;
import jeff.authentication.service.HandshakeService;

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
		SecretKey rtSecretKey = deserializeRTSecretKey(
				data.getRTKeyStr(),data.getRTSignAlgoJcaName());
		PrivateKey atPrivKey = deserializeATPrivateKey(
				data.getATPrivKeyStr(),data.getATSignAlgoFamilyName());
		JwtParser rtParser = 
				genRefreshTokenParser(rtSecretKey);
		assignRegParamToMySysParam(
				data,rtSecretKey,atPrivKey,rtParser);
	}
	
	/**
	 * 反序列化RT的密鑰。
	 * 
	 * @throws RestTemplateErrorResException
	 * 		   當反序列密鑰的途中發生錯誤則拋出，代表從gateway收到的資料有錯誤，
	 * 		   導致反序列化失敗。
	 * */
	private SecretKey deserializeRTSecretKey(String rtKeyStr,String rtSignAlgo) 
			throws RestTemplateErrorResException {
		try {
			byte[] decodedKeyBytes = 
					Base64.getDecoder().decode(rtKeyStr);
//			需要演算法的JCA name
			return new SecretKeySpec(decodedKeyBytes,rtSignAlgo);
		}catch(Exception e) {
			throw new RestTemplateErrorResException(e);
		}
	}
	
	/**
	 * 創建JWTs解析器</p>
	 * 根據{@link Jwts #parserBuilder}的註解，其創建出來的{@link JwtParser}
	 * 為執行緒安全，所以註冊為單例元件。
	 * */
	private JwtParser genRefreshTokenParser(SecretKey rtSecretKey) {
		return Jwts.parserBuilder()
				.setSigningKey(rtSecretKey)
				.build();
	}
	
	/**
	 * 反序列化AT的私鑰。
	 * @throws RestTemplateErrorResException
	 * 		   當反序列私鑰的途中發生錯誤則拋出，代表從gateway收到的資料有錯誤，
	 * 		   導致反序列化失敗。
	 * */
	private PrivateKey deserializeATPrivateKey(String atKeyStr,String atSignAlgo) 
			throws RestTemplateErrorResException{
		try {
			byte[] decodedKeyBytes = 
					Base64.getDecoder().decode(atKeyStr);
//			kf需要演算法的familyName
			KeyFactory kf = KeyFactory.getInstance(atSignAlgo);
			return kf.generatePrivate(
					new PKCS8EncodedKeySpec(decodedKeyBytes));
		}catch(Exception e) {
			throw new RestTemplateErrorResException(e);
		}
	}
	

	/**
	 * 將註冊成功後得到的變數，賦值給公用系統變數，供各類別使用。
	 * */
	private void assignRegParamToMySysParam(RegistrationData data,
			SecretKey rtKey, PrivateKey atKey, JwtParser rtParser) {
		param.RT_SIGN_ALGO_NAME = data.getRTSignAlgoName();
		param.RT_EXP = data.getRTExp();
		param.RT_SECRET_KEY = rtKey;
		param.RT_PARSER = rtParser;
		
		param.AT_SIGN_ALGO_NAME = data.getATSignAlgoName();
		param.AT_EXP = data.getATExp();
		param.AT_PRIV_KEY = atKey;
		
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
