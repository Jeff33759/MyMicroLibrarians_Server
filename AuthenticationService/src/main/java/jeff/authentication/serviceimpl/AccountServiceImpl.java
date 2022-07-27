package jeff.authentication.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jeff.authentication.common.util.MyUtil;
import jeff.authentication.exception.MyAccountNotFoundException;
import jeff.authentication.model.po.Account;
import jeff.authentication.dao.AccountRepository;
import jeff.authentication.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

	private final AccountRepository accountRepository;
	
	@Autowired
	private MyUtil myUtil;
	
	@Autowired
	public AccountServiceImpl(AccountRepository accountRepositoryImpl) {
		this.accountRepository = accountRepositoryImpl;
	}

	@Override
	public Account getAccountById(String id){
		return accountRepository.findById(id)
				.orElseThrow(()-> new MyAccountNotFoundException(
						"Can not found the account By Id."));
	}
	

	@Override
	public List<Account> initDB() {
		List<Account> initAccounts = myUtil.genInitialAccountList();
		return accountRepository.saveAll(initAccounts);
	}
	

}
