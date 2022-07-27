package jeff.authentication.serviceimpl;

import java.util.Calendar;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jeff.authentication.common.param.SystemParam;
import jeff.authentication.model.bo.CustomUserDetails;
import jeff.authentication.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;

@Service
public class JWTServiceImpl implements JWTService{
	
	@Autowired
	private SystemParam param;
	

	/**
	 * @implNote 因無法確定Jwts.builder()是否為執行緒安全，故不註冊為單例元件
	 * 
	 * */
	@Override
	public String generateRefreshToken(CustomUserDetails user){
		Calendar calendar = Calendar.getInstance();
//		現在時間再加X分鐘，為Token到期時間
        calendar.add(Calendar.MINUTE, param.RT_EXP); 
//      JWT內容的公開資訊，本質上就是Map<String, Object>
        Claims claims = Jwts.claims();
        claims.setIssuer("Jeff's authServer.");
        claims.setExpiration(calendar.getTime());
        claims.setSubject("Refresh token.");
        claims.put("id", user.getUsername());
		return Jwts.builder()
                .setHeaderParam(JwsHeader.ALGORITHM, param.RT_SIGN_ALGO_NAME)
                .setClaims(claims)
                .signWith(param.RT_SECRET_KEY)
                .compact();
	}
	
	
	@Override
	public String generateAccessToken(CustomUserDetails user) {
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, param.AT_EXP); 
        Claims claims = Jwts.claims();
        claims.setIssuer("Jeff's authServer.");
        claims.setExpiration(calendar.getTime());
        claims.setSubject("Access token.");
//      AccessToken需要放比較多的個人資訊(除了密碼)，取代Session，方便客戶端取用
        claims.put("id", user.getUsername());
        claims.put("name", user.getName());
        claims.put("role", user.getRoleStr());
		return Jwts.builder()
                .setHeaderParam(JwsHeader.ALGORITHM, param.AT_SIGN_ALGO_NAME)
                .setClaims(claims)
                .signWith(param.AT_PRIV_KEY) //私鑰加密Signature
                .compact();
	}
	

	@Override
	public Claims parseRefreshTokenToGetClaims(String token) {
		return param.RT_PARSER.parseClaimsJws(token).getBody();
	}

}
