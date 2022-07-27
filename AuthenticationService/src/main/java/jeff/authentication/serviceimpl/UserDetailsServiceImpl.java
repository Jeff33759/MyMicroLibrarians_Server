package jeff.authentication.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jeff.authentication.exception.MyAccountNotFoundException;
import jeff.authentication.model.bo.CustomUserDetails;
import jeff.authentication.model.po.Account;
import jeff.authentication.service.AccountService;

/**
 * 實作{@link UserDetailsService}，設置給{@link AuthenticationManager}，從而實現客製化的認證機制。
 * </p>
 * @author Jeff Huang
 * */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final AccountService accountService;

	@Autowired
	public UserDetailsServiceImpl(AccountServiceImpl accountServiceImpl) {
		this.accountService = accountServiceImpl;
	}

	/**
	 * 根據使用者ID(帳號)，從資料庫中撈出使用者資訊，提供給Security機制去做帳密的驗證比對。
	 * 
	 * @return {@link CustomUserDetails} - 
	 * 		   實作{@link UserDetails}的物件，提供給Security機制去做帳密的驗證比對。
	 * @exception MyAccountNotFoundException
	 * 			  如果{@link AccountService}找不到，將會拋出此例外，之後Security機制會將其包裝為
	 * 			  {@link InternalAuthenticationServiceException}，cause即是{@link MyAccountNotFoundException}。</p>
	 * @apiNote 若回傳的{@link CustomUserDetails}中，表示使用者狀態的四個布林值其中一個被設為False，就會視為登入失敗。</p>
	 * 			詳情參考{@link AuthenticationManager}。
	 * */
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
		Account account = accountService.getAccountById(id);
		return new CustomUserDetails( account.getId(), account.getPassword(),
				account.getEnable(), true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList(account.getAuth_role()),
				account.getName(), account.getAuth_role());
	}
}
