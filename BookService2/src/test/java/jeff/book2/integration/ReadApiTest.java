package jeff.book2.integration;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import jeff.book2.common.constants.OpenApiConst;
import jeff.book2.controller.BookController;
import jeff.book2.model.dto.send.BookQueryRes;
import jeff.book2.model.po.MyBook;

/**
 * 針對所有Read Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class ReadApiTest extends BookApiTestBase{
	
	/**
	 * 測試{@link BookController #queryAllBooks}的成功案例。</p>
	 * 測試分頁與排序與pageSize是否正常。
	 * 會先以AcquiredYear排序，然後相同的再以ID排序。
	 * */
	@Test
	public void testSuccessfulCaseOfQueryAllBooks () {
//		升冪排序、一頁三筆、第一頁
		String queryString = "?nowPage=1&pageSize=3";
		String url = domain + OpenApiConst.Path.BOOK_R + queryString;
		ResponseEntity<BookQueryRes> res = mockReq.getForEntity(url, BookQueryRes.class);
		String serverName = res.getHeaders().get("Server-Name").get(0);
		BookQueryRes body = res.getBody();
		List<MyBook> page1 = body.getResult();
		Assertions.assertEquals("BookService2",serverName);
		Assertions.assertEquals("test4",page1.get(0).getId());
		Assertions.assertEquals("test5",page1.get(1).getId());
		Assertions.assertEquals("test2",page1.get(2).getId());
		Assertions.assertEquals(1,body.getNowPage());
		Assertions.assertEquals(3,body.getPageSize());
		Assertions.assertEquals(5,body.getTotalElements());
		Assertions.assertEquals(2,body.getTotalPages());
//		升冪排序、一頁三筆、第二頁
		queryString = "?nowPage=2&pageSize=3";
		url = domain + OpenApiConst.Path.BOOK_R + queryString;
		ResponseEntity<BookQueryRes> res2 = mockReq.getForEntity(url, BookQueryRes.class);
		String serverName2 = res2.getHeaders().get("Server-Name").get(0);
		BookQueryRes body2 = res2.getBody();
		List<MyBook> page2 = body2.getResult();
		Assertions.assertEquals("BookService2",serverName2);
		Assertions.assertEquals("test3",page2.get(0).getId());
		Assertions.assertEquals("test1",page2.get(1).getId());
		Assertions.assertEquals(2,body2.getNowPage());
		Assertions.assertEquals(3,body2.getPageSize());
		Assertions.assertEquals(5,body2.getTotalElements());
		Assertions.assertEquals(2,body2.getTotalPages());
//		降冪排序、一頁三筆、第一頁
		queryString = "?nowPage=1&pageSize=3&sortRule=desc";
		url = domain + OpenApiConst.Path.BOOK_R + queryString;
		ResponseEntity<BookQueryRes> res3 = mockReq.getForEntity(url, BookQueryRes.class);
		String serverName3 = res3.getHeaders().get("Server-Name").get(0);
		BookQueryRes body3 = res3.getBody();
		List<MyBook> page1Desc = body3.getResult();
		Assertions.assertEquals("BookService2",serverName3);
		Assertions.assertEquals("test1",page1Desc.get(0).getId());
		Assertions.assertEquals("test3",page1Desc.get(1).getId());
		Assertions.assertEquals("test2",page1Desc.get(2).getId());
		Assertions.assertEquals(1,body3.getNowPage());
		Assertions.assertEquals(3,body3.getPageSize());
		Assertions.assertEquals(5,body3.getTotalElements());
		Assertions.assertEquals(2,body3.getTotalPages());
//		升冪排序、一頁五筆、第一頁
		queryString = "?nowPage=1&pageSize=5";
		url = domain + OpenApiConst.Path.BOOK_R + queryString;
		ResponseEntity<BookQueryRes> res4 = mockReq.getForEntity(url, BookQueryRes.class);
		String serverName4 = res4.getHeaders().get("Server-Name").get(0);
		BookQueryRes body4 = res4.getBody();
		List<MyBook> page1Size4 = body4.getResult();
		Assertions.assertEquals("BookService2",serverName4);
		Assertions.assertEquals("test4",page1Size4.get(0).getId());
		Assertions.assertEquals("test5",page1Size4.get(1).getId());
		Assertions.assertEquals("test2",page1Size4.get(2).getId());
		Assertions.assertEquals("test3",page1Size4.get(3).getId());
		Assertions.assertEquals(1,body4.getNowPage());
		Assertions.assertEquals(5,body4.getPageSize());
		Assertions.assertEquals(5,body4.getTotalElements());
		Assertions.assertEquals(1,body4.getTotalPages());
	}
	
	/**
	 * 測試{@link BookController #queryAllBooks}的失敗案例。</p>
	 * */
	@Test
	public void testFailedCaseOfQueryAllBooks () {
//		queryString沒放必要參數的nowPage
		String queryString = "";
		String url = domain + OpenApiConst.Path.BOOK_R + queryString;
		HttpClientErrorException e = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url, BookQueryRes.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e.getRawStatusCode());
