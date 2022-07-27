package jeff.authentication.service;


import jeff.authentication.model.bo.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

/**
 * JWT相關操作服務器。
 * 
 * @author Jeff Huang
 * */
public interface JWTService {
	
	/**
	 * 根據已經通過認證的UserDetails物件，製作一個RefreshToken。</p>
	 * 因為解析與生成都在本服務端，所以採用對稱式加密，加密Token Signature。</p>
	 * 
	 * @param user - 
	 * 		  已經通過驗證的{@link CustomUserDetails}物件。
	 * */
	public String generateRefreshToken(CustomUserDetails user);
	
	/**
	 * 根據已經通過認證的UserDetails物件，製作一個AccessToken。</p>
	 * 由於是分布式伺服器架構，客戶端有解析的必要，所以採用非對稱式加密。</p>
	 * 用私鑰加密Token Signature，用公鑰解析，即使公鑰洩漏了，攻擊者也無法捏造JWTs來取得授權。</p>
	 * 
	 * @param user - 
	 * 		  已經通過驗證的{@link CustomUserDetails}物件。
	 * */
	public String generateAccessToken(CustomUserDetails user);
	
	/**
	 * 解析RefreshToken、驗證其是否有效，並獲得公開內容。
	 * </p>
	 * @return 若Token有效，就回傳解析後的payload，內有使用者資訊。
	 * @exception ExpiredJwtException 當RefreshToken已過期。
	 * */
	public Claims parseRefreshTokenToGetClaims(String token);
	
	/**
	 * 解析AccessToken、驗證其是否有效，並獲得公開內容。
	 * </p>
	 * @return 若Token有效，就回傳解析後的payload，內有使用者資訊。
	 * @exception ExpiredJwtException 當AccessToken已過期。
	 * */
//	public Claims parseAccessTokenToGetClaims(String token);
	
	

}
