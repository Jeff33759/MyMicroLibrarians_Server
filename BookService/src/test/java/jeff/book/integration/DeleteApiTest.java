package jeff.book.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import jeff.book.controller.BookController;

/**
 * 針對所有DELETE Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class DeleteApiTest extends BookApiTestBase{

	/**
	 * 測試{@link BookController #deleteBook}的成功案例。
	 * */
	@Test
	public void testSuccessfulCaseOfDeleteBook () {
		String bookId = "test3";
		String url = domain + "/book/" + bookId;
		ResponseEntity<Void> res = mockReq.exchange(url, HttpMethod.DELETE, null, Void.class);
		HttpStatus statusCode = res.getStatusCode();
		String serverName = res.getHeaders().get("Server-Name").get(0);
		Assertions.assertEquals("BookService1",serverName);
		Assertions.assertEquals(HttpStatus.NO_CONTENT,statusCode);
	}
	
	/**
	 * 測試{@link BookController #deleteBook}的失敗案例。
	 * </p>
	 * 當pathVar為純空白字串的案例，沒辦法用restTemplate測，
	 * 詳情見{@link ReadApiTest #testFailedCaseOfQueryBookById}
	 * */
	@Test
	public void testFailedCaseOfDeleteBook () {
//		當欲刪除的ID不存在
		String notExistBookId = "test100";
		String url = domain + "/book/" + notExistBookId;
		HttpClientErrorException e = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url, HttpMethod.DELETE, null, Void.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.BAD_REQUEST.value(),e.getRawStatusCode());
//		pathVar為空
		String emptyBookId = "";
		String url2 = domain + "/book/" + emptyBookId;
		HttpClientErrorException e2 = Assertions.assertThrows( HttpClientErrorException.class, () -> {
			mockReq.exchange(url2, HttpMethod.DELETE, null, Void.class);
    	}, "HttpClientErrorException was expected but it didn't occurred.");
		Assertions.assertEquals(
				HttpStatus.METHOD_NOT_ALLOWED.value(),e2.getRawStatusCode());
	}
	
}
