package jeff.book.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jeff.book.controller.BookController;

/**
 * 針對所有DELETE Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class DeleteApiTest extends BookApiTestBase{
	
	private RequestBuilder genReqBuilderForDelete(String url, HttpHeaders headers) {
		return MockMvcRequestBuilders
				.delete(url)
				.headers(headers);
	}

	/**
	 * 測試{@link BookController #deleteBook}的成功案例。
	 * */
	@Test
	public void testSuccessfulCaseOfDeleteBook () throws Exception {
		String bookId = "test3";
		String url = "/book/" + bookId;
		RequestBuilder mockReq = 
				genReqBuilderForDelete(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isNoContent(),
			header().string("Server-Name", "BookService1"),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
	}
	
	/**
	 * 測試{@link BookController #deleteBook}的失敗案例。
	 * */
	@Test
	public void testFailedCaseOfDeleteBook () throws Exception {
//		當欲刪除的ID不存在
		String notExistBookId = "test100";
		String url = "/book/" + notExistBookId;
		RequestBuilder mockReq = 
				genReqBuilderForDelete(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isBadRequest()
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		pathVar為空
		String emptyBookId = "";
		url = "/book/" + emptyBookId;
		mockReq = genReqBuilderForDelete(url,mockHttpHeaders);
		ResultMatcher[] resRMArr2 = {
			status().isMethodNotAllowed()
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		pathVar為純空白
		String spaceBookId = " ";
		url = "/book/" + spaceBookId;
		mockReq = genReqBuilderForDelete(url,mockHttpHeaders);
		ResultMatcher[] resRMArr3 = {
			status().isBadRequest()
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
		
	}
	
}
