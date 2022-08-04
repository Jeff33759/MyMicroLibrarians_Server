package jeff.book2.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jeff.book2.common.constants.OpenApiConst;
import jeff.book2.common.param.SystemParam;
import jeff.book2.model.dto.qparam.MyBookQueryParam;
import jeff.book2.model.dto.receive.MyBookPatchReq;
import jeff.book2.model.dto.receive.MyBookReq;
import jeff.book2.model.dto.send.BookQueryRes;
import jeff.book2.model.po.MyBook;
import jeff.book2.service.BookService;
import jeff.book2.serviceimpl.BookServiceImpl;

/**
 * 存放業務服務API。
 * */
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BookController {
	
	private final BookService bookService;
	
	@Autowired
	private SystemParam param;
	

	@Autowired
	public BookController(BookServiceImpl bookServiceImpl) {
		this.bookService = bookServiceImpl;
	}
	
	
	/**
	 * 普通查詢，可以指定排序規則、目前頁數、一頁幾筆。</p>
	 * */
	@GetMapping(path = OpenApiConst.Path.BOOK_R)
	public ResponseEntity<BookQueryRes> queryAllBooks(@Valid @ModelAttribute MyBookQueryParam param){
		BookQueryRes result = bookService.getAllBooks(param);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * id查詢，查出單筆資料。</p>
	 * 因為是單筆資料搜尋，若找不到，回404，而非回空陣列。
	 * */
	@GetMapping(path = OpenApiConst.Path.BOOK_R_BYID)
	public ResponseEntity<MyBook> queryBookById(@NotBlank(message = "'id' must not be null and "
			+ "must contain at least one non-whitespace character. ") 
		@PathVariable("id") String bookId){
		MyBook result = bookService.getBookById(bookId);
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 條件查詢，指定書名模糊查詢、指定入館時間區段、排序規則、目前頁數、一頁幾筆。</p>
	 * 若找不到，一樣回傳狀態碼200，但是Book的結果集陣列會為空。
	 * */
	@GetMapping(path = OpenApiConst.Path.BOOK_R_COND)
	public ResponseEntity<BookQueryRes> queryBooksByCondition(@Valid @ModelAttribute MyBookQueryParam param){
		BookQueryRes result = bookService.getBooksByCondition(param);
		return ResponseEntity.ok(result);
	}

	/**
	 * 新增一本新書。</p>
	 * 注意回傳的location欄位，IP要是gateway而非本伺服端。
	 * */
	@PostMapping(path = OpenApiConst.Path.BOOK_C)
	public ResponseEntity<MyBook> addNewBook(@Valid @RequestBody MyBookReq reqBody){
		MyBook beAddedBook = bookService.addBook(reqBody);
//		若對location欄位的URL發Get請求，將可以得到創建後的新資料
        URI location = ServletUriComponentsBuilder
        		.fromCurrentRequest()
        		.host(param.GATEWAY_IP)
        		.port(param.GATEWAY_PORT)
                .path("/byid/{id}")
//              上面的{id}要填入啥
                .buildAndExpand(beAddedBook.getId())
                .toUri();
		return ResponseEntity.created(location).body(beAddedBook);
	}
	
	
	/**
	 * 取代某本書的資訊，如請求中有未夾帶的參數，就以預設值取代舊資訊。</p>
	 * */
	@PutMapping(path = OpenApiConst.Path.BOOK_REPL)
	public ResponseEntity<MyBook> replaceBook(@NotBlank(message = 
			"'id' must not be null and must contain at least one non-whitespace character. ") 
	@PathVariable("id") String bookId, @Valid @RequestBody MyBookReq reqBody){
		MyBook beReplacedBook = bookService.replaceBook(reqBody,bookId);
//		若對location欄位的URL發Get請求，將可以得到創建後的新資料
        URI location = ServletUriComponentsBuilder
        		.fromCurrentRequest()
        		.host(param.GATEWAY_IP)
        		.port(param.GATEWAY_PORT)
        		.replacePath("/book/byid/{id}")
//              上面的{id}要填入啥
                .buildAndExpand(beReplacedBook.getId())
                .toUri();
		return ResponseEntity.ok().location(location).body(beReplacedBook);
	}
	
	
	/**
	 * 更新某本書的資訊，如請求中有未夾帶的參數，就保留舊值，不以預設值取代。</p>
	 * @param reqBody - 部分更新時，所有欄位都是非必填，若沒填就沿用舊值，
	 * 					但客戶端還是要傳reqBody，即使只是空的JSON。
	 * */
	@PatchMapping(path = OpenApiConst.Path.BOOK_U)
	public ResponseEntity<MyBook> updateBook(@NotBlank(message = 
			"'id' must not be null and must contain at least one non-whitespace character. ") 
	@PathVariable("id") String bookId, @Valid @RequestBody MyBookPatchReq reqBody){
		MyBook beUpdatedBook = bookService.updateBook(reqBody, bookId);
//		若對location欄位的URL發Get請求，將可以得到創建後的新資料
        URI location = ServletUriComponentsBuilder
        		.fromCurrentRequest()
        		.host(param.GATEWAY_IP)
        		.port(param.GATEWAY_PORT)
        		.replacePath("/book/byid/{id}")
//              上面的{id}要填入啥
                .buildAndExpand(beUpdatedBook.getId())
                .toUri();
		return ResponseEntity.ok().location(location).body(beUpdatedBook);
	}
	
	/**
	 * 根據ID刪除某本書
	 * */
	@DeleteMapping(path = OpenApiConst.Path.BOOK_D)
	public ResponseEntity<MyBook> deleteBook(@NotBlank(message = 
			"'id' must not be null and must contain at least one non-whitespace character. ") 
	@PathVariable("id") String bookId){
		bookService.deleteBook(bookId);
		return ResponseEntity.noContent().build();
	}
	
}
