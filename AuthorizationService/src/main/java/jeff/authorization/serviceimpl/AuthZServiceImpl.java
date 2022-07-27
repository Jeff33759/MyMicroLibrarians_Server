package jeff.authorization.serviceimpl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import jeff.authorization.common.param.SystemParam;
import jeff.authorization.common.util.MyUtil;
import jeff.authorization.service.AuthZService;
import jeff.serialization.authz.CustomUserDetails;

@Service
public class AuthZServiceImpl implements AuthZService{
	
	@Autowired
	private SystemParam param;
	
	@Autowired
	private MyUtil unit;

	@Override
	public byte[] parseATAndAuthorizateAndSerialize(String token) throws IOException {
		Claims claim = parseAT(token);
		UserDetails user = genUser(claim);
		Authentication auth = authorizateUser(user);
		return unit.serializeObj(auth);
	}
	
	/**
	 * 解析AccessToken。
	 * */
	private Claims parseAT(String token) {
		return param.AT_PARSER
				.parseClaimsJws(token)
				.getBody();
	}
	
	/**
	 * 建立一個UserDetails物件。</p>
	 * 為了安全，AccessToken裡面不攜帶使用者密碼，所以這裡隨便指訂為"hide"，
	 * */
	private UserDetails genUser(Claims claim) {
		String id = (String)claim.get("id");
		String name = (String)claim.get("name");
		String roleStr = (String)claim.get("role");
		List<GrantedAuthority> role = 
				AuthorityUtils.commaSeparatedStringToAuthorityList(roleStr);
		return new CustomUserDetails(id,"hide",
				true,true,true,true,role,name,roleStr);
	}
	
	/**
	 * 建立授權物件。
	 * */
	private Authentication authorizateUser(UserDetails user) {
//		生成一個認證通過的Authentication物件
		return new UsernamePasswordAuthenticationToken(
				user, null, user.getAuthorities());
	}

}