//		pageSize不得大於20
		String queryString2 = "?nowPage=1&pageSize=21";
		String url2 = domain + OpenApiConst.Path.BOOK_R + queryString2;
		HttpClientErrorException e2 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url2, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e2.getRawStatusCode());
//		pageSize不得小於3
		String queryString3 = "?nowPage=1&pageSize=2";
		String url3 = domain + OpenApiConst.Path.BOOK_R + queryString3;
		
		HttpClientErrorException e3 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url3, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e3.getRawStatusCode());
//		sortRule傳入了"asc"或"desc"以外的字串
		String queryString4 = "?nowPage=1&pageSize=3&sortRule=ASC";
		String url4 = domain + OpenApiConst.Path.BOOK_R + queryString4;
		HttpClientErrorException e4 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url4, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e4.getRawStatusCode());
//		nowPage不得小於1
		String queryString5 = "?nowPage=0&pageSize=3";
		String url5 = domain + OpenApiConst.Path.BOOK_R + queryString5;
		HttpClientErrorException e5 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url5, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e5.getRawStatusCode());
	}

	
	/**
	 * 測試{@link BookController #queryBookById}的成功案例。</p>
	 * */
	@Test
	public void testSuccessfulCaseOfQueryBookById () {
		String bookId = "test3";
		String url = domain + "/book/byid/" + bookId;
		MyBook res = mockReq.getForObject(url, MyBook.class);
		Assertions.assertEquals("test3",res.getId());
	}
	
	/**
	 * 測試{@link BookController #queryBookById}的失敗案例。</p>
	 * 
	 * 當pathVar為純空白字串的案例，沒辦法用restTemplate測，
	 * 因為restTemplate會純空白字串的pathVar去掉，
	 * 變成匹配到"/book/byid"而非"/book/byid/  "，因此會變成回應405而非預期的400。
	 * 若要針對此案例做測試，目前只想到用postman或是WEB瀏覽器去測試。
	 * */
	@Test
	public void testFailedCaseOfQueryBookById () throws UnsupportedEncodingException {
//		查詢不存在的ID
		String bookId = "test100";
		String url = domain + "/book/byid/" + bookId;
		HttpClientErrorException e = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.NOT_FOUND.value(),e.getRawStatusCode());
