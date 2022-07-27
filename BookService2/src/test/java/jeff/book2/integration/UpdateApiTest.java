package jeff.book2.integration;

import java.util.Calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import jeff.book2.controller.BookController;
import jeff.book2.model.dto.receive.MyBookPatchReq;
import jeff.book2.model.dto.receive.MyBookReq;
import jeff.book2.model.po.MyBook;

/**
 * 針對所有Update Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class UpdateApiTest extends BookApiTestBase{
	
	/**
	 * 測試{@link BookController #replaceBook}的成功案例。</p>
	 * 一些非必要沒有傳的參數，看是不是為預設值。
	 * */
	@Test
	public void testSuccessfulCaseOfReplaceBook () {
		String bookId = "test3";
		String url = domain + "/book/" + bookId;
		MyBookReq myBookReq = new MyBookReq();
		myBookReq.setMainTitle("測試");
		myBookReq.setType("test");
		HttpEntity<MyBookReq> reqObj = new HttpEntity<>(myBookReq);
		ResponseEntity<MyBook> res = 
				mockReq.exchange(url, HttpMethod.PUT, reqObj, MyBook.class);
		String serverName = res.getHeaders().get("Server-Name").get(0);
		MyBook replacedBook = res.getBody();
		Assertions.assertEquals("BookService2",serverName);
		Assertions.assertEquals(bookId, replacedBook.getId());
		Assertions.assertEquals("測試",replacedBook.getMainTitle());
		Assertions.assertEquals("test",replacedBook.getType());
//		以下檢查預設值
		Assertions.assertEquals(Calendar.getInstance().get(Calendar.YEAR),
				replacedBook.getAcquiredYear());
		Assertions.assertEquals("",replacedBook.getCreatedYear());
		Assertions.assertEquals("",replacedBook.getCreator());
		Assertions.assertEquals("",replacedBook.getDescription());
		Assertions.assertEquals("",replacedBook.getImageUrl());
		Assertions.assertEquals("",replacedBook.getOriginalUrl());
		Assertions.assertEquals("",replacedBook.getOwner());
		Assertions.assertEquals("",replacedBook.getOwnerCollectionsWebsite());
		Assertions.assertEquals("",replacedBook.getOwnerWebsite());
		Assertions.assertEquals(true,replacedBook.isAvailable());
	}
	
	
	/**
	 * 測試{@link BookController #replaceBook}的失敗案例。
	 * </p>
	 * 當pathVar為純空白字串的案例，沒辦法用restTemplate測，
	 * 詳情見{@link ReadApiTest #testFailedCaseOfQueryBookById}
	 * */
	@Test
	public void testFailedSuccessfulCaseOfReplaceBook () {
		String bookId = "test3";
		String url = domain + "/book/" + bookId;
//		沒有放必要參數的mainTitle
		MyBookReq myBookReq = new MyBookReq();
		myBookReq.setType("test");
		HttpEntity<MyBookReq> reqObj = new HttpEntity<>(myBookReq);
		HttpClientErrorException e = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PUT, reqObj, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e.getRawStatusCode());
//		沒有放必要參數的type
		MyBookReq myBookReq2 = new MyBookReq();
		myBookReq2.setMainTitle("測試");
		HttpEntity<MyBookReq> reqObj2 = new HttpEntity<>(myBookReq2);
		HttpClientErrorException e2 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PUT, reqObj2, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e2.getRawStatusCode());
//		createdYear若有給值，必須為純數字
		MyBookReq myBookReq3 = new MyBookReq();
		myBookReq3.setMainTitle("測試");
		myBookReq3.setType("test");
		myBookReq3.setCreatedYear("a100");
		HttpEntity<MyBookReq> reqObj3 = new HttpEntity<>(myBookReq3);
		HttpClientErrorException e3 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PUT, reqObj3, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e3.getRawStatusCode());
