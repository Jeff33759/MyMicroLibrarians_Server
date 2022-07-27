package jeff.book2.common.constants;

import jeff.book2.controller.BookController;

/**
 * 存放對外開放API的常數。
 * 
 * @see BookController
 * */
public interface OpenApiConst {
	
	/**
	 * 存放常數路徑。
	 * */
	public interface Path {
		
		/**
		 * Book-Read。
		 * */
		public static final String BOOK_R = "/book";
		
		/**
		 * Book-Read-ById。
		 * */
		public static final String BOOK_R_BYID = "/book/byid/{id}";
	
		/**
		 * Book-Read-Condition。
		 * */
		public static final String BOOK_R_COND = "/book/cond";
	
		/**
		 * Book-Create。
		 * */
		public static final String BOOK_C = "/book";
	
		/**
		 * Book-Replace。
		 * */
		public static final String BOOK_REPL = "/book/{id}";
		
		/**
		 * Book-Update。
		 * */
		public static final String BOOK_U = "/book/{id}";
		
		/**
		 * Book-Delete。
		 * */
		public static final String BOOK_D = "/book/{id}";
	}
	
}
