package jeff.book2.exception;

import org.springframework.dao.DataAccessException;


/**
 * 自己做的DataAccessExceptionException，當ID不在DB內時拋出。</p>
 * 
 * @author Jeff Huang
 * */
public class MyDataNotFoundException extends DataAccessException{

	private static final long serialVersionUID = 1L;
	
	public MyDataNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public MyDataNotFoundException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