//		createdYear若有給值，不能為負數
		MyBookReq myBookReq4 = new MyBookReq();
		myBookReq4.setMainTitle("測試");
		myBookReq4.setType("test");
		myBookReq4.setCreatedYear("-1");
		HttpEntity<MyBookReq> reqObj4 = new HttpEntity<>(myBookReq4);
		HttpClientErrorException e4 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PUT, reqObj4, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e4.getRawStatusCode());
//		acquiredYear若有給值，不能為負數
		MyBookReq myBookReq5 = new MyBookReq();
		myBookReq5.setMainTitle("測試");
		myBookReq5.setType("test");
		myBookReq5.setAcquiredYear(-1);
		HttpEntity<MyBookReq> reqObj5 = new HttpEntity<>(myBookReq5);
		HttpClientErrorException e5 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PUT, reqObj5, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e5.getRawStatusCode());
//		修改不存在的ID
		String notExistId = "test100";
		String idNotExistUrl = domain + "/book/" + notExistId;
		MyBookReq myBookReq6 = new MyBookReq();
		myBookReq6.setMainTitle("測試");
		myBookReq6.setType("test");
		HttpEntity<MyBookReq> reqObj6 = new HttpEntity<>(myBookReq6);
		HttpClientErrorException e6 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(idNotExistUrl, HttpMethod.PUT, reqObj6, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e6.getRawStatusCode());
//		pathVar為空
		String emptyBookId = "";
		String emptyUrl = domain + "/book/" + emptyBookId;
		MyBookReq myBookReq7 = new MyBookReq();
		myBookReq7.setMainTitle("測試");
		myBookReq7.setType("test");
		HttpEntity<MyBookReq> reqObj7 = new HttpEntity<>(myBookReq7);
		HttpClientErrorException e7 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(emptyUrl, HttpMethod.PUT, reqObj7, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.METHOD_NOT_ALLOWED.value(),e7.getRawStatusCode());
//		mainTitle不能為純空白
		MyBookReq myBookReq8 = new MyBookReq();
		myBookReq8.setMainTitle(" ");
		myBookReq8.setType("test");
		HttpEntity<MyBookReq> reqObj8 = new HttpEntity<>(myBookReq8);
		HttpClientErrorException e8 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PUT, reqObj8, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e8.getRawStatusCode());
//		type不能為純空白
		MyBookReq myBookReq9 = new MyBookReq();
		myBookReq9.setMainTitle("測試");
		myBookReq9.setType(" ");
		HttpEntity<MyBookReq> reqObj9 = new HttpEntity<>(myBookReq9);
		HttpClientErrorException e9 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PUT, reqObj9, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e9.getRawStatusCode());
	}

	
	
	/**
	 * 測試{@link BookController #updateBook}的成功案例。</p>
	 * 一些非必要沒有傳的參數，看是不是為預設值。
	 * 因為是部分更新，沒有欄位為必填，全部都沒填就全部都沿用舊值。
	 * */
	@Test
	public void testSuccessfulCaseOfUpdateBook () {
		String bookId = "test3";
		String url = domain + "/book/" + bookId;
		MyBookPatchReq myBookReq = new MyBookPatchReq();
		HttpEntity<MyBookPatchReq> reqObj = new HttpEntity<>(myBookReq);
		ResponseEntity<MyBook> res = 
				mockReq.exchange(url, HttpMethod.PATCH, reqObj, MyBook.class);
		String serverName = res.getHeaders().get("Server-Name").get(0);
		MyBook updatedBook = res.getBody();
		Assertions.assertEquals("BookService2",serverName);
//		以下檢查是否保留舊值
		Assertions.assertEquals(bookId, updatedBook.getId());
		Assertions.assertEquals("日本史1",updatedBook.getMainTitle());
		Assertions.assertEquals("test",updatedBook.getType());
		Assertions.assertEquals(2020,updatedBook.getAcquiredYear());
		Assertions.assertEquals("2010",updatedBook.getCreatedYear());
		Assertions.assertEquals("Jeff",updatedBook.getCreator());
		Assertions.assertEquals("Book for test.",updatedBook.getDescription());
		Assertions.assertEquals("",updatedBook.getImageUrl());
		Assertions.assertEquals("",updatedBook.getOriginalUrl());
		Assertions.assertEquals("Jeff's room.",updatedBook.getOwner());
		Assertions.assertEquals("",updatedBook.getOwnerCollectionsWebsite());
		Assertions.assertEquals("",updatedBook.getOwnerWebsite());
		Assertions.assertEquals(false,updatedBook.isAvailable());
	}
	
	
	/**
	 * 測試{@link BookController #updateBook}的失敗案例。
	 * </p>
	 * 當pathVar為純空白字串的案例，沒辦法用restTemplate測，
	 * 詳情見{@link ReadApiTest #testFailedCaseOfQueryBookById}
	 * */
	@Test
	public void testFailedCaseOfUpdateBook () {
		String bookId = "test3";
		String url = domain + "/book/" + bookId;
//		createdYear若有給值，必須為純數字
		MyBookPatchReq myBookReq = new MyBookPatchReq();
		myBookReq.setMainTitle("測試");
		myBookReq.setType("test");
		myBookReq.setCreatedYear("a100");
		HttpEntity<MyBookPatchReq> reqObj = new HttpEntity<>(myBookReq);
		HttpClientErrorException e = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PATCH, reqObj, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e.getRawStatusCode());
