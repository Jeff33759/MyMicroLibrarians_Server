package jeff.authorization.model.dto.receive;

/**
 * 向Gateway註冊後，承接其所回傳的資料。
 * */
public class RegistrationData {

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
