package jeff.apigateway.model.dto.send;

/**
 * AuthZ註冊成功後，回傳給AuthZ的資料。
 * */
public class RegistrationDataForAuthZ {

	/**
	 * 用於反序列化公鑰。
	 * */
	private String ATSignAlgoFamilyName;
	
	private String ATPubKeyStr;

	public String getATSignAlgoFamilyName() {
		return ATSignAlgoFamilyName;
	}

	public void setATSignAlgoFamilyName(String aTSignAlgoFamilyName) {
		ATSignAlgoFamilyName = aTSignAlgoFamilyName;
	}

	public String getATPubKeyStr() {
		return ATPubKeyStr;
	}

	public void setATPubKeyStr(String aTPubKeyStr) {
		ATPubKeyStr = aTPubKeyStr;
	}
	
}
