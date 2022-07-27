package jeff.authentication.exception;

import org.springframework.dao.DataAccessException;

/**
 * 自己做的DataAccessExceptionException，當ID不在DB內時拋出。</p>
 * 
 * @author Jeff Huang
 * */
public class MyAccountNotFoundException extends DataAccessException{


	private static final long serialVersionUID = 1L;

	public MyAccountNotFoundException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	
}
