package jeff.authentication.unit;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jeff.authentication.common.constants.UserRole;
import jeff.authentication.exception.MyAccountNotFoundException;
import jeff.authentication.model.bo.CustomUserDetails;
import jeff.authentication.model.po.Account;
import jeff.authentication.serviceimpl.AccountServiceImpl;
import jeff.authentication.serviceimpl.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

	/**
	 * 待測元件。
	 * */
	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Mock
	private AccountServiceImpl accountServiceImpl;
	
	/**
	 * 當Id存在於資料庫。
	 * */
	@Test
	public void testLoadUserByExistingId() {
		String existingId = "admin";
		Date currentTime = Calendar.getInstance().getTime();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Account mockaccount = new Account();
		mockaccount.setId("admin");
		mockaccount.setPassword(passwordEncoder.encode("admin"));
		mockaccount.setName("管理者");
		mockaccount.setAuth_role(UserRole.ADMIN.name() + ","
				+ UserRole.ADVANCED.name() + ","
				+ UserRole.NORMAL.name());
		mockaccount.setEnable(true);
		mockaccount.setStart_date(currentTime);
		
		Mockito.when(accountServiceImpl.getAccountById(existingId))
			.thenReturn(mockaccount);
		
		CustomUserDetails expected = 
				new CustomUserDetails(mockaccount.getId(), mockaccount.getPassword(),
						mockaccount.getEnable(), true, true, true,
						AuthorityUtils.commaSeparatedStringToAuthorityList(mockaccount.getAuth_role()),
						mockaccount.getName(), mockaccount.getAuth_role());
		
		CustomUserDetails actual = 
				(CustomUserDetails)userDetailsServiceImpl.loadUserByUsername(existingId);
		
		Assertions.assertEquals(expected.getUsername(),actual.getUsername());
		Assertions.assertEquals(expected.getPassword(),actual.getPassword());
		Assertions.assertEquals(expected.getName(),actual.getName());
		Assertions.assertEquals(expected.getRoleStr(),actual.getRoleStr());
		Assertions.assertEquals(expected.getAuthorities(),actual.getAuthorities());
		Assertions.assertEquals(expected.isEnabled(),actual.isEnabled());
		Assertions.assertEquals(expected.isAccountNonExpired(),actual.isAccountNonExpired());
		Assertions.assertEquals(expected.isAccountNonLocked(),actual.isAccountNonLocked());
		Assertions.assertEquals(expected.isCredentialsNonExpired(),actual.isCredentialsNonExpired());
	}
	
	/**
	 * 當Id不存在於資料庫。
	 * */
	@Test
	public void testLoadUserByNonExistingId() {
		String nonExistingId = "anonymouse";
		MyAccountNotFoundException expexted = 
				new MyAccountNotFoundException("Can not found the account.");
		
		Mockito.when(accountServiceImpl.getAccountById(nonExistingId))
				.thenThrow(expexted);
		
		Assertions.assertThrows(expexted.getClass(), () -> {
			userDetailsServiceImpl.loadUserByUsername(nonExistingId);
		},"AccountNotFoundException was expected but it didn't occurred.");
	}
	
}
