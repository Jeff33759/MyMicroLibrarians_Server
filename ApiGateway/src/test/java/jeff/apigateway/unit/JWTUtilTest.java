package jeff.apigateway.unit;

import java.util.Calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.AuthorityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jeff.apigateway.common.constants.JWTConst;
import jeff.apigateway.common.constants.UserRole;
import jeff.serialization.authz.CustomUserDetails;

/**
 * JWT生成與解析的測試案例。
 * */
public class JWTUtilTest {

	/**
	 * RefreshToken解析器。
	 * */
	private JwtParser rtParser = Jwts.parserBuilder()
			.setSigningKey(JWTConst.RT_SECRETKEY)
			.build();

	
	/**
	 * AccessToken解析器，用公鑰解密。
	 * */
	private JwtParser atParser = Jwts.parserBuilder()
			.setSigningKey(JWTConst.AT_KEYPAIR.getPublic())
			.build();
	
	/**
	 * demo帳號
	 * */
	private CustomUserDetails genAuthenticatedUser() {
		return new CustomUserDetails( "normal", "normal",
				true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList(
						UserRole.NORMAL.name()),"一般使用者", UserRole.NORMAL.name());
	}
	
	/**
	 * 測試生成的RefreshToken的邏輯是否正常，以及是否可以成功被密鑰解密成正確的內容。
	 * */
	@Test
	public void testGenerateRefreshToken(){
		CustomUserDetails authenticatedUser = genAuthenticatedUser();

		String refreshToken = generateRT(false,authenticatedUser);
		
		Jws<Claims> Jwts = rtParser.parseClaimsJws(refreshToken);
		JwsHeader<?> header = Jwts.getHeader();
		Claims claims = Jwts.getBody();
		
		Assertions.assertEquals(JWTConst.RT_ALGORITHM.getValue(),
				header.get(JwsHeader.ALGORITHM));
		Assertions.assertEquals("Jeff's authServer.", claims.get("iss"));
		Assertions.assertEquals("Refresh token.", claims.get("sub"));
		Assertions.assertEquals(authenticatedUser.getUsername(), claims.get("id"));
	}
	
	
	/**
	 * 測試生成的AccessToken的邏輯是否正常，以及是否可以成功被密鑰解密成正確的內容。
	 * */
	@Test
	public void testGenerateAccessToken(){
		CustomUserDetails authenticatedUser = genAuthenticatedUser();
		String accessToken = generateAT(false,authenticatedUser); //私鑰加密
		
		Jws<Claims> Jwts = atParser.parseClaimsJws(accessToken); //公鑰解密
		JwsHeader<?> header = Jwts.getHeader();
		Claims claims = Jwts.getBody();

		Assertions.assertEquals(JWTConst.AT_ALGORITHM.getValue(),
				header.get(JwsHeader.ALGORITHM));
		Assertions.assertEquals("Jeff's authServer.", claims.get("iss"));
		Assertions.assertEquals("Access token.", claims.get("sub"));
		Assertions.assertEquals(authenticatedUser.getUsername(), claims.get("id"));
		Assertions.assertEquals(authenticatedUser.getName(), claims.get("name"));
		Assertions.assertEquals(authenticatedUser.getRoleStr(), claims.get("role"));
	}
	
	/**
	 * 測試解析期效內的RefreshToken。
	 * */
	@Test
	public void testParseEffectiveRefreshToken() {
		CustomUserDetails authenticatedUser = genAuthenticatedUser();
		String effectiveRT = generateRT(false,authenticatedUser);
		
		Claims claims = 
				rtParser.parseClaimsJws(effectiveRT).getBody();
		
		Assertions.assertEquals("Jeff's authServer.",claims.get("iss"));
		Assertions.assertEquals("Refresh token.",claims.get("sub"));
		Assertions.assertEquals(authenticatedUser.getUsername(),claims.get("id"));
	}
	
	/**
	 * 測試解析過期的RefreshToken。
	 * */
	@Test
	public void testParseExpiredRefreshToken() {
		CustomUserDetails authenticatedUser = genAuthenticatedUser();
		String expiredRT = generateRT(true,authenticatedUser);
    	
		Assertions.assertThrows(ExpiredJwtException.class, () -> {
    		rtParser.parseClaimsJws(expiredRT).getBody();
    	}, "ExpiredJwtException was expected but it didn't occurred.");
	}
	
	
	/**
	 * 測試解析期效內的AccessToken。
	 * */
	@Test
	public void testParseEffectiveAccessToken() {
		CustomUserDetails authenticatedUser = genAuthenticatedUser();
		String effectiveAT = generateAT(false,authenticatedUser);
		
		Claims claims = 
				atParser.parseClaimsJws(effectiveAT).getBody();
		
		Assertions.assertEquals("Jeff's authServer.", claims.get("iss"));
		Assertions.assertEquals("Access token.", claims.get("sub"));
		Assertions.assertEquals(authenticatedUser.getUsername(), claims.get("id"));
		Assertions.assertEquals(authenticatedUser.getName(), claims.get("name"));
		Assertions.assertEquals(authenticatedUser.getRoleStr(), claims.get("role"));
	}
	
	/**
	 * 測試解析過期的AccessToken。
	 * */
	@Test
	public void testParseExpiredAccessToken() {
		CustomUserDetails authenticatedUser = genAuthenticatedUser();
		String expiredAT = generateAT(true,authenticatedUser);
    	
		Assertions.assertThrows(ExpiredJwtException.class, () -> {
    		atParser.parseClaimsJws(expiredAT).getBody();
    	}, "ExpiredJwtException was expected but it didn't occurred.");
	}
	
	
	/**
	 * 製作過期與非過期的RefreshToken，用於測試解析方法。
	 * 
	 * @param expired - 若過期傳Ture，若沒過期傳false。
	 * */
	private String generateRT(boolean expired,CustomUserDetails user) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, expired ? -1 : 1);	
        Claims claims = Jwts.claims();
        claims.setIssuer("Jeff's authServer.");
        claims.setExpiration(calendar.getTime());
        claims.setSubject("Refresh token.");
        claims.put("id", user.getUsername());
		return Jwts.builder()
                .setHeaderParam(JwsHeader.ALGORITHM, JWTConst.RT_ALGORITHM.getValue())
                .setClaims(claims)
                .signWith(JWTConst.RT_SECRETKEY)
                .compact();
	}
	
	/**
	 * 製作過期與非過期的AccessRefreshToken，用於測試解析方法。
	 * 
	 * @param expired - 若過期傳Ture，若沒過期傳false。
	 * */
	private String generateAT(boolean expired,CustomUserDetails user) {
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expired ? -1 : 1); 
        Claims claims = Jwts.claims();
        claims.setIssuer("Jeff's authServer.");
        claims.setExpiration(calendar.getTime());
        claims.setSubject("Access token.");
        claims.put("id", user.getUsername());
        claims.put("name", user.getName());
        claims.put("role", user.getRoleStr());
		return Jwts.builder()
                .setHeaderParam(JwsHeader.ALGORITHM, JWTConst.AT_ALGORITHM.getValue())
                .setClaims(claims)
                .signWith(JWTConst.AT_KEYPAIR.getPrivate())
                .compact();
	}
	
}
