package jeff.book.common.constants;

/**
 * 自訂義的Mongo error code，對應處理原生無法處理之情況。</p>
 * 
 * @see <a>https://docs.rs/mongodb/0.1.6/src/mongodb/.cargo/registry/src/github.com-1ecc6299db9ec823/mongodb-0.1.6/src/error.rs.html</a>
 * */
public enum CustomMongoErrorCode {

	/**
	 * 當Query單筆資料時，該資料不存在於資料庫。
	 * */
	QUERY_NOT_FOUND(4041,"The id of book does not exist in the database.") ,
	
	/**
	 * 當更新或取代某筆資料時，該筆資料不存在於資料庫。
	 * */
	UPDATE_NOT_FOUND(4042,"The operation is failed because the id of book does not exist in the database."),
	
	/**
	 * 當刪除某筆資料時，該筆資料不存在於資料庫。
	 * */
	DELETE_NOT_FOUND(4043,"The operation is failed because the id of book does not exist in the database.");
	
	private final int errCode;
	
	private final String errHint;
	
	private CustomMongoErrorCode(int code, String hint) {
		this.errCode = code;
		this.errHint = hint;
	}
	
	/**
	 * 得到自訂義的狀態碼。
	 * */
	public int errCode() {
		return this.errCode;
	}
	
	/**
	 * 得到狀態碼對應的說明。
	 * */
	public String errHint() {
		return this.errHint;
	}
	
}
