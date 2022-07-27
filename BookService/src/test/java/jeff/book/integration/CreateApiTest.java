package jeff.book.integration;

import java.net.URI;
import java.util.Calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import jeff.book.common.constants.OpenApiConst;
import jeff.book.controller.BookController;
import jeff.book.model.dto.receive.MyBookReq;
import jeff.book.model.po.MyBook;

/**
 * 針對所有create Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class CreateApiTest extends BookApiTestBase{

	/**
	 * 測試{@link BookController #addNewBook}的成功案例。</p>
	 * 一些非必要沒有傳的參數，看是不是為預設值。
	 * */
	@Test
	public void testSuccessfulCaseOfAddNewBook () {
		String url = domain + OpenApiConst.Path.BOOK_C;
		MyBookReq myBookReq = new MyBookReq();
		myBookReq.setMainTitle("測試");
		myBookReq.setType("test");
		ResponseEntity<MyBook> res = 
				mockReq.postForEntity(url, myBookReq, MyBook.class);
		String serverName = res.getHeaders().get("Server-Name").get(0);
		MyBook newBook = res.getBody();
		URI location = res.getHeaders().getLocation();
		Assertions.assertEquals("BookService1",serverName);
		Assertions.assertEquals("/book/byid/" + newBook.getId(), 
				location.getRawPath());
		Assertions.assertEquals("測試",newBook.getMainTitle());
		Assertions.assertEquals(myBookReq.getType(), newBook.getType());
//		以下檢查預設值
		Assertions.assertEquals(Calendar.getInstance().get(Calendar.YEAR),
				newBook.getAcquiredYear());
		Assertions.assertEquals("",newBook.getCreatedYear());
		Assertions.assertEquals("",newBook.getCreator());
		Assertions.assertEquals("",newBook.getDescription());
		Assertions.assertEquals("",newBook.getImageUrl());
		Assertions.assertEquals("",newBook.getOriginalUrl());
		Assertions.assertEquals("",newBook.getOwner());
		Assertions.assertEquals("",newBook.getOwnerCollectionsWebsite());
		Assertions.assertEquals("",newBook.getOwnerWebsite());
		Assertions.assertEquals(true,newBook.isAvailable());
	}
	
	/**
	 * 測試{@link BookController #addNewBook}的失敗案例。</p>
	 * */
	@Test
	public void testFailedSuccessfulCaseOfAddNewBook () {
		String url = domain + OpenApiConst.Path.BOOK_C;
//		沒有放必要參數的mainTitle
		MyBookReq myBookReq = new MyBookReq();
		myBookReq.setType("test");
		HttpClientErrorException e = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.postForEntity(url, myBookReq, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e.getRawStatusCode());
//		沒有放必要參數的type
		MyBookReq myBookReq2 = new MyBookReq();
		myBookReq2.setMainTitle("測試");
		HttpClientErrorException e2 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.postForEntity(url, myBookReq2, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e2.getRawStatusCode());
//		createdYear若有給值，必須為純數字
		MyBookReq myBookReq3 = new MyBookReq();
		myBookReq3.setMainTitle("測試");
		myBookReq3.setType("test");
		myBookReq3.setCreatedYear("a100");
		HttpClientErrorException e3 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.postForEntity(url, myBookReq3, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e3.getRawStatusCode());
//		createdYear若有給值，不能為負數
		MyBookReq myBookReq4 = new MyBookReq();
		myBookReq4.setMainTitle("測試");
		myBookReq4.setType("test");
		myBookReq4.setCreatedYear("-1");
		HttpClientErrorException e4 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.postForEntity(url, myBookReq4, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e4.getRawStatusCode());
//		acquiredYear若有給值，不能為負數
		MyBookReq myBookReq5 = new MyBookReq();
		myBookReq5.setMainTitle("測試");
		myBookReq5.setType("test");
		myBookReq5.setAcquiredYear(-1);
		HttpClientErrorException e5 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.postForEntity(url, myBookReq5, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e5.getRawStatusCode());
//		mainTitle不能為純空白
		MyBookReq myBookReq6 = new MyBookReq();
		myBookReq6.setMainTitle(" ");
		myBookReq6.setType("test");
		HttpClientErrorException e6 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.postForEntity(url, myBookReq6, MyBook.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e6.getRawStatusCode());
//		type不能為純空白
		MyBookReq myBookReq7 = new MyBookReq();
		myBookReq7.setMainTitle("測試");
		myBookReq7.setType(" ");
		HttpClientErrorException e7 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.postForEntity(url, myBookReq7, MyBook.class);
		}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e7.getRawStatusCode());
	}
	
}
