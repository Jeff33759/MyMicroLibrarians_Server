package jeff.apigateway.exception;

/**
 * 當Server尚未註冊，卻傳送心跳訊號。
 * */
public class HasNotRegisteredException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public HasNotRegisteredException(String message) {
		super(message);
	}

}
