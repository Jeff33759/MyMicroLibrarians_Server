package jeff.authentication.service;

import jeff.authentication.model.bo.CustomUserDetails;
import jeff.authentication.model.dto.send.LoginSuccessRes;
import jeff.authentication.model.dto.send.RefreshATSuccessRes;

/**
 * 授權服務器。
 * 
 * @author Jeff Huang
 * */
public interface AuthService {
	
	/**
	 * 製作包含AccessToken與RefreshToken的回應物件。
	 * 
	 * @return  {@link LoginSuccessRes}
	 * */
	public LoginSuccessRes generateFullJWTs(CustomUserDetails user);
	
	/**
	 * 回傳刷新後的AccessToken。
	 * 
	 * @return  {@link RefreshATSuccessRes}
	 * */
	public RefreshATSuccessRes refreshAT(CustomUserDetails user);
	

}