//		pathVar為空
		bookId = "";
		String url2 = domain + "/book/byid/" + bookId;
		HttpClientErrorException e2 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url2, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.METHOD_NOT_ALLOWED.value(),e2.getRawStatusCode());
	}
	
	
	/**
	 * 測試{@link BookController #queryBooksByCondition}的成功案例。</p>
	 * 入館年份區間，應該要連頭尾都算進去，例如查詢2010~2020，那就會抓出
	 * 包含2010和2020的資料。</p>
	 * 會先以AcquiredYear排序，然後相同的再以ID排序。
	 * */
	@Test
	public void testSuccessfulCaseOfQueryBooksByCondition () {
//		升冪排序、一頁三筆、第一頁，無其他條件
		String queryString = "?nowPage=1&pageSize=3";
		String url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		BookQueryRes res = mockReq.getForObject(url, BookQueryRes.class);
		List<MyBook> result = res.getResult();
		Assertions.assertEquals("test4",result.get(0).getId());
		Assertions.assertEquals("test5",result.get(1).getId());
		Assertions.assertEquals("test2",result.get(2).getId());
		Assertions.assertEquals(1,res.getNowPage());
		Assertions.assertEquals(3,res.getPageSize());
		Assertions.assertEquals(5,res.getTotalElements());
		Assertions.assertEquals(2,res.getTotalPages());
//		升冪排序、一頁三筆、第一頁、標題關鍵字
		queryString = "?nowPage=1&pageSize=3&titleKw=日本";
		url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		BookQueryRes res2 = mockReq.getForObject(url, BookQueryRes.class);
		List<MyBook> condResult = res2.getResult();
		Assertions.assertEquals("test4",condResult.get(0).getId());
		Assertions.assertEquals("test5",condResult.get(1).getId());
		Assertions.assertEquals("test3",condResult.get(2).getId());
		Assertions.assertEquals(1,res2.getNowPage());
		Assertions.assertEquals(3,res2.getPageSize());
		Assertions.assertEquals(3,res2.getTotalElements());
		Assertions.assertEquals(1,res2.getTotalPages());
//		升冪排序、一頁三筆、第一頁、入館年份區間頭&尾
		queryString = "?nowPage=1&pageSize=3&yearFrom=2010&yearTo=2020";
		url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		BookQueryRes res3 = mockReq.getForObject(url, BookQueryRes.class);
		List<MyBook> condResult2 = res3.getResult();
		Assertions.assertEquals("test5",condResult2.get(0).getId());
		Assertions.assertEquals("test2",condResult2.get(1).getId());
		Assertions.assertEquals("test3",condResult2.get(2).getId());
		Assertions.assertEquals(1,res3.getNowPage());
		Assertions.assertEquals(3,res3.getPageSize());
		Assertions.assertEquals(3,res3.getTotalElements());
		Assertions.assertEquals(1,res3.getTotalPages());
//		升冪排序、一頁三筆、第一頁、標題關鍵字+入館年份區間頭&尾
		queryString = "?nowPage=1&pageSize=3&titleKw=日本&yearFrom=2010&yearTo=2020";
		url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		BookQueryRes res4 = mockReq.getForObject(url, BookQueryRes.class);
		List<MyBook> condResult3 = res4.getResult();
		Assertions.assertEquals("test5",condResult3.get(0).getId());
		Assertions.assertEquals("test3",condResult3.get(1).getId());
		Assertions.assertEquals(1,res4.getNowPage());
		Assertions.assertEquals(3,res4.getPageSize());
		Assertions.assertEquals(2,res4.getTotalElements());
		Assertions.assertEquals(1,res4.getTotalPages());
//		降冪排序、一頁三筆、第一頁、標題關鍵字+入館年份區間頭&尾
		queryString = "?nowPage=1&pageSize=3&sortRule=desc&titleKw=日本&yearFrom=2010&yearTo=2020";
		url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		BookQueryRes res5 = mockReq.getForObject(url, BookQueryRes.class);
		List<MyBook> condResult4 = res5.getResult();
		Assertions.assertEquals("test3",condResult4.get(0).getId());
		Assertions.assertEquals("test5",condResult4.get(1).getId());
		Assertions.assertEquals(1,res5.getNowPage());
		Assertions.assertEquals(3,res5.getPageSize());
		Assertions.assertEquals(2,res5.getTotalElements());
		Assertions.assertEquals(1,res5.getTotalPages());
//		升冪排序、一頁三筆、第一頁、入館年份區間頭
		queryString = "?nowPage=1&pageSize=3&yearFrom=2010";
		url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		BookQueryRes res6 = mockReq.getForObject(url, BookQueryRes.class);
		List<MyBook> condResult5 = res6.getResult();
		Assertions.assertEquals("test5",condResult5.get(0).getId());
		Assertions.assertEquals("test2",condResult5.get(1).getId());
		Assertions.assertEquals("test3",condResult5.get(2).getId());
		Assertions.assertEquals(1,res6.getNowPage());
		Assertions.assertEquals(3,res6.getPageSize());
		Assertions.assertEquals(4,res6.getTotalElements());
		Assertions.assertEquals(2,res6.getTotalPages());
//		升冪排序、一頁三筆、第一頁、入館年份區間尾
		queryString = "?nowPage=1&pageSize=3&yearTo=2010";
		url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		BookQueryRes res7 = mockReq.getForObject(url, BookQueryRes.class);
		List<MyBook> condResult6 = res7.getResult();
		Assertions.assertEquals("test4",condResult6.get(0).getId());
		Assertions.assertEquals("test5",condResult6.get(1).getId());
		Assertions.assertEquals(1,res7.getNowPage());
		Assertions.assertEquals(3,res7.getPageSize());
		Assertions.assertEquals(2,res7.getTotalElements());
		Assertions.assertEquals(1,res7.getTotalPages());
	}
	
	/**
	 * 測試{@link BookController #queryBooksByCondition}的失敗案例。</p>
	 * */
	@Test
	public void testFailedSuccessfulCaseOfQueryBooksByCondition () {
//		queryString沒放必要參數的nowPage
		String queryString = "?pageSize=3";
		String url = domain + OpenApiConst.Path.BOOK_R_COND + queryString;
		HttpClientErrorException e = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e.getRawStatusCode());
