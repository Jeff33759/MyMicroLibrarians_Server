package jeff.authentication.unit;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jeff.authentication.common.constants.UserRole;
import jeff.authentication.exception.MyAccountNotFoundException;
import jeff.authentication.model.po.Account;
import jeff.authentication.dao.AccountRepository;
import jeff.authentication.serviceimpl.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

	/**
	 * 待測元件。
	 * */
	@InjectMocks
	private AccountServiceImpl accountServiceImpl;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	
	/**
	 * 查詢存在的ID。
	 * */
	@Test
	public void testGetAccountByExistingId() {
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
		Mockito.when(accountRepository.findById(existingId))
			.thenReturn(Optional.of(mockaccount));
		
		Account expected = mockaccount;
		Account actual = 
				accountServiceImpl.getAccountById(existingId);
		
		Assertions.assertEquals(expected.getId(),actual.getId());
		Assertions.assertEquals(expected.getPassword(),actual.getPassword());
		Assertions.assertEquals(expected.getName(),actual.getName());
		Assertions.assertEquals(expected.getAuth_role(),actual.getAuth_role());
		Assertions.assertEquals(expected.getEnable(),actual.getEnable());
		Assertions.assertEquals(expected.getStart_date(),actual.getStart_date());
	}
	
	
	/**
	 * 查詢不存在的ID。
	 * */
	@Test
	public void testGetAccountByNonExistingId() {
		String nonExistingId = "anonymous";
		
		Mockito.when(accountRepository.findById(nonExistingId))
			.thenReturn(Optional.empty());
	
		MyAccountNotFoundException expected = 
				new MyAccountNotFoundException("Can not found the account.");
				
		Assertions.assertThrows(expected.getClass(), () -> {
			accountServiceImpl.getAccountById(nonExistingId);
		},"AccountNotFoundException was expected but it didn't occurred.");
	}
	
	
}
