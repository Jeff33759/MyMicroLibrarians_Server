package jeff.book.exception;

/**
 * 程式啟動之初，從政府平台撈資料更新資料庫時，若失敗所跳的例外。
 * */
public class UpdatingBookDBException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UpdatingBookDBException(String msg, Throwable cause) {
		super(msg ,cause);
	}

}
