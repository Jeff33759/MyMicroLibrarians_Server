package jeff.apigateway.model.dto.send;


/**
 * AuthN註冊成功後，回傳給AuthN的資料。
 * */
public class RegistrationDataForAuthN {
	
	/**
	 * 較通俗的名稱，用於放在JWT的標頭
	 * */
	private String RTSignAlgoName;

	/**
	 * Java Cryptography Architecture Name。</p>
	 * 用於反序列化密鑰。
	 * */
	private String RTSignAlgoJcaName;

	private Integer RTExp;
	
	private String RTKeyStr;

	/**
	 * 較通俗的名稱，用於放在JWT的標頭
	 * */
	private String ATSignAlgoName;

	/**
	 * 用於反序列化密鑰。
	 * */
	private String ATSignAlgoFamilyName;
	
	private Integer ATExp;

	private String ATPrivKeyStr;
	

	public String getRTSignAlgoName() {
		return RTSignAlgoName;
	}

	public void setRTSignAlgoName(String rTSignAlgoName) {
		RTSignAlgoName = rTSignAlgoName;
	}


	public String getRTSignAlgoJcaName() {
		return RTSignAlgoJcaName;
	}

	public void setRTSignAlgoJcaName(String rTSignAlgoJcaName) {
		RTSignAlgoJcaName = rTSignAlgoJcaName;
	}

	public String getATSignAlgoName() {
		return ATSignAlgoName;
	}

	public void setATSignAlgoName(String aTSignAlgoName) {
		ATSignAlgoName = aTSignAlgoName;
	}

	public String getATSignAlgoFamilyName() {
		return ATSignAlgoFamilyName;
	}

	public void setATSignAlgoFamilyName(String aTSignAlgoFamilyName) {
		ATSignAlgoFamilyName = aTSignAlgoFamilyName;
	}

	public Integer getRTExp() {
		return RTExp;
	}

	public void setRTExp(Integer rTExp) {
		RTExp = rTExp;
	}

	public String getRTKeyStr() {
		return RTKeyStr;
	}

	public void setRTKeyStr(String rTKeyStr) {
		RTKeyStr = rTKeyStr;
	}

	public Integer getATExp() {
		return ATExp;
	}

	public void setATExp(Integer aTExp) {
		ATExp = aTExp;
	}

	public String getATPrivKeyStr() {
		return ATPrivKeyStr;
	}

	public void setATPrivKeyStr(String aTPrivKeyStr) {
		ATPrivKeyStr = aTPrivKeyStr;
	}
	

}