//		queryString的yearTo必須是數字
		String queryString2 = "?nowPage=1&pageSize=3&yearTo=a";
		String url2 = domain + OpenApiConst.Path.BOOK_R_COND + queryString2;
		HttpClientErrorException e2 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url2, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e2.getRawStatusCode());
//		queryString的yearTo不得為負數
		String queryString3 = "?nowPage=1&pageSize=3&yearTo=-1";
		String url3 = domain + OpenApiConst.Path.BOOK_R_COND + queryString3;
		HttpClientErrorException e3 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url3, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e3.getRawStatusCode());
//		queryString的yearFrom必須是數字
		String queryString4 = "?nowPage=1&pageSize=3&yearFrom=a";
		String url4 = domain + OpenApiConst.Path.BOOK_R_COND + queryString4;
		HttpClientErrorException e4 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url4, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e4.getRawStatusCode());
//		queryString的yearFrom不得為負數
		String queryString5 = "?nowPage=1&pageSize=3&yearFrom=-1";
		String url5 = domain + OpenApiConst.Path.BOOK_R_COND + queryString5;
		HttpClientErrorException e5 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url5, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e5.getRawStatusCode());
//		pageSize不得大於20
		String queryString6 = "?nowPage=1&pageSize=21";
		String url6 = domain + OpenApiConst.Path.BOOK_R + queryString6;
		HttpClientErrorException e6 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url6, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e6.getRawStatusCode());
//		pageSize不得小於3
		String queryString7 = "?nowPage=1&pageSize=2";
		String url7 = domain + OpenApiConst.Path.BOOK_R + queryString7;
		HttpClientErrorException e7 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url7, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e7.getRawStatusCode());
//		sortRule傳入了"asc"或"desc"以外的字串
		String queryString8 = "?nowPage=1&pageSize=3&sortRule=ASC";
		String url8 = domain + OpenApiConst.Path.BOOK_R + queryString8;
		HttpClientErrorException e8 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url8, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e8.getRawStatusCode());
//		nowPage不得小於1
		String queryString9 = "?nowPage=0&pageSize=3";
		String url9 = domain + OpenApiConst.Path.BOOK_R + queryString9;
		HttpClientErrorException e9 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.getForObject(url9, BookQueryRes.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e9.getRawStatusCode());
	}
	
	
	
}
