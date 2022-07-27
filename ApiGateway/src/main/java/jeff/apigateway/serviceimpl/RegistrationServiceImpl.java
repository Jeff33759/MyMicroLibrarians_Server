package jeff.apigateway.serviceimpl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jeff.apigateway.common.constants.JWTConst;
import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.model.dto.send.RegistrationDataForAuthN;
import jeff.apigateway.model.dto.send.RegistrationDataForAuthZ;
import jeff.apigateway.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService{
	
	@Autowired
	private MyUtil util;

	@Autowired
	private SystemParam.Book bookParam;

	@Autowired
	private SystemParam.Book2 book2Param;

	@Autowired
	private SystemParam.AuthN authNParam;
	
	@Autowired
	private SystemParam.AuthZ authZParam;

	
	@Override
	public void registerBookServer() {
		bookParam.SIGN = true;
		bookParam.IN_SERVICE = true;
	}
	
	@Override
	public void registerBookServer2() {
		book2Param.SIGN = true;
		book2Param.IN_SERVICE = true;
	}

	/**
	 * 不對重複註冊的情況做判斷篩選，因為如果這裡明明註冊過了(authZParam賦過值)，
	 * 但微服務又送了一次註冊請求，代表微服務那邊沒接收到這邊發過去的密鑰(也許
	 * 因為超時或是資料丟失造成)，所以再跑一次註冊流程，再發一次密鑰給微服務。
	 * */
	@Override
	public RegistrationDataForAuthN registerAuthNServer() {
//		這行比較花時間，先做好確認沒問題，再做authNParam的賦值
		RegistrationDataForAuthN res = genDataForAuthN();
		authNParam.SIGN = true;
		authNParam.IN_SERVICE = true;
		util.checkIfTheStrongDependencyServiceIsInTheService();
		return res;
	}


	@Override
	public RegistrationDataForAuthZ registerAuthZServer() {
//		這行比較花時間，先做好確認沒問題，再做authZParam的賦值
		RegistrationDataForAuthZ res = genDataForAuthZ();
		authZParam.SIGN = true;
		authZParam.IN_SERVICE = true;
		util.checkIfTheStrongDependencyServiceIsInTheService();
		return res;
	}
	
	/**
	 * 將RT密鑰與AT私鑰序列化成位元組資料後，
	 * 以Base64編碼成字串，以便夾帶於JSON回傳給客戶端。
	 * */
	private RegistrationDataForAuthN genDataForAuthN() {
		String encodedRTKey = Base64.getEncoder().encodeToString(
				JWTConst.RT_SECRETKEY.getEncoded());
		RegistrationDataForAuthN res = new RegistrationDataForAuthN();
		res.setRTSignAlgoName(JWTConst.RT_ALGORITHM.name());
		res.setRTSignAlgoJcaName(JWTConst.RT_ALGORITHM.getJcaName());
		res.setRTExp(JWTConst.RT_EXP);
		res.setRTKeyStr(encodedRTKey);
		String encodedATKey = Base64.getEncoder().encodeToString(
				JWTConst.AT_KEYPAIR.getPrivate().getEncoded());
		res.setATSignAlgoName(JWTConst.AT_ALGORITHM.name());
		res.setATSignAlgoFamilyName(JWTConst.AT_ALGORITHM.getFamilyName());
		res.setATExp(JWTConst.AT_EXP);
		res.setATPrivKeyStr(encodedATKey);
		return res;
	}
	
	/**
	 * 將公鑰序列化成位元組資料後，
	 * 以Base64編碼成字串，以便夾帶於JSON回傳給客戶端。
	 * */
	private RegistrationDataForAuthZ genDataForAuthZ() {
		String encodedKey = Base64.getEncoder().encodeToString(
				JWTConst.AT_KEYPAIR.getPublic().getEncoded());
		RegistrationDataForAuthZ res = new RegistrationDataForAuthZ();
//		注意要getFamilyName
		res.setATSignAlgoFamilyName(JWTConst.AT_ALGORITHM.getFamilyName());
		res.setATPubKeyStr(encodedKey);
		return res;
	}


}
