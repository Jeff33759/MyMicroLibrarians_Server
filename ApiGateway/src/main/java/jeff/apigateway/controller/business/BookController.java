package jeff.apigateway.controller.business;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jeff.apigateway.aop.BusinessControllerExResAspect;
import jeff.apigateway.common.constants.OpenApiConst;
import jeff.apigateway.common.util.LoadBalanceUtil;
import jeff.apigateway.service.BusinessForwardingService;
import jeff.apigateway.serviceimpl.BusinessForwardingServiceImpl;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForAddNewBook;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForAllBookApi;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForDeleteBook;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForQueryAllBooks;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForQueryBookById;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForQueryBooksByCondition;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForReplaceBook;
import jeff.apigateway.swagger.annotation.MySwaggerForBook.MySwaggerForUpdateBook;

/**
 * 和BookService Server的業務服務API。
 * 
 * @author Jeff Huang
 * @see BusinessControllerExResAspect
 * */
@MySwaggerForAllBookApi
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
public class BookController {
	
	@Autowired
	private LoadBalanceUtil lbUtil;
	
	private BusinessForwardingService bfService;
	
	@Autowired
	public BookController(BusinessForwardingServiceImpl bfServiceImpl) {
		this.bfService = bfServiceImpl;
	}


	/**
	 * 普通查詢，可以指定排序規則、目前頁數、一頁幾筆。</p>
	 * */
	@MySwaggerForQueryAllBooks
	@GetMapping(path = OpenApiConst.Path.BOOK_R)
	public ResponseEntity<?> queryAllBooks(HttpServletRequest req) throws Exception{
		String rootUrl = lbUtil.switchTheBookServerAddr()
				+ OpenApiConst.Path.BOOK_R;
		return bfService.forwardGetReq(req, rootUrl, null);
	}

	
	/**
	 * id查詢，查出單筆資料。</p>
	 * 因為是單筆資料搜尋，若找不到，回404狀態碼，而非回空陣列。</p>
	 * 
	 * 當id為純空白字元時，Book Server那邊會回傳405，這是因為restTemplate.getForEntity
	 * 會把純空白給消掉，所以匹配到"/book/byid"而非"/book/byid/  "。
	 * book Server有針對此狀況引入驗證函式庫做驗證，所以若用postman把請求直接打給Book Server的話，它會告訴
	 * 你不能為空白並回傳狀態碼400，但若由Gateway的restTemplate來發送請求，就會受到restTemplate特性的影響，變成回405。
	 * 因此種情況不想回傳給客戶端405而是400，所以把pathVar的驗證做在gateway而非BookServer。</p>
	 * 因為不想單純為了驗證pathVar就引入整個驗證函式庫，所以用if判斷，當id為純空白，就回傳給客戶端400。
	 * */
	@MySwaggerForQueryBookById
	@GetMapping(path = OpenApiConst.Path.BOOK_R_BYID)
	public ResponseEntity<?> queryBookById(HttpServletRequest req,
			 @PathVariable("id") String bookId) throws Exception {
		String rootUrl = lbUtil.switchTheBookServerAddr() 
				+ "/book/byid";
		LinkedHashMap<String,String> pathVarMap = 
				new LinkedHashMap<String,String>();
		pathVarMap.put("id", bookId);
		return bfService.forwardGetReq(req, rootUrl, pathVarMap);
	}
	
	/**
	 * 條件查詢，指定書名模糊查詢、指定入館時間區段、排序規則、目前頁數、一頁幾筆。</p>
	 * 若找不到，一樣回傳狀態碼200，但是Book的結果集陣列會為空。
	 * */
	@MySwaggerForQueryBooksByCondition
	@GetMapping(path = OpenApiConst.Path.BOOK_R_COND)
	public ResponseEntity<?> queryBooksByCondition(HttpServletRequest req) 
			throws Exception {
		String rootUrl = lbUtil.switchTheBookServerAddr()
				+ OpenApiConst.Path.BOOK_R_COND;
		return bfService.forwardGetReq(req, rootUrl, null);
	}

	/**
	 * 新增一本新書。
	 * */
	@MySwaggerForAddNewBook
	@PostMapping(path = OpenApiConst.Path.BOOK_C)
	public ResponseEntity<?> addNewBook(HttpServletRequest req) throws Exception {
		String rootUrl = lbUtil.switchTheBookServerAddr()
				+ OpenApiConst.Path.BOOK_C;
		return bfService.forwardPostReq(req, rootUrl, null);
	}
	
	/**
	 * 取代某本書的資訊，如請求中有未夾帶的參數，就以預設值取代舊資訊。</p>
	 * @throws Exception 
	 * */
	@MySwaggerForReplaceBook
	@PutMapping(path = OpenApiConst.Path.BOOK_REPL)
	public ResponseEntity<?> replaceBook(HttpServletRequest req,
			 @PathVariable("id") String bookId) throws Exception{
		String rootUrl = lbUtil.switchTheBookServerAddr()
				+ "/book";
		LinkedHashMap<String,String> pathVarMap = 
				new LinkedHashMap<String,String>();
		pathVarMap.put("id", bookId);
		return bfService.forwardPutReq(req, rootUrl, pathVarMap);
	}
	
	
	/**
	 * 更新某本書的資訊，如請求中有未夾帶的參數，就保留舊值，不以預設值取代。</p>
	 * 雖然所有欄位都是非必填，但客戶端還是要傳reqBody，即使只是空的JSON。
	 * */
	@MySwaggerForUpdateBook
	@PatchMapping(path = OpenApiConst.Path.BOOK_U)
	public ResponseEntity<?> updateBook(HttpServletRequest req,
			 @PathVariable("id") String bookId) throws Exception{
		String rootUrl = lbUtil.switchTheBookServerAddr()
				+ "/book";
		LinkedHashMap<String,String> pathVarMap = 
				new LinkedHashMap<String,String>();
		pathVarMap.put("id", bookId);
		return bfService.forwardPatchReq(req, rootUrl, pathVarMap);
	}
	
	/**
	 * 根據ID刪除某本書
	 * @throws Exception 
	 * */
	@MySwaggerForDeleteBook
	@DeleteMapping(path = OpenApiConst.Path.BOOK_D)
	public ResponseEntity<?> deleteBook(HttpServletRequest req,
			 @PathVariable("id") String bookId) throws Exception{
		String rootUrl = lbUtil.switchTheBookServerAddr()
				+ "/book";
		LinkedHashMap<String,String> pathVarMap = 
				new LinkedHashMap<String,String>();
		pathVarMap.put("id", bookId);
		return bfService.forwardDeleteReq(req, rootUrl, pathVarMap);
	}
	
}
