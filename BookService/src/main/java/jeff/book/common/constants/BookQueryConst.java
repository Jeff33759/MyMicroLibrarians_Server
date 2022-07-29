package jeff.book.common.constants;

public interface BookQueryConst {

	/**
	 * 排序基準欄位一，預設以acquired_year欄位去排序。
	 * */
	public static final String DEFAULT_ORDER_BY = "acquired_year"; 

	/**
	 * 排序基準欄位二，預設以id欄位去排序。
	 * */
	public static final String DEFAULT_ORDER_BY2 = "id"; 
	
	/**
	 * 預設升冪排序。
	 * */
	public static final String DEFAULT_SORTING_RULE = "asc"; 
	
	/**
	 * 預設一頁10筆資料。
	 * */
	public static final Integer DEFAULT_PAGE_SIZE = 10; 
	
	
}
