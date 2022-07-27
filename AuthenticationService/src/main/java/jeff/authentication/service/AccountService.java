package jeff.authentication.service;

import java.util.List;

import jeff.authentication.exception.MyAccountNotFoundException;
import jeff.authentication.model.po.Account;

/**
 * 使用者相關操作服務器。
 * 
 * @author Jeff Huang
 * */
public interface AccountService {
	
	/**
	 * 利用使用者帳號，查找使用者資訊。
	 * 
	 * @param id - 使用者ID
	 * @return {@link Account} - 查找成功的使用者資訊。
	 * @exception MyAccountNotFoundException 找不到該使用者時拋出。
	 * */
	public Account getAccountById(String id);
	
	
	/**
	 * 程式啟動之初，初始化資料庫，新增三個代表不同身分的帳號。
	 * */
	public List<Account> initDB();

	
}
