package jeff.book.service;


import java.util.List;


import jeff.book.exception.MyDataNotFoundException;
import jeff.book.exception.UpdatingBookDBException;
import jeff.book.model.dto.qparam.MyBookQueryParam;
import jeff.book.model.dto.receive.MyBookPatchReq;
import jeff.book.model.dto.receive.MyBookReq;
import jeff.book.model.dto.send.BookQueryRes;
import jeff.book.model.po.MyBook;

public interface BookService {
	
	/**
	 * 一般查詢
	 * */
	public BookQueryRes getAllBooks(MyBookQueryParam param);

	/**
	 * 利用ID查單本書。</p>
	 * 
	 * @exception MyDataNotFoundException
	 * 			  若查找不到，則拋出此例外。
	 * */
	public MyBook getBookById(String id);

	/**
	 * 條件查詢書籍
	 * */
	public BookQueryRes getBooksByCondition(MyBookQueryParam param);
	
	/**
	 * 新增一本書。
	 * */
	public MyBook addBook(MyBookReq bookReq);
	
	/**
	 * 取代某一本書。</p>
	 * 若客戶端沒有傳可選填的欄位參數，那將以預設值覆蓋欄位，不保留原本欄位的舊資料。
	 *
	 * @exception MyDataNotFoundException 
	 * 			  若欲更新的bookId不存在於DB，則跳此例外。
	 * */
	public MyBook replaceBook(MyBookReq bookReq, String id);

	/**
	 * 更新一本書的部分資訊。</p>
	 * 若客戶端沒有傳可選填的欄位參數，則保留該欄位的舊資料。
	 * 
	 * @exception MyDataNotFoundException
	 * 			  若欲更新的bookId不存在於DB，則跳此例外。
	 * */
	public MyBook updateBook(MyBookPatchReq bookReq, String id);
	
	
	/**
	 * 刪除一本書。</p>
	 * 
	 * @exception MyDataNotFoundException
	 * 			  若欲更新的bookId不存在於DB，則跳此例外。
	 * */
	public void deleteBook(String id);
	
	
	/**
	 * 程式啟動之初，初始化資料庫。
	 * 
	 * @throws UpdatingBookDBException 
	 * 		   當把來自政府資料平台的資料儲存至DB時發生錯誤，則拋出。
	 * */
	public void initBookDB(List<MyBook> bookList) throws UpdatingBookDBException;
	
	/**
	 * 清空資料庫。
	 * */
	public void cleanDB();
	
}
