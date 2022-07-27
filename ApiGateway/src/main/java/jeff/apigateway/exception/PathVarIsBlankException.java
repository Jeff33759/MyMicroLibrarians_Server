package jeff.apigateway.exception;

/**
 * 當路徑參數為空或者純空白字元時拋出。
 * */
public class PathVarIsBlankException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PathVarIsBlankException(String message) {
		super(message);
	}
	
}
