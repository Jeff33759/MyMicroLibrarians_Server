package jeff.authentication.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import jeff.authentication.common.constants.UserRole;
import jeff.authentication.model.po.Account;

/**
 * 自做的工具包，註冊為單例Spring Bean。
 * 
 * @author Jeff Huang
 * */
@Component
public class MyUtil {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * 設定Response的格式。</p>
	 * {@code response.getWriter()}不可以在這裡flush掉，否則變成commits狀態的response，
	 * 會造成dispatcherServlet跳ERROR。
	 * </p>
	 * @see {@link LoginAuthenticationExceptionHandler}
	 * @see {@link SecurityExceptionHandler}
	 * @see {@link TokenAuthenticationExceptionHandler}
	 * */
	public void setFormatOfResponse(HttpServletResponse response,
			int sc, Object resObj) throws IOException {
		String jsonStr = this.objectMapper.writeValueAsString(resObj);
		response.setStatus(sc);
		response.setContentType("application/json");
		response.getWriter().print(jsonStr);
	}
	
	/**
	 * 得到現在的時間。
	 * */
	public Date getCurrentTime() {
		return Calendar.getInstance().getTime();
	}
	
	
	/**
	 * 創建一個存放初始帳號的List。</p>
	 * 初始帳號有三個:admin(全能)、librarian(進階)、user(一般)。
	 * */
	public List<Account> genInitialAccountList() {
		List<Account> accountList = new ArrayList<Account>();
		
		Account admin = new Account();
		admin.setId("admin");
		admin.setPassword(passwordEncoder.encode("admin"));
		admin.setName("最高管理者");
		admin.setAuth_role(UserRole.ADMIN.name() + ","
				+ UserRole.ADVANCED.name() + ","
				+ UserRole.NORMAL.name());
		admin.setEnable(true);
		admin.setStart_date(getCurrentTime());
		accountList.add(admin);
		
		Account advanced = new Account();
		advanced.setId("advanced");
		advanced.setPassword(passwordEncoder.encode("advanced"));
		advanced.setName("館藏管理者");
		advanced.setAuth_role(UserRole.ADVANCED.name() + ","
				+ UserRole.NORMAL.name());
		advanced.setEnable(true);
		advanced.setStart_date(getCurrentTime());
		accountList.add(advanced);
		
		Account normal = new Account();
		normal.setId("normal");
		normal.setPassword(passwordEncoder.encode("normal"));
		normal.setName("一般使用者");
		normal.setAuth_role(UserRole.NORMAL.name());
		normal.setEnable(true);
		normal.setStart_date(getCurrentTime());
		accountList.add(normal);
		
		return accountList;
	}

}