//		createdYear若有給值，不能為負數
		MyBookPatchReq myBookReq2 = new MyBookPatchReq();
		myBookReq2.setMainTitle("測試");
		myBookReq2.setType("test");
		myBookReq2.setCreatedYear("-1");
		HttpEntity<MyBookPatchReq> reqObj2 = new HttpEntity<>(myBookReq2);
		HttpClientErrorException e2 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PATCH, reqObj2, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e2.getRawStatusCode());
//		acquiredYear若有給值，不能為負數
		MyBookPatchReq myBookReq3 = new MyBookPatchReq();
		myBookReq3.setMainTitle("測試");
		myBookReq3.setType("test");
		myBookReq3.setAcquiredYear(-1);
		HttpEntity<MyBookPatchReq> reqObj3 = new HttpEntity<>(myBookReq3);
		HttpClientErrorException e3 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PATCH, reqObj3, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e3.getRawStatusCode());
//		修改不存在的ID
		String notExistId = "test100";
		String idNotExistUrl = domain + "/book/" + notExistId;
		MyBookPatchReq myBookReq4 = new MyBookPatchReq();
		myBookReq4.setMainTitle("測試");
		myBookReq4.setType("test");
		HttpEntity<MyBookPatchReq> reqObj4 = new HttpEntity<>(myBookReq4);
		HttpClientErrorException e4 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(idNotExistUrl, HttpMethod.PATCH, reqObj4, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e4.getRawStatusCode());
//		pathVar為空
		String emptyBookId = "";
		String emptyUrl = domain + "/book/" + emptyBookId;
		MyBookPatchReq myBookReq5 = new MyBookPatchReq();
		myBookReq5.setMainTitle("測試");
		myBookReq5.setType("test");
		HttpEntity<MyBookPatchReq> reqObj5 = new HttpEntity<>(myBookReq5);
		HttpClientErrorException e5 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(emptyUrl, HttpMethod.PATCH, reqObj5, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.METHOD_NOT_ALLOWED.value(),e5.getRawStatusCode());
//		mainTitle若有給值，不能為純空白
		MyBookPatchReq myBookReq6 = new MyBookPatchReq();
		myBookReq6.setMainTitle(" ");
		myBookReq6.setType("test");
		HttpEntity<MyBookPatchReq> reqObj6 = new HttpEntity<>(myBookReq6);
		HttpClientErrorException e6 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PATCH, reqObj6, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e6.getRawStatusCode());
//		type若有給值，不能為純空白
		MyBookPatchReq myBookReq7 = new MyBookPatchReq();
		myBookReq7.setMainTitle("測試");
		myBookReq7.setType(" ");
		HttpEntity<MyBookPatchReq> reqObj7 = new HttpEntity<>(myBookReq7);
		HttpClientErrorException e7 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.PATCH, reqObj7, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e7.getRawStatusCode());
	}
	
}
