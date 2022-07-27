package jeff.serialization.authz;

import java.util.Collection;
import jeff.apigateway.filter.AccessTokenAuthorizationFilter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 基於{@link User}，進一步對UserDetails客製化，新增兩個變數供取用。</p>
 * 因為要反序列化AuthZ傳來的物件(Authentication裡面包有CustomUserDetails)，
 * 所以package特別設計不同的路徑，AuthZ那邊也有同樣目錄同樣的類別，才能反序列化成功。</p>
 * 
 * @author Jeff Huang
 * @see AccessTokenAuthorizationFilter #successfulAccessTokenParsing
 * */
public class CustomUserDetails extends User{
	
	private static final long serialVersionUID = 1L;

	private final String name;

	private final String roleStr;
	
	public CustomUserDetails(String id, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			String name, String roleStr) {
		super(id, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.name = name;
		this.roleStr = roleStr;
	}

	public String getName() {
		return name;
	}

	public String getRoleStr() {
		return roleStr;
	}

}